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
package org.entando.entando.plugins.jpmultisite.aps.system.services.multisite;

import com.agiletec.aps.util.ApsProperties;



public class Multisite implements Cloneable {

	public Multisite() {
	}
	
	public Multisite(String code, ApsProperties titles, ApsProperties descriptions, String url, String headerImage, String templateCss, String catCode) {
		this._code = code;
		this._titles = titles;
		this._descriptions = descriptions;
		this._url = url;
		this._headerImage = headerImage;
		this._templateCss = templateCss;
		this._catCode = catCode;
	}

	@Override
	public String toString() {
		return "Multisite{" + "_code=" + _code + ", _titles=" + _titles + ", _descriptions=" + _descriptions + ", _url=" + _url + ", _headerImage=" + _headerImage + ", _templateCss=" + _templateCss + '}';
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + (this._code != null ? this._code.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Multisite other = (Multisite) obj;
		if ((this._code == null) ? (other._code != null) : !this._code.equals(other._code)) {
			return false;
		}
		return true;
	}

	@Override
	public Multisite clone() throws CloneNotSupportedException {
		Multisite multisite = new Multisite();
		multisite.setCode(this.getCode());
		
		ApsProperties titles = new ApsProperties();
		titles.putAll(this.getTitles());
		multisite.setTitles(titles);
		
		ApsProperties descriptions = new ApsProperties();
		descriptions.putAll(this.getDescriptions());
		multisite.setDescriptions(descriptions);
		
		multisite.setUrl(this.getUrl());
		multisite.setHeaderImage(this.getHeaderImage());
		multisite.setTemplateCss(this.getTemplateCss());
		multisite.setCatCode(this.getCatCode());
		return multisite;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
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

	public String getUrl() {
		return _url;
	}
	public void setUrl(String url) {
		this._url = url;
	}

	public String getHeaderImage() {
		return _headerImage;
	}
	public void setHeaderImage(String headerImage) {
		this._headerImage = headerImage;
	}

	public String getTemplateCss() {
		return _templateCss;
	}
	public void setTemplateCss(String templateCss) {
		this._templateCss = templateCss;
	}

	public String getCatCode() {
		return _catCode;
	}

	public void setCatCode(String catcode) {
		this._catCode = catcode;
	}
	
	
	private String _code;
	private ApsProperties _titles;
	private ApsProperties _descriptions;
	private String _url;
	private String _headerImage;
	private String _templateCss;
	private String _catCode;

}
