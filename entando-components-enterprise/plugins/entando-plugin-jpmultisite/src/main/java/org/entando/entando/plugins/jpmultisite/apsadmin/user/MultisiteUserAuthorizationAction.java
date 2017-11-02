/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpmultisite.apsadmin.user;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.group.Group;
import java.util.ArrayList;

import java.util.List;

import org.entando.entando.apsadmin.user.UserAuthorizationAction;
import org.entando.entando.apsadmin.user.UserAuthsFormBean;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class MultisiteUserAuthorizationAction extends UserAuthorizationAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(MultisiteUserAuthorizationAction.class);
	
	@Override
	public String edit() {
		try {
			String result = this.checkUser();
			if (null != result) return result;
			List<Authorization> authorizations = super.getAuthorizationManager().getUserAuthorizations(this.getUsername());
			List<Authorization> newAuthorizations = this.cleanAuthorizations(authorizations);
			UserAuthsFormBean userAuthsFormBean = new MultisiteUserAuthsFormBean(this.getUsername(), newAuthorizations);
			this.getRequest().getSession().setAttribute(CURRENT_FORM_USER_AUTHS_PARAM_NAME,  userAuthsFormBean);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private String checkUser() throws Throwable {
		if (!this.existsUser()) {
			this.addActionError(this.getText("error.user.notExist"));
			return "userList";
		}
		if (SystemConstants.ADMIN_USER_NAME.equals(this.getUsername())) {
			this.addActionError(this.getText("error.user.cannotModifyAdminUser"));
			return "userList";
		}
		if (this.isCurrentUser()) {
			this.addActionError(this.getText("error.user.cannotModifyCurrentUser"));
			return "userList";
		}
		return null;
	}
	
	@Override
	public List<Group> getGroups() {
		return this.cleanGroups(super.getGroups());
	}
	
	protected List<Group> cleanGroups(List<Group> groups) {
		List<Group> newList = new ArrayList<Group>();
		String multisiteCodeSuffix = MultisiteUtils.getMultisiteCodeSuffix(this.getRequest());
		for (Group group : groups) {
			String name = group.getName();
			if (name.contains(multisiteCodeSuffix) || 
					Group.FREE_GROUP_NAME.equals(name) || 
					Group.ADMINS_GROUP_NAME.equals(name)) {
				newList.add(group);
			}
		}
		return newList;
	}
	
	protected List<Authorization> cleanAuthorizations(List<Authorization> authorizations) {
		List<Authorization> newList = new ArrayList<Authorization>();
		String multisiteCodeSuffix = MultisiteUtils.getMultisiteCodeSuffix(this.getRequest());
		for (int i = 0; i < authorizations.size(); i++) {
			Authorization authorization = authorizations.get(i);
			Group group = (null != authorization) ? authorization.getGroup() : null;
			if (null != group) {
				String name = group.getName();
				if (name.contains(multisiteCodeSuffix) || 
						Group.FREE_GROUP_NAME.equals(name) || 
						Group.ADMINS_GROUP_NAME.equals(name)) {
					newList.add(authorization);
				}
			}
		}
		return newList;
	}
	
}
