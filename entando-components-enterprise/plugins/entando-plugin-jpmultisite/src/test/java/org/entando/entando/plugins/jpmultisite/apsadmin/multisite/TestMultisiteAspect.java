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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;
import static junit.framework.Assert.assertEquals;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.MultisiteTestHelper;

/**
 * @author S.Loru
 */
public class TestMultisiteAspect extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() throws Exception {
		this._multisiteManager = (IMultisiteManager) this.getService(JpmultisiteSystemConstants.MULTISITE_MANAGER);
		MultisiteTestHelper.addMultisiteForTesting(_multisiteManager);
	}
	

	public void testCallAction() throws Throwable {
		this.setUserOnSession("admin@asd");
		this.initAction("/do/jpmultisite/Multisite", "new");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		MultisiteTestHelper.deleteAllMultisite(_multisiteManager);
	}

	private IMultisiteManager _multisiteManager;

}
