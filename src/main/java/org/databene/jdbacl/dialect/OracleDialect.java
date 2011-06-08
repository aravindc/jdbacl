/*
 * (c) Copyright 2008-2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
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

package org.databene.jdbacl.dialect;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import org.databene.commons.ArrayBuilder;
import org.databene.commons.StringUtil;
import org.databene.commons.converter.TimestampFormatter;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.model.DBCheckConstraint;
import org.databene.jdbacl.model.DBSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements generic database concepts for Oracle.<br/><br/>
 * Created: 26.01.2008 07:05:28
 * @since 0.4.0
 * @author Volker Bergmann
 */
public class OracleDialect extends DatabaseDialect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OracleDialect.class);
    
	private static final String DATE_PATTERN = "'to_date('''yyyy-MM-dd HH:mm:ss''', ''yyyy-mm-dd HH24:mi:ss'')'";
	private static final String TIME_PATTERN = "'to_date('''HH:mm:ss''', ''HH24:mi:ss'')'";
    private static final String TIMESTAMP_MESSAGE = "to_timestamp(''{0}'', ''yyyy-mm-dd HH24:mi:ss.FF'')";
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";
    private static final Pattern SIMPLE_NOT_NULL_CHECK = Pattern.compile("\"[A-Z0-9_]+\" IS NOT NULL");

	public OracleDialect() {
	    super("oracle", true, true, DATE_PATTERN, TIME_PATTERN);
    }

	@Override
	public boolean isDefaultCatalog(String catalog, String user) {
	    return (catalog == null);
	}
	
	@Override
	public boolean isDefaultSchema(String schema, String user) {
	    return user.toLowerCase().equals(schema.toLowerCase());
	}
	
	@Override
    public String renderFetchSequenceValue(String sequenceName) {
        return "select " + sequenceName + ".nextval from dual";
    }
	
	@Override
    public String formatTimestamp(Timestamp value) {
		String renderedTimestamp = new TimestampFormatter(TIMESTAMP_PATTERN).format(value);
		return MessageFormat.format(TIMESTAMP_MESSAGE, renderedTimestamp);
    }

	@Override
	public DBSequence[] querySequences(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select sequence_name, min_value, max_value, increment_by, " +
				"cycle_flag, order_flag, cache_size, last_number from user_sequences");
		ArrayBuilder<DBSequence> builder = new ArrayBuilder<DBSequence>(DBSequence.class);
		while (resultSet.next()) {
			DBSequence sequence = new DBSequence(resultSet.getString(1), null);
			sequence.setMinValue(new BigInteger(resultSet.getString(2)));
			sequence.setMaxValue(new BigInteger(resultSet.getString(3)));
			sequence.setIncrement(new BigInteger(resultSet.getString(4)));
			sequence.setCycle("Y".equals(resultSet.getString(5)));
			sequence.setOrder("Y".equals(resultSet.getString(6)));
			sequence.setCache(resultSet.getLong(7));
			sequence.setLastNumber(resultSet.getLong(8));
			builder.add(sequence);
		}
		return builder.toArray();
	}
	
	public DBCheckConstraint[] queryCheckConstraints(Connection connection, String schemaName) throws SQLException {
		Statement statement = connection.createStatement();
		statement.setFetchSize(300);
		String query = "select owner, constraint_name, table_name, search_condition " +
				"from user_constraints where constraint_type = 'C'";
		if (schemaName != null)
			query += " and owner = '" + schemaName.toUpperCase() + "'";
		ResultSet resultSet = statement.executeQuery(query);
		ArrayBuilder<DBCheckConstraint> builder = new ArrayBuilder<DBCheckConstraint>(DBCheckConstraint.class);
		//int count = 0;
		while (resultSet.next()) {
			String ownerName = resultSet.getString("owner");
			if (schemaName == null || StringUtil.equalsIgnoreCase(schemaName, ownerName)) {
				String constraintName = resultSet.getString("constraint_name");
				String tableName = resultSet.getString("table_name");
				String condition = resultSet.getString("search_condition");
				//System.out.println(++count + " " + ownerName + "."+ tableName + ": " + condition);
				if (!SIMPLE_NOT_NULL_CHECK.matcher(condition).matches()) {
					try {
						DBCheckConstraint constraint = new DBCheckConstraint(constraintName, tableName, condition);
						builder.add(constraint);
					} catch (Exception e) {
						LOGGER.error("Error parsing check constraint ", e);
					}
				}
			}
		}
		return builder.toArray();
	}
	
	public static void main(String[] args) throws Exception {
		DBCheckConstraint[] cc = new OracleDialect().queryCheckConstraints(DBUtil.connect(args[0]), null);
		System.out.println("Check Constraints:");
		for (DBCheckConstraint c : cc)
			System.out.println(c);
		System.out.println(cc.length + " check constraints found");
	}
	
}