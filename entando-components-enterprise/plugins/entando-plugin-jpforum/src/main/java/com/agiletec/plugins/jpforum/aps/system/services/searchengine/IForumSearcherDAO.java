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
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IForumSearcherDAO {

	public List<Integer> searchPost(String word, Set<String> userGroups) throws ApsSystemException;

	public void init(File dir) throws ApsSystemException;

	public void close();

}
