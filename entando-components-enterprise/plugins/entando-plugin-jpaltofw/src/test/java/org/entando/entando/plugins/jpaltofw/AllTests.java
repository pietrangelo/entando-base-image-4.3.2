package org.entando.entando.plugins.jpaltofw;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.plugins.jpaltofw.aps.system.services.TestAltoManager;
import org.entando.entando.plugins.jpaltofw.aps.system.services.parse.TestAltoConfigDOM;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test JpAlto ");
		suite.addTestSuite(TestAltoConfigDOM.class);
		suite.addTestSuite(TestAltoManager.class);
		//suite.addTestSuite(TestAltoConfigAction.class);
		return suite;
	}
	
}
