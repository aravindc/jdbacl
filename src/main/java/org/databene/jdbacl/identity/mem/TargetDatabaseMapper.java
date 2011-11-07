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

package org.databene.jdbacl.identity.mem;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.databene.jdbacl.identity.IdentityModel;
import org.databene.jdbacl.identity.KeyMapper;
import org.databene.jdbacl.model.Database;

/**
 * In-memory implementation of the mapping functionality needed for a target database.<br/><br/>
 * Created: 24.08.2010 11:15:53
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class TargetDatabaseMapper {

	KeyMapper root;
	Connection target;
	String targetDbId;
	Database database;
	Map<String, TargetTableMapper> tableMappers;
	
	public TargetDatabaseMapper(KeyMapper root, Connection target, String targetDbId, Database database) {
		this.root = root;
		this.target = target;
		this.targetDbId = targetDbId;
		this.database = database;
		tableMappers = new HashMap<String, TargetTableMapper>(500);
    }
	
	// interface -------------------------------------------------------------------------------------------------------
	
	public String getDbId() {
		return targetDbId;
	}
	
	public void store(IdentityModel identity, String naturalKey, Object targetPK) {
		getOrCreateTableMapper(target, targetDbId, identity).store(targetPK, naturalKey);
	}
	
	public String getNaturalKey(IdentityModel identity, Object sourcePK) {
		return getOrCreateTableMapper(target, targetDbId, identity).getNaturalKey(sourcePK);
    }
	
	public Object getTargetPK(IdentityModel identity, String naturalKey) {
		return getOrCreateTableMapper(target, targetDbId, identity).getTargetId(naturalKey);
	}
	
	// helpers ---------------------------------------------------------------------------------------------------------
	
	private TargetTableMapper getOrCreateTableMapper(Connection target, String targetDbId, IdentityModel identity) {
		String tableName = identity.getTableName();
		TargetTableMapper result = tableMappers.get(tableName);
		if (result == null) {
			result = new TargetTableMapper(root, target, targetDbId, identity, database);
			tableMappers.put(tableName, result);
		}
		return result;
	}

}
