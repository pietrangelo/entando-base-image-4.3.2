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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.resource;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;

public class ResourceDataBeanObj implements Serializable, ResourceDataBean {

	public ResourceDataBeanObj(ResourceDataBean bean) throws Throwable {
		InputStream inputStream = bean.getInputStream();
		this.setCategories(bean.getCategories());
		this.setDescr(bean.getDescr());
		this.setFileName(bean.getFileName());
		this.setFileSize(bean.getFileSize());
		this.setMainGroup(bean.getMainGroup());
		this.setMimeType(bean.getMimeType());
		this.setResourceType(bean.getResourceType());
		this.setFile(bean.getFile());
		this.setResourceId(bean.getResourceId());
		
		BufferedInputStream buffer = new BufferedInputStream(inputStream);
		DataInputStream dataInputStream = new DataInputStream(buffer);
		ByteArrayOutputStream temporaryBuffer = new ByteArrayOutputStream( );
		_length = copy(dataInputStream, new DataOutputStream(temporaryBuffer));
		//this.setInputStream(new DataInputStream(new ByteArrayInputStream(temporaryBuffer.toByteArray( ))));
		this.setByteArray(temporaryBuffer.toByteArray());
	}

	private int copy(InputStream source, OutputStream destination) throws IOException {
		int nextByte;
		int numberOfBytesCopied = 0;
		while(-1!= (nextByte = source.read( ))) {
			destination.write(nextByte);
			numberOfBytesCopied++;
		}
		destination.flush( );
		return numberOfBytesCopied;
	}

	
	public void setLength(int length) {
		this._length = length;
	}
	public int getLength() {
		return _length;
	}
	
	@Override
	public String getResourceType() {
		return _resourceType;
	}
	public void setResourceType(String resourceType) {
		this._resourceType = resourceType;
	}
	
	@Override
	public List getCategories() {
		return _categories;
	}
	public void setCategories(List categories) {
		this._categories = categories;
	}

	public void setDescr(String descr) {
		this._descr = descr;
	}
	@Override
	public String getDescr() {
		return _descr;
	}

	public void setFileName(String fileName) {
		this._fileName = fileName;
	}
	@Override
	public String getFileName() {
		return _fileName;
	}

	public void setFileSize(int fileSize) {
		this._fileSize = fileSize;
	}
	@Override
	public int getFileSize() {
		return _fileSize;
	}

	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	@Override
	public String getMainGroup() {
		return _mainGroup;
	}

	public void setMimeType(String mimeType) {
		this._mimeType = mimeType;
	}
	@Override
	public String getMimeType() {
		return _mimeType;
	}
	
	public void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}
	@Override
	public InputStream getInputStream() {
		return _inputStream;
	}

	public void setByteArray(byte[] byteArray) {
		this._byteArray = byteArray;
	}
	public byte[] getByteArray() {
		return _byteArray;
	}

	@Override
	public String getResourceId() {
		return _resourceId;
	}
	public void setResourceId(String resourceId) {
		this._resourceId = resourceId;
	}

	@Override
	public File getFile() {
		return _file;
	}
	public void setFile(File file) {
		this._file = file;
	}

	private int _length;
	private transient InputStream _inputStream;
	private List _categories;
	private String _descr;
	private String _fileName;
	private int _fileSize;
	private String _mainGroup;
	private String _mimeType;
	private String _resourceType;
	
	private byte[] _byteArray;
	
	private String _resourceId;
	private transient File _file;
	
}
