package org.entando.entando.plugins.jpaltofw.aps.services;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "instance")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"pid", "baseUrl", "id", "projectPassword"})
public class AltoConfig {

    @XmlElement(name = "pid")
    private String pid;
    private String baseUrl;
    private String id;
    private String projectPassword;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public AltoConfig() {
        //
    }

    @Override
    public AltoConfig clone() {
        AltoConfig config = new AltoConfig();
        config.setId(this.getId());
        config.setBaseUrl(this.getBaseUrl());
        config.setPid(this.getPid());
        config.setProjectPassword(this.getProjectPassword());
        return config;


    }
}
