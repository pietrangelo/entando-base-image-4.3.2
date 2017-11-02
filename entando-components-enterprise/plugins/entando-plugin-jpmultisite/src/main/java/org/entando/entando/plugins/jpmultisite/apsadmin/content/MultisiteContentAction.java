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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import static com.agiletec.apsadmin.system.BaseAction.USER_NOT_ALLOWED;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentAction;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.MultisiteContentManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.MultisiteSharedContentWrapper;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteContentAction extends ContentAction {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteContentAction.class);

	public String joinMultisite() {
		try {
			MultisiteSharedContentWrapper contentOnSession = (MultisiteSharedContentWrapper) this.updateContentOnSession();
			SharedContent sharedContent = new SharedContent(contentOnSession.getId(), MultisiteUtils.getMultisiteCode(this.getRequest()), this.getShareWithMultisite());
			contentOnSession.getSharedContent().add(sharedContent);
		} catch (Throwable t) {
			_logger.error("error in joinMultisite", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String removeMultisite() {
		try {
			MultisiteSharedContentWrapper contentOnSession = (MultisiteSharedContentWrapper) this.updateContentOnSession();
			Set<SharedContent> sharedContentSet = contentOnSession.getSharedContent();
			for (SharedContent sharedContent : sharedContentSet) {
				String multisiteCodeTo = sharedContent.getMultisiteCodeTo();
				if(StringUtils.contains(multisiteCodeTo, this.getShareWithMultisiteRemove())){
					sharedContentSet.remove(sharedContent);
					break;
				}
			}
		} catch (Throwable t) {
			_logger.error("error in removeMultisite", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String copyContent() {
		try {
			MultisiteSharedContentWrapper content = (MultisiteSharedContentWrapper) this.getContentManager().loadContent(this.getContentId(), this.isCopyPublicVersion());
			if (null == content) {
				throw new ApsSystemException("Content '"
						+ this.getContentId() + "' empty ; copying public version: " + this.isCopyPublicVersion());
			}
			
			//remove all groups
			content.getGroups().clear();
			//remove all categories
			content.getCategories().clear();
			//remove all shared content
			content.getSharedContent().clear();
			String multisiteMainGroup = "multisite" + MultisiteUtils.getMultisiteCodeSuffix(this.getRequest());
			
			
			//set maingroup
			content.setMainGroup(multisiteMainGroup);
			
			if (!this.isUserAllowed(content)) {
				_logger.info("User not allowed to access to this content{}", content.getId());
				return USER_NOT_ALLOWED;
			}
			content.setId(null);
			content.setVersion(Content.INIT_VERSION);
			content.setDescr(this.getText("label.copyOf") + " " + content.getDescr());
			String marker = buildContentOnSessionMarker(content, ApsAdminSystemConstants.PASTE);
			super.setContentOnSessionMarker(marker);
			this.getRequest().getSession().setAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + marker, content);
		} catch (Throwable t) {
			_logger.error("error in copyPaste", t);
			//ApsSystemUtils.logThrowable(t, this, "copyPaste");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public Category getCategoryRoot() {
		Category categoryRoot;
		String currentMultisiteCode = MultisiteUtils.getMultisiteCode(this.getRequest());
		if (StringUtils.isEmpty(currentMultisiteCode)) {
			categoryRoot = super.getCategoryRoot(); //To change body of generated methods, choose Tools | Templates.
		} else {
			categoryRoot = this.getCategoryManager().getCategory(currentMultisiteCode);
		}
		return categoryRoot;
	}
	
	@Override
	protected String saveContent(boolean approve) {
		try {
			MultisiteSharedContentWrapper currentContent = (MultisiteSharedContentWrapper) this.getContent();
			if (null != currentContent) {
				int strutsAction = (null == currentContent.getId()) ? ApsAdminSystemConstants.ADD : ApsAdminSystemConstants.EDIT;
				if (approve) {
					strutsAction += 10;
				}
				if (!this.getContentActionHelper().isUserAllowed(currentContent, this.getCurrentUser())) {
					_logger.info("User not allowed to save content {}", currentContent.getId());
					return USER_NOT_ALLOWED;
				}
				currentContent.setLastEditor(this.getCurrentUser().getUsername());
				String multisiteCode = MultisiteUtils.getMultisiteCodeSuffix(this.getRequest());
				if (approve) {
					((MultisiteContentManager) this.getContentManager()).insertOnLineContent(currentContent, multisiteCode);
				} else {
					((MultisiteContentManager) this.getContentManager()).saveContent(currentContent, multisiteCode);
				}
				this.addActivityStreamInfo(currentContent, strutsAction, true);
				this.getRequest().getSession().removeAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + super.getContentOnSessionMarker());
				_logger.info("Saving content {} - Description: '{}' - User: {}", currentContent.getId(), currentContent.getDescr(), this.getCurrentUser().getUsername());
			} else {
				_logger.error("Save/approve NULL content - User: {}", this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			_logger.error("error in saveContent", t);
			//ApsSystemUtils.logThrowable(t, this, "saveContent");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	
	@Override
	public String saveAndContinue() {
		try {
			MultisiteSharedContentWrapper currentContent = (MultisiteSharedContentWrapper) this.updateContentOnSession();
			if (null != currentContent) {
				if (null == currentContent.getDescr() || currentContent.getDescr().trim().length() == 0) {
					this.addFieldError("descr", this.getText("error.content.descr.required"));
				} else {
					currentContent.setLastEditor(this.getCurrentUser().getUsername());
					((MultisiteContentManager) this.getContentManager()).saveContent(currentContent, MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in saveAndContinue", t);
			return FAILURE;
		}
		return SUCCESS;
	}


	@Override
	protected Content updateContentOnSession() {
		Content content = super.updateContentOnSession();
		MultisiteSharedContentWrapper contentWrapper = (MultisiteSharedContentWrapper) content;
		return contentWrapper;
	}

	public String getShareWithMultisite() {
		return _shareWithMultisite;
	}

	public void setShareWithMultisite(String shareWithMultisite) {
		this._shareWithMultisite = shareWithMultisite;
	}
	
	public String getMultisiteTitle(String multisiteCode) {
		Multisite multisite = new Multisite();
		try {
			multisite = this.getMultisiteManager().loadMultisite(multisiteCode);
		} catch (ApsSystemException ex) {
			_logger.info("Error loading multisite");
			this.addActionError(this.getText(""));
		}
		return (String) multisite.getTitles().get(this.getCurrentLang().getCode());
	}

	public IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}

	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}
	
	public String getShareWithMultisiteRemove() {
		return _shareWithMultisiteRemove;
	}

	public void setShareWithMultisiteRemove(String shareWithMultisiteRemove) {
		this._shareWithMultisiteRemove = shareWithMultisiteRemove;
	}
	
	public List<String> getMultisiteList(){
		List<String> multisiteList = new ArrayList<String>();
		try {
			multisiteList = this.getMultisiteManager().loadMultisites();
			multisiteList.remove(MultisiteUtils.getMultisiteCode(this.getRequest()));
			Set<SharedContent> sharedContentSet = ((MultisiteSharedContentWrapper)this.getContent()).getSharedContent();
			for (SharedContent sharedContent : sharedContentSet) {
				String multisiteCodeTo = sharedContent.getMultisiteCodeTo();
				if(multisiteList.contains(multisiteCodeTo)){
					multisiteList.remove(multisiteCodeTo);
				}
			}
		} catch (ApsSystemException ex) {
			_logger.info("Error loading multisites");
			this.addActionError(this.getText(""));
		}
		return multisiteList;
	}

	@Override
	public List<Group> getGroups() {
		List<Group> groups = super.getGroups(); 
		List<Group> filteredGroups = new ArrayList<Group>();
		if(!MultisiteUtils.getMultisiteCode(this.getRequest()).isEmpty()){
			for (Group group : groups) {
				if(group.getName().equals(Group.FREE_GROUP_NAME) || group.getName().endsWith(MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()))){
					filteredGroups.add(group);
				}
			}
			groups = filteredGroups;
		}
		return groups; 
	}
	
	
	
	
	
	
	private IMultisiteManager _multisiteManager;
	private String _shareWithMultisite;
	private String _shareWithMultisiteRemove;

}
