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
package org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.publish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.ISalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.SalesforceAccessDescriptor;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Campaign;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.ISalesforceFormManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.SalesforceForm;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageTypeNotifierConfig;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;

public class PublicationAction extends SimpleWidgetConfigAction {

	private Logger _logger = LoggerFactory.getLogger(PublicationAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			String typeCode = this.getTypeCode();
			if (typeCode == null || this.getMessageManager().getSmallMessageTypesMap().get(typeCode) == null) {
				this.addFieldError("typeCode", this.getText("Errors.typeCode.required"));
			} else {
				List<String> protectionTypes = Arrays.asList(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPES);
				if (null != this.getFormProtectionType() && this.getFormProtectionType().trim().length() > 0) {
					if (!protectionTypes.contains(this.getFormProtectionType())) {
						this.addFieldError("formProtectionType", this.getText("Errors.formProtectionType.invalid"));
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate");
		}
	}
	
	@Override
	public String init() {
		String result = super.init();
		try {
			if (result.equals(SUCCESS)) {
				ApsProperties config = this.getWidget().getConfig();
				String typeCode = config.getProperty(JpwebdynamicformSystemConstants.TYPECODE_WIDGET_PARAM);
				this.setTypeCode(typeCode);
				String protectionType = config.getProperty(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_WIDGET_PARAM);
				this.setFormProtectionType(protectionType);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "init");
			return FAILURE;
		}
		return result;
	}
	
	public List<SmallMessageType> getMessageTypes() {
		List<SmallMessageType> webdynamicForms = null;
		List<SmallMessageType> list = new ArrayList<SmallMessageType>();
		try {
			webdynamicForms = this.getMessageManager().getSmallMessageTypes();
			List<Integer> salesforceForms = getSalesforceFormManager().getSalesforceForms();
			
			for (int i = 0; i < salesforceForms.size(); i++) {
				SalesforceForm salesforceForm = getSalesforceFormManager().getSalesforceForm(salesforceForms.get(i));
				
				for (SmallMessageType webdynamicForm: webdynamicForms) {
					if (salesforceForm.getCode().equals(webdynamicForm.getCode())) {
						list.add(webdynamicForm);
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getMessageTypes");
			throw new RuntimeException("Error searching message types", t);
		}
		
		return list;
	}
	
	
	public String chooseCampaign() {
		String typeCode = getTypeCode();
		
		try {
			Map<String, SmallMessageType> types = getMessageManager().getSmallMessageTypesMap();
			
			if (null != types) {
				SmallMessageType type = types.get(typeCode);
				if (null != type) {
					setFormDescription(type.getDescr());
				} else {
					_logger.error("Could not find the description of the given entity code");
				}
			} else {
				_logger.error("Could not find the description of the given entity code");
			}
//			FieldSearchFilter frameFilter = new FieldSearchFilter("frame", Integer.valueOf(getFrame()), false);
//			FieldSearchFilter pageFilter = new FieldSearchFilter("pagecode", getPageCode(), false);
//			FieldSearchFilter filters[] = {frameFilter, pageFilter};
			FieldSearchFilter typeCodeFilter = new FieldSearchFilter("code", typeCode, false);
			FieldSearchFilter filters[] = {typeCodeFilter};
			List<Integer> ids = getSalesforceFormManager().searchSalesforceForms(filters);
			if (null != ids 
					&& !ids.isEmpty()) {
				SalesforceForm form = getSalesforceFormManager().getSalesforceForm(ids.get(0));
				String campaignId = form.getConfig().getCampaignId();
				setCampaignId(campaignId);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "chooseCampaign");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		
		try {
			int frame = getFrame();
			String pageCode = getPageCode();
//			FieldSearchFilter frameFilter = new FieldSearchFilter("frame", Integer.valueOf(frame), false);
//			FieldSearchFilter pageFilter = new FieldSearchFilter("pagecode", pageCode, false);
			FieldSearchFilter typeCodeFilter = new FieldSearchFilter("code", _typeCode, false);
			FieldSearchFilter filters[] = {typeCodeFilter};
			List<Integer> ids = getSalesforceFormManager().searchSalesforceForms(filters);
			if (null != ids 
					&& !ids.isEmpty()) {
				SalesforceForm form = getSalesforceFormManager().getSalesforceForm(ids.get(0));
				FormConfiguration formConfig = form.getConfig();
				String campaignId = formConfig.getCampaignId();
				// TODO prendere il nome della campagna
				
				if (campaignId.equals(_campaignId)) {
					// same campaign, do nothing
					_logger.debug("Publishing a form with unchanged campaign");
					setTypeCode(_typeCode); // useless but safer
				} else {
					ApsSystemUtils.getLogger().debug("Must clone a form and associate it to another campaign");
					// clone the configuration changing campaign id
					FormConfiguration clonedConfig = formConfig.clone();
					clonedConfig.setCampaignId(_campaignId);
					clonedConfig.setDescription(_formDescription);
					SalesforceForm clonedForm = new SalesforceForm(pageCode, frame, clonedConfig);
					// create the new entity
					String clonedFormCode = getSalesforceFormManager().replaceFrameForm(clonedForm);
					// update configuration
					MessageTypeNotifierConfig notifierConfig = getMessageManager().getNotifierConfig(_typeCode);
					MessageTypeNotifierConfig clonedNotifierConfig = getMessageManager().getNotifierConfig(clonedFormCode);
					
					clonedNotifierConfig.setMailAttrName(notifierConfig.getMailAttrName());
					clonedNotifierConfig.setMessageModel(notifierConfig.getMessageModel());
					clonedNotifierConfig.setNotifiable(notifierConfig.isNotifiable());
					clonedNotifierConfig.setRecipientsBcc(notifierConfig.getRecipientsBcc());
					clonedNotifierConfig.setRecipientsCc(notifierConfig.getRecipientsCc());
					clonedNotifierConfig.setRecipientsTo(notifierConfig.getRecipientsTo());
					clonedNotifierConfig.setSenderCode(notifierConfig.getSenderCode());
					clonedNotifierConfig.setStore(notifierConfig.isStore());
//					clonedNotifierConfig.setTypeCode(notifierConfig.getTypeCode());
					getMessageManager().saveNotifierConfig(clonedNotifierConfig);
					setTypeCode(clonedFormCode);
					_logger.debug("Created new form with code " + clonedFormCode);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return super.save();
	}
	
	@Override
	protected void createValuedShowlet() throws Exception {
		Widget widget = this.createNewShowlet();
		List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();
		for (int i=0; i<parameters.size(); i++) {
			WidgetTypeParameter param = parameters.get(i);
			String paramName = param.getName();
			String value = this.getRequest().getParameter(paramName);
			// use the newly created entity type
			if (paramName.equals("typeCode")
					&& StringUtils.isNotBlank(_typeCode)) {
				value = _typeCode;
			}
			if (value != null && value.trim().length()>0) {
				widget.getConfig().setProperty(paramName, value);
			}
		}
		this.setShowlet(widget);
	}
	
	/**
	 * Get the campaigns available
	 * @return
	 */
	public Map<String, String> getCampaigns() {
		SalesforceAccessDescriptor sad = null;
		Map<String, String> campaigns = new HashMap<String, String>();

		try {
			sad = getSessionLoginAccessor();
			List<Campaign> campaignsID = this.getSalesforceManager().getCampaigns(sad);
			for (Campaign camp: campaignsID) {
				String key = camp.getId();
				String value = camp.getName();
				_logger.debug("adding campaing '{}' with id '{}'", value, key);
				campaigns.put(key, value);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCampaigns");
		}
		return campaigns;
	}
	
	
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
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	public String getFormProtectionType() {
		return _formProtectionType;
	}
	public void setFormProtectionType(String formProtectionType) {
		this._formProtectionType = formProtectionType;
	}
	public ISalesforceManager getSalesforceManager() {
		return _salesforceManager;
	}
	public void setSalesforceManager(ISalesforceManager salesforceManager) {
		this._salesforceManager = salesforceManager;
	}
	public ISalesforceFormManager getSalesforceFormManager() {
		return _salesforceFormManager;
	}
	public void setSalesforceFormManager(
			ISalesforceFormManager salesforceFormManager) {
		this._salesforceFormManager = salesforceFormManager;
	}
	public IUserProfileManager getProfileManager() {
		return _profileManager;
	}
	public void setProfileManager(IUserProfileManager profileManager) {
		this._profileManager = profileManager;
	}
	protected IMessageManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	public String getCampaignId() {
		return _campaignId;
	}
	public void setCampaignId(String campaignId) {
		this._campaignId = campaignId;
	}
	public String getFormDescription() {
		return _formDescription;
	}
	public void setFormDescription(String formDescription) {
		this._formDescription = formDescription;
	}


	private String _typeCode;
	private String _formProtectionType;
	private String _campaignId;
	private String _formDescription;
	
	private ISalesforceManager _salesforceManager;
	private ISalesforceFormManager _salesforceFormManager;
	private IUserProfileManager _profileManager;
	private IMessageManager _messageManager;
	
}
