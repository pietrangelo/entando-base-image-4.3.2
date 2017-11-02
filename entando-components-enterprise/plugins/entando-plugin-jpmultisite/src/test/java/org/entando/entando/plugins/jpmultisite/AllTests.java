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
package org.entando.entando.plugins.jpmultisite;


import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.TestSharedContentManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.TestMultisiteCloneManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.TestMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype.TestMultisiteWidgetTypeManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.category.TestMultisiteCategoryAction;
import org.entando.entando.plugins.jpmultisite.apsadmin.common.TestMultisiteSwitchAction;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.TestMultisiteAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Change me with a suitable description");

		suite.addTestSuite(TestMultisiteManager.class);
		suite.addTestSuite(TestMultisiteCloneManager.class);
		suite.addTestSuite(TestMultisiteWidgetTypeManager.class);
		suite.addTestSuite(TestMultisiteCategoryAction.class);
		suite.addTestSuite(TestMultisiteAction.class);
		suite.addTestSuite(TestMultisiteSwitchAction.class);
		suite.addTestSuite(TestSharedContentManager.class);
		
		return suite;
	}
	
}
