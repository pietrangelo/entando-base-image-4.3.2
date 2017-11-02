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
package com.agiletec.plugins.jpforum.apsadmin.section;

import java.util.List;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.opensymphony.xwork2.Action;

public class TestSectionTreeAction extends JpforumApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testViewTree() throws Throwable {
		String result = this.executeViewTree();
		assertEquals(Action.SUCCESS, result);
	}

	public void testMoveWithErrors() throws Throwable {
		boolean moveUp = true;
		String selectedNode = "";
		String result = this.executeMove(moveUp, selectedNode);
		assertEquals("sectionTree", result);
		//MOVE WRONG CODE
		selectedNode = "wrong";
		result = this.executeMove(moveUp, selectedNode);
		assertEquals("sectionTree", result);
		List<String> actionErrors = (List<String>) this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
		//MOVE-UP ROOT
		selectedNode = "forum";
		result = this.executeMove(moveUp, selectedNode);
		assertEquals("sectionTree", result);
		actionErrors = (List<String>) this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
	}
	
	public void testMove() throws Throwable {
		Section aSection = this._sectionManager.getSection("a");
		Section bSection = this._sectionManager.getSection("b");
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
		assertEquals(3, _sectionManager.getRoot().getChildren().length);
		//MOVE DOWN - to last position
		boolean moveUp = false;
		String selectedNode = "a";
		String result = this.executeMove(moveUp, selectedNode);
		assertEquals(Action.SUCCESS, result);
		aSection = this._sectionManager.getSection("a");
		bSection = this._sectionManager.getSection("b");
		assertEquals(3, aSection.getPosition());
		assertEquals(2, bSection.getPosition());
		//MOVE DOWN - over last position 
		moveUp = false;
		selectedNode = "a";
		result = this.executeMove(moveUp, selectedNode);
		assertEquals(Action.SUCCESS, result);
		aSection = this._sectionManager.getSection("a");
		bSection = this._sectionManager.getSection("b");
		assertEquals(3, aSection.getPosition());
		assertEquals(2, bSection.getPosition());
		//MOVE UP - 
		moveUp = true;
		selectedNode = "a";
		result = this.executeMove(moveUp, selectedNode);
		assertEquals(Action.SUCCESS, result);
		aSection = this._sectionManager.getSection("a");
		bSection = this._sectionManager.getSection("b");
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
	}
	
	private String executeViewTree() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "viewTree");
		return this.executeAction();
	}
	
	private String executeMove(boolean moveUp, String selectedNode) throws Throwable {
		this.setUserOnSession("admin");
		if (moveUp) {
			this.initAction(NS, "moveUp");
		} else {
			this.initAction(NS, "moveDown");
		}
		this.addParameter("selectedNode", selectedNode);
		return this.executeAction();
	}

	private void init() {
		_sectionManager = (ISectionManager) this.getService(JpforumSystemConstants.SECTION_MANAGER);
	}
	
	private ISectionManager _sectionManager;
	private static final String NS = "/do/jpforum/Section";
}
