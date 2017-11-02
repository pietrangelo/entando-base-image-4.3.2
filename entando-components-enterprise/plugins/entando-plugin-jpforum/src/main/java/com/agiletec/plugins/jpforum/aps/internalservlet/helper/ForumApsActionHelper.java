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
package com.agiletec.plugins.jpforum.aps.internalservlet.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.BaseActionHelper;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class ForumApsActionHelper extends BaseActionHelper implements IForumApsActionHelper {
	
	@Override
	public boolean isUserAllowed(Section section, UserDetails user) {
		if (section == null) return false;
		//IF SECTION CONTAINS FREE_GROUP IS ALLOWED
		if(section.getGroups().contains(Group.FREE_GROUP_NAME)) return true;
		//ADMIN
		boolean isSuperuser = this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER);
		if (isSuperuser) {
			return true;
		}
		//FORUM_SUPERUSER
		boolean isForumSuperuser = this.getAuthorizationManager().isAuthOnPermission(user, JpforumSystemConstants.PERMISSION_SUPERUSER);
		if (isForumSuperuser) {
			return true;
		}
		//LIST OF USER GROUPS
		List<Group> groups = this.getAuthorizationManager().getUserGroups(user);//this.getAllowedGroups(user);
		List<String> userGroups = new ArrayList<String>();
		for (int i = 0; i < groups.size(); i++) {
			Group currentUserGroup = groups.get(i);
			userGroups.add(currentUserGroup.getName());
		}
		//LIST OF SECTION GROUPS
		Set<String> sectionGroupsSet = section.getGroups();
		List<String> sectionGroups = new ArrayList<String>();
		sectionGroups.addAll(sectionGroupsSet);
		//USER IS ALLOWED IF THERE IS AT LEAST 1 GROUP SHARED BETWEEN SECTION AND USER
		return ListUtils.intersection(sectionGroups, userGroups).size() > 0;
	}
	
	public void setSectionManager(ISectionManager sectionManager) {
		this._sectionManager = sectionManager;
	}
	public ISectionManager getSectionManager() {
		return _sectionManager;
	}

	private ISectionManager _sectionManager;
}
