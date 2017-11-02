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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpsalesforce.SalesforceTestSettings;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceBaseTestCase;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.AbstractSalesforceItem;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Campaign;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSalesforceManager extends JpSalesforceBaseTestCase implements SalesforceTestSettings {
	
	public void testLeadPrototype() throws Throwable {

		try {
			if (null == _sad) {
				doLogin();
				if (null == _sad) return;
			}
			Lead prototype = getSalesforceManager().getLeadPrototype(_sad);
			assertNotNull(prototype);
			assertFalse(prototype.isValid());
			assertFalse(prototype.getAttributes().isEmpty());
			assertNotNull(prototype.getAttributes());
			assertFalse(prototype.getAttributes().isEmpty());
			assertNotNull(prototype.getJSON());
			assertEquals(Lead.LEAD, prototype.getType());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testGetLeads() throws Throwable {
		int count = -1;
		Lead lead = createMinLead(TEST_LEAD, TEST_COMPANY);
		String id = null;
		
		if (null == _sad) {
			doLogin();
			if (null == _sad) return;
		}
		List<Lead> list = getSalesforceManager().getLeads(_sad);
		assertNotNull(list);
		assertFalse(list.isEmpty());
		count = list.size();
		assertEquals(Lead.LEAD, list.get(0).getType());
		try {
			// create a new lead
			id = getSalesforceManager().addLead(lead, _sad);
			assertNotNull(id);
			list = getSalesforceManager().getLeads(_sad);
			assertEquals(++count, list.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			if (null != id) {
				getSalesforceManager().deleteLead(id, _sad);
			}
		}
	}

	public void testLoadUnknownLead() throws Throwable {
		try {
			if (null == _sad) {
				doLogin();
				if (null == _sad) return;
			}
			Lead user = getSalesforceManager().getLead("notMePlease", _sad);
			assertNull(user);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testDeleteUnknownLead() throws Throwable {
		try {
			if (null == _sad) {
				doLogin();
				if (null == _sad) return;
			}
			try {
				getSalesforceManager().deleteLead("notMePlease", _sad);
				assertTrue(false);
			} catch (Throwable t) {
				// THIS IS EXPECTED!!! 404 error 
			}
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testLeadCRUD() throws Throwable {
		String id = null;
//		Lead lead = new Lead(TEST_LEAD, TEST_COMPANY);
		Lead lead = createMinLead(TEST_LEAD, TEST_COMPANY);
		
		try {
			if (null == _sad) {
				doLogin();
				if (null == _sad) return;
			}
			// create a new lead
			id = getSalesforceManager().addLead(lead, _sad);
			assertNotNull(id);
			// reload the user
			lead = getSalesforceManager().getLead(id, _sad);
			assertNotNull(lead);
			assertEquals(id, lead.getId());
			assertEquals(TEST_LEAD, lead.getAttribute(AbstractSalesforceItem.NAME));
		} catch (Throwable t) {
			throw t;
		} finally {
			getSalesforceManager().deleteLead(id, _sad);
		}
	}

	public void testUpdate() throws Throwable {
		String id = null;
		final String UPDATE_CORP = "HelloWorld corp.";
//		Lead lead = new Lead(TEST_LEAD, TEST_COMPANY);
		Lead lead = createMinLead(TEST_LEAD, TEST_COMPANY);
		
		try {
			if (null == _sad) {
				doLogin();
				if (null == _sad) return;
			}
			// create a new lead
			id = getSalesforceManager().addLead(lead, _sad);
			assertNotNull(id);
			// reload the user
			lead = getSalesforceManager().getLead(id, _sad);
			assertNotNull(lead);
			assertEquals(TEST_LEAD, lead.getAttribute(AbstractSalesforceItem.NAME));
			assertEquals(TEST_COMPANY, lead.getAttribute("Company"));
			lead.getJSON().put("Company", UPDATE_CORP);
			getSalesforceManager().updateLead(lead, _sad);
			// reload the user again
			lead = getSalesforceManager().getLead(id, _sad);
			assertNotNull(lead);
			assertTrue(UPDATE_CORP.equals(lead.getAttribute("Company")));
		} catch (Throwable t) {
			throw t;
		} finally {
			getSalesforceManager().deleteLead(id, _sad);
		}
	}

	public void testLeadSearch() throws Throwable {
		String id1 = null;
		String id2 = null;
		
		try {
			if (null == _sad) {
				doLogin();
				if (null == _sad) return;
			}
			// create a new lead
			id1 = getSalesforceManager().addLead(createLeadJSON(TEST_LEAD, TEST_LASTNAME), _sad);
			Thread.sleep(1500);
			id2 = getSalesforceManager().addLead(createLeadJSON(TEST_LEAD2, TEST_LASTNAME), _sad);
			Thread.sleep(1500);
			assertNotNull(id1);
			assertNotNull(id2);
			// list 
			List<String> list = getSalesforceManager().searchLeadAllFields(TEST_LEAD, _sad);
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(1, list.size());
			assertEquals(id1, list.get(0));
			Lead lead = getSalesforceManager().getLead(list.get(0), _sad);
			assertNotNull(lead);
			assertEquals(Lead.LEAD, lead.getType());
			// reload the user
			Lead usr = getSalesforceManager().getLead(id1, _sad);
			assertNotNull(usr);
			assertTrue(TEST_LEAD.equals(usr.getAttribute(Lead.FIELD_LEAD_FIRSTNAME)));
		} catch (Throwable t) {
			throw t;
		} finally {
			getSalesforceManager().deleteLead(id1, _sad);
			getSalesforceManager().deleteLead(id2, _sad);
		}
	}
	
	public void testLeadSearch2() throws Throwable {
		String id1 = null;
		String id2 = null;
		
		try {
			if (null == _sad) {
				doLogin();
				if (null == _sad) return;
			}
			// create a new lead
			id1 = getSalesforceManager().addLead(createLeadJSON(TEST_LEAD, TEST_LASTNAME), _sad);
			Thread.sleep(1500);
			id2 = getSalesforceManager().addLead(createLeadJSON(TEST_LEAD2, TEST_LASTNAME), _sad);
			Thread.sleep(1500);
			assertNotNull(id1);
			assertNotNull(id2);
			// list 
			List<String> list = getSalesforceManager().searchLeadAllFields(TEST_LASTNAME, _sad);
			assertNotNull(list);
			assertFalse(list.isEmpty());
			assertEquals(2, list.size());
			Lead leadA = getSalesforceManager().getLead(list.get(0), _sad);
			Lead leadB = getSalesforceManager().getLead(list.get(1), _sad);
			assertEquals(Lead.LEAD, leadA.getType());
			assertEquals(TEST_LASTNAME, leadA.getAttribute(Lead.FIELD_LEAD_LASTNAME));
			assertEquals(TEST_LASTNAME, leadB.getAttribute(Lead.FIELD_LEAD_LASTNAME));
			assertTrue(leadA.getAttribute(Lead.FIELD_LEAD_FIRSTNAME).equals(TEST_LEAD) || leadA.getAttribute(Lead.FIELD_LEAD_FIRSTNAME).equals(TEST_LEAD2));
			assertTrue(leadB.getAttribute(Lead.FIELD_LEAD_FIRSTNAME).equals(TEST_LEAD) || leadB.getAttribute(Lead.FIELD_LEAD_FIRSTNAME).equals(TEST_LEAD2));
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			getSalesforceManager().deleteLead(id1, _sad);
			getSalesforceManager().deleteLead(id2, _sad);
		}
	}
	
	public void testGetCampaigns() throws Throwable {
		if (null == _sad) {
			doLogin();
			if (null == _sad) return;
		}
		List<Campaign> list = getSalesforceManager().getCampaigns(_sad);
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(4, list.size());
		assertEquals(Campaign.CAMPAIGN, list.get(0).getType());
	}
	
	public void testLoadDefaultConfig() {
		SalesforceConfig config = getSalesforceManager().getConfig();
		assertNotNull(config);
		assertEquals("24.0", config.getPreferredVersion());
		assertEquals("https://login.salesforce.com/services/oauth2/authorize", config.getLoginEndpoint());
		assertEquals("https://login.salesforce.com/services/oauth2/token", config.getTokenEndpoint());
	}
	
	public void testCustomConfig() throws Throwable {
		SalesforceConfig config = new SalesforceConfig(CONFIG_XML);
		
		if (null == _sad) {
			doLogin();
			if (null == _sad) return;
		}
		
		assertEquals("20.0", config.getPreferredVersion());
		assertEquals("https://login.salesforce.com/services/oauth2/authorize", config.getLoginEndpoint());
		assertEquals("https://login.salesforce.com/services/oauth2/token", config.getTokenEndpoint());
		assertEquals("my@mail.it", config.getUserId());
		assertEquals("p4$$w0r1d", config.getUserPwd());
		assertEquals("APP_ID", config.getAppId());
		assertEquals("APP_SECRET", config.getAppSecret());
		assertEquals("SECTOK", config.getSecurityToken());
		assertEquals("2677", config.getOid());
	}
	
	
	/**
	 * Perform login to salesforce using the test configuration
	 * @throws Throwable
	 */
	private void doLogin() throws Throwable {
		Logger logger = LoggerFactory.getLogger(TestSalesforceManager.class);
		SalesforceConfig config = getConfigForTest();
		
		if (null != config 
				&& config.isValidForUnmannedLogin()) {
			_sad = getSalesforceManager().login(config);
			assertNotNull(_sad);
			assertTrue(StringUtils.isNotBlank(_sad.getAccessToken()));
			assertTrue(StringUtils.isNotBlank(_sad.getInstaceUrl()));
		} else {
			logger.warn("\n**** SKIPPING TEST BECAUSE OF INVALID CONFIGURATION (edit the SalesforceTestSettings.java interface) ****");
		}
	}

	/**
	 * Create a new JSON
	 * 
	 * {
	 * 	"Phone":"012345",
	 * 	Company":"Test Company"
	 * 	Fax":"12345"
	 * 	LastName":"Micheal"
	 * }
	 * @param firstName
	 * @return
	 */
	private JSONObject createLeadJSON(String firstName, String lastName) {
		JSONObject uobj = new JSONObject();

//		uobj.put(AbstractSalesforceItem.NAME, name);
		uobj.put("LastName", lastName);
		uobj.put("FirstName", firstName);
		uobj.put("Company", TEST_COMPANY);
		uobj.put("Fax", TEST_FAX);
		return uobj;
	}

	private Lead createMinLead(String lastName, String company) throws Throwable {
		Lead lead = new Lead();
		
		lead.addAttribute("LastName", lastName);
		lead.addAttribute("Company", company);
		return lead;
	}
	
	/**
	 * Create the configuration for the test
	 * @return
	 */
	private SalesforceConfig getConfigForTest() {
		final String CONFIG_TEMPLATE = "<config><username>%s</username><oid>%s</oid><app_secret>%s</app_secret><app_id>%s</app_id><auth_url_token>%s</auth_url_token><auth_url_new>%s</auth_url_new><api_version>%s</api_version><security_token>%s</security_token><password>%s</password></config>";
		String xml = String.format(CONFIG_TEMPLATE, 
				USER_ID,
				OID,
				CLIENT_SECRET,
				CLIENT_ID,
				TOKEN_ENDPOINT,
				LOGIN_ENDPOINT,
				VERSION,
				SECURITY_TOKEN,
				USER_PWD);
		SalesforceConfig config = new SalesforceConfig(xml);
		assertTrue(config.isValidForUnmannedLogin());
		return config;
	}

	private SalesforceAccessDescriptor _sad;

	// user create for tests
	private static final String TEST_LASTNAME = "Lastname";
	private static final String TEST_LEAD = "First";
	private static final String TEST_LEAD2 = "Second";
	private static final String TEST_COMPANY = "HalloWorld ltd";
	private static final String TEST_FAX = "12345";

	private static final String CONFIG_XML = "<config><username>my@mail.it</username><oid>2677</oid><app_secret>APP_SECRET</app_secret><app_id>APP_ID</app_id><auth_url_token>https://login.salesforce.com/services/oauth2/token</auth_url_token><auth_url_new>https://login.salesforce.com/services/oauth2/authorize</auth_url_new><api_version>20.0</api_version><security_token>SECTOK</security_token><password>p4$$w0r1d</password></config>";	

};
