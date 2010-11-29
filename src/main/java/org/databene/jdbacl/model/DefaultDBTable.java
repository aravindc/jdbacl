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

import org.databene.commons.ObjectNotFoundException;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.SQLUtil;
import org.databene.commons.depend.Dependent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
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
public class DefaultDBTable extends AbstractDBCompositeObject<DBTableComponent> implements DBTable, Dependent<DBTable> {

    private static final long serialVersionUID = 6829370969378083211L;
    private static final String[] EMPTY_ARRAY = new String[0];
    
    private Set<DBTable> referrers;

    // constructors ----------------------------------------------------------------------------------------------------

    public DefaultDBTable() {
        this(null);
    }

    public DefaultDBTable(String name) {
        this(name, null);
    }

    public DefaultDBTable(String name, DBSchema schema) {
        super(name, schema);
        this.doc = null;
        this.referrers = new HashSet<DBTable>();
    }
    
    @Override
    public void addComponent(DBTableComponent component) {
    	super.addComponent(component);
    }

    // properties ------------------------------------------------------------------------------------------------------

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

	public DBCatalog getCatalog() {
		return getSchema().getCatalog();
	}

	public DBSchema getSchema() {
        return (DBSchema) getOwner();
    }

    public void setSchema(DBSchema schema) {
        setOwner(schema);
    }

    public void setPrimaryKeyConstraint(DBPrimaryKeyConstraint constraint) {
        addComponent(constraint);
    }

    public DBPrimaryKeyConstraint getPrimaryKeyConstraint() {
    	List<DBPrimaryKeyConstraint> pks = getComponents(DBPrimaryKeyConstraint.class, false);
    	return (pks.size() > 0 ? pks.get(0) : null);
    }

    // column operations -----------------------------------------------------------------------------------------------

    public List<DBColumn> getColumns() {
        return getComponents(DBColumn.class, false);
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
        DBColumn column = (DBColumn) getComponent(columnName);
        if (column == null)
            throw new ObjectNotFoundException("Column '" + columnName + 
                    "' not found in table '" + this.getName() + "'");
        return column;
    }

    public void addColumn(DBColumn column) {
        column.setTable(this);
        addComponent(column);
    }

    // index operations ------------------------------------------------------------------------------------------------

    public List<DBIndex> getIndexes() {
        return getComponents(DBIndex.class, false);
    }

    public DBIndex getIndex(String indexName) {
        return (DBIndex) getComponent(indexName);
    }

    public void addIndex(DBIndex index) {
        addComponent(index);
    }

    public void removeIndex(DBIndex index) {
        removeComponent(index);
    }

    // uniqueConstraint operations -------------------------------------------------------------------------------------

    public List<DBUniqueConstraint> getUniqueConstraints() {
        return getComponents(DBUniqueConstraint.class, false);
    }

	public DBUniqueConstraint getUniqueConstraint(String[] columnNames) {
		for (DBUniqueConstraint constraint : getComponents(DBUniqueConstraint.class, false))
			if (Arrays.equals(columnNames, constraint.getColumnNames()))
				return constraint;
		return null;
	}

	public void addUniqueConstraint(DBUniqueConstraint constraint) {
        addComponent(constraint);
    }

    public void removeUniqueConstraint(DBUniqueConstraint constraint) {
        removeComponent(constraint);
    }

    // ForeignKeyConstraint operations ---------------------------------------------------------------------------------

    public List<DBForeignKeyConstraint> getForeignKeyConstraints() {
        return getComponents(DBForeignKeyConstraint.class, false);
    }

    public void addForeignKeyConstraint(DBForeignKeyConstraint constraint) {
        addComponent(constraint);
    }

    public void removeForeignKeyConstraint(DBForeignKeyConstraint constraint) {
        removeComponent(constraint);
    }

    // referrer operations ---------------------------------------------------------------------------------------------
    
    public Collection<DBTable> getReferrers() {
    	return referrers;
    }
    
	public void addReferrer(DBTable referrer) {
		referrers.add(referrer);
    }
    
	public String[] getPKColumnNames() {
		DBPrimaryKeyConstraint pk = getPrimaryKeyConstraint();
		return (pk != null ? pk.getColumnNames() : EMPTY_ARRAY);
	}

    // row operations --------------------------------------------------------------------------------------------------

    public Iterator<DBRow> allRows(Connection connection) throws SQLException {
        return new DBRowIterator(this, connection, null);
    }
    
	public long getRowCount(Connection connection) {
		Object result = DBUtil.queryScalar("select count(*) from " + name, connection);
		return ((Number) result).longValue();
	}

    public DBRow queryByPK(Object[] idParts, Connection connection) throws SQLException {
    	String[] pkColumnNames = getPrimaryKeyConstraint().getColumnNames();
    	if (pkColumnNames.length == 0)
    		throw new ObjectNotFoundException("Table " + name + " has no primary key");
		String whereClause = SQLUtil.renderWhereClause(pkColumnNames, idParts);
        DBRowIterator iterator = new DBRowIterator(this, connection, whereClause);
        if (!iterator.hasNext())
        	throw new ObjectNotFoundException("No " + name + " row with id " + idParts); // TODO handle arrays
		return iterator.next();
    }
    
    public Iterator<DBRow> queryByColumnValues(String[] columns, Object[] values, Connection connection) throws SQLException {
		String whereClause = SQLUtil.renderWhereClause(columns, values);
        return new DBRowIterator(this, connection, whereClause);
    }
    
	// implementation of the 'Dependent' interface ---------------------------------------------------------------------

    public int countProviders() {
        return getForeignKeyConstraints().size();
    }

    public DBTable getProvider(int index) {
        return getForeignKeyConstraints().get(index).getForeignTable();
    }

    public boolean requiresProvider(int index) {
        String fkColumnName = getForeignKeyConstraints().get(index).getForeignKeyColumnNames()[0];
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
