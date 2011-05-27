/*
 * (c) Copyright 2006-2011 by Volker Bergmann. All rights reserved.
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
import java.sql.Types;
import java.util.Set;

import org.databene.commons.CollectionUtil;
import org.databene.commons.Named;

/**
 * Represents a database column type.<br/><br/>
 * Created: 06.01.2007 10:12:29
 * @author Volker Bergmann
 */
public class DBColumnType implements Named, Serializable {

    private static final long serialVersionUID = 7725335502838132325L;
    
    private static final Set<Integer> ALPHA_TYPES = CollectionUtil.toSet(
    	Types.CHAR, Types.CLOB, Types.LONGVARCHAR, Types.LONGNVARCHAR, Types.NCHAR, Types.NCLOB, Types.NVARCHAR, Types.VARCHAR
    );

    private static final Set<Integer> NUMBER_TYPES = CollectionUtil.toSet(
        	Types.BIGINT, Types.DECIMAL, Types.DOUBLE, Types.FLOAT, Types.INTEGER, Types.NUMERIC, Types.SMALLINT, Types.TINYINT
        );

	public static DBColumnType getInstance(int jdbcType, String name) {
        return new DBColumnType(jdbcType, name.toUpperCase());
    }

    private String name;
    private int jdbcType;

    // constructors ----------------------------------------------------------------------------------------------------

    private DBColumnType(int sqlType, String name) {
    	if (name.equals("NCLOB"))
    		sqlType = Types.NCLOB;
        this.jdbcType = sqlType;
        this.name = name.toUpperCase();
//        instances.put(this.name, this);
    }

// properties ------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public boolean isLOB() {
        return jdbcType == Types.BLOB || jdbcType == Types.CLOB || jdbcType == Types.NCLOB || 
        	name.endsWith("CLOB") || "BLOB".equals(name);
    }

    public boolean isAlpha() {
    	if (ALPHA_TYPES.contains(jdbcType)) // standard types
    		return true;
        return name.endsWith("VARCHAR2") || name.endsWith("CLOB"); // fixes for Oracle
    }

	public boolean isNumber() {
        return NUMBER_TYPES.contains(jdbcType);
	}

	public boolean isTemporal() {
	    return jdbcType == Types.DATE || 
	    	jdbcType == Types.TIMESTAMP || 
	    	name.contains("DATE") || 
	    	name.contains("TIME");
    }

// java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final DBColumnType that = (DBColumnType) o;
        return name.equals(that.name);
    }


    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
