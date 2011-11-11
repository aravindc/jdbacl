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

package org.databene.jdbacl.model.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.databene.commons.ArrayUtil;
import org.databene.commons.Assert;
import org.databene.commons.IOUtil;
import org.databene.commons.ImportFailedException;
import org.databene.commons.StringUtil;
import org.databene.commons.SyntaxError;
import org.databene.commons.version.VersionNumber;
import org.databene.commons.xml.XMLUtil;
import org.databene.jdbacl.model.DBPackage;
import org.databene.jdbacl.model.DBProcedure;
import org.databene.jdbacl.model.DBTrigger;
import org.databene.jdbacl.model.FKChangeRule;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBCheckConstraint;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBIndex;
import org.databene.jdbacl.model.DBMetaDataImporter;
import org.databene.jdbacl.model.DBNonUniqueIndex;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.databene.jdbacl.model.DBUniqueIndex;
import org.databene.jdbacl.model.Database;
import org.databene.jdbacl.model.DefaultDBColumn;
import org.databene.jdbacl.model.DefaultDBTable;
import org.databene.jdbacl.model.DefaultDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Imports a meta data model from an XML file.<br/><br/>
 * Created: 28.11.2010 15:18:55
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class XMLModelImporter implements DBMetaDataImporter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLModelImporter.class);

	private File file;
	
	public XMLModelImporter(File file) {
		this.file = file;
	}

	public Database importDatabase() throws ImportFailedException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			Document doc = XMLUtil.parse(in);
			Database database = parseDatabase(doc.getDocumentElement());
			scanReferers(database);
			return database;
		} catch (IOException e) {
			throw new ImportFailedException(e);
		} finally {
			IOUtil.close(in);
		}
	}

	private void scanReferers(Database database) {
		for (DBTable table : database.getTables())
			for (DBForeignKeyConstraint fk : table.getForeignKeyConstraints())
				fk.getRefereeTable().addReferrer(table);
	}

	private Database parseDatabase(Element e) {
		String name = e.getAttribute("name");
		String product = e.getAttribute("databaseProductName");
		VersionNumber version = VersionNumber.valueOf(e.getAttribute("databaseProductVersion"));
		DefaultDatabase db = new DefaultDatabase(name, product, version);
    	db.setImportDate(XMLUtil.getDateAttribute(e, "importDate"));
    	db.setUser(e.getAttribute("user"));
    	db.setTableInclusionPattern(e.getAttribute("tableInclusionPattern"));
    	db.setTableExclusionPattern(e.getAttribute("tableExclusionPattern"));
    	db.setImportedChecks(XMLUtil.getBooleanAttribute(e, "importedChecks"));
    	db.setImportedUKs(XMLUtil.getBooleanAttribute(e, "importedUKs"));
    	db.setImportedIndexes(XMLUtil.getBooleanAttribute(e, "importedIndexes"));
    	db.setImportedSequences(XMLUtil.getBooleanAttribute(e, "importedSequences"));

		// import catalogs
		for (Element child : XMLUtil.getChildElements(e)) {
			String childName = child.getNodeName();
			if ("catalog".equals(childName))
				parseCatalog(child, db);
			else
				throw new UnsupportedOperationException("Not an allowed element within <database>: " + childName);
		}
		return db;
	}

	private DBCatalog parseCatalog(Element e, Database db) {
		String name = StringUtil.emptyToNull(e.getAttribute("name"));
		DBCatalog catalog = new DBCatalog(name, db);
		for (Element child : XMLUtil.getChildElements(e)) {
			String childName = child.getNodeName();
			if ("schema".equals(childName))
				parseSchema(child, catalog);
			else
				throw new UnsupportedOperationException("Not an allowed element within <catalog>: " + childName);
		}
		return catalog;
	}

	private DBSchema parseSchema(Element e, DBCatalog catalog) {
		String name = e.getAttribute("name");
		DBSchema schema = new DBSchema(name, catalog);
		Element[] children = XMLUtil.getChildElements(e);
		for (Element child : children) {
			String childName = child.getNodeName();
			if ("table".equals(childName))
				parseTableName(child, schema);
			else if (!"sequence".equals(childName) && !"trigger".equals(childName) && !"package".equals(childName))
				throw new UnsupportedOperationException("Not an allowed element within <schema>: " + childName);
		}
		for (Element child : children) {
			String childName = child.getNodeName();
			if ("table".equals(childName))
				parseTableStructure(child, schema);
			else if ("sequence".equals(childName))
				parseSequence(child, schema);
			else if ("trigger".equals(childName))
				parseTrigger(child, schema);
			else if ("package".equals(childName))
				parsePackage(child, schema);
			else
				throw new UnsupportedOperationException("Not an allowed element within <schema>: " + childName);
		}
		return schema;
	}

	private DefaultDBTable parseTableName(Element e, DBSchema schema) {
		String name = e.getAttribute("name");
		return new DefaultDBTable(name, schema);
	}

	private DefaultDBTable parseTableStructure(Element e, DBSchema schema) {
		String name = e.getAttribute("name");
		DefaultDBTable table = (DefaultDBTable) schema.getTable(name);
		for (Element child : XMLUtil.getChildElements(e)) {
			String childName = child.getNodeName();
			if ("column".equals(childName))
				parseColumn(child, table);
			else if ("pk".equals(childName))
				parsePK(child, table);
			else if ("uk".equals(childName))
				parseUK(child, table);
			else if ("fk".equals(childName))
				parseFK(child, table, schema);
			else if ("check".equals(childName))
				parseCheck(child, table);
			else if ("index".equals(childName))
				parseIndex(child, table);
			else
				throw new UnsupportedOperationException("Not an allowed element within <table>: " + childName);
		}
		return table;
	}

	private DBColumn parseColumn(Element e, DefaultDBTable table) {
		String name = e.getAttribute("name");
		String typeAndSizeSpec = e.getAttribute("type");
		int jdbcType = Integer.parseInt(e.getAttribute("jdbcType"));
		DefaultDBColumn column = new DefaultDBColumn(name, table, jdbcType, typeAndSizeSpec);
		String defaultValue = e.getAttribute("default");
		if (!StringUtil.isEmpty(defaultValue))
			column.setDefaultValue(defaultValue);
		String nullableSpec = e.getAttribute("nullable");
		boolean nullable = (nullableSpec == null || !"false".equals(nullableSpec));
		column.setNullable(nullable);
		return column;
	}

	private DBPrimaryKeyConstraint parsePK(Element e, DefaultDBTable table) {
		boolean autoNamed = false;
		if (e.getAttribute("autoNamed") != null)
			autoNamed = Boolean.valueOf(e.getAttribute("autoNamed"));
		return new DBPrimaryKeyConstraint(table, e.getAttribute("name"), autoNamed, parseColumnNames(e));
	}

	private DBUniqueConstraint parseUK(Element e, DefaultDBTable table) {
		boolean autoNamed = false;
		if (e.getAttribute("autoNamed") != null)
			autoNamed = Boolean.valueOf(e.getAttribute("autoNamed"));
		return new DBUniqueConstraint(table, e.getAttribute("name"), autoNamed, parseColumnNames(e));
	}

	private DBForeignKeyConstraint parseFK(Element e, DefaultDBTable owner, DBSchema schema) {
		String name = e.getAttribute("name");
		String refereeTableName = e.getAttribute("refereeTable");
		DBTable refereeTable = schema.getTable(refereeTableName);
		Assert.notNull(refereeTable, "refereeTable");
		String colAttr = e.getAttribute("column");
		String[] columnNames = null;
		String[] refereeColumnNames = null;
		if (!StringUtil.isEmpty(colAttr)) {
			columnNames = new String[] { colAttr };
			refereeColumnNames = new String[] { e.getAttribute("refereeColumn") };
		} else {
			Element colsElement = XMLUtil.getChildElement(e, false, true, "columns");
			Element[] colElements = XMLUtil.getChildElements(colsElement, false, "column");
			for (Element colElement : colElements) {
				columnNames = ArrayUtil.append(colElement.getAttribute("name"), columnNames);
				refereeColumnNames = ArrayUtil.append(colElement.getAttribute("refereeColumn"), refereeColumnNames);
			}
		}
		boolean autoNamed = false;
		if (e.getAttribute("autoNamed") != null)
			autoNamed = Boolean.valueOf(e.getAttribute("autoNamed"));
		DBForeignKeyConstraint fk = new DBForeignKeyConstraint(name, autoNamed, owner, columnNames, refereeTable, refereeColumnNames);
		// parse rules
		String updateRule = XMLUtil.getAttribute(e, "updateRule", false);
		if (!StringUtil.isEmpty(updateRule))
			fk.setUpdateRule(FKChangeRule.valueOf(updateRule));
		String deleteRule = XMLUtil.getAttribute(e, "deleteRule", false);
		if (!StringUtil.isEmpty(deleteRule))
			fk.setDeleteRule(FKChangeRule.valueOf(deleteRule));
		return fk;
	}

	private DBCheckConstraint parseCheck(Element e, DefaultDBTable table) {
		try {
			boolean autoNamed = false;
			if (e.getAttribute("autoNamed") != null)
				autoNamed = Boolean.valueOf(e.getAttribute("autoNamed"));
			return new DBCheckConstraint(e.getAttribute("name"),autoNamed, table, e.getAttribute("definition"));
		} catch (Exception ex) {
			LOGGER.error("Error parsing check constraint", ex);
			return null;
		}
		
	}

	private DBIndex parseIndex(Element e, DefaultDBTable table) {
		String name = e.getAttribute("name");
		String uniqueSpec = e.getAttribute("unique");
		boolean unique = (uniqueSpec != null && "true".equals(uniqueSpec));
		String nameDeterministicSpec = e.getAttribute("nameDeterministic");
		boolean nameDeterministic = (nameDeterministicSpec == null || "true".equals(nameDeterministicSpec));
		String[] columnNames = parseColumnNames(e);
		if (unique)
			return new DBUniqueIndex(name, nameDeterministic, table.getUniqueConstraint(columnNames));
		else
			return new DBNonUniqueIndex(name, nameDeterministic, table, columnNames);
	}

	public String[] parseColumnNames(Element e) {
		String colAttr = e.getAttribute("column");
		String[] columnNames = null;
		if (!StringUtil.isEmpty(colAttr))
			columnNames = new String[] { colAttr };
		else {
			Element colsElement = XMLUtil.getChildElement(e, false, true, "columns");
			Element[] colElements = XMLUtil.getChildElements(colsElement, false, "column");
			for (Element colElement : colElements)
				columnNames = ArrayUtil.append(colElement.getAttribute("name"), columnNames);
		}
		return columnNames;
	}

	private DBSequence parseSequence(Element e, DBSchema schema) {
		DBSequence sequence = new DBSequence(e.getAttribute("name"), schema);
		String start = e.getAttribute("start");
		if (!StringUtil.isEmpty(start))
			sequence.setStart(new BigInteger(start));
		String increment = e.getAttribute("increment");
		if (!StringUtil.isEmpty(increment))
			sequence.setIncrement(new BigInteger(increment));
		String maxValue = e.getAttribute("maxValue");
		if (!StringUtil.isEmpty(maxValue))
			sequence.setMaxValue(new BigInteger(maxValue));
		String minValue = e.getAttribute("minValue");
		if (!StringUtil.isEmpty(minValue))
			sequence.setMinValue(new BigInteger(minValue));
		String cycle = e.getAttribute("cycle");
		if (!StringUtil.isEmpty(cycle))
			sequence.setCycle(Boolean.parseBoolean(cycle));
		String cache = e.getAttribute("cache");
		if (!StringUtil.isEmpty(cache))
			sequence.setCache(Long.parseLong(cache));
		String order = e.getAttribute("order");
		if (!StringUtil.isEmpty(order))
			sequence.setOrder(Boolean.parseBoolean(order));
		return sequence;
	}

	private DBTrigger parseTrigger(Element e, DBSchema schema) {
		DBTrigger trigger = new DBTrigger(e.getAttribute("name"), schema);
		String triggerType = e.getAttribute("triggerType");
		if (!StringUtil.isEmpty(triggerType))
			trigger.setTriggerType(triggerType);
		String triggeringEvent = e.getAttribute("triggeringEvent");
		if (!StringUtil.isEmpty(triggeringEvent))
			trigger.setTriggeringEvent(triggeringEvent);
		String tableOwner = e.getAttribute("tableOwner");
		if (!StringUtil.isEmpty(tableOwner))
			trigger.setTableOwner(tableOwner);
		String baseObjectType = e.getAttribute("baseObjectType");
		if (!StringUtil.isEmpty(baseObjectType))
			trigger.setBaseObjectType(baseObjectType);
		String tableName = e.getAttribute("tableName");
		if (!StringUtil.isEmpty(tableName))
			trigger.setTableName(tableName);
		String columnName = e.getAttribute("columnName");
		if (!StringUtil.isEmpty(columnName))
			trigger.setColumnName(columnName);
		String referencingNames = e.getAttribute("referencingNames");
		if (!StringUtil.isEmpty(referencingNames))
			trigger.setReferencingNames(referencingNames);
		String whenClause = e.getAttribute("whenClause");
		if (!StringUtil.isEmpty(whenClause))
			trigger.setWhenClause(whenClause);
		String status = e.getAttribute("status");
		if (!StringUtil.isEmpty(status))
			trigger.setStatus(status);
		String description = e.getAttribute("description");
		if (!StringUtil.isEmpty(description))
			trigger.setDescription(description);
		String actionType = e.getAttribute("actionType");
		if (!StringUtil.isEmpty(actionType))
			trigger.setActionType(actionType);
		String triggerBody = e.getAttribute("triggerBody");
		if (!StringUtil.isEmpty(triggerBody))
			trigger.setTriggerBody(triggerBody);
		return trigger;
	}

	private DBPackage parsePackage(Element e, DBSchema schema) {
		DBPackage pkg = new DBPackage(e.getAttribute("name"), schema);
		String subObjectName = e.getAttribute("subObjectName");
		if (!StringUtil.isEmpty(subObjectName))
			pkg.setSubObjectName(subObjectName);
		String objectId = e.getAttribute("objectId");
		if (!StringUtil.isEmpty(objectId))
			pkg.setObjectId(objectId);
		String dataObjectId = e.getAttribute("dataObjectId");
		if (!StringUtil.isEmpty(dataObjectId))
			pkg.setDataObjectId(dataObjectId);
		String objectType = e.getAttribute("objectType");
		if (!StringUtil.isEmpty(objectType))
			pkg.setObjectType(objectType);
		String status = e.getAttribute("status");
		if (!StringUtil.isEmpty(status))
			pkg.setStatus(status);
		parsePackageProcedures(e, pkg);
		return pkg;
	}

	private void parsePackageProcedures(Element pkgElement, DBPackage pkg) {
		for (Element e : XMLUtil.getChildElements(pkgElement)) {
			String nodeName = e.getNodeName();
			if ("procedure".equals(nodeName)) {
				DBProcedure procedure = new DBProcedure(e.getAttribute("name"), pkg);
				String objectId = e.getAttribute("objectId");
				if (!StringUtil.isEmpty(objectId))
					procedure.setObjectId(objectId);
				String subProgramId = e.getAttribute("subProgramId");
				if (!StringUtil.isEmpty(subProgramId))
					procedure.setSubProgramId(subProgramId);
				String overload = e.getAttribute("overload");
				if (!StringUtil.isEmpty(overload))
					procedure.setOverload(overload);
			} else
				throw new SyntaxError("Illegal child element of <package>", XMLUtil.format(e));
		}
	}

	public void close() throws IOException {
		// nothing special to do
	}
	
}
