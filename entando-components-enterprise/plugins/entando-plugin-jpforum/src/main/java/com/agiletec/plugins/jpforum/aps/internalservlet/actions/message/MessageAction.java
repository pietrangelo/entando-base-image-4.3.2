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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.message;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.services.message.Message;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;

public class MessageAction extends AbstractForumAction implements IMessageAction {

	private static final Logger _logger =  LoggerFactory.getLogger(MessageAction.class);

	@Override
	public String[] getBreadCrumbs() {
		return null;
	}

	@Override
	public Section getCurrentSection() {
		return null;
	}

	@Override
	public String newMessage() {
		try {
			String checkRecipient = this.checkRecipient();
			if (null != checkRecipient) return checkRecipient;
			this.setMessageType(Message.TYPE_MESSAGE);
			this.setSender(this.getCurrentUser().getUsername());
			if (0 != this.getCode()) {
				this.setRefsMessage(this.getMessageManager().getMessage(this.getCode()));
			}
			String quote = super.getRequest().getParameter("quote");
			if (null != quote && quote.equals("true")) {
				if (null != this.getRefsMessage() && this.getRefsMessage().getRecipient().equals(this.getCurrentUser().getUsername())) {
					this.setTitle("RE: " + this.getRefsMessage().getTitle());
					this.setBody("> " + this.getRefsMessage().getBody().replaceAll("\r\n", "\r\n> "));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in newMessage", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String newWarn() {
		try {
			String checkRecipient = this.checkRecipient();
			if (null != checkRecipient) return checkRecipient;
			this.setMessageType(Message.TYPE_WARN);
			this.setSender(this.getCurrentUser().getUsername());
			String quote = super.getRequest().getParameter("quote");
			if (null != quote && quote.equals("true")) {
				Message message = this.getMessageManager().getMessage(this.getCode());
				if (null != message && message.getRecipient().equals(this.getCurrentUser().getUsername())) {
					this.setTitle("RE: " + message.getTitle());
					this.setBody("> " + message.getBody().replaceAll("\r\n", "\r\n> "));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in newMessage", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String saveMessage() {
		try {
			Message message = this.createMessage();
			this.getMessageManager().addMessage(message);
		} catch (Throwable t) {
			_logger.error("error in sendMessage", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String readMessage() {
		try {
			Message message = this.getMessageManager().readMessage(this.getCode());
			if (null == message) {
				this.addActionError(this.getText("jpforum.error.message.null"));
				return INPUT;
			} else if (!message.getRecipient().equals(this.getCurrentUser().getUsername())) {
				this.addActionError(this.getText("jpforum.error.message.notyours"));
				return INPUT;
			}
			this.setSender(message.getSender());
			this.setRecipient(message.getRecipient());
			this.setTitle(message.getTitle());
			this.setBody(message.getBody());
			this.setMessageType(message.getMessageType());
			this.setMessageDate(message.getMessageDate());
		} catch (Throwable t) {
			_logger.error("error in readMessage", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private String checkRecipient() {
		//recipient deve avere il ruolo forum user.
		String retval = null;
		try {

			if (null == this.getRecipient() || this.getRecipient().trim().length() == 0) {
				this.addFieldError("recipient", this.getText("jpforum.error.nullRecipientParam"));
				return INPUT;
			}
			if (this.getRecipient().equals(this.getCurrentUser().getUsername())) {
				this.addFieldError("recipient", this.getText("jpforum.error.wrongRecipient"));
				return INPUT;
			}
//			UserDetails theUser = this.getUserManager().getUser(this.getRecipient());
//			UserDetails recipient = this.getAuthenticationProviderManager().getUser(theUser.getUsername(), theUser.getPassword());
//			if (null == recipient) {
//				this.addFieldError("recipient", this.getText("jpforum.error.nullRecipient"));
//				return INPUT;
//			}
//			boolean isAuthOnPermission = this.getAuthorizationManager().isAuthOnPermission(recipient, JpforumSystemConstants.PERMISSION_FORUM_USER);
//			if (!isAuthOnPermission) {
//				this.addFieldError("recipient", this.getText("jpforum.error.recipient.notForumUser"));
//				return INPUT;
//			}
		} catch (Throwable t) {
			_logger.error("error in checkRecipient", t);
			throw new RuntimeException("errore in controllo recipient");
		}
		return retval;
	}

	@Override
	public String removeMessage() {
		try {
			Message message = this.getMessageManager().readMessage(this.getCode());
			if (null == message) {
				this.addActionError(this.getText("jpforum.error.message.null"));
				return INPUT;
			} else if (!message.getRecipient().equals(this.getCurrentUser().getUsername())) {
				this.addActionError(this.getText("jpforum.error.message.notyours"));
				return INPUT;
			}
			this.setSender(message.getSender());
			this.setRecipient(message.getRecipient());
			this.setTitle(message.getTitle());
			this.setBody(message.getBody());
			this.setMessageType(message.getMessageType());
			this.setMessageDate(message.getMessageDate());
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in removeMessage", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String doRemoveMessage() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getMessageManager().deleteMessage(this.getCode());
			}
		} catch (Throwable t) {
			_logger.error("error in doRemoveMessage", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private Message createMessage() {
		Message message = new Message();
		message.setBody(this.getBody());
		message.setMessageDate(new Date());
		message.setMessageType(this.getMessageType());
		message.setRecipient(this.getRecipient());
		message.setSender(this.getSender());
		message.setTitle(this.getTitle());
		return message;
	}

	public void setCode(int code) {
		this._code = code;
	}
	public int getCode() {
		return _code;
	}

	public void setSender(String sender) {
		this._sender = sender;
	}
	public String getSender() {
		return _sender;
	}

	public void setRecipient(String recipient) {
		this._recipient = recipient;
	}
	public String getRecipient() {
		return _recipient;
	}

	public void setTitle(String title) {
		this._title = title;
	}
	public String getTitle() {
		return _title;
	}

	public void setBody(String body) {
		this._body = body;
	}
	public String getBody() {
		return _body;
	}

	public void setMessageType(String messageType) {
		this._messageType = messageType;
	}
	public String getMessageType() {
		return _messageType;
	}

	public void setMessageDate(Date messageDate) {
		this._messageDate = messageDate;
	}
	public Date getMessageDate() {
		return _messageDate;
	}

	public void setAuthenticationProviderManager(IAuthenticationProviderManager authenticationProviderManager) {
		this._authenticationProviderManager = authenticationProviderManager;
	}
	protected IAuthenticationProviderManager getAuthenticationProviderManager() {
		return _authenticationProviderManager;
	}

	public void setRefsMessage(Message refsMessage) {
		this._refsMessage = refsMessage;
	}
	public Message getRefsMessage() {
		return _refsMessage;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}

	private int _code;
	private String _sender;
	private String _recipient;
	private String _title;
	private String _body;
	private String _messageType;
	private Date _messageDate;
	private IAuthenticationProviderManager _authenticationProviderManager;
	private Message _refsMessage;
	private int _strutsAction;

}
