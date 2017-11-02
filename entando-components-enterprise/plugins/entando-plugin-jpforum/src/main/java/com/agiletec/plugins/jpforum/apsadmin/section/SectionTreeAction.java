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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpforum.aps.system.services.searchengine.IForumSearchManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.apsadmin.section.helper.ISectionActionHelper;

public class SectionTreeAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SectionTreeAction.class);

	public ITreeNode getTreeRootNode() {
		ITreeNode node = null;
		try {
			node = this.getHelper().getAllowedTreeRoot(this.getCurrentUser());
		} catch (Throwable t) {
			_logger.error("error in getTreeRootNode", t);
		}
		return node;
	}

	public String moveDown() {
		return this.moveSection(false);
	}

	public String moveUp() {
		return this.moveSection(true);
	}
	
	public String reloadIndex() {
		try {
			this.getSearchManager().startReloadPostReferences();
			_logger.debug("Ricaricamento indicizzazione dei posts avviata");
			this.addActionMessage(this.getText("jpforum.message.index.reload"));
		} catch (Throwable t) {
			_logger.error("error in reloadIndex", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected String moveSection(boolean moveUp) {
		String selectedNode = this.getSelectedNode();
		try {
			String check = this.checkSelectedNode(selectedNode);
			if (null != check) return check;
			Section currentSection = (Section) this.getSectionManager().getNode(this.getSelectedNode());
			if (!isUserAllowed((Section) currentSection.getParent())) {
				_logger.debug("Utente corrente non abilitato a spostare la sezione selezionata");
				this.addActionError(this.getText("jpforum.error.section.userNotAllowed"));
				return SUCCESS;
			}
			if (this.getSectionManager().getRoot().getCode().equals(currentSection.getCode())) {
				this.addActionError(this.getText("jpforum.error.section.movementHome.notAllowed"));
				return SUCCESS;
			}
			boolean result = this.getSectionManager().moveSection(selectedNode, moveUp);
			if (!result) {
				if (moveUp) {
					this.addActionError(this.getText("jpforum.error.section.movementUp.notAllowed"));
				} else {
					this.addActionError(this.getText("jpforum.error.section.movementDown.notAllowed"));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in moveSection", t);
			return FAILURE;
		}
		return SUCCESS;
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
		if (this.getTreeRootNode().getCode().equals(selectedNode)) {
			this.addActionError(this.getText("jpforum.error.section.selectedSection.root"));
			return "sectionTree";
		}
		if (!this.isUserAllowed(selectedSection)) {
			this.addActionError(this.getText("jpforum.error.section.userNotAllowed"));
			return "sectionTree";
		}
		return null;
	}
	
	private boolean isUserAllowed(Section section) throws Throwable {
		return this.getHelper().isUserAllowed(section, this.getCurrentUser());
	}

	public void setSelectedNode(String selectedNode) {
		this._selectedNode = selectedNode;
	}
	public String getSelectedNode() {
		return _selectedNode;
	}
	
	public void setHelper(ISectionActionHelper helper) {
		this._helper = helper;
	}
	protected ISectionActionHelper getHelper() {
		return _helper;
	}

	public void setSectionManager(ISectionManager sectionManager) {
		this._sectionManager = sectionManager;
	}
	protected ISectionManager getSectionManager() {
		return _sectionManager;
	}

	public void setSearchManager(IForumSearchManager searchManager) {
		this._searchManager = searchManager;
	}
	protected IForumSearchManager getSearchManager() {
		return _searchManager;
	}

	private String _selectedNode;
	
	private ISectionActionHelper _helper;
	private ISectionManager _sectionManager;
	private IForumSearchManager _searchManager;

}
