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
package org.entando.entando.plugins.jpsocial.apsadmin.social;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.IBitLyManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.IFacebookManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ILinkedInManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ITwitterManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.IYoutubeManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.model.YoutubeCategory;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.ISocialPostManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.GoogleCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.LinkedInCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.YoutubeCredentials;
import org.entando.entando.plugins.jpsocial.apsadmin.social.helper.Post;
import org.entando.entando.plugins.jpsocial.apsadmin.social.helper.PostAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AttachAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.ImageAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.apsadmin.content.ContentAction;
import com.rosaloves.bitlyj.Url;

/**
 * @author entando
 */
public abstract class AbstractSocialAction extends ContentAction {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractSocialAction.class);

	/**
	 * Get the default attribute of the content
	 *
	 * @param content
	 * @param provider
	 * @return
	 */
	protected PostAttribute addDefaultContentAttribute(Post post, String provider) throws ApsSystemException {
		PostAttribute attributePost = null;
		try {
			if (null != post && null != post.getContent()) {
				Content content = post.getContent();
				AttributeInterface attributeInterface = content.getAttributeByRole(JacmsSystemConstants.ATTRIBUTE_ROLE_TITLE);
				String defaultLanguage = this.getLangManager().getDefaultLang().getCode();

				// remove the existing queue
				if (null != post.getQueue() && !post.getQueue().isEmpty()) {
					post.getQueue().clear();
				}
				// add the attribute to the delivery queue
				if (null != provider) {
					if (null != attributeInterface) {
						attributePost = new PostAttribute(provider, attributeInterface.getName(), defaultLanguage, attributeInterface.isMultilingual());
					} else {
						attributePost = new PostAttribute(provider, JpSocialSystemConstants.ATTRIBUTE_SOCIALDESCRIPTION, defaultLanguage, false);
					}
					// don't ask for user confirmation
					post.setFast(true);
					// append the permalink to the content
					attributePost.setAppendLink(true);
					// add the attribute
					post.getQueue().add(attributePost);
				}
			}
		} catch (Throwable t) {
			_logger.error("error getting the default attribute", t);
			throw new ApsSystemException("jpsocial: error getting the default attribute");
		}
		return attributePost;
	}

	/**
	 * Get the caegories of the current content post
	 *
	 * @param langCode
	 * @return
	 * @throws ApsSystemException
	 */
	private List<String> getPostCategories(String langCode) throws ApsSystemException {
		List<String> result = new ArrayList<String>();
		Post post = this.getPendingPost();
		try {
			if (null != post.getContent()
					&& null != post.getContent().getCategories()) {
				List<Category> categories = post.getContent().getCategories();
				Iterator<Category> itr = categories.iterator();
				while (itr.hasNext()) {
					Category category = itr.next();
					String title = category.getTitle(langCode);
					if (null != title) {
						result.add(title);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error extracting content categories", t);
			throw new ApsSystemException("jpsocial: error extracting content categories");
		}
		return result;
	}

	/**
	 * Generate the redirect url to the desired action of the backend
	 *
	 * @param provider
	 * @param action
	 * @return
	 */
	protected String getBackendUrl(String provider, String action) {
		String baseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		StringBuilder url = null;
		try {
			url = new StringBuilder(baseUrl);
			if (null != provider && !"".equals(provider)) {
				if (provider.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
					url.append(JpSocialSystemConstants.SERVLET_FACEBOOK);
				} else if (provider.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
					url.append(JpSocialSystemConstants.SERVLET_TWITTER);
				} else if (provider.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_YOUTUBE)) {
					url.append(JpSocialSystemConstants.SERVLET_YOUTUBE);
				}
			}
			if (url != null && !url.toString().endsWith("/")) {
				url.append("/");
			}
			url.append("do/jacms/Content/");
			url.append(action);
			if (provider.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
				url.append("/contentOnSessionMarker=").append(super.getContentOnSessionMarker());
			} else {
				url.append("?contentOnSessionMarker=").append(super.getContentOnSessionMarker());
			}
		} catch (Throwable t) {
			_logger.error("error in getBackendUrl", t);
		}
		// WARNING TWITTER SERVLET WORKS IN A DIFFERENT WAY! MUST GET RID OF THE SERVLET REFERENCE
		return url.toString().replace(JpSocialSystemConstants.SERVLET_TWITTER + "/", "");
	}

	/**
	 * Build the error string given the unsent parameters map
	 *
	 * @param map
	 * @return
	 */
	protected String getUnsentAttributesErrorString(Map<String, List<String>> map) {
		StringBuilder result = new StringBuilder(getText("jpsocial.message.unsentAttributes"));
		result.append(" ");
		if (null != map && !map.isEmpty()) {
			Iterator<String> kitr = map.keySet().iterator();
			while (kitr.hasNext()) {
				String queue = kitr.next();
				result.append(queue);
				result.append(": ");
				List<String> list = map.get(queue);
				if (null != list && !list.isEmpty()) {
					Iterator<String> litr = list.iterator();
					while (litr.hasNext()) {
						String attribute = litr.next();
						result.append(attribute);
						result.append(",");
					}
					// remove the last comma
					result.deleteCharAt(result.length());
					result.append(" ");
				}
			}
		}
		return result.toString();
	}

	/**
	 * Get the Youtube categories
	 *
	 * @return
	 */
	public List<YoutubeCategory> getYoutubeCategories() {
		return this.getYoutubeManager().getYoutubeGategories();
	}

	/**
	 * Save and update content before editing the delivery queue
	 *
	 * @return
	 */
	protected String saveAndApproveBeforePost(boolean checkFields) {
		Content content = this.updateContentOnSession();
		if (checkFields) {
			this.getContentActionHelper().scanEntity(content, this);
			// check if there are errors before saving
			if (getFieldErrors().size() > 0) {
				return INPUT;
			}
		}
		return this.saveAndApprove();
	}

	/**
	 * Generate the link to the content, in the shortened form, if possible
	 *
	 * @return
	 */
	private String getContentPermalink(Content content) {
		Lang lang = this.getLangManager().getDefaultLang();
		Map<String, String> map = new HashMap<String, String>();
		String link = "";
		try {
			if (null != content) {
				String pageName = content.getViewPage();
				IPage page = this.getPageManager().getPage(pageName);
				map.put("contentId", content.getId());
				_logger.debug("getContentPermalink [content.getViewPage()] - pageName: {}", pageName);
				_logger.debug("getContentPermalink - page: {}", page);
				_logger.debug("getContentPermalink - lang: {}", lang);
				link = this.getUrlManager().createURL(page, lang, map);
				link = this.getShortUrl(link);
				//add permalink to fields
				this.setService(JACMS_CONTENT_MANAGER);
				this.setIdObject(content.getId());
				this.setPermalink(link);
			}
		} catch (Throwable t) {
			this.addActionError(this.getText("jpsocial.error.permalink"));
			_logger.error("error in getContentPermalink", t);
		}
		return link;
	}

	/**
	 * Ad an action error for the given attribute
	 *
	 * @param attribute
	 */
	private void setError(PostAttribute attribute) {
		addActionError(getText("jpsocial.message.emptyText", new String[]{attribute.getName(), attribute.getLang()}));
		_logger.debug("error in setError - {} is empty", attribute.getId());
	}

	/**
	 * This valorizes the VO with the fields to post. NOTE: this method seems to
	 * be very difficult but actually is really simple. TODO decompose in
	 * smaller methods Wer ist normal hier und wer ist hier krank?!
	 *
	 * @return
	 */
	protected List<PostAttribute> extractAttributesValue() throws ApsSystemException {
		Post post = getPendingPost();
		Set<PostAttribute> queue = post.getQueue();
		List<PostAttribute> invalidPostAttribute = new ArrayList<PostAttribute>();
		try {
			// value the objects
			if (null != post) {
				Content content = post.getContent();
				// multiple attributes must always be confirmed before expedition
				if (post.isFast() && post.getQueue().size() > 1) {
					post.setFast(false);
				}
				// iterate content and queue attributes in a sorted manner
				if (null != content) {
					Iterator<PostAttribute> itr = queue.iterator();
					while (itr.hasNext()) {
						PostAttribute postAttribute = itr.next();
						String attributeName = postAttribute.getName();
						String permalink = "";
						// generate permalink if needed
						if (postAttribute.isAppendLink()) {
							permalink = " " + getContentPermalink(content);
						}
						// evaluate the default description of the content
						if (attributeName.equals(JpSocialSystemConstants.ATTRIBUTE_SOCIALDESCRIPTION)) {
							String description = content.getDescr() + permalink;
							if (null != description && !description.equals("")) {
								postAttribute.setText(description);
								postAttribute.setValue(null);
								postAttribute.setType(JpSocialSystemConstants.TYPE_TEXT);
							}
						} else {
							// evaluate each post attribute against the content
							AttributeInterface attrInterface = content.getAttributeMap().get(attributeName);
							Object attribute = content.getAttribute(attributeName);
							if (attrInterface.getType().equals(JpSocialSystemConstants.ATTRIBUTE_SOCIALHYPERTEXT)) {
								String text = ((HypertextAttribute) attribute).getTextForLang(postAttribute.getLang());
								if (null == text || "".equals(text.trim())) {
									setError(postAttribute);
								} else {
									// get rid of HTML tags
									text = text.replaceAll("<[^<>]+>", "").trim() + permalink;
									// unescape html entities
									text = StringEscapeUtils.unescapeHtml(text);
									// TODO ESCACPE BACK TO TEXT!
									postAttribute.setText(text);
									postAttribute.setValue(attribute);
									postAttribute.setType(JpSocialSystemConstants.TYPE_TEXT);
								}
							} else if (attrInterface.getType().equals(JpSocialSystemConstants.ATTRIBUTE_SOCIALLONGTEXT)) {
								String text = ((TextAttribute) attribute).getTextForLang(postAttribute.getLang());

								if (null == text || "".equals(text.trim())) {
									setError(postAttribute);
								} else {
									postAttribute.setText(text + permalink);
									postAttribute.setValue(attribute);
									postAttribute.setType(JpSocialSystemConstants.TYPE_TEXT);
								}
							} else if (attrInterface.getType().equals(JpSocialSystemConstants.ATTRIBUTE_SOCIALMONOTEXT)) {
								String text = ((MonoTextAttribute) attribute).getText();
								if (null == text || "".equals(text.trim())) {
									setError(postAttribute);
								} else {
									postAttribute.setText(text + permalink);
									postAttribute.setValue(attribute);
									postAttribute.setType(JpSocialSystemConstants.TYPE_TEXT);
								}
							} else if (attrInterface.getType().equals(JpSocialSystemConstants.ATTRIBUTE_SOCIALIMAGE)) {
								ResourceInterface resource = ((ImageAttribute) attribute).getResource();
								ResourceInstance instance = null;
								String text = "";
								String path = "";
								if (null != resource) {
									instance = ((AbstractMultiInstanceResource) resource).getInstance(0, null);
								}
								if (null != resource && null != instance) {
									path = resource.getDefaultUrlPath();
									postAttribute.setType(JpSocialSystemConstants.TYPE_IMAGE);
									postAttribute.setValue(attribute);
									postAttribute.setPath(path);
								} else {
									// invalidate attribute
									setError(postAttribute);
								}
								if (attrInterface.isMultilingual()) {
									text = ((ImageAttribute) attribute).getTextForLang(postAttribute.getLang());
								} else {
									text = ((ImageAttribute) attribute).getText();
								}
								if (null == text || "".equals(text.trim())) {
									setError(postAttribute);
								} else {
									postAttribute.setText(text + permalink);
								}
							} else if (attrInterface.getType().equals(JpSocialSystemConstants.ATTRIBUTE_SOCIALATTACH)) {
								ResourceInterface resource = ((AttachAttribute) attribute).getResource();
								ResourceInstance instance = null;
								String text = "";
								boolean check = true;
								if (null != resource) {
									instance = ((AbstractMonoInstanceResource) resource).getInstance();
								}
								if (null != resource && null != instance) {
									InputStream stream = this.getStorageManager().getStream(resource.getFolder() + instance.getFileName(), false);
									postAttribute.setType(JpSocialSystemConstants.TYPE_VIDEO);
									postAttribute.setValue(attribute);
									postAttribute.setStream(stream);
								} else {
									check = false;
									setError(postAttribute);
								}
								if (attrInterface.isMultilingual()) {
									text = ((AttachAttribute) attribute).getTextForLang(postAttribute.getLang());
								} else {
									text = ((AttachAttribute) attribute).getText();
								}
								if (null == text || "".equals(text.trim())) {
									setError(postAttribute);
									check = false;
								} else {
									postAttribute.setText(text + permalink);
								}
								// check whether the video is supported
								if (check && !getYoutubeManager().isYoutubeVideo(instance.getFileName())) {
									invalidPostAttribute.add(postAttribute);
								}
							} else if (attrInterface.isTextAttribute()) {
								// process the attribute with the role of Title assigned to it. It is a *text attribute for sure
								String text = "";

								if (attrInterface.isMultilingual()) {
									text = ((TextAttribute) attribute).getTextForLang(postAttribute.getLang());
								} else {
									text = ((MonoTextAttribute) attribute).getText();
								}
								if (null == text || "".equals(text.trim())) {
									setError(postAttribute);
								} else {
									postAttribute.setText(text + permalink);
									postAttribute.setValue(attribute);
									postAttribute.setType(JpSocialSystemConstants.TYPE_TEXT);
								}
							}
						}
					}
				} // else System.out.println("NO CONTENTS TO ITERATE OVER");
			} // else System.out.println("NO PENDING POST");
		} catch (Throwable t) {
			_logger.error("Error while evaluating content post attributes", t);
			throw new ApsSystemException("Error while evaluating content post attributes");
		}
		return invalidPostAttribute;
	}

	/**
	 * Redirect to the edit page again after the social network log in
	 *
	 * @return
	 */
	@Deprecated
	public String getAuthenticationURL(String socialNetworkId) {
		ConfigInterface configInterface = (ConfigInterface) this.getConfigManager();
		String appBaseUrl = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
		try {
			// Twitter
			if (socialNetworkId.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
				return this.getTwitterManager().getAuthenticationURL(null);
			}
			// facebook
			if (socialNetworkId.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
				return this.getFacebookManager().getAuthenticationURL(null);
			}
			// Youtube
			if (socialNetworkId.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_YOUTUBE)) {
				return this.getYoutubeManager().getAuthenticationURL(null);
			}
		} catch (Throwable t) {
			_logger.error("error in getRedirectionActionURL", t);
		}
		return appBaseUrl;
	}

	/**
	 * If we are not able to shorten the link we simply return it as it was in
	 * the origin. Exception is masked.
	 *
	 * @param urlToBeShortened
	 * @return
	 */
	private String getShortUrl(String urlToBeShortened) {
		try {
			Url shottedUrl = this.getBitLyManager().shortUrl(urlToBeShortened);
			return shottedUrl.getShortUrl();
		} catch (Throwable t) {
			_logger.error("error shortening the URL {}", urlToBeShortened, t);
			return urlToBeShortened;
		}
	}

	/**
	 * Get all the elements of the Post in the same order of the attributes of
	 * the content.
	 *
	 * @return the list of the post attributes
	 */
	public Set<PostAttribute> getDeliveryQueue(String queueId) {
		Post post = this.getPendingPost();
		List<Lang> langs = this.getLangs();
		Set<PostAttribute> result = new LinkedHashSet<PostAttribute>();
		try {
			if (null != post && null != queueId && !"".equalsIgnoreCase(queueId)) {
				Set<PostAttribute> postAttributes = post.getSortedQueue(langs);
				if (null != postAttributes && !postAttributes.isEmpty()) {
					Iterator<PostAttribute> itr = postAttributes.iterator();
					while (itr.hasNext()) {
						PostAttribute postAttribute = itr.next();
						if (postAttribute.getQueue().equalsIgnoreCase(queueId)
								&& !postAttribute.isIgnore()) {
							result.add(postAttribute);
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getDeliveryQueue", t);
		}
		return result;
	}

	public List<String> getSupportedProviders() {
		return Arrays.asList(JpSocialSystemConstants.SUPPORTED_PROVIDERS);
	}

	/**
	 * Only the servlet tampers with credentials so it's sufficient to see if
	 * the corresponding object is in the session
	 *
	 * @param providerName
	 * @return
	 */
	public boolean isLogged(String providerName) throws ApsSystemException {
		// fetch all credentials from session
		loadSocialSessionCredentials();
		this._credentialsFacebook = JpSocialSystemUtils.getFacebookCredentialsFromCookie(this.getRequest());
		this._credentialsTwitter = JpSocialSystemUtils.getTwitterCredentialsFromCookie(this.getRequest());
		if (providerName.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
			if (null != this.getCredentialsTwitter()) {
				return true;
			}
		} else if (providerName.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
			if (null != this.getCredentialsFacebook()) {
				return true;
			}
		} else if (providerName.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_GOOGLE)) {
			if (null != this.getCredentialsGoogle()) {
				return true;
			}
		} else if (providerName.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_YOUTUBE)) {
			if (null != this.getCredentialsYoutube()) {
				return true;
			}
		} else if (providerName.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_LINKEDIN)) {
			if (null != this.getCredentialsLinkedin()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get, from the given properties indexed by the language code, the one in
	 * the most suitable language: if the localization in the current back-end
	 * language is not available then use the default language.
	 *
	 * @param labels the label properties
	 * @return the localized label
	 */
	public String getLabel(ApsProperties labels) {
		String label = labels.getProperty(this.getCurrentLang().getCode());
		if (label == null || label.trim().length() == 0) {
			Lang defaultLang = this.getLangManager().getDefaultLang();
			label = labels.getProperty(defaultLang.getCode());
		}
		return label;
	}

	/**
	 * Submit the image attribute to the given social network
	 *
	 * @param attribute
	 * @throws ApsSystemException
	 */
	private void postImage(PostAttribute attribute) throws Throwable {
		String id = null;
		try {
			String text = attribute.getText();
			String path = attribute.getPath();
			if (attribute.getQueue().equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)
					&& isLogged(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
				_logger.debug("Submitting image attribute named {} in {}", attribute.getName(), JpSocialSystemConstants.PROVIDER_FACEBOOK);
				// facebook
				id = this.getFacebookManager().publishImage(_credentialsFacebook.getClient(), path, text);
				_logger.debug("image successfully submitted with ID {}", id);

			}
		} catch (Throwable t) {
			_logger.error("Error detected while posting an IMAGE attribute", t);
			throw new ApsSystemException("Error detected while posting an IMAGE attribute", t);
		}
	}

	private void postVideo(PostAttribute attribute) throws ApsSystemException {
		String id = null;
		try {
			String text = attribute.getText();
			InputStream stream = attribute.getStream();
			List<String> keywords = getPostCategories(attribute.getLang());
			if (attribute.getQueue().equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_YOUTUBE)
					&& isLogged(JpSocialSystemConstants.PROVIDER_YOUTUBE)) {
				_logger.debug("Submitting video attribute named {} to {}", attribute.getName(), JpSocialSystemConstants.PROVIDER_YOUTUBE);
				this.getYoutubeManager().uploadVideo(this.getCredentialsYoutube().getAccessToken(),
						stream,
						text,
						text,
						attribute.getCategory(),
						keywords,
						Arrays.asList(new String[]{"Entando", "Social CMS"}));

				_logger.debug("video successfully submitted with ID {}", id);
			}
		} catch (Throwable t) {
			_logger.error("Error detected while posting an VIDEO attribute", t);
			throw new ApsSystemException("Error detected while posting an VIDEO attribute", t);
		}
	}

	/**
	 * Submit the textual attribute to the intended social network
	 *
	 * @param text
	 * @throws ApsSystemException
	 */
	private void postText(PostAttribute attribute) throws Throwable {
		String id = null;
		try {
			String text = attribute.getText();
			if (attribute.getQueue().equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)
					&& isLogged(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
				_logger.debug("Submitting text attribute {} in {}", attribute.getName(), JpSocialSystemConstants.PROVIDER_FACEBOOK);
				// facebook
				id = this.getFacebookManager().publishMessage(_credentialsFacebook.getClient(), text);
				_logger.debug("Text attribute successfully submitted with ID {}", id);

			} else if (attribute.getQueue().equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)
					&& isLogged(JpSocialSystemConstants.PROVIDER_TWITTER)) {
				_logger.debug("Submitting text attribute {} in {}", attribute.getName(), JpSocialSystemConstants.PROVIDER_TWITTER);
				// tweet!
				id = this.getTwitterManager().tweet(text, _credentialsTwitter.getAccessToken(), _credentialsTwitter.getTokenSecret());
			} else if (attribute.getQueue().equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_LINKEDIN)
					&& isLogged(JpSocialSystemConstants.PROVIDER_LINKEDIN)) {
				_logger.debug("Submitting text attribute {} in {}", attribute.getName(), JpSocialSystemConstants.PROVIDER_TWITTER);
				this.getLinkedInManager().share(text, _credentialsLinkedin.getLinkedInAccessToken());
			}
			if (StringUtils.isNotBlank(id)) {
				createAndAddSocialPost(id, this.getIdObject(), this.getService(), this.getPermalink(), attribute.getQueue(), text, this.getCurrentUser().getUsername());
			}
		} catch (Throwable t) {
			_logger.error("Error detected while posting a TEXT attribute", t);
			throw new ApsSystemException("Error detected while posting a TEXT attribute", t);
		}
	}

	/**
	 * Invoke the proper manager for the type of the attribute
	 *
	 * @param attribute
	 */
	private boolean postAttribute(PostAttribute attribute) {
		try {
			if (null != attribute) {
				switch (attribute.getType()) {
					case JpSocialSystemConstants.TYPE_TEXT:
						postText(attribute);
						break;
					case JpSocialSystemConstants.TYPE_IMAGE:
						postImage(attribute);
						break;
					case JpSocialSystemConstants.TYPE_VIDEO:
						postVideo(attribute);
						break;
				}
			}
		} catch (Throwable t) {
			_logger.error("error in postAttribute", t);
			return false;
		}
		return true;
	}

	/**
	 * Collect the attributes in the given languages and invoke the proper
	 * manager to post each attribute
	 */
	protected String submitContent() throws ApsSystemException {
		Post pendingPost = getPendingPost();
		StringBuilder networks = new StringBuilder("");
		try {
			// load OAuth credentials
			loadSocialSessionCredentials();
			if (!pendingPost.getQueue().isEmpty()) {
				Iterator<PostAttribute> itr = pendingPost.getQueue().iterator();
				// get the categories of the content
				//List<Category> categories = pendingPost.getContent().getCategories();
				// submit each attribute
				while (itr.hasNext()) {
					PostAttribute currentAttribute = itr.next();
					// check whether to exclude the attribute form delivery
					if (!currentAttribute.isIgnore()) {
						this.setIdObject(pendingPost.getContent().getId());
						this.setService(JACMS_CONTENT_MANAGER);
						boolean posted = postAttribute(currentAttribute);
						if (posted) {
							// mark the attribute dispatched
							currentAttribute.setSubmitted(true);
							// track social networks used
							if (-1 == networks.toString().indexOf(currentAttribute.getQueue())) {
								networks.append(currentAttribute.getQueue());
								networks.append(",");
							}
						} else {
							addFieldError(currentAttribute.getName(),
									this.getText("jpsocial.message.attributeNotPosted",
											new String[]{this.getText("jpsocial.message." + currentAttribute.getQueue())}));
						}
					}
				}
				if (1 < networks.lastIndexOf(",")) {
					return networks.substring(0, networks.lastIndexOf(",")).toString();
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while submitting a post to the social network", t);
			throw new ApsSystemException("Error while submitting a post to the social network", t);
		}
		return networks.toString();
	}

	protected void addNoLoginToSession() {
		this.getRequest().getSession().setAttribute(JpSocialSystemConstants.NO_LOGIN, JpSocialSystemConstants.NO_LOGIN);
	}

	/**
	 * Get the pending post from the session
	 *
	 * @return *
	 */
	public Post getPendingPost() {
		String username = this.getCurrentUser().getUsername();
		Post pendingPost = (Post) this.getRequest().getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_PENDING);
		// create a new post object if the case
		if (null == pendingPost
				|| !pendingPost.getUsername().equals(username)) {
			pendingPost = new Post(username);
			putPostOnSession(pendingPost);
		}
		return pendingPost;
	}

	/**
	 * Clean the data of the post in the session, without affecting the user
	 * name
	 *
	 * @return
	 */
	protected Post emptyPostInSession() {
		Post pendingPost = getPendingPost();
		if (null != pendingPost.getQueue() && !pendingPost.getQueue().isEmpty()) {
			pendingPost.getQueue().clear();
		}
		pendingPost.setContent(null);
		pendingPost.setFast(false);
		return pendingPost;
	}

	/**
	 * Remove post from the session
	 */
	protected void purgePostInSession() {
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_PENDING);
	}

	/**
	 * Create a new empty post with the specified content.
	 *
	 * @param content
	 * @return
	 */
	protected Post resetPostInSession(Content content) {
		Post post = emptyPostInSession();

		post.setContent(content);
		post.setFast(false);
		return post;
	}

	/**
	 * Load the credentials in the session. Invoked automatically when getting
	 * the ave * current user
	 */
	protected void loadSocialSessionCredentials() {
//		this._credentialsTwitter = (TwitterCredentials) this.getRequest().getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_TWITTER);
//		this._credentialsFacebook = (FacebookCredentials) this.getRequest().getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_FACEBOOK);
		this._credentialsGoogle = (GoogleCredentials) this.getRequest().getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_GOOGLE);
		this._credentialsYoutube = (YoutubeCredentials) this.getRequest().getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_YOUTUBE);
		this._credentialsLinkedin = (LinkedInCredentials) this.getRequest().getSession().getAttribute(JpSocialSystemConstants.SESSION_PARAM_LINKEDIN);
	}

	protected SocialPost createAndAddSocialPost(String socialId, String objectId, String service, String permalink, String provider, String text, String user) throws ApsSystemException {
		SocialPost post;
		post = new SocialPost(text, permalink, service, objectId, provider, socialId, user);
		this.getSocialPostManager().addSocialPost(post);
		return post;
	}

	/**
	 * Remove credentials forcing the reconnection to the social network
	 */
	protected void removeSocialSessionCredentials() {
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_TWITTER);
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_FACEBOOK);
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_GOOGLE);
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_YOUTUBE);
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_LINKEDIN);
	}

	/**
	 * Put the given post into session
	 *
	 * @param pendingPost
	 */
	protected void putPostOnSession(Post pendingPost) {
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_PENDING);
		this.getRequest().getSession().setAttribute(JpSocialSystemConstants.SESSION_PARAM_PENDING, pendingPost);
	}

	protected FacebookCredentials getCredentialsFacebook() {
		return _credentialsFacebook;
	}

	protected TwitterCredentials getCredentialsTwitter() {
		return _credentialsTwitter;
	}

	public String getStatus() {
		return _status;
	}

	public void setStatus(String status) {
		this._status = status;
	}

	public ITwitterManager getTwitterManager() {
		return _twitterManager;
	}

	public void setTwitterManager(ITwitterManager twitterManager) {
		this._twitterManager = twitterManager;
	}

	protected IBitLyManager getBitLyManager() {
		return _bitLyManager;
	}

	public void setBitLyManager(IBitLyManager bitLyManager) {
		this._bitLyManager = bitLyManager;
	}

	public IURLManager getUrlManager() {
		return _urlManager;
	}

	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
		this.getPageManager();
	}

	public IFacebookManager getFacebookManager() {
		return _facebookManager;
	}

	public void setFacebookManager(IFacebookManager facebookManager) {
		this._facebookManager = facebookManager;
	}

	public GoogleCredentials getCredentialsGoogle() {
		return _credentialsGoogle;
	}

	public IYoutubeManager getYoutubeManager() {
		return _youtubeManager;
	}

	public void setYoutubeManager(IYoutubeManager youtubeManager) {
		this._youtubeManager = youtubeManager;
	}

	public YoutubeCredentials getCredentialsYoutube() {
		return _credentialsYoutube;
	}

	public void setCredentialsYoutube(YoutubeCredentials credentialsYoutube) {
		this._credentialsYoutube = credentialsYoutube;
	}

	public void setRedirUrl(String redirUrl) {
		this._redirUrl = redirUrl;
	}

	public String getRedirUrl() {
		return _redirUrl;
	}

	public IStorageManager getStorageManager() {
		return _storageManager;
	}

	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}

	public ILinkedInManager getLinkedInManager() {
		return _linkedInManager;
	}

	public void setLinkedInManager(ILinkedInManager linkedInManager) {
		this._linkedInManager = linkedInManager;
	}

	public LinkedInCredentials getCredentialsLinkedin() {
		return _credentialsLinkedin;
	}

	public void setCredentialsLinkedin(LinkedInCredentials credentialsLinkedin) {
		this._credentialsLinkedin = credentialsLinkedin;
	}

	public ISocialPostManager getSocialPostManager() {
		return _socialPostManager;
	}

	public void setSocialPostManager(ISocialPostManager socialPostManager) {
		this._socialPostManager = socialPostManager;
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

	public String getIdObject() {
		return _idObject;
	}

	public void setIdObject(String idObject) {
		this._idObject = idObject;
	}
	private String _service;
	private String _idObject;
	// management variables
	private String _permalink;
	private String _status;
	private String _redirUrl;
	// managers
	private LinkedInCredentials _credentialsLinkedin;
	private TwitterCredentials _credentialsTwitter;
	private FacebookCredentials _credentialsFacebook;
	private GoogleCredentials _credentialsGoogle;
	private YoutubeCredentials _credentialsYoutube;
	private IBitLyManager _bitLyManager;
	private IURLManager _urlManager;
	private ITwitterManager _twitterManager;
	private IFacebookManager _facebookManager;
	private IYoutubeManager _youtubeManager;
	private IStorageManager _storageManager;
	private ILinkedInManager _linkedInManager;
	private ISocialPostManager _socialPostManager;
	public static final String JACMS_CONTENT_MANAGER = "jacmsContentManager";
}
