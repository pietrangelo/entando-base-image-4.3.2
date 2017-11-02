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

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.mock.MockResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertNotNull;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpmultisite.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.content.MultisiteContentManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.resource.MultisiteResourceManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype.MultisiteWidgetTypeManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.MultisiteConfigTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class TestMultisiteCloneManager  extends ApsPluginBaseTestCase {
	
	private static final Logger _logger = LoggerFactory.getLogger(TestMultisiteCloneManager.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() throws Exception {
		_logger.info(this.getClass().getName() + " init");
		IMultisiteManager multisiteManager = (IMultisiteManager) this.getService(JpmultisiteSystemConstants.MULTISITE_MANAGER);
		ICategoryManager categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
		_widgetTypeManager = (MultisiteWidgetTypeManager) this.getService(JpmultisiteSystemConstants.MULTISITE_WIDGETTYPE_MANAGER);
		_multisiteCloneManager = (IMultisiteCloneManager) this.getService(JpmultisiteSystemConstants.MULTISITE_CLONE_MANAGER);
		_groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
		_resourceManager = (MultisiteResourceManager) this.getService(JpmultisiteSystemConstants.MULTISITE_RESOURCE_MANAGER);
		_multisiteContentManager = (MultisiteContentManager) this.getService(JpmultisiteSystemConstants.MULTISITE_CONTENT_MANAGER);
		assertNotNull(multisiteManager);
		assertNotNull(categoryManager);
		assertNotNull(_groupManager);
		assertNotNull(_resourceManager);
		assertNotNull(_multisiteCloneManager);
		assertNotNull(_widgetTypeManager);
		assertNotNull(_multisiteContentManager);
		this._multisiteManager = multisiteManager;
		this._categoryManager = categoryManager;
		MultisiteTestHelper.addMultisiteForTesting(_multisiteManager);
	}
	
	public void testInit(){
		assertNotNull(_multisiteCloneManager);
		assertNotNull(_groupManager);
	}
	
	public void testCloneGroup() throws ApsSystemException{
		List<Group> groups = _groupManager.getGroups();
		int countAsdGroups = 0;
		for (Group group : groups) {
			if(group.getName().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + "asd")){
				countAsdGroups++;
			}
		}
		assertEquals(countAsdGroups,1);
		countAsdGroups = 0;
		Group newGroup = new Group();
		newGroup.setName("test@asd");
		newGroup.setDescr("test@asd");
		_groupManager.addGroup(newGroup);
		groups = _groupManager.getGroups();
		for (Group group : groups) {
			if(group.getName().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + "asd")){
				countAsdGroups++;
			}
		}
		assertEquals(countAsdGroups,2);
		Multisite tesMultisite = new Multisite("tes", null, null, null, null, null, null);
		Multisite asdMultisite = _multisiteManager.loadMultisite("asd");
		_multisiteCloneManager.cloneGroups(asdMultisite, tesMultisite);
		int countTesGroups = 0;
		groups = _groupManager.getGroups();
		for (Group group : groups) {
			if(group.getName().endsWith(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + "tes")){
				countTesGroups++;
				assertEquals("test@asd", group.getDescr());
				assertEquals("test@tes", group.getName());
				_groupManager.removeGroup(group);
			}
		}
		assertEquals(countTesGroups,1);
	}
	
	public void testCloneCategories() throws ApsSystemException {
		Multisite tesMultisite = new Multisite("tes", null, null, null, null, null, null);
		Multisite asdMultisite = _multisiteManager.loadMultisite("asd");
		Category asdCategory = new Category();
		asdCategory.setCode("test@asd");
		asdCategory.setParentCode("asd");
		ApsProperties apsProperties = new ApsProperties();
		apsProperties.setProperty("en", "asd");
		apsProperties.setProperty("it", "asd");
		asdCategory.setTitles(apsProperties);
		asdCategory.setDefaultLang("it");
		_categoryManager.addCategory(asdCategory);
		int asdCount = 0;
		List<Category> categoriesList = _categoryManager.getCategoriesList();
		for (Category category : categoriesList) {
			if(category.getCode().endsWith(asdMultisite.getCode())){
				_logger.error("category code: {}", category.getCode());
				asdCount++;
			}
		}
		assertEquals(2, asdCount);
		Category tesCategory = new Category();
		tesCategory.setCode("tes");
		tesCategory.setParentCode("jpmultisite_categoryRoot");
		tesCategory.setTitle("en", "testtest");
		tesCategory.setTitle("it", "testtest");
		tesCategory.setDefaultLang("it");
		_categoryManager.addCategory(tesCategory);
		_multisiteCloneManager.cloneCategories(asdMultisite, tesMultisite);
		int tesCount = 0;
		categoriesList = _categoryManager.getCategoriesList();
		for (Category category : categoriesList) {
			if(category.getCode().endsWith(tesMultisite.getCode())){
				tesCount++;
				_categoryManager.deleteCategory(category.getCode());
			}
		}
		_categoryManager.deleteCategory(tesCategory.getCode());
		assertEquals(2, tesCount);
	}
	
	public void testCloneWidgetType() throws ApsSystemException {
		Multisite tesMultisite = new Multisite("tes", null, null, null, null, null, null);
		Multisite asdMultisite = _multisiteManager.loadMultisite("asd");
		List<WidgetType> widgetForAsd = _widgetTypeManager.getWidgetTypesByMultisiteCode("asd");
		_multisiteCloneManager.cloneWidgets(asdMultisite, tesMultisite);
		List<WidgetType> widgetForTes = _widgetTypeManager.getWidgetTypesByMultisiteCode("tes");
		assertEquals(widgetForAsd.size(), widgetForTes.size());
		for (WidgetType widgetType : widgetForTes) {
			_widgetTypeManager.deleteWidgetTypeLocked(widgetType.getCode());
		}
		
	}
	
	public void testCloneResources() throws ApsSystemException {
		EntitySearchFilter[] filtersAsd = new EntitySearchFilter[1];
		EntitySearchFilter[] filtersTes = new EntitySearchFilter[1];
		EntitySearchFilter multisiteAsdFilter = new EntitySearchFilter(IResourceManager.RESOURCE_ID_FILTER_KEY, false, "asd", true);
		EntitySearchFilter multisiteTesFilter = new EntitySearchFilter(IResourceManager.RESOURCE_ID_FILTER_KEY, false, "tes", true);
		filtersAsd[0] = multisiteAsdFilter;
		filtersTes[0] = multisiteTesFilter;
		Multisite tesMultisite = new Multisite("tes", null, null, null, null, null, null);
		Multisite asdMultisite = _multisiteManager.loadMultisite("asd");
		assertNotNull(asdMultisite);
		createResourceForTest(asdMultisite.getCode());
		List<String> resourcesId = _resourceManager.searchResourcesId(filtersAsd, null, null);
		assertEquals(1, resourcesId.size());
		_multisiteCloneManager.cloneResources(asdMultisite, tesMultisite);
		resourcesId = _resourceManager.searchResourcesId(filtersAsd, null, null);
		assertEquals(1, resourcesId.size());
		for (String resourceId : resourcesId) {
			ResourceInterface loadResource = _resourceManager.loadResource(resourceId);
			_resourceManager.deleteResource(loadResource);
		}
		resourcesId = _resourceManager.searchResourcesId(filtersTes, null, null);
		assertEquals(1, resourcesId.size());
		for (String resourceId : resourcesId) {
			ResourceInterface loadResource = _resourceManager.loadResource(resourceId);
			_resourceManager.deleteResource(loadResource);
		}
		resourcesId = _resourceManager.searchResourcesId(filtersAsd, null, null);
		assertEquals(0, resourcesId.size());
		resourcesId = _resourceManager.searchResourcesId(filtersTes, null, null);
		assertEquals(0, resourcesId.size());
	}
	
	private void createResourceForTest(String multisiteCode) throws ApsSystemException {
		List<String> allowedGroups = this.getAllGroupCodes();
    	ResourceInterface res = null;
    	String resDescrToAdd = "Entando Logo";
    	String resourceType = "Image";
    	String categoryCodeToAdd = "resCat1";
		ResourceDataBean bean = this.getMockResource(resourceType, "multisite@asd", resDescrToAdd, categoryCodeToAdd);
		_resourceManager.addResource(bean, multisiteCode);
	}
	
	private List<String> getAllGroupCodes() {
    	List<String> groupCodes = new ArrayList<String>();
    	List<Group> groups = this._groupManager.getGroups();
    	for (int i = 0; i < groups.size(); i++) {
			groupCodes.add(groups.get(i).getName());
		}
    	return groupCodes;
    }
	
	private ResourceDataBean getMockResource(String resourceType, 
    		String mainGroup, String resDescrToAdd, String categoryCodeToAdd) {
    	File file = new File("target/test/entando_logo.jpg");
    	MockResourceDataBean bean = new MockResourceDataBean();
    	bean.setFile(file);
    	bean.setDescr(resDescrToAdd);
    	bean.setMainGroup(mainGroup);
    	bean.setResourceType(resourceType);
    	bean.setMimeType("image/jpeg");
    	List<Category> categories = new ArrayList<Category>();
    	ICategoryManager catManager = 
    		(ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
    	Category cat = catManager.getCategory(categoryCodeToAdd);
    	categories.add(cat);
    	bean.setCategories(categories);
    	return bean;
    }
	
//	public void testClonePages() throws ApsSystemException {
//		Multisite asdMultisite = _multisiteManager.loadMultisite("asd");
//		Multisite asd2Multisite = _multisiteManager.loadMultisite("asd2");
//		_multisiteCloneManager.clonePages(asdMultisite, asd2Multisite);
//		assertNotNull(asdMultisite);
//	}
	
	public void testCloneContents() throws ApsSystemException {
		Multisite asdMultisite = _multisiteManager.loadMultisite("asd");
		Multisite tesMultisite = new Multisite("tes", null, null, null, null, null, null);
		EntitySearchFilter[] filters = new EntitySearchFilter[1];
		EntitySearchFilter multisiteFilter = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false, JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + asdMultisite.getCode(), true);
		filters[0] = multisiteFilter;
		List<String> contentsId = _multisiteContentManager.searchId(filters);
		assertEquals(1, contentsId.size());
		_multisiteCloneManager.cloneContents(asdMultisite, tesMultisite);
		multisiteFilter = new EntitySearchFilter(IContentManager.ENTITY_ID_FILTER_KEY, false, JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + "tes", true);
		filters[0] = multisiteFilter;
		contentsId = _multisiteContentManager.searchId(filters);
		for (String contentId : contentsId) {
			_multisiteContentManager.deleteContent(_multisiteContentManager.loadContent(contentId, false));
		}
		assertEquals(1, contentsId.size());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		MultisiteTestHelper.deleteAllMultisite(_multisiteManager);
	}

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new MultisiteConfigTestUtils();
	}

	private ICategoryManager _categoryManager;
	private IMultisiteManager _multisiteManager;
	private MultisiteWidgetTypeManager _widgetTypeManager;
	private IMultisiteCloneManager _multisiteCloneManager;
	private IGroupManager _groupManager;
	private MultisiteResourceManager _resourceManager;
	private MultisiteContentManager _multisiteContentManager;

}