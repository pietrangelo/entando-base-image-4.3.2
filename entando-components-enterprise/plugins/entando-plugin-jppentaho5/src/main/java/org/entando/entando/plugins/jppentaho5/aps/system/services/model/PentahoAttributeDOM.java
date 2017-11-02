package org.entando.entando.plugins.jppentaho5.aps.system.services.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author entando
 */
public class PentahoAttributeDOM extends DefaultHandler {

    private static Logger _logger = LoggerFactory.getLogger(PentahoAttributeDOM.class);
    
	@Override
	public void startElement (String uri, String localName, String qName, Attributes attributes) throws SAXException  {
		if (qName.equalsIgnoreCase("parameters")) {
			_parameterMap = new HashMap<String, PentahoParameter>();
			_parameterList = new ArrayList<PentahoParameter>();
		} else if (qName.equalsIgnoreCase("parameter")) {
			_pentahoParameter = new PentahoParameter();

			String name = attributes.getValue("name");
			String isList = attributes.getValue("is-list");
			String isMandatory = attributes.getValue("is-mandatory");
			String isMultiSelect = attributes.getValue("is-multi-select");
			String isStrict = attributes.getValue("is-strict");
			String type = attributes.getValue("type");
			String timezoneHint = attributes.getValue("timezone-hint");

			_pentahoParameter.setName(name);
			_pentahoParameter.setIsList(Boolean.valueOf(isList));
			_pentahoParameter.setIsMandatory(Boolean.valueOf(isMandatory));
			_pentahoParameter.setIsMultiSelect(Boolean.valueOf(isMultiSelect));
			_pentahoParameter.setIsStrict(Boolean.valueOf(isStrict));
			_pentahoParameter.setType(type);
			_pentahoParameter.setTimezoneHint(timezoneHint);

//            System.out.println("name: " + name);
//            System.out.println(" isList " + isList);
//            System.out.println(" isMandatory " + isMandatory);
//            System.out.println(" isMultiSelect " + isMultiSelect);
//            System.out.println(" isStrict " + isStrict);
//            System.out.println(" type " + type);
//            System.out.println(" timezoneHint " + timezoneHint);

		} else if (qName.equalsIgnoreCase("attribute")) {
			_pentahoAttribute = new PentahoAttribute();

			String name = attributes.getValue("name");
			String namespace = attributes.getValue("namespace");
			String value = attributes.getValue("value");

			_pentahoAttribute.setName(name);
			_pentahoAttribute.setNamespace(namespace);
			_pentahoAttribute.setValue(value);

//            System.out.println("    name " + name);
//            System.out.println("    namespace " + namespace);
//            System.out.println("    value " + value);

			_pentahoParameter.getAttribute().put(name, _pentahoAttribute);

		} else if (qName.equalsIgnoreCase("values")) {
//            System.out.println(" VALUES " + localName);
		} else if (qName.equalsIgnoreCase("value")) {
//            System.out.println(" VALUE " + localName);
			_pentahoParameterValue = new PentahoParameterValue();

			String label = attributes.getValue("label");
			String isNull = attributes.getValue("null");
			String isSelected = attributes.getValue("selected");
			String type = attributes.getValue("type");
			String value = attributes.getValue("value");

			_pentahoParameterValue.setLabel(label);
			_pentahoParameterValue.setIsNull(Boolean.valueOf(isNull));
			_pentahoParameterValue.setSelected(Boolean.valueOf(isSelected));
			_pentahoParameterValue.setType(type);
			_pentahoParameterValue.setValue(value);

//            System.out.println("        label " + label);
//            System.out.println("        isNull " + isNull);
//            System.out.println("        isSelected " + isSelected);
//            System.out.println("        type " + type);
//            System.out.println("        value " + value);

			_pentahoParameter.getValues().put(value, _pentahoParameterValue);
                } else if (qName.equalsIgnoreCase("output-parameter")) {
                    _logger.warn("ignoring parameter 'output-parameter'");
                    if (null != attributes
                            && attributes.getLength() > 0)
                    {
                        _logger.warn("current attributes:");
                        for (int i = 0; i < attributes.getLength(); i++)
                        {
                            _logger.warn("attribute {}: class '{}' value '{}'",
                                    i,
                                    attributes.getLocalName(i).getClass().getCanonicalName(),
                                    attributes.getValue(i));
                        }
                    }
		} else {
                    if (StringUtils.isNotBlank(qName)) {
                        _logger.warn("unexpected Pentaho parameter '{}'", qName);
                    }
                    throw new RuntimeException("Unknown pentaho parameter declaration: " + qName);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("value")) {
			_pentahoParameterValue = null;
		}
		if (qName.equalsIgnoreCase("parameter")) {
			_parameterMap.put(_pentahoParameter.getName(), _pentahoParameter);
			_parameterList.add(_pentahoParameter);
			_pentahoParameter = null;
		}
		if (qName.equalsIgnoreCase("attribute")) {
			_pentahoAttribute = null;
		}
	}

	/*
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
//        System.out.println("    characters");
    }
	 */

	public Map<String, PentahoParameter> getParameterMap() {
		return _parameterMap;
	}
	
	public List<PentahoParameter> getParameterList() {
		return _parameterList;
	}

	private Map<String, PentahoParameter> _parameterMap;
	private List<PentahoParameter> _parameterList;

	PentahoParameter _pentahoParameter;
	PentahoAttribute _pentahoAttribute;
	PentahoParameterValue _pentahoParameterValue;
}
