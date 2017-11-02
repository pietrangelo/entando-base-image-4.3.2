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
package org.entando.entando.plugins.jpjasper.apsadmin.config;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.JasperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.system.BaseAction;

public class JasperConfigAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperConfigAction.class);
	
	public String entryConfig() {
		try {
			JasperConfig config = this.getJasperServerManager().getJasperConfig();
			if (null == config) {
				config = new JasperConfig();
			}
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("error in entryConfig", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String saveConfig() {
		try {
			JasperConfig config = this.getConfig();
			this.getJasperServerManager().updateJasperConfig(config);
			this.addActionMessage(this.getText("message.jasper.config.updated"));
		} catch (Throwable t) {
			_logger.error("error saveConfigin ", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String pingServer() {
		HttpURLConnection connection = null;
		try {
			String url = this.getConfig().getBaseURL();
			if (StringUtils.isBlank(url)) {
				this.addActionError(this.getText("message.jasper.server.connection.ko"));
			} else {
				URL u = new URL(url);
				connection = (HttpURLConnection) u.openConnection();
				connection.setRequestMethod("HEAD");
				int code = connection.getResponseCode();
				if (code != 200) {
					this.addActionError(this.getText("message.jasper.server.connection.ko"));
				} else {
					this.addActionMessage(this.getText("message.jasper.server.connection.ok"));
				}
			}
		} catch (Throwable t) {
			this.addActionError(this.getText("message.jasper.server.connection.ko"));
			//ApsSystemUtils.logThrowable(t, this, "pingServer");
			//return FAILURE;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return SUCCESS;
	}

    /**
     * Return a plain list of the free pages in the portal.
     * @return the list of the free pages of the portal.
     */
    public List<IPage> getFreePages() {
        IPage root = this.getPageManager().getRoot();
        List<IPage> pages = new ArrayList<IPage>();
        this.addPages(root, pages);
        return pages;
    }

    private void addPages(IPage page, List<IPage> pages) {
        if (page.getGroup().equals(Group.FREE_GROUP_NAME)) {
            pages.add(page);
        }
        IPage[] children = page.getChildren();
        for (int i = 0; i < children.length; i++) {
            this.addPages(children[i], pages);
        }
    }


	public JasperConfig getConfig() {
		return _config;
	}
	public void setConfig(JasperConfig config) {
		this._config = config;
	}

	protected IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}
	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}


	private IJasperServerManager _jasperServerManager;
	private IPageManager _pageManager;
	private JasperConfig _config;
}
