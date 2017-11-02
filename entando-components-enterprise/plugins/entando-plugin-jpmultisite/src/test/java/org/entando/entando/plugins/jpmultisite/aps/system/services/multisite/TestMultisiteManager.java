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
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import java.util.Collections;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpmultisite.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.MultisiteConfigTestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class TestMultisiteManager extends ApsPluginBaseTestCase {
	
	private static final Logger _logger = LoggerFactory.getLogger(TestMultisiteManager.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	private void init() throws Exception {
		_logger.info(this.getClass().getName() + " init");
		IMultisiteManager multisiteManager = (IMultisiteManager) this.getService(JpmultisiteSystemConstants.MULTISITE_MANAGER);
		ICategoryManager categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
		IWidgetTypeManager widgetTypeManager = (IWidgetTypeManager) this.getService("jpMultisiteWidgetTypeManager");
		assertNotNull(multisiteManager);
		assertNotNull(categoryManager);
		this._multisiteManager = multisiteManager;
		this._categoryManager = categoryManager;
		this._widgetTypeManager = widgetTypeManager;
		MultisiteTestHelper.addMultisiteForTesting(_multisiteManager);
	}
	

	public void testWidget() throws Exception {
		List<WidgetType> widgetTypes = this._widgetTypeManager.getWidgetTypes();
		int count = 0;
		BeanComparator comparator = new BeanComparator("code");
		Collections.sort(widgetTypes, comparator);
		for (WidgetType widgetType : widgetTypes) {
			_logger.info("*** type ***   {}", widgetType.getCode());
			if (widgetType.getCode().endsWith("@asd")) {
				count++;
			}
		}
		assertEquals(count, widgetTypes.size() / 3);
	}

	public void testSiteByUrl() throws Exception {
		List<String> loadMultisites = this._multisiteManager.loadMultisites();
		assertEquals(2, loadMultisites.size());
		String loadMultisiteByUrl = this._multisiteManager.loadMultisiteByUrl("http://test.com");
		assertEquals("asd", loadMultisiteByUrl);
		String loadMultisiteByUrl_2 = this._multisiteManager.loadMultisiteByUrl("http://testp.com");
		assertEquals("asd2", loadMultisiteByUrl_2);
		String loadMultisiteByUrl_3 = this._multisiteManager.loadMultisiteByUrl("http://google.com");
		assertEquals("", loadMultisiteByUrl_3);
		String loadMultisiteByUrl_4 = this._multisiteManager.loadMultisiteByUrl("http://127.0.0.1:8080");
		assertEquals("", loadMultisiteByUrl_4);
	}

	public void testDeleteMultisite() throws ApsSystemException {
		try {
			Multisite asdMultisite = this._multisiteManager.loadMultisite("asd");
			assertNotNull(asdMultisite);
			Category categoryAsd = this._categoryManager.getCategory("asd");
			assertNotNull(categoryAsd);
			addCategories("asd");
			this._multisiteManager.deleteMultisite("asd");
			asdMultisite = this._multisiteManager.loadMultisite("asd");
			assertNull(asdMultisite);
			categoryAsd = this._categoryManager.getCategory("asd");
			assertNull(categoryAsd);
		} catch (Throwable t) {
			_logger.error("Error deleting multisite", t);
			throw new ApsSystemException("Error deleting Multisite", t);
		}
	}

	private void addCategories(String code) throws ApsSystemException {
		try {
			Multisite asdMultisite = _multisiteManager.loadMultisite(code);
			List<Category> categoriesList = _categoryManager.getCategoriesList();
			assertNotNull(categoriesList);
			int size = categoriesList.size();
			Category testCategory = new Category();
			testCategory.setCode("test@asd");
			testCategory.setTitles(asdMultisite.getTitles());
			testCategory.setParent(_categoryManager.getCategory(asdMultisite.getCatCode()));
			testCategory.setParentCode("asd");

			_categoryManager.addCategory(testCategory);

			Category testChild1Category = new Category();
			testChild1Category.setCode("test1@asd");
			testChild1Category.setTitles(asdMultisite.getTitles());
			testChild1Category.setParent(_categoryManager.getCategory("test@asd"));
			testChild1Category.setParentCode("test@asd");
			_categoryManager.addCategory(testChild1Category);
			for (int i = 2, j = 1; i < 10; i++, j++) {
				Category testChild2Category = new Category();
				testChild2Category.setCode("test" + i + "@asd");
				testChild2Category.setTitles(asdMultisite.getTitles());
				testChild2Category.setParent(_categoryManager.getCategory("test" + j + "@asd"));
				testChild2Category.setParentCode("test" + j + "@asd");
				_categoryManager.addCategory(testChild2Category);
			}
			categoriesList = _categoryManager.getCategoriesList();
			assertEquals(size + 10, categoriesList.size());
		} catch (Throwable t) {
			_logger.error("Error adding category", t);
			throw new ApsSystemException("Error adding category", t);
		}
	}
	
	@Test(expected=ApsSystemException.class)
	public void testMultisiteClone() throws ApsSystemException {
		Multisite asdMultisite = null;
		Multisite asd2Multisite = null;
		try{
		asdMultisite = _multisiteManager.loadMultisite("asd");
		asd2Multisite = _multisiteManager.loadMultisite("asd2");
		} catch (Throwable t){
			_logger.error("error loading multisite", t);
		}
		try{
		_multisiteManager.cloneMultisite(asdMultisite, asd2Multisite, null);
		} catch (Throwable t) {
			assertEquals(ApsSystemException.class, t.getClass());
		}
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
	private IWidgetTypeManager _widgetTypeManager;

}
