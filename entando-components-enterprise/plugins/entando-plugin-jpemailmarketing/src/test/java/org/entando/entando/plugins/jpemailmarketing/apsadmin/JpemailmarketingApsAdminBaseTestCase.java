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
package org.entando.entando.plugins.jpemailmarketing.apsadmin;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.agiletec.ConfigTestUtils;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.ActionSupport;

import org.entando.entando.plugins.jpemailmarketing.JpemailmarketingConfigTestUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.internalservlet.form.FormSubscriberAction;

public class JpemailmarketingApsAdminBaseTestCase extends ApsAdminBaseTestCase {

	protected Map<String, List<String>> getFieldErrors(ActionSupport actionSupport) {
		FormSubscriberAction action = (FormSubscriberAction) this.getAction();
		return action.getFieldErrors();
	}
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new JpemailmarketingConfigTestUtils();
	}

    protected void setInitParameters(Properties params) {
    	params.setProperty("config", 
    				"struts-default.xml," +
    				"struts-plugin.xml," +
    				"struts.xml," +
    				"entando-struts-plugin.xml," +
    				"japs-struts-plugin.xml");
    }
    
    // TODO Edit this constant before running your junit test
   // private static final String CUSTOM_STRUTS_PLUGIN = "change_me-jpemailmarketing-struts-plugin.xml";
}
