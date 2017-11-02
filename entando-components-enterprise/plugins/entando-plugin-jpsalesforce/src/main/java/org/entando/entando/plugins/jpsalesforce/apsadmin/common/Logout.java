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
package org.entando.entando.plugins.jpsalesforce.apsadmin.common;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.SalesforceFormAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class Logout {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceFormAction.class);
	
	@After("execution(* com.agiletec.apsadmin.common.DispatchAction.doLogout())")
	public void doLogout(JoinPoint joinPoint){
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.removeAttribute(JpSalesforceConstants.SESSIONPARAM_MANNED_SAD);
		_logger.info("session objects destroyed on logout");
	}
}
