/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ArrayFormat;
import org.databene.commons.ArrayUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.iterator.TableRowIterator;
import org.databene.jdbacl.model.DBTable;

/**
 * {@link IdentityModel} implementation for tables which have a natural key as primary key.<br/><br/>
 * Created: 12.12.2010 12:23:14
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class NaturalPkIdentity extends IdentityModel {
	
	public NaturalPkIdentity(DBTable table) {
		super(table);
	}

	@Override
	public TableRowIterator createNkPkIterator(
			Connection connection, String dbId, KeyMapper mapper) {
		String[] pkColumns = table.getPKColumnNames();
		if (ArrayUtil.isEmpty(pkColumns))
			throw new ConfigurationError("Table '" + table.getName() + "' has no primary key");
		StringBuilder builder = new StringBuilder("select ");
		builder.append(pkColumns[0]);
		for (int i = 1; i < pkColumns.length; i++)
			builder.append(" || '|' || ").append(pkColumns[i]);
		for (String columnName : pkColumns)
			builder.append(", ").append(columnName);
		builder.append(" from ").append(table.getName());
		String query = builder.toString();
		return table.query(query, connection);
	}

	@Override
	public String getDescription() {
		return "Identity definition by primary: " + ArrayFormat.format(table.getPKColumnNames());
	}

}
