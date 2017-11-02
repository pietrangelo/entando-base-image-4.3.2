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
package com.agiletec.plugins.jpforum.aps.system.services.sanction;

import java.util.Date;

public class Sanction {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public Date getStartDate() {
		return _startDate;
	}
	public void setStartDate(Date startDate) {
		this._startDate = startDate;
	}

	public Date getEndDate() {
		return _endDate;
	}
	public void setEndDate(Date endDate) {
		this._endDate = endDate;
	}

	public String getModerator() {
		return _moderator;
	}
	public void setModerator(String moderator) {
		this._moderator = moderator;
	}

	private int _id;
	private String _username;
	private Date _startDate;
	private Date _endDate;
	private String _moderator;
}
