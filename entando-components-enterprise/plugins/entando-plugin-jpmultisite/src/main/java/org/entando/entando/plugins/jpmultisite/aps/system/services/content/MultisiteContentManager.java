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
package org.entando.entando.plugins.jpmultisite.aps.system.services.content;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.entando.entando.aps.system.services.cache.CacheInfoEvict;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.MultisiteSharedContentWrapper;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;

/**
 * @author S.Loru
 */
public class MultisiteContentManager extends ContentManager {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteContentManager.class);

	@Override
	public Content loadContent(String id, boolean onLine) throws ApsSystemException {
		MultisiteSharedContentWrapper content = (MultisiteSharedContentWrapper) super.loadContent(id, onLine);
		FieldSearchFilter filterByContentId = new FieldSearchFilter(("contentId"), content.getId(), false);
		List<Integer> sharedContents = this.getSharedContentManager().searchSharedContents(new FieldSearchFilter[]{filterByContentId});
		if (sharedContents != null && !sharedContents.isEmpty()) {
			Set<SharedContent> sharedContentSet = new HashSet<SharedContent>();
			for (Integer sharedContentId : sharedContents) {
				sharedContentSet.add(this.getSharedContentManager().getSharedContent(sharedContentId));
			}
			content.setSharedContent(sharedContentSet);
		}
		return content;
	}

	public void saveContent(Content content, String currentMultisiteCode) throws ApsSystemException {
		try {
			content.setLastModified(new Date());
			content.incrementVersion(false);
			String status = content.getStatus();
			if (null == status) {
				content.setStatus(Content.STATUS_DRAFT);
			} else if (status.equals(Content.STATUS_PUBLIC)) {
				content.setStatus(Content.STATUS_READY);
			}
			if (null == content.getId()) {
				IKeyGeneratorManager keyGenerator = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
				int key = keyGenerator.getUniqueKeyCurrentValue();
				String id = content.getTypeCode() + key + currentMultisiteCode;
				content.setId(id);
				this.getContentDAO().addEntity(content);
			} else {
				this.getContentDAO().updateEntity(content);
			}
			updateSharedContent(content);
		} catch (Throwable t) {
			_logger.error("Error while saving content", t);
			throw new ApsSystemException("Error while saving content", t);
		}
	}
	
	public void saveClonedContent(Content content) throws ApsSystemException {
		try {
			if (null == content.getId()) {
				throw new ApsSystemException("Error while saving content on cloning - missing id");
			}
			content.setLastModified(new Date());
			content.incrementVersion(false);
			String status = content.getStatus();
			if (null == status) {
				content.setStatus(Content.STATUS_DRAFT);
			} else if (status.equals(Content.STATUS_PUBLIC)) {
				content.setStatus(Content.STATUS_READY);
			}
			this.getContentDAO().addEntity(content);
		} catch (Throwable t) {
			_logger.error("Error while saving content", t);
			throw new ApsSystemException("Error while saving content", t);
		}
	}

	protected void updateSharedContent(Content content) throws ApsSystemException {
		try {
			FieldSearchFilter filterByContentId = new FieldSearchFilter(("contentId"), content.getId(), false);
			List<Integer> searchSharedContents = this.getSharedContentManager().searchSharedContents(new FieldSearchFilter[]{filterByContentId});
			MultisiteSharedContentWrapper contentShared = (MultisiteSharedContentWrapper) content;
			Set<SharedContent> sharedContentSet = contentShared.getSharedContent();
			for (SharedContent sharedContent : sharedContentSet) {
				sharedContent.setContentId(content.getId());
				this.getSharedContentManager().addSharedContent(sharedContent);
			}
			for (Integer sharedContentId : searchSharedContents) {
				this.getSharedContentManager().deleteSharedContent(sharedContentId);
			}
		} catch (Throwable t) {
			_logger.error("Error while saving content", t);
			throw new ApsSystemException("Error while saving content", t);
		}
	}
	
	/**
	 * Publish a content.
	 * @param content The ID associated to the content to be displayed in the portal.
	 * @throws ApsSystemException in case of error.
	 */
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
			key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_CACHE_PREFIX.concat(#content.id)", condition = "#content.id != null")
	@CacheInfoEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsToEvictCsv(#content.id, #content.typeCode)")
	public void insertOnLineContent(Content content, String multisiteCode) throws ApsSystemException {
		try {
			content.setLastModified(new Date());
			if (null == content.getId()) {
				content.setCreated(new Date());
				this.saveContent(content, multisiteCode);
			}
			content.incrementVersion(true);
			content.setStatus(Content.STATUS_PUBLIC);
			this.getContentDAO().insertOnLineContent(content);
			int operationEventCode = -1;
			if (content.isOnLine()) {
				operationEventCode = PublicContentChangedEvent.UPDATE_OPERATION_CODE;
			} else {
				operationEventCode = PublicContentChangedEvent.INSERT_OPERATION_CODE;
			}
			updateSharedContent(content);
			this.notifyPublicContentChanging(content, operationEventCode);
		} catch (Throwable t) {
			_logger.error("Error while inserting content on line", t);
			throw new ApsSystemException("Error while inserting content on line", t);
		}
	}

	/**
	 * Notify the modification of a published content.
	 *
	 * @param content The modified content.
	 * @param operationCode the operation code to notify.
	 * @exception ApsSystemException in caso of error.
	 */
	private void notifyPublicContentChanging(Content content, int operationCode) throws ApsSystemException {
		PublicContentChangedEvent event = new PublicContentChangedEvent();
		event.setContent(content);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	public ISharedContentManager getSharedContentManager() {
		return _sharedContentManager;
	}

	public void setSharedContentManager(ISharedContentManager sharedContentManager) {
		this._sharedContentManager = sharedContentManager;
	}

	private ISharedContentManager _sharedContentManager;

}
