/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.keygenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorDAO;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

public class KeyGeneratorDAO extends AbstractDAO implements IKeyGeneratorDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(KeyGeneratorDAO.class);
	
	@Override
	public int getUniqueKey() {
		Connection conn = null;
		int key = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			String query = this.getQueries().getProperty(this.getType(this.getDataSource()));
			if (null == query) {
				query = DEFAULT_QUERY;
			}
			res = stat.executeQuery(query);
			if (res.next()) {
				key = res.getInt(1);
			}
		} catch (Throwable t) {
			_logger.error("errore in recupero sequenza",  t);
			throw new RuntimeException("errore in recupero sequenza", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return key;
	}
	
	protected String getType(DataSource dataSource) throws ApsSystemException {
		String typeString = null;
		try {
			String driverClassName = this.invokeGetMethod("getDriverClassName", dataSource);
			Iterator<Object> typesIter = this.getDatabaseTypeDrivers().keySet().iterator();
			while (typesIter.hasNext()) {
				String typeCode = (String) typesIter.next();
				List<String> driverClassNames = (List<String>) this.getDatabaseTypeDrivers().get(typeCode);
				if (null != driverClassNames && driverClassNames.contains(driverClassName)) {
					typeString = typeCode;
					break;
				}
			}
			return typeString.toUpperCase();
		} catch (Throwable t) {
			_logger.error("Invalid type for db - '{}' - ", typeString, t);
			throw new ApsSystemException("Invalid type for db - '" + typeString + "'", t);
		}
	}
	
	protected String invokeGetMethod(String methodName, DataSource dataSource) throws Throwable {
		Method method = dataSource.getClass().getDeclaredMethod(methodName);
		return (String) method.invoke(dataSource);
	}
	
	@Override
	public void updateKey(int currentKey) {
		//NOTHING TO DO
	}
	
	public Properties getQueries() {
		return _queries;
	}
	public void setQueries(Properties queries) {
		this._queries = queries;
	}
	
	protected Properties getDatabaseTypeDrivers() {
		return _databaseTypeDrivers;
	}
	public void setDatabaseTypeDrivers(Properties databaseTypeDrivers) {
		this._databaseTypeDrivers = databaseTypeDrivers;
	}
	
	private final String DEFAULT_QUERY = "SELECT nextval('uniquekey')";

	private Properties _queries;
	private Properties _databaseTypeDrivers;
	
}
