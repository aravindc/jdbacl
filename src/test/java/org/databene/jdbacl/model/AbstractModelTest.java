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

package org.databene.jdbacl.model;

/**
 * Abstract parent class for testing XML in-/output of jdbacl models.<br/><br/>
 * Created: 28.11.2010 09:58:56
 * @since 0.6.4
 * @author Volker Bergmann
 */
public abstract class AbstractModelTest {
	
	protected static String TEST_MODEL_FILENAME = "org/databene/jdbacl/model/xml/testmodel.xml";

	@SuppressWarnings("unused")
	protected static Database createTestModel() {
		Database db = new DefaultDatabase("db1");
		DBCatalog catalog = new DBCatalog("cat1", db);
		DBSchema schema = new DBSchema("schema1", catalog);
		
		DefaultDBTable table1 = new DefaultDBTable("table1", schema);
		DBColumn id1 = new DefaultDBColumn("id1", table1, "int");
		table1.addColumn(id1); // TODO add owner table to ctor args?
		DBColumn name1 = new DefaultDBColumn("name1", table1, "int");
		DBPrimaryKeyConstraint pk1 = new DBPrimaryKeyConstraint(table1, "table1_pk", "id1");
		DBUniqueConstraint uk1 = new DBUniqueConstraint(table1, "table1_name1_uk", "name1");
		DBIndex index1 = new DBUniqueIndex("index1", uk1);
		
		DefaultDBTable table2 = new DefaultDBTable("table2", schema);
		DBColumn id2 = new DefaultDBColumn("id2", table2, "int");
		DBColumn ref2 = new DefaultDBColumn("ref2", table2, "int");
		DBPrimaryKeyConstraint pk2 = new DBPrimaryKeyConstraint(table2, "table2_pk", "id2");
		DBForeignKeyConstraint fk2 = new DBForeignKeyConstraint("table2_fk2", table2, new String[] {"ref2"}, table1, new String[] {"id1"});
		
		DefaultDBTable table3 = new DefaultDBTable("table3", schema);
		DBColumn id3_1 = new DefaultDBColumn("id3_1", table3, "int");
		DBColumn id3_2 = new DefaultDBColumn("id3_2", table3, "int");
		DBColumn name3 = new DefaultDBColumn("name3", table3, "varchar(8)");
		DBColumn type3 = new DefaultDBColumn("type3", table3, "char");
		DBPrimaryKeyConstraint pk3 = new DBPrimaryKeyConstraint(table3, "table3_pk", "id3_1", "id3_2");
		DBUniqueConstraint uk3 = new DBUniqueConstraint(table3, "table3_name3_uk", "name3", "type3");
		
		DefaultDBTable table4 = new DefaultDBTable("table4", schema);
		DBColumn id4 = new DefaultDBColumn("id4", table4, "int");
		DBColumn ref4_1 = new DefaultDBColumn("ref4_1", table4, "int");
		DBColumn ref4_2 = new DefaultDBColumn("ref4_2", table4, "int");
		DBPrimaryKeyConstraint pk4 = new DBPrimaryKeyConstraint(table4, "table4_pk", "id4");
		DBForeignKeyConstraint fk4 = new DBForeignKeyConstraint("table4_fk2", table4, new String[] {"ref4_1", "ref4_2"}, 
				table3, new String[] {"id3_1", "id3_2"});
		DBIndex index4 = new DBNonUniqueIndex("index4", table4, "ref4_1", "ref4_2");
		
		return db;
	}
	
}
