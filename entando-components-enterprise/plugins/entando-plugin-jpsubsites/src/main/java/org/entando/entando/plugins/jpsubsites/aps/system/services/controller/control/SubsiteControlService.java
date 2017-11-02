/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.system.services.controller.control;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.ControlServiceInterface;
import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.plugins.jpsubsites.aps.system.JpsubsitesSystemConstants;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.ISubsiteManager;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubsiteControlService implements ControlServiceInterface {
	
	private static final Logger _logger = LoggerFactory.getLogger(SubsiteControlService.class);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("{} : initialized", this.getClass().getName());
	}
	
	@Override
	public int service(RequestContext reqCtx, int status) {
		if (status == ControllerManager.ERROR) {
			return status;
		}
		int retStatus = ControllerManager.CONTINUE;
		IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		try {
			ISubsiteManager subsiteManager = this.getSubsiteManager();
			Subsite subsite = subsiteManager.getSubsiteForPage(page);
			if (subsite != null) {
				reqCtx.getRequest().setAttribute(JpsubsitesSystemConstants.REQUEST_PARAM_CURRENT_SUBSITE, subsite);
			}
		} catch (Throwable t) {
			retStatus = ControllerManager.SYS_ERROR;
			ApsSystemUtils.logThrowable(t, this, "service", "An error occurred in subsite control");
		}
		return retStatus;
	}
	
	protected ISubsiteManager getSubsiteManager() {
		return _subsiteManager;
	}
	public void setSubsiteManager(ISubsiteManager subsiteManager) {
		this._subsiteManager = subsiteManager;
	}
	
	private ISubsiteManager _subsiteManager;
	
}