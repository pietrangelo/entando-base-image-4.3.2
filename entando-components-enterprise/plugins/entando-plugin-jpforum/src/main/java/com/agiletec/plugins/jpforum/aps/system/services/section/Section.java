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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.ApsProperties;

public class Section extends TreeNode implements Cloneable {

	private static final Logger _logger =  LoggerFactory.getLogger(Section.class);

	@Override
	public Section getParent() {
		return (Section) super.getParent();
	}
	
	@Override
	public Section clone() {
		Section cloned = null;
		try {
			cloned = (Section) super.clone();
		} catch (CloneNotSupportedException e) {
			_logger.error("error in clone", e);
		}
		return cloned;
	}
	
	public String getFullPath( String separator) {
		String code = this.getCode();
		ITreeNode parent = this.getParent();
		if (parent != null && parent.getParent() != null && 
				!parent.getCode().equals(parent.getParent().getCode())) {
			String parentCode = this.getParent().getFullPath(separator);
			code = parentCode + separator + code;
		}
		return code;
	}
	
	/**
	 * Restituisce una lista di sezione figlie della corrente, filtrate in base ai gruppi passati come parametro
	 * @param groups
	 * @return
	 */
	public List<Section> getChildren(Set<String> groups) {
		//XXX TESTARE NON MI CONVINCE
		if (null == groups) {
			groups = new HashSet<String>();
		}
		groups.add(Group.FREE_GROUP_NAME);
		List<Section> filteredChildren = null;
		Section[] children = (Section[]) this.getChildren();
		if (null != children) {
			filteredChildren = new ArrayList<Section>();
			for (int i = 0; i < children.length; i++) {
				Section child = children[i];
				if (child.getGroups().contains(Group.FREE_GROUP_NAME) || this.getCommonGroups(child.getGroups(), groups).size() > 0 ) {
					filteredChildren.add(child);
				}
			}
		}
		return filteredChildren;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getCommonGroups(Set<String> sectionGroups, Set<String> fileterGroups) {
		if (null == sectionGroups || null == fileterGroups) return new ArrayList<String>();
		List<String> sectionGroupsList = new ArrayList<String>();
		sectionGroupsList.addAll(sectionGroups);
		List<String> filterGroupsList = new ArrayList<String>();
		filterGroupsList.addAll(fileterGroups);
		List<String> intersection = ListUtils.intersection(sectionGroupsList, filterGroupsList);
		return intersection;
	}
	
	public void addGroup(String groupName) {
		if (null == this.getGroups()) {
			this.setGroups(new HashSet<String>());
		}
		this.getGroups().add(groupName);
	}
	
	public String getDescription(String langCode) {
		return this.getDescriptions().getProperty(langCode);
	}
	
    protected void setPosition(int position) {
        super.setPosition(position);
    }

	public boolean isOpen() {
		return _open;
	}
	public void setOpen(boolean open) {
		this._open = open;
	}
	
	public Set<String> getGroups() {
		if (null == _groups || _groups.size() == 0) {
			_groups = new HashSet<String>();
			//FIXME
			//_groups.add(Group.FREE_GROUP_NAME);
		} 
		return _groups;
	}
	
	public void setGroups(Set<String> groups) {
		this._groups = groups;
	}
	
	public int getUnauthBahaviour() {
		return _unauthBahaviour;
	}
	public void setUnauthBahaviour(int unauthBahaviour) {
		this._unauthBahaviour = unauthBahaviour;
	}
	
	public ApsProperties getDescriptions() {
		return _descriptions;
	}
	public void setDescriptions(ApsProperties descriptions) {
		this._descriptions = descriptions;
	}
	
	public void setParentCode(String parentCode) {
		this._parentCode = parentCode;
	}
	public String getParentCode() {
		return _parentCode;
	}

	private String _parentCode;
	private boolean _open;
	private Set<String> _groups;
	private int _unauthBahaviour;
	//private Set<String> _moderators;
	private ApsProperties _descriptions = new ApsProperties();

	public static final int UNAUTH_BEHAVIOUR_HIDDEN = 0;
	public static final int UNAUTH_BEHAVIOUR_READONLY = 1;

}
