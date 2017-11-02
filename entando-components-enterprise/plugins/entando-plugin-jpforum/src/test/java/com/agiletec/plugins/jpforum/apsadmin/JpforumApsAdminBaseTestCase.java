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
package com.agiletec.plugins.jpforum.apsadmin;

import java.util.HashMap;

import org.apache.struts2.util.TokenHelper;

import com.agiletec.ConfigTestUtils;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jpforum.PluginConfigTestUtils;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.opensymphony.xwork2.ActionContext;

public class JpforumApsAdminBaseTestCase extends ApsAdminBaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	protected void setToken() {
		String token = TokenHelper.generateGUID();
		ActionContext.getContext().setSession(new HashMap<String, Object>());
		String tokenName = "test_tokenName";
		String tokenSessionAttributeName = TokenHelper.buildTokenSessionAttributeName(tokenName);
		ActionContext.getContext().getSession().put(tokenSessionAttributeName, token);
		this.addParameter(TokenHelper.TOKEN_NAME_FIELD, new String[]{tokenName});
		this.addParameter(tokenName, new String[]{token});
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.addForumUsers();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.deleteForumUsers();
		super.tearDown();
	}
	
	/**
	 * Metodo di utilità da utilizzarsi in setUp
	 * Crea un utente, il ruolo e il permesso base per l'utilizzo del forum 
	 * @throws Exception
	 */
	protected void addForumUsers() throws Exception {
		IRoleManager roleManager = (IRoleManager) this.getService(SystemConstants.ROLE_MANAGER);
		IGroupManager groupManager= (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
		IAuthorizationManager authorizationManager = (IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
		
		if (null == roleManager.getPermission(JpforumSystemConstants.PERMISSION_FORUM_USER)) {
			Permission permissionForumUser = new Permission();
			permissionForumUser.setName(JpforumSystemConstants.PERMISSION_FORUM_USER);
			permissionForumUser.setDescription(JpforumSystemConstants.PERMISSION_FORUM_USER);
			roleManager.addPermission(permissionForumUser);
		}
		
		if (null == roleManager.getPermission(JpforumSystemConstants.PERMISSION_SECTION_MODERATOR)) {
			Permission permissionSectionModerator = new Permission();
			permissionSectionModerator.setName(JpforumSystemConstants.PERMISSION_SECTION_MODERATOR);
			permissionSectionModerator.setDescription(JpforumSystemConstants.PERMISSION_SECTION_MODERATOR);
			roleManager.addPermission(permissionSectionModerator);
		}
		
		if (null == roleManager.getPermission(JpforumSystemConstants.PERMISSION_SUPERUSER)) {
			Permission permissionForumSuperuser = new Permission();
			permissionForumSuperuser.setName(JpforumSystemConstants.PERMISSION_SUPERUSER);
			permissionForumSuperuser.setDescription(JpforumSystemConstants.PERMISSION_SUPERUSER);
			roleManager.addPermission(permissionForumSuperuser);
		}
		
		Role roleForumUser = roleManager.getRole(JpforumSystemConstants.ROLE_FORUM_USER);
		if (null == roleForumUser) {
			roleForumUser = new Role();
			roleForumUser.setDescription("forum user");
			roleForumUser.setName(JpforumSystemConstants.ROLE_FORUM_USER);
			roleForumUser.addPermission(JpforumSystemConstants.PERMISSION_FORUM_USER);
			roleManager.addRole(roleForumUser);
		}
		
		Role roleModerator = roleManager.getRole(JpforumSystemConstants.ROLE_SECTION_MODERATOR);
		if (null == roleModerator) {
			roleModerator = new Role();
			roleModerator.setDescription("forum moderator");
			roleModerator.setName(JpforumSystemConstants.ROLE_SECTION_MODERATOR);
			roleModerator.addPermission(JpforumSystemConstants.PERMISSION_FORUM_USER);
			roleModerator.addPermission(JpforumSystemConstants.PERMISSION_SECTION_MODERATOR);
			roleManager.addRole(roleModerator);
		}
		
		Role roleSuperuser = roleManager.getRole(JpforumSystemConstants.ROLE_SUPERUSER);
		if (null == roleSuperuser) {
			roleSuperuser = new Role();
			roleSuperuser.setDescription("forum superuser");
			roleSuperuser.setName(JpforumSystemConstants.ROLE_SUPERUSER);
			roleSuperuser.addPermission(JpforumSystemConstants.PERMISSION_SUPERUSER);
			roleSuperuser.addPermission(JpforumSystemConstants.PERMISSION_SECTION_MODERATOR);
			roleSuperuser.addPermission(JpforumSystemConstants.PERMISSION_FORUM_USER);
			roleManager.addRole(roleSuperuser);
		}
		
		Group freeGroup = groupManager.getGroup(Group.FREE_GROUP_NAME);
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		if (null == userManager.getUser(FORUM_USER)) {
			User baseUser = new User();
			baseUser.setUsername(FORUM_USER);
			baseUser.setPassword(FORUM_USER);
			baseUser.setDisabled(false);
			userManager.addUser(baseUser);
			authorizationManager.addUserAuthorization(FORUM_USER, Group.FREE_GROUP_NAME, JpforumSystemConstants.ROLE_FORUM_USER);
			//((IApsAuthorityManager) roleManager).setUserAuthorization(FORUM_USER, roleForumUser);
			//((IApsAuthorityManager) groupManager).setUserAuthorization(FORUM_USER, freeGroup);
		}
		
		if (null == userManager.getUser(FORUM_MODERATOR)) {
			User moderaror = new User();
			moderaror.setUsername(FORUM_MODERATOR);
			moderaror.setPassword(FORUM_MODERATOR);
			moderaror.setDisabled(false);
			userManager.addUser(moderaror);
			authorizationManager.addUserAuthorization(FORUM_MODERATOR, Group.FREE_GROUP_NAME, JpforumSystemConstants.ROLE_FORUM_USER);
			authorizationManager.addUserAuthorization(FORUM_MODERATOR, Group.FREE_GROUP_NAME, JpforumSystemConstants.ROLE_SECTION_MODERATOR);
			//((IApsAuthorityManager) roleManager).setUserAuthorization(FORUM_MODERATOR, roleModerator);
			//((IApsAuthorityManager) roleManager).setUserAuthorization(FORUM_MODERATOR, roleForumUser);
			//((IApsAuthorityManager) groupManager).setUserAuthorization(FORUM_MODERATOR, freeGroup);
		}
		
		if (null == userManager.getUser(FORUM_SUPERUSER)) {
			User superuser = new User();
			superuser.setUsername(FORUM_SUPERUSER);
			superuser.setPassword(FORUM_SUPERUSER);
			superuser.setDisabled(false);
			userManager.addUser(superuser);	
			authorizationManager.addUserAuthorization(FORUM_SUPERUSER, Group.ADMINS_GROUP_NAME, JpforumSystemConstants.ROLE_SUPERUSER);
			//((IApsAuthorityManager) roleManager).setUserAuthorization(FORUM_SUPERUSER, roleSuperuser);
		}
	}


	/**
	 * Da utilizzarsi in teardown. Elimina le mofifiche fatte dal metodo addForumUser
	 * @throws Exception
	 */
	protected void deleteForumUsers() throws Exception {
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		userManager.removeUser(FORUM_USER);
		userManager.removeUser(FORUM_MODERATOR);
		userManager.removeUser(FORUM_SUPERUSER);
	}
	
	public static final String FORUM_USER = "forumuser";
	public static final String FORUM_MODERATOR = "forummoderator";
	public static final String FORUM_SUPERUSER = "forumsuperuser";
	
}
