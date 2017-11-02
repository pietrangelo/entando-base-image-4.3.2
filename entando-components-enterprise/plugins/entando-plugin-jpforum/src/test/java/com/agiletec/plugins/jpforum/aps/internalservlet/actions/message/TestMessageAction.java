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

import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpforum.apsadmin.JpforumApsAdminBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.message.MessageAction;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpforum.aps.system.services.message.Message;
import com.opensymphony.xwork2.Action;

public class TestMessageAction extends JpforumApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNewMessage_1() throws Throwable {
		String recipient = "mainEditor";
		String result = this.executeNewMessage(recipient, Message.TYPE_MESSAGE);
		assertEquals(Action.SUCCESS, result);
	}

	public void testNewMessage_2() throws Throwable {
		String recipient = "";
		String result = this.executeNewMessage(recipient, Message.TYPE_MESSAGE);
		assertEquals(Action.INPUT, result);
		MessageAction action = (MessageAction) this.getAction();
		Map<String, List<String>> fieldErrors = action.getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("recipient").size());
	}
	
	public void testSaveMessage() throws Throwable {
		String recipient = FORUM_USER;
		String result = this.executeSaveMessage(recipient, Message.TYPE_MESSAGE);
		assertEquals(Action.SUCCESS, result);
		Map<String, List<Integer>> messages =  _messageManager.getIncomingMessages(recipient);
		assertEquals(1, messages.get(Message.TYPE_MESSAGE).size());
	}
	
	public void testReadMessage() throws Throwable {
		String recipient = FORUM_SUPERUSER;
		String sender = FORUM_USER;
		Message warnMessage = this.createTestMessage(sender, recipient);
		this._messageManager.addMessage(warnMessage);
		Message message = this._messageManager.getMessage(warnMessage.getCode());
		assertTrue(message.isToRead());
		String result = this.executeReadMessage(recipient, message.getCode());
		assertEquals(Action.SUCCESS, result);
		message = this._messageManager.getMessage(warnMessage.getCode());
		assertFalse(message.isToRead());
	}

	private Message createTestMessage(String sender, String recipient) {
		Message message = new Message();
		message.setSender(sender);
		message.setRecipient(recipient);
		message.setBody("deleteMe body");
		message.setTitle("deleteMe title");
		message.setMessageType(Message.TYPE_WARN);
		return message;
	}
	
	private String executeNewMessage(String recipient, String messageType) throws Throwable {
		this.setUserOnSession(FORUM_SUPERUSER);
		this.initAction(NS, "newMessage");
		this.addParameter("sender", FORUM_SUPERUSER);
		this.addParameter("recipient", recipient);
		this.addParameter("messageType", messageType);
		return this.executeAction();
	}

	private String executeSaveMessage(String recipient, String messageType) throws Throwable {
		this.setUserOnSession(FORUM_SUPERUSER);
		this.initAction(NS, "saveMessage");
		this.setToken();
		this.addParameter("sender", FORUM_SUPERUSER);
		this.addParameter("recipient", recipient);
		this.addParameter("title", "title_deleteme");
		this.addParameter("body", "body_deleteme");
		this.addParameter("messageType", messageType);
		return this.executeAction();
	}

	private String executeReadMessage(String recipient, int code) throws Throwable {
		this.setUserOnSession(recipient);
		this.initAction(NS, "readMessage");
		this.addParameter("code", code);
		return this.executeAction();
	}

	@Override
	protected void tearDown() throws Exception {
		this.deleteAllMessages(FORUM_USER);
		this.deleteAllMessages(FORUM_SUPERUSER);
		super.tearDown();
	}

	private void deleteAllMessages(String username) throws ApsSystemException {
		Map<String, List<Integer>> incomingMessages = _messageManager.getIncomingMessages(username);
		if (null != incomingMessages) {
			List<Integer> warns = incomingMessages.get(Message.TYPE_WARN);
			List<Integer> messages = incomingMessages.get(Message.TYPE_MESSAGE);
			if (null != warns) {
				for (int i = 0; i < warns.size(); i++) {
					_messageManager.deleteMessage(warns.get(i));
				}
			}
			if (null != messages) {
				for (int i = 0; i < messages.size(); i++) {
					_messageManager.deleteMessage(messages.get(i));
				}
			}			
		}
	}

	private void init() {
		_messageManager = (IMessageManager) this.getService(JpforumSystemConstants.MESSAGE_MANAGER);
	}

	private IMessageManager _messageManager;
	public static final String NS = "/do/jpforum/Front/Message";
}
