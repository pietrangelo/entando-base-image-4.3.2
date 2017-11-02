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
package com.agiletec.plugins.jpforum.aps.system.services.thread.event;

import java.util.List;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;

public class PostOperationEvent extends ApsEvent {


	public void notify(IManager srv) {
		((PostOperationObserver) srv).updateFromPostOperation(this);
	}
	
	public Class getObserverInterface() {
		return PostOperationObserver.class;
	}
	
	public Post getPost() {
		return _post;
	}

	public void setPost(Post post) {
		this._post = post;
	}
	

	public int getOperationCode() {
		return _operationCode;
	}

	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public void setPostCodes(List<Integer> postCodes) {
		this._postCodes = postCodes;
	}

	public List<Integer> getPostCodes() {
		return _postCodes;
	}

	private Post _post;
	private List<Integer> _postCodes;
	
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

	public static final int REMOVE_THREAD_OPERATION_CODE = 4;
	public static final int REFRESH_SECTIONS_TREE = 5;
}
