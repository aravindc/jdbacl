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
import org.databene.commons.ObjectNotFoundException;

/**
 * Represents a foreign key constraint.<br/><br/>
 * Created: 06.01.2007 09:00:59
 * @author Volker Bergmann
 */
public class DBForeignKeyConstraint extends DBConstraint {

    private static final long serialVersionUID = -7488054587082654132L;
    
    private String[] fkColumnNames;
    
    private DBTable refereeTable;
    private String[] refereeColumnNames;
    

    public DBForeignKeyConstraint(String name, DBTable owner, DBTable refereeTable) {
        this(name, owner, null, refereeTable, null);
    }

    public DBForeignKeyConstraint(String name, DBTable owner, String[] fkColumnNames, 
    		DBTable refereeTable, String[] refereeColumnNames) {
        super(owner, name);
        this.fkColumnNames = fkColumnNames;
        this.refereeTable = refereeTable;
        this.refereeColumnNames = refereeColumnNames;
    }

    public String[] getForeignKeyColumnNames() {
        return fkColumnNames;
    }

    public void addForeignKeyColumn(String fkColumn, String refereeColumn) {
        this.fkColumnNames = ArrayUtil.append(fkColumnNames, fkColumn);
        this.refereeColumnNames = ArrayUtil.append(refereeColumnNames, refereeColumn);
    }
    
    public String columnReferencedBy(String fkColumnName) {
    	return columnReferencedBy(fkColumnName, true);
    }

    public String columnReferencedBy(String fkColumnName, boolean required) {
    	int index = ArrayUtil.indexOf(fkColumnName, fkColumnNames);
    	if (index < 0) {
    		if (required)
    			throw new ObjectNotFoundException("foreign key '" + name + "' does not have a column '" + fkColumnName + "'");
    		else
    			return null;
    	}
    	return refereeColumnNames[index];
    }

    public DBTable getForeignTable() { // TODO rename to getRefereeTable()
        return refereeTable;
    }

    @Override
    public String[] getColumnNames() {
    	return fkColumnNames;
    }

	public String[] getRefereeColumnNames() {
		return refereeColumnNames;
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder("(");
		builder.append(fkColumnNames[0]);
    	for (int i = 1; i < fkColumnNames.length; i++)
    		builder.append(", ").append(fkColumnNames[i]);
    	builder.append(") -> ").append(refereeTable.getName()).append(" (");
		builder.append(refereeColumnNames[0]);
    	for (int i = 1; i < refereeColumnNames.length; i++)
    		builder.append(", ").append(refereeColumnNames[i]);
        return builder.append(")").toString();
    }

}
