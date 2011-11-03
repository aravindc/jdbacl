/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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

import static org.junit.Assert.*;

import java.sql.Connection;

import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.model.DBSequence;
import org.junit.Test;

/**
 * Tests the PostgreSQLDialect.<br/><br/>
 * Created: 10.11.2009 18:18:51
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class PostgreSQLDialectTest extends DatabaseDialectTest<PostgreSQLDialect> {
	
	public PostgreSQLDialectTest() {
	    super(new PostgreSQLDialect());
    }

	@Test
	public void testFormatDate() {
		assertEquals("date '1971-02-03'", dialect.formatValue(DATETIME_19710203131415));
	}
	
	@Test
	public void testFormatTime() {
		assertEquals("time '13:14:15'", dialect.formatValue(TIME_131415));
	}
	
	@Test
	public void testFormatTimestamp() {
		assertEquals("timestamp '1971-02-03 13:14:15.123456789'", 
				dialect.formatValue(TIMESTAMP_19710203131415123456789));
	}
	
	@Test
	public void testIsDeterministicPKName() {
		assertTrue(dialect.isDeterministicPKName("USER_PK"));
	}
	
	@Test
	public void testIsDeterministicUKName() {
		assertTrue(dialect.isDeterministicUKName("USER_NAME_UK"));
	}
	
	@Test
	public void testIsDeterministicFKName() {
		assertTrue(dialect.isDeterministicFKName("USER_ROLE_FK"));
	}
	
	@Test
	public void testIsDeterministicIndexName() {
		assertTrue(dialect.isDeterministicIndexName("USER_NAME_IDX"));
	}
	
	@Test
	public void testRegex() {
		assertTrue(dialect.supportsRegex());
		assertEquals("code ~ '[A-Z]{5}'", dialect.regexQuery("code", false, "[A-Z]{5}"));
		assertEquals("NOT code ~ '[A-Z]{5}'", dialect.regexQuery("code", true, "[A-Z]{5}"));
	}
	
	@Test
	public void testRenderCreateSequence() {
		assertEquals("CREATE SEQUENCE my_seq", dialect.renderCreateSequence(new DBSequence("my_seq", null)));
		assertEquals("CREATE SEQUENCE my_seq START WITH 10 INCREMENT BY 2 MAXVALUE 999 MINVALUE 5 CYCLE CACHE 3", 
				dialect.renderCreateSequence(createConfiguredSequence()));
	}
	
	@Test // requires a PostgreSQL installation configured as environment named 'postgres'
	public void testSetNextSequenceValue() throws Exception {
		if (!DBUtil.existsEnvironment("postgres")) {
			logger.warn("Skipping test " + getClass() + ".testSetNextSequenceValue() since there is no 'postgres' " +
					"environment defined");
			return;
		}
		Connection connection = DBUtil.connect("postgres", false);
		String sequenceName = getClass().getSimpleName();
		try {
			DBUtil.executeUpdate("create sequence " + sequenceName, connection);
			dialect.setNextSequenceValue(sequenceName, 123, connection);
			String seqValQuery = dialect.renderFetchSequenceValue(sequenceName);
			assertEquals(123L, DBUtil.queryScalar(seqValQuery, connection));
		} finally {
			DBUtil.executeUpdate("drop sequence " + sequenceName, connection);
		}
	}
	
	@Test
	public void testRenderFetchSequenceValue() {
		assertEquals("select nextval('SEQ')", dialect.renderFetchSequenceValue("SEQ"));
	}
	
	@Test
	public void testDropSequence() {
		assertEquals("drop sequence SEQ", dialect.renderDropSequence("SEQ"));
	}
	
	@Test
	public void testRenderCase() {
		assertEquals("CASE WHEN condition1 THEN result1 WHEN condition2 THEN result2 ELSE result4 END AS col", 
				dialect.renderCase("col", "result4", "condition1", "result1", "condition2", "result2"));
	}
	
}
