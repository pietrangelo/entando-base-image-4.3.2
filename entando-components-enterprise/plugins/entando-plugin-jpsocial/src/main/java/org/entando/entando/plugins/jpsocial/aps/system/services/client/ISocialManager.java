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
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER;

/**
 *
 * @author S.Loru
 */
public interface ISocialManager {
	
	public String getAuthenticationUrl(PROVIDER provider, String callbackURL) throws ApsSystemException;
	
	public String shareText(PROVIDER provider, String text);
	
	
}
