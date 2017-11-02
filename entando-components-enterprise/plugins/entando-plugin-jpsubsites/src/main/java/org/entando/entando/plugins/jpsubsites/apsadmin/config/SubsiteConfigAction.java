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
package org.entando.entando.plugins.jpsubsites.apsadmin.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.ISubsiteManager;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.SubsiteConfig;

/**
 * @author E.Santoboni
 */
public class SubsiteConfigAction extends BaseAction {

	private SubsiteConfig _subsiteConfig;

	private ISubsiteManager _subsiteManager;
	private IPageManager _pageManager;
	private ICategoryManager _categoryManager;
	private IPageModelManager _pageModelManager;
	private IResourceManager _resourceManager;
	private IContentManager _contentManager;
	private IContentModelManager _contentModelManager;

	public String edit() {
		try {
			this.setSubsiteConfig(this.getSubsiteManager().getSubsiteConfig());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			SubsiteConfig config = this.getSubsiteConfig();
			List<SmallEntityType> contentTypes = this.getContentTypes();
			for (int i = 0; i < contentTypes.size(); i++) {
				SmallEntityType smallContentType = contentTypes.get(i);
				String fieldName = "modelId_" + smallContentType.getCode();
				String modelIdString = this.getRequest().getParameter(fieldName);
				Long id = null;
				try {
					id = Long.parseLong(modelIdString);
				} catch (Exception e) {
				}
				if (null != id) {
					ContentModel model = this.getContentModelManager().getContentModel(id);
					String[] args = {modelIdString, smallContentType.getDescription()};
					if (null == model) {
						this.addFieldError(fieldName, this.getText("error.jpsubsite.config.invalidModel", args));
						continue;
					}
					if (!model.getContentType().equals(smallContentType.getCode())) {
						this.addFieldError(fieldName, this.getText("error.jpsubsite.config.invalidModelForType", args));
						continue;
					}
					config.addContentModel(smallContentType.getCode(), id);
				}
			}
			if (!this.hasFieldErrors()) {
				this.getSubsiteManager().updateConfig(config);
				this.addActionMessage(this.getText("message.jpsubsite.configUpdated"));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	public List<IPage> getFreePageList() {
		IPage root = this.getPageManager().getOnlineRoot();//getRoot();
		List<IPage> pages = new ArrayList<IPage>();
		this.addPages(root, pages, true);
		return pages;
	}

	public List<IPage> getPageList() {
		IPage root = this.getPageManager().getOnlineRoot();//.getRoot();
		List<IPage> pages = new ArrayList<IPage>();
		this.addPages(root, pages, false);
		return pages;
	}

	private void addPages(IPage page, List<IPage> pages, boolean onlyFree) {
		if (!onlyFree || page.getGroup().equals(Group.FREE_GROUP_NAME)) {
			pages.add(page);
		}
		IPage[] children = page.getChildren();
		for (int i = 0; i < children.length; i++) {
			this.addPages(children[i], pages, onlyFree);
		}
	}

	public List<Category> getCategories() {
		Category root = this.getCategoryManager().getRoot();
		List<Category> categories = new ArrayList<Category>();
		this.addCategories(root, categories);
		return categories;
	}

	private void addCategories(Category category, List<Category> categories) {
		categories.add(category);
		Category[] children = category.getChildren();
		for (int i = 0; i < children.length; i++) {
			this.addCategories(children[i], categories);
		}
	}

	public List<ContentModel> getModelsByType(String contentTypeCode) {
		return this.getContentModelManager().getModelsForContentType(contentTypeCode);
	}

	public List<SmallEntityType> getContentTypes() {
		return this.getContentManager().getSmallEntityTypes();
	}

	public List<PageModel> getPageModels() {
		List<PageModel> models = new ArrayList<PageModel>();
		models.addAll(this.getPageModelManager().getPageModels());
		BeanComparator compar = new BeanComparator("descr");
		Collections.sort(models, compar);
		return models;
	}

	public SubsiteConfig getSubsiteConfig() {
		return _subsiteConfig;
	}

	public void setSubsiteConfig(SubsiteConfig subsiteConfig) {
		this._subsiteConfig = subsiteConfig;
	}

	protected ISubsiteManager getSubsiteManager() {
		return _subsiteManager;
	}

	public void setSubsiteManager(ISubsiteManager subsiteManager) {
		this._subsiteManager = subsiteManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}

	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}

	protected IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}

	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}

	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}

	public void setResourceManager(IResourceManager _resourceManager) {
		this._resourceManager = _resourceManager;
	}

}
