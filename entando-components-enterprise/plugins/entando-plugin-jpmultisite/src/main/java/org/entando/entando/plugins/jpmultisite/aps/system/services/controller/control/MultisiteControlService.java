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
package org.entando.entando.plugins.jpmultisite.aps.system.services.controller.control;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteControlService extends AbstractControlService {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteControlService.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public int service(RequestContext reqCtx, int status) {
		_logger.debug("{} invoked", this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		HttpServletRequest request = reqCtx.getRequest();
		HttpSession session = request.getSession();
		// if another control service ends with an error, exit immediately.
		if (status == ControllerManager.ERROR) {
			return status;
		}
		if (null == reqCtx.getExtraParam(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE)) {
			try {
				StringBuffer requestURL = reqCtx.getRequest().getRequestURL();
				String multisiteCode = this.getMultisiteManager().loadMultisiteByUrl(requestURL.toString());
				session.setAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE, multisiteCode);
				retStatus = ControllerManager.CONTINUE;
			} catch (Throwable t) {
				retStatus = ControllerManager.SYS_ERROR;
				_logger.error("Error while loading multisite code", t);
			}
		}
		return retStatus;
	}

	public IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}

	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}
	
	private IMultisiteManager _multisiteManager;

}
