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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupManager;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.ActionInvocation;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeManager;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.event.MultisiteChangedEvent;
import org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype.IMultisiteWidgetTypeManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
@Aspect
public class MultisiteManager extends AbstractService implements IMultisiteManager {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteManager.class);

	@Override
	public void init() throws Exception {
		this.checkCategoryRoot();
		this._multisites = this.getMultisiteDAO().loadMultisitesMap();
		_logger.debug("{} ready.", this.getClass().getName());
	}

	@Around("execution(* com.agiletec.apsadmin.system.BaseInterceptorMadMax.intercept(..)) && args(invocation)")
	public Object checkUserAllowedForMultisite(ProceedingJoinPoint pjp, ActionInvocation invocation) throws Throwable {
		_logger.debug("checkUserAllowedForMultisite");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		if(currentUser != null && currentUser.getUsername().contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)){
			if(!invocation.getProxy().getNamespace().contains("jpmultisite") && !("update".equals(invocation.getProxy().getActionName()) && "/do/ActivityStream".equals(invocation.getProxy().getNamespace()))){
				_logger.debug("returning " + JpmultisiteSystemConstants.MULTISITE_DEFAULT_ERROR_RESULT);
				return BaseAction.FAILURE;
			} 
		}
		return pjp.proceed();
	}

	@Override
	public String loadMultisiteByUrl(String url) throws ApsSystemException {
		Map<String, Multisite> multisites = this._multisites;
		String multisiteCode = "";
		try {
			URI currentURL = new URI(url);
			for (Map.Entry<String, Multisite> entry : multisites.entrySet()) {
				String code = entry.getKey();
				Multisite multisite = entry.getValue();
				URI multisiteURL = new URI(multisite.getUrl());
				if (StringUtils.equals(multisiteURL.getHost(), currentURL.getHost())) {
					multisiteCode = code;
					break;
				}
			}
		} catch (Throwable t) {
			_logger.error("Error loading multisite by url {}", url, t);
			throw new ApsSystemException("Error loading multisite by url: " + url, t);
		}
		return multisiteCode;
	}

	private void checkCategoryRoot() {
		Category categoryRoot = this.getCategoryManager().getCategory(this.getRootCategoryCode());
		if (null == categoryRoot) {
			try {
				categoryRoot = new Category();
				categoryRoot.setCode(this.getRootCategoryCode());
				Category root = this.getCategoryManager().getRoot();
				ApsProperties titles = new ApsProperties();
				Set<Object> langCodes = root.getTitles().keySet();
				Iterator<Object> iter = langCodes.iterator();
				while (iter.hasNext()) {
					Object langCode = (Object) iter.next();
					titles.put(langCode, "Multisite Root");
				}
				categoryRoot.setTitles(titles);
				categoryRoot.setParent(root);
				categoryRoot.setParentCode(root.getCode());
				this.getCategoryManager().addCategory(categoryRoot);
				_logger.debug("Multisite category root Created ");
			} catch (Throwable t) {
				_logger.error("Error on adding Multisite category root", t);
				throw new RuntimeException("Error on adding Multisite category root", t);
			}
		}
		_logger.debug("Multisite categoryRoot");
		this.setRootCategory(categoryRoot);

	}

	@Override
	public Multisite loadMultisite(String code) throws ApsSystemException {
		Multisite multisite = null;
		try {
			multisite = this.getMultisiteDAO().loadMultisite(code);
		} catch (Throwable t) {
			_logger.error("Error loading multisite with code '{}'", code, t);
			throw new ApsSystemException("Error loading multisite with code: " + code, t);
		}
		return multisite;
	}

	@Override
	public List<String> loadMultisites() throws ApsSystemException {
		List<String> multisites = new ArrayList<String>();
		try {
			multisites = this.getMultisiteDAO().loadMultisitesCode();
		} catch (Throwable t) {
			_logger.error("Error loading Multisite list", t);
			throw new ApsSystemException("Error loading Multisite ", t);
		}
		return multisites;
	}

	@Override
	public List<String> searchMultisites(FieldSearchFilter filters[]) throws ApsSystemException {
		List<String> multisites = new ArrayList<String>();
		try {
			multisites = this.getMultisiteDAO().searchMultisites(filters);
		} catch (Throwable t) {
			_logger.error("Error searching Multisites", t);
			throw new ApsSystemException("Error searching Multisites", t);
		}
		return multisites;
	}

	@Override
	public void addMultisite(Multisite multisite, UserDetails user) throws ApsSystemException {
		try {
			//int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			//multisite.setCode(Integer.toString(key));
			this.getCategoryManager().addCategory(createCategoryForMultisite(multisite));
			this.duplicateWidgetType(multisite);
			this.getMultisiteDAO().insertMultisite(multisite);
			this.notifyMultisiteChangedEvent(multisite, MultisiteChangedEvent.INSERT_OPERATION_CODE);
			Group multisiteGroup = this.addUserAndPermissions(multisite, user);
			addNewPage(multisiteGroup, multisite.getCode());
			refresh();
		} catch (Throwable t) {
			this.getCategoryManager().deleteCategory(multisite.getCatCode());
			deleteUserByMultisiteCode(multisite.getCode());
			deleteAllCategoriesRelated(multisite.getCode());
			deleteWidgetType(multisite.getCode());
			_logger.error("Error adding Multisite", t);
			throw new ApsSystemException("Error adding Multisite", t);
		}
	}

	//FIX create a rollback method
	@Override
	public void cloneMultisite(Multisite source, Multisite clone, UserDetails user) throws ApsSystemException {
		try{
			if(this.loadMultisite(clone.getCode()) != null) {
				_logger.error("Error cloning multisite - a multisite with that code already exists");
				throw new ApsSystemException("Error cloning multisite - a multisite with that code already exists");
			}
			addMultisite(clone, user);
			this.getMultisiteCloneManager().cloneGroups(source, clone);
			this.getMultisiteCloneManager().cloneCategories(source, clone);
			this.getMultisiteCloneManager().cloneWidgets(source, clone);
			((WidgetTypeManager) this.getJacmsWidgetTypeManager()).init();
			this.getMultisiteCloneManager().clonePages(source, clone);
			this.getMultisiteCloneManager().cloneResources(source, clone);
			this.getMultisiteCloneManager().cloneContents(source, clone);
		} catch (Throwable t) {
			_logger.error("Error cloning multisite", t);
			this.deleteMultisite(clone.getCode());
			throw new ApsSystemException("Error cloning multisite", t);
		}
	}
	
	private void addNewPage(Group group, String multisiteCode) throws ApsSystemException{
		IPage page = null;
		try{
			page = buildNewPage(group, multisiteCode);
			this.getPageManager().addPage(page);
		} catch (Throwable t) {
			_logger.error("Error adding Multisite Root page", t);
			throw new ApsSystemException("Error adding Multisite Root page", t);
		}
	}
	
	private IPage buildNewPage(Group group, String multisiteCode) throws ApsSystemException {
		Page page = new Page();
		try {
			page.setParent(this.getPageManager().getPage(this._configManager.getParam(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE)));
			page.setGroup(group.getName());
			page.setShowable(true);
			page.setUseExtraTitles(false);
			PageModel pageModel = this.getPageModelManager().getPageModel(JpmultisiteSystemConstants.MULTISITE_PAGEMODEL_HOME);
			page.setModel(pageModel);
			page.setWidgets(new Widget[pageModel.getFrames().length]);
			page.setTitles(getPageTitles(multisiteCode));
			page.setCode("home"+JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR+multisiteCode);
			page.setCharset(null);
			page.setMimeType(null);
		} catch (Throwable t) {
			_logger.error("Error building new page", t);
			throw new ApsSystemException("Error building new page", t);
		}
		return page;
	}
	
	private ApsProperties getPageTitles(String multisiteCode){
		ApsProperties properties = new ApsProperties();
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String code = lang.getCode();
			properties.put(code, "Home for multisite " + multisiteCode);
		}
		return properties;
	}
	
	private Group addUserAndPermissions(Multisite multisite, UserDetails user) throws ApsSystemException {
		Group group = new Group();
		try {
			this.getUserManager().addUser(user);
		} catch (Throwable t) {
			_logger.error("Error adding Multisite User", t);
			throw new ApsSystemException("Error adding Multisite User", t);
		}
		try {
			String groupName = MultisiteUtils.getRootGroupForMultisite(multisite.getCode());
			group.setName(groupName);
			group.setDescription("Group for multisite with code: " + multisite.getCode());
			this.getGroupManager().addGroup(group);
			//Group addedGroup = this.getGroupManager().getGroup(groupName);
			//this.getGroupManager().setUserAuthorization(user.getUsername(), authority);
			//Role role = this.getRoleManager().getRole("admin");
			//((IApsAuthorityManager) this.getRoleManager()).setUserAuthorization(user.getUsername(), role);
			this.getAuthorizationManager().addUserAuthorization(user.getUsername(), groupName, "admin");
		} catch (Throwable t) {
			this.getUserManager().removeUser(user);
			_logger.error("Error granting Multisite User permissions", t);
			throw new ApsSystemException("Error granting Multisite User permissions", t);
		}
		return group;
	}
	
	private void duplicateWidgetType(Multisite multisite) throws ApsSystemException {
		try {
			if (StringUtils.isNotBlank(multisite.getCode())) {
				List<WidgetType> widgetTypes = this.getWidgetTypeManager().getWidgetTypes();
				for (WidgetType widgetType : widgetTypes) {
					if (!widgetType.getCode().contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)) {
						widgetType.setCode(widgetType.getCode() + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + multisite.getCode());
						this.getWidgetTypeManager().addWidgetType(widgetType);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error duplicating widgetType", t);
			throw new ApsSystemException("Error duplicating widgetType", t);
		}
	}

	//FIXIT ACTUALLY WIDGET CANNOT BE DELETED
	private void deleteWidgetType(String multisiteCode) throws ApsSystemException {
		try {
			if (StringUtils.isNotBlank(multisiteCode)) {
				List<WidgetType> widgetTypes = this.getWidgetTypeManager().getWidgetTypes();
				for (WidgetType widgetType : widgetTypes) {
					if (widgetType.getCode().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + multisiteCode)) {
						widgetType.setLocked(false);
						
						this.getWidgetTypeManager().deleteWidgetType(widgetType.getCode());
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error deleting widgetType", t);
			throw new ApsSystemException("Error deleting widgetType", t);
		}
	}

	private Category createCategoryForMultisite(Multisite multisite) {
		Category multisiteCategory = new Category();
		multisiteCategory.setCode(multisite.getCode());
		multisiteCategory.setTitles(multisite.getTitles());
		multisiteCategory.setParent(this.getRootCategory());
		multisiteCategory.setParentCode(this.getRootCategory().getCode());
		return multisiteCategory;
	}

	@Override
	public void updateMultisite(Multisite multisite, UserDetails user) throws ApsSystemException {
		try {
			this.getMultisiteDAO().updateMultisite(multisite);
			if (null != user) {
				User updatedUser = (User) this.getUserManager().getUser(user.getUsername());
				updatedUser.setPassword(user.getPassword());
				this.getUserManager().updateUser(updatedUser);
			}
			this.notifyMultisiteChangedEvent(multisite, MultisiteChangedEvent.UPDATE_OPERATION_CODE);
			refresh();
		} catch (Throwable t) {
			_logger.error("Error updating Multisite", t);
			throw new ApsSystemException("Error updating Multisite " + multisite, t);
		}
	}

	@Override
	public void deleteMultisite(String code) throws ApsSystemException {
		try {
			Multisite multisite = this.loadMultisite(code);
			this.getMultisiteDAO().removeMultisite(code);
			if(StringUtils.isNotBlank(multisite.getHeaderImage())){
				this.getStorageManager().deleteFile(multisite.getHeaderImage(), false);
			}
			if(StringUtils.isNotBlank(multisite.getTemplateCss())){
				this.getStorageManager().deleteFile(multisite.getTemplateCss(), false);
			}
			deleteUserByMultisiteCode(code);
			deleteAllCategoriesRelated(code);
			deleteWidgetType(code);
			deleteGroupByMultisite(code);
			deletePage(code);
			this.notifyMultisiteChangedEvent(multisite, MultisiteChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting Multisite with code {}", code, t);
			throw new ApsSystemException("Error deleting Multisite with code:" + code, t);
		}
	}

	private void deletePage(String code) throws ApsSystemException{
		try {
			this.getPageManager().deletePage("home"+JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR+code);
		} catch (Throwable t) {
			_logger.error("Error deleting pages for Multisite with code {}", code, t);
			throw new ApsSystemException("Error deleting pages for Multisite with code:" + code, t);
		}
	}
	
	private void deleteGroupByMultisite(String code) throws ApsSystemException {
		try {
			List<Group> groups = this.getGroupManager().getGroups();
			for (Group group : groups) {
				if(group.getName().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR+code)){
					this.getGroupManager().removeGroup(group);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error deleting group for multisite with code {}", code, t);
			throw new ApsSystemException("Error deleting group for multisite with code:" + code, t);
		}
	}

	private void deleteAllCategoriesRelated(String catCode) throws ApsSystemException {
		Category category = this.getCategoryManager().getCategory(catCode);
		_logger.info("catcode: {}", catCode);
		while (this.getCategoryManager().getCategory(catCode) != null && this.getCategoryManager().getCategory(catCode).getChildren() != null && this.getCategoryManager().getCategory(catCode).getChildren().length > 0) {
			Category[] children = category.getChildren();
			_logger.info("children.length {}", children.length);
			for (int i = 0; i < children.length; i++) {
				Category categoryChild = children[i];
				_logger.info("categoryChild: {}", categoryChild.getCode());
				deleteAllCategoriesRelated(categoryChild.getCode());
			}
		}
		_logger.info("deleting cat: {}", catCode);
		this.getCategoryManager().deleteCategory(catCode);
	}

	private void deleteUserByMultisiteCode(String code) throws ApsSystemException {
		List<String> usernames = this.getUserManager().getUsernames();
		for (int i = 0; i < usernames.size(); i++) {
			String username = usernames.get(i);
			if (username.endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + code)) {
				try {
					/*
					List<IApsAuthority> authorizationsByUser = this.getGroupManager().getAuthorizationsByUser(username);
					for (IApsAuthority iApsAuthority : authorizationsByUser) {
						this.getGroupManager().removeUserAuthorization(username, iApsAuthority);
					}
					*/
					this.getAuthorizationManager().deleteUserAuthorizations(username);
					this.getUserManager().removeUser(username);
				} catch (Throwable t) {
					_logger.error("Error deleting user {}", username, t);
					throw new ApsSystemException("Error deleting Multisite with username:" + username, t);
				}
			}
		}
	}

	private void notifyMultisiteChangedEvent(Multisite multisite, int operationCode) {
		MultisiteChangedEvent event = new MultisiteChangedEvent();
		event.setMultisite(multisite);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}

	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setMultisiteDAO(IMultisiteDAO multisiteDAO) {
		this._multisiteDAO = multisiteDAO;
	}

	protected IMultisiteDAO getMultisiteDAO() {
		return _multisiteDAO;
	}

	public ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	public Map<String, Multisite> getMultisites() {
		return _multisites;
	}

	public void setMultisites(Map<String, Multisite> multisites) {
		this._multisites = multisites;
	}

	public ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	public Category getRootCategory() {
		return _rootCategory;
	}

	public void setRootCategory(Category rootCategory) {
		this._rootCategory = rootCategory;
	}
	
	@Override
	public String getRootCategoryCode() {
		if (null == this._rootCategoryCode) {
			return DEFAULT_CATEGORY_ROOT;
		}
		return _rootCategoryCode;
	}

	public void setRootCategoryCode(String rootCategoryCode) {
		this._rootCategoryCode = rootCategoryCode;
	}

	public IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	public IMultisiteWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IMultisiteWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	protected GroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(GroupManager groupManager) {
		this._groupManager = groupManager;
	}
	/*
	protected IRoleManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IRoleManager roleManager) {
		this._roleManager = roleManager;
	}
	*/
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	public IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}

	public ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	public IStorageManager getStorageManager() {
		return _storageManager;
	}
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}

	public IMultisiteCloneManager getMultisiteCloneManager() {
		return _multisiteCloneManager;
	}
	public void setMultisiteCloneManager(IMultisiteCloneManager multisiteCloneManager) {
		this._multisiteCloneManager = multisiteCloneManager;
	}

	public IWidgetTypeManager getJacmsWidgetTypeManager() {
		return _jacmsWidgetTypeManager;
	}
	public void setJacmsWidgetTypeManager(IWidgetTypeManager jacmsWidgetTypeManager) {
		this._jacmsWidgetTypeManager = jacmsWidgetTypeManager;
	}
	
	private Map<String, Multisite> _multisites = new HashMap<String, Multisite>();

	private ConfigInterface _configManager;
	private String _rootCategoryCode;
	private IMultisiteDAO _multisiteDAO;

	private Category _rootCategory;
	public static final String DEFAULT_CATEGORY_ROOT = "jpmultisite_categoryRoot";

	private IUserManager _userManager;
	private IKeyGeneratorManager _keyGeneratorManager;
	private ICategoryManager _categoryManager;
	private IMultisiteWidgetTypeManager _widgetTypeManager;

	private GroupManager _groupManager;
	//private IRoleManager _roleManager;
	private IAuthorizationManager _authorizationManager;
	
	private IPageManager _pageManager;
	private IPageModelManager _pageModelManager;
	private ILangManager _langManager;
	private IStorageManager _storageManager;
	private IMultisiteCloneManager _multisiteCloneManager;
	private IWidgetTypeManager _jacmsWidgetTypeManager;
	
}