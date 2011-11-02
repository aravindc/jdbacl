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

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.databene.commons.NameUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.TimeUtil;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.DatabaseTestUtil;
import org.databene.jdbacl.JDBCConnectData;
import org.databene.jdbacl.model.DBSequence;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parent class for testing concrete {@link DatabaseDialect} implementations.<br/><br/>
 * Created: 10.11.2009 16:59:14
 * @since 0.6.0
 * @author Volker Bergmann
 */
public abstract class DatabaseDialectTest<E extends DatabaseDialect> {
	
	protected Logger logger;

	protected E dialect;
	
	protected final static Date DATETIME_19710203131415 = TimeUtil.date(1971, 1, 3, 13, 14, 15, 0);
	protected final static Time TIME_131415 = TimeUtil.time(13, 14, 15, 0);
	protected final static Timestamp TIMESTAMP_19710203131415123456789 = TimeUtil.timestamp(1971, 1, 3, 13, 14, 15, 123456789);
	
	public DatabaseDialectTest(E dialect) {
	    this.dialect = dialect;
	    this.logger = LoggerFactory.getLogger(getClass());
    }

	@Test
	public void testSequenceSetupConsistency() {
		boolean supported = dialect.isSequenceSupported();
		if (supported)
			assertSequenceSupported();
		else
			assertSequenceNotSupported();
	}

	private void assertSequenceSupported() {
	    String sequence = "DUMMY_SEQ";
	    dialect.renderFetchSequenceValue(sequence);
	    dialect.renderDropSequence(sequence);
    }
	
	private void assertSequenceNotSupported() {
	    String sequence = "DUMMY_SEQ";
	    try {
	    	dialect.renderFetchSequenceValue(sequence);
	    	fail("UnsupportedOperationException expected");
	    } catch (UnsupportedOperationException e) {
	    	// this is expected
	    }
	    try {
	    	dialect.renderDropSequence(sequence);
	    	fail("UnsupportedOperationException expected");
	    } catch (UnsupportedOperationException e) {
	    	// this is expected
	    }
    }
	
	protected void testSequencesOnline(String databaseId) throws Exception {
		JDBCConnectData data = DatabaseTestUtil.getConnectData(databaseId);
		if (data == null) {
			System.out.println("Skipping test: testSequencesOnline(" + databaseId + ")");
			return;
		}
		String sequenceName = getClass().getSimpleName();
		Connection connection = DBUtil.connect(data, false);
		try {
			dialect.createSequence(sequenceName, 23, connection);
			DBSequence[] sequences = dialect.querySequences(connection);
			String[] sequenceNames = NameUtil.getNames(sequences);
			assertTrue(StringUtil.containsIgnoreCase(sequenceName, sequenceNames));
			assertEquals(23L, DBUtil.queryLong(dialect.renderFetchSequenceValue(sequenceName), connection));
		} finally {
			DBUtil.executeUpdate(dialect.renderDropSequence(sequenceName), connection);
			DBUtil.close(connection);
		}
	}
	
	protected DBSequence createConfiguredSequence() {
		DBSequence seq = new DBSequence("my_seq", null);
		seq.setStart(new BigInteger("10"));
		seq.setIncrement(new BigInteger("2"));
		seq.setMaxValue(new BigInteger("999"));
		seq.setMinValue(new BigInteger("5"));
		seq.setCycle(true);
		seq.setCache(3L);
		seq.setOrder(true);
		return seq;
	}

}
