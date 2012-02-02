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

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.databene.commons.version.VersionNumber;
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
 * {@link Database} instance which supports lazy loading of database details.<br/><br/>
 * Created: 30.01.2012 11:08:14
 * @since 0.8.0
 * @author Volker Bergmann
 */
@SuppressWarnings("serial")
public class LazyDatabase implements Database {
	
	private Database realDatabase;
	private LazyJDBCDBImporter importer;

	private boolean sequencesImported;
	private boolean triggersImported;
	private boolean packagesImported;
	
	public LazyDatabase(Database realDatabase, LazyJDBCDBImporter importer) {
		this.realDatabase = realDatabase;
		this.importer = importer;
		this.sequencesImported = false;
		this.triggersImported = false;
		this.packagesImported = false;
		for (DBCatalog catalog : realDatabase.getCatalogs())
			catalog.setDatabase(this);
	}
	
	
	
	// CompositeDBObject interface implementation ----------------------------------------------------------------------
	
	public String getEnvironment() {
		return realDatabase.getEnvironment();
	}
	
	public String getName() {
		return realDatabase.getName();
	}

	public String getObjectType() {
		return realDatabase.getObjectType();
	}

	public CompositeDBObject<?> getOwner() {
		return realDatabase.getOwner();
	}

	public void setOwner(CompositeDBObject<?> owner) {
		realDatabase.setOwner(owner);
	}
	
	public String getDoc() {
		return realDatabase.getDoc();
	}
	
	public List<DBCatalog> getComponents() {
		return realDatabase.getComponents();
	}
	
	public boolean isIdentical(DBObject other) {
		return realDatabase.isIdentical(other);
	}

	
	
	// general information ---------------------------------------------------------------------------------------------
	
	public String getDatabaseProductName() {
		return realDatabase.getDatabaseProductName();
	}

	public VersionNumber getDatabaseProductVersion() {
		return realDatabase.getDatabaseProductVersion();
	}

	public String getUser() {
		return realDatabase.getUser();
	}

	public Date getImportDate() {
		return realDatabase.getImportDate();
	}

	public String getTableInclusionPattern() {
		return realDatabase.getTableInclusionPattern();
	}

	public String getTableExclusionPattern() {
		return realDatabase.getTableExclusionPattern();
	}
	
	
	
	// catalogs --------------------------------------------------------------------------------------------------------
	
	public List<DBCatalog> getCatalogs() {
		return realDatabase.getCatalogs();
	}

	public DBCatalog getCatalog(String catalogName) {
		return realDatabase.getCatalog(catalogName);
	}

	public void addCatalog(DBCatalog catalog) {
		realDatabase.addCatalog(catalog);
	}

	public void removeCatalog(DBCatalog catalog) {
		realDatabase.removeCatalog(catalog);
	}
	
	
	
	// schemas ---------------------------------------------------------------------------------------------------------
	
	public DBSchema getSchema(String schemaName) {
		return realDatabase.getSchema(schemaName);
	}
	
	
	
	// tables ----------------------------------------------------------------------------------------------------------
	
	public List<DBTable> getTables(boolean recursive) {
		return realDatabase.getTables(recursive);
	}

	public List<DBTable> getTables() {
		return realDatabase.getTables();
	}

	public DBTable getTable(String name) {
		return realDatabase.getTable(name);
	}

	public DBTable getTable(String name, boolean required) {
		return realDatabase.getTable(name, required);
	}

	public void removeTable(String tableName) {
		realDatabase.removeTable(tableName);
	}
	
	
	
	// sequences -------------------------------------------------------------------------------------------------------
	
	public List<DBSequence> getSequences() {
		haveSequencesImported();
		return realDatabase.getSequences();
	}
	
	public List<DBSequence> getSequences(boolean recursive) {
		haveSequencesImported();
		return realDatabase.getSequences(recursive);
	}
	
	public synchronized void haveSequencesImported() {
		if (!sequencesImported) {
			importer.importSequences();
			this.sequencesImported = true;
		}
	}
	
	public boolean isSequencesImported() {
		return sequencesImported;
	}
	
	public void setSequencesImported(boolean sequencesImported) {
		this.sequencesImported = sequencesImported;
	}
	
	
	
	// triggers --------------------------------------------------------------------------------------------------------

	public List<DBTrigger> getTriggers() {
		haveTriggersImported();
		return realDatabase.getTriggers();
	}
	
	public synchronized void haveTriggersImported() {
		if (!triggersImported) {
			try {
				importer.importTriggers();
				triggersImported = true;
			} catch (SQLException e) {
				throw new RuntimeException("Import of database triggers ailed: " + getName(), e);
			}
		}
	}
	
	public boolean isTriggersImported() {
		return triggersImported;
	}
	
	public void setTriggersImported(boolean triggersImported) {
		this.triggersImported = triggersImported;
	}
	
	
	
	// packages --------------------------------------------------------------------------------------------------------	
	
	public List<DBPackage> getPackages() {
		havePackagesImported();
		return realDatabase.getPackages();
	}

	public synchronized void havePackagesImported() {
		if (!packagesImported) {
			try {
				importer.importPackages();
				packagesImported = true;
			} catch (SQLException e) {
				throw new RuntimeException("Import of database packages failed: " + getName(), e);
			}
		}
	}
	
	public boolean isPackagesImported() {
		return packagesImported;
	}
	
	public void setPackagesImported(boolean packagesImported) {
		this.packagesImported = packagesImported;
	}



}
