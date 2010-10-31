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
import org.databene.commons.NullSafeComparator;
import org.databene.commons.ObjectNotFoundException;
import org.databene.commons.collection.OrderedNameMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JDBC catalog.<br/><br/>
 * Created: 06.01.2007 08:57:57
 * @author Volker Bergmann
 */
public class DBCatalog implements Named, Serializable {

    private static final long serialVersionUID = 3956827426638393655L;
    
	private String name;
    private String doc;
    private Database database;
    private OrderedNameMap<DBSchema> schemas;

    // constructors ----------------------------------------------------------------------------------------------------

    public DBCatalog() {
        this(null);
    }

    public DBCatalog(String name) {
        this.name = name;
        this.schemas = OrderedNameMap.createCaseInsensitiveMap();
    }

    // properties ------------------------------------------------------------------------------------------------------

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

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

    // schema operations -----------------------------------------------------------------------------------------------

    public List<DBSchema> getSchemas() {
        return schemas.values();
    }

    public DBSchema getSchema(String schemaName) {
        return schemas.get(schemaName);
    }

    public void addSchema(DBSchema schema) {
        schema.setCatalog(this);
        schemas.put(schema.getName(), schema);
    }

    public void removeSchema(DBSchema schema) {
        schemas.remove(schema.getName());
        schema.setCatalog(null);
    }

    // table operations ------------------------------------------------------------------------------------------------
    
    public List<DBTable> getTables() {
    	List<DBTable> tables = new ArrayList<DBTable>();
        for (DBSchema schema : schemas.values())
            for (DBTable table : schema.getTables())
            	tables.add(table);
        return tables;
    }

    public DBTable getTable(String name) {
        for (DBSchema schema : schemas.values())
            for (DBTable table : schema.getTables())
            	if (table.getName().equals(name))
            		return table;
        throw new ObjectNotFoundException("Table '" + name + "'");
    }
    
	public void removeTable(String tableName) {
		DBTable table = getTable(tableName);
		schemas.get(table.getSchema()).removeTable(table);
    }
	
    // java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final DBCatalog that = (DBCatalog) o;
        return NullSafeComparator.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

    @Override
    public String toString() {
        return name;
    }
}
