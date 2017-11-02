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
package org.entando.entando.plugins.jpsubsites.aps.system.services.linkresolver;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.LinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import org.apache.commons.lang3.StringUtils;

import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.ISubsiteManager;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servizio gestore della risoluzione dei link sinbolici.
 * Scopo di questa classe è l'individuazione in un testo di stringhe rappresentanti
 * link simbolici, e la loro traduzione e sostituzione nel testo con i 
 * corrispondenti URL.
 * @author 
 */
public class SubsiteLinkResolverManager extends LinkResolverManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(SubsiteLinkResolverManager.class);
	
	private ISubsiteManager _subsiteManager;
	
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
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				pageUrl.setPageCode(symbolicLink.getPageDest());
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_ON_PAGE_TYPE) {
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				pageUrl.setPageCode(symbolicLink.getPageDest());
				pageUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, symbolicLink.getContentDest());
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_TYPE) {
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				String contentIdDest = symbolicLink.getContentDest();
				String pageCode = this.getContentPageMapperManager().getPageCode(contentIdDest);
				boolean forwardToDefaultPage = !this.isPageAllowed(reqCtx, pageCode);
				if (forwardToDefaultPage) {
					String viewPageCode = this.getSubsiteViewerPage(contentId, pageUrl, reqCtx);
					if (viewPageCode == null) {
						viewPageCode = this.getContentManager().getViewPage(contentId);	
					}
					pageUrl.setPageCode(viewPageCode);
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
	
	private String getSubsiteViewerPage(String contentId, PageURL pageUrl, RequestContext reqCtx) {
		String viewPageCode = null;
		try {
			ISubsiteManager subsiteManager = this.getSubsiteManager();
			String subsiteId = subsiteManager.getSubsiteCodeForContent(contentId);
			if (null != subsiteId) {
				Subsite subsite = subsiteManager.getSubsite(subsiteId);
				String subsiteViewerPage = this.getSubsiteViewerPage(contentId, subsite, reqCtx);
				if (subsiteViewerPage != null) {
					viewPageCode = subsiteViewerPage;
					ContentRecordVO contentRecord = this.getContentManager().loadContentVO(contentId);
					if (contentRecord != null) {
						String typeCode = contentRecord.getTypeCode();
						Long modelId = subsiteManager.getModelForContentType(typeCode);
						if (modelId!=null) {
							pageUrl.addParam("modelId", modelId.toString());
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error searching subsite viewer", t);
			throw new RuntimeException("Error searching subsite viewer", t);
		}
		return viewPageCode;
	}
	
	private String getSubsiteViewerPage(String contentId, Subsite subsite, RequestContext reqCtx) {
		String viewPageCode = null;
		String contentTypeCode = contentId.substring(0, 3);
		String pageCodeForType = (null != subsite.getViewerPages()) ? subsite.getViewerPages().getProperty(contentTypeCode) : null;
		if (StringUtils.isNotEmpty(pageCodeForType) && this.isPageAllowed(reqCtx, pageCodeForType)) {
			viewPageCode = pageCodeForType;
		} else {
			String subsiteViewerPage = subsite.getContentViewerPage();
			if (StringUtils.isNotEmpty(subsiteViewerPage) && this.isPageAllowed(reqCtx, subsiteViewerPage)) {
				viewPageCode = subsiteViewerPage;
			}
		}
		return viewPageCode;
	}
	
	protected ISubsiteManager getSubsiteManager() {
		return _subsiteManager;
	}
	public void setSubsiteManager(ISubsiteManager subsiteManager) {
		this._subsiteManager = subsiteManager;
	}
	
}