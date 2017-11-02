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
package org.entando.entando.plugins.jpmultisite.aps.system.services.multisite;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.MultisiteContentManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.resource.MultisiteResourceManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype.IMultisiteWidgetTypeManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.entando.entando.plugins.jpmultisite.aps.system.services.resource.model.SimpleResourceDataBean;

/**
 * @author S.Loru
 */
public class MultisiteCloneManager extends AbstractService implements IMultisiteCloneManager {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteCloneManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}

	@Override
	public void cloneGroups(Multisite source, Multisite clone) throws ApsSystemException {
		List<Group> groups = this.getGroupManager().getGroups();
		Group cloneGroup = null;
		for (Group group : groups) {
			String description = "";
			if (!group.getName().equals(MultisiteUtils.getRootGroupForMultisite(source.getCode())) && group.getName().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode())) {
				description = group.getDescription();
				cloneGroup = new Group();
				cloneGroup.setName(group.getName().replace(source.getCode(), clone.getCode()));
				cloneGroup.setDescription(description);
				this.getGroupManager().addGroup(cloneGroup);
			}
		}
	}

	@Override
	public void cloneCategories(Multisite source, Multisite clone) throws ApsSystemException {
		List<Category> categoriesList = this.getCategoryManager().getCategoriesList();
		for (Category category : categoriesList) {
			if (category.getCode().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode())) {
				Category cloneCategory = new Category();
				if (category.getParentCode().equals(source.getCode())) {
					cloneCategory.setParentCode(category.getParentCode().replace(source.getCode(), clone.getCode()));
				} else {
					cloneCategory.setParentCode(category.getParentCode().replace(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode(), JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + clone.getCode()));
				}
				cloneCategory.setTitles(category.getTitles());
				cloneCategory.setCode(category.getCode().replace(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode(), JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + clone.getCode()));
				this.getCategoryManager().addCategory(cloneCategory);
			}
		}
	}

	@Override
	public void cloneWidgets(Multisite source, Multisite clone) throws ApsSystemException {
		List<WidgetType> widgetTypes = ((IMultisiteWidgetTypeManager) this.getWidgetTypeManager()).getWidgetTypesByMultisiteCode(source.getCode());
		for (WidgetType widgetType : widgetTypes) {
			WidgetType cloneWidgetType = widgetType.clone();
			cloneWidgetType.setCode(widgetType.getCode().replace(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode(), JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + clone.getCode()));
			if (this.getWidgetTypeManager().getWidgetType(cloneWidgetType.getCode()) == null) {
				this.getWidgetTypeManager().addWidgetType(cloneWidgetType);
			}
		}
	}

	@Override
	public void clonePages(Multisite source, Multisite clone) throws ApsSystemException {
		List<IPage> searchPages = this.getPageManager().searchPages("home" + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode(), getAllGroupCodeByMultisiteCode(source.getCode()));
		if (searchPages != null && searchPages.size() == 1) {
			IPage page = searchPages.get(0);
			if (page.getCode().equals("home" + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode())) {
				clonePage(source, clone, page);
			}
		}
	}

	private void clonePage(Multisite source, Multisite clone, IPage rootPage) throws ApsSystemException {
		try {
			if (!rootPage.getCode().equals("home" + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode())) {
				IPage newPage = buildNewPage(source, clone, rootPage);
				this.getPageManager().addPage(newPage);
			} else {
				IPage homeClonePage = this.getPageManager().getPage("home"+JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR+clone.getCode());
				((Page) homeClonePage).setModel(rootPage.getModel());
				Widget[] widgets = pageWidgets(rootPage, source, clone);
				((Page) homeClonePage).setWidgets(widgets);
				Set<String> cloneExtraGroups = clonePageExtraGroups(rootPage, source, clone);
				((Page) homeClonePage).setExtraGroups(cloneExtraGroups);
				this.getPageManager().updatePage(homeClonePage);
			}
			IPage[] children = rootPage.getChildren();
			for (IPage page : children) {
				clonePage(source, clone, page);
			}
		} catch (Throwable t) {
			_logger.error("Error building new page", t);
			throw new ApsSystemException("Error building new page", t);
		}

	}

	protected IPage buildNewPage(Multisite source, Multisite clone, IPage sourcePage) throws ApsSystemException {
		Page page = new Page();
		try {
			page.setParent(this.getPageManager().getPage(MultisiteUtils.replaceMultisiteCodeWithSuffix(sourcePage.getParentCode(), source.getCode(), clone.getCode())));
			page.setGroup(sourcePage.getGroup());
			page.setShowable(sourcePage.isShowable());
			page.setUseExtraTitles(sourcePage.isUseExtraTitles());
			PageModel pageModel = this.getPageModelManager().getPageModel(sourcePage.getModel().getCode());
			page.setModel(pageModel);
			Widget[] widgets = pageWidgets(sourcePage, source, clone);
			page.setWidgets(widgets);
			Set<String> cloneExtraGroups = clonePageExtraGroups(sourcePage, source, clone);
			page.setExtraGroups(cloneExtraGroups);

			page.setTitles(sourcePage.getTitles());
			String newPageCode = buildNewPageCode(sourcePage, source, clone);
			page.setCode(newPageCode);
			clonePageCharset(sourcePage, page);
			clonePageMimeType(sourcePage, page);
		} catch (Throwable t) {
			_logger.error("Error building new page", t);
			throw new ApsSystemException("Error building new page", t);
		}
		return page;
	}

	private String buildNewPageCode(IPage sourcePage, Multisite source, Multisite clone) {
		String replaceMultisiteCode = MultisiteUtils.replaceMultisiteCodeWithSuffix(sourcePage.getCode(), source.getCode(), clone.getCode());
		if(!replaceMultisiteCode.contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)){
			replaceMultisiteCode = replaceMultisiteCode + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + clone.getCode();
		}
		return replaceMultisiteCode;
	}

	private void clonePageMimeType(IPage sourcePage, Page page) {
		String mimetype = sourcePage.getMimeType();
		if (null != mimetype && mimetype.trim().length() > 0) {
			page.setMimeType(mimetype);
		} else {
			page.setMimeType(null);
		}
	}

	private void clonePageCharset(IPage sourcePage, Page page) {
		String charset = sourcePage.getCharset();
		if (null != charset && charset.trim().length() > 0) {
			page.setCharset(charset);
		} else {
			page.setCharset(null);
		}
	}

	private Set<String> clonePageExtraGroups(IPage sourcePage, Multisite source, Multisite clone) {
		Set<String> extraGroups = sourcePage.getExtraGroups();
		Set<String> cloneExtraGroups = new HashSet<String>();
		if(null != extraGroups ) {
			for (String groupCode : extraGroups) {
				cloneExtraGroups.add(MultisiteUtils.replaceMultisiteCodeWithSuffix(groupCode, source.getCode(), clone.getCode()));
			}
		}
		return cloneExtraGroups;
	}

	private Widget[] pageWidgets(IPage sourcePage, Multisite source, Multisite clone) {
		Widget[] widgets = sourcePage.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			Widget widget = widgets[i];
			if(widget != null){
				widgets[i] = new Widget();
				WidgetType type = widget.getType();
				String replaceMultisiteCode = MultisiteUtils.replaceMultisiteCodeWithSuffix(type.getCode(), source.getCode(), clone.getCode());
				widgets[i].setType(this.getWidgetTypeManager().getWidgetType(replaceMultisiteCode));
				widgets[i].setConfig(widget.getConfig());
				//widgets[i].setPublishedContent(MultisiteUtils.replaceMultisiteCodeWithSuffix(widget.getPublishedContent(), source.getCode(), clone.getCode()));
			}
		}
		return widgets;
	}

	private List<String> getAllGroupCodeByMultisiteCode(String code) {
		List<String> groupCodes = new ArrayList<String>();
		List<Group> groups = this._groupManager.getGroups();
		for (Group group : groups) {
			if (group.getName().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + code)) {
				groupCodes.add(group.getName());
			}
		}
		return groupCodes;
	}

	@Override
	public void cloneResources(Multisite source, Multisite clone) throws ApsSystemException {
		EntitySearchFilter[] filters = new EntitySearchFilter[1];
		EntitySearchFilter multisiteFilter = new EntitySearchFilter(IResourceManager.RESOURCE_ID_FILTER_KEY, false, source.getCode(), true);
		filters[0] = multisiteFilter;
		try {
			List<String> listResource = this.getResourceManager().searchResourcesId(filters, null, null);
			for (String code : listResource) {
				ResourceInterface resource = this.getResourceManager().loadResource(code);
				SimpleResourceDataBean cloneResource = new SimpleResourceDataBean();
				cloneResource.setResourceType(resource.getType());
				resourceCloneCategories(resource, cloneResource, source.getCode(), clone.getCode());
				cloneResource.setDescr(resource.getDescr());
				InputStream resourceStream = resource.getResourceStream();
				cloneResource.setMainGroup(resource.getMainGroup().replace(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode(), JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + clone.getCode()));
				cloneResource.setFile(saveTempFile(resource.getMasterFileName(), resourceStream));
				cloneResource.setMimeType(URLConnection.guessContentTypeFromStream(resourceStream));
				this.getResourceManager().addResource(cloneResource, clone.getCode());
			}
		} catch (Throwable t) {
			_logger.error("Error on cloning resources", t);
			throw new ApsSystemException("Error on cloning resources", t);
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

	private void resourceCloneCategories(ResourceInterface source, SimpleResourceDataBean clone, String multisiteSourceCode, String multisiteCloneCode) {
		List<Category> categories = source.getCategories();
		List<Category> cloneCategories = new ArrayList<Category>();
		for (Category category : categories) {
			String code = category.getCode();
			code = code.replace(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + multisiteSourceCode, JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + multisiteCloneCode);
			Category clonedCategory = this.getCategoryManager().getCategory(code);
			cloneCategories.add(clonedCategory);
		}
		clone.setCategories(cloneCategories);
	}

	@Override
	public void cloneContents(Multisite source, Multisite clone) throws ApsSystemException {
		EntitySearchFilter[] filters = new EntitySearchFilter[1];
		EntitySearchFilter multisiteFilter = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false, JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + source.getCode(), true);
		filters[0] = multisiteFilter;
		List<String> contentsId = this.getContentManager().searchId(filters);
			for (String contentId : contentsId) {
				try{
					ContentRecordVO loadContentVO = this.getContentManager().loadContentVO(contentId);
					Content loadedContent = this.getContentManager().loadContent(contentId, loadContentVO.isOnLine());
					cloneContent(loadedContent, source, clone);
				} catch (Throwable t) {
					_logger.error("Error on cloning content with id:'{}'", contentId, t);
					throw new ApsSystemException("Error on cloning content with id: "+contentId, t);
				}
			}
	}
	
	private void cloneContent(Content loadedContent, Multisite source, Multisite clone) throws ApsSystemException {
		loadedContent.setId(MultisiteUtils.replaceMultisiteCodeWithSuffix(loadedContent.getId(), source.getCode(), clone.getCode()));
		loadedContent.setVersion(Content.INIT_VERSION);
		loadedContent.setMainGroup(MultisiteUtils.replaceMultisiteCodeWithSuffix(loadedContent.getMainGroup(), source.getCode(), clone.getCode()));
		loadedContent.setCreated(new Date());
		cloneContentCategories(loadedContent, source, clone);
		cloneContentGroups(loadedContent, source, clone);
		this.getContentManager().saveClonedContent(loadedContent);
	}

	private void cloneContentCategories(Content loadedContent, Multisite source, Multisite clone) throws ApsSystemException {
		List<Category> categories = loadedContent.getCategories();
		for (Category category : categories) {
			try{
			Category cloneCategory = this.getCategoryManager().getCategory(MultisiteUtils.replaceMultisiteCodeWithSuffix(category.getCode(), source.getCode(), clone.getCode()));
			loadedContent.removeCategory(category);
			loadedContent.addCategory(cloneCategory);
			} catch (Throwable t){
				_logger.error("Error on cloning categories '{}'", category.getCode(), t);
				throw new ApsSystemException("Error on cloning categories: "+category.getCode(), t);
			}
		}
	}
	
	private void cloneContentGroups(Content loadedContent, Multisite source, Multisite clone) throws ApsSystemException {
		Set<String> groups = loadedContent.getGroups();
		for (String group : groups) {
			try{
				loadedContent.getGroups().remove(group);
				loadedContent.addGroup(MultisiteUtils.replaceMultisiteCodeWithSuffix(group, source.getCode(), clone.getCode()));
			} catch (Throwable t){
				_logger.error("Error on cloning categories '{}'", null, t);
				throw new ApsSystemException("Error on cloning categories: "+null, t);
			}
		}
	}
	

	public IGroupManager getGroupManager() {
		return _groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	public ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	public IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager _widgetTypeManager) {
		this._widgetTypeManager = _widgetTypeManager;
	}

	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public MultisiteResourceManager getResourceManager() {
		return _resourceManager;
	}

	public void setResourceManager(MultisiteResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}

	public IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}

	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}

	public MultisiteContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(MultisiteContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	private IGroupManager _groupManager;
	private ICategoryManager _categoryManager;
	private IWidgetTypeManager _widgetTypeManager;
	private IPageManager _pageManager;
	private MultisiteResourceManager _resourceManager;
	private MultisiteContentManager _contentManager;
	private IPageModelManager _pageModelManager;

}
