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
package org.entando.entando.plugins.jpsocial.aps.system.httpclient;

import java.net.URL;
import org.apache.commons.httpclient.HttpClient;

/**
 * A source of Jakarta Commons HttpClient objects.
 * 
 * @author John Kristian
 */
public interface HttpClientPool {

    /** Get the appropriate HttpClient for sending a request to the given URL. */
    public HttpClient getHttpClient(URL server);

}
