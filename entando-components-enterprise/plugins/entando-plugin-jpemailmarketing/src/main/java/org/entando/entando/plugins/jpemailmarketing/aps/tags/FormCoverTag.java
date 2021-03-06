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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpemailmarketing.apsadmin.portal.specialwidget.form.FormConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class FormCoverTag  extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(FormCoverTag.class);

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		//TODO ADD CONSTANTS CLASS
		IFormManager formManager = (IFormManager) ApsWebApplicationUtils.getBean("jpemailmarketingFormManager", this.pageContext);
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		try {
		String basePath = "/do/FrontEnd/jpemailmarketing/Form/viewFile.action?type=cover&courseId=";

		Form form = null;
			if (null != this.getKey()) {
				form = formManager.getForm(this.getKey());
			} else {
				Widget widget = this.extractWidget(reqCtx);
				ApsProperties widgetConfig = widget.getConfig();
				String courseIdParam = this.extractWidgetParameter(FormConfigAction.CONFIG_PARAM_COURSEID, widgetConfig, reqCtx);
				if (StringUtils.isNotBlank(courseIdParam)) {
					form = formManager.getForm(new Integer(courseIdParam));
				}
			}

			if (null != form && null != form.getFileCoverName()) {
				String path = basePath + form.getCourseId();
				this.pageContext.setAttribute(this.getVar(), path);
			}
			
		} catch (Throwable t) {
			_logger.error("Error in doStartTag", t);
			throw new JspException("Error in FormCoverTag doStartTag", t);
		}
		return super.doStartTag();
	}

	 
	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}
	
	@Override
	public void release() {
		this.setVar(null);
		this.setKey(null);
	}

	private Widget extractWidget(RequestContext reqCtx) {
		Widget widget = null;
		widget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		return widget;
	}

	protected String extractWidgetParameter(String parameterName, ApsProperties widgetConfig, RequestContext reqCtx) {
		return (String) widgetConfig.get(parameterName);
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	public Integer getKey() {
		return _key;
	}
	public void setKey(Integer key) {
		this._key = key;
	}

	private String _var;
	private Integer _key;
	
}
