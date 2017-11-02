package org.entando.entando.plugins.jpaltofw.aps.system.services.util;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import org.entando.entando.plugins.jpaltofw.aps.services.IAltoManager;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;


/**
 * Helper for the jpalto tests. Use its functionalities to arrange the system for jpalto tests.
 * @author F.Locci
 */
public class JpaltoTestHelper {

    private String config;

    private ConfigInterface configManager;
    private IAltoManager altoManager;

    public JpaltoTestHelper(ConfigInterface configManager, IAltoManager altoManager) {
        this.configManager = configManager;
        this.altoManager = altoManager;
        this.config = this.configManager.getConfigItem(AltoSystemConstants.CONFIG_ITEM_CODE);
    }

    public void resetConfig() throws Exception {
        this.configManager.updateConfigItem(AltoSystemConstants.CONFIG_ITEM_CODE, this.config);
        try {
            ((AbstractService) this.altoManager).refresh();
        } catch (Throwable t) {
            throw new ApsSystemException("Error in resetConfig", t);
        }
    }
}
