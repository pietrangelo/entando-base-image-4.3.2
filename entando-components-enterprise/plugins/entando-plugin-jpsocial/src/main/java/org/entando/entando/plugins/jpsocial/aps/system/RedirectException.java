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
package org.entando.entando.plugins.jpsocial.aps.system;

import net.oauth.OAuthException;

/**
 * Indicates that the server should redirect the HTTP client.
 * 
 * @author John Kristian
 */
public class RedirectException extends OAuthException {

    public RedirectException(String targetURL) {
        super(targetURL);
    }

    public String getTargetURL() {
        return getMessage();
    }

    private static final long serialVersionUID = 1L;

}
