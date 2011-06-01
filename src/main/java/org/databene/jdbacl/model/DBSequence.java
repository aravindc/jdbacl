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

import org.databene.commons.NullSafeComparator;

/**
 * TODO Document class.<br/><br/>
 * Created: 31.05.2011 17:55:05
 * @since TODO version
 * @author Volker Bergmann
 */
public class DBSequence extends AbstractDBObject {

	private static final long serialVersionUID = 8602052311285255364L;

	private static final long CACHE_DEFAULT = 20; // TODO this applies only for Oracle
	
	private long start = 1;
	private long increment = 1;
	private Long maxValue = null;
	private Long minValue = null;
	private boolean cycle = false;
	private long cache = CACHE_DEFAULT;
	private boolean order = false;
	private long lastNumber = 0;

	public DBSequence(String name, CompositeDBObject<?> owner) {
		super(name, "sequence", owner);
	}

	public long getStart() {
		return start;
	}

	public Long getStartIfNotDefault() {
		return (this.start != 1 ? this.start : null);
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getIncrement() {
		return increment;
	}
	
	public Long getIncrementIfNotDefault() {
		return (increment != 1 ? increment : null);
	}
	
	public void setIncrement(long increment) {
		this.increment = increment;
	}

	public Long getMaxValue() {
		return maxValue;
	}

	public Long getMaxValueIfNotDefault() {
		return maxValue;
	}

	public void setMaxValue(Long maxValue) {
		this.maxValue = maxValue;
	}

	public Long getMinValue() {
		return minValue;
	}

	public Long getMinValueIfNotDefault() {
		return minValue;
	}

	public void setMinValue(Long minValue) {
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

	public long getLastNumber() {
		return lastNumber;
	}
	
	public void setLastNumber(long lastNumber) {
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
		if (start != 1)
			builder.append(" start with ").append(start);
		if (increment != 1)
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
