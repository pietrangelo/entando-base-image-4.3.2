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
package org.entando.entando.plugins.jpjasper.aps.services.jasperserver;


import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;

public class TestRestResource extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetRestResource() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);

			String cookie = this._jasperServerManager.getCookieHeader(user);
			assertNotNull(cookie);
			
			JasperResourceDescriptor jasperResourceDescriptor = this._jasperServerManager.getRestResource(cookie, "/reports/samples/Department");
			
			assertNotNull(jasperResourceDescriptor);
		} finally {
			this._userManager.removeUser(username);
		}
	}

	
	private void init() {
		this._jasperServerManager = (IJasperServerManager) this.getApplicationContext().getBean("jpjasperServerManager");
		this._userManager = (IUserManager) this.getApplicationContext().getBean(SystemConstants.USER_MANAGER);
	}

	private IJasperServerManager _jasperServerManager;
	private IUserManager _userManager;
}

