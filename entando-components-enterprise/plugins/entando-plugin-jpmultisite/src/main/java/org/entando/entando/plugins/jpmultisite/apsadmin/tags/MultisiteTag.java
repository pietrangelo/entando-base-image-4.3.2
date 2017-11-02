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
package org.entando.entando.plugins.jpmultisite.apsadmin.tags;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteTag.class);

	@Override
	public int doEndTag() throws JspException {
		IMultisiteManager multisiteManager = (IMultisiteManager) ApsWebApplicationUtils.getBean(JpmultisiteSystemConstants.MULTISITE_MANAGER, this.pageContext);
		IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
		try {
			Multisite multisite = multisiteManager.loadMultisite(this.getCode());
			if(null != multisite){
				if("home".equals(this.getKey())) {
					IPage page = pageManager.getPage("home"+JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR+multisite.getCode());
					if(null != page) {
						printOrSetAttribute(page.getCode());
					}
				} else if("title".equals(this.getKey()) && StringUtils.isNotBlank(this.getLang())) {
					printOrSetAttribute(multisite.getTitles().get(this.getLang()));
				} else if("descr".equals(this.getKey()) && StringUtils.isNotBlank(this.getLang())) {
					printOrSetAttribute(multisite.getDescriptions().get(this.getLang()));
				} else if("img".equals(this.getKey())) {
					printOrSetAttribute(multisite.getHeaderImage());
				} else if("css".equals(this.getKey())) {
					printOrSetAttribute(multisite.getTemplateCss());
				} else {
					printOrSetAttribute(multisite);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Errore tag", t);
		}
		this.release();
		return super.doEndTag();
	}

	@Override
	public void release() {
		this.setVar(null);
		this.setCode(null);
	}
	
	private void printOrSetAttribute(Object obj) throws Exception {
		if(null != this.getVar()){
			this.pageContext.setAttribute(this.getVar(), obj);
		} else {
			this.pageContext.getOut().print(obj);
		}
	}

	public String getVar() {
		return _var;
	}

	public void setVar(String var) {
		this._var = var;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		this._code = code;
	}

	public String getKey() {
		return _key;
	}

	public void setKey(String key) {
		this._key = key;
	}

	public String getLang() {
		return _lang;
	}

	public void setLang(String lang) {
		this._lang = lang;
	}
	
	private String _var;
	private String _code;
	private String _key;
	private String _lang;

}
