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
package com.agiletec.plugins.jpforum.aps.system.services.config;

/**
 * Forum Configuration Class
 *
 */
public class ForumConfig {
	
	public void setPostsPerPage(int postsPerPage) {
		this._postsPerPage = postsPerPage;
	}
	public int getPostsPerPage() {
		return _postsPerPage;
	}

	public void setProfileTypecode(String profileTypecode) {
		this._profileTypecode = profileTypecode;
	}
	public String getProfileTypecode() {
		return _profileTypecode;
	}

	public void setProfileNickAttributeName(String profileNickAttributeName) {
		this._profileNickAttributeName = profileNickAttributeName;
	}
	public String getProfileNickAttributeName() {
		return _profileNickAttributeName;
	}
	
	public void setAttachsPerPost(int attachsPerPost) {
		this._attachsPerPost = attachsPerPost;
	}
	public int getAttachsPerPost() {
		return _attachsPerPost;
	}

	private int _postsPerPage;
	private String _profileTypecode;
	private String _profileNickAttributeName;
	private int _attachsPerPost;
}
