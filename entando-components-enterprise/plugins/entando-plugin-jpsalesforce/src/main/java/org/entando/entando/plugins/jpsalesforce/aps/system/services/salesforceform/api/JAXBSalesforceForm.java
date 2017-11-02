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
*/package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.api;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.SalesforceForm;
import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;

@XmlRootElement(name = "salesforceForm")
@XmlType(propOrder = {"id", "code", "pagecode", "frame", "config"})
public class JAXBSalesforceForm {

    public JAXBSalesforceForm() {
        super();
    }

    public JAXBSalesforceForm(SalesforceForm salesforceForm) throws Throwable {
		this.setId(salesforceForm.getId());
		this.setCode(salesforceForm.getCode());
		this.setPagecode(salesforceForm.getPagecode());
		this.setFrame(salesforceForm.getFrame());
		this.setConfig(salesforceForm.getConfig());
    }
    
    public SalesforceForm getSalesforceForm() {
    	SalesforceForm salesforceForm = new SalesforceForm();
		salesforceForm.setId(this.getId());
		salesforceForm.setCode(this.getCode());
		salesforceForm.setPagecode(this.getPagecode());
		salesforceForm.setFrame(this.getFrame());
		salesforceForm.setConfig(this.getConfig());
    	return salesforceForm;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "code", required = true)
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	@XmlElement(name = "pagecode", required = true)
	public String getPagecode() {
		return _pagecode;
	}
	public void setPagecode(String pagecode) {
		this._pagecode = pagecode;
	}

	@XmlElement(name = "frame", required = true)
	public int getFrame() {
		return _frame;
	}
	public void setFrame(int frame) {
		this._frame = frame;
	}

	@XmlElement(name = "config", required = true)
	public FormConfiguration getConfig() {
		return _config;
	}
	public void setConfig(FormConfiguration config) {
		this._config = config;
	}


	private int _id;
	private String _code;
	private String _pagecode;
	private int _frame;
	private FormConfiguration _config;

}
