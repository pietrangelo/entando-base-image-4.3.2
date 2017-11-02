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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.searchengine.IForumSearchManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class SearchAction extends AbstractForumAction implements ISearchAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SearchAction.class);

	@Override
	public String search() {
		return SUCCESS;
	}

	public List<Integer> getPosts() {
		List<Integer> posts = null;
		try {
			if (null != this.getTextToFind() && this.getTextToFind().trim().length() > 0) {
				List<Group> groups = this.getAllowedGroups(this.getCurrentUser());
				Set<String> userGroups = new HashSet<String>();
				if (null != groups) {
					Iterator<Group> it = groups.iterator();
					while (it.hasNext()) {
						userGroups.add(it.next().getName());
					}
				}
				posts = this.getSearchManager().searchPosts(this.getTextToFind(), userGroups);
			}

		} catch (Throwable t) {
			_logger.error("error in getPosts", t);
		}
		return posts;
	}

	protected List<Group> getAllowedGroups(UserDetails user) {
		List<Group> groups = new ArrayList<Group>();
		List<Group> groupsOfUser = this.getAuthorizationManager().getGroupsOfUser(user);
		boolean isAdministrator = false;
		Iterator<Group> iter = groupsOfUser.iterator();
    	while (iter.hasNext()) {
    		Group group = iter.next();
    		if (group.getName().equals(Group.ADMINS_GROUP_NAME)) {
    			isAdministrator = true;
    			break;
    		}
    	}
		if (isAdministrator) {
			Collection<Group> collGroup = this.getGroupManager().getGroups();
			groups.addAll(collGroup);
		} else {
			groups.addAll(groupsOfUser);
		}
    	return groups;
    }

	public Section getCurrentSection() {
		Section section = null;
		try {
			section = this.getSection(this.getSectionCode());
		} catch (Throwable t) {
			_logger.error("errore in caricamento sezioneCorrente", t);
			throw new RuntimeException("errore in caricamento sezioneCorrente " );
		}
		return section;
	}

	public String[] getBreadCrumbs() {
		this.getRequest().setAttribute(JpforumSystemConstants.ATTR_SHOW_LAST_BREADCRUMB, true);
		return super.getBreadCrumbs(this.getCurrentSection().getCode());
	}

	public void setTextToFind(String textToFind) {
		this._textToFind = textToFind;
	}
	public String getTextToFind() {
		return _textToFind;
	}

	public void setSearchManager(IForumSearchManager searchManager) {
		this._searchManager = searchManager;
	}
	protected IForumSearchManager getSearchManager() {
		return _searchManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	public IGroupManager getGroupManager() {
		return _groupManager;
	}

	public String getSectionCode() {
		return _sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this._sectionCode = sectionCode;
	}

	private String _textToFind;
	private IForumSearchManager _searchManager;
	private IGroupManager _groupManager;
	private String _sectionCode;

}
