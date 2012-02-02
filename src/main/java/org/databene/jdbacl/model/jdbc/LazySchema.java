/*
 * (c) Copyright 2012 by Volker Bergmann. All rights reserved.
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

import java.util.List;

import org.databene.jdbacl.model.CompositeDBObject;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBObject;
import org.databene.jdbacl.model.DBPackage;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBTrigger;
import org.databene.jdbacl.model.Database;

/**
 * TODO Document class.<br/><br/>
 * Created: 02.02.2012 14:19:01
 * @since TODO version
 * @author Volker Bergmann
 */
public class LazySchema implements DBSchema {
	
	private static final long serialVersionUID = -9060481922696115129L;
	
	private DBSchema realSchema;
	
    public LazySchema(DBSchema realSchema) {
		this.realSchema = realSchema;
    	DBCatalog catalog = realSchema.getCatalog();
    	catalog.removeSchema(realSchema);
    	catalog.addSchema(this);
		realSchema.setOwner(catalog);
	}

	DBSchema getRealSchema() {
		return realSchema;
	}

	public Database getDatabase() {
        return realSchema.getDatabase();
    }
    
    public DBCatalog getCatalog() {
        return realSchema.getCatalog();
    }

    // CompositeDBObject implementation --------------------------------------------------------------------------------

	public List<DBObject> getComponents() {
		return realSchema.getComponents();
	}

	public String getObjectType() {
		return realSchema.getObjectType();
	}

	public String getName() {
		return realSchema.getName();
	}

	public String getDoc() {
		return realSchema.getDoc();
	}
	
	public boolean isIdentical(DBObject other) {
		return realSchema.isIdentical(other);
	}
	
	
	
    // table operations ------------------------------------------------------------------------------------------------

	public List<DBTable> getTables() {
		return realSchema.getTables();
	}

	public CompositeDBObject<?> getOwner() {
		return realSchema.getOwner();
	}

	public List<DBTable> getTables(boolean recursive) {
		return realSchema.getTables(recursive);
	}

	public DBTable getTable(String tableName) {
		return realSchema.getTable(tableName);
	}

	public void addTable(DBTable table) {
		realSchema.addTable(table);
	}

	public void removeTable(DBTable table) {
		realSchema.removeTable(table);
	}

	public List<DBSequence> getSequences(boolean recursive) {
		getLazyDatabase().haveSequencesImported();
		return realSchema.getSequences(recursive);
	}

	public void addSequence(DBSequence sequence) {
		getLazyDatabase().haveSequencesImported();
		realSchema.addSequence(sequence);
	}

	public void setOwner(CompositeDBObject<?> owner) {
		realSchema.setOwner(owner);
	}

	public List<DBTrigger> getTriggers() {
		getLazyDatabase().haveTriggersImported();
		return realSchema.getTriggers();
	}

	public void addTrigger(DBTrigger trigger) {
		getLazyDatabase().haveTriggersImported();
		realSchema.addTrigger(trigger);
	}

	public void addPackage(DBPackage dbPackage) {
		getLazyDatabase().havePackagesImported();
		realSchema.addPackage(dbPackage);
	}
	
	public List<DBPackage> getPackages() {
		getLazyDatabase().havePackagesImported();
		return realSchema.getPackages();
	}
	
	private LazyDatabase getLazyDatabase() {
		return (LazyDatabase) getCatalog().getDatabase();
	}
	
}
