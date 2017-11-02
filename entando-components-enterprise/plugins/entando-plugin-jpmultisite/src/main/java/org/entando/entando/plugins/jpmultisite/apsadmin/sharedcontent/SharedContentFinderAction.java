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
package org.entando.entando.plugins.jpmultisite.apsadmin.sharedcontent;

import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.ISharedContentManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedContentFinderAction extends AbstractContentAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SharedContentFinderAction.class);

	public List<Integer> getSharedContentsId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			
			//SHOW ONLY THE CONTENT SHARED WITH THE CURRENT MULTISITE
			FieldSearchFilter multisiteToFilter = new FieldSearchFilter(("multisitecodeto"), MultisiteUtils.getMultisiteCode(this.getRequest()), true);
			filters = this.addFilter(filters, multisiteToFilter);
			
			if (null != this.getId()) {
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getContentId())) {
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("contentid"), this.getContentId(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getMultisiteCodeSrc())) {
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("multisitecodesrc"), this.getMultisiteCodeSrc(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			
			List<Integer> sharedContents = this.getSharedContentManager().searchSharedContents(filters);
			return sharedContents;
		} catch (Throwable t) {
			_logger.error("Error getting sharedContents list", t);
			throw new RuntimeException("Error getting sharedContents list", t);
		}
	}

	protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public SharedContent getSharedContent(int id) {
		SharedContent sharedContent = null;
		try {
			sharedContent = this.getSharedContentManager().getSharedContent(id);
		} catch (Throwable t) {
			_logger.error("Error getting sharedContent with id {}", id, t);
			throw new RuntimeException("Error getting sharedContent with id " + id, t);
		}
		return sharedContent;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}


	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}


	public String getMultisiteCodeSrc() {
		return _multisiteCodeSrc;
	}
	public void setMultisiteCodeSrc(String multisiteCodeSrc) {
		this._multisiteCodeSrc = multisiteCodeSrc;
	}


	public String getMultisiteCodeTo() {
		return _multisiteCodeTo;
	}
	public void setMultisiteCodeTo(String multisiteCodeTo) {
		this._multisiteCodeTo = multisiteCodeTo;
	}


	protected ISharedContentManager getSharedContentManager() {
		return _sharedContentManager;
	}
	public void setSharedContentManager(ISharedContentManager sharedContentManager) {
		this._sharedContentManager = sharedContentManager;
	}
	
	private Integer _id;
	private String _contentId;
	private String _multisiteCodeSrc;
	private String _multisiteCodeTo;
	private ISharedContentManager _sharedContentManager;
}