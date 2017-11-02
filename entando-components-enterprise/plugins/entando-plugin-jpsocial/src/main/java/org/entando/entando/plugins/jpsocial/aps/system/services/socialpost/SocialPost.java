package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost;

import java.util.Date;

public class SocialPost {

	public SocialPost() {
	}

	public SocialPost(String text, String permalink, String service, String objectid, String provider, String socialid, String username) {
		this._text = text;
		this._permalink = permalink;
		this._service = service;
		this._objectid = objectid;
		this._provider = provider;
		this._socialid = socialid;
		this._username = username;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}

	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}

	public String getPermalink() {
		return _permalink;
	}
	public void setPermalink(String permalink) {
		this._permalink = permalink;
	}

	public String getService() {
		return _service;
	}
	public void setService(String service) {
		this._service = service;
	}

	public String getObjectid() {
		return _objectid;
	}
	public void setObjectid(String objectid) {
		this._objectid = objectid;
	}

	public String getProvider() {
		return _provider;
	}
	public void setProvider(String provider) {
		this._provider = provider;
	}

	public String getSocialid() {
		return _socialid;
	}
	public void setSocialid(String socialid) {
		this._socialid = socialid;
	}

	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	
	private int _id;
	private String _text;
	private Date _date;
	private String _permalink;
	private String _service;
	private String _objectid;
	private String _provider;
	private String _socialid;
	private String _username;

}
