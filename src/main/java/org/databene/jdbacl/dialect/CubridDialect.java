/*
 * (c) Copyright 2012 by Volker Bergmann. All rights reserved.
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

package org.databene.jdbacl.dialect;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.databene.commons.ArrayBuilder;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTrigger;
import org.databene.jdbacl.sql.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DatabaseDialect} implementation for the CUBRID database.<br/><br/>
 * Created: 13.04.2012 06:53:40
 * @since 0.7.7
 * @author Volker Bergmann
 */
public class CubridDialect extends DatabaseDialect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CubridDialect.class);

	public CubridDialect() {
		super("cubrid", true, true, "'yyyy-MM-dd'", "'HH-mm-ss'");
	}

	@Override
	public boolean isDefaultCatalog(String catalog, String user) {
		return true;
	}

	@Override
	public boolean isDefaultSchema(String schema, String user) {
		return true;
	}
	
	
	
	// sequence support ------------------------------------------------------------------------------------------------
	
	@Override
	public DBSequence[] querySequences(Connection connection)throws SQLException {
		String query = "select name, owner, current_val, increment_val, max_val, min_val, cyclic, " +
				"class_name, att_name, cached_num from db_serial";
		ResultSet resultSet = DBUtil.executeQuery(query, connection);
		ArrayBuilder<DBSequence> builder = new ArrayBuilder<DBSequence>(DBSequence.class);
		while (resultSet.next()) {
			DBSequence sequence = new DBSequence(resultSet.getString(1), null);
			sequence.setLastNumber(new BigInteger(resultSet.getString(3)));
			sequence.setIncrement(new BigInteger(resultSet.getString(4)));
			sequence.setMaxValue(new BigInteger(resultSet.getString(5)));
			sequence.setMinValue(new BigInteger(resultSet.getString(6)));
			sequence.setCycle(resultSet.getInt(7) != 0);
			sequence.setCache(resultSet.getLong(10));
		}
		return builder.toArray();
	}
	
	@Override
	public void createSequence(String name, long initialValue, Connection connection) throws SQLException {
		DBUtil.executeQuery("create serial " + name + " start with " + initialValue, connection);
	}
	
	public String renderCreateSequence(DBSequence sequence) {
		/*
		CREATE SERIAL serial_name
		[ START WITH initial ]
		[ INCREMENT BY interval]
		[ MINVALUE min | NOMINVALUE ]
		[ MAXVALUE max | NOMAXVALUE ]
		[ CACHE integer | NOCACHE ]
		[ CYCLE | NOCYCLE ]
	 */
		StringBuilder builder = new StringBuilder("CREATE SERIAL ");
		builder.append(renderSequenceNameAndType(sequence));
		BigInteger start = sequence.getStart();
		if (start != null && isNotOne(start))
			builder.append(" START WITH ").append(start);
		BigInteger increment = sequence.getIncrement();
		if (increment != null && isNotOne(increment))
			builder.append(" INCREMENT BY ").append(increment);
		if (isSequenceBoundarySupported()) {
			BigInteger maxValue = sequence.getMaxValue();
			if (maxValue != null)
				builder.append(" MAXVALUE ").append(maxValue);
			BigInteger minValue = sequence.getMinValue();
			if (minValue != null)
				builder.append(" MINVALUE ").append(minValue);
		}
		// apply cache settings
    	Long cache = sequence.getCache();
    	if (cache != null)
    		builder.append(" CACHE ").append(cache);
    	// apply cycle settings
		Boolean cycle = sequence.isCycle(); 
		if (cycle != null)
			builder.append(cycle ? " CYCLE" : " " + sequenceNoCycle());
		return builder.toString();
	}


	
	// querying triggers -----------------------------------------------------------------------------------------------
	
	@Override
	public List<DBTrigger> queryTriggers(DBSchema schema, Connection connection) throws SQLException {
		String query = "SELECT owner, name, status, priority, event, " +
			"target_class, target_attribute, target_class_attribute, condition_type, condition, condition_time, " +
			"action_type, action_definition, action_time FROM db_trigger";
		ResultSet resultSet = DBUtil.executeQuery(query, connection);
		List<DBTrigger> triggers = new ArrayList<DBTrigger>();
		try {
			while (resultSet.next()) {
				DBTrigger trigger = new DBTrigger(resultSet.getString("name"), null);
				trigger.setOwner(schema);
				schema.receiveTrigger(trigger); // use receiveTrigger(), because the DBTrigger ctor would cause a recursion in trigger import
				trigger.setStatus(resultSet.getString("status"));
				// TODO priority is ignored
				trigger.setTriggeringEvent(resultSet.getString("event"));
				// TODO target_class is ignored
				// TODO target_attribute is ignored
				// TODO target_class_attribute is ignored
				// TODO condition_type is ignored
				trigger.setWhenClause(resultSet.getString("condition"));
				// TODO condition_time is ignored
				trigger.setActionType(resultSet.getString("action_type"));
				trigger.setTriggerBody(resultSet.getString("action_definition"));
				// TODO action_time is ignored
				triggers.add(trigger);
				LOGGER.debug("Imported trigger: {}", trigger.getName());
			}
		} finally {
			DBUtil.closeResultSetAndStatement(resultSet);
		}
		return triggers;
	}
	
	// recognition of generated names ----------------------------------------------------------------------------------
	
	@Override
	public boolean isDeterministicPKName(String pkName) {
		return true;
	}

	@Override
	public boolean isDeterministicUKName(String ukName) {
		return true;
	}

	@Override
	public boolean isDeterministicFKName(String fkName) {
		return true;
	}

	@Override
	public boolean isDeterministicIndexName(String indexName) {
		return true;
	}
	
	
	
	// other features --------------------------------------------------------------------------------------------------

	public String trim(String expression) {
		return "trim(" + expression + ")";
	}

	@Override
	public void restrictRownums(int offset, int rowCount, Query query) {
		if (offset == 0)
			query.addOption("limit " + rowCount);
		else
			query.addOption("limit " + offset + ", " + rowCount);
	}

}
