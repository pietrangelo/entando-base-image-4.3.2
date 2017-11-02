package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.api;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;

@XmlRootElement(name = "socialPost")
@XmlType(propOrder = {"id", "text", "date", "permalink", "service", "objectid", "provider", "socialid", "username"})
public class JAXBSocialPost {

    public JAXBSocialPost() {
        super();
    }

    public JAXBSocialPost(SocialPost socialPost) {
		this.setId(socialPost.getId());
		this.setText(socialPost.getText());
		this.setDate(socialPost.getDate());
		this.setPermalink(socialPost.getPermalink());
		this.setService(socialPost.getService());
		this.setObjectid(socialPost.getObjectid());
		this.setProvider(socialPost.getProvider());
		this.setSocialid(socialPost.getSocialid());
		this.setUsername(socialPost.getUsername());
    }
    
    public SocialPost getSocialPost() {
    	SocialPost socialPost = new SocialPost();
		socialPost.setId(this.getId());
		socialPost.setText(this.getText());
		socialPost.setDate(this.getDate());
		socialPost.setPermalink(this.getPermalink());
		socialPost.setService(this.getService());
		socialPost.setObjectid(this.getObjectid());
		socialPost.setProvider(this.getProvider());
		socialPost.setSocialid(this.getSocialid());
		socialPost.setUsername(this.getUsername());
    	return socialPost;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "text", required = true)
	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}

	@XmlElement(name = "date", required = true)
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}

	@XmlElement(name = "permalink", required = true)
	public String getPermalink() {
		return _permalink;
	}
	public void setPermalink(String permalink) {
		this._permalink = permalink;
	}

	@XmlElement(name = "service", required = true)
	public String getService() {
		return _service;
	}
	public void setService(String service) {
		this._service = service;
	}

	@XmlElement(name = "objectid", required = true)
	public String getObjectid() {
		return _objectid;
	}
	public void setObjectid(String objectid) {
		this._objectid = objectid;
	}

	@XmlElement(name = "provider", required = true)
	public String getProvider() {
		return _provider;
	}
	public void setProvider(String provider) {
		this._provider = provider;
	}

	@XmlElement(name = "socialid", required = true)
	public String getSocialid() {
		return _socialid;
	}
	public void setSocialid(String socialid) {
		this._socialid = socialid;
	}

	@XmlElement(name = "username", required = true)
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
