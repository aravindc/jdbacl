/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
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

/**
 * Represents a database package.<br/><br/>
 * Created: 05.12.2010 11:06:48
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class DBPackage extends AbstractCompositeDBObject<DBPackageComponent> implements DBPackageComponent, TableHolder, SequenceHolder {

    private static final long serialVersionUID = 5890222751656809426L;
    
    PackageAndTableSupport support;
    
    // constructors ----------------------------------------------------------------------------------------------------

    public DBPackage(String name) {
        this(name, null);
    }

    public DBPackage(String name, CompositeDBObject<? extends DBObject> parent) {
    	super(name, "package");
    	if (parent instanceof DBPackage)
			((DBPackage) parent).addPackage(this);
    	this.support = new PackageAndTableSupport();
    }

    private void addPackage(DBPackage dbPackage) {
		support.addPackage(dbPackage);
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
		result.addAll(support.getTables());
		result.addAll(support.getPackages());
		return result;
	}
	
    // table operations ------------------------------------------------------------------------------------------------

    public List<DBTable> getTables() {
        return support.getTables();
    }

    public List<DBTable> getTables(boolean recursive) {
		return support.getTables(recursive);
    }

    public List<DBTable> getTables(boolean recursive, List<DBTable> result) {
    	return support.getTables(recursive, result);
    }

    public DBTable getTable(String tableName) {
        return support.getTable(tableName);
    }

    public void addTable(DBTable table) {
        support.addTable(table);
    }

    public void removeTable(DBTable table) {
        support.removeTable(table);
    }

    // sequence operations ---------------------------------------------------------------------------------------------
    
	public List<DBSequence> getSequences(boolean recursive) {
		return support.getSequences(recursive);
	}

	public List<DBSequence> getSequences(boolean recursive, List<DBSequence> result) {
		return support.getSequences(recursive, result);
	}

}
