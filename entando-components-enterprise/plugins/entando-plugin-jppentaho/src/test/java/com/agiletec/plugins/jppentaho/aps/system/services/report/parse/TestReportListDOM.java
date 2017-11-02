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
package com.agiletec.plugins.jppentaho.aps.system.services.report.parse;

import java.util.List;

import com.agiletec.plugins.jppentaho.aps.JppentahoBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportHttpParam;
import com.agiletec.plugins.jppentaho.aps.system.services.report.parse.ReportListDOM;

public class TestReportListDOM extends JppentahoBaseTestCase {
	
	public void test() throws ApsSystemException {
		ReportListDOM reportListDOM = new ReportListDOM(XML)	;
		
		List<ReportHttpParam> param = reportListDOM.getReportParams();
		assertNotNull(param);
		assertEquals(param.size(), 2);
		
		ReportHttpParam param1 = param.get(0);
		assertNotNull(param1);
		
		assertEquals(param1.getName(), "Elenco Progetti.prpt");
		assertEquals(param1.getPath(), "");
		assertEquals(param1.getSolution(), "agiletec");
		assertEquals(param1.getDescription(), "Elenco Progetti");
		assertEquals(param1.getTitle(), "/pentaho-solutions/agiletec/Elenco Progetti.prpt");
	}
	
	private String XML = "<tree>" +
			"	<leaf>" +
		"<leafText>Elenco Progetti</leafText>" +
		"<link>http://192.168.184.13:8080/pentaho/content/reporting/reportviewer/report.html?solution=agiletec&amp;path=&amp;name=Elenco Progetti.prpt&amp;userid=admin&amp;password=admin|1288196683464|1|I2jgsSZnmAo4sgZuuiDJ9tbmexk=" +
		"</link>" +
		"<target>_blank</target>" +
		"<title>/pentaho-solutions/agiletec/Elenco Progetti.prpt</title>" +
	"</leaf>" +
	"<leaf>" +
		"<leafText>ProgettiDipOreCostoXLS</leafText>" +
		"<link>http://192.168.184.13:8080/pentaho/content/reporting/reportviewer/report.html?solution=agiletec&amp;path=&amp;name=ProgettiDipOreCostoXLS.prpt&amp;userid=admin&amp;password=admin|1288196683464|1|I2jgsSZnmAo4sgZuuiDJ9tbmexk=" +
		"</link>" +
		"<target>_blank</target>" +
		"<title>/pentaho-solutions/agiletec/ProgettiDipOreCostoXLS.prpt</title>" +
	"</leaf>" +
	"</tree>";
	
}