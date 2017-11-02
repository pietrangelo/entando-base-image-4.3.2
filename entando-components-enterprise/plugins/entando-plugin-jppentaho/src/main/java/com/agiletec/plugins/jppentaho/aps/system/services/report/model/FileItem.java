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
package com.agiletec.plugins.jppentaho.aps.system.services.report.model;

import java.util.ArrayList;
import java.util.List;

public class FileItem {
	
	public void addChild(FileItem child) {
		this._childs.add(child);
	}

	public void setDirectory(boolean directory) {
		this._directory = directory;
	}
	public boolean isDirectory() {
		return _directory;
	}

	public void setName(String name) {
		this._name = name;
	}
	public String getName() {
		return _name;
	}

	public void setType(String type) {
		this._type = type;
	}
	public String getType() {
		return _type;
	}

	public void setChilds(List<FileItem> childs) {
		this._childs = childs;
	}
	public List<FileItem> getChilds() {
		return _childs;
	}

	public void setRoot(boolean root) {
		this._root = root;
	}
	public boolean isRoot() {
		return _root;
	}

	private boolean _root = false;
	private boolean _directory = false;
	private String _type;
	private String _name;
	private List<FileItem> _childs = new ArrayList<FileItem>();
	
}
