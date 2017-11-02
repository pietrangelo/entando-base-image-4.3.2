package org.entando.entando.plugins.jppentaho5.aps.system.services;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.PentahoAuthenticationCredentials;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.PentahoClient;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.PentahoFileResourceService;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model.PentahoRepositoryFileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;

public class PentahoFilesystemManager extends AbstractService implements IPentahoFilesystemManager {

	private static Logger _logger = LoggerFactory.getLogger(PentahoFilesystemManager.class);

	@Override
	public void init() throws Exception {
		_logger.info(this.getClass().getCanonicalName() + " ready");

		System.out.println("--------------------------------------");
		System.out.println("-  PENTAHO FILESYSTEM MANAGER START  -");
		System.out.println("--------------------------------------");

	    PentahoAuthenticationCredentials pentahoCredentials = new PentahoAuthenticationCredentials();
	    PentahoClient pentahoClient = new PentahoClient(pentahoCredentials);
	    _pentahoFileResourceService = new PentahoFileResourceService(pentahoClient);
	    _pentahoFileResourceService.setDebug(true);
	    
	    String username =_pentahoManager.getConfiguration().getUsername();
	    String password =_pentahoManager.getConfiguration().getPassword();
	    String hostname =_pentahoManager.getConfiguration().getInstance();
	    
		URL url = new URL(hostname);
		   
		_logger.debug("url prot. : "+url.getProtocol());		
		_logger.debug("url host  : "+url.getHost());
		_logger.debug("url port  : "+url.getPort());
		_logger.debug("url path  : "+url.getPath());
		
	    pentahoCredentials.setUsername(username);
	    pentahoCredentials.setPassword(password);
	    
	    pentahoClient.setHostname(url.getHost());
	    
	    pentahoClient.setPort(url.getPort());
	    pentahoClient.setSchema(url.getProtocol());	 	    
		pentahoClient.setBasePath(url.getPath());
			
	}

	@Override
	public List<String> getReportsPath(String filter) throws ApsSystemException {

		return 	listDirectoryFiles("", filter);
	}

	protected List<String> listDirectoryFiles(String directoryName, String filterExtension) {
		// get all the files from a directory

		//System.out.println(" listDirectoryFiles: "+directoryName);

		List<String> resultList = new ArrayList<String>();	  

		List<PentahoRepositoryFileDto> filesApi = null;

		try {
			filesApi = _pentahoFileResourceService.getFilesChildren(directoryName.replaceAll(" ", "%20"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (filesApi!=null) {
			for (PentahoRepositoryFileDto file : filesApi) {

				if (file.isFolder()) {
					//System.out.println("FOLDER -> " + file.getPath());
					resultList.addAll(listDirectoryFiles(file.getPath(), filterExtension));	        	

				} else if (!file.isFolder()) {
					if (filterExtension.isEmpty()) 
					{
						//System.out.println("FILE -> " +file.getPath());	        			        	
						resultList.add(file.getPath());
					}
					else if (!filterExtension.isEmpty() && file.getName().endsWith(filterExtension))
					{
						//System.out.println("FILE -> " +file.getPath());	        			        	
						resultList.add(file.getPath());
					}
				}

			}
		}

		return resultList;	   
	}
	
	public String getAuthTicket() throws ApsSystemException {
	   String ticket="";
	   try {		  
		   ticket=_pentahoFileResourceService.getAuthTicket();			
	   } catch (Throwable e) {
			_logger.error("Error on getAuthCookie: ", e);			
	   }
	   return ticket;
	}

	public IPentahoManager getPentahoManager() {
		return _pentahoManager;
	}

	public void setPentahoManager(IPentahoManager pentahoManager) {
		this._pentahoManager = pentahoManager;
	}


	private PentahoFileResourceService _pentahoFileResourceService;
	private IPentahoManager _pentahoManager;
        
	public final static String MANAGER_ID = "jppentaho5FileSystemManager";
}
