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
package org.entando.entando.plugins.jpsalesforce.aps.internalservlet;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.ISalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.apsadmin.system.BaseAction;

public class AbstractSalesforceFrontAction extends BaseAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(AbstractSalesforceFrontAction.class);
	
	/**
	 * @Deprecated Use the wp:pageWithWidget tag 
	 * Get the page where the widget for creating new Leads is deployed
	 * @return
	 */
	@Deprecated
	public String getNewLeadWidgetPageUrl() {
		return getWidgetPageUrl(JpSalesforceConstants.WIDGET_LEAD_NEW_CODE, 0);
	}
	
	/**
	 * Return the page where the widget Search lead is deployed
	 * @return
	 */
	public String getSearchLeadWidgetPageUrl() {
		return getWidgetPageUrl(JpSalesforceConstants.WIDGET_LEAD_SEARCH_CODE, 0);
	}
	
	/**
	 * Return the Lead given the ID
	 * @param id
	 * @return
	 */
	public Lead getLead(String id) {
		Lead lead = null;
		SalesforceAccessDescriptor sad = getSessionAccessor();
		try {
			lead = getSalesforceManager().getLead(id, sad);
		} catch (Throwable t) {
			_logger.debug("Error getting lead list", t);
		}
		return lead;
	}
	
	/**
	 * Returns true if the user is logged into salesforce
	 * @return
	 */
	public boolean isLogged() {
		SalesforceAccessDescriptor sad = getSessionAccessor();
		return (null != sad ? true:false);
	}
	
	/**
	 * Get the unmanned access descriptor from the session
	 * @return
	 */
	protected SalesforceAccessDescriptor getSessionAccessor() {
		SalesforceAccessDescriptor sad =
				(SalesforceAccessDescriptor) this.getRequest().getSession().getAttribute(JpSalesforceConstants.SESSIONPARAM_MANNED_SAD);
		return sad;
	}
	
	/**
	 * Get the redirection URL for the given portal page
	 * @return
	 */
	protected String getPageUrl(String pageCode) {
		String url = null;
		
		if (StringUtils.isNotBlank(pageCode)) {
			IPage page = getPageManager().getPage(pageCode);
			url = getUrlManager().createUrl(page, getCurrentLang(), null);
		} else {
			_logger.debug("Couldn't generate the page URL because of missing or invalid code");
		}
		return url;
	}
	
	/**
	 * Get the page where the desired widget is placed
	 * @param widgetCode
	 * @param idx index to the desired page
	 * @return
	 */
	private String getWidgetPageUrl(String widgetCode, int idx) {
		String url = null;
		List<IPage> newWidgetPageCodeList = null;
		
		try {
			if (StringUtils.isNotBlank(widgetCode)) {
				newWidgetPageCodeList = getPageManager().getWidgetUtilizers(widgetCode);
				if (null != newWidgetPageCodeList 
						&& !newWidgetPageCodeList.isEmpty()) {
					url = getUrlManager().createUrl(newWidgetPageCodeList.get(idx), getCurrentLang(), null);
				}
			}
		} catch (ApsSystemException e) {
			_logger.error("Error locating the page where the widget for creating new leads is placed", e);
		}	
		return url;
	}
	
	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	
	public ConfigInterface getConfigManager() {
		return _configManager;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	public ISalesforceManager getSalesforceManager() {
		return _salesforceManager;
	}

	public void setSalesforceManager(ISalesforceManager salesforceManager) {
		this._salesforceManager = salesforceManager;
	}
	
	public String getSobj() {
		return _sobj;
	}

	public void setSobj(String sobj) {
		this._sobj = sobj;
	}
	
	public String getPageCode() {
		return _pageCode;
	}

	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}
	
	public IURLManager getUrlManager() {
		return _urlManager;
	}

	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private IURLManager _urlManager;
	private IPageManager _pageManager;
	private ConfigInterface _configManager;
	private ISalesforceManager _salesforceManager;
	
	private int _strutsAction;
	// ID for every Salesforce SObject (Lead, Campaign etc etc)
	private String _sobj;
	private String _pageCode;
}
