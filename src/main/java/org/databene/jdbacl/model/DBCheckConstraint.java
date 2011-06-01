/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

/**
 * TODO Document class.<br/><br/>
 * Created: 01.06.2011 12:40:30
 * @since TODO version
 * @author Volker Bergmann
 */
public class DBCheckConstraint extends DBConstraint implements MultiColumnObject {
	
	private static final long serialVersionUID = 3766067048212751458L;
	
	private String tableName;
	private String condition;

	public DBCheckConstraint(String name, String tableName, String condition) {
		super(name, "check constraint", null);
		this.tableName = tableName;
		this.condition = condition;
		// TODO parse condition
	}

	public DBCheckConstraint(String name, DBTable owner, String condition) {
		super(name, "check constraint", owner);
		this.condition = condition;
		if (owner != null)
        	owner.addCheckConstraint(this);
		// TODO parse condition
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public boolean isIdentical(DBObject other) {
		if (this == other)
			return true;
		if (other == null || !(other instanceof DBCheckConstraint))
			return false;
		DBCheckConstraint that = (DBCheckConstraint) other;
		return this.condition.equals(that.getCondition());
	}

	public String getCondition() {
		return condition;
	}

	@Override
	public String[] getColumnNames() {
		throw new UnsupportedOperationException("DBConstraint.getColumnNames() is not implemented"); // TODO implement DBConstraint.getColumnNames
	}

	@Override
	public String toString() {
		return "CHECK " + condition;
	}
	
}
