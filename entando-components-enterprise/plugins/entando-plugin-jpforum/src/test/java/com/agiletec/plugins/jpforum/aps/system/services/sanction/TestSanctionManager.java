/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpforum.aps.system.services.sanction;

import java.util.Date;
import java.util.List;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.ISanctionManager;
import com.agiletec.plugins.jpforum.aps.system.services.sanction.Sanction;

public class TestSanctionManager extends JpforumBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testAddSanction() throws ApsSystemException  {
		Date start = DateConverter.parseDate("01/01/2010", "dd/MM/yyyy");
		Date end = DateConverter.parseDate("22/12/2050", "dd/MM/yyyy");
		Sanction sanction = this.createTestSanction("mainEditor", start, end, "admin");
		_sanctionManager.addSanction(sanction);
		List<Integer> sanctions = _sanctionManager.getSanctions("mainEditor");
		assertNotNull(sanctions);
		assertEquals(1, sanctions.size());
		int code = sanctions.get(0);
		Sanction userSanction = _sanctionManager.getSanction(code);
		assertEquals(userSanction.getModerator(), "admin");
		assertEquals(userSanction.getUsername(), "mainEditor");
		assertEquals(DateConverter.getFormattedDate(userSanction.getStartDate(), "dd/MM/yyyy"), "01/01/2010");
		assertEquals(DateConverter.getFormattedDate(userSanction.getEndDate(), "dd/MM/yyyy"), "22/12/2050");
		assertTrue(_sanctionManager.isUnderSanction("mainEditor"));
		_sanctionManager.deleteSanction(code);
		 sanctions = _sanctionManager.getSanctions("mainEditor");
		assertNotNull(sanctions);
		assertEquals(0, sanctions.size());
	}


	private Sanction createTestSanction(String username, Date startDate, Date endDate, String moderator) {
		Sanction sanction = new Sanction();
		sanction.setEndDate(endDate);
		sanction.setModerator(moderator);
		sanction.setStartDate(startDate);
		sanction.setUsername(username);
		return sanction;
	}

	public void testDeleteSanctions() {
		//AOP
	}

	private void init() {
		_sanctionManager = (ISanctionManager) this.getService(JpforumSystemConstants.SANCTION_MANAGER);
	}
	
	@Override
	protected void tearDown() throws Exception {
		List<Integer> sanctions = _sanctionManager.getSanctions("mainEditor");
		if (null != sanctions && sanctions.size() > 0) {
			for (int i = 0; i < sanctions.size(); i++) {
				int id = sanctions.get(i);
				_sanctionManager.deleteSanction(id);
			}
		}
		super.tearDown();
	}

	private ISanctionManager _sanctionManager;
	
}
