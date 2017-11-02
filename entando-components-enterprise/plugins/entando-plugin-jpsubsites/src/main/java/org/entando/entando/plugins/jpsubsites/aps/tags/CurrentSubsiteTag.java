/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.tags;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.plugins.jpsubsites.aps.system.JpsubsitesSystemConstants;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;

public class CurrentSubsiteTag extends TagSupport {
    
	@Override
    public int doStartTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		Subsite subsite = (Subsite) request.getAttribute(JpsubsitesSystemConstants.REQUEST_PARAM_CURRENT_SUBSITE);
		if (subsite != null) {
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			try {
				String param = this.getParam();
				if (param.equals("title")) {
					this._value = this.extractLangParam(subsite.getTitles(), reqCtx);
				} else if (param.equals("descr")) {
					this._value = this.extractLangParam(subsite.getDescriptions(), reqCtx);
				} else if (param.equals("css")) {
					this._value = subsite.getCssFileName();
				} else if (param.equals("img")) {
					this._value = subsite.getImageFileName();
				} else if (param.equals("category")) {
					this._value = subsite.getCategoryCode();
				} else if (param.equals("contentViewer")) {
					this._value = subsite.getCategoryCode();
				} else if (param.equals("home")) {
					this._value = subsite.getHomepage();
				} else if (param.equals("code")) {
					_value = subsite.getCode();
				} else {
					this._value = this.extractLangParam(subsite.getTitles(), reqCtx);
				}
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "doStartTag");
				throw new JspException("Errore inizializzazione tag", t);
			}
		}
		return EVAL_BODY_INCLUDE;
	}
    
    private String extractLangParam(ApsProperties properties, RequestContext reqCtx) {
    	String value = null;
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		if (properties!=null) {
			if ((this._langCode == null) || (this._langCode.equals(""))
					|| (currentLang.getCode().equalsIgnoreCase(this._langCode))) {
				// restituisci il parametro nella lingua corrente
				value = properties.getProperty(currentLang.getCode());
			} else {
				value = properties.getProperty(this._langCode);
			}
			if (value == null || value.trim().equals("")) {
				ILangManager langManager = 
					(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
				value = properties.getProperty(langManager.getDefaultLang().getCode());
			}
		}
		return value;
    }
    
    @Override
	public int doEndTag() throws JspException {
		if (this._value != null) {
			if (_varName != null) {
				this.pageContext.setAttribute(_varName, _value);
			} else {
				try {
					this.pageContext.getOut().print(_value);
				} catch (IOException e) {
					throw new JspException("Errore in doEndTag", e);
				}
			}
		}
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		this._langCode = null;
		this._varName = null;
		this._value = null;
	}
	
	/**
	 * Restituisce il codice della lingua nel quale 
	 * si richiede il titolo della pagina.
	 * @return Il codice lingua.
	 */
	public String getLangCode() {
		return _langCode;
	}
	
	/**
	 * Imposta il codice della lingua nel quale 
	 * si richiede il titolo della pagina.
	 * @param langCode Il codice lingua.
	 */
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	/**
	 * Imposta il nome della variabile mediante il quale 
	 * inserire il valore del parametro di pagina richiesto nel pageContext.
	 * @param var Il nome della variabile.
	 */
	public void setVar(String var) {
		this._varName = var;
	}
	
	/**
	 * Restituisce il nome della variabile mediante il quale 
	 * inserire il valore del parametro di pagina richiesto nel pageContext.
	 * @return Il nome della variabile.
	 */
	protected String getVar() {
		return _varName;
	}
	
	protected String getParam() {
		return _param;
	}
	public void setParam(String param) {
		this._param = param;
	}
	
    private String _varName;
	private String _langCode;
	private String _value;
	
	private String _param;
	
}