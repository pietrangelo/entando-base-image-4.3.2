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

package org.entando.entando.plugins.jpmultisite.apsadmin.resource;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import com.agiletec.plugins.jacms.apsadmin.resource.ResourceAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.resource.MultisiteResourceManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.category.helper.MultisiteCategoryActionHelper;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteResourceAction extends ResourceAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteResourceAction.class);
	
	@Override
	public String save() {
		try {
			if (ApsAdminSystemConstants.ADD == this.getStrutsAction()) {
				((MultisiteResourceManager) this.getResourceManager()).addResource(this, MultisiteUtils.getMultisiteCode(this.getRequest()));
			} else if (ApsAdminSystemConstants.EDIT == this.getStrutsAction()) {
				this.getResourceManager().updateResource(this);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public Category getCategoryRoot() {
		return this.getCategoryManager().getCategory(MultisiteUtils.getMultisiteCode(this.getRequest())); //To change body of generated methods, choose Tools | Templates.
	}


}
