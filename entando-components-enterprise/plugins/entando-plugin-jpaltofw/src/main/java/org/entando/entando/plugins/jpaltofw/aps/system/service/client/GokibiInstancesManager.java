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
package org.entando.entando.plugins.jpaltofw.aps.system.service.client;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.ApsProperties;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
@Aspect
public class GokibiInstancesManager extends AbstractService implements IGokibiInstancesManager {
	
	private static final Logger logger =  LoggerFactory.getLogger(GokibiInstancesManager.class);
	
    @Override
    public void init() throws Exception {
		this.loadConfigs();
        logger.info(this.getClass().getName() + ": initialized ");
    }
	
	protected void loadConfigs() throws ApsSystemException {
		try {
			String itemName = AltoSystemConstants.CONFIG_ITEM_CODE;
			ConfigInterface configManager = (ConfigInterface) super.getBeanFactory().getBean(SystemConstants.BASE_CONFIG_MANAGER);
			String xml = configManager.getConfigItem(itemName);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + itemName);
			}
			GokibiInstancesDOM configDOM = new GokibiInstancesDOM();
			this.setInstances(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			logger.error("loadConfigs", t);
			throw new ApsSystemException("Errore in fase di inizializzazione", t);
		}
	}
	
	public String refreshCatalogue(Properties properties) throws Throwable {
		try {
			this.checkViewWidgets();
		} catch (Throwable t) {
			logger.error("refreshCatalogue", t);
		}
		return "success";
	}
	
	@Before("execution(* com.agiletec.apsadmin.portal.WidgetsViewerAction.viewWidgets(..))")
    public void checkViewWidgets() {
        try {
			List<GokibiInstance> instances = this.getInstances();
			if (null == instances) {
				return;
			}
			WidgetType parentType = this.getWidgetTypeManager().getWidgetType(AltoSystemConstants.WIDGET_CODE);
			if (null == parentType) {
				return;
			}
			GokibiClient client = new GokibiClient();
			for (int i = 0; i < instances.size(); i++) {
				GokibiInstance instance = instances.get(i);
				Map<String, GokibiWidget> gokibiWidgets = client.getWidgets(instance.getBaseUrl(), instance.getPid());
				if (null == gokibiWidgets || gokibiWidgets.isEmpty()) {
					continue;
				}
				Iterator<String> keyIter = gokibiWidgets.keySet().iterator();
				while (keyIter.hasNext()) {
					String key = keyIter.next();
					GokibiWidget gokibiWidget = gokibiWidgets.get(key);
					String widgetLongKey = "gkb_" + instance.getId() + "_" + gokibiWidget.getName() + "_" + gokibiWidget.getType();
					String widgetKeyCode = DigestUtils.md5Hex(widgetLongKey);
					if (null != this.getWidgetTypeManager().getWidgetType(widgetKeyCode)) {
						continue;
					}
					WidgetType widgetType = new WidgetType();
					widgetType.setCode(widgetKeyCode);
					ApsProperties titles = new ApsProperties();
					String title = "Alto FW Instance '" + instance.getId() + "' - Widget '" + gokibiWidget.getName() +"' - " + gokibiWidget.getType();
					titles.put("it", title);
					titles.put("en", title);
					widgetType.setTitles(titles);
					widgetType.setParentType(parentType);
					widgetType.setMainGroup(Group.FREE_GROUP_NAME);
					ApsProperties config = new ApsProperties();
					config.put(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL, instance.getBaseUrl());
					config.put(AltoSystemConstants.WIDGET_PARAM_SERVER_PID, instance.getPid());
					config.put(AltoSystemConstants.WIDGET_PARAM_WIDGET_CODE, key);
					widgetType.setConfig(config);
					this.getWidgetTypeManager().addWidgetType(widgetType);
				}
			}
		} catch (Throwable t) {
			logger.error("error refreshing widget catalogue", t);
		}
    }
	
	@Override
	public List<GokibiInstance> getInstances() {
		if (null != this._instances) {
			
		}
		return _instances;
	}
	protected void setInstances(List<GokibiInstance> instances) {
		this._instances = instances;
	}
	
	protected IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}
	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}
    
	private List<GokibiInstance> _instances;
	private IWidgetTypeManager _widgetTypeManager;
	
}
