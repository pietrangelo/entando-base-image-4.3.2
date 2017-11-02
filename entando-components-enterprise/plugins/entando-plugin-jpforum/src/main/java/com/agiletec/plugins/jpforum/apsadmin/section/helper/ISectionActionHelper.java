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
package com.agiletec.plugins.jpforum.apsadmin.section.helper;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public interface ISectionActionHelper {
	
	public ITreeNode getAllowedTreeRoot(UserDetails currentUser) throws ApsSystemException;
	
	/**
	 * Verifica la compatibilità dei gruppi dell'utente con i gruppi della sezione
	 * @param section
	 * @param user 
	 * @return true se l'utente è abilitato alla modifica della sezione
	 * @throws ApsSystemException in case of error
	 */
	public boolean isUserAllowed(Section section, UserDetails user) throws ApsSystemException;

}
