/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpaltofw.aps.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import static javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE;
import static javax.servlet.jsp.tagext.Tag.EVAL_PAGE;
import org.apache.commons.lang3.StringUtils;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import static org.apache.taglibs.standard.tag.common.core.OutSupport.out;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;
import org.entando.entando.plugins.jpaltofw.aps.system.service.client.GokibiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class GokibiWidgetTag extends OutSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(GokibiWidgetTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			ApsProperties config = widget.getConfig();
			if (null != config) {
				String baseUrl = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL);
				String pid = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_PID);
				String widgetCode = config.getProperty(AltoSystemConstants.WIDGET_PARAM_WIDGET_CODE);
				if (null != baseUrl && null != pid && null != widgetCode) {
					String placeholder = new GokibiClient().getPlaceholder(baseUrl, pid, widgetCode);
					if (StringUtils.isNotEmpty(placeholder)) {
						this.setPlaceholder(placeholder);
					}
					/*
					GokibiWidget gokibiWidget = new GokibiClient().getWidget(baseUrl, pid, widgetCode);
					if (null != gokibiWidget) {
						this.setPlaceholder(gokibiWidget.getPlaceholder());
					}
					*/
				}
			}
		} catch (Throwable t) {
			_logger.error("Error during tag initialization", t);
			throw new JspException("Error during tag initialization", t);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		if (null == this.getPlaceholder()) {
			_logger.debug("Null information");
			return super.doEndTag();
		}
		if (this.getVar() != null) {
			this.pageContext.setAttribute(this.getVar(), this.getPlaceholder());
		} else {
			try {
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), this.getPlaceholder());
				} else {
					this.pageContext.getOut().print(this.getPlaceholder());
				}
			} catch (Throwable t) {
				_logger.error("Error closing tag", t);
				throw new JspException("Error closing tag", t);
			}
		}
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		super.escapeXml = true;
		this.setVar(null);
		this.setPlaceholder(null);
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	public String getPlaceholder() {
		return _placeholder;
	}
	public void setPlaceholder(String placeholder) {
		this._placeholder = placeholder;
	}
	
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _var;
	private String _placeholder;
	
}
