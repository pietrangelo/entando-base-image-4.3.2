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
package org.entando.entando.plugins.jpjasper.aps.tags;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.embedded.EmbeddedDashboardWidgetAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;

public class IFrameEmbeddedDashboardTag extends IFrameEmbeddedTag {

	private static final Logger _logger =  LoggerFactory.getLogger(IFrameEmbeddedDashboardTag.class);
	
	@Override
	public String buildIframeEndpoint(RequestContext reqCtx, UserDetails currentUser, Widget showlet) {
		String baseUrl = null;
		try {
			baseUrl = super.buildIframeEndpoint(reqCtx, currentUser, showlet);
			String dashboardResource = this.extractShowletParameter(EmbeddedDashboardWidgetAction.WIDGET_PARAM_DASHBOARD_RESOURCE, showlet.getConfig(), reqCtx);
			String extraParams = this.extractShowletParameter(EmbeddedDashboardWidgetAction.WIDGET_PARAM_DASHBOARD_EXTRA_PARAMS, showlet.getConfig(), reqCtx);
			if (!baseUrl.endsWith("&")) {
				baseUrl = baseUrl + "&";
			}
			baseUrl = baseUrl + "dashboardResource="+dashboardResource;
			if (StringUtils.isNotBlank(extraParams)) {
				baseUrl = baseUrl + "&=" + extraParams;
			}
		} catch (Throwable t) {
			_logger.error("error in buildIframeEndpoint", t);
			throw new RuntimeException("Error in buildIframeEndpoint", t);
		}
		return baseUrl;
	}
	
}