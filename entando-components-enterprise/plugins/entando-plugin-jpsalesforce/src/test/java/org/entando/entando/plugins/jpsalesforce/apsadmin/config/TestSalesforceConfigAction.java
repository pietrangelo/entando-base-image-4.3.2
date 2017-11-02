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
package org.entando.entando.plugins.jpsalesforce.apsadmin.config;

import org.entando.entando.plugins.jpsalesforce.apsadmin.ApsAdminPluginBaseTestCase;

public class TestSalesforceConfigAction extends ApsAdminPluginBaseTestCase {
	
	public void testConfigEdit() throws Throwable {
		try {
			setUserOnSession("admin");
			this.initAction("/do/jpsalesforce/Config", "edit");
			String result = this.executeAction();
			assertEquals("success", result);
		} catch (Throwable t) {
			throw t;
		}
	}

}
