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

package org.databene.jdbacl;

import java.util.List;
import java.util.Set;

import org.databene.commons.CollectionUtil;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBTable;

/**
 * Provides utility methods for creating SQL queries and commands.<br/><br/>
 * Created: 01.09.2010 09:38:46
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class SQLUtil {
	
	private static final Set<String> NO_SIZE_TYPES = CollectionUtil.toSet(
			"DATE", "BLOB", "CLOB", "NCLOB");
	
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
	    	builder.append(" NOT NULL");
	    
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
    
}
