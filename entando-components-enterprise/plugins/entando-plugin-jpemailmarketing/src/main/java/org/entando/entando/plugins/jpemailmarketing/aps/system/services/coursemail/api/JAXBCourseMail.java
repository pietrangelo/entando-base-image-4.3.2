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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.CourseMail;

@XmlRootElement(name = "courseMail")
@XmlType(propOrder = {"id", "courseid", "position",  "dalayDays", "emailSubject", "emailContent", "createdat", "updatedat"})
public class JAXBCourseMail {

	public JAXBCourseMail() {
		super();
	}

	public JAXBCourseMail(CourseMail courseMail) {
		this.setId(courseMail.getId());
		this.setCourseid(courseMail.getCourseid());
		this.setPosition(courseMail.getPosition());
		this.setDalayDays(courseMail.getDalayDays());
		this.setEmailSubject(courseMail.getEmailSubject());
		this.setEmailContent(courseMail.getEmailContent());
		this.setCreatedat(courseMail.getCreatedat());
		this.setUpdatedat(courseMail.getUpdatedat());
	}

	public CourseMail getCourseMail() {
		CourseMail courseMail = new CourseMail();
		courseMail.setId(this.getId());
		courseMail.setCourseid(this.getCourseid());
		courseMail.setPosition(this.getPosition());
		courseMail.setDalayDays(this.getDalayDays());
		courseMail.setEmailSubject(this.getEmailSubject());
		courseMail.setEmailContent(this.getEmailContent());
		courseMail.setCreatedat(this.getCreatedat());
		courseMail.setUpdatedat(this.getUpdatedat());
		return courseMail;
	}

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "courseid", required = true)
	public int getCourseid() {
		return _courseid;
	}
	public void setCourseid(int courseid) {
		this._courseid = courseid;
	}

	@XmlElement(name = "position", required = true)
	public int getPosition() {
		return _position;
	}
	public void setPosition(int position) {
		this._position = position;
	}

	@XmlElement(name = "dalayDays", required = true)
	public int getDalayDays() {
		return _dalayDays;
	}
	public void setDalayDays(int dalayDays) {
		this._dalayDays = dalayDays;
	}

	@XmlElement(name = "emailSubject", required = true)
	public String getEmailSubject() {
		return _emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this._emailSubject = emailSubject;
	}

	@XmlElement(name = "emailContent", required = true)
	public String getEmailContent() {
		return _emailContent;
	}
	public void setEmailContent(String emailContent) {
		this._emailContent = emailContent;
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

	private int _id;
	private int _courseid;
	private int _position;
	private int _dalayDays;
	private String _emailSubject;
	private String _emailContent;
	private Date _createdat;
	private Date _updatedat;

}
