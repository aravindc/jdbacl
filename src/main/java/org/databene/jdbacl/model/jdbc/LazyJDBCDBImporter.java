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

import java.sql.Connection;
import java.sql.SQLException;

import org.databene.commons.ConnectFailedException;
import org.databene.commons.ImportFailedException;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBCheckConstraint;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBDataType;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.Database;
import org.databene.jdbacl.model.DefaultDBColumn;
import org.databene.jdbacl.model.DefaultDBSchema;
import org.databene.jdbacl.model.DefaultDBTable;
import org.databene.jdbacl.model.DefaultDatabase;
import org.databene.jdbacl.model.TableType;

/**
 * Performs lazy loading of database meta data.<br/><br/>
 * Created: 29.01.2012 21:30:29
 * @since 0.8.0
 * @author Volker Bergmann
 */
public class LazyJDBCDBImporter extends AbstractJDBCDBImporter {

	public LazyJDBCDBImporter(String environment) {
		super(environment);
	}

	public LazyJDBCDBImporter(String url, String driver, String user, String password, String catalog, String schema) {
		super(url, driver, user, password, catalog, schema);
	}

	public LazyJDBCDBImporter(Connection connection, String user, String schemaName) {
		super(connection, user, schemaName);
	}
	
	public LazyJDBCDBImporter(DefaultDatabase defaultDB) {
		super(defaultDB.getEnvironment());
		try {
			this.environment = defaultDB.getEnvironment();
			this.tableInclusionPattern = defaultDB.getTableInclusionPattern();
			this.tableExclusionPattern = defaultDB.getTableExclusionPattern();
			init();
		} catch (Exception e) {
			throw new RuntimeException("Initialization of " + this + " failed", e);
		}
	}

	@Override
	public Database importDatabase() throws ConnectFailedException, ImportFailedException {
		Database superDB = super.importDatabase();
		return new LazyDatabase(superDB, this);
	}
	
	protected void importDBObjects() throws SQLException, ConnectFailedException {
		importCatalogs();
		importSchemas();
		importAllTableNames();
	}
	
	@Override
	protected void schemaFound(String schemaName, DBCatalog catalog) {
        new LazySchema(new DefaultDBSchema(schemaName, catalog));
	}

	protected void tableFound(String tableName, TableType tableType, String doc, DBSchema schema) {
		new LazyTable(this, tableName, tableType, doc, schema);
    }
	
	@Override
	protected void columnFound(String columnName, DBDataType dataType,
			Integer columnSize, Integer fractionDigits, boolean nullable,
			String defaultValue, String comment, DBTable table) {
		LazyTable lazyTable = (LazyTable) table;
        DefaultDBColumn column = new DefaultDBColumn(columnName, lazyTable.realTable, dataType, columnSize, fractionDigits);
        column.setDoc(comment);
        column.setNullable(nullable);
        column.setTable(lazyTable);
	}

	@Override
	protected void pkFound(String pkName, String[] columnNames, DBTable table) {
		LazyTable lazyTable = (LazyTable) table;
		DBPrimaryKeyConstraint constraint = new DBPrimaryKeyConstraint(
				lazyTable.realTable, pkName, dialect.isDeterministicPKName(pkName), columnNames);
		for (String columnName : columnNames) {
			DBColumn column = lazyTable.getColumn(columnName);
		    column.addUkConstraint(constraint);
		}
		constraint.setTable(lazyTable);
	}

	@Override
	protected void fkFound(DBForeignKeyConstraint fk, DBTable table) {
		LazyTable lazyTable = (LazyTable) table;
		lazyTable.realTable.addForeignKey(fk);
		fk.setTable(lazyTable);
	}

	@Override
	protected void referrerFound(String fktable_name, DBTable table) {
		DBTable referrer = database.getTable(fktable_name);
		LazyTable lazyTable = (LazyTable) table;
		lazyTable.realTable.addReferrer(referrer);
	}
    
	@Override
	protected void checkFound(DBCheckConstraint check, DBTable table) {
		LazyTable lazyTable = (LazyTable) table;
		table.addCheckConstraint(check);
		check.setTable(lazyTable);
	}
	
	// methods for triggering metadata lookup by LazyTable -------------------------------------------------------------
	
    public void importColumnsForTable(DefaultDBTable table) {
		importColumns(table.getCatalog(), table.getSchema().getName(), table.getName(), tableNameFilter, errorHandler);
    }

	protected void sequenceFound(DBSequence sequence, DBSchema schema) {
		((LazySchema) schema).getRealSchema().addSequence(sequence);
	}
	
}
