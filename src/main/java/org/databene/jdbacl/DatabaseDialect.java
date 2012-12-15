/*
 * (c) Copyright 2008-2012 by Volker Bergmann. All rights reserved.
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

package org.databene.jdbacl;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.databene.commons.ArrayUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.ObjectNotFoundException;
import org.databene.commons.StringUtil;
import org.databene.commons.TimeUtil;
import org.databene.commons.converter.TimestampFormatter;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBPackage;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBTrigger;
import org.databene.jdbacl.sql.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides abstractions of concepts that are implemented differently 
 * by different database vendors.<br/><br/>
 * @since 0.4.0
 * @author Volker Bergmann
 */
public abstract class DatabaseDialect {
	
	private static final String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private   final String system;
    public    final boolean quoteTableNames;
    protected final boolean sequenceSupported;
	private   final DateFormat dateFormat;
	private   final DateFormat datetimeFormat;
	private   final DateFormat timeFormat;
	private   Set<String> reservedWords;
    
    public DatabaseDialect(String system, boolean quoteTableNames, boolean sequenceSupported, 
    		String datePattern, String timePattern, String datetimePattern) {
        this.system = system;
        this.quoteTableNames = quoteTableNames;
        this.sequenceSupported = sequenceSupported;
        this.dateFormat = new SimpleDateFormat(datePattern);
        this.timeFormat = new SimpleDateFormat(timePattern);
        this.datetimeFormat = new SimpleDateFormat(datetimePattern);
        this.reservedWords = null;
    }

    public String getSystem() {
    	return system;
    }
    
	public boolean isReservedWord(String word, Connection connection) throws SQLException {
		return (word != null && getReservedWords(connection).contains(word.toUpperCase()));
	}

	public Set<String> getReservedWords(Connection connection) throws SQLException {
		if (reservedWords == null)
			importReservedWords(connection);
		return reservedWords;
	}
	
	/**
	 * Imports the reserved words defined in a configuration file, then adds words retrieved by 
	 * {@link DatabaseMetaData#getSQLKeywords()}. If no system-specific configuration file is found, 
	 * jdbacl falls back to the keywords defined in SQL:2003. The combination of both approaches 
	 * happens, since the documentation of {@link DatabaseMetaData#getSQLKeywords()} says that it 
	 * "Retrieves a comma-separated list of all of this database's SQL keywords that are NOT also 
	 * SQL:2003 keywords. By adding keywords dynamically retrieved from the JDBC driver I also get 
	 * the chance to automatically handle keywords introduced in a new database version without 
	 * having loads of different reserved-words-files.
	 * @throws SQLException 
	 */
	protected void importReservedWords(Connection connection) throws SQLException {
		this.reservedWords = new HashSet<String>();
		parseReservedWordsConfigFile();
		if (connection != null)
			importReservedWordsFromDriver(connection);
	}

	private void importReservedWordsFromDriver(Connection connection) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		String keywordList = metaData.getSQLKeywords();
		logger.debug("Imported keywords: " + keywordList);
		String[] keywords = StringUtil.splitAndTrim(keywordList, ',');
		for (String keyword : keywords)
			this.reservedWords.add(keyword.toUpperCase());
	}

	private void parseReservedWordsConfigFile() {
		String resourceName = "org/databene/jdbacl/dialect/" + system + "-reserved_words.txt";
		if (IOUtil.isURIAvailable(resourceName)) {
			parseReservedWords(resourceName);
		} else {
			logger.debug("Configuration file not found: " + resourceName + ". Falling back to SQL:2003 keywords");
			parseReservedWords("org/databene/jdbacl/dialect/SQL2003-reserved_words.txt");
		}
	}

	private void parseReservedWords(String resourceName) {
		logger.debug("reading reserved word from config file " + resourceName);
		try {
			for (String word : IOUtil.readTextLines(resourceName, false))
				reservedWords.add(word.trim());
		} catch (IOException e) {
			throw new ConfigurationError("Error reading file " + resourceName, e);
		}
	}

	
    public abstract boolean isDefaultCatalog(String catalog, String user);
    
    public abstract boolean isDefaultSchema(String schema, String user);
    
	public boolean isSequenceSupported() {
    	return sequenceSupported;
    }

	public boolean isSequenceBoundarySupported() {
    	return sequenceSupported;
    }

    public DBSequence[] querySequences(Connection connection) throws SQLException {
		throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support querying sequences");
	}

	public void createSequence(String name, long initialValue, Connection connection) throws SQLException {
		if (sequenceSupported)
			DBUtil.executeUpdate("create sequence " + name + " start with " + initialValue, connection);
		else
			throw checkSequenceSupport("createSequence");
    }
    
	public String renderCreateSequence(DBSequence sequence) {
		StringBuilder builder = new StringBuilder("CREATE SEQUENCE ");
		builder.append(renderSequenceNameAndType(sequence));
		BigInteger start = sequence.getStart();
		if (start != null && isNotOne(start))
			builder.append(" START WITH ").append(start);
		BigInteger increment = sequence.getIncrement();
		if (increment != null && isNotOne(increment))
			builder.append(" INCREMENT BY ").append(increment);
		if (isSequenceBoundarySupported()) {
			BigInteger maxValue = sequence.getMaxValue();
			if (maxValue != null)
				builder.append(" MAXVALUE ").append(maxValue);
			BigInteger minValue = sequence.getMinValue();
			if (minValue != null)
				builder.append(" MINVALUE ").append(minValue);
		}
		Boolean cycle = sequence.isCycle(); 
		if (cycle != null)
			builder.append(cycle ? " CYCLE" : " " + sequenceNoCycle());
		return builder.toString();
	}

	protected String renderSequenceNameAndType(DBSequence sequence) {
		return sequence.getName();
	}
	
	protected String sequenceNoCycle() {
		return "NOCYCLE";
	}

	protected static boolean isNotOne(BigInteger i) {
		return (BigInteger.ONE.compareTo(i) != 0);
	}

    public String renderFetchSequenceValue(String sequenceName) {
		throw checkSequenceSupport("nextSequenceValue");
    }

    public void setNextSequenceValue(String sequenceName, long value, Connection connection) throws SQLException {
		if (sequenceSupported) {
			long old = DBUtil.queryLong(renderFetchSequenceValue(sequenceName), connection);
			long increment = value - old - 1;
			if (increment != 0) {
				BigInteger formerIncrement = getSequence(sequenceName, connection).getIncrement();
				DBUtil.executeUpdate("alter sequence " + sequenceName + " increment by " + increment, connection);
				DBUtil.queryLong(renderFetchSequenceValue(sequenceName), connection);
				DBUtil.executeUpdate("alter sequence " + sequenceName + " increment by " + formerIncrement, connection);
			}
		} else
			throw checkSequenceSupport("incrementSequence");
    }
    
	public DBSequence getSequence(String sequenceName, Connection connection) throws SQLException {
		DBSequence[] sequences = querySequences(connection);
		for (DBSequence seq : sequences)
			if (seq.getName().equalsIgnoreCase(sequenceName))
				return seq;
		throw new ObjectNotFoundException("No sequence found with name '" + sequenceName + "'");
	}

	public String renderDropSequence(String sequenceName) {
		if (sequenceSupported)
			return "drop sequence " + sequenceName;
		else
			throw checkSequenceSupport("dropSequence");
    }
    
    public String insert(DBTable table, List<ColumnInfo> columnInfos) {
        StringBuilder builder = new StringBuilder("insert into ");
        appendQualifiedTableName(table, builder).append(" (");
        if (columnInfos.size() > 0)
            appendColumnName(columnInfos.get(0).name, builder);
        for (int i = 1; i < columnInfos.size(); i++) {
            builder.append(",");
            appendColumnName(columnInfos.get(i).name, builder);
        }
        builder.append(") values (");
        if (columnInfos.size() > 0)
            builder.append("?");
        for (int i = 1; i < columnInfos.size(); i++)
            builder.append(",?");
        builder.append(")");
        String sql = builder.toString();
        logger.debug("built SQL statement: " + sql);
        return sql;
    }

	public String update(DBTable table, String[] pkColumnNames, List<ColumnInfo> columnInfos) {
    	if (pkColumnNames.length == 0)
    		throw new UnsupportedOperationException("Cannot update table without primary key: " + table.getName());
        StringBuilder builder = new StringBuilder("update ");
        appendQualifiedTableName(table, builder).append(" set");
        for (int i = 0; i < columnInfos.size(); i++) {
        	if (!ArrayUtil.contains(columnInfos.get(i).name, pkColumnNames)) {
	            builder.append(" ");
	            appendColumnName(columnInfos.get(i).name, builder);
	            builder.append("=?");
	            if (i < columnInfos.size() - pkColumnNames.length - 1)
	            	builder.append(", ");
        	}
        }
        builder.append(" where");
        for (int i = 0; i < pkColumnNames.length; i++) {
        	builder.append(' ');
        	appendColumnName(pkColumnNames[i], builder);
        	builder.append("=?");
        	if (i < pkColumnNames.length - 1)
        		builder.append(" and");
        }
        String sql = builder.toString();
        logger.debug("built SQL statement: " + sql);
        return sql;
    }

	public String formatValue(Object value) {
		if (value instanceof CharSequence || value instanceof Character)
			return "'" + DBUtil.escape(value.toString()) + "'";
		else if (value instanceof Timestamp)
			return formatTimestamp((Timestamp) value);
		else if (value instanceof Time)
			return timeFormat.format(value);
		else if (value instanceof Date) {
			if (TimeUtil.isMidnight((Date) value))
				return dateFormat.format(value);
			else
				return datetimeFormat.format(value);
		} else
			return String.valueOf(value);
    }

	public String formatTimestamp(Timestamp timestamp) {
		return "'" + new TimestampFormatter(DEFAULT_TIMESTAMP_PATTERN).format(timestamp) + "'";
	}
	
	// private helpers -------------------------------------------------------------------------------------------------
	
    private StringBuilder appendQualifiedTableName(DBTable table, StringBuilder builder) {
    	DBCatalog catalog = table.getCatalog();
		if (catalog != null && catalog.getName() != null)
    		appendQuoted(catalog.getName(), builder).append('.');
    	if (table.getSchema() != null && table.getSchema().getName() != null)
    		appendQuoted(table.getSchema().getName(), builder).append('.');
    	return appendQuoted(table.getName(), builder);
    }

	private StringBuilder appendColumnName(String columnName, StringBuilder builder) {
    	return appendQuoted(columnName, builder);
	}
	
    private StringBuilder appendQuoted(String name, StringBuilder builder) {
    	if (quoteTableNames)
    		return builder.append('"').append(name).append('"');
    	else
    		return builder.append(name);
    }
    
    protected UnsupportedOperationException checkSequenceSupport(String methodName) {
		if (!sequenceSupported)
			return new UnsupportedOperationException("Sequence not supported in " + system);
		else
			return new UnsupportedOperationException(methodName + "() not implemented");
    }

    /** Determines if a primary key constraint name was explicitly specified on creation 
     * or at least generated by the database in a deterministic (reproducible) way */
	public abstract boolean isDeterministicPKName(String pkName);

    /** Determines if a unique key constraint name was explicitly specified on creation 
     * or at least generated by the database in a deterministic (reproducible) way */
	public abstract boolean isDeterministicUKName(String ukName);

    /** Determines if a foreign key constraint name was explicitly specified creation 
     * or at least generated by the database in a deterministic (reproducible) way */
	public abstract boolean isDeterministicFKName(String fkName);

    /** Determines if an index name was explicitly specified creation 
     * or at least generated by the database in a deterministic (reproducible) way */
	public abstract boolean isDeterministicIndexName(String indexName);
	
	/** Tells if the database supports regular expressions */
	public boolean supportsRegex() {
		return false;
	}

	/** 
	 * Renders a query condition for a regular expression.
	 * @param expression a column name or a SQL value expression to be checked with a regular expression
	 * @param not if set to true, the query fits expressions which do not match the regular expression
	 * @param regex the regular expression to check with
	 * @return a string with a SQL query condition.
	 * @throws UnsupportedOperationException if the database does not support regular expressions
	 */
	public String regexQuery(String expression, boolean not, String regex) {
		throw new UnsupportedOperationException(system + " does not support regular expressions");
	}
	
	public String trim(String expression) {
		throw new UnsupportedOperationException(system + " does not support trimming");
	}

	public String renderCase(String columnName, String elseExpression, String... whenThenExpressionPairs) {
		StringBuilder builder = new StringBuilder();
		builder.append("CASE");
		for (int i = 0; i < whenThenExpressionPairs.length; i += 2) {
			builder.append(" WHEN ").append(whenThenExpressionPairs[i]); // when part
			builder.append(" THEN ").append(whenThenExpressionPairs[i + 1]); // then part
		}
		if (!StringUtil.isEmpty(elseExpression))
			builder.append(" ELSE ").append(elseExpression); // else part
		builder.append(" END"); // closing the case
		if (columnName != null)
			builder.append(" AS ").append(columnName); // applying column name
		return builder.toString();
	}
	
    /* TODO v0.8.x implement queries for indexes, views, functions and procedures
    public List<DBView> queryViews(Connection connection) throws SQLException {
		return new ArrayList<DBView>();
		// ORA: select VIEW_NAME, OWNER from SYS.ALL_VIEWS order by OWNER, VIEW_NAME
	}

    public List<DBIndex> queryIndexes(DBSchema schema, Connection connection) throws SQLException {
		return new ArrayList<DBIndex>();
	}

    public List<DBFunction> queryFunctions(Connection connection) throws SQLException {
		return new ArrayList<DBTrigger>();
		// ORA: select OBJECT_NAME, OWNER from SYS.ALL_OBJECTS where upper(OBJECT_TYPE) = upper('FUNCTION') order by OWNER, OBJECT_NAME 
	}

    public List<DBProcedure> queryProcedures(Connection connection) throws SQLException {
		return new ArrayList<DBTrigger>();
		// ORA: select OBJECT_NAME, OWNER from SYS.ALL_OBJECTS where upper(OBJECT_TYPE) = upper('PROCEDURE') order by OWNER, OBJECT_NAME 
	}
	*/
	
    public List<DBTrigger> queryTriggers(DBSchema schema, Connection connection) throws SQLException {
		return new ArrayList<DBTrigger>();
	}

    public List<DBPackage> queryPackages(DBSchema schema, Connection connection) throws SQLException {
		return new ArrayList<DBPackage>();
	}

	public abstract void restrictRownums(int rowOffset, int rowCount, Query query);

}
