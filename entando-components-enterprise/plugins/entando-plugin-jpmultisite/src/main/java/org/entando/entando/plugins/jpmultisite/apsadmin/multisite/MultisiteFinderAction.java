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
package org.entando.entando.plugins.jpmultisite.apsadmin.multisite;

import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultisiteFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(MultisiteFinderAction.class);

	public List<String> getMultisitesId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (StringUtils.isNotBlank(this.getCode())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("code"), this.getCode(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getTitles())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("titles"), this.getTitles(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getDescriptions())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("descriptions"), this.getDescriptions(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getUrl())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("url"), this.getUrl(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getHeaderImage())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("headerImage"), this.getHeaderImage(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getTemplateCss())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("templateCss"), this.getTemplateCss(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getGroup())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("group"), this.getGroup(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getCategory())) {
				//TODO add a constant into your IMultisiteManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("category"), this.getCategory(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<String> multisites = this.getMultisiteManager().searchMultisites(filters);
			return multisites;
		} catch (Throwable t) {
			_logger.error("Error getting multisites list", t);
			throw new RuntimeException("Error getting multisites list", t);
		}
	}

	protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public Multisite getMultisite(String code) {
		Multisite multisite = null;
		try {
			multisite = this.getMultisiteManager().loadMultisite(code);
		} catch (Throwable t) {
			_logger.error("Error getting multisite with code {}", code, t);
			throw new RuntimeException("Error getting multisite with code " + code, t);
		}
		return multisite;
	}


	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}


	public String getTitles() {
		return _titles;
	}
	public void setTitles(String titles) {
		this._titles = titles;
	}


	public String getDescriptions() {
		return _descriptions;
	}
	public void setDescriptions(String descriptions) {
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


	public String getGroup() {
		return _group;
	}
	public void setGroup(String group) {
		this._group = group;
	}


	public String getCategory() {
		return _category;
	}
	public void setCategory(String category) {
		this._category = category;
	}


	protected IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}
	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}
	
	private String _code;
	private String _titles;
	private String _descriptions;
	private String _url;
	private String _headerImage;
	private String _templateCss;
	private String _group;
	private String _category;
	private IMultisiteManager _multisiteManager;
}