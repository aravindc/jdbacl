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

package org.databene.jdbacl.model.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;

import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.Database;
import org.junit.Test;

/**
 * Tests the {@link LazyJDBCDBImporter}.<br/><br/>
 * Created: 31.01.2012 12:32:56
 * @since 0.8.0
 * @author Volker Bergmann
 */
public class LazyJDBCDBImporterTest extends AbstractJDBCDBImporterTest {

	@Test
	public void testImportDatabase_HSQL() throws Exception {
		Connection connection = setupDatabase();
		LazyJDBCDBImporter importer = new LazyJDBCDBImporter(connection, "sa", "public");
		Database db = importer.importDatabase();
		assertTrue(db instanceof LazyDatabase);
		LazyDatabase ldb = (LazyDatabase) db;
		checkImports(false, false, false, ldb);
		DBSchema schema = checkSchema(db);
		DBTable table = checkTables(schema);
		checkIndexes(table);
		checkImports(false, false, false, ldb);
		checkSequences(schema);
		checkImports(true, false, false, ldb);
		checkTriggers(schema);
		checkImports(true, true, false, ldb);
		checkPackages(schema);
		checkImports(true, true, true, ldb);
	}

	private void checkSequences(DBSchema schema) {
		List<DBSequence> sequences = schema.getSequences(true);
		assertEquals(1, sequences.size());
		assertEquals("SEQ1", sequences.get(0).getName());
	}

	private void checkTriggers(DBSchema schema) {
		assertEquals(0, schema.getTriggers().size());
	}

	private void checkPackages(DBSchema schema) {
		assertEquals(0, schema.getPackages().size());
	}

	public void checkImports(boolean sequences, boolean triggers, boolean packages, LazyDatabase ldb) {
		assertEquals(sequences, ldb.isSequencesImported());
		assertEquals(triggers,  ldb.isTriggersImported());
		assertEquals(packages,  ldb.isPackagesImported());
	}

}
