/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify;

import java.util.Date;

public class Notify {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getPayload() {
		return _payload;
	}
	public void setPayload(String payload) {
		this._payload = payload;
	}

	public Date getSenddate() {
		return _senddate;
	}
	public void setSenddate(Date senddate) {
		this._senddate = senddate;
	}

	public int getAttempts() {
		return _attempts;
	}
	public void setAttempts(int attempts) {
		this._attempts = attempts;
	}

	public int getSent() {
		return _sent;
	}
	public void setSent(int sent) {
		this._sent = sent;
	}

	public String getSender() {
		return _sender;
	}
	public void setSender(String sender) {
		this._sender = sender;
	}

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
