package org.entando.entando.plugins.jpactiviti.apsadmin.config;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.apsadmin.admin.BaseAdminAction;

public class ActivitiConfigAction extends BaseAdminAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(ActivitiConfigAction.class);
	
	@Override
	public String updateSystemParams() {
		try {
			this.initLocalMap();
			this.updateLocalParams(true);
			String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
			this.extractExtraParameters();
			String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, this.getSystemParams());
			this.getConfigManager().updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
			this.addActionMessage(this.getText("message.configSystemParams.ok"));
		} catch (Throwable t) {
			_logger.error("error in updateSystemParams", t);
			//ApsSystemUtils.logThrowable(t, this, "updateSystemParams");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void initLocalMap() throws Throwable {
		String xmlParams = this.getConfigManager().getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
		Map<String, String> systemParams = SystemParamsUtils.getParams(xmlParams);
		this.setSystemParams(systemParams);
	}
	
	/**
	 * Refresh the map of parameters with values fetched from the request
	 * @param keepOldParam when true, when a system parameter is not found in request, the previous system parameter will be stored
	 */
	private void updateLocalParams(boolean keepOldParam) {
		Iterator<String> paramNames = this.getSystemParams().keySet().iterator();
		while (paramNames.hasNext()) {
			String paramName = (String) paramNames.next();
			String newValue = this.getRequest().getParameter(paramName);
			if (null != newValue) {
				this.getSystemParams().put(paramName, newValue);
			} else {
				if (!keepOldParam) {
					this.getSystemParams().put(paramName, "false");
				}
			}
		}
	}
	
}
