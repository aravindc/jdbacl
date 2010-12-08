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

package org.databene.jdbacl.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.databene.commons.ObjectNotFoundException;
import org.databene.commons.StringUtil;
import org.databene.commons.collection.OrderedNameMap;

/**
 * Default implementation of the {@link Database} interface.<br/><br/>
 * Created: 05.12.2010 16:18:57
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class DefaultDatabase extends AbstractCompositeDBObject<DBCatalog> implements Database {
	
	private static final long serialVersionUID = -1873203097942961523L;
	
	OrderedNameMap<DBCatalog> catalogs;
	
    // constructors ----------------------------------------------------------------------------------------------------

    public DefaultDatabase(String name) {
        super(name);
        this.catalogs = OrderedNameMap.createCaseInsensitiveMap();
    }

    // CompositeDBObject implementation --------------------------------------------------------------------------------

	public List<DBCatalog> getComponents() {
		return catalogs.values();
	}
	
    // catalog operations ----------------------------------------------------------------------------------------------

    public List<DBCatalog> getCatalogs() {
        return getComponents();
    }

    public DBCatalog getCatalog(String catalogName) {
        return catalogs.get(catalogName);
    }

    public void addCatalog(DBCatalog catalog) {
        catalog.setDatabase(this);
        catalogs.put(catalog.getName(), catalog);
    }

    public void removeCatalog(DBCatalog catalog) {
        catalogs.remove(catalog.getName());
        catalog.setOwner(null);
    }

    // table operations ------------------------------------------------------------------------------------------------

    public Set<DBTable> getTables() {
    	Set<DBTable> tables = new HashSet<DBTable>();
        for (DBCatalog catalog : getComponents())
            for (DBTable table : catalog.getTables())
            	tables.add(table);
        return tables;
    }

    public DBTable getTable(String name) {
        for (DBCatalog catalog : getComponents())
            for (DBTable table : catalog.getTables())
            	if (StringUtil.equalsIgnoreCase(table.getName(), name))
            		return table;
        throw new ObjectNotFoundException("Table '" + name + "'");
    }
    
	public void removeTable(String tableName) {
		DBTable table = getTable(tableName);
		table.getSchema().removeTable(table);
    }

}