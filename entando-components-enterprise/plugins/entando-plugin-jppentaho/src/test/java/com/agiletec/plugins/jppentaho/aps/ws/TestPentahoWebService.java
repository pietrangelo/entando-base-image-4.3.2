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
package com.agiletec.plugins.jppentaho.aps.ws;

import com.agiletec.plugins.jppentaho.aps.JppentahoBaseTestCase;

public class TestPentahoWebService extends JppentahoBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();		
	}
	
	public void test() throws Throwable {
		String solution = "steel-wheels"; 
		String path = "reports";
		String server =  "http://127.0.0.1:8180/";
		String username = "joe";
		String password = "password";
		String target = "_blank";
		
		String xml = null;
		xml = _pentahoWebService.getSolutionDirectoryList(solution, path, server, "pentaho", username, password, target);
		System.out.println(" XML " + xml);
		System.out.println("\n\n\n\n");
		xml = _pentahoWebService.getList(solution, path, server, "pentaho", username, password, target);
		System.out.println(" XML " + xml);
	}
	
	private void init() {
		_pentahoWebService = (IPentahoWebService) this.getApplicationContext().getBean("jppentahoPentahoWebService");
	}
	
	private IPentahoWebService _pentahoWebService;
	
}