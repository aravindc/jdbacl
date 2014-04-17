/*
 * (c) Copyright 2006-2014 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General License.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.databene.commons.collection.OrderedNameMap;

/**
 * Represents a JDBC database schema.<br/><br/>
 * Created: 06.01.2007 08:57:57
 * @author Volker Bergmann
 */
public class DBSchema extends AbstractCompositeDBObject<DBObject> implements TableHolder, SequenceHolder, Serializable {

	private static final long serialVersionUID = -8481832064630225273L;
	
	private List<DBObject> components;
	private OrderedNameMap<DBTable> tables;
	private OrderedNameMap<DBSequence> sequences;
    private OrderedNameMap<DBTrigger> triggers;
    private OrderedNameMap<DBPackage> packages;
    
    // constructors ----------------------------------------------------------------------------------------------------

    public DBSchema(String name) {
        this(name, null);
    }

    public DBSchema(String name, DBCatalog catalog) {
    	super(name, "schema");
    	if (catalog != null)
    		catalog.addSchema(this);
		this.components = new ArrayList<DBObject>();
		this.tables = OrderedNameMap.createCaseIgnorantMap();
		this.sequences = OrderedNameMap.createCaseIgnorantMap();
    	this.triggers = OrderedNameMap.createCaseIgnorantMap();
    	this.packages = OrderedNameMap.createCaseIgnorantMap();
    }

    // properties ------------------------------------------------------------------------------------------------------

    @Override
	public String getName() {
        return name;
    }

    public Database getDatabase() {
        return getCatalog().getDatabase();
    }
    
    // catalog operations ----------------------------------------------------------------------------------------------

    public DBCatalog getCatalog() {
        return (DBCatalog) owner;
    }

    public void setCatalog(DBCatalog catalog) {
        this.owner = catalog;
    }

    // CompositeDBObject implementation --------------------------------------------------------------------------------

	@Override
	public List<DBObject> getComponents() {
		return components;
	}
	
    // table operations ------------------------------------------------------------------------------------------------

    @Override
	public List<DBTable> getTables() {
		return tables.values();
    }

	@Override
	public List<DBTable> getTables(boolean recursive) {
		return getTables();
	}
	
    @Override
	public DBTable getTable(String tableName) {
        return tables.get(tableName);
    }

    public void addTable(DBTable table) {
        tables.put(table.getName(), table);
        components.add(table);
    }

    public void removeTable(DBTable table) {
        tables.remove(table.getName());
        components.remove(table);
    }
    
    // sequence operations ---------------------------------------------------------------------------------------------

	@Override
	public List<DBSequence> getSequences(boolean recursive) {
		getDatabase().haveSequencesImported();
		return sequences.values();
	}
	
    public void addSequence(DBSequence sequence) {
		getDatabase().haveSequencesImported();
    	receiveSequence(sequence);
    }
    
	public void receiveSequence(DBSequence sequence) {
		this.sequences.put(sequence.getName(), sequence);
    	components.add(sequence);
	}

	// trigger operations ----------------------------------------------------------------------------------------------

	public List<DBTrigger> getTriggers() {
		getDatabase().haveTriggersImported();
		return triggers.values();
	}
	
	public void addTrigger(DBTrigger trigger) {
		getDatabase().haveTriggersImported();
		receiveTrigger(trigger);
	}
	
	public void receiveTrigger(DBTrigger trigger) {
		this.triggers.put(trigger.getName(), trigger);
		components.add(trigger);
	}

	// package operations ----------------------------------------------------------------------------------------------

	public List<DBPackage> getPackages() {
		getDatabase().havePackagesImported();
		return packages.values();
	}
	
	public void addPackage(DBPackage pkg) {
		getDatabase().havePackagesImported();
		receivePackage(pkg);
	}

	public void receivePackage(DBPackage pkg) {
		packages.put(pkg.getName(), pkg);
		components.add(pkg);
	}

}
