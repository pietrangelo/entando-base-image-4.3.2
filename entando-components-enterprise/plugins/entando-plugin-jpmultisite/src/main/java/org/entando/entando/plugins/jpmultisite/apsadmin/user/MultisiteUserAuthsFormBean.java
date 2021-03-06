/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpmultisite.apsadmin.user;

import com.agiletec.aps.system.services.authorization.Authorization;

import java.io.Serializable;
import java.util.List;
import org.entando.entando.apsadmin.user.UserAuthsFormBean;

/**
 * @author E.Santoboni
 */
public class MultisiteUserAuthsFormBean extends UserAuthsFormBean implements Serializable {
	
	public MultisiteUserAuthsFormBean(String username, List<Authorization> authorizations) {
		super(username, authorizations);
	}
	
}