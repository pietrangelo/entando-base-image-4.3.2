/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpaltofw.aps.system.service.client;

/**
 * @author E.Santoboni
 */
public class GokibiInstance {
	
	@Override
	public GokibiInstance clone() {
		GokibiInstance clone = new GokibiInstance();
		clone.setBaseUrl(this.getBaseUrl());
		clone.setId(this.getId());
		clone.setPid(this.getPid());
		return clone;
	}
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
	}
	
	public String getBaseUrl() {
		return _baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this._baseUrl = baseUrl;
	}
	
	public String getPid() {
		return _pid;
	}
	public void setPid(String pid) {
		this._pid = pid;
	}
	
	private String _id;
	private String _baseUrl;
	private String _pid;
	
}
