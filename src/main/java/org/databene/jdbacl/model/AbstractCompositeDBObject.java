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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.databene.commons.OrderedMap;

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

    public AbstractCompositeDBObject(String name) {
    	this(name, null);
    }

    public AbstractCompositeDBObject(String name, CompositeDBObject<?> owner) {
    	super(name, owner);
    }

    // sub component operations ----------------------------------------------------------------------------------------
/*
	public List<C> getComponents() {
		List<C> result = new ArrayList<C>();
		for (Map<String, C> map : components.values())
			result.addAll(map.values());
        return result;
    }

    public C getComponent(Class<? extends DBObject> type, String name) {
        Map<String, C> typeMap = components.get(type);
        return (typeMap != null ? typeMap.get(name) : null);
    }

	public <T extends DBObject> List<T> getComponents(Class<T> type, boolean recursive) {
		return addSubComponents(type, recursive, new ArrayList<T>());
	}
	
    public void addComponent(C component) {
        component.setOwner(this);
        Map<String, C> typeMap = components.get(component.getClass());
        typeMap.put(component.getName(), component);
    }

    public void removeComponent(C component) {
        components.remove(component.getName());
        component.setOwner(null);
    }

	@SuppressWarnings("unchecked")
	private <T extends DBObject> List<T> addSubComponents(Class<T> type, boolean recursive, List<T> result) {
		for (DBObject component : getComponents()) {
			if (type.isAssignableFrom(component.getClass()))
				result.add((T) component);
			if (recursive && component instanceof CompositeDBObject)
				addSubComponents(type, true, result);
		}
		return result;
	}
*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean deepEquals(CompositeDBObject<?> other) {
		if (!this.equals(other))
			return false;
		List<? extends DBObject> thisComponents = this.getComponents();
		List<? extends DBObject> otherComponents = other.getComponents();
		if (thisComponents.size() != otherComponents.size())
			return false;
		Iterator<C> componentIterator = this.getComponents().iterator();
		for (int i = 0; i < thisComponents.size(); i++) {
			C component = componentIterator.next();
			DBObject otherComponent = otherComponents.get(i);
			if ((component instanceof CompositeDBObject)) {
				if (!(otherComponent instanceof CompositeDBObject))
					return false;
				if (!((CompositeDBObject) component).deepEquals((CompositeDBObject) otherComponent))
					return false;
			} else if (!component.equals(otherComponent))
				return false;
		}
		return true;
	}
	
}