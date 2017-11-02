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
package org.entando.entando.plugins.jpsocial.aps.tags;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.services.IBitLyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.rosaloves.bitlyj.Url;

/**
 * @author S.Loru
 */
public class SocialContentPermalink extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(SocialContentPermalink.class);

	@Override
	public int doStartTag() throws JspException {
		try {
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			this.setBitLyManager((IBitLyManager) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.BITLY_MANAGER, request));
			this.setLangManager((ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, request));
			this.setPageManager((IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, request));
			this.setUrlManager((IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, request));
			this.setContentManager((IContentManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_MANAGER, request));
			String link = getContentPermalink();
			if (null != getVar() && !"".equals(getVar().trim())) {
				this.pageContext.setAttribute(getVar(), link);
			} else {
				this.pageContext.setAttribute(VAR_SOCIALPERMALINK, link);
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
		}
		return EVAL_PAGE;
	}

	private String getContentPermalink() throws ApsSystemException {
		Content content = this.getContentManager().loadContent(this.getContentId(), true);
		Lang lang = this.getLangManager().getDefaultLang();
		Map<String, String> map = new HashMap<String, String>();
		String link = "";
		try {
			if (null != content) {
				String pageName = content.getViewPage();
				IPage page = this.getPageManager().getPage(pageName);
				map.put("contentId", content.getId());
				link = this.getUrlManager().createURL(page, lang, map);
				link = this.getShortUrl(link);
			}
		} catch (Throwable t) {
			_logger.error("error in getContentPermalink", t);
		}
		return link;
	}

	private String getShortUrl(String urlToBeShortened) {
		try {

			Url shortedUrl = this.getBitLyManager().shortUrl(urlToBeShortened);
			return shortedUrl.getShortUrl();
		} catch (Throwable t) {
			_logger.error("error shortening the URL {}", urlToBeShortened, t);
			return urlToBeShortened;
		}
	}

	public IBitLyManager getBitLyManager() {
		return _bitLyManager;
	}

	public void setBitLyManager(IBitLyManager bitLyManager) {
		this._bitLyManager = bitLyManager;
	}

	public ILangManager getLangManager() {
		return _langManager;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public IURLManager getUrlManager() {
		return _urlManager;
	}

	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}

	public String getVar() {
		return _var;
	}

	public void setVar(String var) {
		this._var = var;
	}

	public IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	public String getContentId() {
		return _contentId;
	}

	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	private IBitLyManager _bitLyManager;
	private ILangManager _langManager;
	private IPageManager _pageManager;
	private IURLManager _urlManager;
	private IContentManager _contentManager;
	private String _var;
	private String _contentId;
	private static final String VAR_SOCIALPERMALINK = "contentPermalink";
}
