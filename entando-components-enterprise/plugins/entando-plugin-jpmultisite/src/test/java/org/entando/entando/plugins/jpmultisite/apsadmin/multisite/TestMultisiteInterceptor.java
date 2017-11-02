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
package org.entando.entando.plugins.jpmultisite.apsadmin.multisite;


import com.agiletec.ConfigTestUtils;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import static junit.framework.Assert.assertEquals;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;

/**
 * @author S.Loru
 */
public class TestMultisiteInterceptor extends ApsAdminBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.initAction("/do", "doLogin");
	}
	
	public void testSuccessfulLogin() throws Throwable {
		String multisiteCode = (String) this.getRequest().getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		assertNull(multisiteCode);
		String result = this.executeLogin("admin", "admin");
		assertEquals(Action.SUCCESS, result);
		multisiteCode = (String) this.getRequest().getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		assertEquals("", multisiteCode);
	}
	
	private String executeLogin(String username, String password) throws Throwable {
		this.addParameter("username", username);
		this.addParameter("password", password);
		String result = super.executeAction();
		return result;
	}

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new MultisiteConfigTestUtils();
	}
	
}
