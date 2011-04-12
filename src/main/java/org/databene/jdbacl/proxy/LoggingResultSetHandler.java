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

package org.databene.jdbacl.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.LogCategories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Document class.<br/><br/>
 * Created: 12.04.2011 14:02:38
 * @since TODO version
 * @author Volker Bergmann
 */
public class LoggingResultSetHandler implements InvocationHandler {

    private static final Logger JDBC_LOGGER = LoggerFactory.getLogger(LogCategories.JDBC);
    
    private static volatile AtomicInteger openResultSetCount = new AtomicInteger();

	// attributes ------------------------------------------------------------------------------------------------------

	private ResultSet realResultSet;
	
	// constructor -----------------------------------------------------------------------------------------------------

	public LoggingResultSetHandler(ResultSet realResultSet) {
		this.realResultSet = realResultSet;
		openResultSetCount.incrementAndGet();
		JDBC_LOGGER.debug("created result set {}", this);
	}
	
	// InvocationHandler interface implementation ----------------------------------------------------------------------

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			String methodName = method.getName();
			if ("close".equals(methodName)) {
				openResultSetCount.decrementAndGet();
				JDBC_LOGGER.debug("closing result set {}", this);
			}
			return BeanUtil.invoke(realResultSet, method, args);
		} catch (ConfigurationError e) {
			if (e.getCause() instanceof InvocationTargetException && e.getCause().getCause() instanceof SQLException)
				throw e.getCause().getCause();
			else
				throw e;
		}
	}

	// tracking methods ------------------------------------------------------------------------------------------------
	
	public static int getOpenResultSetCount() {
		return openResultSetCount.get();
	}

	public static void resetOpenResultSetCount() {
		openResultSetCount.set(0);
	}
	
}
