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

package org.entando.entando.plugins.jpmultisite.apsadmin.category.helper;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.apsadmin.category.helper.CategoryActionHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * @author S.Loru
 */
public class MultisiteCategoryActionHelper extends CategoryActionHelper {

	public ITreeNode getAllowedTreeRoot(String multisiteCode) {
		TreeNode root = new TreeNode();
		if(StringUtils.isEmpty(multisiteCode)){
			return root;
		}
		ITreeNode currentRoot = this.getCategoryManager().getCategory(multisiteCode);
		this.fillTreeNode(root, root, currentRoot);
		this.addTreeWrapper(root, null, currentRoot);
		return root;
	}
	
	private void addTreeWrapper(TreeNode currentNode, TreeNode parent, ITreeNode currentTreeNode) {
		ITreeNode[] children = currentTreeNode.getChildren();
		for (int i=0; i<children.length; i++) {
			ITreeNode newCurrentTreeNode = children[i];
			TreeNode newNode = new TreeNode();
			this.fillTreeNode(newNode, currentNode, newCurrentTreeNode);
			currentNode.addChild(newNode);
			this.addTreeWrapper(newNode, currentNode, newCurrentTreeNode);
		}
	}
	
}
