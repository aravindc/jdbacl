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

package org.databene.jdbacl.model.pd;

import java.util.List;

import org.databene.jdbacl.model.DBCompositeObjectImpl;
import org.databene.jdbacl.model.DBObject;
import org.databene.jdbacl.model.DBTable;

/**
 * TODO Document class.<br/><br/>
 * Created: 09.11.2010 13:13:28
 * @since TODO version
 * @author Volker Bergmann
 */
public class DBPackage extends DBCompositeObjectImpl<DBObject> {

	private static final long serialVersionUID = -7566813784153247669L;

	public DBPackage(String name) {
		super(name);
	}

	public List<DBPackage> getSubPackages() {
		return getComponents(DBPackage.class, false);
	}
	
	public List<DBTable> getTables(boolean recursive) {
		return getComponents(DBTable.class, recursive);
	}
	
}
