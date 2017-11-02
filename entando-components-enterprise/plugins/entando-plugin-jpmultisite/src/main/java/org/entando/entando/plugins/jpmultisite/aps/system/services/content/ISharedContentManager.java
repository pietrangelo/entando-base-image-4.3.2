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
package org.entando.entando.plugins.jpmultisite.aps.system.services.content;

import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface ISharedContentManager {

	public SharedContent getSharedContent(int id) throws ApsSystemException;

	public List<Integer> getSharedContents() throws ApsSystemException;

	public List<Integer> searchSharedContents(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addSharedContent(SharedContent sharedContent) throws ApsSystemException;

	public void updateSharedContent(SharedContent sharedContent) throws ApsSystemException;

	public void deleteSharedContent(int id) throws ApsSystemException;

}