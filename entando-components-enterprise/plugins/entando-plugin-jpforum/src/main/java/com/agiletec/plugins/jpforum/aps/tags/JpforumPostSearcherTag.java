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
package com.agiletec.plugins.jpforum.aps.tags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.searchengine.IForumSearchManager;

public class JpforumPostSearcherTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(JpforumPostSearcherTag.class);

    @Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			IForumSearchManager searchManager = (IForumSearchManager) ApsWebApplicationUtils.getBean(JpforumSystemConstants.SEARCH_MANAGER, pageContext);
			IGroupManager groupManager = (IGroupManager) ApsWebApplicationUtils.getBean(SystemConstants.GROUP_MANAGER, pageContext);
			IAuthorizationManager authorizationManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, pageContext);
			String searchText = request.getParameter("textToFind");

			List<Integer> posts = this.getPosts(searchText, searchManager, groupManager, authorizationManager);
			this.pageContext.setAttribute(this.getVar(), posts);

			
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Errore inizializzazione tag", t);
		}
		return EVAL_PAGE;
	}

	public List<Integer> getPosts(String searchText, IForumSearchManager searchManager, IGroupManager groupManager, IAuthorizationManager authorizationManager) {
		List<Integer> posts = null;
		try {
			if (null != searchText && searchText.trim().length() > 0) {
				List<Group> groups = this.getAllowedGroups(this.getCurrentUser(), authorizationManager, groupManager);
				Set<String> userGroups = new HashSet<String>();
				if (null != groups) {
					Iterator<Group> it = groups.iterator();
					while (it.hasNext()) {
						userGroups.add(it.next().getName());
					}
				}
				posts = searchManager.searchPosts(searchText, userGroups);
			}
		} catch (Throwable t) {
			_logger.error("error in getPosts", t);
		}
		return posts;
	}

	protected List<Group> getAllowedGroups(UserDetails user, IAuthorizationManager authorizationManager, IGroupManager groupManager) {
		List<Group> groups = new ArrayList<Group>();
		List<Group> groupsOfUser = authorizationManager.getGroupsOfUser(user);
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
			Collection<Group> collGroup = groupManager.getGroups();
			groups.addAll(collGroup);
		} else {
			groups.addAll(groupsOfUser);
		}
    	return groups;
    }
	
	protected UserDetails getCurrentUser() {
		UserDetails currentUser = (UserDetails) pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		return currentUser;
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}


	private String _var;
}
