/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.portal;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.SelectItem;
import static com.agiletec.apsadmin.portal.AbstractPortalAction.CUSTOM_WIDGETS_CODE;
import static com.agiletec.apsadmin.portal.AbstractPortalAction.STOCK_WIDGETS_CODE;
import static com.agiletec.apsadmin.portal.AbstractPortalAction.USER_WIDGETS_CODE;
import com.agiletec.apsadmin.portal.WidgetTypeAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype.IMultisiteWidgetTypeManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteWidgetTypeAction extends WidgetTypeAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteWidgetTypeAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().containsKey("showletTypeCode")) {
			Map<String, List<String>> fieldErrors = this.getFieldErrors();
			fieldErrors.remove("showletTypeCode");
			this.setFieldErrors(fieldErrors);
			String widgetTypeCode = this.getShowletTypeCode().trim();

			String regexPattern = JpmultisiteSystemConstants.MULTISITE_CODE_REGEX;
			if (null != widgetTypeCode && !widgetTypeCode.matches(regexPattern)) {
				this.addFieldError("showletTypeCode", this.getText("wrongCharacters"));
			}
			if (null != widgetTypeCode && widgetTypeCode.length() > 30) {
				this.addFieldError("showletTypeCode", this.getText("wrongMaxLength"));
			}
			this.setWidgetTypeCode(widgetTypeCode);
		}
	}

	@Override
	protected String saveUserShowlet() {
		String saveUserShowlet = super.saveUserShowlet();
		reloadConfiguration();
		return saveUserShowlet;
	}

	@Override
	public String delete() {
		String delete = super.delete();
		reloadConfiguration();
		return delete; 
	}
	
	
	protected void reloadConfiguration(){
		try {
            ApsWebApplicationUtils.executeSystemRefresh(this.getRequest());
            _logger.info("Reload config started");
        } catch (Throwable t) {
        	_logger.error("error in reloadConfig", t);
        }
	}
	

	@Override
	public String getWidgetTypeCode() {
		String widgetTypeCode = super.getWidgetTypeCode(); 
		String multisiteCodeSuffix = MultisiteUtils.getMultisiteCodeSuffix(this.getRequest());
		if(StringUtils.isNotEmpty(multisiteCodeSuffix)){
			if(!widgetTypeCode.endsWith(multisiteCodeSuffix)){
				widgetTypeCode = widgetTypeCode.concat(multisiteCodeSuffix);
			}
		} 
		return widgetTypeCode; 
	}

	@Override
	protected Map<String, List<SelectItem>> getWidgetFlavoursMapping(List<String> pluginCodes) {
		Map<String, List<SelectItem>> mapping = new HashMap<String, List<SelectItem>>();
		_logger.debug("********************************** MULTISITE CODE: "+ MultisiteUtils.getMultisiteCodeSuffix(this.getRequest())+ " is blank?"+ StringUtils.isBlank(MultisiteUtils.getMultisiteCodeSuffix(this.getRequest())));
		List<WidgetType> types = this.getMultisiteWidgetTypeManager().getWidgetTypesByMultisiteCode(MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()));
		_logger.debug("***********TYPES******* size: " + types.size());
		for (int i = 0; i < types.size(); i++) {
			WidgetType type = types.get(i);
			String pluginCode = type.getPluginCode();
			if (null != pluginCode && pluginCode.trim().length() > 0) {
				//is a plugin's widgets
				if (!pluginCodes.contains(pluginCode)) {
					pluginCodes.add(pluginCode);
				}
				this.addFlavourWidgetType(pluginCode, type, mapping);
			} else if (type.isUserType()) {
				//is a user widgets
				this.addFlavourWidgetType(USER_WIDGETS_CODE, type, mapping);
			} else {
				//is a core widgets
				String typeCheck = StringUtils.remove(type.getCode(), MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()));
				if (this.getStockWidgetCodes().contains(typeCheck)) {
					this.addFlavourWidgetType(STOCK_WIDGETS_CODE, type, mapping);
				} else {
					this.addFlavourWidgetType(CUSTOM_WIDGETS_CODE, type, mapping);
				}
			}
		}
		Collections.sort(pluginCodes);
		return mapping;
	}
	
	@Override
	public List<Group> getGroups() {
		List<Group> groups = super.getGroups(); 
		List<Group> filteredGroups = new ArrayList<Group>();
		if(!MultisiteUtils.getMultisiteCode(this.getRequest()).isEmpty()){
			for (Group group : groups) {
				if(group.getName().equals(Group.FREE_GROUP_NAME) || group.getName().endsWith(MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()))){
					filteredGroups.add(group);
				}
			}
			groups = filteredGroups;
		}
		return groups; 
	}

	public IMultisiteWidgetTypeManager getMultisiteWidgetTypeManager() {
		return multisiteWidgetTypeManager;
	}

	public void setMultisiteWidgetTypeManager(IMultisiteWidgetTypeManager multisiteWidgetTypeManager) {
		this.multisiteWidgetTypeManager = multisiteWidgetTypeManager;
	}
	
	private IMultisiteWidgetTypeManager multisiteWidgetTypeManager;

}
