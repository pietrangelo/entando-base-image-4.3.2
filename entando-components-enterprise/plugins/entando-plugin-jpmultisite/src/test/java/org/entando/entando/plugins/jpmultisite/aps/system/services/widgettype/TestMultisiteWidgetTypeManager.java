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
package org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import static junit.framework.Assert.assertNotNull;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpmultisite.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class TestMultisiteWidgetTypeManager extends ApsPluginBaseTestCase {

	private static final Logger _logger = LoggerFactory.getLogger(TestMultisiteWidgetTypeManager.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() throws Exception {
		_logger.info(this.getClass().getName() + " init");
		IMultisiteWidgetTypeManager widgetTypeManager = (IMultisiteWidgetTypeManager) this.getService("jpMultisiteWidgetTypeManager");
		assertTrue(widgetTypeManager instanceof MultisiteWidgetTypeManager);
		this._widgetTypeManager = widgetTypeManager;
	}

	private void duplicateWidgetType(String test) throws ApsSystemException {
		try {
			if (StringUtils.isNotBlank(test)) {
				List<WidgetType> widgetTypes = this._widgetTypeManager.getWidgetTypes();
				for (WidgetType widgetType : widgetTypes) {
					if (!widgetType.getCode().contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)) {
						widgetType.setCode(widgetType.getCode() + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + test);
						this._widgetTypeManager.addWidgetType(widgetType);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error duplicating widgetType", t);
			throw new ApsSystemException("Error duplicating widgetType", t);
		}
	}

	public void testInit() {
		assertNotNull(_widgetTypeManager);
	}

	public void testDuplicationDelete() throws Exception {
		List<WidgetType> widgetTypes = this._widgetTypeManager.getWidgetTypes();
		int size = widgetTypes.size();
		duplicateWidgetType("test");
		List<WidgetType> widgetTypesDoubled = this._widgetTypeManager.getWidgetTypes();
		assertEquals(size, widgetTypesDoubled.size() / 2);
		for (WidgetType widgetType : widgetTypesDoubled) {
			if (widgetType.getCode().contains("@test")) {
				this._widgetTypeManager.deleteWidgetTypeLocked(widgetType.getCode());
			}
		}
		widgetTypesDoubled = this._widgetTypeManager.getWidgetTypes();
		for (WidgetType widgetType : widgetTypesDoubled) {
		}
		assertEquals(size, widgetTypesDoubled.size());
	}
	
	
	public void testGetWidgetByMultisite() throws ApsSystemException{
		List<WidgetType> widgetTypes = this._widgetTypeManager.getWidgetTypes();
		duplicateWidgetType("test");
		List<WidgetType> widgetTypesDoubled = this._widgetTypeManager.getWidgetTypes();
		assertEquals(widgetTypes.size(), widgetTypesDoubled.size() / 2);
		List<WidgetType> widgetTypesByMultisiteCode = this._widgetTypeManager.getWidgetTypesByMultisiteCode("test");
		assertEquals(widgetTypes.size(), widgetTypesByMultisiteCode.size());
		for (WidgetType widgetType : widgetTypesByMultisiteCode) {
			assertTrue(widgetType.getCode().endsWith("@test"));
		}
	}
	
	
	public void testGetWidgetByMultisiteBlank() throws ApsSystemException{
		_logger.info("**** START BLANK ****");
		List<WidgetType> widgetTypes = this._widgetTypeManager.getWidgetTypes();
		duplicateWidgetType("test");
		List<WidgetType> widgetTypesDoubled = this._widgetTypeManager.getWidgetTypes();
		assertEquals(widgetTypes.size(), widgetTypesDoubled.size() / 2);
		List<WidgetType> widgetTypesByMultisiteCode = this._widgetTypeManager.getWidgetTypesByMultisiteCode(null);
		assertEquals(widgetTypes.size(), widgetTypesByMultisiteCode.size());
		for (WidgetType widgetType : widgetTypesByMultisiteCode) {
			assertTrue(!widgetType.getCode().contains("@test"));
		}
		widgetTypesByMultisiteCode = this._widgetTypeManager.getWidgetTypesByMultisiteCode("");
		assertEquals(widgetTypes.size(), widgetTypesByMultisiteCode.size());
		for (WidgetType widgetType : widgetTypesByMultisiteCode) {
			assertTrue(!widgetType.getCode().contains("@test"));
		}
		widgetTypesByMultisiteCode = this._widgetTypeManager.getWidgetTypesByMultisiteCode(" ");
		assertEquals(widgetTypes.size(), widgetTypesByMultisiteCode.size());
		for (WidgetType widgetType : widgetTypesByMultisiteCode) {
			assertTrue(!widgetType.getCode().contains("@test"));
		}
		_logger.info("**** END BLANK ****");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		for (WidgetType widgetType : this._widgetTypeManager.getWidgetTypes()) {
			if (widgetType.getCode().contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)) {
				this._widgetTypeManager.deleteWidgetTypeLocked(widgetType.getCode());
			}
		}
	}

	private IMultisiteWidgetTypeManager _widgetTypeManager;

}
