package org.entando.entando.plugins.jpaltofw.aps.system.services;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import org.entando.entando.plugins.jpaltofw.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpaltofw.aps.services.AltoConfig;
import org.entando.entando.plugins.jpaltofw.aps.services.IAltoManager;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;
import org.entando.entando.plugins.jpaltofw.aps.system.services.util.JpaltoTestHelper;

public class AbstractAltoConfigTestCase extends ApsPluginBaseTestCase {

    protected JpaltoTestHelper helper;
    protected IAltoManager altoManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    @Override
    protected void tearDown() throws Exception {
        this.helper.resetConfig();
        super.tearDown();
    }


    protected void init() throws Exception {
        try {
            ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
            this.altoManager = (IAltoManager) this.getService(AltoSystemConstants.BEAN_ITEM_CODE);
            this.helper = new JpaltoTestHelper(configManager, this.altoManager);
            assertNotNull(altoManager);
            assertNotNull(helper);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    protected AltoConfig createAltoConfig() {
        final AltoConfig config = new AltoConfig();
        config.setId("id-test");
        config.setBaseUrl("localhost:9000");
        config.setPid("pid-test");
        config.setProjectPassword("password-test");
        return config;
    }
}
