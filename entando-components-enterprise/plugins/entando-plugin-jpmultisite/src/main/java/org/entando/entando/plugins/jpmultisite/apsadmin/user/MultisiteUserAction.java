/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.user;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.apsadmin.user.UserAction;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteUserAction extends UserAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteUserAction.class);

	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().containsKey("username")) {
			Map<String, List<String>> fieldErrors = this.getFieldErrors();
			fieldErrors.remove("username");
			this.setFieldErrors(fieldErrors);
			String username = this.getUsername().trim();
			if(StringUtils.isBlank(username)){
				this.addFieldError("username", this.getText("requiredstring"));
			}
			String regexPattern = JpmultisiteSystemConstants.MULTISITE_CODE_REGEX;
			if (null != username && !username.matches(regexPattern)) {
				this.addFieldError("username", this.getText("wrongCharacters"));
			}
			if (null != username && username.length() > 30) {
				this.addFieldError("username", this.getText("wrongMaxLength"));
			}
		}
	}
	
	@Override
	public String save() {
		User user = null;
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				user = new User();
				user.setUsername(this.getMultisiteUsername());
				user.setPassword(this.getPassword());
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				user = (User) this.getUserManager().getUser(this.getMultisiteUsername());
				if (null != this.getPassword() && this.getPassword().trim().length()>0) {
					user.setPassword(this.getPassword());
				}
				this.checkUserProfile(user, false);
			}
			user.setDisabled(!this.isActive());
			if (this.isReset()) {
				user.setLastAccess(new Date());
				user.setLastPasswordChange(new Date());
			}
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getUserManager().addUser(user);
				this.checkUserProfile(user, true);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getUserManager().updateUser(user);
				if (null != this.getPassword() && this.getPassword().trim().length()>0) {
					this.getUserManager().changePassword(this.getMultisiteUsername(), this.getPassword());
				}
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			//ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void checkUserProfile(User user, boolean add) throws ApsSystemException {
		String username = user.getUsername();
		try {
			IUserProfile userProfile = this.getUserProfileManager().getProfile(username);
			if (null == userProfile) {
				userProfile = this.getUserProfileManager().getDefaultProfileType();
				if (null != userProfile) {
					userProfile.setId(username);
					this.getUserProfileManager().addProfile(username, userProfile);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error adding default profile for user {}", username, t);
			//ApsSystemUtils.logThrowable(t, this, "checkUserProfile");
			throw new ApsSystemException("Error adding default profile for user " + username, t);
		}
	}

	public String getMultisiteUsername() {
		String username = this.getUsername();
		String multisiteCodeSuffix = MultisiteUtils.getMultisiteCodeSuffix(this.getRequest());
		if(!username.endsWith(multisiteCodeSuffix)) {
			username = username.concat(multisiteCodeSuffix);
		}
		return username;
	}
	
	
	

}
