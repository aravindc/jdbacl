/*
 * (c) Copyright 2010-2012 by Volker Bergmann. All rights reserved.
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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.databene.commons.HeavyweightIterator;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.bean.HashCodeBuilder;
import org.databene.commons.iterator.TabularIterator;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBCheckConstraint;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.CompositeDBObject;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBIndex;
import org.databene.jdbacl.model.DBObject;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBRow;
import org.databene.jdbacl.model.DBRowIterator;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBTableComponent;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.databene.jdbacl.model.DefaultDBTable;
import org.databene.jdbacl.model.TableType;

/**
 * Table representation that supports lazy loading of its column and constraint info.<br/><br/>
 * Created: 18.08.2010 10:56:11
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class LazyTable implements DBTable {

	private static final long serialVersionUID = 188548842580766795L;
	
	private String name;
	private TableType type;
	private String doc;
	private LazyJDBCDBImporter importer;
	private DBSchema schema;
	
	DefaultDBTable realTable;
	
	boolean columnsImported;
	boolean pkImported;
	boolean fksImported;
    boolean indexesImported;
    boolean referrersImported;
	boolean checksImported;
	
	public LazyTable(LazyJDBCDBImporter importer, String tableName, TableType type, String doc, DBSchema schema) {
	    this.importer = importer;
	    this.schema = schema;
	    this.name = tableName;
	    this.type = type;
	    this.doc = doc;
	    this.realTable = null;
	    
		this.columnsImported = false;
		this.pkImported = false;
		this.fksImported = false;
		this.referrersImported = false;
		this.checksImported = false;
	    this.indexesImported = false;
        if (schema != null)
        	schema.addTable(this);
    }
	
	public LazyTable(DefaultDBTable realTable, DBSchema schema, LazyJDBCDBImporter importer) {
	    this.importer = importer;
	    this.schema = schema;
	    this.realTable = realTable;
	    this.name = realTable.getName();
	    this.type = realTable.getType();
	    this.doc = realTable.getDoc();
	    
		this.columnsImported = false;
		this.pkImported = false;
		this.fksImported = false;
		this.referrersImported = false;
		this.checksImported = false;
	    this.indexesImported = false;
        if (schema != null)
        	schema.addTable(this);
	}

	public TableType getType() {
		return type;
	}
	
	// implementation of the meta data part of the 'DBTable' interface -------------------------------------------------
	
	public String getObjectType() {
		return "table";
	}

	public List<DBTableComponent> getComponents() {
		return getRealTable().getComponents();
	}

	public CompositeDBObject<?> getOwner() {
		return getRealTable().getOwner();
	}

	public void setOwner(CompositeDBObject<?> owner) {
		getRealTable().setOwner(owner);
	}
	
	public void setPrimaryKey(DBPrimaryKeyConstraint pk) {
		getRealTableWithPK().setPrimaryKey(pk);
	}

	public void addUniqueConstraint(DBUniqueConstraint uk) {
		getRealTableWithPK().addUniqueConstraint(uk);
	}

	public void addForeignKey(DBForeignKeyConstraint fk) {
		getRealTableWithFKs().addForeignKey(fk);
	}

	public void addIndex(DBIndex index) {
		getRealTableWithIndexes().addIndex(index);
	}

	public DBRowIterator queryRows(String whereClause, Connection connection) throws SQLException {
        return getRealTableWithColumns().queryRows(whereClause, connection).withTable(this);
	}

	public HeavyweightIterator<Object> queryPKValues(Connection connection) {
		return getRealTableWithPK().queryPKValues(connection);
	}

	public TabularIterator query(String query, Connection connection) {
		return getRealTable().query(query, connection);
	}

	public DBRow queryByPK(Object pk, Connection connection) throws SQLException {
		return getRealTableWithPK().queryByPK(pk, connection).withTable(this);
	}

	public DBCatalog getCatalog() {
		return schema.getCatalog();
    }

	public DBSchema getSchema() {
		return schema;
    }

	public String getName() {
	    return name;
    }

	public String getDoc() {
	    return doc;
    }

	public String[] getColumnNames() {
		return getRealTableWithColumns().getColumnNames();
	}

	public DBColumn getColumn(String columnName) {
	    return getRealTableWithColumns().getColumn(columnName);
    }

	public List<DBColumn> getColumns() {
	    return getRealTableWithColumns().getColumns();
    }

	public void addColumn(DBColumn column) {
		getRealTableWithColumns().addColumn(column);
	}

	public DBColumn[] getColumns(String[] columnNames) {
	    return getRealTableWithColumns().getColumns(columnNames);
    }

	public Set<DBForeignKeyConstraint> getForeignKeyConstraints() {
	    return getRealTableWithFKs().getForeignKeyConstraints();
    }

	public DBForeignKeyConstraint getForeignKeyConstraint(String[] columnNames) {
	    return getRealTableWithFKs().getForeignKeyConstraint(columnNames);
	}

	public void setChecksImported(boolean checksImported) {
		this.checksImported = checksImported;
	}

	public List<DBCheckConstraint> getCheckConstraints() {
		return getRealTableWithChecks().getCheckConstraints();
	}

	public void addCheckConstraint(DBCheckConstraint checkConstraint) {
		getRealTableWithChecks().addCheckConstraint(checkConstraint);
	}

	public DBIndex getIndex(String indexName) {
	    return getRealTableWithIndexes().getIndex(indexName);
    }

	public List<DBIndex> getIndexes() {
	    return getRealTableWithIndexes().getIndexes();
    }

	public String[] getPKColumnNames() {
	    return getRealTableWithPK().getPKColumnNames();
    }

	public DBPrimaryKeyConstraint getPrimaryKeyConstraint() {
	    return getRealTableWithPK().getPrimaryKeyConstraint();
    }

	public long getRowCount(Connection connection) {
	    return getRealTable().getRowCount(connection);
    }

	public Set<DBUniqueConstraint> getUniqueConstraints(boolean includePK) {
	    return getRealTableWithIndexes().getUniqueConstraints(includePK);
    }

	public DBUniqueConstraint getUniqueConstraint(String name) {
	    return getRealTableWithIndexes().getUniqueConstraint(name);
    }

	public Collection<DBTable> getReferrers() {
	    return getRealTableWithReferrers().getReferrers();
    }
	
	public void addReferrer(DBTable table) {
		getRealTableWithReferrers().addReferrer(table);
	}

	public DBRowIterator queryRowsByCellValues(String[] columnNames, Object[] values, Connection connection)
            throws SQLException {
	    return getRealTableWithColumns().queryRowsByCellValues(columnNames, values, connection).withTable(this);
    }

	public DBRow queryByPK(Object[] idParts, Connection connection) throws SQLException {
	    return getRealTableWithPK().queryByPK(idParts, connection).withTable(this);
    }

	public DBRowIterator allRows(Connection connection) throws SQLException {
	    return getRealTableWithColumns().allRows(connection).withTable(this);
    }
	

	
	// implementation of the 'Dependent' part of the 'DBTable' interface -----------------------------------------------
	
	public int countProviders() {
	    return getRealTable().countProviders();
    }

	public DBTable getProvider(int index) {
	    return getRealTable().getProvider(index);
    }

	public boolean requiresProvider(int index) {
	    return getRealTable().requiresProvider(index);
    }
	
	public boolean isIdentical(DBObject other) {
		return getRealTable().isIdentical(other);
	}
	
	
		
	// access to the wrapped object ------------------------------------------------------------------------------------

	private DefaultDBTable getRealTable() {
		if (realTable == null) {
			realTable = new DefaultDBTable(name, type, doc, null);
			realTable.setSchema(schema);
		}
	    return realTable;
    }

	public DefaultDBTable getRealTableWithColumns() {
		getRealTable();
		if (!columnsImported) {
			importer.importColumnsForTable(realTable);
			columnsImported = true;
		}
	    return realTable;
    }

	public DefaultDBTable getRealTableWithPK() {
		getRealTableWithColumns();
		if (!pkImported) {
			importer.importPrimaryKeyOfTable(this);
			pkImported = true;
		}
	    return realTable;
    }

	public DefaultDBTable getRealTableWithFKs() {
		getRealTableWithColumns();
		if (!fksImported) {
			importer.importImportedKeys(this);
			fksImported = true;
		}
	    return realTable;
    }

	public DefaultDBTable getRealTableWithReferrers() {
		getRealTableWithColumns();
		if (!referrersImported) {
			importer.importRefererTables(realTable);
			referrersImported = true;
		}
	    return realTable;
    }

	public DefaultDBTable getRealTableWithIndexes() {
		getRealTableWithColumns();
		if (!indexesImported) {
			importer.importIndexesOfTable(realTable, false);
			indexesImported = true;
		}
	    return realTable;
    }

	public DefaultDBTable getRealTableWithChecks() {
		getRealTableWithColumns();
		if (!checksImported) {
			synchronized (realTable.getCatalog()) {
				if (!checksImported) {
					importer.importAllChecks();
					checksImported = true;
				}
			}
		}
	    return realTable;
    }

	// java.lang.Object overrides --------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return name;
	}

	@Override
    public int hashCode() {
	    return HashCodeBuilder.hashCode(schema, name);
    }

	@Override
    public boolean equals(Object other) {
	    if (this == other)
		    return true;
	    if (other == null || !(other instanceof DBTable))
		    return false;
	    DBTable that = (DBTable) other;
	    if (!NullSafeComparator.equals(this.schema, that.getSchema()))
	    	return false;
	    return NullSafeComparator.equals(this.name, that.getName());
    }

}
