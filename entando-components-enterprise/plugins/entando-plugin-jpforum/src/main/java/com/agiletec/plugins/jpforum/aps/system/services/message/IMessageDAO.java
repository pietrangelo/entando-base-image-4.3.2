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

import java.util.List;
import java.util.Map;

public interface IMessageDAO {

	public void insertMessage(Message message);

	public void removeMessage(int code);

	public Message loadMessage(int code);

	public Message readMessage(int code);

	public Map<String, List<Integer>> loadIncomingMessages(String recipient);

	public Map<String, Integer> loadNewMessages(String recipient);

	public void removeMessages(String username);

}
