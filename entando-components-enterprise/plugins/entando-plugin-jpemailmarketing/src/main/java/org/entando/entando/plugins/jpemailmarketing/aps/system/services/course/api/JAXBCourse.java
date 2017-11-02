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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;

@XmlRootElement(name = "course")
@XmlType(propOrder = {"id", "name", "sender", "active", "cronexp", "crontimezoneid", "createdat", "updatedat"})
public class JAXBCourse {

    public JAXBCourse() {
        super();
    }

    public JAXBCourse(Course course) {
		this.setId(course.getId());
		this.setName(course.getName());
		this.setSender(course.getSender());
		this.setActive(course.getActive());
		this.setCronexp(course.getCronexp());
		this.setCrontimezoneid(course.getCrontimezoneid());
		this.setCreatedat(course.getCreatedat());
		this.setUpdatedat(course.getUpdatedat());
    }
    
    public Course getCourse() {
    	Course course = new Course();
		course.setId(this.getId());
		course.setName(this.getName());
		course.setSender(this.getSender());
		course.setActive(this.getActive());
		course.setCronexp(this.getCronexp());
		course.setCrontimezoneid(this.getCrontimezoneid());
		course.setCreatedat(this.getCreatedat());
		course.setUpdatedat(this.getUpdatedat());
    	return course;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "name", required = true)
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	@XmlElement(name = "sender", required = true)
	public String getSender() {
		return _sender;
	}
	public void setSender(String sender) {
		this._sender = sender;
	}

	@XmlElement(name = "active", required = true)
	public int getActive() {
		return _active;
	}
	public void setActive(int active) {
		this._active = active;
	}

	@XmlElement(name = "cronexp", required = true)
	public String getCronexp() {
		return _cronexp;
	}
	public void setCronexp(String cronexp) {
		this._cronexp = cronexp;
	}

	@XmlElement(name = "crontimezoneid", required = true)
	public String getCrontimezoneid() {
		return _crontimezoneid;
	}
	public void setCrontimezoneid(String crontimezoneid) {
		this._crontimezoneid = crontimezoneid;
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
	private String _name;
	private String _sender;
	private int _active;
	private String _cronexp;
	private String _crontimezoneid;
	private Date _createdat;
	private Date _updatedat;

}
