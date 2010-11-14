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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a database.<br/><br/>
 * Created: 06.01.2007 18:34:20
 * @author Volker Bergmann
 */
public class Database extends DBCompositeObjectImpl<DBCatalog> implements Named, Serializable {
	
	private static final long serialVersionUID = -1873203097942961523L;
	
    // constructors ----------------------------------------------------------------------------------------------------

    public Database(String name) {
        super(name);
    }

    // catalog operations ----------------------------------------------------------------------------------------------

    public List<DBCatalog> getCatalogs() {
        return getComponents();
    }

    public DBCatalog getCatalog(String catalogName) {
        return getComponent(catalogName);
    }

    public void addCatalog(DBCatalog catalog) {
        catalog.setDatabase(this);
        addComponent(catalog);
    }

    public void removeCatalog(DBCatalog catalog) {
        removeComponent(catalog);
        catalog.setOwner(null);
    }

    // table operations ------------------------------------------------------------------------------------------------

    public Set<DBTable> getTables() {
    	Set<DBTable> tables = new HashSet<DBTable>();
        for (DBCatalog catalog : components.values())
            for (DBTable table : catalog.getTables())
            	tables.add(table);
        return tables;
    }

    public DBTable getTable(String name) {
        for (DBCatalog catalog : components.values())
            for (DBTable table : catalog.getTables())
            	if (table.getName().equals(name))
            		return table;
        throw new ObjectNotFoundException("Table '" + name + "'");
    }
    
	public void removeTable(String tableName) {
		DBTable table = getTable(tableName);
		components.get(table.getCatalog()).removeTable(tableName);
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Database that = (Database) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
