/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.multisite;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.SystemConstants;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * @author S.Loru
 */
public class MultisiteConfigTestUtils extends ConfigTestUtils{

	public MultisiteConfigTestUtils() {
	}
	
	public MultisiteConfigTestUtils(String url) {
		_applicationBaseUrl = url;
	}
	

	@Override
	protected SimpleNamingContextBuilder createNamingContext() {
		SimpleNamingContextBuilder builder = null;
		try {
			builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
			InputStream in = new FileInputStream("target/test/conf/contextTestParams.properties");
			Properties testConfig = new Properties();
			testConfig.load(in);
			in.close();
			
			builder.bind("java:comp/env/logName", testConfig.getProperty("logName"));
			builder.bind("java:comp/env/logFilePrefix", testConfig.getProperty("logFilePrefix"));
			builder.bind("java:comp/env/logLevel", testConfig.getProperty("logLevel"));
			builder.bind("java:comp/env/logFileSize", testConfig.getProperty("logFileSize"));
			builder.bind("java:comp/env/logFilesCount", testConfig.getProperty("logFilesCount"));
			
			builder.bind("java:comp/env/configVersion", testConfig.getProperty("configVersion"));
			
			builder.bind("java:comp/env/applicationBaseURL", testConfig.getProperty("applicationBaseURL"));
			builder.bind("java:comp/env/resourceRootURL", testConfig.getProperty("resourceRootURL"));
			builder.bind("java:comp/env/protectedResourceRootURL", testConfig.getProperty("protectedResourceRootURL"));
			builder.bind("java:comp/env/resourceDiskRootFolder", testConfig.getProperty("resourceDiskRootFolder"));
			builder.bind("java:comp/env/protectedResourceDiskRootFolder", testConfig.getProperty("protectedResourceDiskRootFolder"));
			
			builder.bind("java:comp/env/indexDiskRootFolder", testConfig.getProperty("indexDiskRootFolder"));
			testConfig.put(SystemConstants.PAR_APPL_BASE_URL, _applicationBaseUrl);
			Iterator<Map.Entry<Object, Object>> configIter = testConfig.entrySet().iterator();
			while (configIter.hasNext()) {
				Map.Entry<Object, Object> entry = configIter.next();
				builder.bind("java:comp/env/" + (String) entry.getKey(), (String) entry.getValue()); 
			}
			
			this.createDatasources(builder, testConfig);
		} catch (Throwable t) {
			throw new RuntimeException("Error on creation naming context", t);
		}
		return builder;
	}
	
	private void createDatasources(SimpleNamingContextBuilder builder, Properties testConfig) {
		List<String> dsNameControlKeys = new ArrayList<String>();
		Enumeration<Object> keysEnum = testConfig.keys();
		while (keysEnum.hasMoreElements()) {
			String key = (String) keysEnum.nextElement();
			if (key.startsWith("jdbc.")) {
				String[] controlKeys = key.split("\\.");
				String dsNameControlKey = controlKeys[1];
				if (!dsNameControlKeys.contains(dsNameControlKey)) {
					this.createDatasource(dsNameControlKey, builder, testConfig);
					dsNameControlKeys.add(dsNameControlKey);
				}
			}
		}
	}
	
	private void createDatasource(String dsNameControlKey, SimpleNamingContextBuilder builder, Properties testConfig) {
		String beanName = testConfig.getProperty("jdbc."+dsNameControlKey+".beanName");
		try {
			String className = testConfig.getProperty("jdbc."+dsNameControlKey+".driverClassName");
			String url = testConfig.getProperty("jdbc."+dsNameControlKey+".url");
			String username = testConfig.getProperty("jdbc."+dsNameControlKey+".username");
			String password = testConfig.getProperty("jdbc."+dsNameControlKey+".password");
			Class.forName(className);
			BasicDataSource ds = new BasicDataSource();
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setMaxActive(8);
			ds.setMaxIdle(4);
			ds.setDriverClassName(className);
			builder.bind("java:comp/env/jdbc/" + beanName, ds);
		} catch (Throwable t) {
			throw new RuntimeException("Error on creation datasource '" + beanName + "'", t);
		}
	}

	
	private String _applicationBaseUrl = "http://localhost";

}
