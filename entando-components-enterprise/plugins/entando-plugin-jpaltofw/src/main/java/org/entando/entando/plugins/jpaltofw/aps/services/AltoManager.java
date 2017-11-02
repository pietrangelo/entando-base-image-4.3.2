package org.entando.entando.plugins.jpaltofw.aps.services;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import org.entando.entando.plugins.jpaltofw.aps.services.parse.AltoConfigDOM;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AltoManager extends AbstractService implements IAltoManager {

    private static Logger _logger = LoggerFactory.getLogger(AltoManager.class);
    private ConfigInterface configManager;
    private AltoConfig config;

    @Override
    public void init() throws Exception {
        try {
            this.loadConfigs();

        } catch (Throwable t) {
            _logger.error("{} Manager: Error on initialization", this.getClass().getName(), t);
        }
    }

    private void loadConfigs() throws ApsSystemException {
        try {
            ConfigInterface configManager = this.getConfigManager();
            String xml = configManager.getConfigItem(AltoSystemConstants.CONFIG_ITEM_CODE);
            if (xml == null) {
                throw new ApsSystemException("Configuration item not present: " + AltoSystemConstants.CONFIG_ITEM_CODE);
            }
            AltoConfigDOM configDOM = new AltoConfigDOM();
            this.setConfig(configDOM.extractConfig(xml));
        } catch (Throwable t) {
            _logger.error("Error in loadConfigs", t);
            throw new ApsSystemException("Error in loadConfigs", t);
        }
    }

    @Override
    public AltoConfig getAltoConfig() throws ApsSystemException {
        try {
            if (config != null) {
                return this.config.clone();
            }
        } catch (Throwable t) {
            _logger.error("Error loading mail service configuration", t);
            throw new ApsSystemException("Error loading mail service configuration", t);
        }
        throw new ApsSystemException("config is null");
    }

    @Override
    public void updateAltoConfig(final AltoConfig config) throws ApsSystemException {
        try {
            String xml = new AltoConfigDOM().createConfigXml(config);
            this.getConfigManager().updateConfigItem(AltoSystemConstants.CONFIG_ITEM_CODE, xml);
            this.setConfig(config);
        } catch (Throwable t) {
            _logger.error("Error updating configs", t);
            throw new ApsSystemException("Error updating configs", t);
        }
    }

    public AltoConfig getConfig() {
        return config;
    }

    public void setConfig(AltoConfig config) {
        this.config = config;
    }

    public ConfigInterface getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this.configManager = configManager;
    }
}
