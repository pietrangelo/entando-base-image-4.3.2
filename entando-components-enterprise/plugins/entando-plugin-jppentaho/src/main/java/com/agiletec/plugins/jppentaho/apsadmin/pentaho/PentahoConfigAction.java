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
package com.agiletec.plugins.jppentaho.apsadmin.pentaho;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.PentahoReportUserWidgetConfig;

public class PentahoConfigAction extends PentahoAbstractBaseAction {

	public String config() {
		UserDetails currentUser = (UserDetails) this.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		// TODO
		String username = currentUser.getUsername();
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
		String solution = null;
		Integer currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
		solution = this.extractSolutionFromConfig( username, reqCtx, currentFrame, page);
		String server = this.getDynamicReportManager().generatePentahoServerPublicUrl();
		// TODO auth
		 username = "joe";
		ITreeNode fileItem = this.getDynamicReportManager().getSolutionContents(solution, server, username);
		this.setRoot(fileItem);
		username = currentUser.getUsername();
		PentahoReportUserWidgetConfig  config = this.getDynamicReportManager().loadUserConfiguration(username, currentFrame, page.getCode());
		this.setConfig(config);
		return SUCCESS;
	}

	public String saveConfig() {
		//TODO modifica non serve la lettura dalla tabella del plugin
		UserDetails userDetails = this.getCurrentUser();
		String username = userDetails.getUsername();
		String configPath = this.getConfigPath();
		if (configPath == null || configPath.trim().length() == 0) {
			configPath = "solution";
		}
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
		Widget currShowlet =  (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		Integer currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
		this.getDynamicReportManager().saveUserConfiguration(null, configPath, username, currentFrame, currShowlet.getType(), page.getCode());
		return SUCCESS;
	}

	public String saveConfigSingleReport() {
		// TODO
		UserDetails userDetails = this.getCurrentUser();
		String username = userDetails.getUsername();
		String configPath = this.getConfigPath();
		String name = null;
		if (configPath == null || configPath.trim().length() == 0) {
			configPath = "solution";
		} else {
			int i = configPath.lastIndexOf("/");
			name = configPath.substring(i+1, configPath.length());
			configPath = configPath.substring(0, i);
		}
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		Page page = (Page) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
		Widget currShowlet =  (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		Integer currentFrame = (Integer) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_FRAME));
		this.getDynamicReportManager().saveUserConfiguration(name, configPath, username, currentFrame, currShowlet.getType(), page.getCode());
		return SUCCESS;
	}

	public boolean isReportPath(String path) {
		if (path.endsWith(".prpt")) {
			return true;
		}
		return false;
	}

	public void setConfigPath(String configPath) {
		this._configPath = configPath;
	}
	public String getConfigPath() {
		return _configPath;
	}

	public void setRoot(ITreeNode root) {
		this._root = root;
	}
	public ITreeNode getRoot() {
		return _root;
	}

	public void setConfig(PentahoReportUserWidgetConfig config) {
		this._config = config;
	}
	public PentahoReportUserWidgetConfig getConfig() {
		return _config;
	}

	private String _configPath;
    private ITreeNode _root;
    private PentahoReportUserWidgetConfig _config;

}