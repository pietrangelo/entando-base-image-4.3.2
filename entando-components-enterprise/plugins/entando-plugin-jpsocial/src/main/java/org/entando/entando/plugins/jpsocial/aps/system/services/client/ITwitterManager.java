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
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.ISocialUser;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public interface ITwitterManager extends ISocialBaseManager{
	
	/**
	 * Tweet!!!
	 * @param status
	 * @param accessToken
	 * @param accessTokenSecret
	 * @return
	 * @throws Exception
	 */
	public String tweet(String status, String accessToken,
			String accessTokenSecret) throws ApsSystemException;
	
	
	public String tweet(Twitter client, String message) throws ApsSystemException;
	
	public ISocialUser createSocialCredentials(String accessToken, String secretToken);
	
	public Twitter createClient();
	
	public Twitter createClient(String accessToken, String tokenSecret);

}
