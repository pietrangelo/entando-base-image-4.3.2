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
package org.entando.entando.plugins.jpsubsites.aps.system;

/**
 * This interface contains the constants of Subsite Plugin
 * @author E.Santoboni, E.Mezzano
 */
public interface JpsubsitesSystemConstants {
	
	/**
	 * The name of the Spring bean mapping the Subsite Manager.
	 */
	public static final String SUBSITE_MANAGER = "jpsubsitesSubsiteManager";
	
	/**
	 * The name of the configuration item containing the Subsites Configuration.
	 */
	public static final String SUBSITE_CONFIG_ITEM = "jpsubsites_subsiteConfig";
	
	/**
	 * The name of the request parameter containing the current subsite.
	 */
	public static final String REQUEST_PARAM_CURRENT_SUBSITE = "jpsubsites_currentSubsite";
	
	public static final String CONTENT_LIST_HELPER = "jpsubsitesContentListHelper";
	
	public static final String BREADCRUMBS_NAVIGATION_PARSER = "jpsubsitesBreadcrumbsNavigatorParser";
        
        public static final String SUBSITE_SUFFIX_SEPARATOR = "@";
        
        public static final String SUBSITE_GROUP_PREFIX = "subsite";
            
        public static final String SESSION_PAR_CURRENT_SUBSITE = "currentSubsite";
	
}