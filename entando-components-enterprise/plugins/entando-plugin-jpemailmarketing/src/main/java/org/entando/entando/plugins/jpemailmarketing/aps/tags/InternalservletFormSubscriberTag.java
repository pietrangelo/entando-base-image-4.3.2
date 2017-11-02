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
package org.entando.entando.plugins.jpemailmarketing.aps.tags;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.entando.entando.plugins.jpemailmarketing.apsadmin.portal.specialwidget.form.FormConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.InternalServletTag;
import com.agiletec.aps.util.ApsProperties;

public class InternalservletFormSubscriberTag extends InternalServletTag {

	private static final Logger _logger =  LoggerFactory.getLogger(InternalservletFormSubscriberTag.class);

	@Override
	protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget widget) throws ServletException, IOException {
		HttpServletRequest request = reqCtx.getRequest();
		try {
			String actionPath = this.extractIntroActionPath(reqCtx, widget);
			if (!this.isStaticAction()) {
				String requestActionPath = request.getParameter(REQUEST_PARAM_ACTIONPATH);
				String currentFrameActionPath = request.getParameter(REQUEST_PARAM_FRAMEDEST);
				Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
				if (requestActionPath != null && currentFrameActionPath != null && currentFrame.toString().equals(currentFrameActionPath)) {
					if (this.isAllowedRequestPath(requestActionPath)) {
						actionPath = requestActionPath;
					}
				}
			}
			String courseValue = this.extractWidgetParameter(FormConfigAction.CONFIG_PARAM_COURSEID, widget.getConfig(), reqCtx);
			int courseId = new Integer(courseValue);

			actionPath = actionPath + "?" + FormConfigAction.CONFIG_PARAM_COURSEID + "="+ courseId;

			RequestDispatcher requestDispatcher = request.getRequestDispatcher(actionPath);
			requestDispatcher.include(request, responseWrapper);
		} catch (Throwable t) {
			_logger.error("Error including widget ", t);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(ERROR_JSP);
			requestDispatcher.include(request, responseWrapper);
		}
	}

	protected String extractWidgetParameter(String parameterName, ApsProperties widgetConfig, RequestContext reqCtx) {
		return (String) widgetConfig.get(parameterName);
	}
	
	@Override
	public String getActionPath() {
		return "/ExtStr2" + NS + ACTION_NAME;
	}
	
	private static final String NS = "/do/FrontEnd/jpemailmarketing/Form";
	private static final String ACTION_NAME = "/intro";

	private static final String ERROR_JSP = "/WEB-INF/plugins/jpemailmarketing/aps/jsp/internalservlet/form/frontend-form-error.jsp";
}
