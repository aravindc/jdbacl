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

package org.databene.jdbacl.model.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.databene.commons.IOUtil;
import org.databene.commons.ImportFailedException;
import org.databene.jdbacl.model.DBMetaDataImporter;
import org.databene.jdbacl.model.Database;

/**
 * Deserializes a database model from a file.<br/><br/>
 * Created: 12.08.2010 10:14:21
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class DeserializingImporter implements DBMetaDataImporter {
	
	private File file;
	
	public DeserializingImporter(File file) {
	    this.file = file;
    }

	public Database importDatabase() throws ImportFailedException {
	    ObjectInputStream in = null;
	    try {
		    in = new ObjectInputStream(new FileInputStream(file));
	    	return (Database) in.readObject();
	    } catch (Exception e) {
	        throw new ImportFailedException("Database import from file '" + file + "' failed", e);
        } finally {
	    	IOUtil.close(in);
	    }
    }

}
