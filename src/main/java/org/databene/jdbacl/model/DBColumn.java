/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
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

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.databene.commons.ArrayUtil;
import org.databene.commons.Named;

/**
 * Represents a database column.<br/><br/>
 * Created: 06.01.2007 08:58:49
 * @author Volker Bergmann
 */
public class DBColumn extends DBObjectImpl implements Named, Serializable {

	private static final long serialVersionUID = 5693941485232520002L;
	
    private DBColumnType type;
    private Integer size;
    private Integer fractionDigits;
    private String defaultValue;
    private boolean versionColumn;

    private List<DBUniqueConstraint> ukConstraints; // constraints may be unnamed, so a Map does not make sense
    private DBConstraint notNullConstraint;
//    private DBForeignKeyConstraint fkConstraint;

    // constructors ----------------------------------------------------------------------------------------------------

    public DBColumn() {
        this(null, null);
    }

    public DBColumn(String name, DBColumnType type) {
        this(name, type, null);
    }

    public DBColumn(String name, DBColumnType type, Integer size) {
        this(name, type, size, null);
    }
    
    public DBColumn(String name, DBColumnType type, Integer size, Integer fractionDigits) {
        this(null, name, type, size, fractionDigits);
    }

    public DBColumn(DBTable table, String name, DBColumnType type, Integer size, Integer fractionDigits) {
    	super(name);
        setTable(table);
        this.name = name;
        this.type = type;
        this.size = size;
        this.fractionDigits = fractionDigits;
        this.doc = null;
        this.defaultValue = null;
        this.ukConstraints = new ArrayList<DBUniqueConstraint>();
        this.notNullConstraint = null;
//        this.fkConstraint = null;
        this.versionColumn = false;
    }

    // properties ------------------------------------------------------------------------------------------------------

    public DBTable getTable() {
        return (DBTable) getOwner();
    }

    public void setTable(DBTable table) {
        setOwner(table);
        if (table != null)
        	table.addComponent(this);
    }

    public DBColumnType getType() {
        return type;
    }

    public void setType(DBColumnType type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getFractionDigits() {
        return fractionDigits;
    }

    public void setFractionDigits(Integer fractionDigits) {
        this.fractionDigits = fractionDigits;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isUnique() {
    	for (DBUniqueConstraint constraint : ukConstraints)
    		if (constraint.getColumnNames().length == 1)
    			return true;
    	return false;
    }
    
    public List<DBUniqueConstraint> getUkConstraints() {
        return ukConstraints;
    }

    public void addUkConstraint(DBUniqueConstraint constraint) {
        this.ukConstraints.add(constraint);
    }

    public DBConstraint getNotNullConstraint() {
        return notNullConstraint;
    }

    public void setNotNullConstraint(DBConstraint notNullConstraint) {
        this.notNullConstraint = notNullConstraint;
    }

    public boolean isNullable() {
        return (notNullConstraint == null);
    }

    public void setNullable(boolean nullable) {
        if (nullable) {
            // if a NotNullConstraint exists then remove it
            notNullConstraint = null;
        } else {
            // if there needs to be a NotNullConstraint, check if there exists one, first
            if (this.isNullable()) {
                this.notNullConstraint = new DBNotNullConstraint(getTable(), null, name);
            }
        }
    }

    public boolean isVersionColumn() {
        return versionColumn;
    }

    public void setVersionColumn(boolean versionColumn) {
        this.versionColumn = versionColumn;
    }

	public boolean isForeignKeyComponent() {
		for (DBForeignKeyConstraint fk : getTable().getForeignKeyConstraints())
			if (ArrayUtil.contains(fk.getColumnNames(), name))
				return true;
	    return false;
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
