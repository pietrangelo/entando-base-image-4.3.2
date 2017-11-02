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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.sanction;

import java.util.Date;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.sanction.SanctionAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.ISanctionManager;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.Sanction;
import com.opensymphony.xwork2.Action;

public class TestSanctionAction extends JpforumApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testNewSanction() throws Throwable {
		String result = this.executeNewSanction(FORUM_USER);
		assertEquals("jpforumlogin", result);

		result = this.executeNewSanction(FORUM_MODERATOR);
		assertEquals("jpforumlogin", result);

		result = this.executeNewSanction("admin");
		assertEquals(Action.SUCCESS, result);

		result = this.executeNewSanction(FORUM_SUPERUSER);
		assertEquals(Action.SUCCESS, result);
		SanctionAction action = (SanctionAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
		assertEquals(FORUM_SUPERUSER, action.getModerator());
	}

	public void testSaveNewSanctionOk() throws Throwable {
		String moderator = FORUM_SUPERUSER;
		String moderated = FORUM_USER;
		String startDate = DateConverter.getFormattedDate(new Date(), "dd/MM/yyyy");
		String endDate = "22/12/2012";
		String result = this.executeSaveSanction(moderator, moderated, startDate, endDate);
		assertEquals(Action.SUCCESS, result);
	}
	
	public void testTrashSanction() throws Throwable {
		String moderator = FORUM_SUPERUSER;
		String moderated = FORUM_USER;
		String startDate = DateConverter.getFormattedDate(new Date(), "dd/MM/yyyy");
		String endDate = "22/12/2012";
		Sanction sanction = this.createTestSanction(moderated, startDate, endDate, moderator);
		_sanctionManager.addSanction(sanction);
		assertTrue(sanction.getId() > 0);
		String result = this.executeTrashSanction(moderator, sanction.getId());
		assertEquals(Action.SUCCESS, result);
		SanctionAction action = (SanctionAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.DELETE, action.getStrutsAction());
		assertEquals(sanction.getId(), action.getId());
	}

	public void testDeleteSanction() throws Throwable {
		String moderator = FORUM_SUPERUSER;
		String moderated = FORUM_USER;
		String startDate = DateConverter.getFormattedDate(new Date(), "dd/MM/yyyy");
		String endDate = "22/12/2012";
		Sanction sanction = this.createTestSanction(moderated, startDate, endDate, moderator);
		_sanctionManager.addSanction(sanction);
		assertTrue(sanction.getId() > 0);
		String result = this.executeDeleteSanction(moderator, sanction.getId());
		assertEquals(Action.SUCCESS, result);
		assertEquals(0, _sanctionManager.getSanctions(FORUM_USER).size());
	}

	private String executeNewSanction(String moderator) throws Throwable {
		this.setUserOnSession(moderator);
		this.initAction(NS, "newSanction");
		return this.executeAction();
	}
	
	private String executeSaveSanction(String moderator, String moderated, String startDate, String endDate) throws Throwable {
		this.setUserOnSession(moderator);
		this.initAction(NS, "saveSanction");
		this.addParameter("moderator", moderator);
		this.addParameter("username", moderated);
		this.addParameter("startDate", DateConverter.parseDate(startDate, "dd/MM/yyyy"));
		this.addParameter("endDate", DateConverter.parseDate(endDate, "dd/MM/yyyy"));
		return this.executeAction();
	}

	private String executeTrashSanction(String moderator, int sanctionId) throws Throwable {
		this.setUserOnSession(moderator);
		this.initAction(NS, "trashSanction");
		this.addParameter("id", sanctionId);
		return this.executeAction();
	}

	private String executeDeleteSanction(String moderator, int sanctionId) throws Throwable {
		this.setUserOnSession(moderator);
		this.initAction(NS, "deleteSanction");
		this.addParameter("id", sanctionId);
		this.addParameter("strutsAction", ApsAdminSystemConstants.DELETE);
		return this.executeAction();
	}

	private Sanction createTestSanction(String username, String startDate, String endDate, String moderator) {
		Sanction sanction = new Sanction();
		Date start = DateConverter.parseDate(startDate, "dd/MM/yyyy");
		Date end = DateConverter.parseDate(endDate, "dd/MM/yyyy");
		sanction.setEndDate(end);
		sanction.setModerator(moderator);
		sanction.setStartDate(start);
		sanction.setUsername(username);
		return sanction;
	}


	private void init() {
		_sanctionManager = (ISanctionManager) this.getService(JpforumSystemConstants.SANCTION_MANAGER);
	}

	@Override
	protected void tearDown() throws Exception {
		this._sanctionManager.deleteSanctions(FORUM_USER);
		super.tearDown();
	}
	
	private static final String NS = "/do/jpforum/Front/Sanction";
	private ISanctionManager _sanctionManager;

}
