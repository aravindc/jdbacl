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

import java.util.Iterator;
import java.util.List;

/**
 * Abstract implementation of the {@link CompositeDBObject} interface which serves as parent class
 * for individual implementations.<br/><br/>
 * Created: 09.11.2010 11:47:43
 * @since 0.6.4
 * @author Volker Bergmann
 */
public abstract class AbstractCompositeDBObject<C extends DBObject> extends AbstractDBObject implements CompositeDBObject<C>  {

	private static final long serialVersionUID = 4823482587175647368L;
	
    // constructors ----------------------------------------------------------------------------------------------------

    public AbstractCompositeDBObject(String name, String type) {
    	this(name, type, null);
    }

    public AbstractCompositeDBObject(String name, String type, CompositeDBObject<?> owner) {
    	super(name, type, owner);
    }

	public boolean isIdentical(DBObject other) {
		if (this == other)
			return true;
		if (other == null || !(this.getObjectType().equals(other.getObjectType())))
			return false;
		List<? extends DBObject> thisComponents = this.getComponents();
		List<? extends DBObject> otherComponents = ((CompositeDBObject<?>) other).getComponents();
		if (thisComponents.size() != otherComponents.size())
			return false;
		Iterator<C> componentIterator = this.getComponents().iterator();
		for (int i = 0; i < thisComponents.size(); i++) {
			C component = componentIterator.next();
			DBObject otherComponent = otherComponents.get(i);
			if (!component.isIdentical(otherComponent))
				return false;
		}
		return true;
	}
	
}
