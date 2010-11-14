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

package org.databene.jdbacl.model.pd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.ParseException;
import org.databene.commons.StringUtil;
import org.databene.commons.tree.TreeLogger;
import org.databene.commons.xml.XMLUtil;
import org.databene.jdbacl.model.DBColumn;
import org.databene.jdbacl.model.DBColumnType;
import org.databene.jdbacl.model.DBCompositeObject;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBTreeModel;
import org.databene.jdbacl.model.DefaultDBTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * TODO Document class.<br/><br/>
 * Created: 09.11.2010 10:59:02
 * @since 1.0
 * @author Volker Bergmann
 */
public class PDMParser {

	public PDModel parsePDM(File file) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			Document doc = XMLUtil.parse(in);
			return parseRootModel(doc.getDocumentElement());
		} finally {
			IOUtil.close(in);
		}
	}

	private PDModel parseRootModel(Element e) {
		assertName("Model", e);
		return parseRootObject(getSingleChild(e));
	}

	private PDModel parseRootObject(Element e) {
		assertName("o:RootObject", e);
		return parseRootObjectChildren(getSingleChild(e));
	}

	private PDModel parseRootObjectChildren(Element e) {
		assertName("c:Children", e);
		return parseModel(getSingleChild(e));
	}

	private PDModel parseModel(Element e) {
		assertName("o:Model", e);
		String name = codeChild(e);
		PDModel model = new PDModel(name);
		Element pe = XMLUtil.getChildElement(e, false, false, "Packages");
		if (pe != null)
			parsePackages(pe, model);
		Element de = XMLUtil.getChildElement(e, false, false, "Domains");
		if (de != null)
			parseDomains(de, model);
		return model;
	}

	private void parsePackages(Element e, DBCompositeObject<?> owner) {
		assertName("c:Packages", e);
		for (Element child : XMLUtil.getChildElements(e))
			if ("o:Package".equals(child.getNodeName()))
				parsePackage(child, owner);
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DBPackage parsePackage(Element e, DBCompositeObject owner) {
		assertName("o:Package", e);
		DBPackage p = new DBPackage(codeChild(e));
		owner.addComponent(p);
		for (Element c : XMLUtil.getChildElements(e)) {
			String cn = c.getNodeName();
			if ("c:Packages".equals(cn))
				parsePackages(c, p);
			else if ("c:Tables".equals(cn))
				parseTables(c, p);
		}
		return p;
    }

	private void parseTables(Element e, DBPackage owner) {
		assertName("c:Tables", e);
		for (Element c : XMLUtil.getChildElements(e, false, "Table"))
			parseTable(c, owner);
    }

	private DBTable parseTable(Element e, DBPackage owner) {
		assertName("o:Table", e);
		DBTable table = new DefaultDBTable(codeChild(e));
		owner.addComponent(table);
		Element ce = XMLUtil.getChildElement(e, false, false, "Columns");
		if (ce != null)
			parseColumns(ce, table);
		return table;
	}

	private void parseColumns(Element e, DBTable table) {
		assertName("c:Columns", e);
		for (Element c : XMLUtil.getChildElements(e, false, "Column"))
			parseColumn(c, table);
	}

	private DBColumn parseColumn(Element e, DBTable table) {
		assertName("o:Column", e);
		String name = codeChild(e);
		String type = textChild(e, "DataType");
		if (type.indexOf('(') >= 0)
			type = type.substring(0, type.indexOf('('));
		String length = textChild(e, "Length", false);
		Integer size = (StringUtil.isEmpty(length) ? null : Integer.parseInt(length));
		// TODO evaluate ListOfValues
		// TODO evaluate LowValue and HighValue
		return new DBColumn(table, name, DBColumnType.getInstance(type), size, 0 /* TODO fractionDigits */);
	}

	private void parseDomains(Element e, PDModel model) {
		assertName("c:Domains", e);
		for (Element c : XMLUtil.getChildElements(e, false, "PhysicalDomain"))
			parseDomain(c, model);
    }

	private PDDomain parseDomain(Element e, PDModel model) {
		assertName("o:PhysicalDomain", e);
		String name = codeChild(e);
		String type = textChild(e, "DataType");
		if (type.indexOf('(') >= 0)
			type = type.substring(0, type.indexOf('('));
		String length = textChild(e, "Length", false);
		Integer size = (StringUtil.isEmpty(length) ? null : Integer.parseInt(length));
		// TODO evaluate ListOfValues
		// TODO evaluate LowValue and HighValue
		return new PDDomain(model, name, DBColumnType.getInstance(type), size, 0 /* TODO fractionDigits */);
	}

	private void assertName(String expectedName, Element element) {
		String actualName = element.getNodeName();
		if (!expectedName.equals(actualName))
			throw new ConfigurationError("Expected element name '" + expectedName + "', " +
					"but found '" + actualName);
	}
	
	private void assertSingleElement(Element[] array, Element e) {
		if (array.length != 1)
			throw new ParseException("Expected one child element under '" + e.getNodeName() + "', " +
					"but found " + array.length, XMLUtil.format(e));
	}

	private Element getSingleChild(Element e) {
		Element[] cs = XMLUtil.getChildElements(e);
		assertSingleElement(cs, e);
		return cs[0];
	}

	private String codeChild(Element e) {
		return textChild(e, "Code");
	}

	private String textChild(Element element, String childName) {
		return textChild(element, childName, true);
	}

	private String textChild(Element element, String childName, boolean required) {
		Element childElement = XMLUtil.getChildElement(element, false, required, childName);
		return (childElement != null ? childElement.getTextContent() : null);
	}

	public static void main(String[] args) throws IOException {
		PDModel model = new PDMParser().parsePDM(new File("GLOBEPDMLVL2REL1_1_2.pdm"));
		System.out.println(model);
		new TreeLogger().log(new DBTreeModel(model));
	}
	
}
