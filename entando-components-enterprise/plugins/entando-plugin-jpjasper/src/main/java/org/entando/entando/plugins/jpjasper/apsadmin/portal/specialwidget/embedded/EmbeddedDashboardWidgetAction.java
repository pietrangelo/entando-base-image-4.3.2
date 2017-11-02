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
package org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.embedded;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedDashboardWidgetAction extends AbstractEmbeddedJasperConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(EmbeddedDashboardWidgetAction.class);

	@Override
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String dashboard = this.getWidget().getConfig().getProperty(WIDGET_PARAM_DASHBOARD_RESOURCE);
		String extraParams = this.getWidget().getConfig().getProperty(WIDGET_PARAM_DASHBOARD_EXTRA_PARAMS);
		this.setDashboardResource(dashboard);
		this.setExtraParams(extraParams);
		return result;
	}
	
	@Override
	public String getServerEndpoint() {
		return "/flow.html?_flowId=dashboardRuntimeFlow";
	}
	
	@SuppressWarnings("unchecked")
	public List<JasperResourceDescriptor> getDashboards() {
		List<JasperResourceDescriptor> resourceDescriptors = null;
		try {
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			resourceDescriptors = this.getJasperServerManager().searchRestResources(cookieHeader, null, WsTypeParams.DASHBOARD, null, true);

			if (null != resourceDescriptors && !resourceDescriptors.isEmpty()) {
				Collections.sort(resourceDescriptors, new BeanComparator("uriString"));
			}

		} catch (Throwable t) {
			_logger.error("error in getDashboards", t);
			throw new RuntimeException("error in getDashboards", t);
		}
		return resourceDescriptors;
	}
		
	public String getDashboardResource() {
		return _dashboardResource;
	}
	public void setDashboardResource(String dashboardResource) {
		this._dashboardResource = dashboardResource;
	}

	public String getExtraParams() {
		return _extraParams;
	}
	public void setExtraParams(String extraParams) {
		this._extraParams = extraParams;
	}

	private String _dashboardResource;
	private String _extraParams;

	public static final String WIDGET_PARAM_DASHBOARD_RESOURCE = "dashboardResource";
	public static final String WIDGET_PARAM_DASHBOARD_EXTRA_PARAMS = "extraParams";
	
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_DASHBOARD_RESOURCE} instead
	 */
	public static final String SHOWLET_PARAM_DASHBOARD_RESOURCE = WIDGET_PARAM_DASHBOARD_RESOURCE;
	/**
	 * @deprecated Use {@link #WIDGET_PARAM_DASHBOARD_EXTRA_PARAMS} instead
	 */
	public static final String SHOWLET_PARAM_DASHBOARD_EXTRA_PARAMS = WIDGET_PARAM_DASHBOARD_EXTRA_PARAMS;
}
