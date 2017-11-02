/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps;


import org.entando.entando.plugins.jpnotify.JpnotifyConfigTestUtils;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;

public class JpnotifyBaseTestCase extends BaseTestCase {

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new JpnotifyConfigTestUtils();
	}

	
}
