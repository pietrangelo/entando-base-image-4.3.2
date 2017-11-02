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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber;

import java.util.Date;

public class Subscriber {

	/**
	 * @return name &lt;example@com&gt;
	 */
	public String getPrettyEmailAddress() {
		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(this.getName()).append(" <").append(this.getEmail()).append(">");
		return sbBuffer.toString();
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	public String getEmail() {
		return _email;
	}
	public void setEmail(String email) {
		this._email = email;
	}

	public String getHash() {
		return _hash;
	}
	public void setHash(String hash) {
		this._hash = hash;
	}

	public String getStatus() {
		return _status;
	}
	public void setStatus(String status) {
		this._status = status;
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


	private int _id;
	private int _courseId;
	private String _name;
	private String _email;
	private String _hash;
	private String _status;
	private Date _createdat;
	private Date _updatedat;

	public static final String STATUS_UNCONFIRMED = "unconfirmed";
	public static final String STATUS_CONFIRMED = "confirmed";
	public static final String STATUS_UNSUBSCRIBED = "unsubscribed";

	/**
	 * on registration the user choose to NOT receive any other communication
	 */
	public static final String STATUS_UNREGISTERED = "unregistered";
	
	public static final String[] STATUS_SUBSCRIBERS = {STATUS_UNCONFIRMED, STATUS_CONFIRMED, STATUS_UNSUBSCRIBED};
	public static final String[] STATUS_ALL = {STATUS_UNCONFIRMED, STATUS_CONFIRMED, STATUS_UNSUBSCRIBED, STATUS_UNREGISTERED};
	
}
