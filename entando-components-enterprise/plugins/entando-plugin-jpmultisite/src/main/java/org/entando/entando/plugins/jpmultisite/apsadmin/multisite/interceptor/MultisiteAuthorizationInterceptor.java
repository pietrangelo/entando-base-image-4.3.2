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
package org.entando.entando.plugins.jpmultisite.apsadmin.multisite.interceptor;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.InterceptorMadMax;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteAuthorizationInterceptor extends AbstractInterceptor {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteAuthorizationInterceptor.class);

	/**
	 * In order to get access to Request before this interceptor you need to
	 * call servletConfig interceptor
	 *
	 * @param invocation
	 * @return
	 * @throws Exception
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		_logger.debug("Interceptor: " + this.getClass().getName());
		String result = null;
		HttpSession session = this.getRequest().getSession();
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		StringBuffer requestURL = this.getRequest().getRequestURL();
		String multisiteCode = this.getMultisiteManager().loadMultisiteByUrl(requestURL.toString());
		if (StringUtils.isNotEmpty(multisiteCode)) {
			if (!usernanameMatchMultisiteCodeSuffix(currentUser.getUsername()) && !SystemConstants.ADMIN_USER_NAME.equals(currentUser.getUsername())) {
				return InterceptorMadMax.DEFAULT_ERROR_RESULT;
			}
		} else if (currentUser.getUsername().contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)) {
			return InterceptorMadMax.DEFAULT_ERROR_RESULT;
		}
		result = invocation.invoke();
		_logger.debug("Interceptor: " + this.getClass().getName() + " done.");
		return result;
	}

	private boolean usernanameMatchMultisiteCodeSuffix(String username) {
		boolean res = false;
		if (username.endsWith(MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()))) {
			res = true;
		}
		return res;
	}

	private IMultisiteManager getMultisiteManager() {
		return (IMultisiteManager) ApsWebApplicationUtils.getBean(JpmultisiteSystemConstants.MULTISITE_MANAGER, this.getRequest());
	}

	private HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

}
