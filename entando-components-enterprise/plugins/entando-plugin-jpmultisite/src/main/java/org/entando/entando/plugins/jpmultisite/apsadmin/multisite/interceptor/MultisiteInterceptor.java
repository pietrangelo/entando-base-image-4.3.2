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

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */


public class MultisiteInterceptor extends AbstractInterceptor {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteInterceptor.class);
	
	/**
	 * In order to get access to Request before this interceptor you need to call
	 * servletConfig interceptor
	 * @param invocation
	 * @return
	 * @throws Exception 
	 */

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		_logger.debug("Interceptor: " + this.getClass().getName());
		String result = null;
		HttpSession session = this.getRequest().getSession();
		if(null == session.getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE)){
			StringBuffer requestURL = this.getRequest().getRequestURL();
			String multisiteCode = this.getMultisiteManager().loadMultisiteByUrl(requestURL.toString());
			session.setAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE, multisiteCode);
			_logger.debug("MultisiteCode: " + multisiteCode);
		}
		result = invocation.invoke();
		_logger.debug("Interceptor: " + this.getClass().getName() + " done.");
		return result;
	}
	
	private IMultisiteManager getMultisiteManager() {
		return (IMultisiteManager) ApsWebApplicationUtils.getBean(JpmultisiteSystemConstants.MULTISITE_MANAGER, this.getRequest());
	}
	
	private HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

}
