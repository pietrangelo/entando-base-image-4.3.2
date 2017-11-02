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

import com.agiletec.aps.util.ApsProperties;

public class Subsite implements Cloneable {
	
	private String _code;
	private ApsProperties _titles;
	private ApsProperties _descriptions;
	private String _homepage;
	private String _groupName;
	private String _categoryCode;
	private String _contentViewerPage;
	private String _cssFileName;
	private String _imageFileName;
	private boolean _visibility;
	private ApsProperties _viewerPages;
	
	@Override
	public Subsite clone() {
		Subsite subsite = new Subsite();
		subsite.setCode(this.getCode());

		ApsProperties titles = new ApsProperties();
		titles.putAll(this.getTitles());
		subsite.setTitles(titles);

		ApsProperties descriptions = new ApsProperties();
		descriptions.putAll(this.getDescriptions());
		subsite.setDescriptions(descriptions);
		subsite.setHomepage(this.getHomepage());
		subsite.setCategoryCode(this.getCategoryCode());
		subsite.setGroupName(this.getGroupName());
		subsite.setContentViewerPage(this.getContentViewerPage());
		subsite.setCssFileName(this.getCssFileName());
		subsite.setImageFileName(this.getImageFileName());
		subsite.setGroupName(this.getGroupName());
		subsite.setVisibility(this.getVisibility());
		subsite.setViewerPages(this.getViewerPages());
		return subsite;
	}

	public String getCode() {
		return _code;
	}
	
	public void setCode(String code) {
		if (code != null) {
			code = code.trim();
		}
		this._code = code;
	}
	
	public ApsProperties getTitles() {
		return _titles;
	}
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}

	public ApsProperties getDescriptions() {
		return _descriptions;
	}
	public void setDescriptions(ApsProperties descriptions) {
		this._descriptions = descriptions;
	}

	public String getHomepage() {
		return _homepage;
	}
	public void setHomepage(String homepage) {
		this._homepage = homepage;
	}

	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}
	/*
	public void setOffGroupFree(boolean b) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	*/
	public String getCategoryCode() {
		return _categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this._categoryCode = categoryCode;
	}

	public String getContentViewerPage() {
		return _contentViewerPage;
	}
	public void setContentViewerPage(String contentViewerPage) {
		this._contentViewerPage = contentViewerPage;
	}

	public String getCssFileName() {
		return _cssFileName;
	}
	public void setCssFileName(String cssFileName) {
		this._cssFileName = cssFileName;
	}

	public String getImageFileName() {
		return _imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this._imageFileName = imageFileName;
	}
	
	public boolean getVisibility() {
		return _visibility;
	}
	public void setVisibility(boolean visibility) {
		this._visibility = visibility;
	}
	
	public ApsProperties getViewerPages() {
		return _viewerPages;
	}
	public void setViewerPages(ApsProperties viewerPages) {
		this._viewerPages = viewerPages;
	}
	
}
