/*
 * (c) Copyright 2012 by Volker Bergmann. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.ArrayUtil;
import org.databene.commons.CollectionUtil;
import org.databene.jdbacl.SQLUtil;

/**
 * TODO Document class.<br/><br/>
 * Created: 22.03.2012 21:00:34
 * @since TODO version
 * @author Volker Bergmann
 */
public class ForeignKeyPath {
	
	private String startTable;
	private List<DBForeignKeyConstraint> edges;

	public ForeignKeyPath(DBForeignKeyConstraint... edges) {
		this.startTable = (ArrayUtil.isEmpty(edges) ? null : edges[0].getTable().getName());
		this.edges = CollectionUtil.toList(edges);
	}

	public ForeignKeyPath(String startTable) {
		this.startTable = startTable;
		this.edges = new ArrayList<DBForeignKeyConstraint>();
	}

	public ForeignKeyPath(ForeignKeyPath prototype) {
		this.startTable = prototype.startTable;
		this.edges = new ArrayList<DBForeignKeyConstraint>(prototype.edges);
	}

	public String getStartTable() {
		return startTable;
	}
	
	public List<DBForeignKeyConstraint> getEdges() {
		return edges;
	}

	public void addEdge(DBForeignKeyConstraint fk) {
		if (edges.size() == 0) {
			if (startTable == null)
				startTable = fk.getTable().getName();
			else if (!startTable.equals(fk.getTable().getName()))
				throw new IllegalArgumentException("Expected reference from " + startTable + ", " +
					"but found one to " + fk.getTable());
		}
		edges.add(fk);
	}

	public String getTargetTable() {
		if (edges.size() > 0)
			return edges.get(edges.size() - 1).getRefereeTable().getName();
		else
			return startTable;
	}

	public ForeignKeyPath derivePath(DBForeignKeyConstraint fk) {
		ForeignKeyPath result = new ForeignKeyPath(this);
		result.addEdge(fk);
		return result;
	}

	public List<DBTable> getIntermediates() {
		List<DBTable> result = new ArrayList<DBTable>(edges.size() - 1);
		for (int i = 0; i < edges.size() - 1; i++)
			result.add(edges.get(i).getRefereeTable());
		return result;
	}

	public boolean hasIntermediate(DBTable intermediate) {
		for (int i = 0; i < edges.size() - 1; i++)
			if (edges.get(i).getRefereeTable().equals(intermediate))
				return true;
		return false;
	}

	public String[] getEndColumnNames() {
		return CollectionUtil.lastElement(edges).getRefereeColumnNames();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName()).append('[');
		for (DBForeignKeyConstraint edge : edges)
			builder.append(edge.getTable().getName())
				.append(SQLUtil.renderColumnNames(edge.getColumnNames()))
				.append(" -> ");
		if (edges.size() > 0) {
			DBForeignKeyConstraint endEdge = CollectionUtil.lastElement(edges);
			builder.append(endEdge.getRefereeTable().getName()).append(SQLUtil.renderColumnNames(endEdge.getRefereeColumnNames()));
		}
		return builder.append(']').toString();
	}
	
}
