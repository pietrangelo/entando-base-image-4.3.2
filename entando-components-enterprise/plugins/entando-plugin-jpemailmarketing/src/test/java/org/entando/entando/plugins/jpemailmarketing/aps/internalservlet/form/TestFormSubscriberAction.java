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
package org.entando.entando.plugins.jpemailmarketing.aps.internalservlet.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.apsadmin.JpemailmarketingApsAdminBaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;

public class TestFormSubscriberAction extends JpemailmarketingApsAdminBaseTestCase {

	private static final Logger _logger =  LoggerFactory.getLogger(TestFormSubscriberAction.class);
	
	public void testIntro() throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", "1");
		String result = this.executeAction("intro", params);
		assertEquals(Action.SUCCESS, result);
		FormSubscriberAction action = (FormSubscriberAction) this.getAction();
		assertEquals(FormSubscriberAction.ACTION_ENTRY, action.getStrutsAction());
	}
	
	public void testSignUp_withInvalidName() throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", "1");
		params.put("strutsAction", FormSubscriberAction.ACTION_ENTRY);
		params.put("email", "spuddu@entando.com");
		params.put("name", "");
		String result = this.executeAction("signup", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getFieldErrors(this.getAction());
		assertEquals(1, fieldErrors.size());
		assertTrue(fieldErrors.containsKey("name"));
		_logger.info(StringUtils.join(fieldErrors.values(),"\n"));
	}
	
	public void testSignUp_withInvalidEmail() throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", "1");
		params.put("strutsAction", FormSubscriberAction.ACTION_ENTRY);
		params.put("name", "spuddu");
		params.put("email", "spuddu");
		String result = this.executeAction("signup", params);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getFieldErrors(this.getAction());
		assertEquals(1, fieldErrors.size());
		assertTrue(fieldErrors.containsKey("email"));
		_logger.info(StringUtils.join(fieldErrors.values(),"\n"));
		
		params.put("email", "");
		result = this.executeAction("signup", params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getFieldErrors(this.getAction());
		assertEquals(1, fieldErrors.size());
		assertTrue(fieldErrors.containsKey("email"));
	}

	public void testSignUp_success() throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", "1");
		params.put("strutsAction", FormSubscriberAction.ACTION_ENTRY);
		params.put("name", "spuddu");
		params.put("email", "spuddu@entando.com");
		String result = this.executeAction("signup", params);
		Map<String, List<String>> fieldErrors = this.getFieldErrors(this.getAction());
		_logger.info(StringUtils.join(fieldErrors.values(),"\n"));
		assertEquals(0, fieldErrors.size());
		assertEquals(Action.SUCCESS, result);
		
		FormSubscriberAction action = (FormSubscriberAction) this.getAction();
		assertEquals(FormSubscriberAction.ACTION_SUCCESS, action.getStrutsAction());
		
		//TODO teardown
		_logger.info("--- complete the test ---");
	}
	
	
	private String executeAction(String action, Map<String, Object> params) throws Throwable {
		//this.setUserOnSession("admin");
		this.initAction(NS, action);
		if (null != params) {
			this.addParameters(params);
		}
		String result = this.executeAction();
		return result;
	}

	private static final String NS = "/do/FrontEnd/jpemailmarketing/Form";
}
