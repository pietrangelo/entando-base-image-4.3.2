/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.system.services.subsite;

import java.util.Collection;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.SubsiteConfig;

public interface ISubsiteManager {
	
	public SubsiteConfig getSubsiteConfig();
	
	public void updateConfig(SubsiteConfig config) throws ApsSystemException ;
	
	public String getRootPageCode();
	
	public String getCategoriesRoot();
	
	/**
	 * Load the map of subsites
	 * @return The map of subsites
	 * @throws ApsSystemException in case of error
	 */
	public Map<String, Subsite> getSubsitesMap() throws ApsSystemException;
	
	/**
	 * Load the collection of subsites
	 * @return The collection of subsites
	 * @throws ApsSystemException in case of error
	 */
	public Collection<Subsite> getSubsites() throws ApsSystemException;
	
	/**
	 * Load the subsite associated to the content of given id.
	 * The relationship is obtained through the associated categories.
	 * @param contentId Thje id of the content.
	 * @return The subsite associated to the content of given id.
	 * @throws ApsSystemException in case of error
	 */
	public String getSubsiteCodeForContent(String contentId) throws ApsSystemException;
	
	/**
	 * Returns the code of the model for the given content type.
	 * @param contentType The code of the content type.
	 * @return The code of the model for the given content type.
	 * @throws ApsSystemException In case of error.
	 */
	public Long getModelForContentType(String contentType) throws ApsSystemException;
	
	/**
	 * Load the subsite of given code.
	 * @param code The code of the subsite.
	 * @return The Desired subsite.
	 * @throws ApsSystemException In case of error.
	 */
	public Subsite getSubsite(String code) throws ApsSystemException;
	
	/**
	 * Returns the code of the subsite associated to the given category.
	 * @param categoryCode The code of the category.
	 * @return The code of the subsite associated to the given category.
	 * @throws ApsSystemException In case of error.
	 */
	public String getSubsiteCodeForCategory(String categoryCode) throws ApsSystemException;
	
	public Subsite getSubsiteFromRootPage(String rootPageCode) throws ApsSystemException;
	
	/**
	 * Returns the code of the subsite associated to the given root page.
	 * @param rootPageCode The code of the root of the desired subsite.
	 * @return The code of the subsite associated to the given root page.
	 * @throws ApsSystemException In case of error.
	 */
	public String getSubsiteCodeFromRootPage(String rootPageCode) throws ApsSystemException;
	
	public Subsite getSubsiteForPage(IPage page) throws ApsSystemException;
	
	/**
	 * Returns the code of the subsite associated to the given page or to its ancestor.
	 * @param page The page.
	 * @return The code of the subsite associated to the given page or to its ancestor.
	 * @throws ApsSystemException In case of error.
	 */
	public String getSubsiteCodeForPage(IPage page) throws ApsSystemException;
	
	/**
	 * Add the given subsite.
	 * @param subsite The new subsite.
	 * @throws ApsSystemException In case of error.
	 */
	public void addSubsite(Subsite subsite) throws ApsSystemException;
	
	/**
	 * Update the given subsite.
	 * @param subsite The subsite to update.
	 * @throws ApsSystemException In case of error.
	 */
	public void updateSubsite(Subsite subsite) throws ApsSystemException;
	
	/**
	 * Remove the subsite of given code.
	 * @param code The code of the subsite to remove.
	 * @throws ApsSystemException In case of error.
	 */
	public void removeSubsite(String code) throws ApsSystemException;
	
 //       public void cloneResources(Subsite source, Subsite clone) throws ApsSystemException;
        
       // public void cloneContents(Subsite source, Subsite clone) throws ApsSystemException;
        
	public static final String PLUGIN_STATIC_SUBFOLDER = "plugins/jpsubsites/static";
	public static final String CSS_SUBFOLDER = PLUGIN_STATIC_SUBFOLDER + "/css";
	public static final String HEADER_SUBFOLDER = PLUGIN_STATIC_SUBFOLDER + "/img";
	
}