/*
 * (c) Copyright 2006-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.jdbacl.model.jdbc;

import org.databene.commons.ConnectFailedException;
import org.databene.commons.ErrorHandler;
import org.databene.commons.Escalator;
import org.databene.commons.Filter;
import org.databene.commons.ImportFailedException;
import org.databene.commons.LoggerEscalator;
import org.databene.commons.ObjectNotFoundException;
import org.databene.commons.StringUtil;
import org.databene.commons.Level;
import org.databene.commons.collection.OrderedNameMap;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.DatabaseDialectManager;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBColumnType;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBMetaDataImporter;
import org.databene.jdbacl.model.DBIndex;
import org.databene.jdbacl.model.DBNonUniqueIndex;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.databene.jdbacl.model.DBUniqueIndex;
import org.databene.jdbacl.model.Database;
import org.databene.jdbacl.model.DefaultDBColumn;
import org.databene.jdbacl.model.DefaultDBTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Imports a DB model via JDBC.<br/><br/>
 * Created: 06.01.2007 19:16:45
 * @author Volker Bergmann
 */
public final class JDBCDBImporter implements DBMetaDataImporter, Closeable {

    private static final Logger logger = LoggerFactory.getLogger(JDBCDBImporter.class);

    final String user;
    String catalogName;
    String schemaName;
    Pattern includeTables;
    Pattern excludeTables;
    boolean importingIndexes;
	boolean importingUKs = true;
	boolean lazy = false;
	
    final Connection connection;
    DatabaseDialect dialect;
    String productName;

    Escalator escalator = new LoggerEscalator();
    ErrorHandler errorHandler;
    TableNameFilter tableNameFilter;

	DatabaseMetaData metaData;
	Database database;

    public JDBCDBImporter(String url, String driverClassname, String user, String password) throws ConnectFailedException {
        this(url, driverClassname, user, password, null, ".*", true);
    }

    public JDBCDBImporter(String url, String driver, String user, String password, 
    		String schemaName, String tablePattern, boolean importingIndexes) throws ConnectFailedException {
    	this(DBUtil.connect(url, driver, user, password), user, schemaName, tablePattern, importingIndexes);
    }

    public JDBCDBImporter(Connection connection, String user, 
    		String schemaName, String includeTables, boolean importingIndexes) {
    	this.connection = connection;
        this.user = user;
        this.schemaName = schemaName;
        this.includeTables = Pattern.compile(includeTables != null ? includeTables : ".*");
        this.importingIndexes = importingIndexes;
        this.errorHandler = new ErrorHandler(getClass());
    }

    // properties ------------------------------------------------------------------------------------------------------
    
    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }
    
    public void setFaultTolerant(boolean faultTolerant) {
    	this.errorHandler = new ErrorHandler(getClass().getName(), (faultTolerant ? Level.warn : Level.error));
    }

	public Connection getConnection() {
	    return connection;
    }

    @Deprecated
	public void setTablePattern(Pattern tablePattern) {
    	this.includeTables = tablePattern;
    }

	public void setIncludeTables(String includeTables) {
    	this.includeTables = (StringUtil.isEmpty(includeTables) ? null : Pattern.compile(includeTables));
    }

	public void setExcludeTables(String excludeTables) {
    	this.excludeTables = (StringUtil.isEmpty(excludeTables) ? null : Pattern.compile(excludeTables));
    }

	public boolean isImportingIndexes() {
    	return importingIndexes;
    }

	public void setImportingIndexes(boolean importingIndexes) {
    	this.importingIndexes = importingIndexes;
    }

	public void setImportingUKs(boolean importingUKs) {
		this.importingUKs  = importingUKs;
    }

    public void setSchemaName(String schemaName) {
	    this.schemaName = schemaName;
    }
    
    public void setCatalogName(String catalogName) {
	    this.catalogName = catalogName;
    }
    
	public void setLazy(boolean lazy) {
    	this.lazy = lazy;
    }

	// functional interface --------------------------------------------------------------------------------------------
	
	public Database importDatabase() throws ImportFailedException {
        logger.info("Importing database metadata. Be patient, this may take some time...");
        long startTime = System.currentTimeMillis();
        tableNameFilter = new TableNameFilter();
        try {
            metaData = connection.getMetaData();
            productName = metaData.getDatabaseProductName();
            if (logger.isDebugEnabled())
                logger.debug("Product name: " + productName);
            dialect = DatabaseDialectManager.getDialectForProduct(productName);
            if (isOracle()) // fix for Oracle varchar column size, see http://kr.forums.oracle.com/forums/thread.jspa?threadID=554236
            	DBUtil.executeUpdate("ALTER SESSION SET NLS_LENGTH_SEMANTICS=CHAR", connection);
            database = new Database(productName);
            importCatalogs();
            importSchemas();
            importTables();
            if (!lazy) {
            	importColumns();
	            if (importingIndexes || importingUKs)
	                importIndexes();
            	importPrimaryKeys();
	            importImportedKeys();
            }
            return database;
        } catch (SQLException e) {
            throw new ImportFailedException(e);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Imported database metadata within " + duration + " ms.");
        }
    }

	public void close() {
        DBUtil.close(connection);
	}
	
	// private helper methods ------------------------------------------------------------------------------------------

	private void importCatalogs() throws SQLException {
        logger.info("Importing catalogs");
        ResultSet catalogSet = metaData.getCatalogs();
        int catalogCount = 0;
        while (catalogSet.next()) {
            String catalogName = catalogSet.getString(1);
            logger.debug("found catalog " + catalogName);
            if ((schemaName == null && user.equalsIgnoreCase(catalogName)) 
            		|| (schemaName != null && this.catalogName.equalsIgnoreCase(catalogName))
            		|| catalogName.equalsIgnoreCase(connection.getCatalog()))
                this.catalogName = catalogName;
            database.addCatalog(new DBCatalog(catalogName));
            catalogCount++;
        }
        if (catalogCount == 0)
            database.addCatalog(new DBCatalog(null));
        catalogSet.close();
    }

    private void importSchemas() throws SQLException {
        logger.info("Importing schemas");
        int schemaCount = 0;
        ResultSet schemaSet = metaData.getSchemas();
        while (schemaSet.next()) {
            String schemaName = schemaSet.getString(1);
            String catalogName = null;
            if (schemaSet.getMetaData().getColumnCount() >= 2)
            	catalogName = schemaSet.getString(2);
            if (schemaName.equalsIgnoreCase(this.schemaName) 
            		|| (this.schemaName == null && dialect.isDefaultSchema(schemaName, user))) {
	            logger.debug("importing schema {}", schemaName);
	            if (schemaName.equalsIgnoreCase(this.schemaName))
	            	this.schemaName = schemaName; // take over capitalization used in the DB
	            DBSchema schema = new DBSchema(schemaName);
	            database.getCatalog(catalogName).addSchema(schema);
	            schemaCount++;
            } else
                logger.debug("ignoring schema {}", schemaName);
        }
        if (schemaCount == 0)
        	database.getCatalogs().get(0).addSchema(new DBSchema(null));
        schemaSet.close();
    }

	private void importTables() throws SQLException {
        logger.info("Importing tables");
        ResultSet tableSet = metaData.getTables(catalogName, schemaName, null, new String[] { "TABLE", "VIEW" });
        List<DBTable> importedTables = new ArrayList<DBTable>();
        while (tableSet.next()) {

            // parsing ResultSet line
            String tCatalogName = tableSet.getString(1);
            String tSchemaName = tableSet.getString(2);
            String tableName = tableSet.getString(3);
            if (tableName.startsWith("BIN$")) {
                if (isOracle() && tableName.startsWith("BIN$"))
                    escalator.escalate("BIN$ table found (for improved performance " +
                                "execute 'PURGE RECYCLEBIN;')", this, tableName);
                continue;
            }
            if (!tableSupported(tableName)) {
                if (logger.isDebugEnabled())
                    logger.debug("ignoring table: " + tCatalogName + ", " + tSchemaName + ", " + tableName);
            	continue;
            }
            String tableType = tableSet.getString(4); // Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"
            String tableRemarks = tableSet.getString(5);
            if (logger.isDebugEnabled())
                logger.debug("importing table: " + tCatalogName + ", " + tSchemaName + ", " + tableName + ", " + tableType + ", " + tableRemarks);

            DBCatalog catalog = database.getCatalog(tCatalogName);
			DBTable table = createTable(catalog, catalog.getSchema(tSchemaName), tableName, tableRemarks, lazy);
            importedTables.add(table);
        }
        if (!lazy)
        	for (DBTable table : importedTables)
        		importTableDetails((DefaultDBTable) table);
        tableSet.close();
    }

	DefaultDBTable importTable(DBCatalog catalog, DBSchema schema, String tableName, String remarks) {
		DefaultDBTable table = (DefaultDBTable) createTable(catalog, schema, tableName, remarks, false);
		importTableDetails(table);
        return table;
    }

	private void importTableDetails(DefaultDBTable table) {
	    importColumns(table.getCatalog(), table.getSchema().getName(), table.getName(), null, null);
	    importPrimaryKeys(table);
	    importImportedKeys(table);
	    importRefererTables(table);
    }

	private DBTable createTable(DBCatalog catalog, DBSchema schema, String tableName, String remarks, boolean lazy) {
	    DBTable table;
		if (lazy) {
			table = new LazyTable(this, schema, tableName, remarks);
		} else {
			DefaultDBTable dTable = new DefaultDBTable(tableName);
			dTable.setDoc(remarks);
			dTable.setSchema(schema);
			table = dTable;
		}
        if (schema != null)
            schema.addTable(table);
	    return table;
    }

	private boolean isOracle() {
		return productName.toLowerCase().startsWith("oracle");
	}

    private void importColumns() {
		if (this.catalogName != null)
			importColumns(database.getCatalog(this.catalogName), this.schemaName, null, tableNameFilter, errorHandler);
		else
			for (DBCatalog catalog : database.getCatalogs())
				importColumns(catalog, this.schemaName, null, tableNameFilter, errorHandler);
    }

    private void importColumns(DBCatalog catalog, String schemaName, String tablePattern, Filter<String> tableFilter, ErrorHandler errorHandler) {
        String catalogName = catalog.getName();
        String schemaPattern = (schemaName != null ? schemaName : (catalog.getSchemas().size() == 1 ? catalog.getSchemas().get(0).getName() : null));
        logger.debug("Importing columns for catalog '" + catalogName + "', schemaPattern '" + schemaName + "', tablePattern '" + tablePattern + "'");
        ResultSet columnSet = null;
        try {
        	columnSet = metaData.getColumns(catalogName, schemaPattern, tablePattern, null);
	        ResultSetMetaData setMetaData = columnSet.getMetaData();
	        if (setMetaData.getColumnCount() == 0)
	            return;
	        while (columnSet.next()) {
	            String colSchemaName = columnSet.getString(2);
	            String tableName = columnSet.getString(3);
	            String columnName = columnSet.getString(4);
	            if (tableName.startsWith("BIN$") || (tableFilter != null && !tableFilter.accept(tableName))) {
	            	if (logger.isDebugEnabled())
	            		logger.debug("ignoring column " + catalogName + "." + colSchemaName + "." + tableName + "." + columnName);
	                continue;
	            }
	            int sqlType = columnSet.getInt(5);
	            String columnType = columnSet.getString(6);
	            int columnSize = columnSet.getInt(7);
	            int decimalDigits = columnSet.getInt(9);
	            boolean nullable = columnSet.getBoolean(11);
	            String comment = columnSet.getString(12);
	            String defaultValue = columnSet.getString(13);
	
	            if (logger.isDebugEnabled())
	            	logger.debug("found column: " + catalogName + ", " + colSchemaName + ", " + tableName + ", "
	                        + columnName + ", " + sqlType + ", " + columnType + ", " + columnSize + ", " + decimalDigits
	                        + ", " + nullable + ", " + comment + ", " + defaultValue);
	
	            Integer fractionDigits = (decimalDigits > 0 ? decimalDigits : null);
	            DefaultDBColumn column = new DefaultDBColumn(columnName, DBColumnType.getInstance(sqlType, columnType), columnSize, fractionDigits);
	            if (!StringUtil.isEmpty(comment))
	                column.setDoc(comment);
	            if (!StringUtil.isEmpty(defaultValue)) {
	                if (!column.getType().isAlpha())
	                    defaultValue = removeBrackets(defaultValue); // some driver adds brackets to number defaults
	                column.setDefaultValue(defaultValue.trim()); // oracle thin driver produces "1 "
	            }
	            if (!nullable)
	                column.setNullable(false);
	
	            DefaultDBTable table = (DefaultDBTable) catalog.getTable(tableName);
	            if (table == null) {
	                DBSchema schema = catalog.getSchema(schemaName);
	                if (schema != null)
	                    table = (DefaultDBTable) schema.getTable(tableName);
	            }
	            if (table != null)
	                table.addColumn(column);
	            // not used: importVersionColumnInfo(catalogName, table, metaData);
	        }
    	} catch (SQLException e) {
    		// possibly we try to access a catalog to which we do not have access rights
    		if (errorHandler == null)
    			errorHandler = new ErrorHandler(getClass());
    		errorHandler.handleError("Error in parsing columns of catalog " + catalog.getName(), e);
        } finally {
        	DBUtil.close(columnSet);
        }
    }

    private static String removeBrackets(String defaultValue) {
        if (StringUtil.isEmpty(defaultValue))
            return defaultValue;
        if (!defaultValue.startsWith("(") || !defaultValue.endsWith(")"))
            return defaultValue;
        return removeBrackets(defaultValue.substring(1, defaultValue.length() - 1));
    }

    
/*
    private void importVersionColumnInfo(DBCatalog catalogName, DBTable table, DatabaseMetaData metaData) throws SQLException {
        ResultSet versionColumnSet = metaData.getVersionColumns(catalogName.getName(), null, table.getName());
//        DBUtil.print(versionColumnSet);
        while (versionColumnSet.next()) {
            // short scope = versionColumnSet.getString(1);
            String columnName = versionColumnSet.getString(2);
            //int dataType = versionColumnSet.getInt(3);
            //String typeName = versionColumnSet.getString(4);
            //int columnSize = versionColumnSet.getInt(5);
            //int bufferLength = versionColumnSet.getInt(6);
            //short decimalDigits = versionColumnSet.getShort(7);
            //short pseudoColumn = versionColumnSet.getShort(8);
            DBColumn column = table.getColumn(columnName);
            column.setVersionColumn(true);
        }
    }
*/
    private void importPrimaryKeys() {
        int count = 0;
        DBSchema schema = database.getCatalog(catalogName).getSchema(schemaName);
        if (schema != null)
            for (DBTable table : schema.getTables()) {
                if (!tableSupported(table.getName()))
                	continue;
                importPrimaryKeys((DefaultDBTable) table);
                count++;
            }
        if (count > 0)
            return;
        DBCatalog catalog = database.getCatalog(catalogName);
        if (catalog != null)
            for (DBTable table : catalog.getTables()) {
                if (!tableSupported(table.getName()))
                	continue;
                importPrimaryKeys((DefaultDBTable) table);
            }
    }

    private void importPrimaryKeys(DefaultDBTable table) {
        logger.debug("Importing primary keys for table " + table);
        ResultSet pkset = null;
        try {
	        pkset = metaData.getPrimaryKeys(catalogName, schemaName, table.getName());
	        TreeMap<Short, String> pkComponents = new TreeMap<Short, String>();
	        String pkName = null;
	        while (pkset.next()) {
	        	String tableName = pkset.getString(3);
	            if (!tableName.equals(table.getName())) // Bug fix for Firebird: 
	            	continue;							// When querying X, it returns the pks of XY too
	
	            String columnName = pkset.getString(4);
	            short keySeq = pkset.getShort(5);
	            pkComponents.put(keySeq, columnName);
	            pkName = pkset.getString(6);
	            if (logger.isDebugEnabled())
	                logger.debug("found pk column " + columnName + ", " + keySeq + ", " + pkName);
	        }
	        if (pkComponents.size() > 0) {
		        String[] columnNames = pkComponents.values().toArray(new String[pkComponents.size()]);
		        DBPrimaryKeyConstraint constraint = new DBPrimaryKeyConstraint(table, pkName, columnNames);
		        table.setPrimaryKey(constraint);
		        for (String columnName : columnNames) {
		        	DBColumn column = table.getColumn(columnName);
		            column.addUkConstraint(constraint);
		        }
	        }
        } catch (SQLException e) {
        	errorHandler.handleError("Error importing primary key of table " + table.getName());
        } finally {
        	DBUtil.close(pkset);
        }
    }

    private void importIndexes() {
        for (DBCatalog catalog : database.getCatalogs()) {
        	try {
        		importIndexes(catalog, metaData);
        	} catch (SQLException e) {
        		// possibly we try to query a catalog to which we do not have access rights
        		errorHandler.handleError("Error parsing metadata of catalog " + catalog.getName());
        	}
        }
    }

	private void importIndexes(DBCatalog catalog, DatabaseMetaData metaData) throws SQLException {
	    for (DBTable table : catalog.getTables()) {
	    	if (!tableSupported(table.getName()))
	    		continue;
	        logger.debug("Importing indexes for table '" + table.getName() + "'");
	        OrderedNameMap<DBIndexInfo> tableIndexes = new OrderedNameMap<DBIndexInfo>();
	        ResultSet indexSet = null;
	        try {
		        indexSet = metaData.getIndexInfo(catalog.getName(), null, table.getName(), !importingIndexes, false);
		        //DBUtil.print(indexSet);
		        while (indexSet.next()) {
		            String indexName = null;
		            try {
		                boolean unique = !indexSet.getBoolean(4);
		                String indexCatalogName = indexSet.getString(5);
		                indexName = indexSet.getString(6);
		                short indexType = indexSet.getShort(7);
		                /* not used: 
		                 * tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions
		                 * tableIndexClustered - this is a clustered index
		                 * tableIndexHashed - this is a hashed index
		                 * tableIndexOther - this is some other style of index
		                 */
		                short ordinalPosition = indexSet.getShort(8);
		                if (ordinalPosition == 0)
		                    continue;
		                String columnName = indexSet.getString(9);
		                String ascOrDesc = indexSet.getString(10);
		                Boolean ascending = (ascOrDesc != null ? ascOrDesc.charAt(0) == 'A' : null);
		                int cardinality = indexSet.getInt(11);
		                int pages = indexSet.getInt(12);
		                String filterCondition = indexSet.getString(13);
		                if (logger.isDebugEnabled())
		                    logger.debug("found " + (unique ? "unique index " : "index ") + indexName + ", " 
		                            + indexCatalogName + ", " + indexType + ", " 
		                            + ordinalPosition + ", " + columnName + ", " + ascOrDesc + ", " 
		                            + cardinality + ", " + pages + ", " + filterCondition);
		                DBIndexInfo index = tableIndexes.get(indexName);
		                if (index == null) {
		                    index = new DBIndexInfo(indexName, indexType, indexCatalogName, unique,
		                        ordinalPosition, columnName,
		                        ascending, cardinality, pages, filterCondition);
		                    tableIndexes.put(indexName, index);
		                } else {
		                    index.addColumn(ordinalPosition, columnName);
		                }
		            } catch (Exception e) {
		            	errorHandler.handleError("Error parsing indexes: ", e); // TODO errors when parsing GLB indexes
		            }
		        }
	        } finally {
	        	DBUtil.close(indexSet);
	        }
	        for (DBIndexInfo indexInfo : tableIndexes.values()) {
                DBIndex index = null;
	            try {
	                if (indexInfo.unique) {
	                	if (!StringUtil.equalsIgnoreCase(indexInfo.columnNames, table.getPKColumnNames())) {
		                    DBUniqueConstraint constraint = new DBUniqueConstraint(
		                    		table, indexInfo.name, indexInfo.columnNames);
		                    ((DefaultDBTable) table).addUniqueConstraint(constraint);
		                    index = new DBUniqueIndex(indexInfo.name, constraint);
			                ((DefaultDBTable) table).addIndex(index);
	                	}
	                } else {
	                    index = new DBNonUniqueIndex(indexInfo.name, table, indexInfo.columnNames);
		                ((DefaultDBTable) table).addIndex(index);
	                }
	            } catch (ObjectNotFoundException e) {
	                logger.error("Error parsing index: " + index, e);
	            }
	        }
	    }
    }

    private void importImportedKeys() {
        logger.info("Importing imported keys");
        int count = 0;
        for (DBCatalog catalog : database.getCatalogs())
	        for (DBSchema schema : catalog.getSchemas())
	            for (DBTable table : schema.getTables()) {
	                if (!tableSupported(table.getName()))
	                	continue;
	                importImportedKeys((DefaultDBTable) table);
	                count++;
	            }
    }

    private boolean tableSupported(String tableName) {
	    return tableNameFilter.accept(tableName);
    }

    private void importImportedKeys(DefaultDBTable table) {
        logger.debug("Importing imported keys for table {}", table.getName());
        DBCatalog catalog = table.getCatalog();
        DBSchema schema = table.getSchema();
        String catalogName = (catalog != null ? catalog.getName() : null);
        String tableName = table.getName();
        String schemaName = (schema != null ? schema.getName() : null);
        ResultSet resultSet = null;
        try {
	        resultSet = metaData.getImportedKeys(catalogName, schemaName, tableName);
	        List<ImportedKey> importedKeys = new ArrayList<ImportedKey>();
	        ImportedKey recent = null;
	        while (resultSet.next()) {
	            ImportedKey cursor = ImportedKey.parse(resultSet, catalog, schema, table);
	            if (cursor == null) 
	            	continue;
	            if (cursor.key_seq > 1) {
	                recent.addForeignKeyColumn(cursor.fkcolumn_name, cursor.pkcolumn_name);
	            } else
	                importedKeys.add(cursor);
	            recent = cursor;
	        }
	        for (ImportedKey key : importedKeys) {
	            DBForeignKeyConstraint foreignKeyConstraint = new DBForeignKeyConstraint(key.fk_name, table, key.getPkTable());
	            int n = key.getForeignKeyColumnNames().size();
				for (int i = 0; i < n; i++)
	                foreignKeyConstraint.addForeignKeyColumn(key.getForeignKeyColumnNames().get(i), 
	                		key.getRefereeColumnNames().get(i));
	            table.addForeignKey(foreignKeyConstraint);
	            if (logger.isDebugEnabled())
	            	logger.debug("Imported foreign key {}", foreignKeyConstraint);
	        }
        } catch (SQLException e) {
        	errorHandler.handleError("Error importing foreign key constraints", e);
        } finally {
	        DBUtil.close(resultSet);
        }
     }

	private void importRefererTables(DefaultDBTable table) {
        logger.debug("Importing exported keys for table {}", table.getName());
        DBCatalog catalog = table.getCatalog();
        DBSchema schema = table.getSchema();
        String catalogName = (catalog != null ? catalog.getName() : null);
        String tableName = table.getName();
        String schemaName = (schema != null ? schema.getName() : null);
        ResultSet resultSet = null;
        try {
	        resultSet = metaData.getExportedKeys(catalogName, schemaName, tableName);
	        while (resultSet.next()) {
	            String fktable_cat = resultSet.getString(5);
	            String fktable_schem = resultSet.getString(6);
	            String fktable_name = resultSet.getString(7);
	            if (tableSupported(fktable_name)) {
		            DBTable referrer = database.getTable(fktable_name);
		            if (logger.isDebugEnabled())
		            	logger.debug("Imported referrer: " + referrer);
		            table.addReferrer(referrer);
	            }
	        }
        } catch (SQLException e) {
        	errorHandler.handleError("Error importing foreign key constraints", e);
        } finally {
	        DBUtil.close(resultSet);
        }
     }

    class TableNameFilter implements Filter<String> {

		public boolean accept(String tableName) {
			if (tableName.contains("$") || (excludeTables != null && excludeTables.matcher(tableName).matches()))
				return false;
		    return (includeTables == null || includeTables.matcher(tableName).matches());

        }
    	
    }
    
    // java.lang.Object overrides --------------------------------------------------------------------------------------

	@Override
	public String toString() {
	    return getClass().getSimpleName();
	}
	
}
