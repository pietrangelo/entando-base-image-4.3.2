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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.helper.EntityNaming;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormBinding;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.model.ApsEntity;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.CheckBoxAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ThreeStateAttribute;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;


public class SalesforceMessageDAO implements ISalesforceMessageDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceMessageDAO.class);

	protected void processLabels(String code, List<FormBinding> bindings) throws Throwable {
		String attrName = null;
		String key = null;
		
		for (FormBinding bind: bindings) {
			if (StringUtils.isNotBlank(bind.getProfileField()) // for bindings with no profiles
					&& !bind.getProfileField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
				attrName = bind.getProfileField();
			} else {
				attrName = bind.getLeadField();
			}
			// truncate to attribute boundary
			if (attrName.length() > 10) {
				attrName = attrName.substring(0, 10);
			}
			key = String.format(JpSalesforceConstants.WEB_DYNAMIC_FORM_LABEL_BASE, code, attrName);
			_logger.debug("Generating label for '{}'", key);
			
			ApsProperties labels = getI18nManager().getLabelGroup(key);
			
			if (null == labels) {
				getI18nManager().addLabelGroup(key, bind.getLabels());
			} else {
				getI18nManager().updateLabelGroup(key, bind.getLabels());
			}
		}
	}

	/**
	 * Process the binding and return an attribute to add to the new Message
	 * entity type. 
	 * @param bind
	 * @return
	 */
	protected AttributeInterface processFormBinding(FormBinding bind, Map<String, AttributeInterface> map) {
		AttributeInterface attr = null;
		AttributeInterface profileAttr = null;

		if (StringUtils.isNotBlank(bind.getProfileField()) // for binding with no profiles
				&& !bind.getProfileField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
			profileAttr = map.get(bind.getProfileField());
			_logger.debug("analysing attribute '{}' which is a '{}'", profileAttr.getName() , profileAttr.getClass().getCanonicalName());

			if (profileAttr.getClass().equals(MonoTextAttribute.class)) {
				attr = new MonoTextAttribute();

				attr.setType("Monotext");
			} else if (profileAttr.getClass().equals(TextAttribute.class)) {
				attr = new TextAttribute();

				attr.setType("Text");
			} else if (profileAttr.getClass().equals(BooleanAttribute.class)) {
				attr = new BooleanAttribute();

				attr.setType("Boolean");
			} else if (profileAttr.getClass().equals(CheckBoxAttribute.class)) {
				attr = new CheckBoxAttribute();

				attr.setType("Checkbox");
			} else if (profileAttr.getClass().equals(DateAttribute.class)) {
				attr = new DateAttribute();

				attr.setType("Date");
			} else if (profileAttr.getClass().equals(EnumeratorAttribute.class)) {
				attr = new EnumeratorAttribute();
				//				String[] items = ((EnumeratorAttribute)profileAttr).getItems();
				String staticItems = ((EnumeratorAttribute)profileAttr).getStaticItems();
				String customSeparator = ((EnumeratorAttribute)profileAttr).getCustomSeparator();
				String extractorBeanName = ((EnumeratorAttribute)profileAttr).getExtractorBeanName();

				attr.setType("Enumerator");
				//				((EnumeratorAttribute)attr).setItems(items);
				((EnumeratorAttribute)attr).setStaticItems(staticItems);
				((EnumeratorAttribute)attr).setCustomSeparator(customSeparator);
				((EnumeratorAttribute)attr).setExtractorBeanName(extractorBeanName);
			} else if (profileAttr.getClass().equals(NumberAttribute.class)) {
				attr = new NumberAttribute();

				attr.setType("Number");
			} else if (profileAttr.getClass().equals(ThreeStateAttribute.class)) {
				attr = new ThreeStateAttribute();

				attr.setType("Threestate");
			} else {
				_logger.debug("Unknown mapping type '{}'", profileAttr.getClass().getCanonicalName());
			}
			// common settings
			_logger.debug("Assigning attribute name '{}'", profileAttr.getName());
			attr.setName(profileAttr.getName());
			attr.setValidationRules(profileAttr.getValidationRules());
			attr.setDisablingCodes(profileAttr.getDisablingCodes());
			attr.setSearchable(profileAttr.isSearchable());
//			attr.setRequired(profileAttr.isRequired());
			attr.setIndexingType(profileAttr.getIndexingType());
			attr.setDescription(profileAttr.getDescription());
			attr.setRoles(profileAttr.getRoles());
			attr.setDisablingCodes(profileAttr.getDisablingCodes());
		} else {
			attr = new MonoTextAttribute();
			String name = bind.getLeadField();

			// FIXME / TODO attribute length should really be a CONSTANT!
			if (name.length() > 10) {
				name = name.substring(0, 10);
			}
			_logger.debug("Creating Monotext attribute '{}'", name);
			attr.setName(name);
			attr.setType("Monotext");
			attr.setIndexingType("TEXT");
		}
		attr.setRequired(bind.isMandatory());
		return attr;
	}

	/**
	 * Parse the form configuration a create a new entity using the attribute
	 * taken from the profile whenever is possible.
	 * @param config
	 * @return
	 */
	protected IApsEntity parseFormConfiguration(FormConfiguration config, String code) {
		IApsEntity entityPrototype = new ApsEntity();
		Map<String, AttributeInterface> map = null;

		if (StringUtils.isNotBlank(config.getProfileId())
				&& !config.getProfileId().equals(JpSalesforceConstants.NO_PROFILE_KEY)) {
			IApsEntity profileAttr = getProfileManager().getEntityPrototype(config.getProfileId());
			map = profileAttr.getAttributeMap();
			}
		List<FormBinding> bindings = config.getBindings();
		// assign code
		entityPrototype.setTypeCode(code);
		// description
		String description = config.getDescription();
		entityPrototype.setTypeDescr(description);
		// bindings
		for (FormBinding bind: bindings) {
			AttributeInterface newAttribute = processFormBinding(bind, map);
			entityPrototype.addAttribute(newAttribute);
		}
		return entityPrototype;
	}

	/**
	 * Create a new form type. We do not make any cross check since we assume the
	 * configuration given is valid
	 * @param config
	 * @return
	 * @throws Throwable
	 */
	@Override
	public String createMessageEntity(FormConfiguration config) throws Throwable {
		String code = null;
		IApsEntity entityPrototype = null;
		try {
			// get the entity prototypes already present
			Map<String, IApsEntity> prototypeMap = getMessageManager().getEntityPrototypes();
			code = EntityNaming.generateEntityName(prototypeMap);
			_logger.debug("Creating new Message entity with code " + code);
			entityPrototype = parseFormConfiguration(config, code);
			((ApsEntityManager)getMessageManager()).addEntityPrototype(entityPrototype);
			// process labels
			processLabels(code, config.getBindings());
		} catch (Throwable t) {
			_logger.error("Failed to create a form entity type from the configuration", t);
			throw new RuntimeException("Failed to create form entity type from the configuration");
		}
		return code;
	}
	
	/**
	 * Create a new form type. We do not make any cross check since we assume the
	 * configuration given is always valid
	 * @param config
	 * @throws Throwable
	 */
	@Override
	public void updateMessageEntity(FormConfiguration config, String typeCode) throws Throwable {
		IApsEntity entityPrototype = null;
		try {
			// get the entity prototypes already present
			_logger.debug("Updating message entity with code " + typeCode);
			entityPrototype = parseFormConfiguration(config, typeCode);
			((ApsEntityManager)getMessageManager()).updateEntityPrototype(entityPrototype);
			// process labels
			processLabels(typeCode, config.getBindings());
		} catch (Throwable t) {
			_logger.error("Failed to create a form entity type from the configuration", t);
			throw new RuntimeException("Failed to create form entity type from the configuration");
		}
	}

	@Override
	public void deleteMessageEntity(String code) throws Throwable {
		try {
			((ApsEntityManager)getMessageManager()).removeEntityPrototype(code);
		} catch (Throwable t) {
			_logger.error("Failed to delete a form entity type from the configuration", t);
			throw new RuntimeException("Failed to delete a form entity type from the configuration");
		}
	}

	@Override
	public Map<String, String> prepareWebToLeadForm(Message message, FormConfiguration config) {
		Map<String, String> map = new HashMap<String, String>();

		if (null != message) {
			List<FormBinding> bindings = config.getBindings();

			for (FormBinding bind: bindings) {
				if (StringUtils.isNotBlank(bind.getLeadField())
						&& !bind.getLeadField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
					String key = bind.getLeadField();
					String attributeName = null;
					
					if (bind.getProfileField().equals(JpSalesforceConstants.NO_BINDING_KEY)) {
						attributeName = key;
					} else {
						attributeName = bind.getProfileField();
					}
					AttributeInterface attr = message.getAttributeMap().get(attributeName);
					String value = null;

					if (attr instanceof MonoTextAttribute) {
						value = ((MonoTextAttribute)attr).getText();
					} else if (attr instanceof NumberAttribute) {
						value = ((NumberAttribute)attr).getNumber();
					} else if (attr instanceof  BooleanAttribute) {
						value = ((BooleanAttribute)attr).getBooleanValue().toString();
					} else {
						throw new RuntimeException("Unknown attribute when mapping message to web2lead ".concat(attr.getClass().getCanonicalName()));
					}
					_logger.debug("Adding mapping for the web2lead '{}':'{}'", key, value);
					map.put(key, value);
				}
			}
		}
		return map;
	}

	public IMessageManager getMessageManager() {
		return _messageManager;
	}


	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}

	public IUserProfileManager getProfileManager() {
		return _profileManager;
	}


	public void setProfileManager(IUserProfileManager profileManager) {
		this._profileManager = profileManager;
	}

	public II18nManager getI18nManager() {
		return _i18nManager;
	}

	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}
	private IMessageManager _messageManager;
	private IUserProfileManager _profileManager;

	private II18nManager _i18nManager;
}
