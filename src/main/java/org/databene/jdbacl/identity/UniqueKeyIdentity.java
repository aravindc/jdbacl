/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.jdbacl.identity;

import java.sql.Connection;

import org.databene.commons.ArrayBuilder;
import org.databene.commons.ArrayFormat;
import org.databene.commons.ArrayUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.converter.ThreadSafeConverter;
import org.databene.commons.iterator.ConvertingIterator;
import org.databene.commons.iterator.TableRowIterator;
import org.databene.jdbacl.model.Database;

/**
 * {@link IdentityModel} implementation based on a unique-key-constraint.<br/><br/>
 * Created: 06.12.2010 09:10:05
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class UniqueKeyIdentity extends IdentityModel {
	
	private String[] columnNames;

	public UniqueKeyIdentity(String tableName, String... columnNames) {
		super(tableName);
		setColumns(columnNames);
	}

	public void setColumns(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	@Override
	public TableRowIterator createNkPkIterator(
			Connection connection, String dbId, KeyMapper mapper, Database database) {
		if (ArrayUtil.isEmpty(columnNames))
			throw new ConfigurationError("No unique key columns defined");
		StringBuilder builder = new StringBuilder("select ");
		builder.append(columnNames[0]);
		for (int i = 1; i < columnNames.length; i++)
			builder.append(", ").append(columnNames[i]);
		String[] pkColumnNames = database.getTable(tableName).getPKColumnNames();
		for (String columnName : pkColumnNames)
			builder.append(", ").append(columnName);
			
		builder.append(" from ").append(tableName);
		String query = builder.toString();
		TableRowIterator rawIterator = query(query, connection);
		ColumnToNkConverter converter = new ColumnToNkConverter(dbId, mapper);
		return new UniqueKeyNkPkIterator(rawIterator, converter, pkColumnNames);
	}

	@Override
	public String getDescription() {
		return "Identity definition by unique key: " + ArrayFormat.format(columnNames);
	}

	public class UniqueKeyNkPkIterator extends ConvertingIterator<Object[], Object[]> implements TableRowIterator {
		
		String[] pkColumnNames;

		public UniqueKeyNkPkIterator(TableRowIterator rawIterator, ColumnToNkConverter converter, String[] pkColumnNames) {
			super(rawIterator, converter);
			this.pkColumnNames = columnNames;
		}

		public String[] getColumnLabels() {
			String[] labels = new String[1 + pkColumnNames.length];
			labels[0] = "NK";
			for (int i = 1; i < labels.length; i++)
				labels[i] = pkColumnNames[i - 1];
			return labels;
		}
		
	}
	
	public class ColumnToNkConverter extends ThreadSafeConverter<Object[], Object[]> {
		
		String dbId;
		KeyMapper mapper;
		
		public ColumnToNkConverter(String dbId, KeyMapper mapper) {
			super(Object[].class, Object[].class);
			this.dbId = dbId;
			this.mapper = mapper;
		}

		public Object[] convert(Object[] raw) {
			NKBuilder nkBuilder = new NKBuilder();
			for (int i = 0; i < columnNames.length; i++) {
				Object value = raw[i];
				nkBuilder.addComponent(value);
			}
			ArrayBuilder<Object> arrayBuilder = new ArrayBuilder<Object>(Object.class);
			arrayBuilder.add(nkBuilder.toString());
			for (int i = columnNames.length; i < raw.length; i++)
				arrayBuilder.add(raw[i]);
			return arrayBuilder.toArray();
		}

	}

}
