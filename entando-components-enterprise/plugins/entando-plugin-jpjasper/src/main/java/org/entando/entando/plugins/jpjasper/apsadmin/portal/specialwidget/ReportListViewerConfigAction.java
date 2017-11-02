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
package org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

public class ReportListViewerConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportListViewerConfigAction.class);
	
	@Override
	public String save() {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			
			boolean hasTitleError = this.checkTitle();
			if (hasTitleError) {
				return INPUT;
			}
			
			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			_logger.debug("Saving Widget - code = {}, pageCode = {}, frame = {}", this.getWidget().getType().getCode(), this.getPageCode(), this.getFrame());
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return "configure";
	}
	
	
	protected boolean checkTitle() {
		boolean error = false;
		String titleParamPrefix = SHOWLET_PARAM_TITLE ;
		Lang defaultLang = this.getLangManager().getDefaultLang();
		String defaultTitleParam = titleParamPrefix + defaultLang.getCode();
		String defaultTitle = this.getWidget().getConfig().getProperty(defaultTitleParam);
		if (defaultTitle == null || defaultTitle.trim().length() == 0) {
			String[] args = {defaultLang.getDescr()};
			this.addFieldError(defaultTitleParam, this.getText("error.reportList.defaultLangTitle.required", args));
			error = true;
		}

		return error;
	}

	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		this.setRecursive(this.getWidget().getConfig().getProperty(SHOWLET_PARAM_RECURSIVE_SEARCH));
		this.setFolder(this.getWidget().getConfig().getProperty(SHOWLET_PARAM_FOLDER));
		this.setFilter(this.getWidget().getConfig().getProperty(SHOWLET_PARAM_FILTER_STRING));
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<JasperResourceDescriptor> getFolders() {
		List<JasperResourceDescriptor> resourceDescriptors = null;
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			resourceDescriptors = this.getJasperServerManager().searchRestResources(cookieHeader, null, WsTypeParams.FOLDER, null, true);

			if (null != resourceDescriptors && !resourceDescriptors.isEmpty()) {
				Collections.sort(resourceDescriptors, new BeanComparator("uriString"));
			}
		} catch (Throwable t) {
			_logger.error("error in getFolders", t);
			throw new RuntimeException("error in getFolders", t);
		}
		return resourceDescriptors;
	}

	public String getFolder() {
		return _folder;
	}
	public void setFolder(String folder) {
		this._folder = folder;
	}

	public String getRecursive() {
		return _recursive;
	}
	public void setRecursive(String recursive) {
		this._recursive = recursive;
	}

	protected IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}
	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}

	public String getFilter() {
		return _filter;
	}
	public void setFilter(String filter) {
		this._filter = filter;
	}

	private String _folder;
	private String _filter;
	private String _recursive;

	private IJasperServerManager _jasperServerManager;

	public static final String SHOWLET_PARAM_FOLDER = "folder";
	public static final String SHOWLET_PARAM_FILTER_STRING = "filter";
	public static final String SHOWLET_PARAM_RECURSIVE_SEARCH = "recursive";
	public static final String SHOWLET_PARAM_TITLE = "title_";
}
