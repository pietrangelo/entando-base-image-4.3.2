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

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.ISharedContentManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedContentAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SharedContentAction.class);

	public String newSharedContent() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newSharedContent", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String edit() {
		try {
			SharedContent sharedContent = this.getSharedContentManager().getSharedContent(this.getId());
			if (null == sharedContent) {
				this.addActionError(this.getText("error.sharedContent.null"));
				return INPUT;
			}
			this.populateForm(sharedContent);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			SharedContent sharedContent = this.createSharedContent();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getSharedContentManager().addSharedContent(sharedContent);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getSharedContentManager().updateSharedContent(sharedContent);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			SharedContent sharedContent = this.getSharedContentManager().getSharedContent(this.getId());
			if (null == sharedContent) {
				this.addActionError(this.getText("error.sharedContent.null"));
				return INPUT;
			}
			this.populateForm(sharedContent);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getSharedContentManager().deleteSharedContent(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String view() {
		try {
			SharedContent sharedContent = this.getSharedContentManager().getSharedContent(this.getId());
			if (null == sharedContent) {
				this.addActionError(this.getText("error.sharedContent.null"));
				return INPUT;
			}
			this.populateForm(sharedContent);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(SharedContent sharedContent) throws Throwable {
		this.setId(sharedContent.getId());
		this.setContentId(sharedContent.getContentId());
		this.setMultisiteCodeSrc(sharedContent.getMultisiteCodeSrc());
		this.setMultisiteCodeTo(sharedContent.getMultisiteCodeTo());
	}
	
	private SharedContent createSharedContent() {
		SharedContent sharedContent = new SharedContent();
		sharedContent.setId(this.getId());
		sharedContent.setContentId(this.getContentId());
		sharedContent.setMultisiteCodeSrc(this.getMultisiteCodeSrc());
		sharedContent.setMultisiteCodeTo(this.getMultisiteCodeTo());
		return sharedContent;
	}
	

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	public String getMultisiteCodeSrc() {
		return _multisitecodesrc;
	}
	public void setMultisiteCodeSrc(String multisitecodesrc) {
		this._multisitecodesrc = multisitecodesrc;
	}

	public String getMultisiteCodeTo() {
		return _multisitecodeto;
	}
	public void setMultisiteCodeTo(String multisitecodeto) {
		this._multisitecodeto = multisitecodeto;
	}

	protected ISharedContentManager getSharedContentManager() {
		return _sharedContentManager;
	}
	public void setSharedContentManager(ISharedContentManager sharedContentManager) {
		this._sharedContentManager = sharedContentManager;
	}
	
	private int _strutsAction;
	private int _id;
	private String _contentId;
	private String _multisitecodesrc;
	private String _multisitecodeto;
	
	private ISharedContentManager _sharedContentManager;
	
}