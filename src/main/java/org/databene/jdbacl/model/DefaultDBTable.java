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

package org.databene.jdbacl.model;

import org.databene.commons.Named;
import org.databene.commons.ObjectNotFoundException;
import org.databene.commons.collection.OrderedNameMap;
import org.databene.jdbacl.DBUtil;
import org.databene.commons.depend.Dependent;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * Represents a database table.<br/><br/>
 * Created: 06.01.2007 08:58:49
 * @author Volker Bergmann
 */
public class DefaultDBTable implements DBTable, Dependent<DBTable>, Named, Serializable {

    private static final long serialVersionUID = 6829370969378083211L;
    
	private DBCatalog catalog;
    private DBSchema schema;
    private String name;
    private String doc;
    private OrderedNameMap<DBColumn> columns;
    private DBPrimaryKeyConstraint primaryKeyConstraint;
    private List<DBUniqueConstraint> uniqueConstraints;
    private List<DBForeignKeyConstraint> foreignKeyConstraints;
    private Set<DBTable> referrers;
    private OrderedNameMap<DBIndex> indexes;

    // constructors ----------------------------------------------------------------------------------------------------

    public DefaultDBTable() {
        this(null);
    }

    public DefaultDBTable(String name) {
        this(null, name);
    }

    public DefaultDBTable(DBCatalog catalog, String name) {
        this.name = name;
        this.catalog = catalog;
        this.columns = new OrderedNameMap<DBColumn>();
        this.primaryKeyConstraint = null;
        this.doc = null;
        this.schema = null;
        this.indexes = new OrderedNameMap<DBIndex>();
        this.uniqueConstraints = new ArrayList<DBUniqueConstraint>();
        this.foreignKeyConstraints = new ArrayList<DBForeignKeyConstraint>();
        this.referrers = new HashSet<DBTable>();
    }

    // properties ------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public DBCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(DBCatalog catalog) {
        this.catalog = catalog;
    }

    public DBSchema getSchema() {
        return schema;
    }

    public void setSchema(DBSchema schema) {
        this.schema = schema;
    }

    public void setPrimaryKeyConstraint(DBPrimaryKeyConstraint constraint) {
        this.primaryKeyConstraint = constraint;
    }

    public DBPrimaryKeyConstraint getPrimaryKeyConstraint() {
        return primaryKeyConstraint;
    }

    // column operations -----------------------------------------------------------------------------------------------

    public List<DBColumn> getColumns() {
        return columns.values();
    }

    public DBColumn[] getColumns(List<String> columnNames) {
        List<DBColumn> list = new ArrayList<DBColumn>(columnNames.size());
        for (String columnName : columnNames) {
            DBColumn column = getColumn(columnName);
            if (column == null)
                throw new IllegalArgumentException("Table '" + name + "' does not have a column '" + columnName + "'");
            list.add(column);
        }
        DBColumn[] array = new DBColumn[columnNames.size()];
        return list.toArray(array);
    }

    public DBColumn getColumn(String columnName) {
        DBColumn column = columns.get(columnName.toUpperCase());
        if (column == null)
            throw new ObjectNotFoundException("Column '" + columnName + 
                    "' not found in table '" + this.getName() + "'");
        return column;
    }

    public void addColumn(DBColumn column) {
        column.setTable(this);
        columns.put(column.getName().toUpperCase(), column);
    }

    // index operations ------------------------------------------------------------------------------------------------

    public List<DBIndex> getIndexes() {
        return indexes.values();
    }

    public DBIndex getIndex(String indexName) {
        return indexes.get(indexName);
    }

    public void addIndex(DBIndex index) {
        indexes.put(index.getName(), index);
    }

    public void removeIndex(DBIndex index) {
        indexes.remove(index.getName());
    }

    // uniqueConstraint operations -------------------------------------------------------------------------------------

    public List<DBUniqueConstraint> getUniqueConstraints() {
        return uniqueConstraints;
    }

    public void addUniqueConstraint(DBUniqueConstraint constraint) {
        uniqueConstraints.add(constraint);
    }

    public void removeUniqueConstraint(DBUniqueConstraint constraint) {
        uniqueConstraints.remove(constraint);
    }

    // ForeignKeyConstraint operations ---------------------------------------------------------------------------------

    public List<DBForeignKeyConstraint> getForeignKeyConstraints() {
        return foreignKeyConstraints;
    }

    public void addForeignKeyConstraint(DBForeignKeyConstraint constraint) {
        foreignKeyConstraints.add(constraint);
    }

    public void removeForeignKeyConstraint(DBForeignKeyConstraint constraint) {
        foreignKeyConstraints.remove(constraint);
    }

    // referrer operations ---------------------------------------------------------------------------------------------
    
    public Collection<DBTable> getReferrers() {
    	return referrers;
    }
    
	public void addReferrer(DBTable referrer) {
		referrers.add(referrer);
    }
    
	public String[] getPKColumnNames() {
        return primaryKeyConstraint.getColumnNames();
	}

    // row operations --------------------------------------------------------------------------------------------------

    public Iterator<DBRow> allRows(Connection connection) throws SQLException {
        return new DBRowIterator(this, connection, null);
    }
    
	public long getRowCount(Connection connection) {
		Object result = DBUtil.queryScalar("select count(*) from " + name, connection);
		return ((Number) result).longValue();
	}

    public DBRow queryById(Object[] idParts, Connection connection) throws SQLException {
    	String[] pkColumnNames = primaryKeyConstraint.getColumnNames();
    	if (pkColumnNames.length == 0)
    		throw new ObjectNotFoundException("Table " + name + " has no primary key");
		String whereClause = DBUtil.renderWhereClause(pkColumnNames, idParts);
        DBRowIterator iterator = new DBRowIterator(this, connection, whereClause);
        if (!iterator.hasNext())
        	throw new ObjectNotFoundException("No " + name + " row with id " + idParts); // TODO handle arrays
		return iterator.next();
    }
    
    public Iterator<DBRow> queryByColumnValues(String[] columns, Object[] values, Connection connection) throws SQLException {
		String whereClause = DBUtil.renderWhereClause(columns, values);
        return new DBRowIterator(this, connection, whereClause);
    }
    
	// implementation of the 'Dependent' interface ---------------------------------------------------------------------

    public int countProviders() {
        return foreignKeyConstraints.size();
    }

    public DBTable getProvider(int index) {
        return foreignKeyConstraints.get(index).getForeignTable();
    }

    public boolean requiresProvider(int index) {
        String fkColumnName = foreignKeyConstraints.get(index).getForeignKeyColumnNames()[0];
		return !getColumn(fkColumnName).isNullable();
    }

	// java.lang.Object overrides --------------------------------------------------------------------------------------
	
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final DefaultDBTable that = (DefaultDBTable) o;
        return name.equals(that.name); // TODO there might be different tables with same name in different schemas
    }

    @Override
    public int hashCode() {
        return name.hashCode(); // TODO there might be different tables with same name in different schemas
    }

    @Override
    public String toString() {
        return name;
    }

}
