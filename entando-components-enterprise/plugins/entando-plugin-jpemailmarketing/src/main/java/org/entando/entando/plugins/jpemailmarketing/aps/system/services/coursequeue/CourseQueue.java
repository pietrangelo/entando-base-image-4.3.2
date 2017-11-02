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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue;

import java.util.Date;

public class CourseQueue {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public Long getOperationindex() {
		return _operationindex;
	}
	public void setOperationindex(Long operationindex) {
		this._operationindex = operationindex;
	}

	public int getSubscriberid() {
		return _subscriberid;
	}
	public void setSubscriberid(int subscriberid) {
		this._subscriberid = subscriberid;
	}

	public int getCoursemailid() {
		return _coursemailid;
	}
	public void setCoursemailid(int coursemailid) {
		this._coursemailid = coursemailid;
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

	public String getMgmessageid() {
		return _mgmessageid;
	}
	public void setMgmessageid(String mgmessageid) {
		this._mgmessageid = mgmessageid;
	}

	public Integer getMgstatus() {
		return _mgstatus;
	}
	public void setMgstatus(Integer mgstatus) {
		this._mgstatus = mgstatus;
	}


	public Date getScheduledDate() {
		return _scheduledDate;
	}
	public void setScheduledDate(Date scheduledDate) {
		this._scheduledDate = scheduledDate;
	}

	public Boolean getSent() {
		return _sent;
	}
	public void setSent(Boolean sent) {
		this._sent = sent;
	}


	private int _id;
	private Long _operationindex;
	private int _subscriberid;
	private int _coursemailid;
	private Date _scheduledDate;
	private Boolean _sent;
	
	private Date _createdat;
	private Date _updatedat;
	private String _mgmessageid;
	private Integer _mgstatus;

}
