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

package org.entando.entando.plugins.jpmultisite.aps.system.services.multisite;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.util.ApsProperties;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteTestHelper {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteTestHelper.class);

	public static void addMultisiteForTesting(IMultisiteManager multisiteManager) throws ApsSystemException {
		try {
			ApsProperties titles = new ApsProperties();
			titles.put("en", "test_title");
			titles.put("it", "test_title");
			ApsProperties descriptions = new ApsProperties();
			descriptions.put("en", "test_descriptions");
			descriptions.put("it", "test_descriptions");
			Multisite multisite = new Multisite("asd", titles, descriptions, "http://test.com", null, null, "asd");
			if (multisiteManager.loadMultisite("asd") == null) {
				User user = new User();
				user.setUsername("admin@asd");
				user.setPassword("admin@asd");
				multisiteManager.addMultisite(multisite, user);
			}
			multisite = new Multisite("asd2", titles, descriptions, "http://testp.com", null, null, "asd2");
			if (multisiteManager.loadMultisite("asd2") == null) {
				User user = new User();
				user.setUsername("admin@asd2");
				user.setPassword("admin@asd2");
				multisiteManager.addMultisite(multisite, user);
			}
			assertEquals(2, multisiteManager.loadMultisites().size());
		} catch (Throwable t) {
			_logger.error("error creating multisite for testing", t);
			throw new ApsSystemException("error creating multisite for testing", t);
		}
	}

	public static void deleteAllMultisite(IMultisiteManager multisiteManager) throws ApsSystemException {
		try {
			List<String> loadMultisites = multisiteManager.loadMultisites();
			for (String code : loadMultisites) {
				_logger.debug("tear down - deleting {}", code);
				multisiteManager.deleteMultisite(code);
			}
		} catch (Throwable t) {
			_logger.error("error deleting multisites", t);
			throw new ApsSystemException("error deleting multisites", t);
		}
	}
	
}
