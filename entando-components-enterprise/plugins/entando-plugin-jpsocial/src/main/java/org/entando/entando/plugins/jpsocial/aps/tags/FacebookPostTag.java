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
package org.entando.entando.plugins.jpsocial.aps.tags;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.FacebookManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.IFacebookManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.ISocialPostManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * WARNING: FOR THIS TO WORK TOMCAT >= 7.07 IS NEEDED
 *
 * @author entando
 */
public class FacebookPostTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(FacebookPostTag.class);

	@Override
	public int doStartTag() throws JspException {
		File postImage = null;
		String filePath = null;
		String postText = null;
		boolean proceed = false;
		
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			long limit = DEFAULT_MAX_UPLOAD_SIZE;
			try {
				if (null != getMaxSize()) {
					limit = Long.valueOf(getMaxSize());
				}
			} catch (NumberFormatException e) {
				_logger.error("invalid max upload size limit, defaulting to {}", limit, e);
			}
			if (isMultipart) {
				List<FileItem> postParam = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : postParam) {
					String fieldName = "";
					String fieldValue = "";
					if (item.isFormField()) {
						fieldName = item.getFieldName();
						fieldValue = item.getString();
						if (fieldName.equals(FIELD_POST_TEXT)) {
							postText = fieldValue;
							proceed = true;
						} else {
							_logger.error("ignoring parameter {}={}", fieldName, fieldValue);
						}
					} else {
						fieldName = item.getFieldName();
						String fileName = FilenameUtils.getName(item.getName());
						String contentType = item.getContentType();
						if (fieldName.equals(FIELD_POST_IMAGE)) {
							if (item.getSize() > 0 && contentType.contains("image")) {
								if (limit > item.getSize()) {
									// save file
									postImage = new File(fileName);
									filePath = postImage.getAbsolutePath();
									item.write(postImage);
									// ok
									proceed = true;
								} else {
									// ko
									proceed = false;
									_logger.debug("{} exceeds the upload limit, ignored", fileName);
								}
							} else if (item.getSize() > 0) {
								proceed = false;
								_logger.debug("{} is not recognized as an image (content type: {})", fileName, contentType);
							}
						} else {
							_logger.debug("ignoring file {}", fileName);
						}
					}
				}
			} else {
				return EVAL_BODY_INCLUDE;
			}
		} catch (FileUploadException e) {
			_logger.error("Failed to upload file");
		} catch (Throwable t) {
			_logger.error("error while executing the tag");
		}
		try {
			proceed = postOnFacebook(proceed, postImage, filePath, postText);
		} catch (Throwable t) {
			_logger.error("error while posting on facebook");
		}
		if (null != getSharerd() && !"".equals(getSharerd().trim())) {
            this.pageContext.setAttribute(getSharerd(), proceed);
          } else {
            this.pageContext.setAttribute(VAR_SENT, proceed);
          }
		return SKIP_BODY;
	}
	
	private boolean postOnFacebook(boolean proceed, File postImage, String filePath, String postText) throws ApsSystemException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		FacebookManager facebookManager = (FacebookManager) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.FACEBOOK_CLIENT_MANAGER, pageContext);
//		FacebookCredentials facebookCredentials = (FacebookCredentials) request.getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_FACEBOOK);
		FacebookCredentials facebookCredentials = JpSocialSystemUtils.getFacebookCredentialsFromCookie(request);
		if (facebookCredentials != null) {
			try {
				String id;
				if (null != postImage) {
					id = facebookManager.publishImage(facebookCredentials.getClient(), filePath, postText);
					postImage.delete();
				} else {
					id = facebookManager.publishMessage(facebookCredentials.getClient(), postText);
				}
				createAndAddSocialPost(id, PROVIDER.facebook.toString(), postText, JpSocialSystemUtils.getCurrentUserName(request));
			} catch (Throwable t) {
				_logger.error("error sharing content", t);
				proceed = false;
			}
		} else {
			proceed = false;
		}
		return proceed;
	}
	
	protected SocialPost createAndAddSocialPost(String socialId, String provider, String text, String user) throws ApsSystemException {
		return createAndAddSocialPost(socialId, null, null, null, provider, text, user);
	}
	protected SocialPost createAndAddSocialPost(String socialId, String objectId, String service, String permalink, String provider, String text, String user) throws ApsSystemException {
		SocialPost post;
		post = new SocialPost(text, permalink, service, objectId, provider, socialId, user);
		ISocialPostManager socialPostManager = (ISocialPostManager) ApsWebApplicationUtils.getBean("jpsocialSocialPostManager", pageContext);
		socialPostManager.addSocialPost(post);
		return post;
	}
	
	public String getSharerd() {
		return _sharerd;
	}
	
	public void setSharerd(String sharerd) {
		this._sharerd = sharerd;
	}
	
	public String getMaxSize() {
		return _maxSize;
	}
	
	public void setMaxSize(String maxSize) {
		this._maxSize = maxSize;
	}
	
	public String getErrMsgVar() {
		return _errMsgVar;
	}
	
	public void setErrMsgVar(String errMsgVar) {
		this._errMsgVar = errMsgVar;
	}
	
	public String getAuthVar() {
		return _authVar;
	}
	
	public void setAuthVar(String authVar) {
		this._authVar = authVar;
	}
	private String _authVar;
	private String _sharerd;
	private String _maxSize;
	private String _errMsgVar;
	private IFacebookManager facebookManager;
	public final static String VAR_LOGIN = "loginUrl";
	public final static String VAR_SENT = "shared";
	public final static String FIELD_POST_TEXT = "postText";
	public final static String FIELD_POST_IMAGE = "postImage";
	public final static long DEFAULT_MAX_UPLOAD_SIZE = 1048576;
}
