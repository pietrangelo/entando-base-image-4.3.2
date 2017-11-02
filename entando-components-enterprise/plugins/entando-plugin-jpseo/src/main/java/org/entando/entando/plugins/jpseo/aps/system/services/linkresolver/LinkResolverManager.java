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
package org.entando.entando.plugins.jpseo.aps.system.services.linkresolver;

import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.system.services.url.PageURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * @author E.Santoboni
 */
public class LinkResolverManager extends com.agiletec.plugins.jacms.aps.system.services.linkresolver.LinkResolverManager {

	private static final Logger _logger =  LoggerFactory.getLogger(LinkResolverManager.class);
	/*
	@Override
	protected String convertToURL(String symbolicString, RequestContext reqCtx) {
		String url = null;
		SymbolicLink symbolicLink = new SymbolicLink();
		if (symbolicLink.setSymbolicDestination(symbolicString)) {
			url = this.resolveLink(symbolicLink, reqCtx);
		}
		return url;
	}
	*/
	@Override
	public String resolveLink(SymbolicLink symbolicLink, String contentId, RequestContext reqCtx) {
		if (null == symbolicLink) {
			_logger.error("Null Symbolic Link");
			return "";
		}
		String url = null;
		try {
			if (symbolicLink.getDestType() == SymbolicLink.URL_TYPE) {
				url = symbolicLink.getUrlDest();
			} else if (symbolicLink.getDestType() == SymbolicLink.PAGE_TYPE) {
				PageURL pageUrl = (PageURL) this.getUrlManager().createURL(reqCtx);
				pageUrl.setPageCode(symbolicLink.getPageDest());
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_ON_PAGE_TYPE) {
				PageURL pageUrl = (PageURL) this.getUrlManager().createURL(reqCtx);
				pageUrl.setPageCode(symbolicLink.getPageDest());
				pageUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, symbolicLink.getContentDest());
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_TYPE) {
				PageURL pageUrl = (PageURL) this.getUrlManager().createURL(reqCtx);
				String contentIdDest = symbolicLink.getContentDest();
				String pageCode = this.getContentPageMapperManager().getPageCode(contentIdDest);
				boolean forwardToDefaultPage = !this.isPageAllowed(reqCtx, pageCode);// TODO Verificare perché è al contrario
				if (forwardToDefaultPage) {
					String viewPageCode = this.getContentManager().getViewPage(contentIdDest);	
					pageUrl.setPageCode(viewPageCode);
					String langCode = null;
					if (null != reqCtx) {
						Lang lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
						if (null != lang) {
							langCode = lang.getCode();
						}
					}
					String friendlyCode = this.getSeoMappingManager().getContentReference(contentIdDest, langCode);
					if (null != friendlyCode) {
						pageUrl.setFriendlyCode(friendlyCode);
					}
					pageUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, contentIdDest);
				} else {
					pageUrl.setPageCode(pageCode);
				}
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.RESOURCE_TYPE) {
				ResourceInterface resource = this.getResourceManager().loadResource(symbolicLink.getResourceDest());
				if (null != resource) {
					url = resource.getDefaultUrlPath();
					if (!resource.getMainGroup().equals(Group.FREE_GROUP_NAME)) {
						if (!url.endsWith("/")) {
							url += "/";
						}
						url += AbstractResourceAttribute.REFERENCED_RESOURCE_INDICATOR + "/" + contentId + "/";
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error resolve link from SymbolicLink", t);
			throw new RuntimeException("Error resolve link from SymbolicLink", t);
		}
		return url;
	}
	
	protected ISeoMappingManager getSeoMappingManager() {
		return _seoMappingManager;
	}
	public void setSeoMappingManager(ISeoMappingManager seoMappingManager) {
		this._seoMappingManager = seoMappingManager;
	}
	
	private ISeoMappingManager _seoMappingManager;
	
}