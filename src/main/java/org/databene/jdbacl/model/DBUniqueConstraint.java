/*
 * (c) Copyright 2006-2010 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.ArrayUtil;

/**
 * Represents a unique constraint on one or the combination of several columns of one table.<br/>
 * <br/>
 * Created: 06.01.2007 09:00:37
 * @author Volker Bergmann
 */
public class DBUniqueConstraint extends DBConstraint {

    private static final long serialVersionUID = -8241121848879185421L;
    
	private String[] columnNames;

    /**
     * @param name the constraint name - it may be null
     * @param columnNames the names of the columns to which the constraint applies
     */
    public DBUniqueConstraint(DBTable owner, String name, String... columnNames) {
        super(name, owner);
        this.columnNames = columnNames;
        owner.addUniqueConstraint(this);
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    public void addColumn(String columnName) {
        columnNames = ArrayUtil.append(columnNames, columnName);
    }

    public void removeColumn(String columnName) {
    	columnNames = ArrayUtil.removeElement(columnNames, columnName);
    }
    
}
