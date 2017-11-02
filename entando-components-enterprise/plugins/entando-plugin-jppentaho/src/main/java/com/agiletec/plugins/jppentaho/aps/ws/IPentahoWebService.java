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
package com.agiletec.plugins.jppentaho.aps.ws;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public interface IPentahoWebService {
	
	public String getList(String solution, String path, String server, String contextPath, 
			String username, String password, String target) throws ServiceException, MalformedURLException, RemoteException;
	
	public String getSolutionDirectoryList(String solution, String path, String server, String contextPath, 
			String username, String password, String target) throws Throwable;
    
}