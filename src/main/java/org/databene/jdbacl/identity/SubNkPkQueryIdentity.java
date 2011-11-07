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

package org.databene.jdbacl.identity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;

import org.databene.commons.ArrayFormat;
import org.databene.commons.HeavyweightIterator;
import org.databene.commons.IOUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.iterator.ConvertingIterator;
import org.databene.commons.iterator.TableRowIterator;
import org.databene.jdbacl.ArrayResultSetIterator;
import org.databene.jdbacl.QueryIterator;
import org.databene.jdbacl.ResultSetConverter;
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

	private String parentTableNames[]; // TODO v0.7 support multiple 'parent' and 'parentColumns' property
	private String subNkPkQuery;
	private IdentityProvider identityProvider;

	public SubNkPkQueryIdentity(String tableName, String[] parentTableNames, IdentityProvider identityProvider) {
	    super(tableName);
	    this.parentTableNames = parentTableNames;
	    this.identityProvider = identityProvider;
    }
	
	// properties ------------------------------------------------------------------------------------------------------

	public void setSubNkPkQuery(String subNkPkQuery) {
	    this.subNkPkQuery = subNkPkQuery;
    }

	@Override
	public String getDescription() {
		return "Sub identity of (" + ArrayFormat.format(parentTableNames) + "):" + 
			SystemInfo.getLineSeparator() + subNkPkQuery;
	}

	// implementation --------------------------------------------------------------------------------------------------
	
    public TableRowIterator createNkPkIterator(Connection connection, String dbId, KeyMapper mapper, Database database) {
		return new RecursiveIterator(connection, dbId, mapper, database);
    }
    
    // helper class for recursive iteration ----------------------------------------------------------------------------

    public class RecursiveIterator implements TableRowIterator {
    	
    	Connection connection;
    	String dbId;
    	KeyMapper mapper;
    	HeavyweightIterator<Object> ownerPkIterator;
    	String ownerNK;
    	TableRowIterator subNkPkIterator;

	    public RecursiveIterator(Connection connection, String dbId, KeyMapper mapper, Database database) {
	        this.connection = connection;
	        this.dbId = dbId;
	        this.mapper = mapper;
	        ownerPkIterator = createParentPkIterator(connection, database); // TODO v0.7 support multiple parents
	        createSubNkPkIterator(connection, dbId);
        }

		protected HeavyweightIterator<Object> createParentPkIterator(Connection connection, Database database) {
			DBTable parentTable = database.getTable(parentTableNames[0]);
			StringBuilder query = new StringBuilder("select ");
			query.append(ArrayFormat.format(parentTable.getPKColumnNames()));
			query.append(" from ").append(parentTable);
	    	Iterator<ResultSet> rawIterator = new QueryIterator(query.toString(), connection, 100);
	        ResultSetConverter<Object> converter = new ResultSetConverter<Object>(Object.class, true);
	    	return new ConvertingIterator<ResultSet, Object>(rawIterator, converter);
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
	        	ownerNK = mapper.getNaturalKey(dbId, identityProvider.getIdentity(parentTableNames[0]), ownerPk); // TODO v0.7 support multiple owners
	        	if (ownerNK == null)
	        		throw new InvalidIdentityDefinitionError(tableName + " row with PK " + ownerPk + 
	        				" cannot be found. Most likely this is a subsequent fault of a parent's identity" +
	        				" definition: " + ArrayFormat.format(parentTableNames));
	        	String query = SQLUtil.substituteMarkers(subNkPkQuery, "?", ownerPk);
	        	subNkPkIterator = new ArrayResultSetIterator(connection, query);
	        } else
	        	subNkPkIterator = null;
        }

    }

}