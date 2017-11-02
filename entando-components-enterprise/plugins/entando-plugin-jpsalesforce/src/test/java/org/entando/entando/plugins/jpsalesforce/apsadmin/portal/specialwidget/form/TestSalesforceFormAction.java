/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.ISalesforceFormManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.SalesforceForm;
import org.entando.entando.plugins.jpsalesforce.apsadmin.ApsAdminPluginBaseTestCase;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormBinding;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;


public class TestSalesforceFormAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInit() throws Throwable {
		IPage page = this.getPageManager().getPage(PAGE_ID);
		Widget widget = page.getWidgets()[FRAME];
		assertNull(widget);
		if (abortTest()) return;
		try {
			setUserOnSession("admin");
			this.initAction("/do/Page/SpecialWidget", "salesforceFormConfig");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("formProtectionType", PROTECTION_TYPE);
			this.addParameter("formDescription", DESCRIPTION);
			String result = this.executeAction();
			assertEquals("success", result);
		} catch (Throwable t) {
			throw t;
		} finally {
			page = this.getPageManager().getPage(PAGE_ID);
			page.getWidgets()[FRAME] = null;
			this.getPageManager().updatePage(page);
		}
	}

	public void testConfigure_1() throws Throwable {
		if (abortTest()) return;
		try {
			setUserOnSession("admin");
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "configure");
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testConfigure_2() throws Throwable {
		try {
			setUserOnSession("admin");
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "configure");
			this.addParameter("campaignId", CAMPAIGN_ID);
			this.addParameter("profileId", PFL);
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("formProtectionType", PROTECTION_TYPE);
			this.addParameter("formDescription", DESCRIPTION);
			String result = this.executeAction();
			assertEquals("success", result);
			SalesforceFormAction action = (SalesforceFormAction) this.getAction();
			// selects
			assertNotNull(action.getCampaigns());
			assertNotNull(action.getProfiles());
			action = (SalesforceFormAction) this.getAction();
			// lead fields
			List<String> list = action.getLeadFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			// profile fields
			list = action.getProfileFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testConfigure_3() throws Throwable {
		try {
			setUserOnSession("admin");
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "configure");
			this.addParameter("campaignId", CAMPAIGN_ID);
			this.addParameter("profileId", JpSalesforceConstants.NO_PROFILE_KEY);
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("formProtectionType", PROTECTION_TYPE);
			this.addParameter("formDescription", DESCRIPTION);
			String result = this.executeAction();
			assertEquals("success", result);
			SalesforceFormAction action = (SalesforceFormAction) this.getAction();
			// selects
			assertNotNull(action.getCampaigns());
			assertNotNull(action.getProfiles());
			action = (SalesforceFormAction) this.getAction();
			// lead fields
			List<String> list = action.getLeadFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			// profile fields
			list = action.getProfileFields();
			assertNotNull(list);
			assertTrue(list.isEmpty());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testAdd_1() throws Throwable {
		try {
			setUserOnSession("admin");
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "add");
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testAdd_2() throws Throwable {
		try {
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "add");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindLeadField", "unknownLead");
			this.addParameter("bindProfileField", "unknownProfile");
			this.addParameter("mandatory", "true");
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("en"), "labelen");
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("it"), "labelit");
			String result = this.executeAction();
			assertEquals("failure", result); // expected because can't update remaining fields
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testAdd_3() throws Throwable {
		try {
			
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			SalesforceFormAction action = executeAddBinding(LEAD1, LANGUAGE, true);
			// lead fields
			List<String> list = action.getLeadFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(3, list.size());
			assertEquals(LEAD2, list.get(0));
			// profile fields
			list = action.getProfileFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(3, list.size());
			assertEquals(FULLNAME, list.get(0));
			// written data
			List<FormBinding> bindings = action.getBindings();
			assertNotNull(list);
			assertFalse(bindings.isEmpty());
			assertEquals(LEAD1, bindings.get(0).getLeadField());
			assertEquals(LANGUAGE, bindings.get(0).getProfileField());
			assertTrue(bindings.get(0).isMandatory());
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testAdd_4() throws Throwable {

		try {
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			SalesforceFormAction action = executeAddBinding(LEAD2, FULLNAME, false);
			// lead fields
			List<String> list = action.getLeadFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(3, list.size());
			assertEquals(LEAD1, list.get(0));
			// profile fields
			list = action.getProfileFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(3, list.size());
			assertEquals(LANGUAGE, list.get(0));
			// written data
			List<FormBinding> bindings = action.getBindings();
			assertNotNull(list);
			assertFalse(bindings.isEmpty());
			assertEquals(LEAD2, bindings.get(0).getLeadField());
			assertEquals(FULLNAME, bindings.get(0).getProfileField());
			assertFalse(bindings.get(0).isMandatory());
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testAdd_5() throws Throwable {

		try {
			resetLists();
			setUserOnSession("admin");
			putLeadsOnlyConfigurationInSession();
			SalesforceFormAction action = executeAddBinding(LEAD2, null, false);
			// lead fields
			List<String> list = action.getLeadFields();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(3, list.size());
			assertEquals(LEAD1, list.get(0));
			// profile fields
			list = action.getProfileFields();
			assertNotNull(list);
			assertTrue(list.isEmpty());
			// written data
			List<FormBinding> bindings = action.getBindings();
			assertNotNull(list);
			assertFalse(bindings.isEmpty());
			assertEquals(LEAD2, bindings.get(0).getLeadField());
			assertEquals(JpSalesforceConstants.NO_BINDING_KEY, bindings.get(0).getProfileField());
			assertFalse(bindings.get(0).isMandatory());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testAdd_6() throws Throwable {
		SalesforceFormAction action = null;
		String result = null;

		try {
			resetLists();
			setUserOnSession("admin");
			putLeadsOnlyConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "add");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindLeadField", JpSalesforceConstants.NO_BINDING_KEY);
			this.addParameter("bindProfileField", JpSalesforceConstants.NO_BINDING_KEY);
			this.addParameter("mandatory", String.valueOf(false));
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("en"), LABEL_EN);
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("it"), LABEL_IT);
			result = this.executeAction();
			assertEquals("input", result);
			action = (SalesforceFormAction) this.getAction();
			Collection<String> errors = action.getActionErrors();
			assertNotNull(errors);
			assertFalse(errors.isEmpty());
			assertEquals("Invalid fields configuration", errors.iterator().next());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testDelete_1() throws Throwable {
		try {
			setUserOnSession("admin");
			//			putConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "delete");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindingId", 1);
			String result = this.executeAction();
			assertEquals("failure", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testDelete_2() throws Throwable {
		try {
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "delete");
			//			this.addParameter("bindingId", 1);
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}


	public void testDelete_3() throws Throwable {
		try {
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			executeAddBinding(LEAD1, LANGUAGE, true);
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "delete");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindingId", 0);
			String result = this.executeAction();
			assertEquals("success", result);
			SalesforceFormAction action = (SalesforceFormAction) this.getAction();
			List<String> leads = action.getLeadFields();
			List<String> profiles = action.getProfileFields();
			assertNotNull(leads);
			assertFalse(leads.isEmpty());
			assertEquals(4, leads.size());
			assertNotNull(profiles);
			assertFalse(profiles.isEmpty());
			assertEquals(4, profiles.size());
			assertTrue(profiles.contains(FULLNAME));
			assertTrue(profiles.contains(LANGUAGE));
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testUp_1() throws Throwable {
		try {
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "up");
			//			this.addParameter("bindingId", 1);
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testUp_2() throws Throwable {
		try {
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "up");
			this.addParameter("bindingId", "StringAndNotANumber");
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testUp_3() throws Throwable {
		SalesforceFormAction action = null;

		try {
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			action = executeAddBinding(LEAD1, FULLNAME, true);
			// check whether the fields where added
			List<FormBinding> list = action.getBindings();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(1, list.size());
			assertEquals(LEAD1, list.get(0).getLeadField());
			// check left field from profiles
			List<String> profileLeft = action.getProfileFields();
			assertNotNull(profileLeft);
			assertFalse(profileLeft.isEmpty());
			assertEquals(LANGUAGE, profileLeft.get(0));
			// add the remaining fields
			action = executeAddBinding(LEAD2, LANGUAGE, true);
			list = action.getBindings();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
			assertEquals(LEAD1, list.get(0).getLeadField());
			assertEquals(LEAD2, list.get(1).getLeadField());
			assertNotNull(action.getLeadFields());
			assertEquals(2, action.getLeadFields().size());
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "up");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindingId", 1);
			String result = this.executeAction();
			assertEquals("success", result);
			action = (SalesforceFormAction) this.getAction();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
			list = action.getBindings();
			assertEquals(LEAD2, list.get(0).getLeadField());
			assertEquals(LEAD1, list.get(1).getLeadField());

			// push up again the first element: nothing should happen
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "up");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindingId", 0);
			result = this.executeAction();
			assertEquals("success", result);
			action = (SalesforceFormAction) this.getAction();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
			list = action.getBindings();
			assertEquals(LEAD2, list.get(0).getLeadField());
			assertEquals(LEAD1, list.get(1).getLeadField());

		} catch (Throwable t) {
			throw t;
		}
	}

	public void testDown_1() throws Throwable {
		try {
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "down");
			//			this.addParameter("bindingId", 1);
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testDown_2() throws Throwable {
		SalesforceFormAction action = null;

		try {
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			action = executeAddBinding(LEAD2, FULLNAME, true);
			// check whether the fields where added
			List<FormBinding> list = action.getBindings();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(1, list.size());
			assertEquals(LEAD2, list.get(0).getLeadField());
			// check remaining profile
			List<String> leads = action.getLeadFields();
			assertNotNull(leads);
			assertFalse(leads.isEmpty());
			assertEquals(3, leads.size());
			assertEquals(LEAD1, leads.get(0));
			// check left field from profiles
			List<String> profileLeft = action.getProfileFields();
			assertNotNull(profileLeft);
			assertFalse(profileLeft.isEmpty());
			assertEquals(LANGUAGE, profileLeft.get(0));
			// add the remaining fields
			action = executeAddBinding(LEAD1, LANGUAGE, true);
			list = action.getBindings();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
			assertEquals(LEAD2, list.get(0).getLeadField());
			assertEquals(LEAD1, list.get(1).getLeadField());
			assertNotNull(action.getLeadFields());
			assertEquals(2, action.getLeadFields().size());

			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "down");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindingId", 0);
			String result = this.executeAction();
			assertEquals("success", result);
			action = (SalesforceFormAction) this.getAction();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
			list = action.getBindings();
			assertEquals(LEAD1, list.get(0).getLeadField());
			assertEquals(LEAD2, list.get(1).getLeadField());

			// push up again the first element: nothing should happen
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "down");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindingId", 1);
			result = this.executeAction();
			assertEquals("success", result);
			action = (SalesforceFormAction) this.getAction();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
			list = action.getBindings();
			assertEquals(LEAD1, list.get(0).getLeadField());
			assertEquals(LEAD2, list.get(1).getLeadField());

		} catch (Throwable t) {
			throw t;
		}
	}

	public void testEditBinding_1() throws Throwable {
		try {
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "edit");
			//			this.addParameter("bindingId", 1);
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testEditBinding_2() throws Throwable {
		try {
			setUserOnSession("admin");
			//			putTestConfigurationInSession();
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "edit");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindingId", 1);
			String result = this.executeAction();
			assertEquals("failure", result);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testEdit_3() throws Throwable {
		SalesforceFormAction action = null;
		try {
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			executeAddBinding(LEAD1, FULLNAME, true);
			action = executeAddBinding(LEAD2, LANGUAGE, true);
			List<FormBinding> bindings = action.getBindings();
			assertNotNull(bindings);
			assertFalse(bindings.isEmpty());
			assertEquals(2, bindings.size());
			FormBinding binding = bindings.get(0);
			assertNotNull(binding);
			assertEquals(0, binding.getId());
			assertNotNull(binding.getLabels());
			assertEquals(LABEL_EN, binding.getLabels().get("en"));
			assertTrue(bindings.get(0).isMandatory());
			// change label and mandatory attribute
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "add");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			this.addParameter("bindLeadField", "aNewLefield");		// this won't change
			this.addParameter("bindProfileField", "aNewProfile");	// this won't change
			this.addParameter("strutsAction", ApsAdminSystemConstants.EDIT);
			this.addParameter("mandatory", "false");
			this.addParameter("bindingId", 1);
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("en"), "newLabelEn");
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("it"), "nuovaEtichettaIt");
			String result = this.executeAction();
			action = (SalesforceFormAction) this.getAction();
			assertEquals("success", result);
			bindings = action.getBindings();
			assertNotNull(bindings);
			bindings = action.getBindings();
			assertFalse(bindings.isEmpty());
			binding = bindings.get(0);
			// this shouldn't be changed...
			assertEquals(LABEL_EN, binding.getLabels().get("en"));
			// ...but these should!
			binding = bindings.get(1);
			assertEquals("newLabelEn", binding.getLabels().get("en"));
			assertEquals("nuovaEtichettaIt", binding.getLabels().get("it"));
			assertFalse(binding.isMandatory());
			// finally
			assertEquals(2, bindings.size());
		} catch (Throwable t) {
			throw t;
		}
	}

	// test missing attribute declared mandatory in the profile
	public void testSave_1() throws Throwable {
		SalesforceFormAction action = null;
		String result = null;
		
		try{
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			executeAddBinding(LEAD1, EMAIL, true);
			executeAddBinding(LEAD2, FULLNAME, true);
			action = executeAddBinding(LEAD3, LANGUAGE, true);
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "saveConfig");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			result = this.executeAction();
			action = (SalesforceFormAction) this.getAction();
			assertEquals("input", result);
			assertNotNull(action.getActionErrors());
			assertFalse(action.getActionErrors().isEmpty());
			assertEquals(1, action.getActionErrors().size());
			Iterator<String> itr = action.getActionErrors().iterator();
			assertEquals("The attribute '" + BIRTHDATE + "' must be used and declared mandatory in conjunction with a lead field", itr.next());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	// test attribute not declared mandatory
	public void testSave_2() throws Throwable {
		SalesforceFormAction action = null;
		String result = null;
		
		try{
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			executeAddBinding(LEAD1, EMAIL, true);
			executeAddBinding(LEAD2, FULLNAME, true);
			executeAddBinding(LEAD3, LANGUAGE, false); // note this!
			action = executeAddBinding(LEAD4, BIRTHDATE, true);
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "saveConfig");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			result = this.executeAction();
			action = (SalesforceFormAction) this.getAction();
			assertEquals("input", result);
			assertNotNull(action.getActionErrors());
			assertFalse(action.getActionErrors().isEmpty());
			assertEquals(1, action.getActionErrors().size());
			Iterator<String> itr = action.getActionErrors().iterator();
			assertEquals("The attribute '" + LANGUAGE + "' must be declared mandatory as required by the profile configuration", itr.next());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSave_3() throws Throwable {
		SalesforceFormAction action = null;
		String result = null;
		IApsEntity messagePrototype = null;
		SalesforceForm form = null;
		try{
			messagePrototype = _messageManager.getEntityPrototype("AAA");
			assertNull(messagePrototype);
			form = _salesforceFormManager.getSalesforceForm(1);
			assertNull(form);
			
			resetLists();
			setUserOnSession("admin");
			putLeadProfileConfigurationInSession();
			executeAddBinding(LEAD1, EMAIL, true);
			executeAddBinding(LEAD2, FULLNAME, true);
			executeAddBinding(LEAD3, LANGUAGE, true);
			executeAddBinding(LEAD4, BIRTHDATE, true);
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "saveConfig");
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			result = this.executeAction();
			assertEquals("redirectWebformConfig", result);
			action = (SalesforceFormAction) this.getAction();
			form = _salesforceFormManager.getSalesforceForm(1);
			assertNotNull(form);
			messagePrototype = _messageManager.getEntityPrototype("AAA");
			assertNotNull(messagePrototype);
		} catch (Throwable t) {
			throw t;
		} finally {
				IPage page = this.getPageManager().getPage(PAGE_ID);
				page.getWidgets()[FRAME] = null;
				this.getPageManager().updatePage(page);
				_salesforceFormManager.deleteSalesforceForm(1);
				((ApsEntityManager)_messageManager).removeEntityPrototype("AAA");
				messagePrototype = _messageManager.getEntityPrototype("AAA");
				assertNull(messagePrototype);
				form = _salesforceFormManager.getSalesforceForm(1);
				assertNull(form);
		}
	}
	
	private SalesforceFormAction executeAddBinding(String lead, String profile, boolean mandatory) throws Throwable {
		SalesforceFormAction action = null;

		try {
			this.initAction("/do/jpsalesforce/Page/SpecialWidget/form", "add");
			this.addParameter("bindLeadField", lead);
			this.addParameter("bindProfileField", profile);
			this.addParameter("mandatory", String.valueOf(mandatory));
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("en"), LABEL_EN);
			this.addParameter(SalesforceFormAction.LABEL_PREFIX.concat("it"), LABEL_IT);
			// widget data
			this.addParameter("pageCode", PAGE_ID);
			this.addParameter("frame", String.valueOf(FRAME));
			this.addParameter("widgetTypeCode", ApsAdminPluginBaseTestCase.JPSALESFORCE_FORM_WIDGETCODE);
			String result = this.executeAction();
			assertEquals("success", result);
			action = (SalesforceFormAction) this.getAction();
		} catch (Throwable t) {
			throw t;
		}
		return action;
	}

	private void putLeadProfileConfigurationInSession() {
		resetLists();
		HttpSession session = this.getRequest().getSession();
		FormConfiguration fc = new FormConfiguration(CAMPAIGN_ID, PFL);
		session.setAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG, fc);
		fc.setLeadFields(_leadFields);
		fc.setProfileFields(_profileFields);
		fc.setRequiredProfileFields(_reqProfileFields);
		fc.setDescription(DESCRIPTION);
		fc.setProtectionType(PROTECTION_TYPE);
	}
	
	private void putLeadsOnlyConfigurationInSession() {
		resetLists();
		_profileFields.clear();
		HttpSession session = this.getRequest().getSession();
		FormConfiguration fc = new FormConfiguration(CAMPAIGN_ID, PFL);
		session.setAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG, fc);
		fc.setLeadFields(_leadFields);
		fc.setProfileFields(_profileFields);
	}
	
	private void resetLists() {
		_leadFields.clear();
		_profileFields.clear();
		_reqProfileFields.clear();
		
		_leadFields.add(LEAD1);
		_leadFields.add(LEAD2);
		_leadFields.add(LEAD3);
		_leadFields.add(LEAD4);
		
		_profileFields.add(FULLNAME);
		_profileFields.add(LANGUAGE);
		_profileFields.add(EMAIL);
		_profileFields.add(BIRTHDATE);
		
		// mandatory fields for the default profile
		_reqProfileFields.add(EMAIL);
		_reqProfileFields.add(BIRTHDATE);
		_reqProfileFields.add(LANGUAGE);
		_reqProfileFields.add(FULLNAME);
	}

	private void init() throws Exception {
		try {
			// init services
//			this._salesforceManager = (ISalesforceManager) super.getService(JpSalesforceConstants.SALESFORCE_MANAGER);
			this._messageManager = (IMessageManager) super.getService(JpwebdynamicformSystemConstants.MESSAGE_MANAGER);
			this._salesforceFormManager = (ISalesforceFormManager) this.getService(JpSalesforceConstants.SALESFORCE_FORM_MANAGER);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private IMessageManager _messageManager;
	private ISalesforceFormManager _salesforceFormManager;
	
	public static final String PFL = "PFL";
	public static final String CAMPAIGN_ID = "701i0000000dtCjAAI";
	public static final int FRAME = 0;
	public static final String PAGE_ID = "pagina_2";
	public static final String DESCRIPTION = "Web to lead form";
	public static final String PROTECTION_TYPE = JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_NONE;
	

	public static final List<String> _leadFields = new ArrayList<String>();
	public static final List<String> _profileFields = new ArrayList<String>();
	public static final List<String> _reqProfileFields = new ArrayList<String>();

	public static final String LEAD1 = "lead1";
	public static final String LEAD2 = "lead2";
	public static final String LEAD3 = "lead3";
	public static final String LEAD4 = "lead4";
	public static final Object LABEL_EN = "labelen";
	public static final Object LABEL_IT = "labelit";
	
	public static final String EMAIL = "email";
	public static final String BIRTHDATE = "birthdate";
	public static final String LANGUAGE = "language";
	public static final String FULLNAME = "fullname";
}
