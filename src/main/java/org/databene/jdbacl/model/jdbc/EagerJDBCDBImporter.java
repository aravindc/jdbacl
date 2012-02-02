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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.databene.commons.ConnectFailedException;
import org.databene.contiperf.StopWatch;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBCheckConstraint;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBDataType;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DefaultDBColumn;
import org.databene.jdbacl.model.DefaultDBSchema;
import org.databene.jdbacl.model.DefaultDBTable;
import org.databene.jdbacl.model.TableType;

/**
 * Performs eager loading of database meta data.<br/><br/>
 * Created: 29.01.2012 23:12:33
 * @since 0.8.0
 * @author Volker Bergmann
 */
public class EagerJDBCDBImporter extends AbstractJDBCDBImporter {

	public EagerJDBCDBImporter(String environment) {
		super(environment);
	}

	public EagerJDBCDBImporter(String url, String driver, String user, String password, String catalog, String schema) {
		super(url, driver, user, password, catalog, schema);
	}

	public EagerJDBCDBImporter(Connection connection, String user, String schemaName) {
		super(connection, user, schemaName);
	}

	protected void importDBObjects() throws SQLException, ConnectFailedException {
		importCatalogs();
		importSchemas();
		importAllTableNames();
		importDetailsForAllTables();
		if (dialect.isSequenceSupported())
			importSequences();
		importTriggers();
		importPackages();
	}

	private void importDetailsForAllTables() {
		importColumnsForAllTables();
		importIndexes(false);
		importAllPrimaryKeys();
		importImportedKeys();
		importAllChecks();
	}

	protected void schemaFound(String schemaName, DBCatalog catalog) {
        new DefaultDBSchema(schemaName, catalog);
	}

	protected void tableFound(String tableName, TableType tableType, String doc, DBSchema schema) {
		new DefaultDBTable(tableName, tableType, doc, schema);
    }
	
    private final void importColumnsForAllTables() {
		if (this.catalogName != null)
			importColumns(database.getCatalog(this.catalogName), this.schemaName, null, tableNameFilter, errorHandler);
		else
			for (DBCatalog catalog : database.getCatalogs()) {
				for (DBSchema schema : catalog.getSchemas())
					importColumns(catalog, schema.getName(), null, tableNameFilter, errorHandler);
			}
    }
	
	@Override
	protected void columnFound(String columnName, DBDataType dataType,
			Integer columnSize, Integer fractionDigits, boolean nullable,
			String defaultValue, String comment, DBTable table) {
        DefaultDBColumn column = new DefaultDBColumn(columnName, table, dataType, columnSize, fractionDigits);
        column.setDoc(comment);
        column.setNullable(nullable);
	}
	
    private final void importIndexes(boolean uniquesOnly) {
        for (DBCatalog catalog : database.getCatalogs())
			for (DBSchema schema : catalog.getSchemas())
			    importIndexesOfSchema(uniquesOnly, schema);
    }

	public void importIndexesOfSchema(boolean uniquesOnly, DBSchema schema) {
		logger.debug("Importing indexes for schema '" + schema.getName() + "'");
		try {
		    importIndexesOfSchemaAtOnce(schema, uniquesOnly);
		} catch (SQLException e) {
			if (e.getMessage().contains("ORA-00903")) {
				logger.error("Error in bulk index import", e);
				// Oracle may fail if a table name is a reserved word
				logger.info("Falling back to (slow) table-wise import to avoid Oracle error.");
				importIndexesOfSchemaTablewise(schema, uniquesOnly);
			} else {
				// possibly we try to query a catalog to which we do not have access rights
				errorHandler.handleError("Error parsing index data of schema " + schema.getName(), e);
			}
		}
	}

	public final void importIndexesOfSchemaAtOnce(DBSchema schema, boolean uniquesOnly) throws SQLException {
        StopWatch watch = new StopWatch("importIndexesOfSchemaAtOnce");
		ResultSet indexSet = null;
		try {
			indexSet = metaData.getIndexInfo(schema.getCatalog().getName(), schema.getName(), null, uniquesOnly, true);
			parseIndexSet(indexSet, schema, null);
	    } finally {
        	DBUtil.close(indexSet);
		}
	    watch.stop();
	}

	private final void importIndexesOfSchemaTablewise(DBSchema schema, boolean uniquesOnly) {
        StopWatch watch = new StopWatch("importIndexesOfSchemaTablewise");
        for (DBTable table : schema.getTables()) {
        	if (tableNameFilter.accept(table.getName()))
        		importIndexesOfTable(table, uniquesOnly);
        }
	    watch.stop();
    }
	
	
	
	// primary keys ----------------------------------------------------------------------------------------------------

	private void importAllPrimaryKeys() {
        StopWatch watch = new StopWatch("importAllPrimaryKeys");
        int count = 0;
        DBSchema schema = database.getCatalog(catalogName).getSchema(schemaName);
        if (schema != null)
            for (DBTable table : schema.getTables()) {
                if (!tableSupported(table.getName()))
                	continue;
                importPrimaryKeyOfTable(table);
                count++;
            }
        if (count > 0)
            return;
        DBCatalog catalog = database.getCatalog(catalogName);
        if (catalog != null)
            for (DBTable table : catalog.getTables()) {
                if (!tableSupported(table.getName()))
                	continue;
                importPrimaryKeyOfTable(table);
            }
        watch.stop();
    }

	protected void pkFound(String pkName, String[] columnNames, DBTable table) {
		DBPrimaryKeyConstraint constraint = new DBPrimaryKeyConstraint(table, pkName, dialect.isDeterministicPKName(pkName), columnNames);
		for (String columnName : columnNames) {
			DBColumn column = table.getColumn(columnName);
		    column.addUkConstraint(constraint);
		}
	}
    
    private void importImportedKeys() {
        logger.info("Importing imported keys");
        StopWatch watch = new StopWatch("importImportedKeys");
        int count = 0;
        for (DBCatalog catalog : database.getCatalogs())
	        for (DBSchema schema : catalog.getSchemas())
	            for (DBTable table : schema.getTables()) {
	                if (!tableSupported(table.getName()))
	                	continue;
	                importImportedKeys(table);
	                count++;
	            }
        watch.stop();
    }
    
	protected void fkFound(DBForeignKeyConstraint fk, DBTable table) {
		table.addForeignKey(fk);
	}

	protected void checkFound(DBCheckConstraint check, DBTable table) {
		table.addCheckConstraint(check);
	}
    
	protected void referrerFound(String fktable_name, DBTable table) {
		DBTable referrer = database.getTable(fktable_name);
		table.addReferrer(referrer);
	}

	protected void sequenceFound(DBSequence sequence, DBSchema schema) {
		schema.addSequence(sequence);
	}
	
}
