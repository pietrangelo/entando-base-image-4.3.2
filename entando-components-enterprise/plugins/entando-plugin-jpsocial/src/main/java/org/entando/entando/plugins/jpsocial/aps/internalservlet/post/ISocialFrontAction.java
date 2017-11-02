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
package org.entando.entando.plugins.jpsocial.aps.internalservlet.post;

/**
 * @author entando
 */
interface ISocialFrontAction {

  /**
   * Tweet intro
   * @return 
   */
  public String tweetStart();
  
  /**
   * Facebook intro
   * @return 
   */
  public String facebookStart();
  
  /**
   * Tweet the free text provided
   * @return 
   */
  public String tweet();
  
  /**
   * Post the free text with an optianl image on facebook
   * @return 
   */
  public String postFacebook();
  
  /**
   * Redirect to system logout after destroying ALL the social credentials
   * @return 
   */
  public String socialLogout();
}
