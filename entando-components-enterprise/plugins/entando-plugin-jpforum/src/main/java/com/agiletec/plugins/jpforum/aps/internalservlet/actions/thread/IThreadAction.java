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
package com.agiletec.plugins.jpforum.aps.internalservlet.actions.thread;

import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;

public interface IThreadAction {

	public String viewThread();
	
	public String newThread();
	
	public Thread getCurrentThread();
	
	public String changeStatus();

	public String changePin();

	public String deleteThread();

	public String trashThread();
}
