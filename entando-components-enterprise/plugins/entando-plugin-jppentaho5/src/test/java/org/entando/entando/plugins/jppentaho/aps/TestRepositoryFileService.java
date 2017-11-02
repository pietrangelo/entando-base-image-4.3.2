package org.entando.entando.plugins.jppentaho.aps;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.PentahoAuthenticationCredentials;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.PentahoClient;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.PentahoFileResourceService;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model.PentahoRepositoryFileDto;
import org.junit.Assert;
import org.junit.Test;

public class TestRepositoryFileService extends ApsPluginBaseTestCase implements PentahoTestParameters  {

	
  public TestRepositoryFileService() {
  }

 
  public static void setUpClass() {
  }

 
  public static void tearDownClass() {
  }

  @Override
  public void setUp() {
    PentahoAuthenticationCredentials pentahoCredentials = new PentahoAuthenticationCredentials();
    PentahoClient pentahoClient = new PentahoClient(pentahoCredentials);
    _pentahoFileResourceService = new PentahoFileResourceService(pentahoClient);
    _pentahoFileResourceService.setDebug(false);
    pentahoCredentials.setUsername(USERNAME);
    pentahoCredentials.setPassword(PASSWORD);
    pentahoClient.setHostname(HOSTNAME);
    pentahoClient.setPort(PORT);
    pentahoClient.setSchema(SCHEMA);
  }

  @Override
  public void tearDown() {
    // no op
  }

  /**
   * 
   * Test of getFilesChildren method, of class PentahoFileResourceService.
   */
  @Test
  public void testGetRepositoryFiles() throws Throwable {
    String id = null;
    String name = null;
    
    try {
      
    	List<PentahoRepositoryFileDto> rootDirectory = _pentahoFileResourceService.getFilesChildren("");
         
    	List<PentahoRepositoryFileDto> files = _pentahoFileResourceService.getFilesChildren("public/Steel%20Wheels");
      
      Assert.assertNotNull(files);
      Assert.assertFalse(files.isEmpty());
      
      for (PentahoRepositoryFileDto directory : rootDirectory) {
    	  name = directory.getName();
    	  for (PentahoRepositoryFileDto file : files) {
        	  
        	  // String xml = JAXBHelper.marshall(files, true, false);
        	  // System.out.println(xml);
        	  
            id = file.getId();
            
            System.out.println("name: "+name +" id:"+id);        
          }  
      }
      
      System.out.println("-------------------------------------------------");
      System.out.println("- 	         List all files 				  -");
      System.out.println("-------------------------------------------------");

      List<String> list = listAllFilesXcdf();
      System.out.println("size:"+ list.size());
      
      for (int i = 0; i < list.size(); i++) {
			System.out.println(i+") "+list.get(i));
	  }
 
      System.out.println("-------------------------------------------------");
      
      
    } catch (Throwable t) {
      throw  t;
    }
  }
  
  public List<String> listPublicFilesPrpt() {
	  return listDirectoryFiles("public",".prpt");
  }
  
  public List<String> listPublicFilesXcdf() {
	  return listDirectoryFiles("public",".xcdf");
  }
  
  public List<String> listAllFilesPrpt() {
	  return listDirectoryFiles("",".prpt");
  }
  
  public List<String> listAllFilesXcdf() {
	  return listDirectoryFiles("",".xcdf");
  }
  
  public List<String> listAllFiles() {
	  return listDirectoryFiles("","");
  }
  
  
  public List<String> listDirectoryFiles(String directoryName, String filterExtension) {
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
  
  private PentahoFileResourceService _pentahoFileResourceService;
}