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
package com.agiletec.plugins.jpforum;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.attach.TestAttachAction;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.message.TestMessageAction;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.post.TestPostAction;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.section.TestSectionBrowseAction;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.thread.TestThreadAction;
import com.agiletec.plugins.jpforum.aps.system.services.config.TestConfigManager;
import com.agiletec.plugins.jpforum.aps.system.services.markup.TestMarkupParser;
import com.agiletec.plugins.jpforum.aps.system.services.message.TestMessageManager;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.TestSanctionManager;
import com.agiletec.plugins.jpforum.aps.system.services.searchengine.TestForumSearchEngineManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.TestSectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.TestStatisticManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.TestThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.attach.TestAttachManager;
import com.agiletec.plugins.jpforum.apsadmin.config.TestConfigAction;
import com.agiletec.plugins.jpforum.apsadmin.section.TestSectionAction;
import com.agiletec.plugins.jpforum.apsadmin.section.TestSectionTreeAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.agiletec.plugins.jpforum");
		
		//SERVICES
		suite.addTestSuite(TestMarkupParser.class);
		suite.addTestSuite(TestMessageManager.class);
		suite.addTestSuite(TestSanctionManager.class);		
		suite.addTestSuite(TestForumSearchEngineManager.class);		
		suite.addTestSuite(TestSectionManager.class);
		suite.addTestSuite(TestStatisticManager.class);
		suite.addTestSuite(TestThreadManager.class);
		suite.addTestSuite(TestAttachManager.class);
		suite.addTestSuite(TestConfigManager.class);
		//INTERNAL_SERVLET
		suite.addTestSuite(TestAttachAction.class);
		suite.addTestSuite(TestMessageAction.class);
		suite.addTestSuite(TestPostAction.class);
		suite.addTestSuite(TestSectionAction.class);
		suite.addTestSuite(TestSectionBrowseAction.class);
		suite.addTestSuite(TestThreadAction.class);
		//ADMIN
		suite.addTestSuite(TestSectionTreeAction.class);
		suite.addTestSuite(TestSectionAction.class);
		suite.addTestSuite(TestConfigAction.class);
		
		return suite;
	}

}
