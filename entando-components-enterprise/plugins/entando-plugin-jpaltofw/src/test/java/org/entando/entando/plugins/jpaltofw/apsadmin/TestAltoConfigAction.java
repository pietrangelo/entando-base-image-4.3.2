package org.entando.entando.plugins.jpaltofw.apsadmin;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.opensymphony.xwork2.Action;
import org.entando.entando.plugins.jpaltofw.aps.services.AltoConfig;
import org.entando.entando.plugins.jpaltofw.aps.services.IAltoManager;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;
import org.entando.entando.plugins.jpaltofw.aps.system.services.util.JpaltoTestHelper;

public class TestAltoConfigAction extends ApsAdminPluginBaseTestCase {

    private JpaltoTestHelper helper;
    private IAltoManager altoManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    private void init() throws Exception {
        try {
            ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
            this.altoManager = (IAltoManager) this.getService(AltoSystemConstants.BEAN_ITEM_CODE);
            this.helper = new JpaltoTestHelper(configManager, this.altoManager);
        } catch (Exception e) {
            throw e;
        }
    }


/*    public void testEditAction() throws Throwable {
        try {
            this.setUserOnSession("admin");
            this.initAction("/do/jpalto/AltoConfig", "edit");
            String result = this.executeAction();
            assertEquals(Action.SUCCESS, result);
            AltoConfigAction action = (AltoConfigAction) this.getAction();
            AltoConfig altoConfig = action.getAltoConfig();
            assertNotNull(altoConfig);
            assertEquals("alto1", altoConfig.getId());
            assertEquals("http://localhost:8081/alto/", altoConfig.getBaseUrl());
            assertEquals("alto", altoConfig.getPid());
            assertEquals("adminadmin", altoConfig.getProjectPassword());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }*/
}
