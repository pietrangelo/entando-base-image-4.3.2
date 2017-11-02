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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.section;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.plugins.jpforum.apsadmin.section.SectionTreeAction;

public class SectionTreeFrontAction extends SectionTreeAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SectionTreeFrontAction.class);

	@Override
	public ITreeNode getTreeRootNode() {
		ITreeNode node = null;
		try {
			node = this.getHelper().getAllowedTreeRoot(this.getCurrentUser());
		} catch (Throwable t) {
			_logger.error("error in getTreeRootNode", t);
		}
		return node;
	}
	
}