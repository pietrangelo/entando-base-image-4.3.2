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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services;

import org.entando.entando.plugins.jpemailmarketing.aps.JpemailmarketingBaseTestCase;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;

public class TestFormManager extends JpemailmarketingBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetForm() {
		//TODO complete test
		assertNotNull(this._formManager);
	}

	public void testGetForms() {
		//TODO complete test
		assertNotNull(this._formManager);
	}
	
	public void testSearchForms() {
		//TODO complete test
		assertNotNull(this._formManager);
	}

	public void testAddForm() {
		//TODO complete test
		assertNotNull(this._formManager);
	}

	public void testUpdateForm() {
		//TODO complete test
		assertNotNull(this._formManager);
	}

	public void testDeleteForm() {
		//TODO complete test
		assertNotNull(this._formManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._formManager = (IFormManager) this.getService("jpemailmarketingFormManager");
	}
	
	private IFormManager _formManager;
}

