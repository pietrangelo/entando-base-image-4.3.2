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
package com.agiletec.plugins.jpforum.aps.system.services.thread;

import java.util.Date;

public class Post {

	public int getCode() {
		return _code;
	}
	public void setCode(int code) {
		this._code = code;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public Date getPostDate() {
		return _postDate;
	}
	public void setPostDate(Date postDate) {
		this._postDate = postDate;
	}

	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}

	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}

	public int getThreadId() {
		return _threadId;
	}
	public void setThreadId(int threadId) {
		this._threadId = threadId;
	}
	
	private int _code;
	private String _username;
	private Date _postDate;
	private String _title;
	private String _text;
	private int _threadId;
	
}
