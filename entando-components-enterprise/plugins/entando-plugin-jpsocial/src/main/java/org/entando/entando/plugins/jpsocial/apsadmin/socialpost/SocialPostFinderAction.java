package org.entando.entando.plugins.jpsocial.apsadmin.socialpost;

import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.ISocialPostManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author scaffolder
 */
public class SocialPostFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialPostFinderAction.class);

	public List<Integer> getSocialPostsId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getText()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("text"), this.getText(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getDateStart() || null != this.getDateEnd()) {
				Date startDate = this.getDateStart();
				Date endDate = this.getDateEnd();
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("date"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			if (null != this.getPermalink()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("permalink"), this.getPermalink(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getService()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("service"), this.getService(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getObjectid()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("objectid"), this.getObjectid(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getProvider()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("provider"), this.getProvider(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getSocialid()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("socialid"), this.getSocialid(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getUser()) {
				//TODO add a constant into your ISocialPostManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("user"), this.getUser(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<Integer> socialPosts = this.getSocialPostManager().searchSocialPosts(filters);
			return socialPosts;
		} catch (Throwable t) {
			_logger.error("Error getting socialPosts list", t);
			throw new RuntimeException("Error getting socialPosts list", t);
		}
	}

	protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public SocialPost getSocialPost(int id) {
		SocialPost socialPost = null;
		try {
			socialPost = this.getSocialPostManager().getSocialPost(id);
		} catch (Throwable t) {
			_logger.error("Error getting socialPost with id {}", id, t);
			throw new RuntimeException("Error getting socialPost with id " + id, t);
		}
		return socialPost;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}


	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}


	public Date getDateStart() {
		return _dateStart;
	}
	public void setDateStart(Date dateStart) {
		this._dateStart = dateStart;
	}


	public Date getDateEnd() {
		return _dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this._dateEnd = dateEnd;
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


	public String getUser() {
		return _user;
	}
	public void setUser(String user) {
		this._user = user;
	}


	protected ISocialPostManager getSocialPostManager() {
		return _socialPostManager;
	}
	public void setSocialPostManager(ISocialPostManager socialPostManager) {
		this._socialPostManager = socialPostManager;
	}
	
	private Integer _id;
	private String _text;
	private Date _dateStart;
	private Date _dateEnd;
	private String _permalink;
	private String _service;
	private String _objectid;
	private String _provider;
	private String _socialid;
	private String _user;
	private ISocialPostManager _socialPostManager;
}
