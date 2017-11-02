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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceForm;

import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceBaseTestCase;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormBinding;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;
import org.json.JSONObject;
import org.json.XML;

public class TestFormConfiguration extends JpSalesforceBaseTestCase {
	
	public void testFormBindingJSON() throws Throwable {
		try {
			FormBinding bind = new FormBinding(new JSONObject(JSON_BIND));
			assertEquals("firstname", bind.getLeadField());
			assertEquals(0, bind.getId());
			assertEquals("fullname", bind.getProfileField());
			assertTrue(bind.isMandatory());
			assertNotNull(bind.getLabels());
			assertFalse(bind.getLabels().isEmpty());
			assertEquals(2, bind.getLabels().size());
			assertEquals("label it", bind.getLabels().get("it"));
			assertEquals("label en", bind.getLabels().get("en"));
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testFormConfigurationJSON() throws Throwable {
		try {
			FormConfiguration form = new FormConfiguration(new JSONObject(JSON_FORM));
			assertEquals("myGreatCampaign", form.getCampaignId());
			assertEquals("PFL", form.getProfileId());
			assertNotNull(form.getBindings());
			assertFalse(form.getBindings().isEmpty());
			assertEquals(4, form.getBindings().size());
			FormBinding bind = form.getBindings().get(0);
			assertEquals("firstname", bind.getLeadField());
			assertEquals(0, bind.getId());
			assertEquals("fullname", bind.getProfileField());
			assertFalse(bind.isMandatory());
			assertNotNull(bind.getLabels());
			assertFalse(bind.getLabels().isEmpty());
			assertEquals(2, bind.getLabels().size());
			assertEquals("label it 0", bind.getLabels().get("it"));
			assertEquals("label en 0", bind.getLabels().get("en"));
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testFormConfigurationXML() throws Throwable {
		try {
			JSONObject json = XML.toJSONObject(XML_FORM);
			FormConfiguration form = new FormConfiguration(json);
			assertEquals("myGreatCampaign", form.getCampaignId());
			assertEquals("PFL", form.getProfileId());
			assertNotNull(form.getBindings());
			assertFalse(form.getBindings().isEmpty());
			assertEquals(4, form.getBindings().size());
			FormBinding bind = form.getBindings().get(0);
			assertEquals("firstname", bind.getLeadField());
			assertEquals(0, bind.getId());
			assertEquals("fullname", bind.getProfileField());
			assertFalse(bind.isMandatory());
			assertNotNull(bind.getLabels());
			assertFalse(bind.getLabels().isEmpty());
			assertEquals(2, bind.getLabels().size());
			assertEquals("label it 0", bind.getLabels().get("it"));
			assertEquals("label en 0", bind.getLabels().get("en"));
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testFormConfigurationXML_2() throws Throwable {
		try {
			JSONObject json = XML.toJSONObject(XML_FORM_PROFILE_ONLY);
			FormConfiguration form = new FormConfiguration(json);
			assertEquals("myGreatCampaign", form.getCampaignId());
			assertEquals("LFP", form.getProfileId());
			assertNotNull(form.getBindings());
			assertFalse(form.getBindings().isEmpty());
			assertEquals(1, form.getBindings().size());
			FormBinding bind = form.getBindings().get(0);
			assertEquals("firstname", bind.getLeadField());
			assertEquals(0, bind.getId());
			assertEquals("fullname", bind.getProfileField());
			assertFalse(bind.isMandatory());
			assertNotNull(bind.getLabels());
			assertFalse(bind.getLabels().isEmpty());
			assertEquals(2, bind.getLabels().size());
			assertEquals("label it 0", bind.getLabels().get("it"));
			assertEquals("label en 0", bind.getLabels().get("en"));
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testFormConfigurationXML_3() throws Throwable {
		try {
			JSONObject json = XML.toJSONObject(XML_NO_PROFILE);
			FormConfiguration form = new FormConfiguration(json);
			assertEquals("myGreatCampaign", form.getCampaignId());
			assertEquals(JpSalesforceConstants.NO_PROFILE_KEY, form.getProfileId());
			assertNotNull(form.getBindings());
			assertFalse(form.getBindings().isEmpty());
			assertEquals(4, form.getBindings().size());
			FormBinding bind = form.getBindings().get(0);
			assertEquals("firstname", bind.getLeadField());
			assertEquals(0, bind.getId());
//			assertEquals("fullname", bind.getProfileField());
			assertTrue(bind.isMandatory());
			assertNotNull(bind.getLabels());
			assertFalse(bind.getLabels().isEmpty());
			assertEquals(2, bind.getLabels().size());
			assertEquals("label it 0", bind.getLabels().get("it"));
			assertEquals("label en 0", bind.getLabels().get("en"));
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private String JSON_BIND = "{\"id\":0,\"leadField\":\"firstname\",\"labels\":{\"it\":\"label it\",\"en\":\"label en\"},\"isMandatory\":true,\"profileField\":\"fullname\"}";
	private String JSON_FORM = "{\"campaignId\":\"myGreatCampaign\",\"bindings\":[{\"id\":0,\"leadField\":\"firstname\",\"labels\":{\"it\":\"label it 0\",\"en\":\"label en 0\"},\"isMandatory\":false,\"profileField\":\"fullname\"},{\"id\":1,\"leadField\":\"lang\",\"labels\":{\"it\":\"label it 1\",\"en\":\"label en 1\"},\"isMandatory\":true,\"profileField\":\"language\"},{\"id\":2,\"leadField\":\"mail\",\"labels\":{\"it\":\"label it 2\",\"en\":\"label en 2\"},\"isMandatory\":true,\"profileField\":\"email\"},{\"id\":3,\"leadField\":\"birth\",\"labels\":{\"it\":\"label it 3\",\"en\":\"label en 3\"},\"isMandatory\":true,\"profileField\":\"birthdate\"}],\"profileId\":\"PFL\"}";
	private String XML_FORM = "<form><campaignId>myGreatCampaign</campaignId><bindings><id>0</id><leadField>firstname</leadField><labels><it>label it 0</it><en>label en 0</en></labels><isMandatory>false</isMandatory><profileField>fullname</profileField></bindings><bindings><id>1</id><leadField>lang</leadField><labels><it>label it 1</it><en>label en 1</en></labels><isMandatory>true</isMandatory><profileField>language</profileField></bindings><bindings><id>2</id><leadField>mail</leadField><labels><it>label it 2</it><en>label en 2</en></labels><isMandatory>true</isMandatory><profileField>email</profileField></bindings><bindings><id>3</id><leadField>birth</leadField><labels><it>label it 3</it><en>label en 3</en></labels><isMandatory>true</isMandatory><profileField>birthdate</profileField></bindings><profileId>PFL</profileId></form>";
	private String XML_FORM_PROFILE_ONLY = "<form><campaignId>myGreatCampaign</campaignId><bindings><id>0</id><leadField>firstname</leadField><labels><it>label it 0</it><en>label en 0</en></labels><isMandatory>false</isMandatory><profileField>fullname</profileField></bindings><profileId>LFP</profileId></form>";
	private String XML_NO_PROFILE = "<form><campaignId>myGreatCampaign</campaignId><bindings><id>0</id><leadField>firstname</leadField><labels><it>label it 0</it><en>label en 0</en></labels><isMandatory>true</isMandatory></bindings><bindings><id>1</id><leadField>LastName</leadField><labels><it>Cognome</it><en>Last name</en></labels><isMandatory>true</isMandatory></bindings><bindings><id>2</id><leadField>Email</leadField><labels><it>posta elettronica</it><en>email</en></labels><isMandatory>true</isMandatory></bindings><bindings><id>3</id><leadField>Country</leadField><labels><it>Nazione</it><en>Country</en></labels><isMandatory>true</isMandatory></bindings><profileId>no-prof_</profileId></form>";
}
