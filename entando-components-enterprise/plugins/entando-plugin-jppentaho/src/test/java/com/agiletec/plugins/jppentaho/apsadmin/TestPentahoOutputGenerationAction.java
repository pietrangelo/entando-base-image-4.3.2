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
package com.agiletec.plugins.jppentaho.apsadmin;

import com.opensymphony.xwork2.Action;

public class TestPentahoOutputGenerationAction extends JpPentahoApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private void init() {
	}
	
	public void test() throws Throwable {
		this.setUserOnSession("admin");
		try {
			this.initAction("/do/jppentaho/Front/pentaho", "config");
			this.addParameter("currentPage", "homepage");
			this.addParameter("frameSource", "0");
			this.addParameter("frameDest", "2");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		}
	}
	
}