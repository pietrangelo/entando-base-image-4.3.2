package org.entando.entando.plugins.jpaltofw.apsadmin;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.plugins.jpaltofw.aps.services.AltoConfig;
import org.entando.entando.plugins.jpaltofw.aps.services.IAltoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AltoConfigAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(AltoConfigAction.class);

    private IAltoManager altoManager;
    private int strutsAction;
    private String id;
    private String baseUrl;
    private String pid;
    private String projectPassword;


    public String save() {
        try {
            AltoConfig config = this.prepareConfig();
            this.getAltoManager().updateAltoConfig(config);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "save");
            return FAILURE;
        }
        return SUCCESS;
    }

    private AltoConfig prepareConfig() {
        AltoConfig config = new AltoConfig();
        config.setId(this.getId());
        config.setPid(this.getPid());
        config.setBaseUrl(this.getBaseUrl());
        config.setProjectPassword(this.getProjectPassword());
        return config;
    }

    public String edit() {
        try {
            AltoConfig config = this.getAltoManager().getAltoConfig();
            if (config != null) {
                this.configToModel(config);
            }

        } catch (Throwable t) {
            logger.error(t.getMessage());
            ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        logger.info("return SUCCESS");
        return SUCCESS;
    }

    public int getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    private void configToModel(AltoConfig config) {
        this.setId(config.getId());
        this.setBaseUrl(config.getBaseUrl());
        this.setPid(config.getPid());
        this.setProjectPassword(config.getProjectPassword());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProjectPassword() {
        return projectPassword;
    }

    public void setProjectPassword(String projectPassword) {
        this.projectPassword = projectPassword;
    }

    public IAltoManager getAltoManager() {
        return altoManager;
    }

    public void setAltoManager(IAltoManager altoManager) {
        this.altoManager = altoManager;
    }
}
