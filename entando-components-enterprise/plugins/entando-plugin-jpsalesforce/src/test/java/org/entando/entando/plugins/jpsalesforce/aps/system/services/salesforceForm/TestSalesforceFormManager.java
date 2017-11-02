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

import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceBaseTestCase;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.SalesforceForm;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;

import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;


public class TestSalesforceFormManager extends JpSalesforceBaseTestCase {

	public void testLoadForm() throws Throwable {
		try {
			SalesforceForm form = getSalesforceFormManager().getSalesforceForm(2677);
			assertNull(form);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	
	public void testAddForm() throws Throwable {
		SalesforceForm mock = mockForm(ID);
		SalesforceForm form = null;
		
			try {
				getSalesforceFormManager().addSalesforceForm(mock);
				assertFalse(mock.getId() == ID);
				form = getSalesforceFormManager().getSalesforceForm(mock.getId());
				assertNotNull(form);
				assertEquals(mock.getId(), form.getId());
				assertEquals(mock.getCode(), form.getCode());
				assertEquals(mock.getFrame(), form.getFrame());
				assertEquals(mock.getPagecode(), form.getPagecode());
			} catch (Throwable t) {
				throw t;
			} finally {
				if (null != form) {
					getSalesforceFormManager().deleteSalesforceForm(mock.getId());
					form = getSalesforceFormManager().getSalesforceForm(mock.getId());
					assertNull(form);
				}
			}
	}

	public void testAddUpdate() throws Throwable {
		SalesforceForm mock = mockForm(ID);
		SalesforceForm form = null;
		int size = 0;
		int actual = 0;
		try {
			List<Integer> list = getSalesforceFormManager().getSalesforceForms();
			size = list.size();
			//add it
			getSalesforceFormManager().addUpdateSalesforceForm(mock);
			list = getSalesforceFormManager().getSalesforceForms();
			actual = list.size();
			assertEquals(++size, actual);
			assertFalse(mock.getId() == ID);
			form = getSalesforceFormManager().getSalesforceForm(mock.getId());
			assertNotNull(form);
			assertEquals(mock.getId(), form.getId());
			assertEquals(mock.getCode(), form.getCode());
			assertEquals(mock.getFrame(), form.getFrame());
			assertEquals(mock.getPagecode(), form.getPagecode());
			// update it
			mock.setCode("APO");
			getSalesforceFormManager().addUpdateSalesforceForm(mock);
			list = getSalesforceFormManager().getSalesforceForms();
			actual = list.size();
			assertEquals(size, actual);
			form = getSalesforceFormManager().getSalesforceForm(mock.getId());
			assertNotNull(form);
			assertEquals(mock.getId(), form.getId());
			assertEquals("APO", form.getCode());
			assertEquals(mock.getFrame(), form.getFrame());
			assertEquals(mock.getPagecode(), form.getPagecode());
		} catch (Throwable t) {
			throw t;
		} finally {
			getSalesforceFormManager().deleteSalesforceForm(mock.getId());
			form = getSalesforceFormManager().getSalesforceForm(mock.getId());
			assertNull(form);
		}
	}
	
	public void testAddFormType() throws Throwable {
		SalesforceForm cfg = mockForm(ID);
		String code = null;
		
		try {
			code = getSalesforceFormManager().addFrameForm(cfg);
			assertFalse(ID == cfg.getId());
			assertFalse(CODE == cfg.getCode());
			assertNotNull(code);
			assertEquals(3, code.length());
			IApsEntity prototype = getMessageManager().getEntityPrototype(code);
			assertNotNull(prototype);
			Map<String, AttributeInterface> attrs = prototype.getAttributeMap();
			assertNotNull(attrs);
			assertFalse(attrs.isEmpty());
			assertEquals(4, attrs.size());
			Object attr = prototype.getAttribute(LANGUAGE);
			assertNotNull(attr);
			attr = prototype.getAttribute(BIRTHDATE);
			assertNotNull(attr);
			attr = prototype.getAttribute(FULLNAME);
			assertNotNull(attr);
			attr = prototype.getAttribute(EMAIL);
			assertNotNull(attr);
		} catch (Throwable t) {
			throw t;
		} finally {
			((IEntityTypesConfigurer)getMessageManager()).removeEntityPrototype(code);
		}
	}
	
	private SalesforceForm mockForm(int id) throws Throwable {
		SalesforceForm form = new SalesforceForm();
		FormConfiguration config = getFormProfileBindingConfiguration();
		
		form.setId(id);
		form.setCode("POB");
		form.setConfig(config);
		form.setPagecode("page");
		form.setFrame(3281);
		
		
		return form;
	}
	
	public final static int ID = 2677;
	public final static String CODE = "POB";
}

