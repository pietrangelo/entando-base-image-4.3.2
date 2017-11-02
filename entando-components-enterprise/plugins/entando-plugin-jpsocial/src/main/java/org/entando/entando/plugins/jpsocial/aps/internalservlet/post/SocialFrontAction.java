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
package org.entando.entando.plugins.jpsocial.aps.internalservlet.post;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.restfb.FacebookClient;

/**
 *
 * @author entando
 */
public class SocialFrontAction extends AbstractSocialFrontAction implements ISocialFrontAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialFrontAction.class);
	
	@Override
	public void validate() {
		//super.validate();
		HttpServletRequest request = this.getRequest();
		Map parameters = request.getParameterMap();

		String facebookText = this.getFacebookText();
		String queue = this.getQueue();
		if (null != facebookText) {

			// Facebook
			if (queue.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
				if ("".equals(facebookText.trim())) {
					addActionError(this.getText("jpsocial.message.nothingToPost"));
				}
			}
		}
		String tweet = this.getTweet();
		// Twitter
		if (null != tweet) {
			if (queue.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
				if (!JpSocialSystemUtils.isTweetValid(tweet)) {
					if ("".equals(tweet.trim())) {
						addActionError(this.getText("jpsocial.message.nothingToPost"));
					} else {
						addActionError(this.getText("jpsocial.message.tweetLenghtInvalid"));
					}
				}
			}
		}
	}

	@Override
	public String tweetStart() {
		HttpServletRequest request = this.getRequest();
		Map parameters = request.getParameterMap();
		return SUCCESS;
	}

	@Override
	public String facebookStart() {
		HttpServletRequest request = this.getRequest();
		Map parameters = request.getParameterMap();
		return SUCCESS;
	}

	@Override
	public String tweet() {
		HttpServletRequest request = this.getRequest();
		Map parameters = request.getParameterMap();
		try {
			loadSocialSessionCredentials();
			if (null != this.getCredentialsTwitter()) {
				String accessToken = getCredentialsTwitter().getAccessToken();
				String tokenSecret = getCredentialsTwitter().getTokenSecret();

				String id = this.getTwitterManager().tweet(this.getTweet(), accessToken, tokenSecret);
				this.createAndAddSocialPost(id, this.getProvider(), this.getTweet(), this.getCurrentUser().getUsername());
				addActionMessage(this.getText("jpsocial.message.front.twitterSubmitted"));
			} else {
				addActionError(this.getText("jpsocial.message.mustLogin"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in post_tweet", t);
			addActionError(this.getText("jpsocial.message.failure"));
//      TODO REDIR A PAGINA DI ERRORE
			return INPUT;
		}
		return SUCCESS;
	}

	@Override
	public String postFacebook() {
		_logger.debug("posting on facebook...");
		HttpServletRequest request = this.getRequest();
		Map parameters = request.getParameterMap();
		String id = null;
		String facebookText = "";
		try {

			loadSocialSessionCredentials();
			if (null != getCredentialsFacebook()) {
				FacebookClient client = this.getCredentialsFacebook().getClient();

				// decide whether to send an image or the text only
				File image = this.getImage();
				facebookText = this.getFacebookText();
				if (null != image) {
					String path = image.getAbsoluteFile().getAbsolutePath();

					id = this.getFacebookManager().publishImage(client, path, facebookText);
					addActionMessage(this.getText("jpsocial.message.front.facebookSubmitted"));
				} else {
					// message only!
					id = this.getFacebookManager().publishMessage(client, facebookText);
				}
			} else {
				addActionError(this.getText("jpsocial.message.mustLogin"));
				return INPUT;
			}
			if (null == id || "".equals(id.trim())) {
				addActionError(this.getText("jpsocial.message.mustLogin"));
				return INPUT;
			}
			if(StringUtils.isBlank(this.getFacebookText()) && null != this.getImage()){
				facebookText = "image";
			}
			createAndAddSocialPost(id, this.getProvider(), facebookText, this.getCurrentUser().getUsername());
		} catch (Throwable t) {
			addActionError(this.getText("jpsocial.message.failure"));
			return INPUT;
		}
		return SUCCESS;
	}

	@Override
	public String socialLogout() {
		try {
			if (!StringUtils.isBlank(this.getProvider())) {
				switch (PROVIDER.valueOf(this.getProvider())) {
					case facebook:
						JpSocialSystemUtils.removeFacebookCredentialsFromCookie(this.getRequest(), ServletActionContext.getResponse());
						break;
					case twitter:
						JpSocialSystemUtils.removeTwitterCredentialsFromCookie(this.getRequest(), ServletActionContext.getResponse());
						break;
				}
			} else {
				JpSocialSystemUtils.removeFacebookCredentialsFromCookie(this.getRequest(), ServletActionContext.getResponse());
				JpSocialSystemUtils.removeTwitterCredentialsFromCookie(this.getRequest(), ServletActionContext.getResponse());
			}
		} catch (Throwable ex) {
			addActionError(this.getText("jpsocial.error.deletecookie"));
			return INPUT;
		} 
		return SUCCESS;
	}

	public String getQueue() {
		return _queue;
	}

	public void setQueue(String queue) {
		this._queue = queue;
	}

	public String getFileCaption() {
		return _fileCaption;
	}

	public void setFileCaption(String fileCaption) {
		this._fileCaption = fileCaption;
	}

	public File getImage() {
		return _image;
	}

	public void setImage(File image) {
		this._image = image;
	}

	public String getImageContentType() {
		return _imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this._imageContentType = imageContentType;
	}

	public String getImageFileName() {
		return _imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this._imageFileName = imageFileName;
	}

	public String getTweet() {
		return _tweet;
	}

	public void setTweet(String tweet) {
		this._tweet = tweet;
	}

	public String getFacebookText() {
		return this._facebookText;
	}

	public void setFacebookText(String facebookText) {
		this._facebookText = facebookText;
	}

	public String getProvider() {
		return _provider;
	}

	public void setProvider(String provider) {
		this._provider = provider;
	}

	public String getRedirectUrl() {
		return _redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this._redirectUrl = redirectUrl;
	}
	
	// management variable
	private String _queue;
	// form variable
	private String _facebookText;
	private String _tweet;
	private File _image;
	private String _imageContentType;
	private String _imageFileName;
	private String _fileCaption;
	
	private String _provider;
	private String _redirectUrl;
}
