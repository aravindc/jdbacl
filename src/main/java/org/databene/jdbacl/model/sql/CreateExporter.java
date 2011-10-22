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

package org.databene.jdbacl.model.sql;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import org.databene.commons.IOUtil;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.DatabaseDialectManager;
import org.databene.jdbacl.NameSpec;
import org.databene.jdbacl.SQLUtil;
import org.databene.jdbacl.model.DBMetaDataExporter;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.Database;

/**
 * Exports database meta data to a DDL file with CREATE TABLE commands.<br/><br/>
 * Created: 11.08.2010 16:23:59
 * @since 0.6.10
 * @author Volker Bergmann
 */
public class CreateExporter implements DBMetaDataExporter {

	File file;
	
	public CreateExporter(File file) {
		this.file = file;
	}

    public void export(Database database) throws IOException {
	    PrintWriter out = null;
	    try {
	    	out = new PrintWriter(new FileWriter(file));
	    	exportSequences(database, out);
	    	exportTables(database, out);
	    	exportForeignKeys(database, out);
	    } finally {
	    	IOUtil.close(out);
	    }
    }

	private void exportSequences(Database database, PrintWriter out) {
		List<DBSequence> sequences = database.getSequences(true);
		DatabaseDialect dialect = DatabaseDialectManager.getDialectForProduct(
			database.getDatabaseProductName(), database.getDatabaseProductVersion());
		for (DBSequence sequence : sequences) {
			out.print(dialect.renderCreateSequence(sequence));
			out.println(';');
			out.println();
		}
	}

	private void exportTables(Database database, PrintWriter out) {
		List<DBTable> tables = DBUtil.dependencyOrderedTables(database);
		for (DBTable table : tables) {
			SQLUtil.renderCreateTable(table, false, NameSpec.IF_REPRODUCIBLE, out);
			out.println(';');
			out.println();
		}
    }

	private void exportForeignKeys(Database database, PrintWriter out) {
		for (DBTable table : database.getTables())
			exportForeignKeys(table, out);
    }

	private void exportForeignKeys(DBTable table, PrintWriter out) {
		Set<DBForeignKeyConstraint> fks = table.getForeignKeyConstraints();
		for (DBForeignKeyConstraint fk : fks) {
			SQLUtil.renderAddForeignKey(fk, NameSpec.IF_REPRODUCIBLE, out);
			out.println(';');
			out.println();
		}
    }

}
