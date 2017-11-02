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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.form.model;

import java.io.File;

/**
 * utility class to manage files associated with the form
 *
 */
public class FormUpload {

	public static FormUpload create(File file, String docFileName, String docContentType) {
		if (null == file) return null;
		FormUpload formUpload = null;
		formUpload = new FormUpload();
		formUpload.setFile(file);
		formUpload.setContentType(docContentType);
		formUpload.setFileName(docFileName);
		return formUpload;
	}

	public File getFile() {
		return _file;
	}
	public void setFile(File file) {
		this._file = file;
	}

	public String getFileName() {
		return _fileName;
	}
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}

	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}

	private File _file;
	private String _fileName;
	private String _contentType;
}
