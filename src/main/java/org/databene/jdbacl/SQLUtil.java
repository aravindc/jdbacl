/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.jdbacl;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.databene.commons.ArrayFormat;
import org.databene.commons.CollectionUtil;
import org.databene.commons.StringUtil;
import org.databene.jdbacl.model.DBCheckConstraint;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBConstraint;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBNotNullConstraint;
import org.databene.jdbacl.model.DBObject;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.databene.jdbacl.model.MultiColumnObject;

/**
 * Provides utility methods for creating SQL queries and commands.<br/><br/>
 * Created: 01.09.2010 09:38:46
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class SQLUtil {
	
	private static final Set<String> NO_SIZE_TYPES = CollectionUtil.toSet(
			"DATE", "BLOB", "CLOB", "NCLOB");

	private static final Set<String> DDL_STATEMENTS = CollectionUtil.toSet(
			"create table", "alter table", "drop table",
			"create unique index", "drop index", "alter index",
			"rename",
			"create materialized view", "alter materialized view", "drop materialized view" 
			);
	
	private static final Set<String> DML_STATEMENTS = CollectionUtil.toSet(
			"insert", "update", "delete", "truncate", "select into");
	
	private static final Set<String> PROCEDURE_CALLS = CollectionUtil.toSet(
			"exec", "call");
	
	public static Object[] parseColumnTypeAndSize(String spec) {
		int lparen = spec.indexOf('(');
		if (lparen < 0)
			return new Object[] { spec };
		String type = spec.substring(0, lparen);
		int rparen = spec.indexOf(')', lparen);
		if (rparen < 0)
			throw new RuntimeException("Illegal column type format: " + spec);
		String[] sizeAndFractionDigits = spec.substring(lparen + 1, rparen).split(",");
		if (sizeAndFractionDigits.length == 1)
			return new Object[] { type, Integer.parseInt(sizeAndFractionDigits[0].trim()) };
		else 
			return new Object[] { type, Integer.parseInt(sizeAndFractionDigits[0].trim()), 
				Integer.parseInt(sizeAndFractionDigits[1].trim()) };
	}
	
    public static String formatColumnNames(DBColumn[] columns) {
        StringBuilder builder = new StringBuilder(columns[0].getName());
        for (int i = 1; i < columns.length; i++)
            builder.append(", ").append(columns[i].getName());
        return builder.toString();
    }

    public static String formatColumnNames(List<DBColumn> columns) {
        StringBuilder builder = new StringBuilder(columns.get(0).getName());
        for (int i = 1; i < columns.size(); i++)
            builder.append(", ").append(columns.get(i).getName());
        return builder.toString();
    }

	public static String renderColumn(DBColumn column) {
		StringBuilder builder = new StringBuilder();
		
	    // column name
	    builder.append(column.getName());
	    
	    // column type & size
		builder.append(' ');
		renderColumnTypeWithSize(column, builder);
	    
	    // default
	    if (column.getDefaultValue() != null)
	    	builder.append(" DEFAULT " + column.getDefaultValue());
	    
	    // nullability
	    if (!column.isNullable())
	    	builder.append(" NOT");
    	builder.append(" NULL");
	    
	    return builder.toString();
    }

	public static String renderColumnTypeWithSize(DBColumn column) {
	    StringBuilder builder = new StringBuilder();
		renderColumnTypeWithSize(column, builder);
		return builder.toString();
    }
	
	public static void renderColumnTypeWithSize(DBColumn column, StringBuilder builder) {
	    String typeName = column.getType().getName();
	    builder.append(typeName);
	    if (column.getSize() != null && !NO_SIZE_TYPES.contains(typeName)) {
	    	builder.append("(" + column.getSize());
	    	if (column.getFractionDigits() != null)
	    		builder.append("," + column.getFractionDigits());
	    	builder.append(")");
	    }
    }
	
	public static String substituteMarkers(String sql, String marker, Object substitution) {
		return sql.replace(marker, renderValue(substitution));
    }

    public static String renderQuery(DBTable table, String[] columnNames, Object[] values) {
		StringBuilder builder = new StringBuilder("SELECT * FROM ").append(table.getName());
		builder.append(" WHERE ").append(renderWhereClause(columnNames, values));
		return builder.toString();
    }
    
    public static String renderWhereClause(String[] columnNames, Object[] values) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < columnNames.length; i++) {
			if (i > 0)
				builder.append(" AND ");
			builder.append(columnNames[i]).append(" = ").append(renderValue(values[i]));
		}
		return builder.toString();
    }
    
    public static String renderValue(Object value) {
	    if (value instanceof String || value instanceof Character)
	    	return "'" + value + "'";
	    else
	    	return String.valueOf(value);
    }
    
	public static Boolean mutatesDataOrStructure(String sql) {
		sql = StringUtil.normalizeSpace(sql.trim().toLowerCase());
		
		// ALTER SESSION does not change data or structure
		if (sql.trim().startsWith("alter session"))
			return false;

		// check if structure is changed...
		if (Boolean.TRUE.equals(mutatesStructure(sql)))
			return true;
		else if (isQuery(sql))
			return false;
		else if (isDML(sql))
			return true;
		return null;
    }

	public static Boolean mutatesStructure(String sql) {
	    if (isDDL(sql))
	   		return true;
	    if (isProcedureCall(sql))
    		return null;
	    return false;
    }
	
	public static boolean isDDL(String sql) {
		sql = StringUtil.normalizeSpace(sql.trim().toLowerCase());
	    for (String ddl : DDL_STATEMENTS)
	    	if (sql.startsWith(ddl))
	    		return true;
	    return false;
	}
	
	public static boolean isDML(String sql) {
		sql = StringUtil.normalizeSpace(sql.trim().toLowerCase());
	    for (String ddl : DML_STATEMENTS)
	    	if (sql.startsWith(ddl))
	    		return true;
	    return false;
	}
	
	public static boolean isProcedureCall(String sql) {
		sql = sql.trim().toLowerCase();
	    for (String call : PROCEDURE_CALLS)
	    	if (sql.startsWith(call))
	    		return true;
	    return false;
	}

	public static boolean isQuery(String sql) {
		sql = sql.trim().toLowerCase();
		// anything else than SELECT must be a mutation...
	    if (!sql.startsWith("select"))
	    	return false;
	    // ... but a 'select' statement might be a 'select into'
	    StringTokenizer t = new StringTokenizer(sql);
	    while (t.hasMoreTokens())
	    	if ("into".equals(t.nextToken()))
	    		return false;
	    // it is a plain select statement
	    return true;
    }

	public static String constraintDescription(DBConstraint constraint) {
		if (constraint instanceof DBPrimaryKeyConstraint)
			return pkDescription((DBPrimaryKeyConstraint) constraint);
		else if (constraint instanceof DBUniqueConstraint)
			return ukDescription((DBUniqueConstraint) constraint);
		else if (constraint instanceof DBForeignKeyConstraint)
			return fkDescription((DBForeignKeyConstraint) constraint);
		else if (constraint instanceof DBForeignKeyConstraint)
			return fkDescription((DBForeignKeyConstraint) constraint);
		else if (constraint instanceof DBNotNullConstraint)
			return notNullDescription((DBNotNullConstraint) constraint);
		else if (constraint instanceof DBCheckConstraint)
			return checkDescription((DBCheckConstraint) constraint);
		else
			throw new UnsupportedOperationException("Unknown constraint type: " + 
					constraint.getClass());
	}
	
	private static String checkDescription(DBCheckConstraint constraint) {
		return "CHECK " + constraint.getCondition();
	}

	private static String notNullDescription(DBNotNullConstraint constraint) {
		return constraint.getColumnNames()[0] + " NOT NULL";
	}

	public static String pkDescription(DBPrimaryKeyConstraint pk) {
		return constraintName(pk) + "PRIMARY KEY " + pk.getTable().getName() + colsInParens(pk);
	}

	public static String pkSpec(DBPrimaryKeyConstraint pk) {
		return constraintName(pk) + "PRIMARY KEY " + colsInParens(pk);
	}
	
	public static String ukDescription(DBUniqueConstraint uk) {
		return constraintName(uk) + "UNIQUE " + uk.getTable().getName() + colsInParens(uk);
    }

	public static String ukSpec(DBUniqueConstraint uk) {
		return constraintName(uk) + "UNIQUE " + colsInParens(uk);
    }

	public static String fkDescription(DBForeignKeyConstraint fk) {
		return constraintName(fk) + "FK " + 
			fk.getTable().getName() + colsInParens(fk) + " -> " +
			fk.getRefereeTable() + colsInParens(fk.getRefereeColumnNames());
	}
	
	public static String fkSpec(DBForeignKeyConstraint fk) {
		return constraintName(fk) + " FOREIGN KEY " + colsInParens(fk.getColumnNames()) +
			" REFERENCES " + fk.getRefereeTable() + colsInParens(fk.getRefereeColumnNames());
	}
	
	public static String leftJoin(String refererAlias, String[] refererColumns, 
			String refereeTable, String refereeAlias, String[] refereeColumns) {
		StringBuilder builder = new StringBuilder("left join ");
		builder.append(refereeTable).append(" ").append(refereeAlias).append(" on "); 
		for (int i = 0; i < refererColumns.length; i++) {
			if (i > 0)
				builder.append(" and ");
			builder.append(refererAlias).append('.').append(refererColumns[i]);
			builder.append(" = ").append(refereeAlias).append('.').append(refereeColumns[i]);
		}
		return builder.toString();
	}

	public static StringBuilder addRequiredCondition(String condition, StringBuilder builder) {
		if (builder.length() > 0)
			builder.append(" and ");
		return builder.append(condition);
	}

	public static StringBuilder addOptionalCondition(String condition, StringBuilder builder) {
		if (builder.length() > 0)
			builder.append(" or ");
		return builder.append(condition);
	}

	public static String ownerDotComponent(DBObject object) {
		return (object.getOwner() != null ? object.getOwner() + "." : "") + object.getName();
	}

	public static void appendConstraintName(DBConstraint constraint, StringBuilder builder) {
		if (constraint.getName() != null)
			builder.append("CONSTRAINT " + quoteNameIfNecessary(constraint.getName()) + ' ');
	}
	
	public static String constraintName(DBConstraint constraint) {
		return (constraint.getName() != null ? 
				"CONSTRAINT " + quoteNameIfNecessary(constraint.getName()) + ' ' : 
				"");
	}
	
	private static String quoteNameIfNecessary(String name) {
		return (name != null && name.indexOf(' ') >= 0 ? '"' + name + '"' : name);
    }

	private static Object colsInParens(MultiColumnObject object) {
		return colsInParens(object.getColumnNames());
	}

	private static Object colsInParens(String[] columnNames) {
		return '(' + ArrayFormat.format(columnNames) + ')';
	}

}
