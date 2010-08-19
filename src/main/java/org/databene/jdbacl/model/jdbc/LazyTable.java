/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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
import java.util.Iterator;
import java.util.List;

import org.databene.commons.NullSafeComparator;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBIndex;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBRow;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.databene.jdbacl.model.DefaultDBTable;

/**
 * Table representation that supports lazy loading of its column and constraint info.<br/><br/>
 * Created: 18.08.2010 10:56:11
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class LazyTable implements DBTable {

	private static final long serialVersionUID = 188548842580766795L;
	
	private JDBCDBImporter importer;
	private DBCatalog catalog;
	private DBSchema schema;
	private String tableName;
	private String remarks;
	
	private DefaultDBTable realTable;
	
	public LazyTable(JDBCDBImporter importer, DBCatalog catalog, DBSchema schema, String tableName, String remarks) {
	    this.importer = importer;
	    this.catalog = catalog;
	    this.schema = schema;
	    this.tableName = tableName;
	    this.remarks = remarks;
    }

	public DBCatalog getCatalog() {
		return catalog;
    }

	public DBSchema getSchema() {
		return schema;
    }

	public String getName() {
	    return tableName;
    }

	public String getDoc() {
	    return remarks;
    }

	public DBColumn getColumn(String columnName) {
	    return getRealTable().getColumn(columnName);
    }

	public List<DBColumn> getColumns() {
	    return getRealTable().getColumns();
    }

	public DBColumn[] getColumns(List<String> columnNames) {
	    return getRealTable().getColumns(columnNames);
    }

	public List<DBForeignKeyConstraint> getForeignKeyConstraints() {
	    return getRealTable().getForeignKeyConstraints();
    }

	public DBIndex getIndex(String indexName) {
	    return getRealTable().getIndex(indexName);
    }

	public List<DBIndex> getIndexes() {
	    return getRealTable().getIndexes();
    }

	public String[] getPKColumnNames() {
	    return getRealTable().getPKColumnNames();
    }

	public DBPrimaryKeyConstraint getPrimaryKeyConstraint() {
	    return getRealTable().getPrimaryKeyConstraint();
    }

	public long getRowCount(Connection connection) {
	    return getRealTable().getRowCount(connection);
    }

	public List<DBUniqueConstraint> getUniqueConstraints() {
	    return getRealTable().getUniqueConstraints();
    }

	public Collection<DBTable> getReferrers() {
	    return getRealTable().getReferrers();
    }
	
	public Iterator<DBRow> queryByColumnValues(String[] columnNames, Object[] values, Connection connection)
            throws SQLException {
	    return getRealTable().queryByColumnValues(columnNames, values, connection);
    }

	public DBRow queryById(Object[] idParts, Connection connection) throws SQLException {
	    return getRealTable().queryById(idParts, connection);
    }

	public Iterator<DBRow> allRows(Connection connection) throws SQLException {
	    return getRealTable().allRows(connection);
    }

	public DefaultDBTable getRealTable() {
		if (realTable == null)
			realTable = (DefaultDBTable) importer.importTable(catalog, schema, tableName, remarks, true);
	    return realTable;
    }

	// implementation of the 'Dependent' interface ---------------------------------------------------------------------
	
	public int countProviders() {
	    return getRealTable().countProviders();
    }

	public DBTable getProvider(int index) {
	    return getRealTable().getProvider(index);
    }

	public boolean requiresProvider(int index) {
	    return getRealTable().requiresProvider(index);
    }
	
	// java.lang.Object overrides --------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return tableName;
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
	    result = prime * result + ((schema == null) ? 0 : schema.hashCode());
	    result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object other) {
	    if (this == other)
		    return true;
	    if (other == null || !(other instanceof DBTable))
		    return false;
	    DBTable that = (DBTable) other;
	    if (!NullSafeComparator.equals(this.catalog, that.getCatalog()))
	    	return false;
	    else if (!NullSafeComparator.equals(this.schema, that.getSchema()))
	    	return false;
	    return NullSafeComparator.equals(this.tableName, that.getName());
    }

}
