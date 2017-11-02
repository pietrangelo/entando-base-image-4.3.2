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
package org.entando.entando.plugins.jpmultisite.apsadmin.portal.specialwidget.multisite;

import java.util.List;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultisiteConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(MultisiteConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String code = this.getWidget().getConfig().getProperty("code");
		this.setCode(code);
		return result;
	}

	public List<String> getMultisitesId() {
		try {
			List<String> multisites = this.getMultisiteManager().searchMultisites(null);
			return multisites;
		} catch (Throwable t) {
			_logger.error("error in getMultisitesId", t);
			throw new RuntimeException("Error getting multisites list", t);
		}
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	protected IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}
	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}

	private String _code;
	private IMultisiteManager _multisiteManager;
}

