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
package com.agiletec.plugins.jpforum.aps.system.services.searchengine;

import java.io.File;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;

public interface IForumIndexerDAO {

	public static final String POST_CODE_FIELD_NAME = "code";
	public static final String POST_TITLE_FIELD_NAME = "title";
	public static final String POST_TEXT_FIELD_NAME = "text";
	public static final String POST_SECTION_GROUPNAME_FIELD_NAME = "sectionGroupName";
	public static final String POST_SECTION_UNAUTHBEHAVIOUR_FIELD_NAME = "sectionUnauthBehaviour";
	
	public void init(File dir, boolean newIndex) throws ApsSystemException;
	
	public void add(Post post) throws ApsSystemException;

	public void delete(String postCodeFieldName, int code) throws ApsSystemException;

	public void close();

}
