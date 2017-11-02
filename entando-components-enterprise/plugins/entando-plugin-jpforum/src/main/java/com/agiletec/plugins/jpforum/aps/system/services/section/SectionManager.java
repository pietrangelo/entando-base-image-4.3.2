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

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.searchengine.IForumSearchManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.event.PostOperationEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectionManager extends AbstractService implements ISectionManager, GroupUtilizer {

	private static final Logger _logger =  LoggerFactory.getLogger(SectionManager.class);

	@Override
	public void init() throws Exception {
		this.loadSections();
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public ITreeNode getRoot() {
		return _root;
	}
	public void setRoot(Section root) {
		this._root = root;
	}
	
	@Override
	public void addSection(Section section) throws ApsSystemException {
		try {
			if (!section.isRoot()) {
				Section parent = section.getParent();
				section.setPosition(parent.getChildren().length + 1);
			}
			this.getSectionDAO().addSection(section);
		} catch (Throwable t) {
			_logger.error("Errore in aggiunta sezione", t);
			throw new ApsSystemException("Errore in aggiunta sezione", t);
		}
		this.loadSections();
	}

	@Override
	public void deleteSection(String code) throws ApsSystemException {
		try {
			Section section = this.getSection(code);
			if (null == section) return;
			this.getSectionDAO().deleteSection(section);
		} catch (Throwable t) {
			_logger.error("Errore in rimozione sezione", t);
			throw new ApsSystemException("Errore in rimozione sezione: " + code, t);
		}
		this.loadSections();
	}

	@Override
	public void updateSection(Section section) throws ApsSystemException {
		try {
			this.getSectionDAO().updateSection(section);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento sezione", t);
		}
		this.loadSections();
	}

	@Override
	public boolean moveSection(String code, boolean moveUp) throws ApsSystemException {
		boolean resultOperation = true; 
		try {
			Section currentSection = this.getSection(code);
			if (null == currentSection) {
				throw new ApsSystemException("Section '" + code + "' non esistente!");
			}
			Section parent = currentSection.getParent();
			ITreeNode[] sisterSections =  parent.getChildren();
			for (int i = 0; i < sisterSections.length; i++) {
				Section sisterSection = (Section) sisterSections[i];
				if (sisterSection.getCode().equals(code)) {
					if (!verifyRequiredMovement(i, moveUp, sisterSections.length)) {
						resultOperation = false;
					} else {
						if (moveUp) {
							Section sectionDown = (Section) sisterSections[i - 1];
							this.moveUpDown(sectionDown, currentSection);
						} else {
							Section sectionUp = (Section) sisterSections[i + 1];
							this.moveUpDown(currentSection, sectionUp);
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in spostamento sezione", t);
			throw new ApsSystemException("Errore in spostamento sezione", t);
		}
		this.loadSections();
		return resultOperation;
	}

	private void moveUpDown(Section sectionDown, Section sectionUp) throws ApsSystemException {
		try {
			this.getSectionDAO().updatePosition(sectionDown, sectionUp);
		} catch (Throwable t) {
			_logger.error("Errore in spostamento sezione", t);
			throw new ApsSystemException("Errore in spostamento sezione", t);
		}
	}
	
	@Override
	public List<String> getModerators(Section section) throws ApsSystemException {
		List<String> currentSectionModerarors = new ArrayList<String>();
		try {
			Set<String> codes = section.getGroups();
			if (null == codes) {
				return currentSectionModerarors;
			}
			Iterator<String> iter = codes.iterator();
			while (iter.hasNext()) {
				String groupName = iter.next();
				List<String> usernames = this.getAuthorizationManager().getUsersByAuthorities(groupName, JpforumSystemConstants.ROLE_SECTION_MODERATOR, true);
				for (int i = 0; i < usernames.size(); i++) {
					String username = usernames.get(i);
					if (!currentSectionModerarors.contains(username)) {
						currentSectionModerarors.add(username);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore calcolo moderatori sezione", t);
			throw new ApsSystemException("Errore calcolo moderatori sezione", t);
		}
		return currentSectionModerarors;
	}

	@Override
	public ITreeNode getNode(String code) {
		return this._sections.get(code);
	}

	@Override
	public Section getSection(String code) {
		return this._sections.get(code);
	}

	@Override
	public List<Section> getGroupUtilizers(String groupName) throws ApsSystemException {
		List<Section> sections = new ArrayList<Section>();
		try {
			List<String> sectionCodes  = this.getSectionDAO().getSectionsForGroup(groupName);
			if (null != sectionCodes && sectionCodes.size() > 0) {
				for (int i = 0; i < sectionCodes.size(); i++) {
					sections.add(this.getSection(sectionCodes.get(i)));
				}
			}
		} catch (Throwable t) {
			_logger.error("errore in recupero sezioni relative al gruppo {}", groupName, t);
			throw new ApsSystemException("errore in recupero sezioni relative al gruppo " + groupName, t);
		}
		return sections;
	}
	
	
	protected void loadSections() throws ApsSystemException {
		List<Section> sections = null;
		try {
			sections = this.getSectionDAO().loadSections();
			if (sections.isEmpty()) {
				Section root = this.createRoot();
				this.addSection(root);
			} else {
				this.build(sections);
			}
//			this.notifyPostChanging(PostOperationEvent.REFRESH_SECTIONS_TREE);
			getSearchManager().startReloadPostReferences();
		} catch (Throwable t) {
			_logger.error("Errore in caricamento albero sezioni", t);
			throw new ApsSystemException("Errore in caricamento albero sezioni.", t);
		}
	}

	private void notifyPostChanging(int operationCode) throws ApsSystemException {
		_logger.debug("Invio notifica ricaricamento generale indici forum. codice evento: {}",operationCode);
		PostOperationEvent event = new PostOperationEvent();
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
	
	private void build(List<Section> sections) throws ApsSystemException {
		try {
			Section root = null;
			Map<String, Section> sectionMap = new HashMap<String, Section>();
			for (int i = 0; i < sections.size(); i++) {
				Section section = (Section) sections.get(i);
				sectionMap.put(section.getCode(), section);
				if (section.getCode().equals(section.getParentCode())) {
					root = section;
				}
			}
			for (int i = 0; i < sections.size(); i++) {
				Section section = (Section) sections.get(i);
				Section parent = (Section) sectionMap.get(section.getParentCode());
				if (section != root) {
					parent.addChild(section);
				}
				section.setParent(parent);
			}
			if (root == null) {
				throw new ApsSystemException( "Errore albero sezioni: root non definita");
			}
			_root = root;
			_sections = sectionMap;
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Errore in costruzione albero sezioni", t);
			throw new ApsSystemException("Errore in costruzione albero sezioni", t);
		}
	}

	private boolean verifyRequiredMovement(int position, boolean moveUp, int dimension) {
		boolean result = true;
		if (moveUp) {
			if (position == 0) {
				result = false;	
			}
		} else {
			if (position == (dimension-1)) {
				result = false;
			}
		}
		return result;
	}

	protected Section createRoot() {
		Section root = new Section();
		root.setCode("forum");
		root.setParentCode("forum");
		List<Lang> langs = this.getLangManager().getLangs();
		ApsProperties titles = new ApsProperties();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
			titles.setProperty(lang.getCode(), "forum");
		}
		root.setTitles(titles);
		ApsProperties descriptions = new ApsProperties();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
			descriptions.setProperty(lang.getCode(), "forum");
		}
		root.setDescriptions(descriptions);
		root.setOpen(true);
		Set<String> groups = new HashSet<String>();
		groups.add(Group.FREE_GROUP_NAME);
		root.setGroups(groups);
		root.setUnauthBahaviour(Section.UNAUTH_BEHAVIOUR_READONLY);
		return root;
	}

	public void setSections(Map<String, Section> sections) {
		this._sections = sections;
	}
	public Map<String, Section> getSections() {
		return _sections;
	}

	public void setSectionDAO(ISectionDAO sectionDAO) {
		this._sectionDAO = sectionDAO;
	}
	public ISectionDAO getSectionDAO() {
		return _sectionDAO;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	public ILangManager getLangManager() {
		return _langManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	public IForumSearchManager getSearchManager() {
		return _searchManager;
	}
	public void setSearchManager(IForumSearchManager searchManager) {
		this._searchManager = searchManager;
	}
	
	private Section _root;
	private Map<String, Section> _sections;
	private ISectionDAO _sectionDAO;
	private ILangManager _langManager;
	private IAuthorizationManager _authorizationManager;
	private IForumSearchManager _searchManager;
	
}
