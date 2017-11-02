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
package com.agiletec.plugins.jpforum.apsadmin.section.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.TreeNodeBaseActionHelper;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class SectionActionHelper extends TreeNodeBaseActionHelper implements ISectionActionHelper {
	
	@Override
	protected ITreeNode getRoot() {
		return this.getSectionManager().getRoot();
	}
	
	@Override
	protected ITreeNode getTreeNode(String code) {
		return this.getSectionManager().getNode(code);
	}
	
	@Override
	public ITreeNode getAllowedTreeRoot(UserDetails user) throws ApsSystemException {
		List<String> groupCodes = this.getGroupCodes(user);
		return this.getAllowedTreeRoot(groupCodes);
	}
	
	@Override
	public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes) throws ApsSystemException {
		ITreeNode root = null;
		if (groupCodes.contains(Group.ADMINS_GROUP_NAME)) {
			root = this.getRoot();
		} else {
			Section pageRoot = (Section) this.getRoot();
			root = new TreeNode();
			this.fillTreeNode((TreeNode) root, null, this.getRoot());
			this.addTreeWrapper((TreeNode) root, null, pageRoot, groupCodes);
		}
		return root;
	}

	private void addTreeWrapper(TreeNode currentNode, TreeNode parent, Section currentTreeNode, Collection<String> groupCodes) {
		ITreeNode[] children = currentTreeNode.getChildren();
		for (int i=0; i<children.length; i++) {
			Section newCurrentTreeNode = (Section) children[i];
			if (this.isUserAllowedOnView(newCurrentTreeNode, groupCodes)) {
				TreeNode newNode = new TreeNode();
				this.fillTreeNode(newNode, currentNode, newCurrentTreeNode);
				currentNode.addChild(newNode);
				this.addTreeWrapper(newNode, currentNode, newCurrentTreeNode, groupCodes);
			} else {
				this.addTreeWrapper(currentNode, currentNode, newCurrentTreeNode, groupCodes);
			}
		}
	}

	private boolean isUserAllowedOnView(Section section, Collection<String> groupCodes) {
		boolean allowed = false;
		if (section.getUnauthBahaviour() == Section.UNAUTH_BEHAVIOUR_READONLY) {
			allowed = true;
		} else {
			allowed = this.isUserAllowed(section, groupCodes);
		}
		return allowed;
	}
	
	public boolean isUserAllowed(Section section, Collection<String> groupCodes) {
		if (null != groupCodes && groupCodes.contains(Group.ADMINS_GROUP_NAME)) {
			return true;
		}
		//LIST OF SECTION GROUPS
		Set<String> sectionGroupsSet = section.getGroups();
		List<String> sectionGroups = new ArrayList<String>();
		sectionGroups.addAll(sectionGroupsSet);
		//USER IS ALLOWED IF THERE IS AT LEAST 1 GROUP SHARED BETWEEN SECTION AND USER
		return ListUtils.intersection(sectionGroups, new ArrayList<String>(groupCodes)).size() > 0;
	}
	
	@Override
	protected boolean isNodeAllowed(String code, Collection<String> groupCodes) {
		Section section = this.getSectionManager().getSection(code);
		return this.isUserAllowed(section, groupCodes);
	}
	
	@Override
	public boolean isUserAllowed(Section section, UserDetails user) throws ApsSystemException {
		if (this.getAuthorizationManager().isAuthOnGroup(user, Group.ADMINS_GROUP_NAME)) {
			return true;
		}
		List<String> groupCodes = this.getGroupCodes(user);
		return isUserAllowed(section, groupCodes);
	}
	
	private List<String> getGroupCodes(UserDetails user) throws ApsSystemException {
		List<String> groupCodes = new ArrayList<String>();
		List<Group> groups = this.getAuthorizationManager().getUserGroups(user);
		for (int i=0; i<groups.size(); i++) {
			groupCodes.add(groups.get(i).getName());
		}
		if (!groupCodes.contains(Group.FREE_GROUP_NAME)) {
			groupCodes.add(Group.FREE_GROUP_NAME);
		}
		return groupCodes;
	}
	
	protected ISectionManager getSectionManager() {
		return _sectionManager;
	}
	public void setSectionManager(ISectionManager sectionManager) {
		this._sectionManager = sectionManager;
	}
	
	private ISectionManager _sectionManager;
	
}
