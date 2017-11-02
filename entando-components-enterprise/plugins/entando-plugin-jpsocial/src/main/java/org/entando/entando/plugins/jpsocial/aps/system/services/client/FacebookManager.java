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
package org.entando.entando.plugins.jpsocial.aps.system.services.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.ISocialUser;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.restfb.BinaryAttachment;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;

/**
 *
 * @author entando
 */
public class FacebookManager extends AbstractService implements IFacebookManager {

	private static final Logger _logger =  LoggerFactory.getLogger(FacebookManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	protected void release() {
//    super.release();
	}

	@Override
	public String getLinkBySocialId(String id) throws ApsSystemException{
		String userId = JpSocialSystemUtils.getUserIdBySocialId(id);
		String postId = JpSocialSystemUtils.getPostIdBySocialId(id);
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(postId)){
			throw new ApsSystemException("Cannot userId not found for this facebook id" + id);
		}
		if(StringUtils.isBlank(postId)){
			throw new ApsSystemException("Cannot postId not found for this facebook id" + id);
		}
		return "https://www.facebook.com/" + userId +"/posts/" + postId;
	}

	/**
	 * Resize the given text to the desired length
	 *
	 * @param text
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	private String checkText(String text, int maxLength) {

		if (null != text && text.trim().length() > 0) {
			// get rid of HTML tags
			text = text.replaceAll("<[^<>]+>", "").trim();
			// get rid of not allowed '<','>'
			text = text.replaceAll("<", "").replaceAll(">", "");
			if (text.length() > maxLength) {
				text = text.substring(0, maxLength);
			}
		}
		return text;
	}

	/*
	 * private BatchRequest doImageBatch(String path, String message) {
	 * BatchRequestBuilder imgBatch = new BatchRequestBuilder("me/photos");
	 *
	 * if (null != path && !"".equals(path.trim())) { String fileName = new
	 * File(path).getName(); imgBatch.attachedFiles(fileName); // include message
	 * body if any if (null != message && !"".equals(message.trim())) {
	 * imgBatch.body(Parameter.with("message", message)); } } return
	 * imgBatch.build(); }
	 *
	 * private BatchRequest diVideoBatch(String path, String message) {
	 * BatchRequestBuilder videoBatch = new BatchRequestBuilder("me/videos");
	 *
	 * if (null != path && !"".equals(path.trim())) { String fileName = new
	 * File(path).getName(); videoBatch.attachedFiles(fileName); // include
	 * message body if any if (null != message && !"".equals(message.trim())) {
	 * videoBatch.body(Parameter.with("message", message)); } } return
	 * videoBatch.build(); }
	 *
	 * private BinaryAttachment doBinaryAttachment(String path) throws
	 * FileNotFoundException { String fileName = new File(path).getName();
	 *
	 * return BinaryAttachment.with(fileName, new FileInputStream(path)); }
	 *
	 * private List<BatchRequest> prepareBatchRequest(List<String> images,
	 * List<String> videos, String message) { List<BatchRequest> batch = new
	 * ArrayList<BatchRequest>();
	 *
	 * return batch; }
	 */
	@Override
	public String publishMessage(FacebookClient client, String message) throws ApsSystemException {
		String messageId = "";

		try {
			message = checkText(message, MESSAGE_LENGTH);
			FacebookType publishMessageResponse =
					client.publish("me/feed", FacebookType.class, Parameter.with("message", message));
			messageId = publishMessageResponse.getId();
		} catch (Throwable t) {
			_logger.error("Error publishing a message in facebook", t);
			throw new ApsSystemException("Error publishing a message in facebook", t);
		}
		return messageId;
	}

	@Override
	public String publishImage(FacebookClient client, String imagePath, String imgDescr) throws ApsSystemException {
		String photoId = "";

		try {
			String fileName = new File(imagePath).getName();

//      System.out.println(">>> publishing image @ " + imagePath);
//      System.out.println(">>> publishing image descrizione " + imgDescr);
//      System.out.println(">>> publishing image filename " + fileName);
			imgDescr = checkText(imgDescr, MESSAGE_WITH_ATTACH_LENGTH);
			FacebookType publishPhotoResponse =
					client.publish("me/photos", FacebookType.class,
					BinaryAttachment.with(fileName, new FileInputStream(imagePath)),
					Parameter.with("message", imgDescr));
			photoId = publishPhotoResponse.getId();
		} catch (Throwable t) {
			_logger.error("Error publishing a photo in facebook", t);
			throw new ApsSystemException("Error publishing a photo in facebook", t);
		}
		return photoId;
	}

	@Override
	public String publishVideo(FacebookClient client, String videoPath, String ImageDescription) throws ApsSystemException {
		String videoId = "";

		// TODO get filename from videoPath
		try {
			FacebookType publishVideoResponse =
					client.publish("me/videos", FacebookType.class,
					BinaryAttachment.with("cat.mov", getClass().getResourceAsStream(videoPath)),
					Parameter.with("message", ImageDescription));
		} catch (Throwable t) {
			_logger.error("Error publishing a video in facebook", t);
			throw new ApsSystemException("Error publishing a video in facebook", t);
		}
		return videoId;
	}

	@Override
	public String getAuthenticationURL(String customUrl) throws ApsSystemException{
		ConfigInterface configInterface = (ConfigInterface) this.getConfigManager();
		StringBuilder url = new StringBuilder("");
		String baseUrl = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
		String appId = configInterface.getParam(JpSocialSystemConstants.FACEBOOK_CONSUMER_KEY_PARAM_NAME);

		url.append("https://www.facebook.com/dialog/oauth?client_id=");
		url.append(appId);
		url.append("&");
		url.append("redirect_uri=");
		if (null != customUrl && !"".equals(customUrl.trim())) {
			// make sure to include the servlet in the request
			if (customUrl.indexOf(JpSocialSystemConstants.SERVLET_FACEBOOK) < 0) {
				customUrl = customUrl.replace(baseUrl, baseUrl.concat(JpSocialSystemConstants.SERVLET_FACEBOOK.concat("/")));
			}
			try {
				customUrl = URLEncoder.encode(customUrl, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				_logger.error("error in getAuthenticationURL", ex);
				throw new ApsSystemException("error encoding url", ex.getCause());
			}
			url.append(customUrl);
		} else {
			// use default!
			url.append(baseUrl);
			url.append("FacebookEnforcer");
		}
		url.append("&");
		url.append("scope=email,publish_stream");

		return url.toString();
	}

	public FacebookClient createClient(String accessToken){
		FacebookClient client = new DefaultFacebookClient(accessToken);
		return client;
	}

	@Override
	public ISocialUser createSocialCredentials(String accessToken) {
		FacebookCredentials credentials = new FacebookCredentials(accessToken);
		credentials.setClient(createClient(accessToken));
		return credentials;
	}

	public ConfigInterface getConfigManager() {
		return configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this.configManager = configManager;
	}
	private ConfigInterface configManager;
	public final static int MESSAGE_LENGTH = 420;
	public final static int MESSAGE_WITH_ATTACH_LENGTH = 10000;
}
