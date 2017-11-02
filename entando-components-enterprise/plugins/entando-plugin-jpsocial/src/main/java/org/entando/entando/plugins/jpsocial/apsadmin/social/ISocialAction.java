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
package org.entando.entando.plugins.jpsocial.apsadmin.social;

/**
 * @author M.Minnai
 */
public interface ISocialAction {
    
    /**
     * Post an existing content from the content list
     * @return 
     */
    public String postContentFromList();
    
    /**
     * Post en entire content on facebook
     * @return 
     */
    public String addAttribute();
    
    /**
     * Post the entire attribute queue in the current social networks
     * @return 
     */
    public String postQueue();
    
    /**
     * Edit the attribute delivery queue
     * @return 
     */
    public String tweetContent();
    
    /**
     * Save and publish the current content and post it with the given provider
     * @return 
     */
    public String postContent();
    
    /**
     * Trampoline for the edit attribute page
     * @return 
     */
    public String editQueue();
    
    /**
     * Exit the list of the delivery queue and return to content edit
     * @return 
     */
    public String cancelPost();
    
    /**
     * Revoke credentials
     * @return 
     */
    public String revokeCredentials();
    
    
    /**
     * If the user is logged to the social network redirect the addDefaultAttributeAction
     * otherwise redirect the user to the login page of the social network
     * @return 
     */
    public String redirectUrlFromList();
    
    
    public String redirectUrlToEdit();
    
    
    public String signedIn();
}
