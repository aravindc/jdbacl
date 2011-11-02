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

import java.util.HashSet;
import java.util.Set;

import org.databene.commons.CollectionUtil;
import org.databene.commons.StringUtil;
import org.databene.jdbacl.sql.ColumnExpression;
import org.databene.jdbacl.sql.SQLParserUtil;
import org.databene.jdbacl.sql.parser.ANTLRNoCaseStringStream;
import org.databene.script.Expression;
import org.databene.script.expression.WrapperExpression;

/**
 * Represents a database check constraint.<br/><br/>
 * Created: 01.06.2011 12:40:30
 * @since 0.6.8
 * @author Volker Bergmann
 */
public class DBCheckConstraint extends DBConstraint {
	
	private static final long serialVersionUID = 3766067048212751458L;
	
	private String tableName;
	private String conditionText;
	private Expression<?> condition;
	private String[] columnNames;

	public DBCheckConstraint(String name, boolean nameDeterministic, DBTable owner, String conditionText) {
		this(name, nameDeterministic, owner.getName(), conditionText);
		owner.addCheckConstraint(this);
	}
	
	public DBCheckConstraint(String name, boolean nameDeterministic, String tableName, String conditionText) {
		super(name, nameDeterministic, "check constraint", null);
		this.tableName = tableName;
		this.conditionText = conditionText;
		this.condition = SQLParserUtil.parseExpression(new ANTLRNoCaseStringStream(conditionText));
		this.columnNames = getColumnNames(condition);
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
		return this.name.equals(that.getName()) 
			&& this.conditionText.equals(that.getConditionText());
	}

	public boolean isEquivalent(DBCheckConstraint that) {
		return this.tableName.equals(that.tableName) 
			&& StringUtil.normalizeSpace(this.conditionText).equals(StringUtil.normalizeSpace(that.getConditionText()));
	}

	public String getConditionText() {
		return conditionText;
	}

	@Override
	public String[] getColumnNames() {
		return columnNames;
	}

	@Override
	public String toString() {
		return (name != null ? "CONSTRAINT " + name : "") + "CHECK " + conditionText;
	}
	
	private String[] getColumnNames(Expression<?> condition) {
		return CollectionUtil.toArray(scanForColumns(condition, new HashSet<String>()), String.class);
	}

	private Set<String> scanForColumns(Expression<?> expression, Set<String> result) {
		if (expression instanceof ColumnExpression)
			result.add(((ColumnExpression) expression).getColumnName());
		else if (expression instanceof WrapperExpression)
			for (Expression<?> sourceExpression : ((WrapperExpression<?>) expression).getSourceExpressions())
				scanForColumns(sourceExpression, result);
		return result;
	}

}
