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

import java.sql.Connection;
import java.sql.ResultSet;

import org.databene.commons.iterator.ConvertingIterator;
import org.databene.commons.iterator.TableRowIterator;

/**
 * Provides a {@link ResultSet}'s rows as {@link Object} arrays.<br/><br/>
 * Created: 13.10.2010 13:38:19
 * @since 0.6.4
 * @author Volker Bergmann
 */
public class ArrayResultSetIterator extends ConvertingIterator<ResultSet, Object[]> implements TableRowIterator {
	
	public ArrayResultSetIterator(Connection connection, String query) {
	    super(new QueryIterator(query, connection, 500), new ResultSetConverter<Object[]>(Object[].class, false));
    }

	public String[] getColumnLabels() {
		return ((QueryIterator) source).getColumnLabels();
	}
	
}
