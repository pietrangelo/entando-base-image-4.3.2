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
package org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol.InputControl;
import org.entando.entando.plugins.jpjasper.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.util.ApsProperties;
import com.opensymphony.xwork2.Action;

public class TestReportViewerConfigAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testInitConfigReportViewer() throws Throwable {
		String uri = "/reports/samples/Cascading_multi_select_report";
		//uri = "/reports/samples/Freight";
		
		String result = this.executeConfigReportViewer(USERNAME, "homepage", "1", REPORT_WIDGET_TYPE_CODE);
		assertEquals(Action.SUCCESS, result);
		ReportViewerConfigAction action = (ReportViewerConfigAction) this.getAction();
		Widget widget = action.getWidget();
		assertNotNull(widget);
		assertEquals(0, widget.getConfig().size());
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("pageCode", "homepage");
		params.put("frame", "1");
		params.put("widgetTypeCode", REPORT_WIDGET_TYPE_CODE);
		params.put("uriString", uri);
		
		result = this.executeEntryConfigReportViewer(USERNAME, params);
		action = (ReportViewerConfigAction) this.getAction();
		assertEquals(Action.SUCCESS, result);
		widget = action.getWidget();
		ApsProperties config = widget.getConfig();
		assertNotNull(config);
		JasperResourceDescriptor report = action.getReport(config.getProperty("uriString"));
		assertNotNull(report);
		List<JasperResourceDescriptor> icList = report.getInputControls();
		assertNotNull(icList);
		assertEquals(3, icList.size());
		
		Map<String, InputControl> icListV2 = action.getReportInputControlsConfigMap();
		for (int i = 0; i < icListV2.size(); i++) {
			System.out.println("ID: " + icListV2.get(i).getId() + " TYPE:" + icListV2.get(i).getType() );
		}
	}
	
	private String executeConfigReportViewer(String userName, String pageCode, String frame, String widgetTypeCode) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/Page/SpecialWidget", "jpjasperReportViewerConfig");
		this.addParameter("pageCode", pageCode);
		this.addParameter("frame", frame);
		if (null != widgetTypeCode && widgetTypeCode.trim().length()>0) {
			this.addParameter("widgetTypeCode", widgetTypeCode);
		}
		return this.executeAction();
	}
	
	private String executeEntryConfigReportViewer(String username,	Map<String, String> params) throws Throwable {
		this.initAction(NS, "entryReportConfig");
		this.addParameters(params);
		return this.executeAction();
	}
	
	private void init() throws Exception {
    	try {
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		//this._roleManager = (IRoleManager) this.getService(SystemConstants.ROLE_MANAGER);
    		//this._groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
    		this._userManager.removeUser(USERNAME);
    		
    		User admin = (User) this._userManager.getUser("admin");
    		
    		User user = new User();
			user.setUsername(USERNAME);
			user.setPassword(USERNAME);
			//user.setAuthorities(admin.getAuthorities());
			this._userManager.addUser(user);
			
			//List<IApsAuthority> roles = ((IApsAuthorityManager)this._roleManager).getAuthorizationsByUser(admin);
			//List<IApsAuthority> groups = ((IApsAuthorityManager)this._groupManager).getAuthorizationsByUser(admin);
			
			//((IApsAuthorityManager)this._roleManager).setUserAuthorizations(USERNAME, roles);
			//((IApsAuthorityManager)this._groupManager).setUserAuthorizations(USERNAME, groups);
			
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
	@Override
	protected void tearDown() throws Exception {
		this._userManager.removeUser(USERNAME);
		super.tearDown();
	}
	
    private IUserManager _userManager;
    private IGroupManager _groupManager;
    private IRoleManager _roleManager;
    
    private static final String USERNAME = "jasperadmin";
    private static final String REPORT_WIDGET_TYPE_CODE = "jpjasper_report";
    private static final String NS = "/do/jpjasper/Page/SpecialWidget/ReportConfig";
	
    /*
     * @deprecated Use {@link #REPORT_WIDGET_TYPE_CODE} instead
     */
    //private static final String REPORT_SHOWLET_TYPE_CODE = REPORT_WIDGET_TYPE_CODE;
}

