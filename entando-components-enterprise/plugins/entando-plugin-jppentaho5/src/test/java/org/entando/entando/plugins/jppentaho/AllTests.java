package org.entando.entando.plugins.jppentaho;

import org.entando.entando.plugins.jppentaho.aps.TestApsSample;
import org.entando.entando.plugins.jppentaho.aps.TestRepositoryFileService;
import org.entando.entando.plugins.jppentaho.apsadmin.TestApsAdminSample;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Change me with a suitable description");

		suite.addTestSuite(TestRepositoryFileService.class);
		suite.addTestSuite(TestApsSample.class);
		suite.addTestSuite(TestApsAdminSample.class);
		
		return suite;
	}
	
}
