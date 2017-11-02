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
package org.entando.entando.plugins.jpsalesforce;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.entando.entando.plugins.jpsalesforce.aps.internalservlet.leads.TestLeadAction;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.TestSalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceForm.TestFormConfiguration;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceForm.TestSalesforceFormManager;
import org.entando.entando.plugins.jpsalesforce.apsadmin.config.TestSalesforceConfigAction;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.TestSalesforceFormAction;


public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Salesforce tests");

		suite.addTestSuite(TestSalesforceManager.class);
		suite.addTestSuite(TestSalesforceFormAction.class);
		suite.addTestSuite(TestSalesforceFormManager.class);
		suite.addTestSuite(TestFormConfiguration.class);
		suite.addTestSuite(TestLeadAction.class);
		suite.addTestSuite(TestSalesforceConfigAction.class);
		
		return suite;
	}
	
}
