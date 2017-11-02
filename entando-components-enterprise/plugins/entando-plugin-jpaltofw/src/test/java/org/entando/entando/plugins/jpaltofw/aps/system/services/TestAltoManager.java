package org.entando.entando.plugins.jpaltofw.aps.system.services;

import org.entando.entando.plugins.jpaltofw.aps.services.AltoConfig;

public class TestAltoManager extends AbstractAltoConfigTestCase {

    public void testGetConfig() throws Exception {
        try {
            AltoConfig config = this.altoManager.getAltoConfig();
            assertNotNull(config);
            assertEquals("alto1", config.getId());
            assertEquals("http://localhost:8081/alto/", config.getBaseUrl());
            assertEquals("alto", config.getPid());
            assertEquals("adminadmin", config.getProjectPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    public void testUpdateConfig() throws Throwable {
        AltoConfig originaryConfig = this.altoManager.getAltoConfig();
        assertNotNull(originaryConfig);
        try {
            AltoConfig config = this.createAltoConfig();
            assertNotNull(config);
            this.altoManager.updateAltoConfig(config);
            AltoConfig updatedConfig = this.altoManager.getAltoConfig();
            assertNotNull(updatedConfig);
            assertEquals("id-test", updatedConfig.getId());
            assertEquals("localhost:9000", updatedConfig.getBaseUrl());
            assertEquals("pid-test", updatedConfig.getPid());
            assertEquals("password-test", updatedConfig.getProjectPassword());

        } catch (Throwable t) {
            throw t;
        } finally {
            this.altoManager.updateAltoConfig(originaryConfig);
            originaryConfig = this.altoManager.getAltoConfig();
            assertNotNull(originaryConfig);
            assertEquals("alto1", originaryConfig.getId());
            assertEquals("http://localhost:8081/alto/", originaryConfig.getBaseUrl());
            assertEquals("alto", originaryConfig.getPid());
            assertEquals("adminadmin", originaryConfig.getProjectPassword());
        }
    }


}
