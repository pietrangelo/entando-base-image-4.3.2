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

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Attach {

	public String getName() {
		String name = getFileName();
		String regExpPattern = "\\d*_(.*)";
		Pattern pattern = Pattern.compile(regExpPattern);
		Matcher codeMatcher = pattern.matcher("");
		codeMatcher.reset(getFileName());
		if (codeMatcher.find()) {
			name = codeMatcher.group(1);
		}
		return name;
	}
	
	public int getPostCodeFromFileName() {
		String code = getFileName();
		String regExpPattern = "(\\d*)_(.*)";
		Pattern pattern = Pattern.compile(regExpPattern);
		Matcher codeMatcher = pattern.matcher("");
		codeMatcher.reset(getFileName());
		if (codeMatcher.find()) {
			code = codeMatcher.group(1);
		}
		return new Integer(code).intValue();
	}

	public InputStream getInputStream() {
		return _inputStream;
	}
	
	public void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}
	
	public String getFileName() {
		return _fileName;
	}
	
	public void setFileName(String fileName) {
		this._fileName = fileName;
	}
	
	public long getFileSize() {
		return _fileSize;
	}
	
	public void setFileSize(long fileSize) {
		this._fileSize = fileSize;
	}
	
	public String getUserName() {
		return _userName;
	}
	
	public void setUserName(String userName) {
		this._userName = userName;
	}
	
	public int getPostCode() {
		return _postCode;
	}
	
	public void setPostCode(int postCode) {
		this._postCode = postCode;
	}
	
	private String _fileName;
	private String _userName;
	private long _fileSize;
	private InputStream _inputStream;
	private int _postCode;
	
}
