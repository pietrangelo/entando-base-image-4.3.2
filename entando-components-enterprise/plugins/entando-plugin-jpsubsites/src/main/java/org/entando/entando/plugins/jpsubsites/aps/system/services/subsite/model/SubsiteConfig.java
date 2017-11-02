/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SubsiteConfig implements Cloneable {
	
	@Override
	public SubsiteConfig clone() {
		SubsiteConfig config = new SubsiteConfig();
		config.setRootPageCode(this.getRootPageCode());
		config.setPageModel(this.getPageModel());
		config.setCategoriesRoot(this.getCategoriesRoot());
		if (null != this.getContentModels()) {
			Iterator<String> iter = this.getContentModels().keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				config.addContentModel(key, this.getContentModels().get(key));
			}
		}
		return config;
	}
	
	public String getRootPageCode() {
		return _rootPageCode;
	}
	public void setRootPageCode(String rootPageCode) {
		this._rootPageCode = rootPageCode;
	}
	
	public String getCategoriesRoot() {
		return _categoriesRoot;
	}
	public void setCategoriesRoot(String categoriesRoot) {
		this._categoriesRoot = categoriesRoot;
	}
	
	public String getPageModel() {
		return _pageModel;
	}
	public void setPageModel(String pageModel) {
		this._pageModel = pageModel;
	}
	
	public Map<String, Long> getContentModels() {
		return _contentModels;
	}
	public void setContentModels(Map<String, Long> contentModels) {
		this._contentModels = contentModels;
	}
	public Long getModelId(String typeCode) {
		return this._contentModels.get(typeCode);
	}
	public void addContentModel(String typeCode, Long modelId) {
		this._contentModels.put(typeCode, modelId);
	}
	
	private String _rootPageCode;
	private String _categoriesRoot;
	private String _pageModel;
	private Map<String, Long> _contentModels = new HashMap<String, Long>();
	
}