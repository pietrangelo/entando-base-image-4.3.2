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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course;

import java.util.Date;

public class Course {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	public String getSender() {
		return _sender;
	}
	public void setSender(String sender) {
		this._sender = sender;
	}

	public int getActive() {
		return _active;
	}
	public void setActive(int active) {
		this._active = active;
	}

	public String getCronexp() {
		return _cronexp;
	}
	public void setCronexp(String cronexp) {
		this._cronexp = cronexp;
	}

	public String getCrontimezoneid() {
		return _crontimezoneid;
	}
	public void setCrontimezoneid(String crontimezoneid) {
		this._crontimezoneid = crontimezoneid;
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
	private String _name;
	private String _sender;
	private int _active;
	private String _cronexp;
	private String _crontimezoneid;
	private Date _createdat;
	private Date _updatedat;

}
