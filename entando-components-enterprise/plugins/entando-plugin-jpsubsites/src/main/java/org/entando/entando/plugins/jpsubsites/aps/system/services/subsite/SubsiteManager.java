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
package org.entando.entando.plugins.jpsubsites.aps.system.services.subsite;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpsubsites.aps.system.JpsubsitesSystemConstants;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.SubsiteConfig;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.parse.SubsiteConfigDOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubsiteManager extends AbstractService implements ISubsiteManager, PageUtilizer, CategoryUtilizer, GroupUtilizer {

	private static final Logger _logger = LoggerFactory.getLogger(SubsiteManager.class);

	private SubsiteConfig _config;
	private Map<String, Subsite> _subsites;

	private ConfigInterface _configManager;
	private ICategoryManager _categoryManager;
	private IPageManager _pageManager;
	private IPageModelManager _pageModelManager;
	private IGroupManager _groupManager;
	private IResourceManager _resourceManager;
	private ISubsiteDAO _subsiteDAO;
	private IContentManager _contentManager;

	@Override
	public void init() throws Exception {
		this.loadConfigs();
		this.checkPageRoot();
		this.checkCategoryRoot();
		this._subsites = this.getSubsiteDAO().loadSubsites();
		_logger.debug("{} : initialized {} subsites", this.getClass().getName(), this._subsites.size());
	}

	/**
	 * Load the XML configuration for Subsites service.
	 *
	 * @throws ApsSystemException In case of error.
	 */
	private void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpsubsitesSystemConstants.SUBSITE_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpsubsitesSystemConstants.SUBSITE_CONFIG_ITEM);
			}
			SubsiteConfigDOM configDOM = new SubsiteConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error loading configuration", t);
			throw new ApsSystemException("Error loading configuration", t);
		}
	}

	@Override
	public Map<String, Subsite> getSubsitesMap() throws ApsSystemException {
		return this._subsites;
	}

	@Override
	public Collection<Subsite> getSubsites() throws ApsSystemException {
		try {
			return this.getSubsitesMap().values();
		} catch (Throwable t) {
			_logger.error("Error loading subsites", t);
			throw new ApsSystemException("Error loading subsites", t);
		}
	}

	@Override
	public String getSubsiteCodeForContent(String contentId) throws ApsSystemException {
		try {
			return this.getSubsiteDAO().loadSubsiteCodeForContent(contentId);
		} catch (Throwable t) {
			_logger.error("Error loading subsite associated to the content of id {}", contentId, t);
			throw new ApsSystemException("Error loading subsite associated to the content of id " + contentId, t);
		}
	}

	@Override
	public Long getModelForContentType(String typeCode) throws ApsSystemException {
		return this.getConfig().getModelId(typeCode);
	}

	@Override
	public Subsite getSubsite(String code) throws ApsSystemException {
		try {
			return this.getSubsitesMap().get(code);
		} catch (Throwable t) {
			_logger.error("Error loading subsite of code {}", code, t);
			throw new ApsSystemException("Error loading subsite of code " + code, t);
		}
	}

	@Override
	public Subsite getSubsiteFromRootPage(String rootPageCode) throws ApsSystemException {
		try {
			if (null == rootPageCode) {
				return null;
			}
			Iterator<Subsite> subsitesIter = this.getSubsites().iterator();
			while (subsitesIter.hasNext()) {
				Subsite subsite = subsitesIter.next();
				if (rootPageCode.equals(subsite.getHomepage())) {
					return subsite;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading subsite from root page of code {}", rootPageCode, t);
			throw new ApsSystemException("Error loading subsite from root page of code " + rootPageCode, t);
		}
		return null;
	}

	@Override
	public String getSubsiteCodeFromRootPage(String rootPageCode) throws ApsSystemException {
		Subsite subsite = this.getSubsiteFromRootPage(rootPageCode);
		if (null != subsite) {
			return subsite.getCode();
		}
		return null;
	}

	@Override
	public String getSubsiteCodeForCategory(String categoryCode) throws ApsSystemException {
		try {
			if (null == categoryCode) {
				return null;
			}
			Iterator<Subsite> subsitesIter = this.getSubsites().iterator();
			while (subsitesIter.hasNext()) {
				Subsite subsite = subsitesIter.next();
				if (categoryCode.equals(subsite.getCategoryCode())) {
					return subsite.getCode();
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading subsite code from category of code {}", categoryCode, t);
			throw new ApsSystemException("Error loading subsite code from category of code " + categoryCode, t);
		}
		return null;
	}

	public String getSubsiteCodeForGroup(String groupName) throws ApsSystemException {
		try {
			if (null == groupName) {
				return null;
			}
			Iterator<Subsite> subsitesIter = this.getSubsites().iterator();
			while (subsitesIter.hasNext()) {
				Subsite subsite = subsitesIter.next();
				if (groupName.equals(subsite.getGroupName())) {
					return subsite.getCode();
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading subsite code from group {}", groupName, t);
			throw new ApsSystemException("Error loading subsite code from group " + groupName, t);
		}
		return null;
	}

	@Override
	public Subsite getSubsiteForPage(IPage page) throws ApsSystemException {
		Subsite subsite = null;
		try {
			if (null == page) {
				return null;
			}
			IPage target = page;
			while (target != null) {
				subsite = this.getSubsiteFromRootPage(target.getCode());
				if (null != subsite) {
					break;
				}
				IPage parent = target.getParent();
				if (parent == null || parent.getCode().equals(target.getCode())) {
					break;
				}
				target = parent;
			}
		} catch (Throwable t) {
			_logger.error("Error loading subsite code from page", t);
			throw new ApsSystemException("Error loading subsite code from page", t);
		}
		return subsite;
	}

	@Override
	public String getSubsiteCodeForPage(IPage page) throws ApsSystemException {
		Subsite subsite = this.getSubsiteForPage(page);
		if (null != subsite) {
			return subsite.getCode();
		}
		return null;
	}

	@Override
	public void addSubsite(Subsite subsite) throws ApsSystemException {
		IPage page = null;
		Category category = null;
		try {
			page = this.buildNewPage(subsite);
			this.getPageManager().addPage(page);
			category = this.buildNewCategory(subsite);
			this.getCategoryManager().addCategory(category);
			this.getSubsiteDAO().addSubsite(subsite);
			this.getSubsitesMap().put(subsite.getCode(), subsite);
		} catch (Throwable t) {
			_logger.error("Error adding subsite", t);
			if (null != page) {
				this.getPageManager().deletePage(page.getCode());
			}
			if (null != category) {
				this.getCategoryManager().deleteCategory(category.getCode());
			}
			throw new ApsSystemException("Error adding subsite", t);
		}
	}

	@Override
	public void updateSubsite(Subsite subsite) throws ApsSystemException {
		try {
			this.getSubsiteDAO().updateSubsite(subsite);
			this.getSubsitesMap().put(subsite.getCode(), subsite);
		} catch (Throwable t) {
			_logger.error("Error updating subsite", t);
			throw new ApsSystemException("Error updating subsite", t);
		}
	}

	@Override
	public void removeSubsite(String code) throws ApsSystemException {
		try {
			this.getSubsiteDAO().deleteSubsite(code);
			this.getSubsitesMap().remove(code);
		} catch (Throwable t) {
			_logger.error("Error removing subsite of code {}", code, t);
			throw new ApsSystemException("Error removing subsite of code " + code, t);
		}
	}

	protected IPage buildNewPage(Subsite subsite) throws ApsSystemException {
		Page page = new Page();
		try {
			page.setCode(subsite.getHomepage().trim());
			page.setTitles(subsite.getTitles());
			SubsiteConfig config = this.getConfig();
			String parentCode = config.getRootPageCode();
			IPage parent = this.getPageManager().getOnlinePage(parentCode);
			if (null == parent) {
				_logger.warn("Root page code not correctly configured - Page {} not exists or is not public ", parentCode);
				throw new ApsSystemException("Root page code not correctly configured - Page " + parentCode + " not exists or is not public ");
			}
			page.setParent(parent);
			page.setParentCode(parentCode);
			//page.setGroup(Group.FREE_GROUP_NAME);
			page.setGroup(subsite.getGroupName());
			page.setShowable(true);
			PageModel pageModel = this.getPageModelManager().getPageModel(config.getPageModel());
			if (null == pageModel) {
				_logger.warn("Page model not correctly configured - PageModel {} not exists", config.getPageModel());
				throw new ApsSystemException("Page model not correctly configured - PageModel " + config.getPageModel() + " not exists");
			}
			page.setModel(pageModel);
			Widget[] defaultWidgets = pageModel.getDefaultWidget();
			Widget[] widgets = new Widget[defaultWidgets.length];
			for (int i = 0; i < defaultWidgets.length; i++) {
				widgets[i] = defaultWidgets[i];
			}
			page.setWidgets(widgets);
		} catch (Throwable t) {
			_logger.error("Error building new page", t);
			throw new ApsSystemException("Error building new page", t);
		}
		return page;
	}

	private void checkPageRoot() throws ApsSystemException {
		try {
			SubsiteConfig config = this.getConfig();
			if (null != this.getPageManager().getOnlinePage(config.getRootPageCode())) {
				return;
			}
			Page subsiteRoot = new Page();
			subsiteRoot.setCode(config.getRootPageCode());
			subsiteRoot.setTitles(this.createDefaultTitles());
			IPage root = this.getPageManager().getOnlineRoot();
			subsiteRoot.setParent(root);
			subsiteRoot.setParentCode(root.getParentCode());
			subsiteRoot.setGroup(Group.FREE_GROUP_NAME);
			subsiteRoot.setShowable(true);
			Collection<PageModel> pageModels = this.getPageModelManager().getPageModels();
			if (pageModels == null) {
				_logger.warn("No one page model is defined");
				return;
			}
			List<PageModel> list = new ArrayList<PageModel>();
			list.addAll(pageModels);
			PageModel pageModel = list.get(0);
			subsiteRoot.setModel(pageModel);
			Widget[] defaultWidgets = pageModel.getDefaultWidget();
			Widget[] widgets = new Widget[defaultWidgets.length];
			for (int i = 0; i < defaultWidgets.length; i++) {
				widgets[i] = defaultWidgets[i];
			}
			subsiteRoot.setWidgets(widgets);
			this.getPageManager().addPage(subsiteRoot);
			this.getPageManager().setPageOnline(subsiteRoot.getCode());
		} catch (Throwable t) {
			_logger.error("Error building root page", t);
			throw new ApsSystemException("Error building root page", t);
		}
	}

	protected void checkCategoryRoot() throws ApsSystemException {
		try {
			SubsiteConfig config = this.getConfig();
			if (null != this.getCategoryManager().getCategory(config.getCategoriesRoot())) {
				return;
			}
			Category subsiteRoot = new Category();
			subsiteRoot.setCode(config.getCategoriesRoot());
			subsiteRoot.setTitles(this.createDefaultTitles());
			Category root = this.getCategoryManager().getRoot();
			subsiteRoot.setParent(root);
			subsiteRoot.setParentCode(root.getCode());
			this.getCategoryManager().addCategory(subsiteRoot);
		} catch (Throwable t) {
			_logger.error("Error building category root", t);
			throw new ApsSystemException("Error building category root", t);
		}
	}

	private ApsProperties createDefaultTitles() {
		ApsProperties titles = new ApsProperties();
		ILangManager langManager = (ILangManager) this.getBeanFactory().getBean(SystemConstants.LANGUAGE_MANAGER);
		List<Lang> langs = langManager.getLangs();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = langs.get(i);
			titles.put(lang.getCode(), "Subsite root");
		}
		return titles;
	}

	private Category buildNewCategory(Subsite subsite) throws ApsSystemException {
		Category category = new Category();
		try {
			category.setCode(subsite.getHomepage().trim());
			category.setTitles(subsite.getTitles());
			SubsiteConfig config = this.getConfig();
			String parentCode = config.getCategoriesRoot();
			Category parent = this.getCategoryManager().getCategory(parentCode);
			if (parent == null) {
				_logger.warn("Root category code not correctly configured - Category " + parentCode + " not exists");
				throw new ApsSystemException("Root category code not correctly configured - Category " + parentCode + " not exists");
			}
			category.setParent(parent);
			category.setParentCode(parentCode);
		} catch (Throwable t) {
			_logger.error("Error building new category", t);
			throw new ApsSystemException("Error building new category", t);
		}
		return category;
	}

	@Override
	public List getPageUtilizers(String pageCode) throws ApsSystemException {
		List<Subsite> subsites = new ArrayList<Subsite>();
		try {
			String subsiteCode = this.getSubsiteDAO().getSubsiteForPage(pageCode);
			if (subsiteCode != null) {
				subsites.add(this.getSubsite(subsiteCode));
			}
			Iterator<Subsite> iter = this.getSubsitesMap().values().iterator();
			while (iter.hasNext()) {
				Subsite subsite = iter.next();
				if (pageCode.equals(subsite.getContentViewerPage())) {
					subsites.add(subsite);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading subsite referenced with page {}", pageCode, t);
			throw new ApsSystemException("Error loading subsite referenced with page " + pageCode, t);
		}
		return subsites;
	}

	@Override
	public List getCategoryUtilizers(String categoryCode) throws ApsSystemException {
		List<Subsite> subsites = new ArrayList<Subsite>();
		try {
			String subsiteCode = this.getSubsiteCodeForCategory(categoryCode);
			if (subsiteCode != null) {
				subsites.add(this.getSubsite(subsiteCode));
			}
		} catch (Throwable t) {
			_logger.error("Error loading subsite referenced with category {}", categoryCode, t);
			throw new ApsSystemException("Error loading subsite referenced with category " + categoryCode, t);
		}
		return subsites;
	}

	@Override
	public void reloadCategoryReferences(String categoryCode) throws ApsSystemException {
		// nothing to do
	}

	@Override
	public List getCategoryUtilizersForReloadReferences(String categoryCode) throws ApsSystemException {
		// nothing to do
		return new ArrayList();
	}

	@Override
	public List getGroupUtilizers(String groupName) throws ApsSystemException {
		List<Subsite> subsites = new ArrayList<Subsite>();
		try {
			String subsiteCode = this.getSubsiteCodeForGroup(groupName);
			if (subsiteCode != null) {
				subsites.add(this.getSubsite(subsiteCode));
			}
		} catch (Throwable t) {
			_logger.error("Error loading subsite referenced with group {}", groupName, t);
			throw new ApsSystemException("Error loading subsite referenced with group " + groupName, t);
		}
		return subsites;
	}

	@Override
	public void updateConfig(SubsiteConfig config) throws ApsSystemException {
		try {
			SubsiteConfigDOM dom = new SubsiteConfigDOM();
			String xml = dom.createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpsubsitesSystemConstants.SUBSITE_CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error updating config", t);
			throw new ApsSystemException("Error updating config", t);
		}
	}

	//METHOD TAKEN FROM AbstractResource
	protected File saveTempFile(String filename, InputStream is) throws ApsSystemException {
		String tempDir = System.getProperty("java.io.tmpdir");
		String filePath = tempDir + File.separator + filename;
		try {
			byte[] buffer = new byte[1024];
			int length = -1;
			FileOutputStream outStream = new FileOutputStream(filePath);
			while ((length = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
				outStream.flush();
			}
			outStream.close();
			is.close();
		} catch (Throwable t) {
			_logger.error("Error on saving temporary file '{}'", filename, t);
			throw new ApsSystemException("Error on saving temporary file", t);
		}
		return new File(filePath);
	}

	@Override
	public String getRootPageCode() {
		return this.getConfig().getRootPageCode();
	}

	@Override
	public String getCategoriesRoot() {
		return this.getConfig().getCategoriesRoot();
	}

	public Category getCategory(String code) {
		return this.getCategoryManager().getCategory(code);
	}

	@Override
	public SubsiteConfig getSubsiteConfig() {
		return _config.clone();
	}

	protected SubsiteConfig getConfig() {
		return _config;
	}

	protected void setConfig(SubsiteConfig config) {
		this._config = config;
	}

	/**
	 * Returns the configuration manager.
	 *
	 * @return The Configuration manager.
	 */
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}

	/**
	 * Set method for Spring bean injection.<br /> Set the Configuration
	 * manager.
	 *
	 * @param configManager The Configuration manager.
	 */
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	/**
	 * Returns the manager of the pages.
	 *
	 * @return The manager of the pages.
	 */
	protected IPageManager getPageManager() {
		return _pageManager;
	}

	/**
	 * Sets the manager of the pages.
	 *
	 * @param pageManager The manager of the pages.
	 */
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	/**
	 * Returns the manager of the subsiteRoot models.
	 *
	 * @return The manager of the subsiteRoot models.
	 */
	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}

	/**
	 * Sets the manager of the subsiteRoot models.
	 *
	 * @param pageModelManager The manager of the subsiteRoot models.
	 */
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	protected ISubsiteDAO getSubsiteDAO() {
		return _subsiteDAO;
	}

	public void setSubsiteDAO(ISubsiteDAO subsiteDAO) {
		this._subsiteDAO = subsiteDAO;
	}

	public IResourceManager getResourceManager() {
		return _resourceManager;
	}

	public void setResourceManager(IResourceManager _resourceManager) {
		this._resourceManager = _resourceManager;
	}

	public IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager _contentManager) {
		this._contentManager = _contentManager;
	}

}
