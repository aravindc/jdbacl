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
 * In-memory implementation of the mapping functionality needed for target database tables.<br/><br/>
 * Created: 24.08.2010 11:13:42
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class TargetTableMapper extends AbstractTableMapper {

	Map<String, Object> nkToPk;

	public TargetTableMapper(KeyMapper root, Connection target, String targetDbId, IdentityModel identity, Database database) {
		super(root, target, targetDbId, identity, database);
	    this.nkToPk = new HashMap<String, Object>(1000);
    }

	@Override
	public Object store(Object targetPK, String naturalKey) {
	    super.store(targetPK, naturalKey);
		return nkToPk.put(naturalKey, targetPK);
	}
	
	public Object getTargetId(String naturalKey) {
		assureInitialized();
		return nkToPk.get(naturalKey);
	}

}
