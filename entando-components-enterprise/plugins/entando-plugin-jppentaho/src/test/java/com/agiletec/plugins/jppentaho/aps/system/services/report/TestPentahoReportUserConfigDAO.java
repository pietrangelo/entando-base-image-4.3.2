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
package com.agiletec.plugins.jppentaho.aps.system.services.report;

import javax.sql.DataSource;

import com.agiletec.plugins.jppentaho.aps.JppentahoBaseTestCase;

import com.agiletec.plugins.jppentaho.aps.system.services.report.model.PentahoReportUserWidgetConfig;

public class TestPentahoReportUserConfigDAO extends JppentahoBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void test() {
		PentahoReportUserWidgetConfig config = null;
		PentahoReportUserWidgetConfig loaded = null;
		try {
			config = new PentahoReportUserWidgetConfig();
			config.setConfig("config");
			config.setFramepos(1);
			config.setPagecode("pagecode");
			config.setWidgetcode("widgetcode");
			config.setUsername("username");

			_configDAO.addConfig(config);

			loaded = _configDAO.getConfig(config);
			assertNotNull(loaded);
			assertEquals("config", loaded.getConfig());
			assertEquals("widgetcode", loaded.getWidgetcode());

			loaded.setConfig("configUpdate");
			_configDAO.updateConfig(loaded);
			loaded = _configDAO.getConfig(config);
			assertNotNull(loaded);
			assertEquals("configUpdate", loaded.getConfig());
		} finally {
			_configDAO.deleteConfig(config);
			loaded = _configDAO.getConfig(config);
			assertNull(loaded);
		}
	}
	
	private void init() {
		_configDAO = new PentahoReportUserConfigDAO();
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		((PentahoReportUserConfigDAO)_configDAO).setDataSource(dataSource);
	}
	
	private IPentahoReportUserConfigDAO _configDAO;
	
}