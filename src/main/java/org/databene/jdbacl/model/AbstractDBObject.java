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

import org.databene.commons.NullSafeComparator;

/**
 * Abstract implementation of the {@link DBObject} interface which serves as 
 * parent class for concrete implementations.<br/><br/>
 * Created: 09.11.2010 11:45:20
 * @since 0.6.4
 * @author Volker Bergmann
 */
public abstract class AbstractDBObject implements DBObject {

	private static final long serialVersionUID = -9344600767967258L;
	
	protected String name;
	protected String objectType;
	protected String doc;
	protected CompositeDBObject<?> owner;
	
	// constructors ----------------------------------------------------------------------------------------------------
    
	public AbstractDBObject(String name, String objectType) {
		this(name, objectType, null);
	}

	@SuppressWarnings({ "rawtypes"})
	public AbstractDBObject(String name, String objectType, CompositeDBObject owner) {
		this.name = name;
		this.objectType = objectType;
		setOwner(owner); // allow child classes to do additional work when setting the owner
	}

    // properties ------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getObjectType() {
    	return objectType;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public CompositeDBObject<?> getOwner() {
		return owner;
	}

	public void setOwner(CompositeDBObject<?> owner) {
		this.owner = owner;
	}
	
	// java.lang.Object overrides --------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !DBObject.class.isAssignableFrom(obj.getClass()))
			return false;
		DBObject that = (DBObject) obj;
		return NullSafeComparator.equals(this.name, that.getName()) && 
			NullSafeComparator.equals(this.objectType, that.getObjectType()) &&
			NullSafeComparator.equals(this.owner, that.getOwner());
	}
	
}
