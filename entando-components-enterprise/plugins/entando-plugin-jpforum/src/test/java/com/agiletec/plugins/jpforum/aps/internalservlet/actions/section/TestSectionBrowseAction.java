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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.section;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

import com.agiletec.plugins.jpforum.aps.internalservlet.actions.section.SectionBrowseAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.opensymphony.xwork2.Action;

public class TestSectionBrowseAction extends JpforumApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testViewFreeSection() throws Throwable {
		//a free
		//b free
		//e admin + customers
		//f admin 
		String username = null;
		String result = this.executeViewSection(username, "a");
		assertEquals(Action.SUCCESS, result);
		SectionBrowseAction action = (SectionBrowseAction) this.getAction();
		assertEquals("a", action.getSection());
		
		username = FORUM_USER;
		result = this.executeViewSection(username, "a");
		assertEquals(Action.SUCCESS, result);
		action = (SectionBrowseAction) this.getAction();
		assertEquals("a", action.getSection());
	}

	public void testViewSection() throws Throwable {
		//a free
		//b free
		//e admin + customers
		//f admin 
		String username = FORUM_SUPERUSER;
		String result = this.executeViewSection(username, "f");
		assertEquals(Action.SUCCESS, result);
		SectionBrowseAction action = (SectionBrowseAction) this.getAction();
		assertEquals("f", action.getSection());

		username = FORUM_MODERATOR;
		result = this.executeViewSection(username, "f");
		assertEquals(Action.SUCCESS, result);
		action = (SectionBrowseAction) this.getAction();
		assertEquals("f", action.getSection());
		
		Section fSection = _sectionManager.getSection("f");
		fSection.setUnauthBahaviour(Section.UNAUTH_BEHAVIOUR_HIDDEN);
		_sectionManager.updateSection(fSection);
		
		//action.getSection == f ma solo per restituire il messaggio
		result = this.executeViewSection(null, "f");
		assertEquals(Action.SUCCESS, result);
		action = (SectionBrowseAction) this.getAction();
		assertEquals("f", action.getSection());
		
	}

	
	private String executeViewSection(String username, String section) throws Throwable {
		if (null != username && username.trim().length() > 0) {
			this.setUserOnSession(username);
		}
		this.initAction(NS, "viewSection");
		this.addParameter("section", section);
		return this.executeAction();
	}

	private void init() {
		_sectionManager = (ISectionManager) this.getService(JpforumSystemConstants.SECTION_MANAGER);
	}

	@Override
	protected void tearDown() throws Exception {
		Section fSection = this._sectionManager.getSection("f");
		fSection.setUnauthBahaviour(Section.UNAUTH_BEHAVIOUR_READONLY);
		this._sectionManager.updateSection(fSection);
		super.tearDown();
	}
	
	private static final String NS = "/do/jpforum/Front/Browse";
	private ISectionManager _sectionManager;
}
