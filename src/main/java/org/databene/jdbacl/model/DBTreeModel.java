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

import org.databene.commons.TreeModel;

/**
 * TODO Document class.<br/><br/>
 * Created: 10.11.2010 10:17:14
 * @since TODO version
 * @author Volker Bergmann
 */
public class DBTreeModel implements TreeModel<DBObject> {

	DBCompositeObject<?> root;
	
	public DBTreeModel(DBCompositeObject<?> root) {
		this.root = root;
	}

	public DBObject getRoot() {
		return root;
	}

	public DBObject getParent(DBObject child) {
		return child.getOwner();
	}

	public boolean isLeaf(DBObject node) {
		return !(node instanceof DBCompositeObject);
	}

	public int getChildCount(DBObject parent) {
		return ((DBCompositeObject<?>) parent).getComponents().size();
	}

	public DBObject getChild(DBObject parent, int index) {
		return ((DBCompositeObject<?>) parent).getComponents().get(index);
	}

	public int getIndexOfChild(DBObject parent, DBObject child) {
		return ((DBCompositeObject<?>) parent).getComponents().indexOf(child);
	}

}
