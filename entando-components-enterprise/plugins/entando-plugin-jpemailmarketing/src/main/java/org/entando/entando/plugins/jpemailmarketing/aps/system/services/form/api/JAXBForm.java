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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;

@XmlRootElement(name = "form")
@XmlType(propOrder = {"courseId", "fileCoverName", "fileIncentiveName", "emailSubject", "emailText", "emailButton", "emailMessageFriendly", "createdat", "updatedat"})
public class JAXBForm {

    public JAXBForm() {
        super();
    }

    public JAXBForm(Form form) {
		this.setCourseId(form.getCourseId());
		this.setFileCoverName(form.getFileCoverName());
		this.setFileIncentiveName(form.getFileIncentiveName());
		this.setEmailSubject(form.getEmailSubject());
		this.setEmailText(form.getEmailText());
		this.setEmailButton(form.getEmailButton());
		this.setEmailMessageFriendly(form.getEmailMessageFriendly());
		this.setCreatedat(form.getCreatedat());
		this.setUpdatedat(form.getUpdatedat());
    }
    
    public Form getForm() {
    	Form form = new Form();
		form.setCourseId(this.getCourseId());
		form.setFileCoverName(this.getFileCoverName());
		form.setFileIncentiveName(this.getFileIncentiveName());
		form.setEmailSubject(this.getEmailSubject());
		form.setEmailText(this.getEmailText());
		form.setEmailButton(this.getEmailButton());
		form.setEmailMessageFriendly(this.getEmailMessageFriendly());
		form.setCreatedat(this.getCreatedat());
		form.setUpdatedat(this.getUpdatedat());
    	return form;
    }

	@XmlElement(name = "courseId", required = true)
	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	@XmlElement(name = "fileCoverName", required = true)
	public String getFileCoverName() {
		return _fileCoverName;
	}
	public void setFileCoverName(String fileCoverName) {
		this._fileCoverName = fileCoverName;
	}

	@XmlElement(name = "fileIncentiveName", required = true)
	public String getFileIncentiveName() {
		return _fileIncentiveName;
	}
	public void setFileIncentiveName(String fileIncentiveName) {
		this._fileIncentiveName = fileIncentiveName;
	}

	@XmlElement(name = "emailSubject", required = true)
	public String getEmailSubject() {
		return _emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this._emailSubject = emailSubject;
	}

	@XmlElement(name = "emailText", required = true)
	public String getEmailText() {
		return _emailText;
	}
	public void setEmailText(String emailText) {
		this._emailText = emailText;
	}

	@XmlElement(name = "emailButton", required = true)
	public String getEmailButton() {
		return _emailButton;
	}
	public void setEmailButton(String emailButton) {
		this._emailButton = emailButton;
	}

	@XmlElement(name = "emailMessageFriendly", required = true)
	public String getEmailMessageFriendly() {
		return _emailMessageFriendly;
	}
	public void setEmailMessageFriendly(String emailMessageFriendly) {
		this._emailMessageFriendly = emailMessageFriendly;
	}

	@XmlElement(name = "createdat", required = true)
	public Date getCreatedat() {
		return _createdat;
	}
	public void setCreatedat(Date createdat) {
		this._createdat = createdat;
	}

	@XmlElement(name = "updatedat", required = true)
	public Date getUpdatedat() {
		return _updatedat;
	}
	public void setUpdatedat(Date updatedat) {
		this._updatedat = updatedat;
	}


	private int _courseId;
	private String _fileCoverName;
	private String _fileIncentiveName;
	private String _emailSubject;
	private String _emailText;
	private String _emailButton;
	private String _emailMessageFriendly;
	private Date _createdat;
	private Date _updatedat;

}
