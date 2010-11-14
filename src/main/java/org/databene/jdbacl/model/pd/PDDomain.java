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

import org.databene.jdbacl.model.DBColumnType;
import org.databene.jdbacl.model.DBObjectImpl;

/**
 * TODO Document class.<br/><br/>
 * Created: 10.11.2010 00:31:49
 * @since TODO version
 * @author Volker Bergmann
 */
public class PDDomain extends DBObjectImpl {

    private static final long serialVersionUID = 8006145564829570491L;
    
	private DBColumnType type;
    private Integer size;
    private Integer fractionDigits;
    private String defaultValue;
    private boolean nullable;

    // constructors ----------------------------------------------------------------------------------------------------

    public PDDomain(PDModel model, String name, DBColumnType type, Integer size, Integer fractionDigits) {
    	super(name);
        this.owner = model;
        model.addComponent(this);
        this.name = name;
        this.type = type;
        this.size = size;
        this.fractionDigits = fractionDigits;
        this.doc = null;
        this.defaultValue = null;
        this.nullable = true;
    }

    // properties ------------------------------------------------------------------------------------------------------

    public PDModel getModel() {
        return (PDModel) getOwner();
    }

    public DBColumnType getType() {
        return type;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getFractionDigits() {
        return fractionDigits;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

    public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

    // java.lang.Object overrides --------------------------------------------------------------------------------------

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder(name).append(" : ").append(type);
        if (size != null) {
            builder.append('(');
            builder.append(size);
            if (fractionDigits != null)
                builder.append(",").append(fractionDigits);
            builder.append(')');
        }
        if (!isNullable())
            builder.append(" NOT NULL");
        return builder.toString();
    }

}
