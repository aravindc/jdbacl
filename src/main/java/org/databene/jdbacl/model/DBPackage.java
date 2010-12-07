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

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.collection.OrderedNameMap;

/**
 * TODO Document class.<br/><br/>
 * Created: 05.12.2010 11:06:48
 * @since TODO version
 * @author Volker Bergmann
 */
public class DBPackage extends AbstractCompositeDBObject<DBPackageComponent> implements DBPackageComponent {

    private static final long serialVersionUID = 5890222751656809426L;
    
    OrderedNameMap<DBTable> tables;
    OrderedNameMap<DBPackage> packages;
    
    // constructors ----------------------------------------------------------------------------------------------------

    public DBPackage(String name) {
        this(name, null);
    }

    public DBPackage(String name, CompositeDBObject<? extends DBObject> parent) {
    	super(name);
    	if (parent instanceof DBPackage)
			((DBPackage) parent).addPackage(this);
    	this.tables = OrderedNameMap.createCaseInsensitiveMap();
    	this.packages = OrderedNameMap.createCaseInsensitiveMap();
    }

    private void addPackage(DBPackage dbPackage) {
		packages.put(dbPackage.getName(), dbPackage);
	}

	public DBSchema getSchema() {
		CompositeDBObject<?> parent = getOwner();
        while (parent != null && !(parent instanceof DBSchema))
        	parent = parent.getOwner();
        return (DBSchema) parent;
    }

	public DBCatalog getCatalog() {
        return getSchema().getCatalog();
    }

    // CompositeDBObject implementation --------------------------------------------------------------------------------

	public List<DBPackageComponent> getComponents() {
		List<DBPackageComponent> result = new ArrayList<DBPackageComponent>();
		result.addAll(tables.values());
		result.addAll(packages.values());
		return result;
	}
	
    // table operations ------------------------------------------------------------------------------------------------

    public List<DBTable> getTables() {
        return tables.values();
    }

    public DBTable getTable(String tableName) {
        return tables.get(tableName);
    }

    public void addTable(DBTable table) {
        tables.put(table.getName(), table);
    }

    public void removeTable(DBTable table) {
        tables.remove(table.getName());
    }

}
