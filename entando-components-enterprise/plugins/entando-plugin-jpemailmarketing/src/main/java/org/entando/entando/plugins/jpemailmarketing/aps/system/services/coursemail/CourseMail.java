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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail;

import java.util.Date;

public class CourseMail {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public int getCourseid() {
		return _courseid;
	}
	public void setCourseid(int courseid) {
		this._courseid = courseid;
	}

	/**
	 * the position in the group
	 * @return
	 */
	public int getPosition() {
		return _position;
	}
	public void setPosition(int position) {
		this._position = position;
	}

	/**
	 * delay days from the position-1 element.
	 * If this is the 0 element, this delay if from the registration date
	 * @return
	 */
	public int getDalayDays() {
		return _dalayDays;
	}
	public void setDalayDays(int dalayDays) {
		this._dalayDays = dalayDays;
	}

	public String getEmailSubject() {
		return _emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this._emailSubject = emailSubject;
	}

	public String getEmailContent() {
		return _emailContent;
	}
	public void setEmailContent(String emailContent) {
		this._emailContent = emailContent;
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


	/**
	 * progressive sum from the user activation
	 * @return
	 */
	public int getSubscriberDelay() {
		return _subscriberDelay;
	}
	public void setSubscriberDelay(int subscriberDelay) {
		this._subscriberDelay = subscriberDelay;
	}


	private int _id;
	private int _courseid;
	private int _position;
	private int _dalayDays;
	private String _emailSubject;
	private String _emailContent;
	private Date _createdat;
	private Date _updatedat;
	
	private int _subscriberDelay;

}
