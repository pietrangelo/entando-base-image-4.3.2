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
package com.agiletec.plugins.jppentaho;

import junit.framework.Test;
import junit.framework.TestSuite;




import com.agiletec.plugins.jppentaho.aps.TestApsSample;
import com.agiletec.plugins.jppentaho.apsadmin.TestApsAdminSample;

/**
 * Launch class for all the jppentaho tests.
 */
public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Entando Plugin jppentaho");
		
//		suite.addTestSuite(TestReportListDOM.class);
//		suite.addTestSuite(TestReportParametersDefinitionDOM.class);
		
		suite.addTestSuite(TestApsSample.class);
		suite.addTestSuite(TestApsAdminSample.class);
				
		return suite;
	}
	
}