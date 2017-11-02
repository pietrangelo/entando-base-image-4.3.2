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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;

@XmlRootElement(name = "subscriber")
@XmlType(propOrder = {"id", "courseId", "name", "email", "hash", "status", "createdat", "updatedat"})
public class JAXBSubscriber {

    public JAXBSubscriber() {
        super();
    }

    public JAXBSubscriber(Subscriber subscriber) {
		this.setId(subscriber.getId());
		this.setCourseId(subscriber.getCourseId());
		this.setName(subscriber.getName());
		this.setEmail(subscriber.getEmail());
		this.setHash(subscriber.getHash());
		this.setStatus(subscriber.getStatus());
		this.setCreatedat(subscriber.getCreatedat());
		this.setUpdatedat(subscriber.getUpdatedat());
    }
    
    public Subscriber getSubscriber() {
    	Subscriber subscriber = new Subscriber();
		subscriber.setId(this.getId());
		subscriber.setCourseId(this.getCourseId());
		subscriber.setName(this.getName());
		subscriber.setEmail(this.getEmail());
		subscriber.setHash(this.getHash());
		subscriber.setStatus(this.getStatus());
		subscriber.setCreatedat(this.getCreatedat());
		subscriber.setUpdatedat(this.getUpdatedat());
    	return subscriber;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "courseId", required = true)
	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	@XmlElement(name = "name", required = true)
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	@XmlElement(name = "email", required = true)
	public String getEmail() {
		return _email;
	}
	public void setEmail(String email) {
		this._email = email;
	}

	@XmlElement(name = "hash", required = true)
	public String getHash() {
		return _hash;
	}
	public void setHash(String hash) {
		this._hash = hash;
	}

	@XmlElement(name = "status", required = true)
	public String getStatus() {
		return _status;
	}
	public void setStatus(String status) {
		this._status = status;
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
	private int _courseId;
	private String _name;
	private String _email;
	private String _hash;
	private String _status;
	private Date _createdat;
	private Date _updatedat;

}
