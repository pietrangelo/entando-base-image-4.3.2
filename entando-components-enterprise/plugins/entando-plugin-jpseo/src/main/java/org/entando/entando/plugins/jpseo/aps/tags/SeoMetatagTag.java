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
package org.entando.entando.plugins.jpseo.aps.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.entando.entando.plugins.jpseo.aps.system.services.page.ISeoPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class SeoMetatagTag extends OutSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoMetatagTag.class);

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		try {
			ISeoPage page = (ISeoPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			Map<String, Object> complexParameters = page.getComplexParameters();
			if (null != complexParameters) {
				Object mapvalue = complexParameters.get(this.getKey());
				if (null != mapvalue) {
					if (mapvalue instanceof ApsProperties) {
						ApsProperties properties = (ApsProperties) mapvalue;
						String value = (String) properties.getProperty(currentLang.getCode());
						if (null == value || value.trim().length() == 0) {
							ILangManager langManager = 
									(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
							Lang defaultLang = langManager.getDefaultLang();
							value = (String) properties.getProperty(defaultLang.getCode());
						}
						this.setValue(value);
					} else {
						this.setValue(mapvalue.toString());
					}
				}
			}
			if (null != this.getValue()) {
				this.evalValue();
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error during tag initialization ", t);
		}
		this.release();
		return EVAL_PAGE;
	}
	
	protected void evalValue() throws JspException {
		if (this.getVar() != null) {
			this.pageContext.setAttribute(this.getVar(), this.getValue());
		} else {
			try {
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), this.getValue());
				} else {
					this.pageContext.getOut().print(this.getValue());
				}
			} catch (IOException e) {
				_logger.error("error in doEndTag", e);
				throw new JspException("Error closing tag ", e);
			}
		}
	}
	
	@Override
	public void release() {
		this._key = null;
		this._var = null;
		this._value = null;
		super.escapeXml = true;
	}
	
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	
	public void setVar(String var) {
		this._var = var;
	}
	protected String getVar() {
		return _var;
	}
	
	public String getValue() {
		return _value;
	}
	public void setValue(String value) {
		this._value = value;
	}
	
	/**
	 * Returns True if the system escape the special characters. 
	 * @return True if the system escape the special characters.
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Set if the system has to escape the special characters. 
	 * @param escapeXml True if the system has to escape the special characters, else false.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _key;
	
	private String _var;
	private String _value;
	
}
