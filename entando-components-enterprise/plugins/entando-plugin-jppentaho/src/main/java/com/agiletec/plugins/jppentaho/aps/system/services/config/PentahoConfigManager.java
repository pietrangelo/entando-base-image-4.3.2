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
package com.agiletec.plugins.jppentaho.aps.system.services.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jppentaho.aps.system.JpPentahoSystemConstants;
import com.agiletec.plugins.jppentaho.aps.system.services.config.event.PentahoConfigChangedEvent;

/**
 * @author E.Santoboni
 */
public class PentahoConfigManager extends AbstractService implements IPentahoConfigManager {

	private static final Logger _logger =  LoggerFactory.getLogger(PentahoConfigManager.class);

	@Override
    public void init() throws Exception {
        this.loadConfigs();
        _logger.debug("{} ready", this.getClass().getName());
    }
    
    /**
     * Load the XML configuration containing smtp configuration and the sender addresses.
     * @throws ApsSystemException
     */
    private void loadConfigs() throws ApsSystemException {
        try {
            ConfigInterface configManager = this.getConfigManager();
            String xml = configManager.getConfigItem(JpPentahoSystemConstants.PENTAHO_CONFIG);
            if (xml == null) {
                throw new ApsSystemException("Configuration item not present: " + JpPentahoSystemConstants.PENTAHO_CONFIG);
            }
            PentahoConfigDOM configDOM = new PentahoConfigDOM();
            if (xml.trim().length() == 0) {
                this.setConfig(new PentahoConfig());
                return;
            }
            this.setConfig(configDOM.extractConfig(xml));
        } catch (Throwable t) {
        	_logger.error("error loading Config", t);
            throw new ApsSystemException("error loading Config", t);
        }
    }

	@Override
    public PentahoConfig getConfig() throws ApsSystemException {
        try {
            return (PentahoConfig) this._config.clone();
        } catch (Throwable t) {
        	_logger.error("Error loading configuration", t);
            throw new ApsSystemException("Error loading configuration", t);
        }
    }

	@Override
    public void updateConfig(PentahoConfig config) throws ApsSystemException {
        try {
            String xml = new PentahoConfigDOM().createConfigXml(config);
            this.getConfigManager().updateConfigItem(JpPentahoSystemConstants.PENTAHO_CONFIG, xml);
            this.setConfig(config);
        } catch (Throwable t) {
        	_logger.error("error updating config", t);
            throw new ApsSystemException("error updating config", t);
        } finally {
            PentahoConfigChangedEvent event = new PentahoConfigChangedEvent();
            this.notifyEvent(event);
        }
    }

    protected void setConfig(PentahoConfig config) {
        this._config = config;
    }

    /**
     * Returns the configuration manager.
     * @return The Configuration manager.
     */
    protected ConfigInterface getConfigManager() {
        return _configManager;
    }

    /**
     * Set method for Spring bean injection.<br /> Set the Configuration manager.
     * @param configManager The Configuration manager.
     */
    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
    private PentahoConfig _config;
    private ConfigInterface _configManager;
}