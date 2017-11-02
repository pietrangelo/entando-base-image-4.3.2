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
package org.entando.entando.plugins.jpsalesforce.aps.internalservlet.leads;

import java.util.List;

import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.entando.entando.plugins.jpsalesforce.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

public class TestLeadAction extends ApsAdminPluginBaseTestCase {
	
	// Missing strutsAction AND pageCode
	public void testNewLead_1() throws Throwable {
		if (abortTest()) return;
		try {
			this.initAction("/do/salesforce/leads", "save");
			this.addAttribute("LastName", LASTNAME);
			this.addAttribute("FirstName", LASTNAME);
			this.addAttribute("Company", COMPANY);
			String result = this.executeAction();
			assertEquals("input", result);	
//			LeadAction action = (LeadAction) this.getAction();
//			System.out.println(">>> " + action.getRedirectionUrl());
//			System.out.println("Messages: " + action.getActionMessages().toString());
//			System.out.println("Action: " + action.getActionErrors().toString());
//			System.out.println("Fields: " + action.getFieldErrors().toString());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testNewLead_2() throws Throwable {
		SalesforceAccessDescriptor sad = null;
		List<String> list = null;
		// missing sobj
		
		if (abortTest()) return;
		try {
			sad = getAccessDescriptorInSession();
			list = getSalesforceManager().searchLeadAllFields(LASTNAME, sad);
			assertNotNull(list);
			assertTrue(list.isEmpty());
			this.initAction("/do/salesforce/leads", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
			this.addParameter("LastName", LASTNAME);
			this.addParameter("FirstName", FIRSTNAME);
			this.addParameter("Company", COMPANY);
			this.addParameter("pageCode", "homepage"); // OPTIONAL
			String result = this.executeAction();
			assertEquals("redirect", result);
			LeadAction action = (LeadAction) this.getAction();
			assertNotNull(action.getRedirectionUrl());
			assertTrue(action.getRedirectionUrl().contains("homepage.page"));
		} catch (Throwable t) {
			throw t;
		} finally {
			Thread.sleep(2000);
			list = getSalesforceManager().searchLeadAllFields(LASTNAME, sad);
			assertNotNull(list);
			assertFalse(list.isEmpty());
			// if this fails then comment out the line and let the next run to clean the DB 
			assertEquals(1, list.size());
			for (String id: list) {
//				Lead lead = getSalesforceManager().getLead(id, sad);
//				System.out.println(">>> deleting " + lead.getName());
				getSalesforceManager().deleteLead(id, sad);
			}
			
			
		}
	}
	
	
	public void testUpdateLead_1() throws Throwable {
		
		if (abortTest()) return;
		try {
			this.initAction("/do/salesforce/leads", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
			this.addParameter("LastName", LASTNAME);
			this.addParameter("FirstName", "aNewName");
			this.addParameter("Company", COMPANY);
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testUpdateLead_2() throws Throwable {
		SalesforceAccessDescriptor sad = null;
		String id = null;
		Lead lead = getLeadForAdd();
		
		if (abortTest()) return;
		try {
			sad = getAccessDescriptorInSession();
			// create a new Lead
			id = getSalesforceManager().addLead(lead, sad);
			assertNotNull(id);
			// update the new lead
			this.initAction("/do/salesforce/leads", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
			this.addParameter("sobj", id);
			this.addParameter("LastName", LASTNAME);
			this.addParameter("FirstName", "aNewName");
			this.addParameter("Company", COMPANY);
			String result = this.executeAction();
			LeadAction action = (LeadAction) this.getAction();
			assertEquals("success", result);
			lead = this.getSalesforceManager().getLead(id, sad);
			assertNotNull(lead);
			assertEquals("aNewName", lead.getAttribute("FirstName"));
		} catch (Throwable t) {
			throw t;
		} finally {
			getSalesforceManager().deleteLead(id, sad);
		}
	}
	
	
	public void testDeleteLead_1() throws Throwable {
		SalesforceAccessDescriptor sad = null;
		
		if (abortTest()) return;
		try {
			sad = getAccessDescriptorInSession();
			this.initAction("/do/salesforce/leads", "delete");
//			this.addParameter("sobj", id);
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testDeleteLead_2() throws Throwable {
		SalesforceAccessDescriptor sad = null;
		Lead lead = getLeadForAdd();
		String id = null;
		boolean deleted = false;
		
		if (abortTest()) return;
		try {
			sad = getAccessDescriptorInSession();
		// create a new Lead
			id = getSalesforceManager().addLead(lead, sad);
			assertNotNull(id);
			this.initAction("/do/salesforce/leads", "delete");
			this.addParameter("sobj", id);
			String result = this.executeAction();
			assertEquals("success", result);
			lead = getSalesforceManager().getLead(id, sad);
			assertNull(lead);
			deleted = true;
		} catch (Throwable t) {
			throw t;
		} finally {
			if (!deleted) {
				System.out.println("Forcibily deleting lead " + id);
				getSalesforceManager().deleteLead(id, sad);
				lead = getSalesforceManager().getLead(id, sad);
				assertNull(lead);
			}
		}
	}
	
	public void testViewLead_1() throws Throwable {
		SalesforceAccessDescriptor sad = null;
		
		if (abortTest()) return;
		try {
			sad = getAccessDescriptorInSession();
			this.initAction("/do/salesforce/leads", "view");
//			this.addParameter("sobj", id);
			String result = this.executeAction();
			assertEquals("input", result);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private Lead getLeadForAdd() throws Throwable {
		Lead lead = new Lead();
		
		lead.addAttribute("LastName", LASTNAME);
		lead.addAttribute("Company", COMPANY);
		
		return lead;
	}
	
	
	public static final String FIRSTNAME = "A. Name";
	public static final String LASTNAME = "surname";
	public static final String COMPANY = "thiscompany ltd";
}
