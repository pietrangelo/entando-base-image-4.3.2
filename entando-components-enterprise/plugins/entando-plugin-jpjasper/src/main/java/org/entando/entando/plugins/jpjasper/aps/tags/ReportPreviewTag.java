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
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.JpJasperSystemConstants;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.ReportViewerConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.InternalServletTag;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;


public class ReportPreviewTag extends InternalServletTag {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportPreviewTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		Widget showlet = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
		IJasperServerManager jasperServerManager = (IJasperServerManager) ApsWebApplicationUtils.getBean(JpJasperSystemConstants.JASPER_CLIENT_MANAGER, this.pageContext);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		String langCode = currentLang.getCode();
		ApsProperties showletConfig = showlet.getConfig();
		String detailUriString =  this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_DETAIL_URI_STRING, showletConfig, reqCtx);
		String detailTitle = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_DETAIL_TITLE + langCode, showletConfig, reqCtx);
		if (StringUtils.isBlank(detailTitle)) {
			detailTitle = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_DETAIL_TITLE + langManager.getDefaultLang().getCode(), showletConfig, reqCtx);
		}
		String detailDownloadFormatsShowletParam =  this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_DETAIL_DOWNLOAD_FORMATS, showletConfig, reqCtx);
		String detailInputControlsParams = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_DETAIL_INPUT_CONTROLS, showletConfig, reqCtx);
		this.pageContext.setAttribute(this.getVarUriString(), detailUriString);
		if (!StringUtils.isBlank(detailUriString)) {
			this.pageContext.setAttribute(this.getVarShowletDownloadFormats(), detailDownloadFormatsShowletParam);
			this.pageContext.setAttribute(this.getVarShowletTitle(), detailTitle);
			this.pageContext.setAttribute(this.getVarShowletInputControlValues(), detailInputControlsParams);
		}
		String viewPage = this.getDetailViewPage();
		if (StringUtils.isBlank(viewPage)) {
			viewPage = jasperServerManager.getJasperConfig().getReportViewerPageCode();
		}
		this.pageContext.setAttribute(this.getDetailPageCodeVar(), viewPage);
		return super.doEndTag();
	}

	@Override
	protected void includeWidget(RequestContext reqCtx, ResponseWrapper responseWrapper, Widget showlet) throws ServletException, IOException {
		String actionPath = "/ExtStr2/do/jpjasper/FrontEnd/Report/renderReportPreview.action";
		String requestActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_ACTIONPATH);
		String currentFrameActionPath = reqCtx.getRequest().getParameter(REQUEST_PARAM_FRAMEDEST);
		Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		if (requestActionPath != null && currentFrameActionPath != null && currentFrame.toString().equals(currentFrameActionPath)) {
			if (this.isAllowedRequestPath(requestActionPath)) {
				actionPath = requestActionPath;
			}
		}
		ApsProperties showletConfig = showlet.getConfig();
		String uriString = null;
		if (null != showletConfig) {
			uriString = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_URI_STRING, showletConfig, reqCtx);
		}
		actionPath = actionPath + "?actionUriString=" + uriString;
		String inputControlsParams = this.extractShowletParameter(ReportViewerConfigAction.WIDGET_PARAM_INPUT_CONTROLS, showletConfig, reqCtx);
		if (!StringUtils.isBlank(inputControlsParams)) {
			actionPath = actionPath + "&showletInputControlValues=" + URIUtil.encodeAll(inputControlsParams);
		}
		String page = ((Page) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE)).getCode();
		Integer frame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
		String key = page + "_" + frame + "_reportResult";
		this.pageContext.setAttribute("execReportVar", key);
		actionPath = actionPath + "&reportKey=" + key;
		_logger.debug("ReportPreviewTag - actionPath: {}", actionPath);
		RequestDispatcher requestDispatcher = reqCtx.getRequest().getRequestDispatcher(actionPath);
		requestDispatcher.include(reqCtx.getRequest(), responseWrapper);
	}

	protected String createDownloadFormatParam(String paramName, String downloadFormatsShowletParam) {
		StringBuilder sbBuffer = new StringBuilder();
		if (!StringUtils.isBlank(downloadFormatsShowletParam)) {
			String[] values = downloadFormatsShowletParam.split(",");
			for (int i = 0; i < values.length; i++) {
				String format = values[i];
				if (!StringUtils.isBlank(format.trim())) {
					sbBuffer.append("&").append(paramName).append("=").append(format);
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


	//	public String getVar() {
	//		return _var;
	//	}
	//	public void setVar(String var) {
	//		this._var = var;
	//	}

//	public String getPagecode() {
//		return _pagecode;
//	}
//	public void setPagecode(String pagecode) {
//		this._pagecode = pagecode;
//	}

	public String getVarUriString() {
		return _varUriString;
	}
	public void setVarUriString(String varUriString) {
		this._varUriString = varUriString;
	}

	public String getVarShowletInputControlValues() {
		return _varShowletInputControlValues;
	}
	public void setVarShowletInputControlValues(String varShowletInputControlValues) {
		this._varShowletInputControlValues = varShowletInputControlValues;
	}

	public String getVarShowletDownloadFormats() {
		return _varShowletDownloadFormats;
	}
	public void setVarShowletDownloadFormats(String varShowletDownloadFormats) {
		this._varShowletDownloadFormats = varShowletDownloadFormats;
	}

	public String getVarShowletTitle() {
		return _varShowletTitle;
	}
	public void setVarShowletTitle(String varShowletTitle) {
		this._varShowletTitle = varShowletTitle;
	}

	//private String _var;

	public String getDetailPageCodeVar() {
		return _detailPageCodeVar;
	}
	public void setDetailPageCodeVar(String detailPageCodeVar) {
		this._detailPageCodeVar = detailPageCodeVar;
	}

	public String getDetailViewPage() {
		return _detailViewPage;
	}
	public void setDetailViewPage(String detailViewPage) {
		this._detailViewPage = detailViewPage;
	}

	//private String _pagecode;
	private String _varUriString = "uriString";
	private String _varShowletInputControlValues="showletInputControlValues";
	private String _varShowletDownloadFormats = "showletDownloadFormats";
	private String _varShowletTitle = "showletTitle";

	private String _detailPageCodeVar = "pageCodeVar";
	private String _detailViewPage;

}