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
package org.entando.entando.plugins.jpmultisite.aps.system.services.content.model;



public class SharedContent {

	public SharedContent() {
	}

	public SharedContent(String contentId, String multisiteCodeSrc, String multisiteCodeTo) {
		this._contentId = contentId;
		this._multisiteCodeSrc = multisiteCodeSrc;
		this._multisiteCodeTo = multisiteCodeTo;
	}

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}

	public String getMultisiteCodeSrc() {
		return _multisiteCodeSrc;
	}
	public void setMultisiteCodeSrc(String multisiteCodeSrc) {
		this._multisiteCodeSrc = multisiteCodeSrc;
	}

	public String getMultisiteCodeTo() {
		return _multisiteCodeTo;
	}
	public void setMultisiteCodeTo(String multisiteCodeTo) {
		this._multisiteCodeTo = multisiteCodeTo;
	}

	@Override
	public String toString() {
		return "SharedContent{" + "_id=" + _id + ", _contentId=" + _contentId + ", _multisiteCodeSrc=" + _multisiteCodeSrc + ", _multisiteCodeTo=" + _multisiteCodeTo + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 43 * hash + (this._contentId != null ? this._contentId.hashCode() : 0);
		hash = 43 * hash + (this._multisiteCodeSrc != null ? this._multisiteCodeSrc.hashCode() : 0);
		hash = 43 * hash + (this._multisiteCodeTo != null ? this._multisiteCodeTo.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SharedContent other = (SharedContent) obj;
		if ((this._contentId == null) ? (other._contentId != null) : !this._contentId.equals(other._contentId)) {
			return false;
		}
		if ((this._multisiteCodeSrc == null) ? (other._multisiteCodeSrc != null) : !this._multisiteCodeSrc.equals(other._multisiteCodeSrc)) {
			return false;
		}
		if ((this._multisiteCodeTo == null) ? (other._multisiteCodeTo != null) : !this._multisiteCodeTo.equals(other._multisiteCodeTo)) {
			return false;
		}
		return true;
	}
	
	private int _id;
	private String _contentId;
	private String _multisiteCodeSrc;
	private String _multisiteCodeTo;

}
