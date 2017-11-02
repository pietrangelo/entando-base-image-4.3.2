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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class ForumUserFinderAction extends AbstractForumAction implements IForumUserFinderAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ForumUserFinderAction.class);

	@Override
	public List<String> getUsers() {
		List<String> users = new ArrayList<String>();
		try {			
			EntitySearchFilter filters[] = new EntitySearchFilter[1];
			filters[0] = new EntitySearchFilter(this.getForumConfigManager().getProfileNickAttributeName(), true, this.getText(), true);
			filters[0].setOrder("ASC");
			List<String> usernames = ((ApsEntityManager)this.getUserProfileManager()).searchId(filters);
			List<String> forumUsersString = this.getAuthorizationManager().getUsersByRole(JpforumSystemConstants.ROLE_FORUM_USER, true);
			List<String> forumModeratorsString = this.getAuthorizationManager().getUsersByRole(JpforumSystemConstants.ROLE_SECTION_MODERATOR, true);
			List<String> forumSuperusersString = this.getAuthorizationManager().getUsersByRole(JpforumSystemConstants.ROLE_SUPERUSER, true);
			Set<String> allForumUsers = new HashSet<String>(forumUsersString);
			allForumUsers.addAll(forumUsersString);
			allForumUsers.addAll(forumModeratorsString);
			allForumUsers.addAll(forumSuperusersString);
			List<String> allUsers = new ArrayList<String>(allForumUsers);
			users = ListUtils.intersection(usernames, allUsers);
		} catch (Throwable t) {
			_logger.error("error in getUsers", t);
		}
		return users;
	}
	
	@Override
	public String[] getBreadCrumbs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Section getCurrentSection() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setText(String text) {
		this._text = text;
	}
	public String getText() {
		return _text;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private String _text;
	private IUserProfileManager _userProfileManager;
	
}