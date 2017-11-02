package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service;

import org.apache.http.HttpStatus;
import org.entando.entando.plugins.jprestapi.aps.core.Endpoint;
import org.entando.entando.plugins.jprestapi.aps.core.IEndpoint;


/**
 *
 * @author entando
 * 
 */
public class PentahoEndpointsDictionary {
  
   public static Endpoint endpoints[] = {
	new Endpoint(IEndpoint.httpVerb.GET, "/api/repo/files/", HttpStatus.SC_OK), 						// 0
	new Endpoint(IEndpoint.httpVerb.GET, "/Login", HttpStatus.SC_OK) 									// 1
    
   };
  
  // mnemonics
      
  //FILES
  public static final int API_GET_FILES = 0;  
  public static final int API_GET_AUTH_TICKET = 1;  
   
  
  }

