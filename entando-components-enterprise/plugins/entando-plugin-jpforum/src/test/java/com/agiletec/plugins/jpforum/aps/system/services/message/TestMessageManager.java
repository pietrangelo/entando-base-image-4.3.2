/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
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

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpforum.aps.system.services.message.Message;

import com.agiletec.plugins.jpforum.aps.JpforumBaseTestCase;

public class TestMessageManager extends JpforumBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetMessage() throws Throwable {
		Message message = _messageManager.getMessage(1);
		assertNotNull(message);
		assertEquals(1, message.getCode());
		assertEquals("admin", message.getSender());
		assertEquals("mainEditor", message.getRecipient());
		assertEquals("test1", message.getTitle());
		assertEquals("test message body", message.getBody());
		assertTrue(message.isToRead());
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.JANUARY, 8, 12, 00);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		assertEquals(cal.getTime(), message.getMessageDate());
		assertEquals(Message.TYPE_MESSAGE, message.getMessageType());
	}

	public void testGetIncomingMessages() throws Throwable {
		Map<String, List<Integer>> incomingMessages = _messageManager.getIncomingMessages("mainEditor");
		assertNotNull(incomingMessages);
		assertEquals(2, incomingMessages.size());
		assertTrue(incomingMessages.containsKey(Message.TYPE_WARN));
		assertTrue(incomingMessages.containsKey(Message.TYPE_MESSAGE));
		
		List<Integer> warns = incomingMessages.get(Message.TYPE_WARN);
		assertEquals(2, warns.size());
		assertTrue(warns.contains(3));
		assertTrue(warns.contains(4));
		assertEquals(new Integer(3), warns.get(0));

		List<Integer> messages = incomingMessages.get(Message.TYPE_MESSAGE);
		assertEquals(1, messages.size());
		assertTrue(messages.contains(1));
	}
	
	
	public void testAddMessage() throws Throwable {
		String recipient = "pageManager";
		Message message = this.createTestMessage(recipient);
		this._messageManager.addMessage(message);
		Map<String, List<Integer>> incomingMessages = _messageManager.getIncomingMessages(recipient);
		assertEquals(1, incomingMessages.size());
		List<Integer> warns = incomingMessages.get(Message.TYPE_WARN);
		List<Integer> messages = incomingMessages.get(Message.TYPE_MESSAGE);
		assertEquals(1, warns.size());
		assertNull(messages);
		//VERIFICA
		int messageCode = warns.get(0);
		Message addedMessage = _messageManager.getMessage(messageCode);
		assertNotNull(addedMessage);
		
		assertEquals("admin", addedMessage.getSender());
		assertEquals(recipient, addedMessage.getRecipient());
		assertEquals("deleteMe title", addedMessage.getTitle());
		assertEquals("deleteMe body", addedMessage.getBody());
		assertEquals(Message.TYPE_WARN, addedMessage.getMessageType());
		assertTrue(addedMessage.isToRead());
		
		Map<String, Integer> newMessages = _messageManager.getNewMessages(recipient);
		assertEquals(new Integer(1), newMessages.get(Message.TYPE_WARN));
		_messageManager.readMessage(addedMessage.getCode());
		newMessages = _messageManager.getNewMessages(recipient);
		assertTrue(newMessages.isEmpty());
		
		
		//PULIZIA
		_messageManager.deleteMessage(messageCode);
		//CONTROLLO
		incomingMessages = _messageManager.getIncomingMessages(recipient);
		assertEquals(0, incomingMessages.size());
		warns = incomingMessages.get(Message.TYPE_WARN);
		messages = incomingMessages.get(Message.TYPE_MESSAGE);
		assertNull(warns);
		assertNull(messages);
	}
	
	private Message createTestMessage(String recipient) {
		Message message = new Message();
		message.setSender("admin");
		message.setRecipient(recipient);
		message.setBody("deleteMe body");
		message.setTitle("deleteMe title");
		message.setMessageType(Message.TYPE_WARN);
		return message;
	}

	public void testReadMessage() throws Throwable {
		String recipient = "pageManager";
		Message message = this.createTestMessage(recipient);
		this._messageManager.addMessage(message);
		Map<String, List<Integer>> incomingMessages = _messageManager.getIncomingMessages(recipient);
		assertEquals(1, incomingMessages.size());
		List<Integer> warns = incomingMessages.get(Message.TYPE_WARN);
		List<Integer> messages = incomingMessages.get(Message.TYPE_MESSAGE);
		assertEquals(1, warns.size());
		assertNull(messages);
		//VERIFICA
		int messageCode = warns.get(0);
		Message addedMessage = _messageManager.readMessage(messageCode);
		
		assertNotNull(addedMessage);
		
		assertEquals("admin", addedMessage.getSender());
		assertEquals(recipient, addedMessage.getRecipient());
		assertEquals("deleteMe title", addedMessage.getTitle());
		assertEquals("deleteMe body", addedMessage.getBody());
		assertEquals(Message.TYPE_WARN, addedMessage.getMessageType());
		assertFalse(addedMessage.isToRead());
		
		//PULIZIA
		_messageManager.deleteMessage(messageCode);
		//CONTROLLO
		incomingMessages = _messageManager.getIncomingMessages(recipient);
		assertEquals(0, incomingMessages.size());
		warns = incomingMessages.get(Message.TYPE_WARN);
		messages = incomingMessages.get(Message.TYPE_MESSAGE);
		assertNull(warns);
		assertNull(messages);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Map<String, List<Integer>> incomingMessages = _messageManager.getIncomingMessages("pageManager");
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
}
