/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.aps.system;

/**
 * @author S.Loru
 */
public class JpmultisiteSystemConstants {
	
	 /**
     * Request context extra param name for multisite code
     */
    public static final String SESSION_PAR_CURRENT_MULTISITE = "currentMultisite";
	
	/**
     * Multisite Manager
     */
	public static final String MULTISITE_MANAGER = "jpmultisiteMultisiteManager";
	
	/**
     * Multisite Clone Manager
     */
	public static final String MULTISITE_CLONE_MANAGER = "jpmultisiteMultisiteCloneManager";
	
	
	/**
     * Multisite Activity Stream Manager
     */
	public static final String MULTISITE_ACTIVITY_STREAM_MANAGER = "jpMultisiteActivityStreamManager";
	
	/**
     * Multisite Widget Type Manager
     */
	public static final String MULTISITE_WIDGETTYPE_MANAGER = "jpMultisiteWidgetTypeManager";
	/**
     * Multisite Resource Manager
     */
	public static final String MULTISITE_RESOURCE_MANAGER = "jpmultisiteResourceManager";
	/**
     * Multisite Content Manager
     */
	public static final String MULTISITE_CONTENT_MANAGER = "jpmultisiteContentManager";
	
	/**
	 * Multisite action result
	 */
	
	public static final String MULTISITE_RESULT = "multisite";
	
	/**
	 * Multisite group prefix
	 */
	
	public static final String MULTISITE_GROUP_PREFIX = "multisite";
	
	/**
	 * Multisite userNotAllowed default error
	 */
	public static final String MULTISITE_DEFAULT_ERROR_RESULT = "multisiteUserNotAllowed";
	
	/**
	 * Multisite suffix separator
	 */
	public static final String MULTISITE_SUFFIX_SEPARATOR = "@";
	
	/**
	 * Multisite subpath for css and header image
	 */
	public static final String MULTISITE_SUBPATH_RESOURCES_ROOT = "plugins/jpmultisite/multisite_resources/";
	
	/**
	 * Multisite css filename
	 */
	public static final String MULTISITE_CSS_FILENAME = "template_css_";
	
	/**
	 * Multisite header filename
	 */
	public static final String MULTISITE_HEADER_IMAGE_FILENAME = "header_image_";
	
	/**
	 * Multisite generic homepage model
	 */
	public static final String MULTISITE_PAGEMODEL_HOME = "jpmultisite_home";
	
	/**
	 * Multisite code regex
	 */
	public static final String MULTISITE_CODE_REGEX = "([a-zA-Z0-9_@])*";
	
}
