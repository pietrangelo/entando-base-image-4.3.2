/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpnotify.aps.system.services.notify.Notify;

@XmlRootElement(name = "notify")
@XmlType(propOrder = {"id", "payload", "senddate", "attempts", "sent", "sender", "recipient"})
public class JAXBNotify {

    public JAXBNotify() {
        super();
    }

    public JAXBNotify(Notify notify) {
		this.setId(notify.getId());
		this.setPayload(notify.getPayload());
		this.setSenddate(notify.getSenddate());
		this.setAttempts(notify.getAttempts());
		this.setSent(notify.getSent());
		this.setSender(notify.getSender());
		this.setRecipient(notify.getRecipient());
    }
    
    public Notify getNotify() {
    	Notify notify = new Notify();
		notify.setId(this.getId());
		notify.setPayload(this.getPayload());
		notify.setSenddate(this.getSenddate());
		notify.setAttempts(this.getAttempts());
		notify.setSent(this.getSent());
		notify.setSender(this.getSender());
		notify.setRecipient(this.getRecipient());
    	return notify;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "payload", required = true)
	public String getPayload() {
		return _payload;
	}
	public void setPayload(String payload) {
		this._payload = payload;
	}

	@XmlElement(name = "senddate", required = true)
	public Date getSenddate() {
		return _senddate;
	}
	public void setSenddate(Date senddate) {
		this._senddate = senddate;
	}

	@XmlElement(name = "attempts", required = true)
	public int getAttempts() {
		return _attempts;
	}
	public void setAttempts(int attempts) {
		this._attempts = attempts;
	}

	@XmlElement(name = "sent", required = true)
	public int getSent() {
		return _sent;
	}
	public void setSent(int sent) {
		this._sent = sent;
	}

	@XmlElement(name = "sender", required = true)
	public String getSender() {
		return _sender;
	}
	public void setSender(String sender) {
		this._sender = sender;
	}

	@XmlElement(name = "recipient", required = true)
	public String getRecipient() {
		return _recipient;
	}
	public void setRecipient(String recipient) {
		this._recipient = recipient;
	}


	private int _id;
	private String _payload;
	private Date _senddate;
	private int _attempts;
	private int _sent;
	private String _sender;
	private String _recipient;

}
