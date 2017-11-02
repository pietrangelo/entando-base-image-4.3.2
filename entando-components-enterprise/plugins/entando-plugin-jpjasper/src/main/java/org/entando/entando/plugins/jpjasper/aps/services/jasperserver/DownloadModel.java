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
package org.entando.entando.plugins.jpjasper.aps.services.jasperserver;

public class DownloadModel {
	
	public DownloadModel(String extension, String mimeType) {
		super();
		this._extension = extension;
		this._mimeType = mimeType;
	}
	
	public String getExtension() {
		return _extension;
	}
	public void setExtension(String extension) {
		this._extension = extension;
	}
	public String getMimeType() {
		return _mimeType;
	}
	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}

	private String _extension;
	private String _mimeType;
}
