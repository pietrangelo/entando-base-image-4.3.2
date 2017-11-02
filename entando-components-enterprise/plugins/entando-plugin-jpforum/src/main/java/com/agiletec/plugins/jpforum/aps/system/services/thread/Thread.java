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

import java.util.List;

public class Thread {

	public int getCode() {
		return _code;
	}
	public void setCode(int code) {
		this._code = code;
	}

	public String getSectionId() {
		return _sectionId;
	}
	public void setSectionId(String sectionId) {
		this._sectionId = sectionId;
	}

	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public boolean isOpen() {
		return _open;
	}
	public void setOpen(boolean open) {
		this._open = open;
	}

	public boolean isPin() {
		return _pin;
	}
	public void setPin(boolean pin) {
		this._pin = pin;
	}

	public void setPostCodes(List<Integer> postCodes) {
		this._postCodes = postCodes;
	}
	public List<Integer> getPostCodes() {
		return _postCodes;
	}

	public void setPost(Post post) {
		this._post = post;
	}
	public Post getPost() {
		return _post;
	}

	private int _code;
	private String _sectionId;
	private String _username;
	private boolean _open;
	private boolean _pin;
	private Post _post;
	private List<Integer> _postCodes;
}
