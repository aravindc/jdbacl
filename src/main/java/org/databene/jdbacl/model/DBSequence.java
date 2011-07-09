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

package org.databene.jdbacl.model;

import java.math.BigInteger;

import org.databene.commons.NullSafeComparator;

/**
 * Represents a database sequence.<br/><br/>
 * Created: 31.05.2011 17:55:05
 * @since 0.6.8
 * @author Volker Bergmann
 */
public class DBSequence extends AbstractDBObject implements DBPackageComponent {

	private static final long serialVersionUID = 8602052311285255364L;

	private static final long CACHE_DEFAULT = 20; // TODO v0.7 this applies only for Oracle
	
	private String catalogName;
	private String schemaName;
	private BigInteger start = BigInteger.ONE;
	private BigInteger increment = BigInteger.ONE;
	private BigInteger maxValue = null;
	private BigInteger minValue = null;
	private boolean cycle = false;
	private long cache = CACHE_DEFAULT;
	private boolean order = false;
	private BigInteger lastNumber = BigInteger.ZERO;

	public DBSequence(String name, DBSchema owner) {
		super(name, "sequence", owner);
	}

	public DBSequence(String name, String catalogName, String schemaName) {
		super(name, "sequence", null);
		this.catalogName = catalogName;
		this.schemaName = schemaName;
	}

	@Override
	public void setOwner(CompositeDBObject<?> owner) {
		super.setOwner(owner);
        if (owner != null) {
    		DBSchema schema = (DBSchema) owner;
    		schema.addSequence(this);
        	this.catalogName = schema.getCatalog().getName();
        	this.schemaName = schema.getName();
        }
	}

	public String getCatalogName() {
		return catalogName;
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	
	public BigInteger getStart() {
		return start;
	}

	public BigInteger getStartIfNotDefault() {
		return (BigInteger.ONE.equals(this.start) ? null : this.start);
	}

	public void setStart(BigInteger start) {
		this.start = start;
	}

	public BigInteger getIncrement() {
		return increment;
	}
	
	public BigInteger getIncrementIfNotDefault() {
		return (BigInteger.ONE.equals(increment) ?  null : increment);
	}
	
	public void setIncrement(BigInteger increment) {
		this.increment = increment;
	}

	public BigInteger getMaxValue() {
		return maxValue;
	}

	public BigInteger getMaxValueIfNotDefault() {
		return maxValue;
	}

	public void setMaxValue(BigInteger maxValue) {
		this.maxValue = maxValue;
	}

	public BigInteger getMinValue() {
		return minValue;
	}

	public BigInteger getMinValueIfNotDefault() {
		return minValue;
	}

	public void setMinValue(BigInteger minValue) {
		this.minValue = minValue;
	}

	public boolean isCycle() {
		return cycle;
	}

	public Boolean getCycleIfNotDefault() {
		return (cycle ? cycle : null);
	}

	public void setCycle(boolean cycle) {
		this.cycle = cycle;
	}

	public long getCache() {
		return cache;
	}
	
	public Long getCacheIfNotDefault() {
		return (cache != CACHE_DEFAULT ? cache : null);
	}
	
	public void setCache(long cache) {
		this.cache = cache;
	}

	public boolean isOrder() {
		return order;
	}

	public Boolean getOrderIfNotDefault() {
		return (order ? true : null);
	}
	
	public void setOrder(boolean order) {
		this.order = order;
	}

	public BigInteger getLastNumber() {
		return lastNumber;
	}
	
	public void setLastNumber(BigInteger lastNumber) {
		this.lastNumber = lastNumber;
	}
	
	public boolean isIdentical(DBObject other) {
		if (this == other)
			return true;
		if (!(other instanceof DBSequence))
			return false;
		DBSequence that = (DBSequence) other;
		return this.name.equals(that.getName()) &&
			this.start == that.getStart() &&
			this.increment == that.getIncrement() &&
			NullSafeComparator.equals(this.maxValue, that.getMaxValue()) &&
			NullSafeComparator.equals(this.minValue, that.getMinValue()) &&
			this.cycle == that.isCycle() &&
			this.cache == that.getCache() &&
			this.order == that.isOrder();
	}

	public String createDDL() {
		StringBuilder builder = new StringBuilder("create sequence ").append(name);
		if (!BigInteger.ONE.equals(start))
			builder.append(" start with ").append(start);
		if (!BigInteger.ONE.equals(increment))
			builder.append(" increment by ").append(increment);
		if (maxValue != null)
			builder.append(" maxvalue ").append(maxValue);
		if (minValue != null)
			builder.append(" minvalue ").append(minValue);
		if (cycle)
			builder.append(" cycle ");
		if (cache <= 1)
			builder.append(" nocache ");
		else if (cache != CACHE_DEFAULT)
			builder.append(" cache ").append(cache);
		if (order)
			builder.append(" order");
		return builder.toString();
	}
	
	public String dropDDL() {
		return "drop sequence " + name;
	}

}
