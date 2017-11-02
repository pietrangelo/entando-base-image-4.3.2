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
package org.entando.entando.plugins.jpsocial.aps.system.services.client;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.restfb.FacebookClient;

/**
 *
 * @author entando
 */
public interface IFacebookManager extends ISocialBaseManager{
  
  /**
   * Post a message
   * @param client
   * @param message
   * @return 
   */
  public String publishMessage(FacebookClient client, String message) throws ApsSystemException; 
  
  /**
   * Publish a photo with the related description
   * @param client
   * @param imgPath
   * @param imgDescr
   * @return 
   */
  public String publishImage(FacebookClient client, String imagePath, String imageDescription) throws ApsSystemException;
  
  /**
   * Publish a video with the related description
   * @param client
   * @param imgPath
   * @param imgDescr
   * @return
   * @throws ApsSystemException 
   */
  public String publishVideo(FacebookClient client, String videoPath, String imageDescription) throws ApsSystemException;
  
  
  
}
