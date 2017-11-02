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

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.JpJasperSystemConstants;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.ReportListViewerConfigAction;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class ReportListTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportListTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		String titleParam = null;
		IJasperServerManager jasperServerManager = (IJasperServerManager) ApsWebApplicationUtils.getBean(JpJasperSystemConstants.JASPER_CLIENT_MANAGER, this.pageContext);
		try {
			//ConfigInterface configService = (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, this.pageContext);
			ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader((HttpServletRequest) pageContext.getRequest(), jasperServerManager);

			if (!StringUtils.isBlank(cookieHeader)) {
				Widget showlet = this.extractShowlet(reqCtx);
				ApsProperties showletConfig = showlet.getConfig();
				
				String folderParam = this.extractShowletParameter(ReportListViewerConfigAction.SHOWLET_PARAM_FOLDER, showletConfig, reqCtx);
				String filterStringParam = this.extractShowletParameter(ReportListViewerConfigAction.SHOWLET_PARAM_FILTER_STRING, showletConfig, reqCtx);
				String recursiveSearch = this.extractShowletParameter(ReportListViewerConfigAction.SHOWLET_PARAM_RECURSIVE_SEARCH, showletConfig, reqCtx);
				
				Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
				String langCode = currentLang.getCode();
				titleParam = this.extractShowletParameter(ReportListViewerConfigAction.SHOWLET_PARAM_TITLE + langCode, showletConfig, reqCtx);
				if (StringUtils.isBlank(titleParam)) {
					titleParam = this.extractShowletParameter(ReportListViewerConfigAction.SHOWLET_PARAM_TITLE + langManager.getDefaultLang().getCode(), showletConfig, reqCtx);
				}
				
				String folder = null;
				if (!StringUtils.isBlank(folderParam)) {
					folder = folderParam;
				}
				String filterString = null;
				if (!StringUtils.isBlank(filterStringParam)) {
					filterString = filterStringParam;
				}
				
				boolean recursive = null != recursiveSearch && recursiveSearch.equalsIgnoreCase("true");
				List<JasperResourceDescriptor> list = jasperServerManager.searchRestResources(cookieHeader, filterString, WsTypeParams.REPORT_UNIT, folder, recursive);
				
				
				if (StringUtils.isBlank(this.getVar())) {
					this.pageContext.getOut().print(list);
				} else {
					this.pageContext.setAttribute(this.getVar(), list);
				}
			} else {
				//TODO ERROR.......
				this.pageContext.setAttribute(this.getVar(), null);
			}
			
			
			if (StringUtils.isNotBlank(titleParam)) {
				this.pageContext.setAttribute(this.getTitleVar(), titleParam);
			}
			
			//
			String viewPage = this.getViewPage();
			if (StringUtils.isBlank(viewPage)) {
				viewPage = jasperServerManager.getJasperConfig().getReportViewerPageCode();
			}
			if (StringUtils.isNotBlank(this.getPageCodeVar())) {
				this.pageContext.setAttribute(this.getPageCodeVar(), viewPage);				
			}

		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Error in ReportListTag", t);
		}
		this.release();
		return super.doEndTag();
	}

	private Widget extractShowlet(RequestContext reqCtx) {
		Widget showlet = null;
		showlet = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		return showlet;
	}

	protected String extractShowletParameter(String parameterName, ApsProperties showletConfig, RequestContext reqCtx) {
		return (String) showletConfig.get(parameterName);
	}

	@Override
	public void release() {
		super.release();
		this.setVar(null);
		this.setTitleVar(null);
		this.setPageCodeVar(null);
	}

	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public String getTitleVar() {
		return _titleVar;
	}
	public void setTitleVar(String titleVar) {
		this._titleVar = titleVar;
	}

	public String getPageCodeVar() {
		return _pageCodeVar;
	}
	public void setPageCodeVar(String pageCodeVar) {
		this._pageCodeVar = pageCodeVar;
	}

	public String getViewPage() {
		return _viewPage;
	}
	public void setViewPage(String viewPage) {
		this._viewPage = viewPage;
	}





	private String _var;
	private String _titleVar = "title";
	private String _pageCodeVar = "pageCodeVar";
	private String _viewPage;
	
}
