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
package org.entando.entando.plugins.jpmultisite.apsadmin.content;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction;
import java.util.List;
import java.util.logging.Level;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.ISharedContentManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteContentFinderAction extends ContentFinderAction {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteContentFinderAction.class);

	/**
	 * Restitusce i filtri per la selezione e l'ordinamento dei contenuti
	 * erogati nell'interfaccia.
	 *
	 * @return Il filtri di selezione ed ordinamento dei contenuti.
	 */
	@Override
	protected EntitySearchFilter[] createFilters() {
		this.createBaseFilters();
		EntitySearchFilter multisiteFilter = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false, MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()), true);
		this.addFilter(multisiteFilter);
		if (null != this.getState() && this.getState().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_STATUS_FILTER_KEY, false, this.getState(), false);
			this.addFilter(filterToAdd);
		}
		if (null != this.getText() && this.getText().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_DESCR_FILTER_KEY, false, this.getText(), true);
			this.addFilter(filterToAdd);
		}
		if (null != this.getOwnerGroupName() && this.getOwnerGroupName().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_MAIN_GROUP_FILTER_KEY, false, this.getOwnerGroupName(), false);
			this.addFilter(filterToAdd);
		}
		if (null != this.getOnLineState() && this.getOnLineState().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.CONTENT_ONLINE_FILTER_KEY, false);
			filterToAdd.setNullOption(this.getOnLineState().trim().equals("no"));
			this.addFilter(filterToAdd);
		}
		if (null != this.getContentIdToken() && this.getContentIdToken().trim().length() > 0) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false, this.getContentIdToken(), true);
			this.addFilter(filterToAdd);
		}
		EntitySearchFilter orderFilter = this.getContentActionHelper().getOrderFilter(this.getLastGroupBy(), this.getLastOrder());
		super.addFilter(orderFilter);
		return this.getFilters();
	}

	@Override
	public String delete() {
		String delete = super.delete();
		if (SUCCESS.equals(delete)) {
			try {
				FieldSearchFilter filterByContentId = new FieldSearchFilter(("contentId"), this.getContentIds(), false);
				List<Integer> searchSharedContents = this.getSharedContentManager().searchSharedContents(new FieldSearchFilter[]{filterByContentId});
				for (Integer searchSharedContent : searchSharedContents) {
					this.getSharedContentManager().deleteSharedContent(searchSharedContent);
				}
			} catch (Throwable t) {
				_logger.error("Error deleting contentd - delete", t);
				throw new RuntimeException("Errore in cancellazione contenuti", t);
			}
		}
		return delete;
	}

	public ISharedContentManager getSharedContentManager() {
		return sharedContentManager;
	}

	public void setSharedContentManager(ISharedContentManager sharedContentManager) {
		this.sharedContentManager = sharedContentManager;
	}

	ISharedContentManager sharedContentManager;

}
