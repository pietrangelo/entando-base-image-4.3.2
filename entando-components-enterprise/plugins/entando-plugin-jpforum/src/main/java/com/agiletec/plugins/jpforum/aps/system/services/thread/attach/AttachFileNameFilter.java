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
package com.agiletec.plugins.jpforum.aps.system.services.thread.attach;

import java.io.File;
import java.io.FilenameFilter;

public class AttachFileNameFilter implements FilenameFilter {

	public AttachFileNameFilter() {}

	
	public AttachFileNameFilter(int postCode) {
		_postCode = postCode;
	}

	public boolean accept(File file, String name) {
		String start = _postCode + "_";
		return name.startsWith(start);
	}


	public int getPostCode() {
		return _postCode;
	}

	public void setPostCode(int postCode) {
		this._postCode = postCode;
	}

	private int _postCode;
}
