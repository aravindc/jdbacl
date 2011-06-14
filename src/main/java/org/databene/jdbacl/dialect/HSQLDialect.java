/*
 * (c) Copyright 2008-2010 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ArrayBuilder;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.model.DBSequence;

/**
 * Implements generic database concepts for HSQL<br/><br/>
 * Created: 26.01.2008 07:04:45
 * @since 0.4.0
 * @author Volker Bergmann
 */
public class HSQLDialect extends DatabaseDialect {
    
	private static final String DATE_PATTERN = "''yyyy-MM-dd''";
	private static final String TIME_PATTERN = "''HH:mm:ss''";

    public HSQLDialect() {
	    super("hsql", false, true, DATE_PATTERN, TIME_PATTERN);
    }

	@Override
    public boolean isDefaultCatalog(String catalog, String user) {
	    return (catalog == null);
    }

	@Override
    public boolean isDefaultSchema(String schema, String user) {
	    return "PUBLIC".equals(schema);
    }

	@Override
    public DBSequence[] querySequences(Connection connection) throws SQLException {
        String query = "select SEQUENCE_CATALOG, SEQUENCE_SCHEMA, SEQUENCE_NAME, CURRENT_VALUE, INCREMENT, CACHE from information_schema.sequences";
        ArrayBuilder<DBSequence> builder = new ArrayBuilder<DBSequence>(DBSequence.class);
        ResultSet resultSet = DBUtil.executeQuery(query, connection);
        while (resultSet.next()) {
        	String name = resultSet.getString("SEQUENCE_NAME");
        	DBSequence sequence = new DBSequence(name, null);
        	sequence.setStart(new BigInteger(resultSet.getString("START_WITH")));
        	sequence.setIncrement(new BigInteger(resultSet.getString("INCREMENT")));
        	sequence.setMinValue(new BigInteger(resultSet.getString("MINIMUM_VALUE")));
        	sequence.setMaxValue(new BigInteger(resultSet.getString("MAXIMUM_VALUE")));
        	sequence.setCycle(resultSet.getBoolean("CYCLE_OPTION"));
        	builder.add(sequence);
        }
		return builder.toArray();
	}

	@Override
    public String renderFetchSequenceValue(String sequenceName) {
        return "call next value for " + sequenceName;
    }

	@Override
	public void setSequenceValue(String sequenceName, long value, Connection connection) throws SQLException {
	    DBUtil.executeUpdate(setSequenceValue(sequenceName, value), connection);
	}

	public String setSequenceValue(String sequenceName, long value) {
	    return "alter sequence " + sequenceName + " restart with " + value;
    }
	
	@Override
	public String renderDropSequence(String name) {
		return "drop sequence " + name;
	}

}
