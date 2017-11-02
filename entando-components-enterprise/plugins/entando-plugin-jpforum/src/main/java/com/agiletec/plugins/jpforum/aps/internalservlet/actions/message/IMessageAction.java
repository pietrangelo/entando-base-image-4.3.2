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

/**
 * Interface for the action that manages private massages
 */
public interface IMessageAction {

	/**
	 * Entry point for creating a new message
	 */
	public String newMessage();

	/**
	 * Entry point for creating a new warn
	 */
	public String newWarn();

	/**
	 * Saves a message or a warn
	 */
	public String saveMessage();

	/**
	 * View a message and updates the read status of the message to false
	 */
	public String readMessage();

	/**
	 * Entry point for deleting a message
	 */
	public String removeMessage();

	/**
	 * Permanently deletes a message
	 */
	public String doRemoveMessage();

}
