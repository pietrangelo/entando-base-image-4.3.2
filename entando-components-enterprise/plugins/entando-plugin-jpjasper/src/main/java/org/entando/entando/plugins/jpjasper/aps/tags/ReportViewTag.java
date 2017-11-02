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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.ReportViewerConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.InternalServletTag;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class ReportViewTag extends InternalServletTag {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportViewTag.class);
	
	@Override
	protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget showlet) throws ServletException, IOException {
		//System.out.println("REPORT_VIEW_TAG");
		String actionPath = "/ExtStr2/do/jpjasper/FrontEnd/Report/renderReport.action";
		String requestActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_ACTIONPATH);
		String currentFrameActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_FRAMEDEST);
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		if (requestActionPath != null && currentFrameActionPath != null && currentFrame.toString().equals(currentFrameActionPath)) {
			if (this.isAllowedRequestPath(requestActionPath)) {
				actionPath = requestActionPath;
			}
		}
		/*
		 * Ordine estrazione uriString:
		 * 1) showlet
		 * 2) tag
		 * 3) parametro request
		 */
		ApsProperties showletConfig = showlet.getConfig();
		String uriString = null;
		if (null != showletConfig) {
			uriString = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_URI_STRING, showletConfig, reqCtx);
		}
		if (StringUtils.isBlank(uriString)) {
			uriString = this.getUriString();
		}
		if (StringUtils.isBlank(uriString)) {
			uriString = pageContext.getRequest().getParameter("uriString");
		}
		
		actionPath = actionPath + "?actionUriString=" + uriString;
		
		String downloadFormatsShowletParam =  this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_DOWNLOAD_FORMATS, showletConfig, reqCtx);
		if (StringUtils.isBlank(downloadFormatsShowletParam)) {
			//downloadFormats in preview
			downloadFormatsShowletParam = pageContext.getRequest().getParameter("downloadFormats");
		}
		if (!StringUtils.isBlank(downloadFormatsShowletParam)) {
			String downloadFormatActionParam = this.createDownloadFormatParam(downloadFormatsShowletParam);
			if (!StringUtils.isBlank(downloadFormatActionParam)) {
				actionPath = actionPath + downloadFormatActionParam;
			}
		}
		
		
		String inputControlsParams = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_INPUT_CONTROLS, showletConfig, reqCtx);
		if (StringUtils.isBlank(inputControlsParams)) {
			//inputControlValues in preview
			inputControlsParams = pageContext.getRequest().getParameter("inputControlValues");
		}
		if (!StringUtils.isBlank(inputControlsParams)) {
			actionPath = actionPath + "&showletInputControlValues=" + URIUtil.encodeAll(inputControlsParams);
		}

		ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
		
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		String langCode = currentLang.getCode();
		String titleParam = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_TITLE + langCode, showletConfig, reqCtx);
		if (StringUtils.isBlank(titleParam)) {
			titleParam = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_TITLE + langManager.getDefaultLang().getCode(), showletConfig, reqCtx);
		}
		//
		if (StringUtils.isBlank(titleParam)) {
			//titleParam in preview
			titleParam = pageContext.getRequest().getParameter("titleParam");
		}
		if (!StringUtils.isBlank(titleParam)) {
			actionPath = actionPath + "&showletTitleParam=" + titleParam;
		}


		_logger.debug("ReportViewTag - actionPath: {}", actionPath);

		RequestDispatcher requestDispatcher = reqCtx.getRequest().getRequestDispatcher(actionPath);
		requestDispatcher.include(reqCtx.getRequest(), responseWrapper);
	}


	protected String createDownloadFormatParam(String downloadFormatsShowletParam) {
		StringBuffer sbBuffer = new StringBuffer();		
		if (!StringUtils.isBlank(downloadFormatsShowletParam)) {
			String[] values = downloadFormatsShowletParam.split(",");
			for (int i = 0; i < values.length; i++) {
				String format = values[i];
				if (!StringUtils.isBlank(format.trim())) {					
					sbBuffer.append("&showletDownloadFormats=").append(format);
				}
			}
		}
		return sbBuffer.toString();
	}


	protected String extractShowletParameter(String parameterName, ApsProperties showletConfig, RequestContext reqCtx) {
		String  value = null;
		if (null != showletConfig && showletConfig.containsKey(parameterName)) {
			value = (String) showletConfig.get(parameterName);
		}
		return value;
	}

	public String getUriString() {
		return _uriString;
	}
	public void setUriString(String uriString) {
		this._uriString = uriString;
	}

	private String _uriString;
	
}
