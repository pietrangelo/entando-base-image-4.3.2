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

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.page.ISeoPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author E.Santoboni
 */
public class CurrentPageTag extends com.agiletec.aps.tags.CurrentPageTag {

	private static final Logger _logger =  LoggerFactory.getLogger(CurrentPageTag.class);

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		try {
			ISeoPage page = (ISeoPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			if (this.getParam() == null || this.getParam().equals(TITLE_INFO)) {
				this.extractPageTitle(page, currentLang, reqCtx);
			} else if (this.getParam().equals(DESCRIPTION_INFO)) {
				this.extractPageDescription(page, currentLang, reqCtx);
			} else if (this.getParam().equals(CODE_INFO)) {
				this.setValue(page.getCode());
			} else if (this.getParam().equals(OWNER_INFO)) {
				this.extractPageOwner(page, reqCtx);
			} else if (this.getInfo().equals(CHILD_OF_INFO)) {
				this.extractIsChildOfTarget(page);
			} else if (this.getInfo().equals(HAS_CHILD)) {
				boolean hasChild = (page.getChildren() != null && page.getChildren().length > 0);
				this.setValue(Boolean.valueOf(hasChild).toString());
			}
			if (null == this.getValue()) this.setValue("");
			this.evalValue();
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error during tag initialization ", t);
		}
		return EVAL_PAGE;
	}
	
	protected void extractPageDescription(ISeoPage page, Lang currentLang, RequestContext reqCtx) {
		if (!page.isUseExtraDescriptions()) {
			this.extractPageDescription(page, currentLang);
			return;
		}
		Object extraDescriptionObject = reqCtx.getExtraParam(JpseoSystemConstants.EXTRAPAR_EXTRA_PAGE_DESCRIPTIONS);
		if (null == extraDescriptionObject) {
			this.extractPageDescription(page, currentLang);
			return;
		}
		if (extraDescriptionObject instanceof String) {
			String extraDescriptionString = (String) extraDescriptionObject;
			if (extraDescriptionString.trim().length() > 0) {
				this.setValue(extraDescriptionString);
			} else {
				this.extractPageDescription(page, currentLang);
			}
		} else if (extraDescriptionObject instanceof Map<?, ?>) {
			this.extractPageDescriptionFromMap(page, currentLang, (Map) extraDescriptionObject);
			if (null == this.getValue()) {
				this.extractPageDescription(page, currentLang);
			}
		} else {
			this.extractPageDescription(page, currentLang);
		}
	}
	
	//method equals than tag PageInfoTag
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
	
	private void extractPageDescriptionFromMap(ISeoPage page, Lang currentLang, Map extraDescriptionsMap) {
		Object value = null;
		if ((this.getLangCode() == null) || (this.getLangCode().equals(""))
						|| (currentLang.getCode().equalsIgnoreCase(this.getLangCode()))) {
			value = extraDescriptionsMap.get(currentLang.getCode());
		} else {
			value = extraDescriptionsMap.get(this.getLangCode());
		}
		if (value == null || value.toString().trim().equals("")) {
			ILangManager langManager = 
					(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			value = extraDescriptionsMap.get(langManager.getDefaultLang().getCode());
		}
		if (null != value && value.toString().trim().length() > 0) {
			this.setValue(value.toString());
		}
	}
	
	public static final String DESCRIPTION_INFO = "description";
	
}