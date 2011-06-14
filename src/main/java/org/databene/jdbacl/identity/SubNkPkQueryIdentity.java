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

package org.databene.jdbacl.identity;

import java.sql.Connection;

import org.databene.commons.ArrayFormat;
import org.databene.commons.HeavyweightIterator;
import org.databene.commons.IOUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.iterator.TableRowIterator;
import org.databene.jdbacl.ArrayResultSetIterator;
import org.databene.jdbacl.SQLUtil;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.Database;

/**
 * {@link IdentityModel} for tables which are owned by another table but have a sub identity 
 * (resulting in a one-to-many relationship between parent and child). 
 * Their natural key is composed from the owner row's natural key and a sub key for the row itself.<br/><br/>
 * Created: 01.09.2010 09:24:26
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class SubNkPkQueryIdentity extends IdentityModel {

	IdentityProvider identityProvider; // TODO v0.7 get rid of model dependency
	String parentTableNames[]; // TODO v0.7 support multiple 'parent' and 'parentColumns' property
	String subNkPkQuery;

	public SubNkPkQueryIdentity(IdentityProvider identityProvider, DBTable table, String... parentTableNames) {
	    super(table);
	    this.identityProvider = identityProvider;
	    this.parentTableNames = parentTableNames;
    }
	
	// properties ------------------------------------------------------------------------------------------------------

	public void setSubNkPkQuery(String subNkPkQuery) {
	    this.subNkPkQuery = subNkPkQuery;
    }

	@Override
	public String getDescription() {
		return "Sub identity of tables " + ArrayFormat.format(parentTableNames) + ':' + 
			SystemInfo.getLineSeparator() + subNkPkQuery;
	}

	// implementation of  --------------------------------------------------------------------------------------------
	
    public TableRowIterator createNkPkIterator(Connection connection, String dbId, KeyMapper mapper) {
		return new RecursiveIterator(connection, dbId, mapper);
    }
    
    private DBTable getParentTable() {
		Database database = table.getCatalog().getDatabase();
		if (parentTableNames.length > 1)
			throw new UnsupportedOperationException("Multi-owner support not yet implemented"); // TODO v0.7 support multiple parents
		return database.getTable(parentTableNames[0]);
	}

	IdentityModel getOwnerIdentity() {
		if (parentTableNames.length > 1)
			throw new UnsupportedOperationException("Multi-owner support not yet implemented"); // TODO v0.7 support multiple parents
        return identityProvider.getIdentity(parentTableNames[0]);
    }

    // helper class for recursive iteration ----------------------------------------------------------------------------

    public class RecursiveIterator implements TableRowIterator {
    	
    	Connection connection;
    	String dbId;
    	KeyMapper mapper;
    	HeavyweightIterator<Object> ownerPkIterator;
    	String ownerNK;
    	TableRowIterator subNkPkIterator;

	    public RecursiveIterator(Connection connection, String dbId, KeyMapper mapper) {
	        this.connection = connection;
	        this.dbId = dbId;
	        this.mapper = mapper;
	        ownerPkIterator = getParentTable().queryPKs(connection);
	        createSubNkPkIterator(connection, dbId);
        }

		public boolean hasNext() {
			if (subNkPkIterator.hasNext())
				return true;
	    	while (subNkPkIterator != null && !subNkPkIterator.hasNext() && ownerPkIterator.hasNext()) {
	    		IOUtil.close(subNkPkIterator);
	    		createSubNkPkIterator(connection, dbId);
	    	}
	    	return (subNkPkIterator != null && subNkPkIterator.hasNext());
	    }

	    public Object[] next() {
	    	Object[] result = (Object[]) subNkPkIterator.next();
	    	result[0] = ownerNK + '|' + result[0];
	    	return result;
	    }

		public String[] getColumnLabels() {
			return subNkPkIterator.getColumnLabels();
		}

	    public void remove() {
		    throw new UnsupportedOperationException(getClass() + " does not support removal");
	    }

	    public void close() {
	    	IOUtil.close(subNkPkIterator);
		    IOUtil.close(ownerPkIterator);
	    }

		private void createSubNkPkIterator(Connection connection, String dbId) {
	        if (ownerPkIterator.hasNext()) {
	        	Object ownerPk = ownerPkIterator.next();
	        	ownerNK = mapper.getNaturalKey(dbId, getOwnerIdentity(), ownerPk); // TODO v0.7 support multiple owners
	        	if (ownerNK == null)
	        		throw new InvalidIdentityDefinitionError(table.getName() + " row with PK " + ownerPk + " cannot be found. Most likely this is a subsequent fault of a parent's identity definition: " + ArrayFormat.format(parentTableNames));
	        	String query = SQLUtil.substituteMarkers(subNkPkQuery, "?", ownerPk);
	        	subNkPkIterator = new ArrayResultSetIterator(connection, query);
	        } else
	        	subNkPkIterator = null;
        }

    }

}