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
package org.entando.entando.plugins.jpseo.aps.system.services.mapping;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.event.SeoChangedEvent;
import org.entando.entando.plugins.jpseo.aps.system.services.page.ISeoPage;
import org.entando.entando.plugins.jpseo.aps.util.FriendlyCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni, E.Mezzano
 */
public class SeoMappingManager extends AbstractService implements ISeoMappingManager, PageChangedObserver, PublicContentChangedObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoMappingManager.class);

	@Override
	public void init() throws Exception {
		this.loadMapping();
		_logger.debug("{} ready. initialized {} references",this.getClass().getName(), this.getMapping().size());
	}
	
	private void loadMapping() throws ApsSystemException {
		try {
			Map<String, FriendlyCodeVO> mapping = this.getSeoMappingDAO().loadMapping();
			Map<String, FriendlyCodeVO> pageFriendlyCodes = new HashMap<String, FriendlyCodeVO>();
			Map<String, ContentFriendlyCode> contentFriendlyCodes = new HashMap<String, ContentFriendlyCode>();
			Iterator<FriendlyCodeVO> codesIter = mapping.values().iterator();
			while (codesIter.hasNext()) {
				FriendlyCodeVO currentCode = codesIter.next();
				if (currentCode.getPageCode()!=null) {
					pageFriendlyCodes.put(currentCode.getPageCode(), currentCode);
				} else if (currentCode.getContentId()!=null) {
					String contentId = currentCode.getContentId();
					ContentFriendlyCode content = contentFriendlyCodes.get(contentId);
					if (content==null) {
						content = new ContentFriendlyCode();
						content.setContentId(contentId);
						contentFriendlyCodes.put(contentId, content);
					}
					content.addFriendlyCode(currentCode.getLangCode(), currentCode.getFriendlyCode());
				}
			}
			this.setMapping(mapping);
			this.setPageFriendlyCodes(pageFriendlyCodes);
			this.setContentFriendlyCodes(contentFriendlyCodes);
		} catch (Throwable t) {
			_logger.error("Error loading mapping", t);
			throw new ApsSystemException("Error loading mapping", t);
		}
	}
	
	@Override
	public void updateFromPageChanged(PageChangedEvent event) {
		IPage page = event.getPage();
		try {
			if (event.getSource().equals(ApsEvent.LOCAL_EVENT)) {
				if (null == this.getPageManager().getPage(page.getCode())) {
					this.deleteMapping(page.getCode(), null);
				} else {
					this.updatePageMapping(page);
				}
			} else {
				System.out.println("jpseo: ignoring EXTERNAL event");
			}
			SeoChangedEvent sevent = new SeoChangedEvent();
			event.setOperationCode(SeoChangedEvent.PAGE_CHANGED_EVENT);
			this.notifyEvent(sevent);
		} catch (Throwable t) {
			_logger.error("Error updating mapping from page changed", t);
		}
	}
	
	private void updatePageMapping(Object page) {
		if (!(page instanceof ISeoPage)) return;
		ISeoPage seoPage = (ISeoPage) page;
		String friendlyCode = seoPage.getFriendlyCode();
		if (null == friendlyCode || friendlyCode.trim().length() == 0) return;
		FriendlyCodeVO vo = new FriendlyCodeVO(seoPage.getFriendlyCode(), seoPage.getCode());
		this.updateMapping(vo);
	}
	
	@Override
	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
		if (null == event.getContent()) return;
		Content content = event.getContent();
		try {
			if (event.getSource().equals(ApsEvent.LOCAL_EVENT)) {
				if (event.getOperationCode() != PublicContentChangedEvent.REMOVE_OPERATION_CODE) {
					this.updateContentMapping(content);
				} else {
					this.deleteMapping(null, content.getId());
				}
			} else {
				System.out.println("jpseo: ignoring EXTERNAL event");
			}
			SeoChangedEvent sevent = new SeoChangedEvent();
			event.setOperationCode(SeoChangedEvent.CONTENT_CHANGED_EVENT);
			this.notifyEvent(sevent);
		} catch (Throwable t) {
			_logger.error("Error updating mapping from public content changed", t);
		}
	}
	
	private void updateContentMapping(Content content) throws ApsSystemException {
		try {
			AttributeInterface attribute = content.getAttributeByRole(JpseoSystemConstants.ATTRIBUTE_ROLE_FRIENDLY_CODE);
			if (null == attribute || !(attribute instanceof ITextAttribute)) {
				attribute = content.getAttributeByRole(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE);
			}
			if (null != attribute && attribute instanceof ITextAttribute) {
				ContentFriendlyCode contentFriendlyCode = this.prepareContentFriendlyCode(content.getId(), (ITextAttribute) attribute);
				this.getSeoMappingDAO().updateMapping(contentFriendlyCode);
				this.loadMapping();
			}
		} catch (Throwable t) {
			_logger.error("Error updating content mapping", t);
			throw new ApsSystemException("Error updating content mapping", t);
		}
	}
	
	private ContentFriendlyCode prepareContentFriendlyCode(String contentId, ITextAttribute attribute) {
		ContentFriendlyCode contentFriendlyCode = new ContentFriendlyCode();
		contentFriendlyCode.setContentId(contentId);
		ILangManager langManager = this.getLangManager();
		String defaultLang = langManager.getDefaultLang().getCode();
		if (((AttributeInterface) attribute).isMultilingual()) {
			String defaultFriendlyCode = FriendlyCodeGenerator.generateFriendlyCode(attribute.getTextForLang(defaultLang));
			contentFriendlyCode.addFriendlyCode(defaultLang, defaultFriendlyCode);
			Iterator<Lang> langs = langManager.getLangs().iterator();
			while (langs.hasNext()) {
				Lang currentLang = langs.next();
				if (!currentLang.isDefault()) {
					String langCode = currentLang.getCode();
					String friendlyCode = FriendlyCodeGenerator.generateFriendlyCode(attribute.getTextForLang(langCode));
					if (friendlyCode!=null && !friendlyCode.equals(defaultFriendlyCode)) {
						contentFriendlyCode.addFriendlyCode(langCode, friendlyCode);
					}
				}
			}
		} else {
			String friendlyCode = FriendlyCodeGenerator.generateFriendlyCode(attribute.getText());
			contentFriendlyCode.addFriendlyCode(defaultLang, friendlyCode);
		}
		return contentFriendlyCode;
	}
	
	private void updateMapping(FriendlyCodeVO vo) {
		try {
			this.getSeoMappingDAO().updateMapping(vo);
			this.loadMapping();
		} catch (Throwable t) {
			_logger.error("Error updating mapping", t);
		}
	}
	
	private void deleteMapping(String pageCode, String contentId) {
		try {
			this.getSeoMappingDAO().deleteMapping(pageCode, contentId);
		} catch (Throwable t) {
			_logger.error("Error deleting mapping", t);
		}
	}
	
	@Override
	public List<String> searchFriendlyCode(FieldSearchFilter[] filters) throws ApsSystemException {
		List<String> codes = null;
		try {
			codes = this.getSeoMappingDAO().searchFriendlyCode(filters);
		} catch (Throwable t) {
			_logger.error("Error searching Friendly Codes", t);
			throw new ApsSystemException("Error searching Friendly Codes", t);
		}
		return codes;
	}
	
	@Override
	public FriendlyCodeVO getReference(String friendlyCode) {
		return this.getMapping().get(friendlyCode);
	}
	
	@Override
	public String getPageReference(String pageCode) {
		FriendlyCodeVO friendlyCode = this.getPageFriendlyCodes().get(pageCode);
		if (friendlyCode!=null) {
			return friendlyCode.getPageCode();
		}
		return null;
	}
	
	@Override
	public String getContentReference(String contentId, String langCode) {
		String friendlyCode = null;
		ContentFriendlyCode content = this.getContentFriendlyCodes().get(contentId);
		if (content!=null) {
			friendlyCode = content.getFriendlyCode(langCode);
			if (friendlyCode==null) {
				friendlyCode = content.getFriendlyCode(this.getLangManager().getDefaultLang().getCode());
			}
		}
		return friendlyCode;
	}
	
	protected Map<String, FriendlyCodeVO> getMapping() {
		return _mapping;
	}
	protected void setMapping(Map<String, FriendlyCodeVO> mapping) {
		this._mapping = mapping;
	}
	
	protected Map<String, FriendlyCodeVO> getPageFriendlyCodes() {
		return _pageFriendlyCodes;
	}
	protected void setPageFriendlyCodes(Map<String, FriendlyCodeVO> pageFriendlyCodes) {
		this._pageFriendlyCodes = pageFriendlyCodes;
	}
	
	protected Map<String, ContentFriendlyCode> getContentFriendlyCodes() {
		return _contentFriendlyCodes;
	}
	protected void setContentFriendlyCodes(Map<String, ContentFriendlyCode> contentFriendlyCodes) {
		this._contentFriendlyCodes = contentFriendlyCodes;
	}
	
	protected ISeoMappingDAO getSeoMappingDAO() {
		return _seoMappingDAO;
	}
	public void setSeoMappingDAO(ISeoMappingDAO seoMappingDAO) {
		this._seoMappingDAO = seoMappingDAO;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	protected IPageManager getPageManager() {
		return pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this.pageManager = pageManager;
	}
	
	private Map<String, FriendlyCodeVO> _mapping;
	private Map<String, FriendlyCodeVO> _pageFriendlyCodes;
	private Map<String, ContentFriendlyCode> _contentFriendlyCodes;
	
	private ISeoMappingDAO _seoMappingDAO;
	private ILangManager _langManager;
	private IPageManager pageManager;
	
}