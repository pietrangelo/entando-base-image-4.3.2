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

package org.entando.entando.plugins.jpmultisite.apsadmin.user.group;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.user.group.GroupAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteGroupAction extends GroupAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteGroupAction.class);
	
	public List<Group> getGroups() {
		List<Group> groups = this.getGroupManager().getGroups();
		List<Group> filteredGroups = new ArrayList<Group>();
		for (Group group : groups) {
			if(group.getName().endsWith(MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()))){
				filteredGroups.add(group);
			}
		}
		BeanComparator comparator = new BeanComparator("descr");
		Collections.sort(groups, comparator);
		return filteredGroups;
	}
	
	/**
	 * Verify if a group with that name already exists, used on validation
	 * @return true in caso positivo, false nel caso il gruppo non esista.
	 */
	@Override
	protected boolean existsGroup() {
		String name = this.getName() + MultisiteUtils.getMultisiteCodeSuffix(this.getRequest());
		boolean exists = (name!=null && name.trim().length()>=0 && this.getGroupManager().getGroup(name)!=null);
		return exists;
	}
	
	@Override
	public String save() {
		try {
			Group group = new Group();
			group.setName(this.getName() + MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()));
			group.setDescription(this.getDescription());
			if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
				this.getGroupManager().addGroup(group);
			} else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				this.getGroupManager().updateGroup(group);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	
}