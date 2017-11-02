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
package org.entando.entando.plugins.jpsocial.apsadmin.social.helper;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.InputStream;

/**
 * This class describes a single post object composed by a provider id and the
 * text to update to a social network
 *
 * @author entando
 */
public class PostAttribute {

	/**
	 * Create a new post attribute for the delievry queue
	 *
	 * @param queue
	 * @param attributeName
	 * @param lang
	 * @param unique when true prevents further references to the same attribute
	 * in the queue
	 */
	public PostAttribute(String queue, String attributeName, String lang, boolean unique) {
		this._queue = queue;
		this._name = attributeName;
		this._lang = lang;
		// create unique ID
		this._id = this._queue.toLowerCase() + "_" + this._name.toLowerCase();
		if (!unique) {
			this._id = this._id.concat("_" + this._lang.toLowerCase());
		}
		// these are always true upon creation
		this._appendLink = false;
		this._ignore = false;
		this._submitted = false;
		this._path = null;
		this._text = null;
		this._value = null;
		this._category = null;
	}

	public void setSubmitted(boolean submitted) throws ApsSystemException {
		// alway execute desired operation...
		this._submitted = submitted;
		// ...but notify strange behaviour
		if (this._ignore && submitted) {
			throw new ApsSystemException("Someone declared an ignored attribute as submitted!");
		}
	}

	/**
	 * Get the attribute name this post takes the content from
	 *
	 * @return
	 */
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		this._name = name;
	}

	/**
	 * Get the languge to use for posting
	 *
	 * @return
	 */
	public String getLang() {
		return _lang;
	}

	public void setLang(String lang) {
		this._lang = lang;
	}

	/**
	 * Get the social network associated with this attribute
	 *
	 * @return
	 */
	public String getQueue() {
		return _queue;
	}

	public void setQueue(String queue) {
		this._queue = queue;
	}

	/**
	 * @Deprecated don't use
	 * @return
	 * @deprecated
	 */
	@Deprecated
	public Object getValue() {
		return _value;
	}

	/**
	 * @Deprecated don't use
	 * @param value
	 * @deprecated
	 */
	@Deprecated
	public void setValue(Object value) {
		this._value = value;
	}

	/**
	 * Get the PLAIN text to be posted
	 *
	 * @return
	 */
	public String getText() {
		return _text;
	}

	public void setText(String text) {
		this._text = text;
	}

	/**
	 * Get the id of this element to post; it is a combination of the queue this
	 * attribute belongs to, the name of the content attribute field and finally
	 * the language code.
	 *
	 * @return
	 */
	public String getId() {
		return _id;
	}

	public boolean isAppendLink() {
		return _appendLink;
	}

	public void setAppendLink(boolean appendLink) {
		this._appendLink = appendLink;
	}

	public int getType() {
		return _type;
	}

	public void setType(int type) {
		this._type = type;
	}

	public String getPath() {
		return _path;
	}

	public void setPath(String path) {
		this._path = path;
	}

	public boolean isIgnore() {
		return _ignore;
	}

	public void setIgnore(boolean ignore) {
		this._ignore = ignore;
	}

	public boolean isSubmitted() {
		return _submitted;
	}

	public String getCategory() {
		return _category;
	}

	public void setCategory(String category) {
		this._category = category;
	}

	public InputStream getStream() {
		return _stream;
	}

	public void setStream(InputStream stream) {
		this._stream = stream;
	}
	private String _queue;
	// The name of the attribute 
	private String _name;
	// language
	private String _lang;
	// generic attribute object
	@Deprecated
	private Object _value;
	// is text or image?
	// the final text to post
	private String _text;
	// unique id of this attribute
	private String _id;
	// decide whether to append the content permalink
	private boolean _appendLink;
	// video and images absolute path
	private String _path;
	private boolean _ignore;
	private boolean _submitted;
	private int _type;
	private String _category;
	private InputStream _stream;
}
