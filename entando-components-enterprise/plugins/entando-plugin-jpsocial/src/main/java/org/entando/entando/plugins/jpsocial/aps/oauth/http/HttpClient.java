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
package org.entando.entando.plugins.jpsocial.aps.oauth.http;

import java.io.IOException;
import java.util.Map;
import net.oauth.OAuthMessage;

/**
 * An HTTP client, which can send an HTTP request and receive the response.
 * 
 * @author jkristian
 */
public interface HttpClient {

    /**
     * Send an HTTP request and return the response.
     * 
     * @param httpParameters
     *            HTTP client parameters, as a map from parameter name to value.
     *            Parameter names are defined as constants below.
     */
    HttpResponseMessage execute(HttpMessage request, Map<String, Object> httpParameters) throws IOException;

    /**
     * The name of the parameter that is the maximum time to wait to connect to
     * the server. (Integer msec)
     */
    static final String CONNECT_TIMEOUT = "connectTimeout";

    /**
     * The name of the parameter that is the maximum time to wait for response
     * data. (Integer msec)
     */
    static final String READ_TIMEOUT = "readTimeout";

    /** The name of the parameter to automatically follow redirects. (Boolean) */
    static final String FOLLOW_REDIRECTS = "followRedirects";

    static final String GET = OAuthMessage.GET;
    static final String POST = OAuthMessage.POST;
    static final String PUT = OAuthMessage.PUT;
    static final String DELETE = OAuthMessage.DELETE;

}
