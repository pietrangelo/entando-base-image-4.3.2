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

import java.util.Map;

import com.agiletec.plugins.jppentaho.aps.JppentahoBaseTestCase;

import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParameter;

public class TestReportParametersDefinitionDOM extends JppentahoBaseTestCase {
	
	public void testReadReportDef() throws Exception {
		_reportParametersDefinitionDOM = new ReportParametersDefinitionDOM(_xml);
		assertNotNull(_reportParametersDefinitionDOM);
		Map<String, ReportParameter> reportParameter = _reportParametersDefinitionDOM.getReportParams();
		assertNotNull(reportParameter);
		assertEquals(3, reportParameter.size());
		ReportParameter para1 = reportParameter.get("idProgetto");
		assertNotNull(para1);
		
		assertEquals("idProgetto", para1.getName());
		assertEquals("java.lang.Integer", para1.getType());
		assertEquals(false, para1.isMultiSelect());
		
		assertEquals(false, para1.isHidden());
		assertNotNull(para1.getValues());
		assertEquals(5, para1.getValues().size());
		assertNotNull(para1.getValues().get("jAPS"));
		assertEquals("16", para1.getValues().get("jAPS"));
		assertNotNull(para1.getParameterRenderType());
		assertEquals("dropdown", para1.getParameterRenderType());
		assertNotNull(para1.getLabel());
		assertEquals("Progetto", para1.getLabel());
		assertNotNull(para1.isMandatory());
		
		assertEquals(true, para1.isMandatory());
		
		ReportParameter para2 = reportParameter.get("dal");
		assertNull(para2.getValues());
		assertNotNull(para2.getParameterRenderType());
		assertEquals("datepicker", para2.getParameterRenderType());
		assertNotNull(para2.getLabel());
		assertEquals("Dal", para2.getLabel());
		assertNotNull(para2.isMandatory());
		assertEquals(false, para2.isMandatory());
		
		assertEquals(5, para1.getValuesNames().size());
		assertEquals("Assistenza Sardinia point", para1.getValuesNames().get(0));
		ReportParameter para3 = reportParameter.get("line");
		assertNotNull(para3.getValues());
		Map<String, String> values = para3.getValues();
		assertNotNull(values);
		assertEquals(7, para3.getValues().size());
		assertEquals("line", para3.getName());
		assertNotNull(para3.getParameterRenderType());
		assertEquals("togglebutton", para3.getParameterRenderType());
	}
	
	
	private ReportParametersDefinitionDOM _reportParametersDefinitionDOM;
	private String _xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<parameters autoSubmitUI=\"true\" is-prompt-needed=\"true\" " +
"	layout=\"vertical\" subscribe=\"false\">" +
"	<parameter is-mandatory=\"false\" is-multi-select=\"false\"" +
"		is-strict=\"true\" name=\"idProgetto\" type=\"java.lang.Integer\">" +
"		<attribute name=\"parameter-render-type\"" +
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\"" +
"			value=\"dropdown\" /> " +
"		<attribute name=\"mandatory\" " +
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\" " +
"			value=\"true\" /> " +
"		<attribute name=\"role\" " +
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\" " +
"			value=\"user\" /> " +
"		<attribute name=\"label\" " + 
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\" " +
"			value=\"Progetto\" /> " +
"		<values> " +
"			<value label=\"Assistenza Sardinia point\" null=\"false\" selected=\"false\" " +
"				type=\"java.lang.Integer\" value=\"12\" /> " +
"			<value label=\"Provincia Cagliari\" null=\"false\" selected=\"false\" " +
"				type=\"java.lang.Integer\" value=\"13\" /> " +
"			<value label=\"Manutenzione Sten Ambiente\" null=\"false\" " +
"				selected=\"false\" type=\"java.lang.Integer\" value=\"15\" /> " +
"			<value label=\"jAPS\" null=\"false\" selected=\"false\" type=\"java.lang.Integer\" " +
"				value=\"16\" /> " +
"			<value label=\"jAPS Open Source\" null=\"false\" selected=\"false\" " +
"				type=\"java.lang.Integer\" value=\"17\" /> " +
"		</values>" +
"	</parameter>" +
"	<parameter is-mandatory=\"true\" is-multi-select=\"false\" " +
"		is-strict=\"false\" name=\"dal\" timzone-hint=\"+0100\" type=\"java.util.Date\"> " +
"		<attribute name=\"parameter-render-type\" " + 
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\" " +
"			value=\"datepicker\" /> " +
"		<attribute name=\"mandatory\" " +
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\" " +
"			value=\"false\" /> " +
"		<attribute name=\"role\" " +
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\" " +
"			value=\"user\" /> " +
"		<attribute name=\"label\" " +
"			namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\" " +
"			value=\"Dal\" /> " +
"	</parameter> " +
"<parameter is-mandatory=\"false\" is-multi-select=\"true\"" +
"	is-strict=\"true\" name=\"line\" type=\"[Ljava.lang.String;\">" +
"	<attribute name=\"parameter-layout\"" +
"		namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\"" +
"		value=\"horizontal\" />" +
"	<attribute name=\"parameter-render-type\"" +
"		namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\"" +
"		value=\"togglebutton\" />" +
"	<attribute name=\"mandatory\"" +
"		namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\"" +
"		value=\"true\" />" +
"	<attribute name=\"role\"" +
"		namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\"" +
"		value=\"user\" />" +
"	<attribute name=\"label\"" +
"		namespace=\"http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core\"" +
"		value=\"Line\" />" +
"	<values>" +
"		<value label=\"Classic Cars\" null=\"false\" selected=\"true\"" +
"			type=\"java.lang.String\" value=\"Classic Cars\" />" +
"		<value label=\"Motorcycles\" null=\"false\" selected=\"false\"" +
"			type=\"java.lang.String\" value=\"Motorcycles\" />" +
"		<value label=\"Planes\" null=\"false\" selected=\"true\" type=\"java.lang.String\"" +
"			value=\"Planes\" />" +
"		<value label=\"Ships\" null=\"false\" selected=\"true\" type=\"java.lang.String\"" +
"			value=\"Ships\" />" +
"		<value label=\"Trains\" null=\"false\" selected=\"true\" type=\"java.lang.String\"" +
"			value=\"Trains\" />" +
"		<value label=\"Trucks and Buses\" null=\"false\" selected=\"false\"" +
"			type=\"java.lang.String\" value=\"Trucks and Buses\" />" +
"		<value label=\"Vintage Cars\" null=\"false\" selected=\"false\"" +
"			type=\"java.lang.String\" value=\"Vintage Cars\" />" +
"	</values>" +
"</parameter>" +
"	<errors>" +
"		<error message=\"This parameter value is of an invalid value\" " +
"			parameter=\"idProgetto\" /> " +
"	</errors> " +
"</parameters>";
	

}
