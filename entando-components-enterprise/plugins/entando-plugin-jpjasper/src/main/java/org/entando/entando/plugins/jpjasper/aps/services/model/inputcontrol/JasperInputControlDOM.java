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
package org.entando.entando.plugins.jpjasper.aps.services.model.inputcontrol;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

public class JasperInputControlDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperInputControlDOM.class);
	
	private static final String ELEM_INPUT_CONTROL = "inputControl";

	public List<InputControl> parseXML(String xml) throws ApsSystemException {
		List<InputControl> list = new ArrayList<InputControl>();
		Element root = this.getRootElement(xml);
		
		this.extractInputControls(root, list);
		return list;
	}
	
	private void extractInputControls(Element root, List<InputControl> list) throws ApsSystemException {
		List<Element> inputControls = root.getChildren(ELEM_INPUT_CONTROL);
		if (null != inputControls && !inputControls.isEmpty()) {
			Iterator<Element> iterator = inputControls.iterator();
			while (iterator.hasNext()) {
				Element current = iterator.next();
				InputControl ic = this.parseInputControl(current);
				list.add(ic);
			}
		}
	}

	private InputControl parseInputControl(Element current) throws ApsSystemException {
		InputControl control = null;
		try {
			control = new InputControl();
			
			/*
<id>Country_multi_select</id>
<label>Country multi select</label>
<mandatory>true</mandatory>
			 */
			control.setId(current.getChildText("id"));
			control.setLabel(current.getChildText("label"));
			String mandatory = current.getChildText("mandatory");
			control.setMandatory(null != mandatory && mandatory.equalsIgnoreCase("true"));
			
			/*
<masterDependencies/>
<readOnly>false</readOnly>
<slaveDependencies>
<controlId>Cascading_name_single_select</controlId>
<controlId>Cascading_state_multi_select</controlId>
</slaveDependencies>
			 */
			
			Element masterDependencies = current.getChild("masterDependencies");
			if (null != masterDependencies && null != masterDependencies.getChildren("controlId")) {
				List<Element> list = masterDependencies.getChildren("controlId");
				for (int i = 0; i < list.size(); i++) {
					Element dep = list.get(i);
					control.getMasterDependencies().add(dep.getValue());
				}
			}
			
			String readonly = current.getChildText("readOnly");
			control.setReadOnly(null != readonly && readonly.equalsIgnoreCase("true"));
			
			Element slavaDependencies = current.getChild("slaveDependencies");
			if (null != slavaDependencies && null != slavaDependencies.getChildren("controlId")) {
				List<Element> list = slavaDependencies.getChildren("controlId");
				for (int i = 0; i < list.size(); i++) {
					Element dep = list.get(i);
					control.getSlaveDependencies().add(dep.getValue());
				}
			}
			
			Element stateElement = current.getChild("state");
			if (null != stateElement) {
				XMLOutputter outp = new XMLOutputter();
				String s = outp.outputString(stateElement);
				control.setStateXML(s);
				
				InputControlState state = this.parseStateElement(stateElement);
				control.setState(state);
				
			}

			/*
<type>multiSelect</type>
<uri>repo:/reports/samples/Cascading_multi_select_report_files/Country_multi_select</uri>
	<validationRules>
	<mandatoryValidationRule>
	<errorMessage>This field is mandatory so you must enter data.</errorMessage>
	<inverted>false</inverted>
	</mandatoryValidationRule>
	</validationRules>
<visible>true</visible>
			 */
			
			control.setType(current.getChildText("type"));
			
			String visible = current.getChildText("visible");
			control.setVisible(null != visible && visible.equalsIgnoreCase("true"));
			
			Element validationRules = current.getChild("validationRules");
			if (null != validationRules) {
				XMLOutputter outp = new XMLOutputter();
				String s = outp.outputString(validationRules);
				control.setValidationRulesXML(s);
			}

		} catch (Throwable t) {
			_logger.error("error in parseInputControl", t);
			throw new ApsSystemException("error in parseInputControl", t);
		}
		return control;
	}

	private InputControlState parseStateElement(Element stateElement) {
		InputControlState state = new InputControlState();
		state.setId(stateElement.getChildText("id"));
		state.setUri(stateElement.getChildText("uri"));
		Element valueEl = stateElement.getChild("value");
		Element errorEl = stateElement.getChild("error");
		if (null != valueEl) {
			state.setValue(valueEl.getValue());
		}
		if (null != errorEl) {
			state.setError(errorEl.getValue());
		}
		Element optionsEl = stateElement.getChild("options");
		if (null != optionsEl) {
			List<InputControlStateOption> options = this.parseOptions(optionsEl);
			state.setOptions(options);
		}
		return state;
	}

	private List<InputControlStateOption> parseOptions(Element optionsEl) {
		List<InputControlStateOption> options = new ArrayList<InputControlStateOption>();
		Iterator<Element> it = optionsEl.getChildren("option").iterator();
		while (it.hasNext()) {
			Element optionEl = it.next();
			InputControlStateOption option = new InputControlStateOption();
			option.setLabel(optionEl.getChildText("label"));
			
			String selected = optionEl.getChildText("selected");
			option.setSelected(null != selected && selected.equalsIgnoreCase("true"));

			option.setValue(optionEl.getChildText("value"));
			options.add(option);
		}
		return options;
	}

	/**
	 * Returns the Xml element from a given text.
	 * @param xmlText The text containing an Xml.
	 * @return The Xml element from a given text.
	 * @throws ApsSystemException In case of parsing exceptions.
	 */
	private Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			_logger.error("Error parsing xml: {}", xmlText, t);
			throw new ApsSystemException("Error parsing xml", t);
		}
		return root;
	}

}
