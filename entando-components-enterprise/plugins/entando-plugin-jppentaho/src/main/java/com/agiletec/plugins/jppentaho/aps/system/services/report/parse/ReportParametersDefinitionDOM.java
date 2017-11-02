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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.ReportParameter;

/**
 * DOM for reading report parameters definition
 * 
 * @author zuanni G.Cocco
 * */
/*
 * 
 
<?xml version="1.0" encoding="UTF-8"?>
<parameters autoSubmitUI="true" is-prompt-needed="true"
	layout="vertical" subscribe="false">
	<parameter is-mandatory="false" is-multi-select="false"
		is-strict="true" name="idProgetto" type="java.lang.Integer">
		<attribute name="parameter-render-type"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="dropdown" />
		<attribute name="mandatory"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="true" />
		<attribute name="role"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="user" />
		<attribute name="label"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="Progetto" />
		<values>
			<value label="Assistenza Sardinia point" null="false" selected="false"
				type="java.lang.Integer" value="12" />
			<value label="Provincia Cagliari" null="false" selected="false"
				type="java.lang.Integer" value="13" />
			<value label="Manutenzione Sten Ambiente" null="false"
				selected="false" type="java.lang.Integer" value="15" />
			<value label="Entando" null="false" selected="false" type="java.lang.Integer"
				value="16" />
			<value label="Entando Open Source" null="false" selected="false"
				type="java.lang.Integer" value="17" />
		</values>
	</parameter>
	<parameter is-mandatory="false" is-multi-select="false"
		is-strict="false" name="dal" timzone-hint="+0100" type="java.util.Date">
		<attribute name="parameter-render-type"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="datepicker" />
		<attribute name="mandatory"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="false" />
		<attribute name="role"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="user" />
		<attribute name="label"
			namespace="http://reporting.pentaho.org/namespaces/engine/parameter-attributes/core"
			value="Dal" />
	</parameter>
	<errors>
		<error message="This parameter value is of an invalid value"
			parameter="idProgetto" />
	</errors>
</parameters>
 
*/
public class ReportParametersDefinitionDOM {
	
	private static final Logger _logger =  LoggerFactory.getLogger(ReportParametersDefinitionDOM.class);
	
	public ReportParametersDefinitionDOM(String xml) throws ApsSystemException {
		this.decodeDOM(xml);
	}

	public Map<String, ReportParameter> getReportParams() {
		Map<String, ReportParameter> rep = null;
		List<Element> elements = _document.getRootElement().getChildren("parameter");
		for (int i = 0 ; i < elements.size(); i++) {
			if (rep == null) {
				rep = new HashMap<String, ReportParameter>();
			}
			Element current = elements.get(i);
			ReportParameter repParam = this.extractReportParameter(current);
			rep.put(repParam.getName(), repParam);
		}
		return rep;
	}
	
	

	private ReportParameter extractReportParameter(Element current) {
		ReportParameter reportParameter = new ReportParameter();
		String name = current.getAttributeValue("name");
		reportParameter.setName(name);
		String type = current.getAttributeValue("type");
		reportParameter.setType(type);
		String isMultiSelect = current.getAttributeValue(ReportParameter.IS_MULTI_SELECT);
		reportParameter.setMultiSelect(new Boolean(isMultiSelect));
		
//		String isMandatory = current.getAttributeValue(ReportParameter.IS_MANDATORY_ATTR_NAME);
//		reportParameter.setMandatory(new Boolean(isMandatory));
		
		Element values = current.getChild("values");
		if (null != values) {
			 this.extractReportParameterValues(values, reportParameter);
		}
		this.extractParameterAttributes(current, reportParameter);
		return reportParameter;
	}

	private void extractParameterAttributes(Element current, ReportParameter reportParameter) {
		List<Element> attributeValues = current.getChildren("attribute");
		for (int i = 0; i < attributeValues.size(); i++) {
			Element attr = attributeValues.get(i);
			String name = attr.getAttributeValue("name");
			String value = attr.getAttributeValue("value");
			if (null != name && name.length() > 0 ) {
				if (name.equals(ReportParameter.LABEL_ATTR_NAME)) {
					reportParameter.setLabel(value);
				} else if (name.equals(ReportParameter.MANDATORY_ATTR_NAME)) {
					if (value.equals("true")) {
						reportParameter.setMandatory(true);
					}
					if (value.equals("false")) {
						reportParameter.setMandatory(false);
					}
				} 
				
				else if (name.equals(ReportParameter.HIDDEN_ATTR_NAME)) {
					if (value.equals("true")) {
						reportParameter.setHidden(true);
					}
					if (value.equals("false")) {
						reportParameter.setHidden(false);
					}
				} else if (name.equals(ReportParameter.PARAMETER_RENDER_TYPE_ATTR_NAME)) {
					reportParameter.setParameterRenderType(value);
				}	
			}
		}
	}

	private void extractReportParameterValues(Element current, ReportParameter reportParameter) {
		Map<String, String> valuesDefinition = new HashMap<String, String>();
		List<String> valuesNames = new ArrayList<String>();
		List<Element> values = current.getChildren("value");
		for (Element value : values) {
			String label = value.getAttributeValue("label");
			String val = value.getAttributeValue("value");
			valuesDefinition.put(label, val);
			valuesNames.add(label);
		}
		reportParameter.setValues(valuesDefinition);
		reportParameter.setValuesNames(valuesNames);
	}

	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			_document = builder.build(reader);
		} catch (Throwable t) {
			_logger.error("Error parsing xml: {}", xmlText, t);
			throw new ApsSystemException("Errore nel parsing della definizione dei parametri del report", t);
		}
	}
	
	private Document _document;
}

	