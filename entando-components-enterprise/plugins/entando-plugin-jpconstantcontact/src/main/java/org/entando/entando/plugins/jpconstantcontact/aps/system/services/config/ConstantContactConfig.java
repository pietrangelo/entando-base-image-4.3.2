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
package org.entando.entando.plugins.jpconstantcontact.aps.system.services.config;


/**
 *<constantContact>
 *	<apikey></apikey>
 *<consumerSecret></consumerSecret>
 * <token></token>
 * <contactPage></contactPage>
 * <contactFrame></contactFrame>
 * <campaignPage></campaignPage>
 * <campaignFrame></campaignFrame>
 * <eventPage></eventPage>
 * <eventFrame></eventFrame>
 *</constantContact>
 *
 */

/**
 * @author Alberto Piras
 */
public class ConstantContactConfig {
    
    /**
     * Get the API_KEY
     * @return API_KEY
     */
    public String getApiKey() {
        return _apiKey;
    }
    
    /**
     * Set the API_KEY
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        this._apiKey = apiKey;
    }
    
     /**
     * Get the Client Secret code
     * @return _secret
     */
    public String getSecret() {
        return _secret;
    }
    
      /**
     * Set the Client Secret code
     * @param secret
     */
    public void setSecret(String _secret) {
        this._secret = _secret;
    }
    
    
     public String getToken() {
        return _token;
    }

    public void setToken(String _token) {
        this._token = _token;
    }
    
    public String getContactPage() {
        return _contactPage;
    }

    public void setContactPage(String _contactPage) {
        this._contactPage = _contactPage;
    }

    public String getContactFrame() {
        return _contactFrame;
    }

    public void setContactFrame(String _contactFrame) {
        this._contactFrame = _contactFrame;
    }

    public String getCampaignPage() {
        return _campaignPage;
    }

    public void setCampaignPage(String _campaignPage) {
        this._campaignPage = _campaignPage;
    }

    public String getCampaignFrame() {
        return _campaignFrame;
    }

    public void setCampaignFrame(String _campaignFrame) {
        this._campaignFrame = _campaignFrame;
    }

    public String getEventPage() {
        return _eventPage;
    }

    public void setEventPage(String _eventPage) {
        this._eventPage = _eventPage;
    }

    public String getEventFrame() {
        return _eventFrame;
    }

    public void setEventFrame(String _eventFrame) {
        this._eventFrame = _eventFrame;
    }
    
    private String _apiKey;
    private String _secret;
    private String _token;
    private String _contactPage;
    private String _contactFrame;
    private String _campaignPage;
    private String _campaignFrame;
    private String _eventPage;
    private String _eventFrame;
     
}
