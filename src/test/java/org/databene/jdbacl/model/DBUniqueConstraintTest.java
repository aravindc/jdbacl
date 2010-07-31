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

import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBColumnType;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the DBUniqueConstraint<br/>
 * <br/>
 * Created: 31.08.2007 09:22:25
 * @author Volker Bergmann
 */
public class DBUniqueConstraintTest {

	@Test
    public void testToString() {
        DBColumnType blobType = DBColumnType.getInstance("blob");
        DBTable table = new DBTable("tablename");
        DBColumn column1 = new DBColumn(table, "column1", blobType, null, null);
        DBColumn column2 = new DBColumn(table, "column2", blobType, null, null);
        DBUniqueConstraint constraint = new DBUniqueConstraint("constraintname", column1, column2);
        assertEquals("DBUniqueConstraint[tablename[column1, column2]]", constraint.toString());
    }
    
	@Test
    public void testEquals() {
        DBTable table = new DBTable("tablename");
        DBColumnType charType = DBColumnType.getInstance("char");
    	DBColumn col1 = new DBColumn(table, "col1", charType, 0, 0);
    	DBColumn col2 = new DBColumn(table, "col2", charType, 0, 0);
    	// simple checks
    	DBUniqueConstraint uc1 = new DBUniqueConstraint("uc1", col1);
    	assertFalse(uc1.equals(null));
    	assertFalse(uc1.equals(""));
    	assertTrue(uc1.equals(uc1));
    	// real comparisons
    	DBUniqueConstraint uc2 = new DBUniqueConstraint("uc2", col2);
    	DBUniqueConstraint uc3 = new DBUniqueConstraint("uc3", col1, col2);
    	assertFalse(uc1.equals(uc2));
    	assertFalse(uc1.equals(uc3));
    	assertFalse(uc3.equals(uc1));
    }
	
}
