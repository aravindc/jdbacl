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

package org.databene.jdbacl.model.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.databene.SimpleXMLWriter;
import org.databene.commons.Encodings;
import org.databene.commons.IOUtil;
import org.databene.commons.StringUtil;
import org.databene.jdbacl.SQLUtil;
import org.databene.jdbacl.model.DBCatalog;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBIndex;
import org.databene.jdbacl.model.DBMetaDataExporter;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBSchema;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.databene.jdbacl.model.Database;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Saves a database meta data model as XML file.<br/><br/>
 * Created: 28.11.2010 06:30:25
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class XMLModelExporter implements DBMetaDataExporter {
	
	private String encoding;
	
	// constructors ----------------------------------------------------------------------------------------------------
	
	public XMLModelExporter() {
		this(Encodings.UTF_8);
	}

	public XMLModelExporter(String encoding) {
		this.encoding = encoding;
	}
	
	// interface -------------------------------------------------------------------------------------------------------

	public void export(Database database, File file) throws IOException, SQLException {
		OutputStream out = new FileOutputStream(file);
		try {
			SimpleXMLWriter writer = new SimpleXMLWriter(out, encoding);
			writer.startDocument();
			exportDatabase(database, writer);
			writer.endDocument();
		} catch (SAXException e) {
			throw new RuntimeException("Error exporting database " + database, e);
		} finally {
			IOUtil.close(out);
		}
	}
	
	// private helper methods ------------------------------------------------------------------------------------------

	private void exportDatabase(Database database, SimpleXMLWriter writer)
			throws SAXException {
		writer.startElement("database", createAttributes("name", database.getName()));
		for (DBCatalog catalog : database.getCatalogs())
			exportCatalog(catalog, writer);
		writer.endElement("database");
	}

	private void exportCatalog(DBCatalog catalog, SimpleXMLWriter writer) throws SAXException {
		writer.startElement("catalog", createAttributes("name", catalog.getName()));
		for (DBSchema schema : catalog.getSchemas())
			exportSchema(schema, writer);
		writer.endElement("catalog");
	}

	private void exportSchema(DBSchema schema, SimpleXMLWriter writer) throws SAXException {
		AttributesImpl atts = createAttributes("name", schema.getName());
		writer.startElement("schema", atts);
		for (DBTable table : schema.getTables())
			exportTable(table, writer);
		writer.endElement("schema");
	}

	private void exportTable(DBTable table, SimpleXMLWriter writer) throws SAXException {
		AttributesImpl atts = createAttributes("name", table.getName());
		writer.startElement("table", atts);
		for (DBColumn column : table.getColumns())
			exportColumn(column, writer);
		DBPrimaryKeyConstraint pk = table.getPrimaryKeyConstraint();
		if (pk != null)
			exportPK(pk, writer);
		exportFks(table.getForeignKeyConstraints(), writer);
		exportUKs(table.getUniqueConstraints(), writer);
		exportIndexes(table.getIndexes(), writer);
		writer.endElement("table");
	}

	private void exportColumn(DBColumn column, SimpleXMLWriter writer) throws SAXException {
		AttributesImpl atts = createAttributes("name", column.getName());
		addAttribute("default", column.getDefaultValue(), atts);
		addAttribute("type", SQLUtil.renderColumnTypeWithSize(column), atts);
		addAttribute("nullable", (column.isNullable() ? null : "false"), atts);
		writer.startElement("column", atts);
		writer.endElement("column");
	}

	private void exportPK(DBPrimaryKeyConstraint pk, SimpleXMLWriter writer) throws SAXException {
		AttributesImpl pkAtts = createAttributes("name", pk.getName());
		String[] pkColumnNames = pk.getColumnNames();
		if (pkColumnNames.length == 1)
			addAttribute("column", pkColumnNames[0], pkAtts);
		writer.startElement("pk", pkAtts);
		if (pkColumnNames.length > 1)
			writeColumnGroup(pkColumnNames, writer);
		writer.endElement("pk");
	}

	public void writeColumnGroup(String[] pkColumnNames, SimpleXMLWriter writer)
			throws SAXException {
		writer.startElement("columns", null);
		for (String pkColumnName : pkColumnNames) {
			AttributesImpl colAtts = createAttributes("name", pkColumnName);
			writer.startElement("column", colAtts);
			writer.endElement("column");
		}
		writer.endElement("columns");
	}

	private void exportUKs(Set<DBUniqueConstraint> uks, SimpleXMLWriter writer) 
			throws SAXException {
		for (DBUniqueConstraint uk : uks) {
			if (uk instanceof DBPrimaryKeyConstraint)
				continue;
			AttributesImpl atts = createAttributes("name", uk.getName());
			String[] columnNames = uk.getColumnNames();
			if (columnNames.length == 1)
				addAttribute("column", columnNames[0], atts);
			writer.startElement("uk", atts);
			if (columnNames.length > 1)
				writeColumnGroup(columnNames, writer);
			writer.endElement("uk");
		}
	}

	private void exportFks(Set<DBForeignKeyConstraint> fks, SimpleXMLWriter writer) 
			throws SAXException {
		for (DBForeignKeyConstraint fk : fks) {
			AttributesImpl atts = createAttributes("name", fk.getName());
			String[] columnNames = fk.getColumnNames();
			if (columnNames.length == 1)
				addAttribute("column", columnNames[0], atts);
			addAttribute("refereeTable", fk.getForeignTable().getName(), atts);
			String[] refereeColumns = fk.getRefereeColumnNames();
			if (refereeColumns.length == 1)
				addAttribute("refereeColumn", refereeColumns[0], atts);
			writer.startElement("fk", atts);
			if (columnNames.length > 1) {
				writer.startElement("columns", null);
				for (String columnName : columnNames) {
					AttributesImpl colAtts = createAttributes("name", columnName);
					addAttribute("refereeColumn", fk.columnReferencedBy(columnName), colAtts);
					writer.startElement("column", colAtts);
					writer.endElement("column");
				}
				writer.endElement("columns");
			}
			writer.endElement("fk");
		}
	}

	private void exportIndexes(List<DBIndex> indexes, SimpleXMLWriter writer) throws SAXException {
		for (DBIndex index : indexes) {
			AttributesImpl atts = createAttributes("name", index.getName());
			addAttribute("unique", (index.isUnique() ? "true" : null), atts);
			String[] columnNames = index.getColumnNames();
			if (columnNames.length == 1)
				addAttribute("column", columnNames[0], atts);
			writer.startElement("index", atts);
			if (columnNames.length > 1)
				writeColumnGroup(columnNames, writer);
			writer.endElement("index");
		}
	}

	private AttributesImpl createAttributes(String attributeName, String attributeValue) {
		AttributesImpl atts = new AttributesImpl();
		return addAttribute(attributeName, attributeValue, atts);
	}

	private AttributesImpl addAttribute(String name, String value, AttributesImpl atts) {
		if (!StringUtil.isEmpty(value)) 
			atts.addAttribute("", "", name, "CDATA", value);
		return atts;
	}

}
