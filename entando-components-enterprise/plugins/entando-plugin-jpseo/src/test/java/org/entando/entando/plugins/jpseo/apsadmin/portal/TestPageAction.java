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
package org.entando.entando.plugins.jpseo.apsadmin.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPage;
import org.entando.entando.plugins.jpseo.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.PageFinderAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.apsadmin.portal.PageAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class TestPageAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testEditPageForAdminUser() throws Throwable {
		String selectedPageCode = "pagina_1";
		String result = this.executeActionOnPage(selectedPageCode, "admin", "edit", null);
		assertEquals(Action.SUCCESS, result);

		IPage page = this._pageManager.getPage(selectedPageCode);

		PageAction action = (PageAction) this.getAction();
		assertEquals(action.getStrutsAction(), ApsAdminSystemConstants.EDIT);
		assertEquals(page.getCode(), action.getPageCode());
		assertEquals(page.getParentCode(), action.getParentPageCode());
		assertEquals(page.getModel().getCode(), action.getModel());
		assertEquals(page.getGroup(), action.getGroup());
		assertEquals(page.isShowable(), action.isShowable());
		assertEquals("Pagina 1", action.getTitles().getProperty("it"));
		assertEquals("Page 1", action.getTitles().getProperty("en"));
	}

	public void testJoinGroupPageForAdminUser() throws Throwable {
		String extraGroup = "administrators";
		String selectedPageCode = "pagina_1";
		Map<String, String> params = new HashMap<String, String>();
		params.put("extraGroupName", extraGroup);

		//add extra group
		String result = this.executeActionOnPage(selectedPageCode, "admin", "joinExtraGroup", params);
		assertEquals(Action.SUCCESS, result);
		PageAction action = (PageAction) this.getAction();
		boolean hasExtraGroupAdministrators = action.getExtraGroups().contains(extraGroup);
		assertTrue(hasExtraGroupAdministrators);

		//remove extra group
		result = this.executeActionOnPage(selectedPageCode, "admin", "removeExtraGroup", params);
		assertEquals(Action.SUCCESS, result);
		action = (PageAction) this.getAction();
		hasExtraGroupAdministrators = action.getExtraGroups().contains(extraGroup);
		assertFalse(hasExtraGroupAdministrators);
	}


	private String executeActionOnPage(String selectedPageCode, String username, String actionName, Map<String, String> params) throws Throwable {
		if (StringUtils.isNotBlank(username)) {
		}
		this.setUserOnSession(username);
		this.initAction("/do/Page", actionName);
		this.addParameter("selectedNode", selectedPageCode);
		if (null != params && !params.isEmpty()) {
			this.addParameters(params);
		}
		String result = this.executeAction();
		return result;
	}




	public void testValidateSavePage() throws Throwable {
		String pageCode = "pagina_test";
		String longPageCode = "very_long_page_code__very_long_page_code";
		assertNull(this._pageManager.getPage(pageCode));
		assertNull(this._pageManager.getPage(longPageCode));
		try {
			IPage root = this._pageManager.getRoot();
			Map<String, String> params = new HashMap<String, String>();
			params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
			params.put("parentPageCode", root.getCode());
			String result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(4, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("model"));
			assertTrue(fieldErrors.containsKey("group"));
			assertTrue(fieldErrors.containsKey("langit"));
			assertTrue(fieldErrors.containsKey("langen"));

			params.put("langit", "Pagina Test");
			params.put("model", "home");
			result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("group"));
			assertTrue(fieldErrors.containsKey("langen"));

			assertNotNull(this._pageManager.getPage("pagina_1"));
			params.put("langen", "Test Page");
			params.put("group", Group.FREE_GROUP_NAME);
			params.put("pageCode", "pagina_1");//page already present
			result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("pageCode"));

			params.put("pageCode", longPageCode);
			result = this.executeSave(params, "admin");
			assertEquals(Action.INPUT, result);
			fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertTrue(fieldErrors.containsKey("pageCode"));
		} catch (Throwable t) {
			this._pageManager.deletePage(pageCode);
			this._pageManager.deletePage(longPageCode);
			throw t;
		}
	}




	

	public void testSavePage_1() throws Throwable {
		String pageCode = "pagina_test";


		assertNull(this._pageManager.getPage(pageCode));
		try {
			IPage root = this._pageManager.getRoot();
			Map<String, String> params = new HashMap<String, String>();
			params.put("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
			params.put("parentPageCode", root.getCode());
			params.put("langit", "Pagina Test 1");
			params.put("langen", "Test Page 1");
			params.put("model", "home");
			params.put("group", Group.FREE_GROUP_NAME);
			params.put("pageCode", pageCode);

			params.put(PageActionAspect.PARAM_FRIENDLY_CODE, "gimmelove");
			
			
			
			String result = this.executeSave(params, "admin");
			assertEquals(Action.SUCCESS, result);
			IPage addedPage = this._pageManager.getPage(pageCode);
			assertNotNull(addedPage);
			assertEquals("Pagina Test 1", addedPage.getTitles().getProperty("it"));
			
			assertTrue(addedPage instanceof SeoPage);
			SeoPage addedSeoPage = (SeoPage) addedPage;
			assertEquals("gimmelove", addedSeoPage.getFriendlyCode());
			
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageManager.deletePage(pageCode);
		}
	}
	 



	private String executeSave(Map<String, String> params, String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Page", "save");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}




	private void init() throws Exception {
		try {
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IPageManager _pageManager = null;

}