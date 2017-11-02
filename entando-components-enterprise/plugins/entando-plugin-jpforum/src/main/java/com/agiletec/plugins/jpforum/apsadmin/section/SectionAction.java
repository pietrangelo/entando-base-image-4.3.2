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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.apsadmin.system.BaseActionHelper;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.apsadmin.section.helper.ISectionActionHelper;

public class SectionAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SectionAction.class);

	@Override
	public void validate() {
		super.validate();
		this.checkCode();
		this.checkTitles();
		this.checkDescriptions();
	}

	private void checkCode() {
		String code = this.getCode();
		if ((this.getStrutsAction() == ApsAdminSystemConstants.ADD)	&& null != code && code.trim().length() > 0) {
			String currectCode = BaseActionHelper.purgeString(code.trim());
			if (currectCode.length() > 0 && null != this.getSectionManager().getSection(currectCode)) {
				String[] args = {currectCode};
				this.addFieldError("code", this.getText("jpforum.error.entrySection.duplicateCode.maskmsg", args));
			}
			this.setCode(currectCode);
		}
	}

	private void checkTitles() {
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String titleKey = "title"+lang.getCode();
			String title = this.getRequest().getParameter(titleKey);
			if (null != title) {
				this.getTitles().put(lang.getCode(), title.trim());
			}
			if (null == title || title.trim().length() == 0) {
				String[] args = {lang.getDescr()};
				this.addFieldError(titleKey, this.getText("jpforum.error.entrySection.insertTitle.maskmsg", args));
			}
		}
	}
	
	/**
	 * in fase di aggiunta e rimozione gruppi (submit che no deve passare per la validazione)
	 * ripopola i campi apsProperties titolo e descrizione
	 */
	private void populateProperties() {
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String titleKey = "title"+lang.getCode();
			String title = this.getRequest().getParameter(titleKey);
			if (null != title) {
				this.getTitles().put(lang.getCode(), title.trim());
			}
			String descriptionKey = "description"+lang.getCode();
			String description = this.getRequest().getParameter(descriptionKey);
			if (null != description) {
				this.getDescriptions().put(lang.getCode(), description.trim());
			}
		}
	}

	private void checkDescriptions() {
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String descriptionKey = "description"+lang.getCode();
			String description = this.getRequest().getParameter(descriptionKey);
			if (null != description) {
				this.getDescriptions().put(lang.getCode(), description.trim());
			}
			if (null == description || description.trim().length() == 0) {
				String[] args = {lang.getDescr()};
				this.addFieldError(descriptionKey, this.getText("jpforum.error.entrySection.insertDescription.maskmsg", args));
			}
		}
	}

	/**
	 * Quando la sezione parent è di gruppo FREE la sezione figlia può essere associata a qualsiasi gruppo <br>
	 * Quando la sezione parent NON è di gruppo FREE la sezione figlia deve essere associata a uno o più gruppi del parent
	 * 
	 * In fase di rimozione gruppo, deve essere effettuata la verifica sui gruppi figli
	 */
	private boolean checkGroups() {
		Section section = this.createSection();//this.getSectionManager().getSection(this.getCode());
		boolean okWithParent = this.checkParentGroups(section);
		boolean okWithChildren = this.checkChildrenGroups(section);
		boolean checkGroups = okWithChildren && okWithParent;
//		if (!checkGroups) {
//			this.addFieldError("groups", this.getText("jpforum.error.entrySection.invalidGroups.maskmsg"));
//		}
		return checkGroups;
	}

	private boolean checkParentGroups(Section section) {
		if (section.isRoot()) return true;
		Section parent = section.getParent();
		Set<String> virtualGroups = new HashSet<String>(section.getGroups());
		virtualGroups.add(this.getSelectedGroup());
		if (parent.getGroups().size() == 1 && parent.getGroups().contains(Group.FREE_GROUP_NAME)) return true;
		return parent.getGroups().containsAll(virtualGroups);
	}

	private boolean checkChildrenGroups(Section section) {
		if (null == section.getChildren() || section.getChildren().length == 0) return true;
		Set<String> virtualGroups = new HashSet<String>(section.getGroups());
		virtualGroups.remove(this.getSelectedGroup());
		boolean check = true;
		ITreeNode[] children = section.getChildren();
		for (int i = 0; i < children.length; i++) {
			Section child = (Section) children[i];
			if (!virtualGroups.containsAll(child.getGroups())) {
				check = false;
				this.addFieldError("groups", this.getText("jpforum.error.secion.lessGroups.children"));
				break;
			}
		}
		return check;
	}
	
	public String newSection() {
		String selectedNode = this.getSelectedNode();
		try {
			String check = this.checkSelectedNode(this.getSelectedNode());
			if (null != check) return check;
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			this.setParentCode(selectedNode);
		} catch (Throwable t) {
			_logger.error("error in newSection", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String saveSection() {
		try {
			Section section = this.createSection();
			if (null == section.getGroups() || section.getGroups().size() == 0) {
				this.addFieldError("groups", this.getText("jpforum.error.entrySection.Group.required"));
				return INPUT;
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getSectionManager().addSection(section);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getSectionManager().updateSection(section);
			}
		} catch (Throwable t) {
			_logger.error("error in saveSection", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String editSection() {
		String selectedNode = this.getSelectedNode();
		try {
			String check = this.checkSelectedNode(this.getSelectedNode());
			if (null != check) return check;
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
			Section currentSection = this.getSectionManager().getSection(selectedNode);
			this.populateForm(currentSection);
		} catch (Throwable t) {
			_logger.error("error in editSection", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trashSection() {
		String selectedNode = this.getSelectedNode();
		try {
			String check = this.checkSelectedNode(this.getSelectedNode());
			if (null != check) return check;
			if (this.getSectionManager().getRoot().getCode().equals(selectedNode)) {
				this.addActionError(this.getText("jpforum.error.rootDelete.notAllowed"));
				return "sectionTree";
			}
			Map<Integer, List<Integer>> threads = this.getThreadManager().getThreads(this.getSelectedNode());
			if (threads.size() > 0) {
				this.addActionError(this.getText("jpforum.error.delete.section.containsThreads"));
				return "sectionTree";
			}
			Section currentSection = this.getSectionManager().getSection(this.getSelectedNode());
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
			this.populateForm(currentSection);
		} catch (Throwable t) {
			_logger.error("error in trashSection", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String deleteSection() {
		Section selectedSection = null;
		try {
			if (null == this.getCode() || this.getCode().trim().length() == 0) {
				this.addActionError(this.getText("jpforum.error.section.noSelection"));
				return "sectionTree";
			}
			selectedSection = (Section) this.getSectionManager().getNode(this.getCode());
			if (null == selectedSection) {
				this.addActionError(this.getText("jpforum.error.section.selectedSection.null"));
				return "sectionTree";
			}
			if (!this.isUserAllowed(selectedSection)) {
				this.addActionError(this.getText("jpforum.error.section.userNotAllowed"));
				return "sectionTree";
			}
			if (this.getSectionManager().getRoot().getCode().equals(this.getCode())) {
				this.addActionError(this.getText("jpforum.error.rootDelete.notAllowed"));
				return "sectionTree";
			}
			Map<Integer, List<Integer>> threads = this.getThreadManager().getThreads(this.getSelectedNode());
			if (threads.size() > 0) {
				this.addActionError(this.getText("jpforum.error.delete.section.containsThreads"));
				return "sectionTree";
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getSectionManager().deleteSection(selectedSection.getCode());
			}
		} catch (Throwable t) {
			_logger.error("error in deleteSection", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String addGroup() {
		try {
			this.populateProperties();
			if (this.checkGroups()) {
				if (null == this.getGroups()) {
					this.setGroups(new HashSet<String>());
				}
				if (!this.getGroups().contains(this.getSelectedGroup())) {
					this.getGroups().add(this.getSelectedGroup());
				}
			} else {
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in addGroup", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String removeGroup() {
		try {
			this.populateProperties();
			if (this.checkGroups()) {
				this.getGroups().remove(this.getSelectedGroup());
			} else {
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in removeGroup", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}
	
	public List<Group> getAvailableGroups() {
		Map<String, Group> groupsMap = this.getGroupManager().getGroupsMap();
		List<Group> groupList = new ArrayList<Group>();
		Section parent = this.getSectionManager().getSection(this.getParentCode());
		Section currentSection = this.getSectionManager().getSection(this.getSelectedNode());
		List<String> parentGroups = new ArrayList<String>(parent.getGroups());
		if (currentSection.isRoot() || (parentGroups.size() == 1 && parentGroups.get(0).equals(Group.FREE_GROUP_NAME))) {
			groupList.addAll(groupsMap.values());
		} else {
			for (int i = 0; i < parentGroups.size(); i++) {
				String groupName = parentGroups.get(i);
				groupList.add(this.getGroupManager().getGroup(groupName));
			}
		}
		return groupList;
	}
	
	public List<UserDetails> getAvailableModerators() throws Throwable {
		List<UserDetails> currentSectionModerarors = new ArrayList<UserDetails>();
		try {
			Set<String> codes = this.getGroups();
			if (null == codes) {
				return currentSectionModerarors;
			}
			List<String> list = new ArrayList<String>();
			Iterator<String> iter = codes.iterator();
			while (iter.hasNext()) {
				String groupName = iter.next();
				List<String> usernames = this.getAuthorizationManager().getUsersByAuthorities(groupName, JpforumSystemConstants.ROLE_SECTION_MODERATOR, true);
				for (int i = 0; i < usernames.size(); i++) {
					String username = usernames.get(i);
					if (!list.contains(username)) {
						UserDetails user = this.getUserManager().getUser(username);
						if (null != user) {
							currentSectionModerarors.add(user);
						}
						list.add(username);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore calcolo moderatori sezione", t);
			throw new ApsSystemException("Errore calcolo moderatori sezione", t);
		}
		return currentSectionModerarors;
	}
	
	public Group getGroup(String groupName) {
		return this.getGroupManager().getGroup(groupName);
	}

	private Section createSection() {
		Section section = new Section();
		section.setCode(this.getCode());
		section.setParentCode(this.getParentCode());
		section.setParent(this.getSectionManager().getSection(this.getParentCode()));
		section.setOpen(this.isOpen());
		section.setTitles(this.getTitles());
		section.setDescriptions(this.getDescriptions());
		section.setUnauthBahaviour(this.getUnauthBahaviour());
		section.setGroups(this.getGroups());
		if (this.getStrutsAction() != ApsAdminSystemConstants.ADD) {
			section.setChildren(this.getSectionManager().getSection(this.getCode()).getChildren());
		}
		return section;
	}

	private void populateForm(Section currentSection) {
		this.setCode(currentSection.getCode());
		this.setParentCode(currentSection.getParentCode());
		this.setPosition(currentSection.getPosition());
		this.setOpen(currentSection.isOpen());
		this.setTitles(currentSection.getTitles());
		this.setDescriptions(currentSection.getDescriptions());
		this.setUnauthBahaviour(currentSection.getUnauthBahaviour());
		this.setGroups(currentSection.getGroups());
	}

	public ITreeNode getTreeRootNode() {
		ITreeNode node = null;
		try {
			node = this.getHelper().getAllowedTreeRoot(this.getCurrentUser());
		} catch (Throwable t) {
			_logger.error("error in getTreeRootNode", t);
		}
		return node;
	}
	
	private boolean isUserAllowed(Section section) throws Throwable {
		return this.getHelper().isUserAllowed(section, this.getCurrentUser());
	}
	
	protected String checkSelectedNode(String selectedNode) throws Throwable {
		if (null == selectedNode || selectedNode.trim().length() == 0) {
			this.addActionError(this.getText("jpforum.error.section.noSelection"));
			return "sectionTree";
		}
		Section selectedSection = (Section) this.getSectionManager().getNode(selectedNode);
		if (null == selectedSection) {
			this.addActionError(this.getText("jpforum.error.section.selectedSection.null"));
			return "sectionTree";
		}
		if (!this.isUserAllowed(selectedSection)) {
			this.addActionError(this.getText("jpforum.error.section.userNotAllowed"));
			return "sectionTree";
		}
		return null;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public void setSelectedNode(String selectedNode) {
		this._selectedNode = selectedNode;
	}
	public String getSelectedNode() {
		return _selectedNode;
	}

	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	public String getParentCode() {
		return _parentCode;
	}
	public void setParentCode(String parentCode) {
		this._parentCode = parentCode;
	}

	public int getPosition() {
		return _position;
	}
	public void setPosition(int position) {
		this._position = position;
	}

	public boolean isOpen() {
		return _open;
	}
	public void setOpen(boolean open) {
		this._open = open;
	}

	public ApsProperties getTitles() {
		return _titles;
	}
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}

	public ApsProperties getDescriptions() {
		return _descriptions;
	}
	public void setDescriptions(ApsProperties descriptions) {
		this._descriptions = descriptions;
	}

	public int getUnauthBahaviour() {
		return _unauthBahaviour;
	}
	public void setUnauthBahaviour(int unauthBahaviour) {
		this._unauthBahaviour = unauthBahaviour;
	}

	public Set<String> getGroups() {
		return _groups;
	}
	public void setGroups(Set<String> groups) {
		this._groups = groups;
	}

	public void setSelectedGroup(String selectedGroup) {
		this._selectedGroup = selectedGroup;
	}
	public String getSelectedGroup() {
		return _selectedGroup;
	}

	public void setSectionManager(ISectionManager sectionManager) {
		this._sectionManager = sectionManager;
	}
	protected ISectionManager getSectionManager() {
		return _sectionManager;
	}

	public void setHelper(ISectionActionHelper helper) {
		this._helper = helper;
	}
	protected ISectionActionHelper getHelper() {
		return _helper;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	public void setThreadManager(IThreadManager threadManager) {
		this._threadManager = threadManager;
	}
	protected IThreadManager getThreadManager() {
		return _threadManager;
	}
	
	private int _strutsAction;
	private String _selectedNode;
	private String _selectedGroup;

	private String _code;
	private String _parentCode;
	private int _position;
	private boolean _open;
	private ApsProperties _titles = new ApsProperties();
	private ApsProperties _descriptions = new ApsProperties();
	private int _unauthBahaviour;
	private Set<String> _groups;

	private ISectionManager _sectionManager;
	private ISectionActionHelper _helper;
	private IGroupManager _groupManager;
	private IUserManager _userManager;
	private IThreadManager _threadManager;
	
}
