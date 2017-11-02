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
package org.entando.entando.plugins.jpsubsites.aps.system.services.resource.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;

/**
 * @author E.Santoboni
 */
public class SimpleResourceDataBean implements ResourceDataBean {
	
	@Override
	public String getResourceId() {
		return null;
	}
	
	/**
	 * Restituisce il tipo di risorsa.
	 * @return Il tipo di risorsa.
	 */
	@Override
	public String getResourceType() {
		return _resourceType;
	}
	
	/**
	 * Setta il tipo di risorsa.
	 * @param type Il tipo di risorsa.
	 */
	public void setResourceType(String type) {
		this._resourceType = type;
	}
	
	/**
	 * Setta la descrizione della risorsa.
	 * @param descr La descrizione della risorsa.
	 */
	public void setDescr(String descr) {
		this._descr = descr;
	}
	
	/**
	 * Restituisce la descrizione della risorsa.
	 * @return La descrizione della risorsa.
	 */
	@Override
	public String getDescr() {
		return _descr;
	}
	
	@Override
	public File getFile() {
		return _file;
	}
	public void setFile(File file) {
		this._file = file;
	}
	
	@Override
	public String getMainGroup() {
		return _mainGroup;
	}
	
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	@Override
	public List<Category> getCategories() {
		return _categories;
	}
	
	public void setCategories(List<Category> categories) {
		this._categories = categories;
	}
	
	@Override
	public int getFileSize() {
		return (int) this._file.length()/1000;
	}
	
	@Override
	public InputStream getInputStream() throws Throwable {
		return new FileInputStream(this._file);
	}
	
	@Override
	public String getFileName() {
		String filename = _file.getName().substring(_file.getName().lastIndexOf('/')+1).trim();
		return filename;
	}
	
	@Override
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimetype) {
		this._mimeType = mimetype;
	}
	
	private String _resourceType;
	private String _descr;
	private String _mainGroup;
	private File _file;
	private List<Category> _categories = new ArrayList<Category>();
	private String _mimeType;
	
}

