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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpsalesforce.aps.system.JpSalesforceConstants;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Lead;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.ISalesforceFormManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.SalesforceForm;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.config.SubscriptionConfig;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.parse.SubscriptionConfigDOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegDAO;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.UserRegManager;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.IUserRegConfig;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.event.AddNewMessageEvent;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.event.AddNewMessageObserver;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

/**
 * @author E.Santoboni
 */
public class PortalSubscriptionManager extends UserRegManager implements IPortalSubscriptionManager, AddNewMessageObserver {
	
	private static final Logger _logger =  LoggerFactory.getLogger(PortalSubscriptionManager.class);
	
	@Override
	public void init() throws Exception {
		//super.init();
		this.loadSubscriptionConfig();
		_logger.info(this.getClass().getName() + ": initialized ");
	}
	
	private void loadSubscriptionConfig() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(ENTANDO_SUBSCRIPTION_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration Item not found: " + ENTANDO_SUBSCRIPTION_CONFIG_ITEM);
			}
			SubscriptionConfigDOM userRegConfigDom = new SubscriptionConfigDOM();
			SubscriptionConfig config = userRegConfigDom.extractSubscriptionConfig(xml);
			this.setSubscriptionConfig(config);
		} catch (Throwable t) {
			_logger.error("error in loadSubscriptionConfig", t);
			throw new ApsSystemException("Error in init", t);
		}
	}
	
	/**
	 * Get the email from an answered form. If the form is bound to a profile we
	 * take the email value looking for the attribute by role.
	 * Otherwise we look for the email attribute from the prototype of the lead
	 * @param message
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getEmailFromSalesforceMessage(Message message) {
		String email = null;

		try {
			if (null != message) {
				String typeCode = message.getTypeCode();

				// check whether there is already a record for this configuration
				FieldSearchFilter codeFilter = new FieldSearchFilter("code", typeCode, false);
				FieldSearchFilter filters[] = {codeFilter};
				List<Integer> ids = getFormManager().searchSalesforceForms(filters);

				if (null != ids 
						&& !ids.isEmpty()) {
					SalesforceForm formConfig = getFormManager().getSalesforceForm(ids.get(0));
					String profileId = formConfig.getConfig().getProfileId();
					_logger.debug("the form configuration '{}' has a profile '{}' associted ", typeCode, profileId);
					
					if (StringUtils.isNotBlank(profileId) 
							&& !profileId.equals(JpSalesforceConstants.NO_PROFILE_KEY)) {
						// check for the email attribute by role
						AttributeInterface emailAttr = message.getAttributeByRole(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_MAIL);
						
						if (null != emailAttr 
								&& StringUtils.isNotBlank((String)emailAttr.getValue())) {
							email = (String) emailAttr.getValue();
						}
					} else {
						// check whether the fixed field for the email is present
						AttributeInterface emailAttr = (AttributeInterface) message.getAttribute(Lead.FIELD_LEAD_EMAIL);
						if (null != emailAttr
								&& StringUtils.isNotBlank((String)emailAttr.getValue())) {
							email = (String) emailAttr.getValue();
						} else {
							_logger.debug("Could not find Email attribute in the submitted message");
						}
					}
				} else {
					_logger.debug("Could not find any defintion for typecode '{}' ", typeCode);
				}
			} else {
				_logger.debug("Could not analyse message to get email from the answered form");
			}
		} catch (Throwable t) {
			_logger.error("Error getting the email from the message", t);
		}
		return email;
	}
	
	@Override
	public void updateFromNewMessage(AddNewMessageEvent event) {
		try {
			Message message = event.getMessage();
			String email = event.getUserEmail();

			// retrieve the email from the message
			if (StringUtils.isBlank(email)) {
				email = getEmailFromSalesforceMessage(message);
				_logger.debug("Getting email from  the message: '{}'", email);
			}

			if (null == message || null == email) {
				_logger.error("Message or user email null");
				return;
			}
			Collection<String> usernames = super.getUsernamesByEmail(email);
			if (null != usernames && !usernames.isEmpty()) {
				_logger.debug("Email " + email + " already used");
				return;
			}
			String username = email.substring(0, email.indexOf("@"));
			if (null != super.getUserManager().getUser(username)) {
				_logger.debug("username " + username + " already present");
				return;
			}
			SubscriptionConfig subscriptionConfig = this.getSubscriptionConfig();
			IUserProfile prototype = super.getUserProfileManager().getProfileType(subscriptionConfig.getUserprofileTypeCode());
			if (null == prototype) {
				_logger.debug("Profile type " + subscriptionConfig.getUserprofileTypeCode() + " doesn't exist");
				return;
			}
			prototype.setId(username);
			this.copyIn(message, prototype);
			this.subscribeUser(prototype);
		} catch (Throwable t) {
			_logger.error("error in updateFromNewMessage", t);
		}
	}
	
	protected void copyIn(Message message, IUserProfile profile) throws ApsSystemException {
		
		try {
			List<AttributeInterface> profileAttribute = profile.getAttributeList();
			for (int i = 0; i < profileAttribute.size(); i++) {
				AttributeInterface attribute = profileAttribute.get(i);
				AttributeInterface messageAttribute = (AttributeInterface) message.getAttribute(attribute.getName());
				if (null == messageAttribute || !messageAttribute.getType().equals(attribute.getType())) {
					_logger.debug("Skipping attribute " + attribute.getName());
					
//					_logger.debug("Message attribute null (name '" + attribute.getName() + "' "
//							+ "or not of the same type (type " + messageAttribute.getType() + "' vs '" + attribute.getType() + "'");
					continue;
				}
				if (!attribute.isSimple()) {
					_logger.debug("User profile attribute (name '" + attribute.getName() + "' is complex");
					continue;
				} else if (attribute instanceof TextAttribute) {
					TextAttribute pr = (TextAttribute) attribute;
					TextAttribute m = (TextAttribute) messageAttribute;
					pr.setTextMap(m.getTextMap());
				} else if (attribute instanceof MonoTextAttribute) {
					MonoTextAttribute pr = (MonoTextAttribute) attribute;
					MonoTextAttribute m = (MonoTextAttribute) messageAttribute;
					pr.setText(m.getText());
				} else if (attribute instanceof NumberAttribute) {
					NumberAttribute pr = (NumberAttribute) attribute;
					NumberAttribute m = (NumberAttribute) messageAttribute;
					pr.setValue(m.getValue());
				} else if (attribute instanceof BooleanAttribute) {
					BooleanAttribute pr = (BooleanAttribute) attribute;
					BooleanAttribute m = (BooleanAttribute) messageAttribute;
					pr.setBooleanValue(m.getBooleanValue());
				} else if (attribute instanceof DateAttribute) {
					DateAttribute pr = (DateAttribute) attribute;
					DateAttribute m = (DateAttribute) messageAttribute;
					pr.setDate(m.getDate());
				}
			}
		} catch (Throwable t) {
			_logger.error("error in copyIn", t);
			throw new ApsSystemException("Error creating profile", t);
		}
	}
	
	protected void subscribeUser(IUserProfile userProfile) throws ApsSystemException {
		try {
			super.init();
			User user = new User();
			user.setDisabled(true);
			user.setUsername(userProfile.getUsername());
			user.setProfile(userProfile);
			user.setPassword("");
			String token = this.createToken(userProfile.getUsername());
			//this.sendAlertRegProfile((IUserProfile) user.getProfile(), token);
			
			IUserRegConfig config = this.getConfig();
			SubscriptionConfig subscriptionConfig = this.getSubscriptionConfig();
			String activationPageCode = config.getActivationPageCode();
			Map<String, String> params = super.prepareMailParams(userProfile, token, activationPageCode);
			super.sendAlert(subscriptionConfig.getActivationTemplates(), params, userProfile);
			
			this.getUserManager().addUser(user);
			this.getUserRegDAO().addActivationToken(userProfile.getUsername(), token, new Date(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
		} catch (Throwable t) {
			_logger.error("error in subscribeUser", t);
			throw new ApsSystemException("Error in Account registration", t);
		}
	}
	
	/*
	private void sendAlertRegProfile(IUserProfile profile, String token) throws ApsSystemException {
		IUserRegConfig config = this.getConfig();
		String activationPageCode = config.getActivationPageCode();
		Map<String, String> params = this.prepareMailParams(profile, token, activationPageCode);
		//TODO CREARE IL TEMPLATE DA CONFIGURAZIONE CUSTOM
		this.sendAlert(config.getActivationTemplates(), params, profile);
	}
	*/
	
	@Override
	public SubscriptionConfig getSubscriptionConfig() {
		return _subscriptionConfig;
	}
	protected void setSubscriptionConfig(SubscriptionConfig subscriptionConfig) {
		this._subscriptionConfig = subscriptionConfig;
	}
	
	public ISalesforceFormManager getFormManager() {
		return _formManager;
	}

	public void setFormManager(ISalesforceFormManager formManager) {
		this._formManager = formManager;
	}
	
	private SubscriptionConfig _subscriptionConfig;
	
	private ISalesforceFormManager _formManager;
}
