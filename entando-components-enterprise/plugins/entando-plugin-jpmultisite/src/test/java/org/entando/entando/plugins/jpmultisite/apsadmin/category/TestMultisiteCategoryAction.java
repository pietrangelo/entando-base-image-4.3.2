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
package org.entando.entando.plugins.jpmultisite.apsadmin.category;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.category.CategoryAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;

/**
 * @author S.Loru
 */
public class TestMultisiteCategoryAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() throws Exception {
		this.getRequest().getSession().setAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE, "test");
		this._categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
		deleteAllTestCategories();
		this._multisiteManager = (IMultisiteManager) this.getService(JpmultisiteSystemConstants.MULTISITE_MANAGER);
	}
	
	private void deleteAllTestCategories() throws Exception { 
		List<Category> categoriesList = this._categoryManager.getCategoriesList();
		for (int i = 0; i < categoriesList.size(); i++) {
			Category category = categoriesList.get(i);
			if(category.getCode().contains("test")){
				_categoryManager.deleteCategory(category.getCode());
			}
		}
	}
	
	public void testNewCategory() throws Throwable {
		this.setUserOnSession("admin");
		assertEquals("test", this.getRequest().getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE));
		this.initAction("/do/jpmultisite/Category", "new");
		String result = this.executeAction();
		assertEquals("categoryTree", result);
		CategoryAction action = (CategoryAction) this.getAction();
		assertEquals(1, action.getActionErrors().size());

		this.setUserOnSession("admin");
		this.initAction("/do/jpmultisite/Category", "new");
		this.addParameter("selectedNode", this._multisiteManager.getRootCategory().getCode());
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		action = (CategoryAction) this.getAction();
		assertEquals(this._multisiteManager.getRootCategory().getCode(), action.getParentCategoryCode());
		assertEquals(0, action.getTitles().size());
		assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
	}

	
	public void testAddCategory_1() throws Throwable {
		String categoryCode = "cat_temp";
		String categoryCodeTest = "cat_temp@test";
		assertNull(this._categoryManager.getCategory(categoryCode));
		try {
			String result = this.saveNewCategory("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			Category category = this._categoryManager.getCategory(categoryCodeTest);
			assertNotNull(category);
			assertEquals("Titolo categoria In Italiano", category.getTitles().getProperty("it"));
			assertEquals(this._multisiteManager.getRootCategoryCode(), category.getParent().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._categoryManager.deleteCategory(categoryCode);
			assertNull(this._categoryManager.getCategory(categoryCode));
		}
	}
	
	public void testDeleteCategory_1() throws Throwable {
		String categoryCode = "cat_temp";
		String categoryCodeTest = "cat_temp@test";
		assertNull(this._categoryManager.getCategory(categoryCode));
		try {
			String result = this.saveNewCategory("admin", categoryCode);
			assertEquals(Action.SUCCESS, result);
			Category category = this._categoryManager.getCategory(categoryCodeTest);
			assertNotNull(category);
			
			this.initAction("/do/jpmultisite/Category", "trash");
			
			this.addParameter("selectedNode", categoryCodeTest);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			category = this._categoryManager.getCategory(categoryCodeTest);
			assertNotNull(category);
			Map<String, Object> references = ((CategoryAction) this.getAction()).getReferences();
			assertNull(references);
			
			this.initAction("/do/jpmultisite/Category", "delete");
			this.addParameter("selectedNode", categoryCodeTest);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			category = this._categoryManager.getCategory(categoryCodeTest);
			assertNull(category);
			references = ((CategoryAction) this.getAction()).getReferences();
			assertNull(references);
		} catch (Throwable t) {
			this._categoryManager.deleteCategory(categoryCode);
			assertNull(this._categoryManager.getCategory(categoryCode));
			throw t;
		}
	}

	private String saveNewCategory(String username, String categoryCode) throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("parentCategoryCode", this._multisiteManager.getRootCategoryCode());
		params.put("strutsAction", "1");
		params.put("categoryCode", categoryCode);
		params.put("langit", "Titolo categoria In Italiano");
		params.put("langen", "Titolo categoria In Inglese");
		String result = this.executeSaveCategory(username, params);
		return result;
	}

	private String executeSaveCategory(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpmultisite/Category", "save");
		this.addParameters(params);
		return this.executeAction();
	}
	
	
	private ICategoryManager _categoryManager;
	private IMultisiteManager _multisiteManager;

}
