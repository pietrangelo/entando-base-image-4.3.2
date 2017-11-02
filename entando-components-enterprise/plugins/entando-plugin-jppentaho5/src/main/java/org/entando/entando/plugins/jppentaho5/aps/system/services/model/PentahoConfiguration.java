package org.entando.entando.plugins.jppentaho5.aps.system.services.model;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.json.XML;

import com.agiletec.aps.system.exception.ApsSystemException;

public class PentahoConfiguration {
    
    public PentahoConfiguration() { }
    
    public PentahoConfiguration(String xml) throws ApsSystemException{
        try {
            if (StringUtils.isNotBlank(xml)) {
                JSONObject json = XML.toJSONObject(xml);
                JSONObject config = json.getJSONObject(PENTAHO_CONFIGURATION);
                
                _username = config.getString(USERNAME);
                _password = config.getString(PASSWORD);
                _instance = config.getString(INSTANCE);
                _casAuthentication = config.getBoolean(CAS);
                if (json.has(TAP_PLUGIN))
                {
                    _tapPlugin = config.getBoolean(TAP_PLUGIN);
                }
                else
                {
                    _tapPlugin = false;
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("error while parsing the configuration", t);
        }
    }
    
    public String toXml() throws ApsSystemException {
        JSONObject json = new JSONObject();
        
        try {
            String username = _username;
            if (StringUtils.isNotBlank(username)) {
                json.put(USERNAME, username);
            } else {
                json.put(USERNAME, JSONObject.NULL);
            }
            
            String password = _password;
            if (StringUtils.isNotBlank(password)) {
                json.put(PASSWORD, password);
            } else {
                json.put(PASSWORD, JSONObject.NULL);
            }
            
            json.put(INSTANCE, _instance);
            json.put(CAS, _casAuthentication);
            json.put(TAP_PLUGIN, _tapPlugin);
            
        } catch (Throwable t) {
            throw new ApsSystemException("Error getting the XML representation of the configuration", t);
        }
        return XML.toString(json, PENTAHO_CONFIGURATION);
    }
    
    @Override
    public PentahoConfiguration clone() throws RuntimeException {
        PentahoConfiguration config = null;
        try {
            config = new PentahoConfiguration(this.toXml());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return config;
    }
    
    public String getInstance() {
        return _instance;
    }
    
    public void setInstance(String instance) {
        this._instance = instance;
    }
    
    public String getUsername() {
        return _username;
    }
    
    public void setUsername(String username) {
        this._username = username;
    }
    
    public String getPassword() {
        return _password;
    }
    
    public void setPassword(String password) {
        this._password = password;
    }
    
    public boolean isCasAuthentication() {
        return _casAuthentication;
    }
    
    public void setCasAuthentication(boolean casAuthentication) {
        this._casAuthentication = casAuthentication;
    }
    
    public boolean isTapPlugin() {
        return _tapPlugin;
    }
    
    public void setTapPlugin(boolean _tapPlugin) {
        this._tapPlugin = _tapPlugin;
    }
    
    private String _instance;
    private String _username;
    private String _password;
    private boolean _casAuthentication;
    private boolean _tapPlugin;
    
    
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String INSTANCE = "pentahoInstance";
    private static final String CAS = "casAuthentication";
    private static final String TAP_PLUGIN = "tapPlugin";
    
    private static final String PENTAHO_CONFIGURATION = "pentaho_config";
    
}
