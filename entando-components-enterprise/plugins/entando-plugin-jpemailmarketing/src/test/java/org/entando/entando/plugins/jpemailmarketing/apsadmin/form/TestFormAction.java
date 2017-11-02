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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpemailmarketing.apsadmin.JpemailmarketingApsAdminBaseTestCase;

import com.opensymphony.xwork2.Action;

public class TestFormAction extends JpemailmarketingApsAdminBaseTestCase {

	/*
INSERT INTO jpemailmarketing_campaign VALUES (1, 'Course', 1, '2014-05-14 14:30:00', '2014-05-14 14:30:00');
INSERT INTO jpemailmarketing_form VALUES (1, NULL, NULL, 'todo subject', 'todo text', 'todo email button', NULL, '2014-05-14 14:30:00', '2014-05-14 14:30:00');

	 */
	
	public void testEdit() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		params.put("courseId", "1");
		String result = this.executeAction("edit", params);
		assertEquals(Action.SUCCESS, result);
		
		FormAction action = (FormAction) this.getAction();
		assertEquals(1, action.getCourseId());
		assertNull(action.getFileCoverName());
		assertNull(action.getFileIncentiveName());
		assertEquals("todo subject", action.getEmailSubject());
	}

	public void testSave_WithErrors() throws Throwable {
		Map<String, String> params = new HashMap<String, String>();
		String result = this.executeAction("save", params);
		assertEquals(Action.INPUT, result);
		
		FormAction action = (FormAction) this.getAction();
		Map<String, List<String>> fieldErrors = action.getFieldErrors();
		assertEquals(3, fieldErrors.size());
		assertTrue(fieldErrors.containsKey("emailSubject"));
		assertTrue(fieldErrors.containsKey("emailText"));
		assertTrue(fieldErrors.containsKey("emailButton"));
		
		//--
		params.put("emailSubject", "test string");
		result = this.executeAction("save", params);
		assertEquals(Action.INPUT, result);
		
		action = (FormAction) this.getAction();
		fieldErrors = action.getFieldErrors();
		assertEquals(2, fieldErrors.size());
		assertFalse(fieldErrors.containsKey("emailSubject"));	
	}

/*
	public void testNew() throws Throwable {
		//TODO complete test
		Map<String, String> params = new HashMap<String, String>();
		String result = this.executeAction("new", params);
		assertEquals(Action.SUCCESS, result);
	}

	public void testTrash() throws Throwable {
		//TODO complete test
		Map<String, String> params = new HashMap<String, String>();
		String result = this.executeAction("trash", params);
		assertEquals(Action.INPUT, result);
	}
	
	public void testDelete() throws Throwable {
		//TODO complete test
		Map<String, String> params = new HashMap<String, String>();
		String result = this.executeAction("delete", params);
		assertEquals(Action.SUCCESS, result);
	}
*/
	
	
	private String executeAction(String action, Map<String, String> params) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction(NS, action);
		if (null != params) {
			this.addParameters(params);
		}
		String result = this.executeAction();
		return result;
	}

	private static final String NS = "/do/jpemailmarketing/Form";

}
