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

import java.util.List;

/**
 * Represents a database column.<br/><br/>
 * Created: 06.01.2007 08:58:49
 * @author Volker Bergmann
 */
public interface DBColumn extends DBTableComponent {
	
    DBColumnType getType();
    void setType(DBColumnType type);
    
    Integer getSize();
    void setSize(Integer size);
    
    Integer getFractionDigits();
    void setFractionDigits(Integer fractionDigits);
    
    String getDefaultValue();
    void setDefaultValue(String defaultValue);
    
    boolean isUnique();
    void setUnique(boolean unique);
    
    List<DBUniqueConstraint> getUkConstraints();
    void addUkConstraint(DBUniqueConstraint constraint);
    
    DBNotNullConstraint getNotNullConstraint();
    void setNotNullConstraint(DBNotNullConstraint constraint);
    
    boolean isNullable();
    void setNullable(boolean nullable);
    
    boolean isVersionColumn();
    void setVersionColumn(boolean versionColumn);
	
	DBForeignKeyConstraint getForeignKeyConstraint();
}