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
package org.entando.entando.plugins.jpsubsites.apsadmin.subsite;

import com.agiletec.aps.system.common.entity.model.SmallEntityType;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.util.EntityAttributeIterator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jacms.aps.system.services.content.widget.RowContentListHelper;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.ISubsiteManager;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubsiteAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(SubsiteAction.class);

	private static final String OLD_SUBSITE_SESSION_PARAM = "sessionParam_oldSubsite";

	private int _strutsAction;
	private String _code;
	private Subsite _subsite = new Subsite();
	private String _cssFileText;

	private File _uploadHeader;
	private String _uploadHeaderContentType;
	private String _uploadHeaderFileName;

	private ISubsiteManager _subsiteManager;
	private IPageManager _pageManager;
	private ICategoryManager _categoryManager;
	private IGroupManager _groupManager;
	private IStorageManager _storageManager;
	private IResourceManager _resourceManager;
	private IContentManager _contentManager;
	private boolean _isOn;

	private IPageActionHelper _pageActionHelper;

	@Override
	public void validate() {
		super.validate();
		String code = this.getSubsite().getCode();
		if (code != null && code.length() > 0) {
			if (!this.isCodeAvailable()) {
				this.addFieldError("subsite.code", this.getText("Errors.code.alreadyInUse"));
			} else if (!this.isPageAvailable()) {
				this.addFieldError("subsite.code", this.getText("Errors.code.page.alreadyInUse"));
			} else if (!this.isCategoryAvailable()) {
				this.addFieldError("subsite.code", this.getText("Errors.category.alreadyInUse"));
			}
		}
		if (!this.checkViewerPage()) {
			this.addFieldError("subsite.contentViewerPage", this.getText("Errors.contentViewerPage.wrong"));// TODO AGGIUNGERE
		}
		ApsProperties titles = this.loadProperties("titles", true);
		this.getSubsite().setTitles(titles);
		ApsProperties descriptions = this.loadProperties("descriptions", false);
		this.getSubsite().setDescriptions(descriptions);
		ApsProperties viewerPage = this.loadViewerPages(true);
		this.getSubsite().setViewerPages(viewerPage);
	}

	private ApsProperties loadProperties(String prefix, boolean validate) {
		ApsProperties properties = new ApsProperties();
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String code = lang.getCode();
			String paramName = prefix + "_" + code;
			String title = this.getRequest().getParameter(paramName);
			if (null != title && title.trim().length() > 0) {
				properties.put(code, title);
			} else if (validate) {
				String[] args = {lang.getDescr()};
				this.addFieldError(paramName, this.getText("Errors." + prefix + ".needValue", args));
			}
		}
		return properties;
	}

	private ApsProperties loadViewerPages(boolean validate) {
		ApsProperties viewerPages = new ApsProperties();
		List<SmallEntityType> types = this.getContentTypes();
		for (int i = 0; i < types.size(); i++) {
			SmallEntityType smet = types.get(i);
			String paramName = smet.getCode() + "_viewerPage";
			String viewPage = this.getRequest().getParameter(paramName);
			if (StringUtils.isNotEmpty(viewPage)) {
				if (null != this.getPageManager().getOnlinePage(viewPage)) {
					viewerPages.put(smet.getCode(), viewPage);
				} else if (validate) {
					String[] args = {smet.getDescription()};
					this.addFieldError(paramName, this.getText("Errors.content.viewerPage.invalid", args));
				}
			}
		}
		return viewerPages;
	}

	public boolean checkViewerPage() {
		boolean checked = false;
		try {
			String contentViewerPage = this.getSubsite().getContentViewerPage();
			if (ApsAdminSystemConstants.EDIT == this.getStrutsAction() && contentViewerPage != null && contentViewerPage.length() > 0) {
				if (this.getSubsiteManager().getSubsiteCodeFromRootPage(contentViewerPage) == null) {
					IPage page = this.getPageManager().getOnlinePage(contentViewerPage);
					if (page != null) {
						String subsiteCode = this.getSubsiteManager().getSubsiteCodeForPage(page);
						if (subsiteCode != null && subsiteCode.equals(this.getSubsite().getCode())) {
							checked = true;
						}
					}
				}
			} else {
				checked = true;
			}
		} catch (Throwable t) {
			_logger.error("Error checking if category is available", t);
			throw new RuntimeException("Error checking if category is available", t);
		}
		return checked;
	}

	public boolean isCodeAvailable() {
		try {
			String code = this.getSubsite().getCode();
			return ApsAdminSystemConstants.EDIT == this.getStrutsAction() || (code != null && this.getSubsiteManager().getSubsite(code) == null);
		} catch (Throwable t) {
			_logger.error("Error checking if code is available", t);
			throw new RuntimeException("Error checking if code is available", t);
		}
	}

	public boolean isPageAvailable() {
		try {
			String code = this.getSubsite().getCode();
			return ApsAdminSystemConstants.EDIT == this.getStrutsAction() || (code != null && this.getPageManager().getDraftPage(code) == null);
		} catch (Throwable t) {
			_logger.error("Error checking if page is available", t);
			throw new RuntimeException("Error checking if page is available", t);
		}
	}

	public boolean isCategoryAvailable() {
		try {
			String code = this.getSubsite().getCode();
			return ApsAdminSystemConstants.EDIT == this.getStrutsAction() || (code != null && this.getCategoryManager().getCategory(code) == null);
		} catch (Throwable t) {
			_logger.error("Error checking if category is available", t);
			throw new RuntimeException("Error checking if category is available", t);
		}
	}

	public boolean existsCategory() {
		try {
			String categoryCode = this.getSubsite().getCategoryCode();
			return categoryCode != null && this.getCategoryManager().getCategory(categoryCode) != null;
		} catch (Throwable t) {
			_logger.error("Error checking if category exists", t);
			throw new RuntimeException("Error checking if category exists", t);
		}
	}

	/**
	 * Execute the preliminar operation to create a new subsite.
	 *
	 * @return The result code of the action.
	 */
	public String newSubsite() {
		this.getRequest().getSession().removeAttribute(OLD_SUBSITE_SESSION_PARAM);
		this.setSubsite(new Subsite());
		this.setStrutsAction(ApsAdminSystemConstants.ADD);
		return SUCCESS;
	}

	/**
	 * Execute the preliminar operation to update a subsite.
	 *
	 * @return The result code of the action.
	 */
	public String edit() {
		try {
			this.getRequest().getSession().removeAttribute(OLD_SUBSITE_SESSION_PARAM);
			Subsite subsite = this.getSubsiteManager().getSubsite(this.getCode());
			if (subsite == null) {
				this.addActionError(this.getText("Errors.subsite.notFound", new String[]{this.getCode()}));
				return "notFound";
			}
			this.setSubsite(subsite.clone());
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
			String cssPath = ISubsiteManager.CSS_SUBFOLDER + File.separator + subsite.getCssFileName();
			try {
				String cssFileText = this.getStorageManager().readFile(cssPath, false);
				this.setCssFileText(cssFileText);
			} catch (Exception e) {
				_logger.info("{} file doesn't not exists", cssPath);
			}
		} catch (Throwable t) {
			_logger.error("Error editing subsite", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Execute the preliminar operation to clone a subsite.
	 *
	 * @return The result code of the action.
	 */
	public String cloneSubsite() {
		try {
			this.getRequest().getSession().removeAttribute(OLD_SUBSITE_SESSION_PARAM);
			Subsite subsite = this.getSubsiteManager().getSubsite(this.getCode());
			if (subsite == null) {
				this.addActionError(this.getText("Errors.subsite.notFound", new String[]{this.getCode()}));
				return "notFound";
			}
			this.setSubsite(subsite.clone());
			this.getRequest().getSession().setAttribute(OLD_SUBSITE_SESSION_PARAM, subsite.getCode());
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			String cssPath = ISubsiteManager.CSS_SUBFOLDER + File.separator + subsite.getCssFileName();
			try {
				String cssFileText = this.getStorageManager().readFile(cssPath, false);
				if (cssFileText != null && !cssFileText.equals("")) {
					this.setCssFileText(cssFileText);
				}
			} catch (Exception e) {
				_logger.info("{} file doesn't not exists", cssPath);
			}
			this.getSubsite().setCode(null);
		} catch (Throwable t) {
			_logger.error("Error cloning subsite", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String saveClone() {
		try {
			final Thread t = new Thread(new Runnable() {
				public void run() {
					executeClone();
				}
			});
			t.start();
		} catch (Throwable t) {
			_logger.error("Error saving subsite", t);
			return FAILURE;
		}
		this.addActionMessage("The cloning operation may take several minutes");
		this.getRequest().getSession(false).setAttribute("messageCloned", "The operation may take several minutes ");
		return SUCCESS;
	}

	private void executeClone() {
		try {
			String codeOld = (String) this.getRequest().getSession().getAttribute(OLD_SUBSITE_SESSION_PARAM);
			_logger.info("Init clone Subsite " + codeOld + " .....");
			Subsite subsiteOld = this.getSubsiteManager().getSubsite(codeOld);
			Subsite subsiteCloned = this.createSubsite();
			if (null != subsiteOld.getImageFileName()) {
				String imgFileName = subsiteOld.getImageFileName();
				String imgExtension = imgFileName.substring(imgFileName.length() - 4);
				imgFileName = imgFileName.substring(0, imgFileName.length() - 4);
				imgFileName = imgFileName + "_" + subsiteCloned.getCode() + imgExtension;
				subsiteCloned.setImageFileName(imgFileName);
			}
			subsiteCloned.setContentViewerPage(subsiteOld.getContentViewerPage());
			if (null != subsiteOld.getCssFileName()) {
				String cssFileName = subsiteOld.getCssFileName();
				cssFileName = cssFileName.substring(0, cssFileName.length() - 4);
				cssFileName = cssFileName + "_" + subsiteCloned.getCode() + ".css";
				subsiteCloned.setCssFileName(cssFileName);
			}
			this.getSubsiteManager().addSubsite(subsiteCloned);
			if (null != subsiteOld.getCssFileName()) {
				String cssPathOld = ISubsiteManager.CSS_SUBFOLDER + File.separator + subsiteOld.getCssFileName();
				InputStream oldcss = this.getStorageManager().getStream(cssPathOld, false);
				if (null != oldcss) {
					String cssPath = ISubsiteManager.CSS_SUBFOLDER + File.separator + subsiteCloned.getCssFileName();
					this.getStorageManager().saveFile(cssPath, false, oldcss);
				} else {
					_logger.info("Null CSS in path {}", cssPathOld);
				}
			}
			if (null != this.getUploadHeader() && null != subsiteOld.getImageFileName()) {
				String imagePathOld = ISubsiteManager.HEADER_SUBFOLDER + File.separator + subsiteOld.getImageFileName();
				InputStream oldImg = this.getStorageManager().getStream(imagePathOld, false);
				if (null != oldImg) {
					String imagePath = ISubsiteManager.HEADER_SUBFOLDER + File.separator + subsiteCloned.getImageFileName();
					this.getStorageManager().saveFile(imagePath, false, oldImg);
				} else {
					_logger.info("Null Image in path {}", imagePathOld);
				}
			}
			this.setCode(subsiteCloned.getCode());

			Map<String, String> resourcesIdMap = this.cloneResources(subsiteOld, subsiteCloned);
			Collection<Content> clonedContents = new ArrayList<Content>();
			Map<String, String> contentsIdMap = this.cloneContents(clonedContents, subsiteOld, subsiteCloned, resourcesIdMap);
			Map<String, String> pagesIdMap = this.clonePages(subsiteOld, subsiteCloned, contentsIdMap);
			this.updateContentReferences(clonedContents, contentsIdMap, pagesIdMap);

			ApsProperties newViewerPages = new ApsProperties();
			ApsProperties viewerPages = subsiteOld.getViewerPages();
			if (null != viewerPages) {
				Iterator<Object> iter = viewerPages.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next().toString();
					String oldPage = viewerPages.getProperty(key);
					String newValue = pagesIdMap.get(oldPage);
					if (null != newValue) {
						newViewerPages.put(key, newValue);
					}
				}
			}
			if (!newViewerPages.isEmpty()) {
				subsiteCloned.setViewerPages(newViewerPages);
			}
			String defaultViewerPage = (null != subsiteOld.getContentViewerPage())
					? pagesIdMap.get(subsiteOld.getContentViewerPage())
					: null;
			subsiteCloned.setContentViewerPage(defaultViewerPage);
			this.getSubsiteManager().updateSubsite(subsiteCloned);
			_logger.info("Subsite " + subsiteOld.getCode() + " CLONED");
		} catch (Throwable t) {
			this.executeDeleteSubsite();
			_logger.error("Error saving cloned subsite", t);
		}
		this.addActionMessage("This operation may take several minutes");
	}

	private Map<String, String> cloneResources(Subsite oldSubsite, Subsite newSubsite) throws ApsSystemException {
		List<ResourceInterface> resourcesToClone = this.getResourcesBySubsite(oldSubsite);
		Iterator<ResourceInterface> itr2 = resourcesToClone.iterator();
		Map<String, String> resourcesIdMap = new HashMap<String, String>();
		while (itr2.hasNext()) {
			ResourceInterface ri = itr2.next();
			if (!ri.getMainGroup().equalsIgnoreCase(oldSubsite.getGroupName())) {
				continue;
			}
			String oldId = ri.getId();
			InputStream stream = ri.getResourceStream();
			if (null == stream) {
				continue;
			}
			String filename = newSubsite.getCode() + "_" + ri.getMasterFileName();
			File tempFile = this.createTempFileForClone(filename, stream);

			BaseResourceDataBean dataBean = new BaseResourceDataBean(tempFile);
			dataBean.setCategories(ri.getCategories());
			dataBean.setDescr(ri.getDescription());
			dataBean.setFileName(ri.getMasterFileName());
			dataBean.setMainGroup(newSubsite.getGroupName());
			dataBean.setMimeType(URLConnection.guessContentTypeFromName(filename));
			dataBean.setResourceType(ri.getType());
			ResourceInterface newResource = this.getResourceManager().addResource(dataBean);
			resourcesIdMap.put(oldId, newResource.getId());
			tempFile.delete();
		}
		return resourcesIdMap;
	}

	private List<ResourceInterface> getResourcesBySubsite(Subsite subsite) throws ApsSystemException {
		Collection<String> groupCollection = new ArrayList<String>();
		groupCollection.add(subsite.getGroupName());
		List<String> resourceIds = this.getResourceManager().searchResourcesId(null, null, null, groupCollection);
		List<ResourceInterface> resources = new ArrayList();
		Iterator itr = resourceIds.iterator();
		while (itr.hasNext()) {
			String id = (String) itr.next();
			ResourceInterface ri = this.getResourceManager().loadResource(id);
			if (null != ri && ri.getMainGroup().equalsIgnoreCase(subsite.getGroupName())) {
				resources.add(ri);
			}
		}
		return resources;
	}

	private Map<String, String> cloneContents(Collection<Content> clonedContents,
			Subsite oldSubsite, Subsite newSubsite, Map<String, String> resourcesIdMap) throws ApsSystemException {
		List<Content> contentsToClone = this.getContentsBySubsite(oldSubsite);
		Map<String, String> contentsIdMap = new HashMap<String, String>();
		Iterator itr3 = contentsToClone.iterator();
		while (itr3.hasNext()) {
			Content contentToClone = (Content) itr3.next();
			if (contentToClone != null) {
				String oldId = contentToClone.getId();
				contentToClone.setMainGroup(this.getSubsite().getGroupName());
				contentToClone.getGroups().clear();
				contentToClone.getCategories().remove(this.getCategory(oldSubsite.getCategoryCode()));
				contentToClone.getCategories().add(this.getCategory(newSubsite.getCategoryCode()));
				EntityAttributeIterator attributeIter = new EntityAttributeIterator(contentToClone);
				while (attributeIter.hasNext()) {
					AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
					if (!(currAttribute instanceof AbstractResourceAttribute)) {
						continue;
					}
					AbstractResourceAttribute resAttr = (AbstractResourceAttribute) currAttribute;
					Map<String, ResourceInterface> resources = resAttr.getResources();
					if (null == resources || resources.isEmpty()) {
						continue;
					}
					Iterator<String> iterResLang = resources.keySet().iterator();
					while (iterResLang.hasNext()) {
						String langCode = iterResLang.next();
						ResourceInterface res = resAttr.getResource(langCode);
						if (resourcesIdMap.containsKey(res.getId())) {
							String newId = resourcesIdMap.get(res.getId());
							ResourceInterface newRes = this.getResourceManager().loadResource(newId);
							resources.put(langCode, newRes);
						}
					}
				}
				contentToClone.setId(null);
				this.getContentManager().insertOnLineContent(contentToClone);
				clonedContents.add(contentToClone);
				contentsIdMap.put(oldId, contentToClone.getId());
			}
		}
		return contentsIdMap;
	}

	private List<Content> getContentsBySubsite(Subsite subsite) throws ApsSystemException {
		Collection<String> groupCollection = new ArrayList<String>();
		groupCollection.add(subsite.getGroupName());
		List<Content> contentsToClone = new ArrayList<Content>();
		List<String> contentList = this.getContentManager().loadWorkContentsId(null, groupCollection);
		Iterator contsItr = contentList.iterator();
		contentList.listIterator();
		while (contsItr.hasNext()) {
			String precContent = (String) contsItr.next();
			Content content = this.getContentManager().loadContent(precContent, true);
			if (content.getMainGroup().equalsIgnoreCase(subsite.getGroupName())) {
				contentsToClone.add(content);
			}
		}
		return contentsToClone;
	}

	private Map<String, String> clonePages(Subsite oldSubsite, Subsite newSubsite, Map<String, String> contentsIdMap) throws ApsSystemException {
		String oldHomepageCode = oldSubsite.getHomepage();
		IPage oldHomepage = (Page) this.getPageManager().getOnlinePage(oldHomepageCode);
		if (null == oldHomepage) {
			oldHomepage = (Page) this.getPageManager().getDraftPage(oldHomepageCode);
		}
		Widget[] oldWidg = oldHomepage.getWidgets();
		Page subsiteNewHomePage = (Page) this.getPageManager().getDraftPage(newSubsite.getCode());
		subsiteNewHomePage.setGroup(newSubsite.getGroupName());
		subsiteNewHomePage.setWidgets(oldWidg);
		subsiteNewHomePage.getMetadata().addExtraGroup(this.getSubsite().getGroupName());
		this.updatePublishedContents(subsiteNewHomePage, contentsIdMap);
		this.getPageManager().updatePage(subsiteNewHomePage);
		//ITreeNode node = this.getPageManager().getNode(oldHomepageCode);
		List<IPage> pagesToClone = new ArrayList<IPage>();
		this.extractChildrenPags(oldHomepage, pagesToClone);
		Map<String, String> pagesIdMap = new HashMap<String, String>();
		Iterator pageIter = pagesToClone.iterator();
		int ipager = 0;
		while (pageIter.hasNext()) {
			Page pageTemp = (Page) pageIter.next();
			String parent = pageTemp.getParentCode();
			Page pageTempCloned = pageTemp;
			String tempID = pageTemp.getCode();
			String code = this.getPageActionHelper().buildCode(newSubsite.getCode(), newSubsite.getCode(), 25);
			if (code.length() > 30) {
				code = code.substring(0, 28) + ipager;
				ipager++;
			}
			pageTempCloned.setCode(code);
			pageTempCloned.setGroup(newSubsite.getGroupName());
			this.updatePublishedContents(pageTempCloned, contentsIdMap);
			if (parent.equals(oldHomepageCode)) {
				pageTempCloned.setParentCode(newSubsite.getCode());
				pageTempCloned.setParent(this.getPageManager().getDraftPage(newSubsite.getCode()));
			} else {
				String newParentCode = pagesIdMap.get(parent);
				pageTempCloned.setParentCode(newParentCode);
				pageTempCloned.setParent(this.getPageManager().getDraftPage(newParentCode));
			}
			pagesIdMap.put(tempID, pageTempCloned.getCode());
			this.getPageManager().addPage(pageTempCloned);
		}
		return pagesIdMap;
	}

	protected File createTempFileForClone(String filename, InputStream is) throws ApsSystemException {
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
			_logger.error("Error on saving temporary file", t);
			throw new ApsSystemException("Error on saving temporary file", t);
		}
		return new File(filePath);
	}

	private void updatePublishedContents(Page pageToUpdate, Map<String, String> contentsIdMap) {
		Widget[] listWidget = pageToUpdate.getWidgets();
		for (int i = 0; i < listWidget.length; i++) {
			Widget widget = listWidget[i];
			if (widget == null || null == widget.getConfig()) {
				continue;
			}
			ApsProperties config = widget.getConfig();
			String keyToFind = "contentId";
			if (config.getProperty(keyToFind) != null) {
				String contentId = config.getProperty(keyToFind);
				if (contentsIdMap.containsKey(contentId)) {
					config.setProperty(keyToFind, contentsIdMap.get(contentId));
				}
			}
			String keyToFind2 = "contents";
			if (config.getProperty(keyToFind2) != null) {
				List<Properties> contentsProperties = RowContentListHelper.fromParameterToContents(config.getProperty(keyToFind2));
				for (int j = 0; j < contentsProperties.size(); j++) {
					Properties contentProperties = contentsProperties.get(j);
					String contentId = contentProperties.getProperty("contentId");
					if (null != contentId && contentsIdMap.containsKey(contentId)) {
						contentProperties.setProperty(keyToFind, contentsIdMap.get(contentId));
					}
				}
				String newWidgetParam = RowContentListHelper.fromContentsToParameter(contentsProperties);
				config.setProperty(keyToFind2, newWidgetParam);
			}
		}
	}

	private void updateContentReferences(Collection<Content> clonedContents,
			Map<String, String> contentsIdMap, Map<String, String> pagesIdMap) throws ApsSystemException {
		Iterator<Content> itr = clonedContents.iterator();
		while (itr.hasNext()) {
			Content clonedContent = itr.next();
			EntityAttributeIterator attributeIter = new EntityAttributeIterator(clonedContent);
			while (attributeIter.hasNext()) {
				AttributeInterface currAttribute = (AttributeInterface) attributeIter.next();
				if (!(currAttribute instanceof LinkAttribute)) {
					continue;
				}
				LinkAttribute linkAttr = (LinkAttribute) currAttribute;
				SymbolicLink sl = linkAttr.getSymbolicLink();
				if (null == sl) {
					continue;
				}
				if (sl.getDestType() == SymbolicLink.CONTENT_TYPE) {
					String contentId = sl.getContentDest();
					if (contentsIdMap.containsKey(contentId)) {
						String newContentId = contentsIdMap.get(contentId);
						sl.setDestinationToContent(newContentId);
					}
				} else if (sl.getDestType() == SymbolicLink.CONTENT_ON_PAGE_TYPE) {
					String contentId = sl.getContentDest();
					String pageCode = sl.getPageDest();
					if (contentsIdMap.containsKey(contentId) || pagesIdMap.containsKey(pageCode)) {
						String newContentId = (contentsIdMap.containsKey(contentId)) ? contentsIdMap.get(contentId) : contentId;
						String newPageCode = (pagesIdMap.containsKey(pageCode)) ? pagesIdMap.get(pageCode) : pageCode;
						sl.setDestinationToContentOnPage(newContentId, newPageCode);
					}
				} else if (sl.getDestType() == SymbolicLink.PAGE_TYPE) {
					String pageCode = sl.getPageDest();
					if (pagesIdMap.containsKey(pageCode)) {
						String newPageCode = pagesIdMap.get(pageCode);
						sl.setDestinationToPage(newPageCode);
					}
				}
			}
			this.getContentManager().insertOnLineContent(clonedContent);
		}
	}

	public String visibility() {
		try {
			final Thread t = new Thread(new Runnable() {
				public void run() {
					visibilityContent();
				}
			});
			t.start();
		} catch (Throwable t) {
			_logger.error("Error on saving visibility", t);
			return FAILURE;
		}
		addActionMessage(this.getText("message.jpsubsite.visibility"));
		return SUCCESS;
	}

	private String visibilityContent() {
		try {
			this._isOn = this.getParameter("on") != null && this.getParameter("on").equals("true");
			Subsite subsite = this.getSubsiteManager().getSubsite(this.getCode());
			if (subsite == null) {
				this.addActionError(this.getText("Errors.subsite.notFound", new String[]{this.getCode()}));
			} else {
				subsite.setVisibility(_isOn);
				Collection<String> groupCollection = new ArrayList<String>();
				groupCollection.add(subsite.getGroupName());
				Collection groupCont = this.getContentManager().loadWorkContentsId(null, groupCollection);
				Iterator itrGroupCont = groupCont.iterator();
				Collection<Content> groupObject = new ArrayList<Content>();
				while (itrGroupCont.hasNext()) {
					String groupContent = (String) itrGroupCont.next();
					Content temp = this.getContentManager().loadContent(groupContent, true);
					groupObject.add(temp);
				}
				if (_isOn) {
					for (Content c : groupObject) {
						if (c != null) {
							c.addGroup(Group.FREE_GROUP_NAME);
							this.getContentManager().saveContent(c);
							this.getContentManager().insertOnLineContent(c);
						}
					}
				} else {
					for (Content d : groupObject) {
						if (d != null) {
							d.getGroups().remove(Group.FREE_GROUP_NAME);
							this.getContentManager().saveContent(d);
							this.getContentManager().insertOnLineContent(d);
						}
					}
				}
			}
			this.getSubsiteManager().updateSubsite(subsite);
			return this.visibilityPage();
		} catch (ApsSystemException t) {
			_logger.error("Error on saving visibility", t);
			return FAILURE;
		}
	}

	protected String visibilityPage() {
		try {
			Subsite subsite = this.getSubsiteManager().getSubsite(this.getCode());
			if (subsite == null) {
				this.addActionError(this.getText("Errors.subsite.notFound", new String[]{this.getCode()}));
				return INPUT;
			} else {
				subsite.setVisibility(_isOn);
				IPage subsiteHomePage = (IPage) this.getPageManager().getDraftPage(subsite.getHomepage());
				if (null == subsiteHomePage) {
					this.addActionError(this.getText("Errors.subsite.pageRootNotFound", new String[]{this.getCode(), subsite.getHomepage()}));
					return INPUT;
				}
				if (this._isOn) {
					this.getPageManager().setPageOnline(subsite.getHomepage());
				} else {
					this.getPageManager().setPageOffline(subsite.getHomepage());
				}
				List<IPage> pageColl = new ArrayList<IPage>();
				this.extractChildrenPags(subsiteHomePage, pageColl);
				Iterator<IPage> itrGroupPageCont = (Iterator) pageColl.iterator();
				if (_isOn) {
					while (itrGroupPageCont.hasNext()) {
						IPage temp = itrGroupPageCont.next();
						this.getPageManager().setPageOnline(temp.getCode());
					}
				} else {
					while (itrGroupPageCont.hasNext()) {
						Page tempRemove = (Page) itrGroupPageCont.next();
						this.getPageManager().setPageOffline(tempRemove.getCode());
					}
				}
				this.getSubsiteManager().updateSubsite(subsite);
			}
		} catch (ApsSystemException t) {
			_logger.error("Error on saving visibility", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void extractChildrenPags(ITreeNode node, List<IPage> coll) {
		ITreeNode[] childrens = node.getChildren();
		if (null == childrens) {
			return;
		}
		for (int i = 0; i < childrens.length; i++) {
			IPage temp = (IPage) childrens[i];
			coll.add((Page) temp);
			this.extractChildrenPags(temp, coll);
		}
	}

	/**
	 * Execute the operation to save a subsite.
	 *
	 * @return The result code of the action.
	 */
	public String save() {
		try {
			Subsite subsite = this.createSubsite();
			if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
				this.getSubsiteManager().updateSubsite(subsite);
				if (null != this.getCssFileText()) {
					String cssPath = ISubsiteManager.CSS_SUBFOLDER + File.separator + subsite.getCssFileName();
					InputStream is = new ByteArrayInputStream(this.getCssFileText().getBytes("UTF-8"));
					this.getStorageManager().saveFile(cssPath, false, is);
				}
				if (null != this.getUploadHeader()) {
					String imagePath = ISubsiteManager.HEADER_SUBFOLDER + File.separator + subsite.getImageFileName();
					this.getStorageManager().saveFile(imagePath, false, this.getUploadInputStream());
				}
			} else if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
				this.getSubsiteManager().addSubsite(subsite);
			}
		} catch (Throwable t) {
			_logger.error("Error on saving subsite", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Execute the preliminar operation to delete a subsite.
	 *
	 * @return The result code of the action.
	 */
	public String trash() {
		try {
			Subsite subsite = this.getSubsiteManager().getSubsite(this.getCode());
			if (subsite == null) {
				this.addActionError(this.getText("Errors.subsite.notFound", new String[]{this.getCode()}));
				return "notFound";
			}
			this.setSubsite(subsite);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("Error on trash subsite", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Delete a subsite.
	 *
	 * @return The result code of the action.
	 */
	public String delete() {
		try {
			Subsite subsite = this.getSubsiteManager().getSubsite(this.getCode());
			if (subsite == null) {
				this.addActionError(this.getText("Errors.subsite.notFound", new String[]{this.getCode()}));
				return INPUT;
			}
			if (subsite.getVisibility()) {
				this.addActionError(this.getText("Errors.subsite.delete.visible", new String[]{this.getCode()}));
				return INPUT;
			}
			final Thread t = new Thread(new Runnable() {
				public void run() {
					executeDeleteSubsite();
				}
			});
			t.start();
			this.getSubsiteManager().removeSubsite(this.getCode());
		} catch (Throwable t) {
			_logger.error("Error deleting subsite", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	protected void executeDeleteSubsite() {
		try {
			_logger.info("Init delete Subsite " + this.getCode() + " .....");
			Subsite subsite = this.getSubsiteManager().getSubsite(this.getCode());
			Page root = (Page) this.getPageManager().getDraftPage(subsite.getHomepage());
			List<IPage> pagesToDelete = new ArrayList<IPage>();
			this.extractChildrenPags(root, pagesToDelete);
			Collections.reverse(pagesToDelete);
			for (int i = 0; i < pagesToDelete.size(); i++) {
				Page page = (Page) pagesToDelete.get(i);
				Widget[] widgets = page.getWidgets();
				page.setWidgets(new Widget[widgets.length]);
				this.getPageManager().updatePage(page);
			}
			root.setWidgets(new Widget[root.getWidgets().length]);
			this.getPageManager().updatePage(root);

			List<Content> contents = this.getContentsBySubsite(subsite);
			for (int i = 0; i < contents.size(); i++) {
				Content content = contents.get(i);
				this.getContentManager().removeOnLineContent(content);
				this.getContentManager().deleteContent(content);
			}
			List<ResourceInterface> resources = this.getResourcesBySubsite(subsite);
			for (int i = 0; i < resources.size(); i++) {
				ResourceInterface resource = resources.get(i);
				this.getResourceManager().deleteResource(resource);
			}
			for (int i = 0; i < pagesToDelete.size(); i++) {
				IPage page = pagesToDelete.get(i);
				this.getPageManager().deletePage(page.getCode());
			}
			this.getPageManager().deletePage(root.getCode());
			this.getCategoryManager().deleteCategory(subsite.getCategoryCode());
			_logger.info("Subsite " + this.getCode() + " DELETED");
		} catch (ApsSystemException t) {
			_logger.error("Error executing delete subsite", t);
		}
	}

	protected Subsite createSubsite() throws ApsSystemException {
		Subsite subsite = this.getSubsite();
		if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
			Subsite originarySubsite = this.getSubsiteManager().getSubsite(subsite.getCode());
			subsite.setHomepage(originarySubsite.getHomepage());
			subsite.setGroupName(originarySubsite.getGroupName());
			subsite.setCategoryCode(originarySubsite.getCategoryCode());
			subsite.setCssFileName(originarySubsite.getCssFileName());
			subsite.setImageFileName(originarySubsite.getImageFileName());
		} else {
			subsite.setHomepage(subsite.getCode());
			subsite.setCategoryCode(subsite.getCode());
			subsite.setContentViewerPage(null);
		}
		return subsite;
	}

	public List<IPage> getSubsitePages() {
		return this.getSubsitePages(true);
	}

	public List<IPage> getSubsitePages(boolean publicPages) {
		IPage root = (publicPages)
				? this.getPageManager().getOnlinePage(this.getSubsite().getHomepage())
				: this.getPageManager().getDraftPage(this.getSubsite().getHomepage());
		List<IPage> pages = new ArrayList<IPage>();
		if (null == root) {
			return pages;
		}
		this.addPages(root, pages);
		return pages;
	}

	public List<Group> getGroupsList() {
		List<Group> groupList = new ArrayList<Group>();
		groupList.addAll(this.getGroupManager().getGroups());
		BeanComparator c = new BeanComparator("description");
		Collections.sort(groupList, c);
		return groupList;
	}

	public Group getGroup(String groupName) {
		return this.getGroupManager().getGroup(groupName);
	}

	public List<Group> getGroupsForNewSubsite() {
		List<Group> groupsForNew = new ArrayList<Group>();
		try {
			Collection<Subsite> subsites = this.getSubsiteManager().getSubsites();
			List<Group> groups = this.getGroupsList();
			for (int i = 0; i < groups.size(); i++) {
				Group group = groups.get(i);
				boolean check = false;
				Iterator<Subsite> iter = subsites.iterator();
				while (iter.hasNext()) {
					Subsite subsite = iter.next();
					String groupCode = (null != subsite) ? subsite.getGroupName() : null;
					if (group.getName().equals(groupCode)) {
						check = true;
					}
				}
				if (!check) {
					groupsForNew.add(group);
				}
			}
		} catch (Exception e) {
			_logger.error("Error extracting groups", e);
		}
		return groupsForNew;
	}

	private void addPages(IPage page, List<IPage> pages) {
		pages.add(page);
		IPage[] children = page.getChildren();
		for (int i = 0; i < children.length; i++) {
			this.addPages(children[i], pages);
		}
	}

	public Content cleanCategory(Content content) {
		Content toReturn = content;
		toReturn.getCategories().removeAll(content.getCategories());
		return toReturn;
	}

	/**
	 * Metodo a servizio della costruzione dell'albero delle pagine. Verifica se
	 * l'utente corrente è abilitato alla visualizzazione della pagina
	 * specificata.
	 *
	 * @param page La pagina da analizzare.
	 * @return True nel caso che l'utente corrente sia abilitato alla
	 * visualizzazione, false in caso contrario.
	 */
	public boolean isVisible(IPage page) {
		return this.isUserAllowed(page);
	}

	/**
	 * Metodo a servizio della costruzione dell'albero delle pagine. Verifica se
	 * il nodo specificato possiede una pagina figlio al quale l'utente corrente
	 * è abilitato alla visualizzazione.
	 *
	 * @param page La pagina da analizzare.
	 * @return True nel caso che il nodo specificato possiede una pagina figlio
	 * al quale l'utente corrente è abilitato alla visualizzazione.
	 */
	public boolean hasChildVisible(IPage page) {
		for (int i = 0; i < page.getChildren().length; i++) {
			IPage child = page.getChildren()[i];
			if (this.isUserAllowed(child)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifica se l'utente corrente è abilitato all'accesso della pagina
	 * specificata.
	 *
	 * @param page La pagina su cui effettuare la verifica.
	 * @return True se l'utente corrente è abilitato all'accesso della pagina
	 * specificata, false in caso contrario.
	 */
	protected boolean isUserAllowed(IPage page) {
		if (page == null) {
			return false;
		}
		String pageGroup = page.getGroup();
		return this.isCurrentUserMemberOf(pageGroup);
	}

	/**
	 * Restituisce la lista delle lingue di sistema. La lingua di default è in
	 * prima posizione.
	 *
	 * @return La lista delle lingue di sstema.
	 */
	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}

	/**
	 * Restutuisce la root delle categorie.
	 *
	 * @return La root delle categorie.
	 */
	public Category getCategoryRoot() {
		String categoryCode = this.getSubsiteManager().getCategoriesRoot();
		Category category = null;
		if (categoryCode != null) {
			category = this.getCategoryManager().getCategory(categoryCode);
		}
		if (category == null) {
			category = this.getCategoryManager().getRoot();
		}
		return category;
	}

	public Category getCategory(String categoryCode) {
		return this.getCategoryManager().getCategory(categoryCode);
	}

	/**
	 * Restituisce una lista piatta delle categorie disponibili, ordinate
	 * secondo la gerarchia dell'albero delle categorie. La categoria root non
	 * viene inclusa nella lista.
	 *
	 * @return La lista piatta delle categorie disponibili.
	 */
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		Category root = this.getCategoryRoot();
		if (null != root) {
			for (Category category : root.getChildren()) {
				this.addCategories(categories, category);
			}
		}
		return categories;
	}

	private void addCategories(List<Category> categories, Category category) {
		categories.add(category);
		for (int i = 0; i < category.getChildren().length; i++) {
			this.addCategories(categories, (Category) category.getChildren()[i]);
		}
	}

	public String getCssUrl() {
		String cssPath = ISubsiteManager.CSS_SUBFOLDER + File.separator + this.getSubsite().getCssFileName();
		return this.getStorageManager().getResourceUrl(cssPath, false);
	}

	public String getHeaderUrl() {
		String imagePath = ISubsiteManager.HEADER_SUBFOLDER + File.separator + this.getSubsite().getImageFileName();
		return this.getStorageManager().getResourceUrl(imagePath, false);
	}

	public List<SmallEntityType> getContentTypes() {
		return this.getContentManager().getSmallEntityTypes();
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		this._code = code;
	}

	public Subsite getSubsite() {
		return _subsite;
	}

	public void setSubsite(Subsite subsite) {
		this._subsite = subsite;
	}

	public String getCssFileText() {
		return _cssFileText;
	}

	public void setCssFileText(String cssFileText) {
		this._cssFileText = cssFileText;
	}

	public boolean getVisibility() {
		return _isOn;
	}

	public void setVisibility(boolean visibility) {
		this._isOn = visibility;
	}

	public File getUploadHeader() {
		return _uploadHeader;
	}

	public void setUploadHeader(File uploadHeader) {
		this._uploadHeader = uploadHeader;
	}

	protected InputStream getUploadInputStream() throws Throwable {
		if (null == this.getUploadHeader()) {
			return null;
		}
		return new FileInputStream(this.getUploadHeader());
	}

	public String getUploadHeaderContentType() {
		return _uploadHeaderContentType;
	}

	public void setUploadHeaderContentType(String uploadHeaderContentType) {
		this._uploadHeaderContentType = uploadHeaderContentType;
	}

	public String getUploadHeaderFileName() {
		return _uploadHeaderFileName;
	}

	public void setUploadHeaderFileName(String uploadHeaderFileName) {
		this._uploadHeaderFileName = uploadHeaderFileName;
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

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}

	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}

	protected IStorageManager getStorageManager() {
		return this._storageManager;
	}

	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}

	public IResourceManager getResourceManager() {
		return _resourceManager;
	}

	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}

	public IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}

	protected IPageActionHelper getPageActionHelper() {
		return this._pageActionHelper;
	}

	public void setPageActionHelper(IPageActionHelper pageActionHelper) {
		this._pageActionHelper = pageActionHelper;
	}

}
