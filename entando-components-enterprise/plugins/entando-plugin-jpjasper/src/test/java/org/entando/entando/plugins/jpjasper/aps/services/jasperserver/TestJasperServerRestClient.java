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
package org.entando.entando.plugins.jpjasper.aps.services.jasperserver;

import java.io.InputStream;
import java.util.List;

import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperRunReportResponse;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;

public class TestJasperServerRestClient extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testMe() {
		assertNotNull(_jasperServerManager);
	}

	public void testGetLoggedClient() throws Throwable {
		String username = "jasperadmin";
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);
			String cookie = ((JasperServerManager)this._jasperServerManager).getCookieHeader(userDetails);
			assertNotNull(cookie);
			assertTrue(cookie.contains("JSESSIONID"));

		} finally {
			this._userManager.removeUser(username);
		}
	}

	public void testSearchRestResources_1() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);

			String query = null;
			String type = null;
			String folder = null;
			boolean recursive = false;
			System.out.println("SEARCH ALL");
			
			String cookie = this._jasperServerManager.getCookieHeader(userDetails);
			
			
			List<JasperResourceDescriptor> list  = this._jasperServerManager.searchRestResources(cookie, query, type, folder, recursive);
			assertEquals(15, list.size());
			for (int i = 0; i < list.size(); i++) {
				JasperResourceDescriptor res = list.get(i);
				System.out.println("TIPO: " + res.getWsType() + " Name: " + res.getName() + " Label: " +  res.getLabel() + " Descr: " + res.getDescription() );
			}

		} finally {
			this._userManager.removeUser(username);
		}
	}

	public void testSearchRestResources_2() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);

			System.out.println("SEARCH REPORT UNITS ");
			String query = null;
			String type = WsTypeParams.REPORT_UNIT;
			String folder = null;
			boolean recursive = false;
			String cookie = this._jasperServerManager.getCookieHeader(userDetails);
			List<JasperResourceDescriptor> list  = this._jasperServerManager.searchRestResources(cookie, query, type, folder, recursive);
			assertEquals(46, list.size());

		} finally {
			this._userManager.removeUser(username);
		}
	}

	public void testSearchRestResources_3() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);

			System.out.println("SEARCH INSIDE FOLDER");
			String query = null;
			String type = WsTypeParams.REPORT_UNIT;
			String folder = "reports/samples";
			boolean recursive = false;
			String cookie = this._jasperServerManager.getCookieHeader(userDetails);
			List<JasperResourceDescriptor>  list  = this._jasperServerManager.searchRestResources(cookie, query, type, folder, recursive);
			assertEquals(15, list.size());

		} finally {
			this._userManager.removeUser(username);
		}
	}


	/*
	public void _____testSearchRestResources() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);


			//			ApacheHttpClient restClient = this._jasperServerManager.getRestClient(userDetails);
			//			assertNotNull(restClient);

			//File f = ((JasperServerManager)this._jasperServerManager).getReport(userDetails);

			//this._jasperServerManager.jasperListRepository(userDetails, "", null, false);


			System.out.println("AAAAAAAA");

			String query = null;
			String type = null;
			String folder = null;
			boolean recursive = false;
			List<JasperResourceDescriptor> list  = ((JasperServerManager)this._jasperServerManager).searchRestResources(userDetails, query, type, folder, recursive);
			assertEquals(15, list.size());
			for (int i = 0; i < list.size(); i++) {
				JasperResourceDescriptor res = list.get(i);
				System.out.println("TIPO: " + res.getWsType() + " Name: " + res.getName() + " Label: " +  res.getLabel() + " Descr: " + res.getDescription() );
			}

			System.out.println("BBBBBBBB");
			query = null;
			type = WsTypeParams.REPORT_UNIT;
			folder = null;
			recursive = false;
			list  = ((JasperServerManager)this._jasperServerManager).searchRestResources(userDetails, query, type, folder, recursive);
			assertEquals(46, list.size());

			System.out.println("CCCCCCCCC");
			query = null;
			type = WsTypeParams.REPORT_UNIT;
			folder = "reports/samples";
			recursive = false;
			list  = ((JasperServerManager)this._jasperServerManager).searchRestResources(userDetails, query, type, folder, recursive);
			assertEquals(15, list.size());

			//assertNotNull(f);
		} finally {
			this._userManager.removeUser(username);
		}
	}
	 */

	public void testGetReport() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);

			String query = "All Accounts Report";
			String type = WsTypeParams.REPORT_UNIT;
			String folder = "reports/samples";
			boolean recursive = false;
			String cookie = this._jasperServerManager.getCookieHeader(userDetails);
			List<JasperResourceDescriptor>list  = ((JasperServerManager)this._jasperServerManager).searchRestResources(cookie, query, type, folder, recursive);
			assertEquals(1, list.size());

			JasperResourceDescriptor report = list.get(0);

			report = this._jasperServerManager.getRestResource(cookie, report.getUriString());

			//TODO test value
			System.out.println("COMPLETARE IL TEST CON IL CKECK DEI VALORI");

		} finally {
			this._userManager.removeUser(username);
		}
	}


	public void testRunReportV2() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);

			String query = "All";
			String type = WsTypeParams.REPORT_UNIT;
			String folder = "reports/samples";
			boolean recursive = false;
			String cookie = this._jasperServerManager.getCookieHeader(userDetails);
			List<JasperResourceDescriptor>list  = ((JasperServerManager)this._jasperServerManager).searchRestResources(cookie, query, type, folder, recursive);
			assertEquals(1, list.size());

			JasperResourceDescriptor report = list.get(0);
			System.out.println("TIPO: " + report.getWsType() + " Name: " + report.getName() + " Label: " +  report.getLabel() + " Descr: " + report.getDescription() );

			String format = "html";
			Object arguments = null;

			//String cookie = this._jasperServerManager.getCookieHeader(userDetails);
			InputStream is = this._jasperServerManager.runReportRestV2(cookie, report.getUriString(), format, null);
			System.out.println(is);

			//assertNotNull(f);
		} finally {
			this._userManager.removeUser(username);
		}
	}

	public void testRunReport() throws Exception {
		String username = "jasperadmin";
		this._userManager.removeUser(username);
		UserDetails userDetails = null;
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(username);
			this._userManager.addUser(user);
			userDetails = this._userManager.getUser(username);
			assertNotNull(userDetails);

			String query = "All";
			String type = WsTypeParams.REPORT_UNIT;
			String folder = "reports/samples";
			boolean recursive = false;
			String cookie = this._jasperServerManager.getCookieHeader(userDetails);
			List<JasperResourceDescriptor>list  = ((JasperServerManager)this._jasperServerManager).searchRestResources(cookie, query, type, folder, recursive);
			assertEquals(1, list.size());

			JasperResourceDescriptor report = list.get(0);
			System.out.println("TIPO: " + report.getWsType() + " Name: " + report.getName() + " Label: " +  report.getLabel() + " Descr: " + report.getDescription() );

			String format = "html";
			Object arguments = null;

			//String x = ((JasperServerManager)this._jasperServerManager).getRestV2ReportsEndpointUrl() + report.getUriString();
			
			
			
			System.out.println("oooooooooooooook");
			String imageUri = "http://www.entando.com/do/action";
			JasperRunReportResponse jasperRunReportResponse = ((JasperServerManager)this._jasperServerManager).runReportRest(cookie,report.getUriString(),format,imageUri,1,null,null);
			assertNotNull(jasperRunReportResponse);
			System.out.println("UUID: " + jasperRunReportResponse.getUuid());

			//String dir = ((JasperServerManager)this._jasperServerManager).getReportOutDir();
			//((JasperServerManager)this._jasperServerManager).downloadReportFile(userDetails, jasperRunReportResponse, "report", dir);

			String myString = ((JasperServerManager)this._jasperServerManager).getReportFileAsString(cookie, jasperRunReportResponse.getUuid(),"report");

			System.out.println(myString);

			//assertNotNull(f);
		} finally {
			this._userManager.removeUser(username);
		}
	}




	private void init() {
		this._jasperServerManager = (IJasperServerManager) this.getApplicationContext().getBean("jpjasperServerManager");
		this._userManager = (IUserManager) this.getApplicationContext().getBean(SystemConstants.USER_MANAGER);
	}

	private IJasperServerManager _jasperServerManager;
	private IUserManager _userManager;
}
