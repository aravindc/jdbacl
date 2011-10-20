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

package org.databene.jdbacl;

import static org.junit.Assert.*;

import org.databene.commons.version.VersionNumber;
import org.databene.jdbacl.dialect.HSQL2Dialect;
import org.databene.jdbacl.dialect.HSQLDialect;
import org.databene.jdbacl.dialect.OracleDialect;
import org.junit.Test;

/**
 * Tests the {@link DatabaseDialectManager}.<br/><br/>
 * Created: 20.10.2011 14:16:04
 * @since 0.6.12
 * @author Volker Bergmann
 */
public class DatabaseDialectManagerTest {

	@Test
	public void testPlainSettings() {
		check("Oracle", "11.2.0.2", OracleDialect.class);
	}

	@Test
	public void testVersionedSettings() {
		check("HSQLDB", "1.5.8", HSQLDialect.class);
		check("HSQLDB", "2",     HSQL2Dialect.class);
		check("HSQLDB", "2.0.0", HSQL2Dialect.class);
		check("HSQLDB", "2.9",   HSQL2Dialect.class);
	}

	private void check(String product, String version, Class<? extends DatabaseDialect> expectedClass) {
		VersionNumber versionNumber = VersionNumber.valueOf(version);
		DatabaseDialect dialect = DatabaseDialectManager.getDialectForProduct(product, versionNumber);
		assertEquals(expectedClass, dialect.getClass());
	}
}
