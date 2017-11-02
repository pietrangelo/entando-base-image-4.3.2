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
package com.agiletec.plugins.jpforum.aps.system.services.message;

import java.util.Date;

public class Message {

	public int getCode() {
		return _code;
	}
	public void setCode(int code) {
		this._code = code;
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
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	public String getBody() {
		return _body;
	}
	public void setBody(String body) {
		this._body = body;
	}
	
	public String getMessageType() {
		return _messageType;
	}
	public void setMessageType(String messageType) {
		this._messageType = messageType;
	}
	
	public Date getMessageDate() {
		return _messageDate;
	}
	public void setMessageDate(Date messageDate) {
		this._messageDate = messageDate;
	}
	
	public boolean isToRead() {
		return _toRead;
	}
	public void setToRead(boolean toRead) {
		this._toRead = toRead;
	}
	
	private int _code;
	private String _sender;
	private String _recipient;
	private String _title;
	private String _body;
	private String _messageType;
	private Date _messageDate;
	private boolean _toRead;

	public static final String TYPE_MESSAGE = "MESSAGE";
	public static final String TYPE_WARN = "WARN";
}
