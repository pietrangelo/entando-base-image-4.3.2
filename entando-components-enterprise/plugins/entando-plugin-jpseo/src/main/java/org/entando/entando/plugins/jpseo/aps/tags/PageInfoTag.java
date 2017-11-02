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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.entando.entando.plugins.jpseo.aps.system.services.page.ISeoPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns the information of the specified page.
 * This tag can use the sub-tag "ParameterTag" to add url parameters 
 * if the info attribute is set to 'url'.
 * @author E.Santoboni
 */
public class PageInfoTag extends com.agiletec.aps.tags.PageInfoTag {

	private static final Logger _logger =  LoggerFactory.getLogger(PageInfoTag.class);

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		try {
			IPageManager pageManager = 
					(IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
			ISeoPage page = (ISeoPage) pageManager.getPage(this.getPageCode());
			if (null == page) {
					_logger.error("Required info for null page : inserted code '{}'", this.getPageCode());
			}
			if (this.getInfo() == null || this.getInfo().equals(CODE_INFO)) {
				this.setValue(page.getCode());
			} else if (this.getInfo().equals(TITLE_INFO)) {
				this.extractPageTitle(page, currentLang);
			} else if (this.getInfo().equals(DESCRIPTION_INFO)) {
				this.extractPageDescription(page, currentLang);
			} else if (this.getInfo().equals(URL_INFO)) {
				this.extractPageUrl(page, currentLang, reqCtx);
			} else if (this.getInfo().equals(OWNER_INFO)) {
				this.extractPageOwner(page, reqCtx);
			} else if (this.getInfo().equals(CHILD_OF_INFO)) {
				this.extractIsChildOfTarget(page);
			} else if (this.getInfo().equals(HAS_CHILD)) {
				boolean hasChild = (page.getChildren() != null && page.getChildren().length > 0);
				this.setValue(new Boolean(hasChild).toString());
			}
			this.evalValue();
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error during tag initialization ", t);
		}
		this.release();
		return EVAL_PAGE;
	}
	
	protected void extractPageDescription(ISeoPage page, Lang currentLang) {
		ApsProperties descriptions = page.getDescriptions();
		String value = null;
		if ((this.getLangCode() == null) || (this.getLangCode().equals(""))
						|| (currentLang.getCode().equalsIgnoreCase(this.getLangCode()))) {
			value = descriptions.getProperty(currentLang.getCode());
		} else {
			value = descriptions.getProperty(this.getLangCode());
		}
		if (value == null || value.trim().equals("")) {
			ILangManager langManager = 
					(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			value = descriptions.getProperty(langManager.getDefaultLang().getCode());
		}
		this.setValue(value);
	}
	
	public static final String DESCRIPTION_INFO = "description";
	
}