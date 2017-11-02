package org.entando.entando.plugins.jpaltofw.aps.system.services.parse;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import org.entando.entando.plugins.jpaltofw.aps.services.AltoConfig;
import org.entando.entando.plugins.jpaltofw.aps.services.parse.AltoConfigDOM;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;
import org.entando.entando.plugins.jpaltofw.aps.system.services.AbstractAltoConfigTestCase;

public class TestAltoConfigDOM extends AbstractAltoConfigTestCase {


    private ConfigInterface configManager;

    private String xml ="<instances> " +
            "<instance> " +
            "<id>alto1</id> " +
            "<baseUrl>http://localhost:8081/alto/</baseUrl> " +
            "<pid>alto</pid> " +
            "</instance> " +
            "</instances>";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    /**
     * Tests the extraction of the configuration from the XML.
     * @throws Throwable
     */
    public void testExtractConfig() throws Throwable {
        System.out.println("Config Manager : "+configManager);
        String xml = this.configManager.getConfigItem(AltoSystemConstants.CONFIG_ITEM_CODE);
        assertNotNull(xml);
        AltoConfig config = new AltoConfigDOM().extractConfig(xml);
        assertNotNull(config);
        assertEquals(AltoConfig.class, config.getClass());
        assertEquals(config.getId(), "alto1");
        assertEquals(config.getBaseUrl(), "http://localhost:8081/alto/");
        assertEquals(config.getPid(), "alto");
    }

    protected void init() throws Exception {
        try {
            this.configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    protected void tearDown() throws Exception {
//        try{
//            super.tearDown();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
