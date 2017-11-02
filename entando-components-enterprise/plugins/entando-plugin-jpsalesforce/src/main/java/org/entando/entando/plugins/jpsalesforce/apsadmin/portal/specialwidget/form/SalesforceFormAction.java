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
package org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.ISalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Campaign;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.ISalesforceFormManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.SalesforceForm;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormBinding;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;

public class SalesforceFormAction extends SimpleWidgetConfigAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceFormAction.class);
		
	/**
	 * Used to get all the localized fields coming from the form
	 */
	private void fetchLocalizedFields() { 
		List<Lang> langs = getLangManager().getLangs();
		
		_labels.clear();
		for (Lang curLang: langs) {
			String currentLangCode = curLang.getCode();
			String labelParam = LABEL_PREFIX.concat(currentLangCode);
			String labelValue = this.getRequest().getParameter(labelParam);
			
			if (StringUtils.isNotBlank(labelValue)) {
				_labels.put(currentLangCode, labelValue.trim());
			} else {
				this.addActionError(getText("jpsalesforce.error.field.label.missing", new String[] {currentLangCode.toUpperCase()}));
			}
		}
	}

	/**
	 * Load the profiles and the campaigns available and the existing
	 * configuration, if any
	 */
	@Override
	public String init() {
		String result = super.init();
		
		try {
			// get the widget configuration
			String formId = 
					getWidget().getConfig().getProperty(JpSalesforceConstants.CONFIG_FORM_ID);
			String formProtectionType =
					getWidget().getConfig().getProperty(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_WIDGET_PARAM);
			// load the existing form configuration
			if (StringUtils.isNotBlank(formId)) {
				SalesforceForm form = getSalesforceFormManager().getSalesforceForm(Integer.valueOf(formId));
				FormConfiguration formConfig = form.getConfig();
				this.getRequest().getSession().setAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG, 
						formConfig);
				setCampaignId(formConfig.getCampaignId());
				setProfileId(formConfig.getProfileId());
				setFormProtectionType(formProtectionType);
				// load the esisting message title
				Map<String, SmallMessageType> types = getMessageManager().getSmallMessageTypesMap();
				if (null != types) {
					SmallMessageType type = types.get(form.getCode());
					if (null != type) {
						setFormDescription(type.getDescr());
					} else {
						_logger.error("Could not find the description of the given entity code");
					}
				} else {
					_logger.error("Could not find the given entity");
				}
			}
			if (result.equals(SUCCESS)) {
				SalesforceAccessDescriptor sad = getSessionLoginAccessor();
				List<Campaign> campaignsID = this.getSalesforceManager().getCampaigns(sad);
				for (Campaign camp: campaignsID) {
					String key = camp.getId();
					String value = camp.getName();
					_logger.debug("adding campaing '{}' with id '{}'", value, key);
					_campaigns.put(key, value);
				}
				// profiles
				List<IApsEntity> userProfileTypes = new ArrayList<IApsEntity>();
				userProfileTypes.addAll(this.getProfileManager().getEntityPrototypes().values());
				for (IApsEntity profile : userProfileTypes) {
					String value = profile.getTypeDescr();
					String key = profile.getTypeCode();
					_logger.debug("adding profile '{}' with id '{}'", value, key);
					_profiles.put(key, value);
				}
				_profiles.put(JpSalesforceConstants.NO_PROFILE_KEY, getText("jpsalesforce.no.profile"));
			}
		} catch (Throwable t) {
			_logger.error("Error retrieving campaigns and profiles list", t);
			return FAILURE;
		}
		return result;
	}
	
	/**
	 * Start the form configuration. Collect all the fields of the lead and then:
	 * @return
	 */
	public String configure() {
		List<String> profileFields = new ArrayList<String>();
		List<String> leadFields = new ArrayList<String>();
		List<String> reqFields = new ArrayList<String>();
		
		try {
			// retrieve the access descriptor 
			SalesforceAccessDescriptor sad = getSessionLoginAccessor();
			// get the fields of the Lead
			Lead leadAttr = getSalesforceManager().getLeadPrototype(sad);
			for (String leadField: leadAttr.getAttributes()) {
				leadFields.add(leadField);
			}
			if (!_profileId.equals(JpSalesforceConstants.NO_PROFILE_KEY)) {
				// get the fields of the profile
				IApsEntity profileAttr = getProfileManager().getEntityPrototype(_profileId);
				Map<String, AttributeInterface> map = profileAttr.getAttributeMap();
				if (null != map) {
					for (String attrName: map.keySet()) {
						profileFields.add(attrName);
						AttributeInterface iface = map.get(attrName);
						if (iface.isRequired()) {
							reqFields.add(attrName);
						}
					}
				}
			}
			_logger.debug("Configuring  widget {} page {} frame in the configuration", 
					getWidgetTypeCode(), getPageCode(), getFrame());
			// create the configuration item in the session
			FormConfiguration config = getSessionFormConfiguration();
			// decide whether to discard the existing configuration
			if (_profileId.equals(config.getProfileId())
					&& _campaignId.equals(config.getCampaignId())) {
				if (null != config.getBindings()) {
					// remove from the availability list (and the required attribute list) existing binding attributes
					for (FormBinding binding: config.getBindings()) {
						leadFields.remove(binding.getLeadField());
						profileFields.remove(binding.getProfileField());
						reqFields.remove(binding.getProfileField());
					}
				}
			} else {
				// the configuration in the session is no longer valid
				_logger.debug("Purging invalid session configuration");
				config = createSessionFormConfiguration();
			}
			// set lists with required and available attributes
			config.getLeadFields().clear();
			config.setLeadFields(leadFields);
			config.getProfileFields().clear();
			config.setProfileFields(profileFields);
			config.getProfileFields().clear();
			config.setRequiredProfileFields(reqFields);
			config.setDescription(getFormDescription());
			config.setProtectionType(getFormProtectionType());
			config.setDescription(getFormDescription());
		} catch (Throwable t) {
			_logger.error("Error retrieving campaigns and profiles list", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Add a new lead - profile  field association 
	 * @return
	 */
	public String add() {
		FormBinding binding =  null;
		try {
			fetchLocalizedFields();
			if (null != getActionErrors() 
					&& !getActionErrors().isEmpty()) {
				return INPUT;
			}
			// check for invalid binding
			if (_bindLeadField.equals(JpSalesforceConstants.NO_BINDING_KEY)
					&& (null != _bindProfileField 
					&& _bindProfileField.equals(JpSalesforceConstants.NO_BINDING_KEY))) {
				this.addActionError(getText("jpsalesforce.error.field.binding.invalid"));
				return INPUT;
			}
			FormConfiguration cfg = this.getSessionFormConfiguration();
			if (getStrutsAction() != ApsAdminSystemConstants.EDIT) {
				binding = new FormBinding(getBindLeadField(), 
						getBindProfileField(), isMandatory(), _labels, cfg.getBindings().size());
				cfg.addFieldAssociation(binding);
			} else {
				// validation has only checked against number format, not the presence itself
				if (null == _bindingId) {
					return INPUT;
				}
				// void strutsAction
				setStrutsAction(ApsAdminSystemConstants.ADD);
				// load the binding to edit
				binding = cfg.getBindings().get(_bindingId);
			}
			binding.setMandatory(_mandatory);
			binding.setLabels(_labels);
		} catch (Throwable t) {
			_logger.error("Error adding form association", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 *  Swap a binding up in the list
	 * @return
	 */
	public String moveUp() {
		FormConfiguration cfg = null;

		try {
			cfg = getSessionFormConfiguration();
			cfg.swap(_bindingId - 1, _bindingId);
		} catch (Throwable t) {
			_logger.error("error moving up binding", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Swap a binding down in the list
	 * @return
	 */
	public String moveDown() {
		FormConfiguration cfg = null;

		try {
			cfg = getSessionFormConfiguration();
			cfg.swap(_bindingId, _bindingId + 1);
		} catch (Throwable t) {
			_logger.error("error moving down binding", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Delete a binding between two fields
	 * @return
	 */
	public String delete() {
		try {
			FormConfiguration cfg = this.getSessionFormConfiguration();
			if (cfg.getBindings().size() > getBindingId()) {
				cfg.deleteFieldAssociation(getBindingId());
			} else {
				_logger.debug("Cowardly I refuse to delete an unknown bind '{}' (refreshing page?)", getBindingId());
			}
		} catch (Throwable t) {
			_logger.error("Error adding form association", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Edit a binding
	 * @return
	 */
	public String edit() {
		try {
			FormConfiguration cfg = getSessionFormConfiguration();
			FormBinding binding = cfg.getBindings().get(_bindingId);
			// set the default values of the field
			String leadField = binding.getLeadField();
			String profileField = binding.getProfileField();
			setBindLeadField(leadField);
			setBindProfileField(profileField);
			// set struts action
			setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error editing a binding", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Do nothing but returning to the main list
	 * @return
	 */
	public String cancel() {
		return SUCCESS;
	}
	
	/**
	 * Save widget configuration. We are essentially mocking the configuration
	 * of the web dynamic form / publish message widget
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String save() {
		String result = null;
		SalesforceForm form = null;
		
		try {
			FormConfiguration config = getSessionFormConfiguration();
			
			if (null != config.getRequiredProfileFields()
					&& !config.getRequiredProfileFields().isEmpty()) {
				List<String> reqList = config.getRequiredProfileFields();
				// check if the remaining attributes contains required ones
				for (String profileField: config.getRemainingProfileFields()) {

					if (null != reqList
							&& reqList.contains(profileField)) {
						this.addActionError(getText("jpsalesforce.error.field.profile.required",
								new String[]{profileField}));
					}
				}
				// check for errors
				for (FormBinding binding: config.getBindings()) {
						String profileField = binding.getProfileField();
						
					if (reqList.contains(profileField)
							&& !binding.isMandatory()) {
						this.addActionError(getText("jpsalesforce.error.field.profile.mandatory",
								new String[]{profileField}));
					}
				}
			}
			if (null != getActionErrors() 
					&& !getActionErrors().isEmpty()) {
				return INPUT;
			}
			form = new SalesforceForm(getPageCode(), getFrame(), config);
			getSalesforceFormManager().addFrameForm(form);
			result = saveWidget(form);
			setTypeCode(form.getCode());
		} catch (Throwable t) {
			_logger.error("Error saving widget configuration", t);
			return  FAILURE;
		}
		return "redirectWebformConfig";
	}
	
	/**
	 * Here's the trick! Save the data in the same format expected by the web 
	 * dynamic form plugin
	 * @param form
	 * @return
	 */
	private String saveWidget(SalesforceForm form) {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			Widget widget = getWidget();
			widget.getConfig().setProperty(JpSalesforceConstants.CONFIG_FORM_ID,
					String.valueOf(form.getId()));
			widget.getConfig().setProperty(JpwebdynamicformSystemConstants.TYPECODE_WIDGET_PARAM,
					form.getCode());
			widget.getConfig().setProperty(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_WIDGET_PARAM,
					form.getConfig().getProtectionType());
			IPage page = this.getPage(this.getPageCode());
			int strutsAction = (null != page.getWidgets()[this.getFrame()]) ? ApsAdminSystemConstants.ADD : ApsAdminSystemConstants.EDIT;
			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			_logger.debug("Saving Widget - code: {}, pageCode: {}, frame: {}", this.getWidget().getType().getCode(), this.getPageCode(), this.getFrame());
			this.addActivityStreamInfo(strutsAction, true);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return "configure";
	}
	
	/**
	 * Get the list of bindings in the form configuration
	 * @return
	 */
	public List<FormBinding> getBindings() {
		List<FormBinding> list = null;
		FormConfiguration cfg = 
				(FormConfiguration) this.getRequest().getSession().getAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG);
		
		if (null != cfg) {
			list = cfg.getBindings();
		}
		return list;
	}
	
	/**
	 * Return the desired binding from the configuration
	 * @param idx
	 * @return
	 */
	public FormBinding getBinding(int idx) {
		List<FormBinding> list = getBindings();
		FormBinding bind = null;
		
		if (null != list 
				&& !list.isEmpty()) {
			bind = list.get(idx);
		}
		return bind;
	}
	
	/**
	 * Used for a select, return the list of available lead fields
	 * available for association
	 * @return
	 */
	public List<String> getLeadFields() {
		List<String> list = null;
		
		try {
			FormConfiguration cfg = getSessionFormConfiguration();
			list = cfg.getRemainingLeadFields();
		} catch (Throwable t) {
			_logger.error("Error getting remaining lead fields", t);
		}
		return list;
	}
	
	/**
	 * Used for a select, return the list of available profile fields
	 * available for association
	 * @return
	 */
	public List<String> getProfileFields() {
		List<String> list = null;
		
		try {
			FormConfiguration cfg = getSessionFormConfiguration();
			list = cfg.getRemainingProfileFields();
		} catch (Throwable t) {
			_logger.error("Error getting remaining profile fields", t);
		}
		return list;
	}
	
	/**
	 * Get the label of the given binding in the desired language
	 * @param idx
	 * @param langCode 
	 * @return
	 */
	public String getLabel(String langCode) {
		String label = null;
		FormConfiguration cfg = 
				(FormConfiguration) this.getRequest().getSession().getAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG);
		if (StringUtils.isNotBlank(langCode)
				&& null != cfg) {
			FormBinding binding = cfg.getBindings().get(_bindingId);
			ApsProperties prop = binding.getLabels();
			label = prop.getProperty(langCode);
		}
		return label;
	}
	
	
	public String getNoBindingKey() {
		return JpSalesforceConstants.NO_BINDING_KEY;
	}
	
	public String getNoBindingValue() {
		return getText("jpsalesforce.label.no.binding");
	}
	
	/**
	 * Get the form protection types
	 * @return
	 */
	public List<SelectItem> getFormProtectionTypeSelectItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (int i = 0; i < JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPES.length; i++) {
			String key = JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPES[i];
			items.add(new SelectItem(key, this.getText("label.formProtectionType." + key)));
		}
		return items;
	}
	
	/**
	 * Get the Salesforce access descriptor in session. If none is found the 
	 * (unmanned) login is is performed
	 * @return
	 */
	private SalesforceAccessDescriptor getSessionLoginAccessor() throws Throwable {
		SalesforceAccessDescriptor sad = 
				(SalesforceAccessDescriptor) this.getRequest().getSession().getAttribute(JpSalesforceConstants.SESSIONPARAM_UNMANNED_SAD);
		if (null == sad 
				|| !sad.isValid()) {
			sad = this.getSalesforceManager().login();
			this.getRequest().getSession().setAttribute(JpSalesforceConstants.SESSIONPARAM_UNMANNED_SAD, sad);
		}
		return sad;
	}
	
	/**
	 * Return the current form configuration in the session. If none is found a
	 * new one is created given that all the needed fields are present
	 * @return
	 * @throws Throwable
	 */
	private FormConfiguration getSessionFormConfiguration() throws Throwable {
		FormConfiguration cfg = 
				(FormConfiguration) this.getRequest().getSession().getAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG);
		if (null == cfg) {
			cfg = new FormConfiguration(_campaignId, _profileId);
			 this.getRequest().getSession().setAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG, cfg);
		}
		return cfg;
	}
	
	/**
	 * Create a new form configuration
	 * @return
	 * @throws Throwable
	 */
	private FormConfiguration createSessionFormConfiguration() throws Throwable {
			 this.getRequest().getSession().removeAttribute(JpSalesforceConstants.SESSIONPARAM_FORM_CONFIG);
		return getSessionFormConfiguration();
	}
	
	public ISalesforceManager getSalesforceManager() {
		return _salesforceManager;
	}
	public void setSalesforceManager(ISalesforceManager salesforceManager) {
		this._salesforceManager = salesforceManager;
	}
	public IUserProfileManager getProfileManager() {
		return _profileManager;
	}
	public void setProfileManager(IUserProfileManager profileManager) {
		this._profileManager = profileManager;
	}
	public Map<String, String> getCampaigns() {
		return _campaigns;
	}
	public void setCampaigns(Map<String, String> campaigns) {
		this._campaigns = campaigns;
	}
	public Map<String, String> getProfiles() {
		return _profiles;
	}
	public void setProfiles(Map<String, String> profiles) {
		this._profiles = profiles;
	}
	public String getCampaignId() {
		return _campaignId;
	}
	public void setCampaignId(String campaignId) {
		this._campaignId = campaignId;
	}
	public String getProfileId() {
		return _profileId;
	}
	public void setProfileId(String profileId) {
		this._profileId = profileId;
	}
	public String getBindLeadField() {
		return _bindLeadField;
	}
	public void setBindLeadField(String bindLeadField) {
		this._bindLeadField = bindLeadField;
	}
	public String getBindProfileField() {
		return _bindProfileField;
	}
	public void setBindProfileField(String bindProfileField) {
		this._bindProfileField = bindProfileField;
	}
	public boolean isMandatory() {
		return _mandatory;
	}	
	public void setMandatory(boolean mandatory) {
		this._mandatory = mandatory;
	}
	public Integer getBindingId() {
		return _bindingId;
	}
	public void setBindingId(Integer bindingId) {
		this._bindingId = bindingId;
	}
	public ISalesforceFormManager getSalesforceFormManager() {
		return _salesforceFormManager;
	}
	public void setSalesforceFormManager(ISalesforceFormManager salesforceFormManager) {
		this._salesforceFormManager = salesforceFormManager;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getFormId() {
		return _formId;
	}
	
	public void setFormId(String formId) {
		this._formId = formId;
	}
	public String getFormProtectionType() {
		return _formProtectionType;
	}
	public void setFormProtectionType(String formProtectionType) {
		this._formProtectionType = formProtectionType;
	}
	public String getFormDescription() {
		return _formDescription;
	}
	public void setFormDescription(String formDescription) {
		this._formDescription = formDescription;
	}
	
	//
	// STEP 1
	//

	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	public IMessageManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}

	// input types
	private String _formId;
	private String _campaignId;
	private String _profileId;
	private String _formProtectionType;
	private String _formDescription;
	
	// selects
	private Map<String, String> _campaigns = new HashMap<String, String>();
	private Map<String, String> _profiles = new HashMap<String, String>();
	
	//
	//  STEP 2
	//
	
	// input types
	private String _bindLeadField;
	private String _bindProfileField;
	private Integer _bindingId;
	private boolean _mandatory;
	private int _strutsAction;
	private String _typeCode;
	private ApsProperties _labels = new ApsProperties();
	
	// class stuff
	private ISalesforceManager _salesforceManager;
	private ISalesforceFormManager _salesforceFormManager;
	private IUserProfileManager _profileManager;
	private IMessageManager _messageManager;
	
	// Label parameters prefix 
	public static final String LABEL_PREFIX = "label_";	

}
