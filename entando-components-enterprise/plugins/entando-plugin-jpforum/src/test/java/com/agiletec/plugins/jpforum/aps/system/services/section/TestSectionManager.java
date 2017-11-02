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
package com.agiletec.plugins.jpforum.aps.system.services.section;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class TestSectionManager extends JpforumBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testInit() {
		assertNotNull(_sectionManager);
	}
	
	public void testGetRoot() {
		Section root = (Section) _sectionManager.getRoot();
		assertNotNull(root);
		assertEquals(-1, root.getPosition());
		assertEquals("forum", root.getCode());
		assertEquals("forum", root.getFullTitle("it"));
		assertEquals("forum", root.getFullTitle("en"));
		assertEquals("forum", root.getDescription("it"));
		assertEquals("forum", root.getDescription("en"));
		assertEquals("forum", root.getParentCode());
		assertTrue(root.isOpen());
		assertTrue(root.isRoot());
		assertEquals(Section.UNAUTH_BEHAVIOUR_READONLY, root.getUnauthBahaviour());
	}

	public void testGetRootNode() {
		String rootcode = this._sectionManager.getRoot().getCode();
		Section root = (Section) _sectionManager.getNode(rootcode);
		assertNotNull(root);
		assertEquals(-1, root.getPosition());
		assertEquals("forum", root.getCode());
		assertEquals("forum", root.getFullTitle("it"));
		assertEquals("forum", root.getFullTitle("en"));
		assertEquals("forum", root.getDescription("it"));
		assertEquals("forum", root.getDescription("en"));
		assertEquals("forum", root.getParentCode());
		assertTrue(root.isOpen());
		assertTrue(root.isRoot());
		assertEquals(Section.UNAUTH_BEHAVIOUR_READONLY, root.getUnauthBahaviour());
	}
	
	public void testAddSection() throws Throwable {
		String parentcode = this._sectionManager.getRoot().getCode();
		Section section = this.createSection(TEST_CODE, parentcode);
		Set<String> groups = new HashSet<String>();
		groups.add("customers");
		groups.add("administrators");
		section.setGroups(groups);
		this._sectionManager.addSection(section);
		Section added = (Section) _sectionManager.getNode(TEST_CODE);
		assertNotNull(added);
		assertEquals(4, added.getPosition());
		assertEquals(TEST_CODE, added.getCode());
		assertEquals(".. / " + "t_it_" + TEST_CODE, added.getShortFullTitle("it"));
		assertEquals(".. / " + "t_en_" + TEST_CODE, added.getShortFullTitle("en"));
		assertEquals("d_it_" + TEST_CODE, added.getDescription("it"));
		assertEquals("d_en_" + TEST_CODE, added.getDescription("en"));
		assertEquals(parentcode, added.getParentCode());
		assertTrue(added.isOpen());
		assertFalse(added.isRoot());
		assertEquals(Section.UNAUTH_BEHAVIOUR_HIDDEN, added.getUnauthBahaviour());
		Set<String> addedGroups = added.getGroups();
		assertEquals(2, addedGroups.size());
		assertTrue(addedGroups.contains("customers") && addedGroups.contains("administrators"));
	}

	public void testUpdateSection() throws Throwable {
		String parentcode = this._sectionManager.getRoot().getCode();
		Section section = this.createSection(TEST_CODE, parentcode);
		Set<String> groups = new HashSet<String>();
		groups.add("customers");
		groups.add("administrators");
		section.setGroups(groups);

		//AGGIUNGO LA SEZIONE DI TEST
		this._sectionManager.addSection(section);
		Section added = (Section) _sectionManager.getNode(TEST_CODE);
		//MODIFICO E AGGIORNO
		added.getTitles().put("it", "nuovo titolo");
		added.getDescriptions().put("it", "nuova descrizione");
		added.setOpen(false);
		added.setUnauthBahaviour(Section.UNAUTH_BEHAVIOUR_READONLY);
		
		added.getGroups().remove("administrators");
		
		this._sectionManager.updateSection(added);
		//VERIFICO AGGIORNAMENTO
		Section added1 = (Section) _sectionManager.getNode(TEST_CODE);
		assertNotNull(added1);
		assertEquals(4, added1.getPosition());
		assertEquals(TEST_CODE, added1.getCode());
		assertEquals(".. / " + "nuovo titolo", added1.getShortFullTitle("it"));
		assertEquals(".. / " + "t_en_" + TEST_CODE, added1.getShortFullTitle("en"));
		assertEquals("nuova descrizione", added1.getDescription("it"));
		assertEquals("d_en_" + TEST_CODE, added1.getDescription("en"));
		assertEquals(parentcode, added1.getParentCode());
		assertFalse(added1.isOpen());
		assertFalse(added1.isRoot());
		assertEquals(Section.UNAUTH_BEHAVIOUR_READONLY, added1.getUnauthBahaviour());
		Set<String> addedGroups = added1.getGroups();
		assertEquals(1, addedGroups.size());
		assertTrue(addedGroups.contains("customers"));
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
		return testSection;
	}
	
	public void testMove() throws Throwable {
		String parentcode = this._sectionManager.getRoot().getCode();
		Section section = this.createSection(TEST_CODE, parentcode);
		Set<String> groups = new HashSet<String>();
		groups.add("customers");
		groups.add("administrators");
		section.setGroups(groups);

		this._sectionManager.addSection(section);
		Section added = (Section) _sectionManager.getNode(TEST_CODE);
		assertNotNull(added);
		assertEquals(4, added.getPosition());
		//test spostamenti
		boolean moveOk =_sectionManager.moveSection(added.getCode(), true);
		assertTrue(moveOk);
		added = (Section) _sectionManager.getNode(TEST_CODE);
		assertEquals(3, added.getPosition());
		Section bSection = _sectionManager.getSection("b");
		assertNotNull(bSection);
		assertEquals(4, bSection.getPosition());
		
		moveOk =_sectionManager.moveSection(added.getCode(), true);
		assertTrue(moveOk);
		added = (Section) _sectionManager.getNode(TEST_CODE);
		assertEquals(2, added.getPosition());
		bSection = _sectionManager.getSection("b");
		assertNotNull(bSection);
		assertEquals(4, bSection.getPosition());
		Section aSection = _sectionManager.getSection("a");
		assertNotNull(aSection);
		assertEquals(3, aSection.getPosition());

		moveOk =_sectionManager.moveSection(added.getCode(), false);
		assertTrue(moveOk);
		added = (Section) _sectionManager.getNode(TEST_CODE);
		assertEquals(3, added.getPosition());
		bSection = _sectionManager.getSection("b");
		assertNotNull(bSection);
		assertEquals(4, bSection.getPosition());
		aSection = _sectionManager.getSection("a");
		assertNotNull(aSection);
		assertEquals(2, aSection.getPosition());
	}
	
	@SuppressWarnings("unchecked")
	public void testGetGroupsUtilizers() throws Throwable {
		List<Section> sections = ((GroupUtilizer)_sectionManager).getGroupUtilizers("administrators");
		assertEquals(2, sections.size());
		sections = ((GroupUtilizer)_sectionManager).getGroupUtilizers("customers");
		assertEquals(1, sections.size());
		assertEquals("e", sections.get(0).getCode());
	}

	private void init() {
		_sectionManager = (ISectionManager) this.getService(JpforumSystemConstants.SECTION_MANAGER);
		_langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
	}
	
	@Override
	protected void tearDown() throws Exception {
		this._sectionManager.deleteSection(TEST_CODE);
		this.waitNotifyingThread();
		super.tearDown();
	}

	private ISectionManager _sectionManager;
	private ILangManager _langManager;
	private static final String TEST_CODE = "delete_me";
}
