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
package org.entando.entando.plugins.jpsugarcrm.aps.services.client;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.entando.entando.plugins.jpsugarcrm.aps.JpSugarCRMSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.sugarcrm.www.sugarcrm.Entry_value;
import com.sugarcrm.www.sugarcrm.Get_entry_list_result_version2;
import com.sugarcrm.www.sugarcrm.Link_name_to_fields_array;
import com.sugarcrm.www.sugarcrm.Name_value;
import com.sugarcrm.www.sugarcrm.SugarsoapPortTypeProxy;
import com.sugarcrm.www.sugarcrm.User_auth;

/**
 * @author E.Santoboni
 * @author Salvatore Isaja, Taktyka s.r.l. (added support for the "redir" parameter)
 */
public class SugarCRMClientManager extends AbstractService implements ISugarCRMClientManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SugarCRMClientManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public String executeLogin(UserDetails user) throws ApsSystemException {
		return executeLogin(user, null);
	}

	@Override
	public String executeLogin(UserDetails user, String redirectUrl) throws ApsSystemException {
		if (null == user) {
			return null;
		}
		String url = null;
		try {
			SugarsoapPortTypeProxy ws = new SugarsoapPortTypeProxy();
			// DEFAULT ENDPOINT http://localhost/sugarcrm/service/v2/soap.php
			String baseUrl = this.getConfigManager().getParam(JpSugarCRMSystemConstants.SUGAR_BASE_URL_PARAM_NAME);
			String ldapAuth = this.getConfigManager().getParam(JpSugarCRMSystemConstants.SUGAR_LDAP_AUTH);
			boolean isLdap = (null != ldapAuth && ldapAuth.equalsIgnoreCase("true"));
			if (null == baseUrl) {
				ws.setEndpoint(null);
			} else {
				if (!baseUrl.endsWith("/")) {
					baseUrl += "/";
				}
				String endPoint = baseUrl + "service/v2/soap.php";
				ws.setEndpoint(endPoint);
			}
			User_auth sugarUser = new User_auth();
			sugarUser.setUser_name(user.getUsername());
			if (!user.getUsername().equals(SystemConstants.GUEST_USER_NAME)
					&& null != user.getPassword()) {
				if (isLdap) {
					/* send password in plain text */
					sugarUser.setPassword(user.getPassword());
					_logger.debug("jpsugarCRM: server auth enforced by LDAP: skipping MD5 generation");
				} else {
					/* encrypt password with MD5 */
					MessageDigest messageDiget = MessageDigest.getInstance("MD5");

					messageDiget.update(user.getPassword().getBytes());
					sugarUser.setPassword((new BigInteger(1, messageDiget.digest())).toString(16));
				}
				Entry_value loginRes = ws.login(sugarUser, this.getClass().getName(), null);
				//System.out.println("getModule_name=" + loginRes.getModule_name());

				String sessionID = loginRes.getId();


				//System.out.println("Session ID: " + sessionID);
				int result = ws.seamless_login(sessionID);
				//System.out.println("RESULT: " + result);
				if (result == 1) {
					if (redirectUrl == null || redirectUrl.trim().length() == 0) {
						url = baseUrl + "index.php?module=Home&action=index&MSID=" + sessionID;
					} else {
						url = baseUrl + redirectUrl + "&MSID=" + sessionID;
					}
					//System.out.println(url);
				}

			} else {
				_logger.debug("jpsugarcrm: guest user (or users with invalid passwords) are prevdentod from loggin on");
			}
		} catch (Throwable t) {
			_logger.error("Error logging user {}", user.getUsername(), t);
			throw new ApsSystemException("Error logging user " + user.getUsername(), t);
		}
		return url;
	}

	public List<CrmLeadInfo> getLeads(UserDetails user, int maxResults) throws ApsSystemException {
		List<CrmLeadInfo> fullResult = new ArrayList<CrmLeadInfo>();
		String sessionID = null;
		try {
			//1 -------------------------------------------------- LOGIN ----------------
			if (null == user) {
				return null;
			}
			SugarsoapPortTypeProxy ws = new SugarsoapPortTypeProxy();
			String baseUrl = this.getConfigManager().getParam(JpSugarCRMSystemConstants.SUGAR_BASE_URL_PARAM_NAME);
			String ldapAuth = this.getConfigManager().getParam(JpSugarCRMSystemConstants.SUGAR_LDAP_AUTH);
			boolean isLdap = (null != ldapAuth && ldapAuth.equalsIgnoreCase("true"));
			if (null == baseUrl) {
				ws.setEndpoint(null);
			} else {
				if (!baseUrl.endsWith("/")) {
					baseUrl += "/";
				}
				String endPoint = baseUrl + "service/v2/soap.php";
				ws.setEndpoint(endPoint);
			}
			User_auth sugarUser = new User_auth();
			sugarUser.setUser_name(user.getUsername());
			if (!user.getUsername().equals(SystemConstants.GUEST_USER_NAME)	&& null != user.getPassword()) {
				if (isLdap) {
					/* send password in plain text */
					sugarUser.setPassword(user.getPassword());
					_logger.debug("jpsugarCRM: server auth enforced by LDAP: skipping MD5 generation");
				} else {
					/* encrypt password with MD5 */
					MessageDigest messageDiget = MessageDigest.getInstance("MD5");

					messageDiget.update(user.getPassword().getBytes());
					sugarUser.setPassword((new BigInteger(1, messageDiget.digest())).toString(16));
				}
				Entry_value loginRes = ws.login(sugarUser, this.getClass().getName(), null);
				//System.out.println("getModule_name=" + loginRes.getModule_name());
				sessionID = loginRes.getId();
			}

			if (null == sessionID) return fullResult;

			//2 -------------------------------------------------- USER ----------------

			CrmUserInfo userInfo = null;
			Get_entry_list_result_version2 crmUser  = ws.get_entry_list(sessionID, "Users", "user_name='"+user.getUsername() + "'", "", 0, new String[]{"first_name","last_name"}, null, 1, 0);
			if (null != crmUser) {
				userInfo = new CrmUserInfo();
				userInfo.setProperties(crmUser.getEntry_list());
			}
			if (null == userInfo) return fullResult;
			
			//3 -------------------------------------------------- LIST ----------------

			String[] select_fields = null;//{"id", "name", "title"};

			Link_name_to_fields_array[] link_name_to_fields_array = null;
			
			String moduleName = "Leads";
			String query = "assigned_user_name='" + userInfo.getFullName() + "'";

			String order_by = "date_modified DESC";
			int offset = 0;
			int max_results = maxResults;
			int deleted = 0;

			Get_entry_list_result_version2 x = ws.get_entry_list(sessionID, moduleName, query, order_by, offset, select_fields, link_name_to_fields_array, max_results, deleted);
			if (null != x) {
				Entry_value[] list = x.getEntry_list();
				for (int i = 0; i < list.length; i++) {
					CrmLeadInfo lead = new CrmLeadInfo();
					lead.setProperties(new HashMap<String, String>());
					Entry_value entry = list[i];
					Name_value[] nv = entry.getName_value_list();
					for (int k = 0; k < nv.length; k++) {
						Name_value n = nv[k];
						lead.getProperties().put(n.getName(), n.getValue());
					}
					fullResult.add(lead);
				}
			}

		} catch (Throwable t) {
			_logger.error("Error getLeads for user {}", user.getUsername(), t);
			throw new ApsSystemException("Error getLeads for user " + user.getUsername(), t);
		}
		return fullResult;
	}


	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	private ConfigInterface _configManager;

}