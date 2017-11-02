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
package com.agiletec.plugins.jpforum.aps.system.services.config;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interface for the forum config service.
 * This service manages the forum settings.
 */
public interface IConfigManager {

	/**
	 * Update the configuration
	 * @param config The holder of the configuration settings
	 * @throws ApsSystemException
	 */
	public void updateConfig(ForumConfig config) throws ApsSystemException;
	
	/**
	 * Return the number of posts per page to be displayed .
	 * @return 
	 */
	public int getPostsPerPage();

	/**
	 * Return the allowed number of attachs per post.
	 * @return 
	 */
	public int getAttachsPerPost();
	
	/**
	 * Returns the name of the attribute of the {@link UserProfile} object that is used to display nickname
	 * @return
	 */
	public String getProfileNickAttributeName();
	
	/**
	 * returns the typecode of the {@link UserProfile} in use
	 * @return
	 */
	public String getProfileTypecode();

	/**
	 * Returns the current configuration
	 * @return
	 */
	public ForumConfig getConfig();
	
}
