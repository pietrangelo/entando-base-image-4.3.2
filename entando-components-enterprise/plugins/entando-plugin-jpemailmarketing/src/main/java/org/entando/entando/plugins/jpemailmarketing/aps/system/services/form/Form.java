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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.form;

import java.util.Date;

public class Form {

	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	public String getFileCoverName() {
		return _fileCoverName;
	}
	public void setFileCoverName(String fileCoverName) {
		this._fileCoverName = fileCoverName;
	}

	public String getFileIncentiveName() {
		return _fileIncentiveName;
	}
	public void setFileIncentiveName(String fileIncentiveName) {
		this._fileIncentiveName = fileIncentiveName;
	}

	public String getEmailSubject() {
		return _emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this._emailSubject = emailSubject;
	}

	public String getEmailText() {
		return _emailText;
	}
	public void setEmailText(String emailText) {
		this._emailText = emailText;
	}

	public String getEmailButton() {
		return _emailButton;
	}
	public void setEmailButton(String emailButton) {
		this._emailButton = emailButton;
	}

	public String getEmailMessageFriendly() {
		return _emailMessageFriendly;
	}
	public void setEmailMessageFriendly(String emailMessageFriendly) {
		this._emailMessageFriendly = emailMessageFriendly;
	}

	public Date getCreatedat() {
		return _createdat;
	}
	public void setCreatedat(Date createdat) {
		this._createdat = createdat;
	}

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
