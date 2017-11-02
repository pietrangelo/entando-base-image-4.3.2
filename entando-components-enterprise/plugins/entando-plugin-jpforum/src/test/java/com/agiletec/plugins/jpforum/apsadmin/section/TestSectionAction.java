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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.apsadmin.section.SectionAction;
import com.opensymphony.xwork2.Action;

public class TestSectionAction extends JpforumApsAdminBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testNewSection() throws Throwable {
		String selectedNode = "";
		String result = this.executeNewSection(selectedNode);
		assertEquals("sectionTree", result);

		selectedNode = "forum";
		result = this.executeNewSection(selectedNode);
		assertEquals(Action.SUCCESS, result);
		SectionAction action = (SectionAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
		assertEquals("forum", action.getSelectedNode());
	}

	public void testSaveNewSection() throws Throwable {
		Section test1Section = this._sectionManager.getSection("test_1");
		Section aSection = this._sectionManager.getSection("a");
		Section bSection = this._sectionManager.getSection("b");
		assertEquals(1, test1Section.getPosition());
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
		assertEquals(3, _sectionManager.getRoot().getChildren().length);
		
		int strutsAction = ApsAdminSystemConstants.ADD;
		//NULL INPUT
		String selectedNode = "";
		String code = "";
		String parentCode = "";
		String result = this.executeSaveNew(selectedNode, code, parentCode, null, null, null, strutsAction);
		assertEquals(Action.INPUT, result);
		//senza descr
		selectedNode = "forum";
		code = TEST_CODE;
		parentCode = "forum";
		ApsProperties titles = new ApsProperties();
		titles.put("it", "title_it");
		titles.put("en", "title_en");
		ApsProperties descriptions = new ApsProperties();
		descriptions.put("it", "description_it");
		descriptions.put("en", "description_en");
		String groups = Group.FREE_GROUP_NAME;
		result = this.executeSaveNew(selectedNode, code, parentCode, titles, null, groups, strutsAction);
		assertEquals(Action.INPUT, result);
		//DESCR
		result = this.executeSaveNew(selectedNode, code, parentCode, titles, descriptions, groups, strutsAction);
//		List<String> actionErrors = (List<String>) this.getAction().getActionErrors();
//		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		
		assertEquals(Action.SUCCESS, result);
		assertNotNull(_sectionManager.getSection(TEST_CODE));
		this._sectionManager.deleteSection(TEST_CODE);
		
		//VERIFICA STATO INIZIALE
		test1Section = this._sectionManager.getSection("test_1");
		aSection = this._sectionManager.getSection("a");
		bSection = this._sectionManager.getSection("b");
		assertEquals(1, test1Section.getPosition());
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
	}

	public void testTrashSection() throws Throwable {
		Section test1Section = this._sectionManager.getSection("test_1");
		Section aSection = this._sectionManager.getSection("a");
		Section bSection = this._sectionManager.getSection("b");
		assertEquals(1, test1Section.getPosition());
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
		assertEquals(3, _sectionManager.getRoot().getChildren().length);
		
		String parentcode = this._sectionManager.getRoot().getCode();
		Section section = this.createSection(TEST_CODE, parentcode);
		this._sectionManager.addSection(section);
		//TRASH ROOT
		String result = this.executeTrashSection(parentcode);
		assertEquals("sectionTree", result);
		//TRASH OK
		result = this.executeTrashSection(TEST_CODE);
		assertEquals(Action.SUCCESS, result);
		SectionAction action = (SectionAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.DELETE, action.getStrutsAction());
		
		this._sectionManager.deleteSection(TEST_CODE);
		//VERIFICA STATO INIZIALE
		test1Section = this._sectionManager.getSection("test_1");
		aSection = this._sectionManager.getSection("a");
		bSection = this._sectionManager.getSection("b");
		assertEquals(1, test1Section.getPosition());
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
	}

	public void testDeleteSection() throws Throwable {
		Section test1Section = this._sectionManager.getSection("test_1");
		Section aSection = this._sectionManager.getSection("a");
		Section bSection = this._sectionManager.getSection("b");
		assertEquals(1, test1Section.getPosition());
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
		assertEquals(3, _sectionManager.getRoot().getChildren().length);
		
		String parentcode = this._sectionManager.getRoot().getCode();
		Section section = this.createSection(TEST_CODE, parentcode);
		this._sectionManager.addSection(section);
		//DELETE ROOT
		int strutsAction = ApsAdminSystemConstants.DELETE;
		String result = this.executeDeleteSection(parentcode, strutsAction);
		assertEquals("sectionTree", result);
		//DELTE OK
		result = this.executeDeleteSection(TEST_CODE, strutsAction);
		assertEquals(Action.SUCCESS, result);
		assertNull(this._sectionManager.getSection(TEST_CODE));

		//VERIFICA STATO INIZIALE
		test1Section = this._sectionManager.getSection("test_1");
		aSection = this._sectionManager.getSection("a");
		bSection = this._sectionManager.getSection("b");
		assertEquals(1, test1Section.getPosition());
		assertEquals(2, aSection.getPosition());
		assertEquals(3, bSection.getPosition());
	}

	public void testAddRemoveGroup() throws Throwable {
		//administrators
		//customers
		//management
		Section f = this._sectionManager.getSection("f");
		String result = this.executeAddGroup("f", "e", "customers", f.getGroups(), f.getTitles(), f.getDescriptions());
		assertEquals(Action.SUCCESS, result);
		SectionAction action = (SectionAction) this.getAction();
		assertEquals(2, action.getGroups().size());

		result = this.executeAddGroup("f", "e", "coach", f.getGroups(), f.getTitles(), f.getDescriptions());
		//List<String> actionErrors = (List<String>) this.getAction().getActionErrors();
		//Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();

		assertEquals(Action.INPUT, result);
		
		action = (SectionAction) this.getAction();
		assertEquals(1, action.getGroups().size());
	}
	


	public void testEditSection() throws Throwable {
		String parentcode = this._sectionManager.getRoot().getCode();
		Section section = this.createSection(TEST_CODE, parentcode);
		this._sectionManager.addSection(section);
		String result = this.executeEditSection(TEST_CODE);
		assertEquals(Action.SUCCESS, result);
		SectionAction action = (SectionAction) this.getAction();
		assertEquals(ApsAdminSystemConstants.EDIT, action.getStrutsAction());
		assertEquals(TEST_CODE, action.getSelectedNode());
		assertEquals(TEST_CODE, action.getCode());
		assertEquals(section.getParentCode(), action.getParentCode());
		assertEquals(section.getPosition(), action.getPosition());
		assertEquals(section.isOpen(), action.isOpen());
		assertEquals(section.getUnauthBahaviour(), action.getUnauthBahaviour());
		assertEquals(section.getTitles().toXml(), action.getTitles().toXml());
		assertEquals(section.getGroups().size(), action.getGroups().size());
	}

	private String executeNewSection(String selectedNode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "new");
		this.addParameter("selectedNode", selectedNode);
		return this.executeAction();
	}
	
	@SuppressWarnings("unchecked")
	private String executeSaveNew(String selectedNode, String code, String parentCode, ApsProperties titles, ApsProperties descriptions, String groups, int strutsAction) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "save");
		this.addParameter("selectedNode", selectedNode);
		this.addParameter("code", code);
		this.addParameter("parentCode", parentCode);
		this.addParameter("strutsAction", strutsAction);
		if (null != titles && titles.size() > 0) {
			Enumeration e = titles.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				this.addParameter("title" + key, titles.get(key));
			}
		}
		if (null != descriptions && descriptions.size() > 0) {
			Enumeration e = descriptions.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				this.addParameter("description" + key, descriptions.get(key));
			}
		}
		if (null != groups && groups.trim().length() > 0) {
			String[] groupArray = groups.split(",");
			for (int i = 0; i < groupArray.length; i++) {
				this.addParameter("groups", groupArray[i]);
			}
		}
		return this.executeAction();
	}
	
	
	private String executeTrashSection(String selectedNode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "trash");
		this.addParameter("selectedNode", selectedNode);
		return this.executeAction();
	}

	private String executeDeleteSection(String code, int strutsAction) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "delete");
		this.addParameter("code", code);
		this.addParameter("strutsAction", strutsAction);
		return this.executeAction();
	}
	
	@SuppressWarnings("unchecked")
	private String executeAddGroup(String code, String parentCode, String selectedGroup, Set<String> existingGroups, ApsProperties titles, ApsProperties descriptions) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "addGroup");
		this.addParameter("code", code);
		this.addParameter("parentCode", parentCode);
		this.addParameter("selectedGroup", selectedGroup);
		Iterator<String> it = existingGroups.iterator();
		while (it.hasNext()) {
			this.addParameter("groups", it.next());
		}
		if (null != titles && titles.size() > 0) {
			Enumeration e = titles.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				this.addParameter("title" + key, titles.get(key));
			}
		}
		if (null != descriptions && descriptions.size() > 0) {
			Enumeration e = descriptions.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				this.addParameter("description" + key, descriptions.get(key));
			}
		}		
		return this.executeAction();
	}
	
	private String executeEditSection(String selectedNode) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, "edit");
		this.addParameter("selectedNode", selectedNode);
		return this.executeAction();
	}

	
	private Section createSection(String code, String parentCode) {
		Section testSection = new Section();
		testSection.setCode(code);
		ApsProperties titles = new ApsProperties();
		List<Lang> langs = this._langManager.getLangs();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
			titles.setProperty(lang.getCode(), "t_" + lang.getCode() + "_" + code);
		}
		testSection.setTitles(titles);
		ApsProperties descriptions = new ApsProperties();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
			descriptions.setProperty(lang.getCode(), "d_" + lang.getCode() + "_" + code);
		}
		testSection.setDescriptions(descriptions);
		testSection.setParentCode(parentCode);
		testSection.setParent(_sectionManager.getNode(parentCode));
		testSection.setOpen(true);
		testSection.addGroup(Group.FREE_GROUP_NAME);
		return testSection;
	}

	private void init() {
		_sectionManager = (ISectionManager) this.getService(JpforumSystemConstants.SECTION_MANAGER);
		_langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
	}

	@Override
	protected void tearDown() throws Exception {
		this._sectionManager.deleteSection(TEST_CODE);
		super.tearDown();
	}

	private ISectionManager _sectionManager;
	private ILangManager _langManager;
	private static final String NS = "/do/jpforum/Section";
	private static final String TEST_CODE = "delete_me";
}
