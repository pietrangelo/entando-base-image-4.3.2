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

package org.entando.entando.plugins.jpmultisite.apsadmin.content;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.plugins.jacms.apsadmin.content.ContentCategoryAction;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.apsadmin.category.helper.MultisiteCategoryActionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteContentCategoryAction extends ContentCategoryAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteContentCategoryAction.class);
	
	@Override
	public ITreeNode getAllowedTreeRootNode() {
		ITreeNode node = null;
		String currentMultisiteCode = (String) this.getRequest().getSession().getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		try {
			node = ((MultisiteCategoryActionHelper) this.getTreeHelper()).getAllowedTreeRoot(currentMultisiteCode);
		} catch (Throwable t) {
			_logger.error("error in getAllowedTreeRootNode", t);
		}
		return node;
	}

	
}
