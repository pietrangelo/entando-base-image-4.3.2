package org.entando.entando.plugins.jpsocial.apsadmin.socialpost;

import java.util.Date;

import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.ISocialPostManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author entando
 */
public class SocialPostAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialPostAction.class);

	public String newSocialPost() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newSocialPost", t);

			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String view() {
		try {
			SocialPost socialPost = this.getSocialPostManager().getSocialPost(this.getId());
			if (null == socialPost) {
				this.addActionError(this.getText("error.socialPost.null"));
				return INPUT;
			}
			this.populateForm(socialPost);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			SocialPost socialPost = this.createSocialPost();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getSocialPostManager().addSocialPost(socialPost);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getSocialPostManager().updateSocialPost(socialPost);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			SocialPost socialPost = this.getSocialPostManager().getSocialPost(this.getId());
			if (null == socialPost) {
				this.addActionError(this.getText("error.socialPost.null"));
				return INPUT;
			}
			this.populateForm(socialPost);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getSocialPostManager().deleteSocialPost(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(SocialPost socialPost) throws Throwable {
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
	
	private SocialPost createSocialPost() {
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
	

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
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

	
	protected ISocialPostManager getSocialPostManager() {
		return _socialPostManager;
	}
	public void setSocialPostManager(ISocialPostManager socialPostManager) {
		this._socialPostManager = socialPostManager;
	}
	
	private int _strutsAction;
	private int _id;
	private String _text;
	private Date _date;
	private String _permalink;
	private String _service;
	private String _objectid;
	private String _provider;
	private String _socialid;
	private String _username;
	
	private ISocialPostManager _socialPostManager;
	
}
