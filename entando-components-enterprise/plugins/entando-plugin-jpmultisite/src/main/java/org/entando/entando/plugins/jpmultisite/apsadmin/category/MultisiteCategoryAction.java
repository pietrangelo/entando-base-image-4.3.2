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
package org.entando.entando.plugins.jpmultisite.apsadmin.category;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.TreeNode;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.apsadmin.category.CategoryAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import static com.agiletec.apsadmin.system.BaseAction.FAILURE;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteCategoryAction extends CategoryAction {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteCategoryAction.class);

	@Override
	public ITreeNode getTreeRootNode() {
		TreeNode root = new TreeNode();
		String multisiteCode = MultisiteUtils.getMultisiteCode(this.getRequest());
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
	
	private void fillTreeNode(TreeNode nodeToValue, TreeNode parent, ITreeNode realNode) {
		nodeToValue.setCode(realNode.getCode());
		if (null == parent) {
			nodeToValue.setParent(nodeToValue);
		} else {
			nodeToValue.setParent(parent);
		}
		Set<Object> codes = realNode.getTitles().keySet();
		Iterator<Object> iterKey = codes.iterator();
		while (iterKey.hasNext()) {
			String key = (String) iterKey.next();
			String title = realNode.getTitles().getProperty(key);
			nodeToValue.getTitles().put(key, title);
		}
	}
	
	@Override
	public String save() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
				Category category = this.getCategory(this.getCategoryCode());
				category.setTitles(this.getTitles());
				this.getCategoryManager().updateCategory(category);
				_logger.debug("Updated category {}", category.getCode());
			} else {
				Category category = this.getHelper().buildNewCategory(this.getCategoryCode() + MultisiteUtils.getMultisiteCodeSuffix(this.getRequest()), this.getParentCategoryCode(), this.getTitles());
				this.getCategoryManager().addCategory(category);
				_logger.debug("Added new category {}", this.getCategoryCode());
			}
		} catch (Exception e) {
			_logger.error("error in save", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	public IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}

	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}

	private IMultisiteManager _multisiteManager;

}
