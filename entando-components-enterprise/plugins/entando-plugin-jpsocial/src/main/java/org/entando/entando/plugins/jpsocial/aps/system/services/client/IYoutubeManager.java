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
import java.io.InputStream;
import java.util.List;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.model.YoutubeCategory;

/**
 * @author entando
 */
public interface IYoutubeManager {
  
  
  /**
   * Upload a video with the given tags
   * @param path
   * @param title
   * @param description
   * @param category
   * @param keywords
   * @param developerTag
   * @param isPrivate
   * @throws ApsSystemException 
   */
  public void uploadVideo(String accessToken, 
          InputStream stream, 
          String title, 
          String description, 
          String ytCategory, 
          List<String> keywords, 
          List<String> developerTag) throws ApsSystemException;
  
  /**
   * Verify the uploadVideo status
   * @return
   * @throws ApsSystemException 
   */
  public boolean verifyUpload() throws ApsSystemException;
  
  /**
   * Get the URL to start the authentication process. If no callback URL is provided
   * the address the edit queue is generated
   * @param url
   * @return 
   */
  public String getAuthenticationURL(String url);
  
  /**
   * Return the list of the youtube categories
   * @return 
   */
  public List<YoutubeCategory> getYoutubeGategories();
  
  
  /**
   * Test a file against Youtube supported formats
   * @param pathFile
   * @return 
   */
  public boolean isYoutubeVideo(String filename);
}
