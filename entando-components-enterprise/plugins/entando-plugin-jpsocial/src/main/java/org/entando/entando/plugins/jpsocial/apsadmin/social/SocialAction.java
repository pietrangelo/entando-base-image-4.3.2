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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.apsadmin.social.helper.Post;
import org.entando.entando.plugins.jpsocial.apsadmin.social.helper.PostAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;

/**
 * @author entando
 */
public class SocialAction extends AbstractSocialAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SocialAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (null != this._origin) {
			if (JpSocialSystemConstants.ORIGIN_EDIT == Integer.valueOf(this._origin)
					|| JpSocialSystemConstants.ORIGIN_LIST == Integer.valueOf(this._origin)) {
				try {
					fetchPostFields();
				} catch (Throwable t) {
					_logger.error("error in validate", t);
				}
			}
		}
	}

	/**
	 * Check what fields have been confirmed for posting
	 */
	private void fetchPostFields() throws ApsSystemException {
		Set<PostAttribute> queue = getPendingPost().getQueue();
		if (null != queue) {
			Iterator<PostAttribute> itr = queue.iterator();
			while (itr.hasNext()) {
				PostAttribute attribute = itr.next();
				String postText = this.getRequest().getParameter(attribute.getId());
				if (null != postText && !"".equals(postText.trim())) {
					postText = postText.trim();
					String postRadio = this.getRequest().getParameter(attribute.getId() + "_confirm");
					if (null != postRadio && !"".equalsIgnoreCase(postRadio.trim())) {
						try {
							// update the textarea value
							attribute.setText(postText);
							// get the youtube category, if the attribute type allows it
							if (attribute.getQueue().equals(JpSocialSystemConstants.PROVIDER_YOUTUBE)) {
								String ytCategory = this.getRequest().getParameter(attribute.getId() + "_ytCategory");

								if (null != ytCategory && !"".equals(ytCategory)) {
									// assign category
									attribute.setCategory(ytCategory);
								} else {
									// assign default youtbe category
									attribute.setCategory(JpSocialSystemConstants.DEFAULT_CATEGORY);
								}
							}
							// check if the user signed in for the current queue
							if (!isLogged(attribute.getQueue())) {
								addActionError(this.getText("jpsocial.message.cannotShareSelected",
										new String[]{this.getText("jpsocial.message." + attribute.getQueue())}));
								return;
							}
						} catch (Throwable t) {
							throw new ApsSystemException("fetchPostFields");
						}
					} else {
						// invalidate attribute!
						attribute.setIgnore(true);
					}
				}
			}
		}
	}
	
	/**
	 * Add a single attribute to the delivery queue
	 * @return the result code.
	 */
	public String addAttribute() {
		//Logger log = ApsSystemUtils.getLogger();
		Content content = this.updateContentOnSession();
		Post post = this.getPendingPost();
		boolean unique = true;
		//avoid login into portal with selected provider credentials
		this.addNoLoginToSession();
		try {
			this.loadSocialSessionCredentials();
			// add the attribute
			if (null != this._queueId
					&& null != this._attributeId
					&& null != this._langId
					&& null != content) {
				// put the content related to the post if the case
				if (null == post.getContent()) {
					post.setContent(content);
				}
				// if the content has changed refresh it and delete existing post queue
				if (null != post.getContent()
						&& null != content.getId()
						&& null != post.getContent().getId()
						&& !post.getContent().getId().equals(content.getId())) {
					post.setContent(content);
					post.getQueue().clear();
				}
				// get the attribute interface
				AttributeInterface attributeInteface = post.getContent().getAttributeMap().get(_attributeId);
				// check if the attribute supports multiple languages
				if (null != attributeInteface) {
					unique = attributeInteface.getType().equalsIgnoreCase(JpSocialSystemConstants.ATTRIBUTE_SOCIALIMAGE)
							|| attributeInteface.getType().equalsIgnoreCase(JpSocialSystemConstants.ATTRIBUTE_SOCIALATTACH);
				}
				PostAttribute attributePost =
						new PostAttribute(_queueId, _attributeId, _langId, unique);
				if (null == post.getAttribyteById(attributePost.getId())) {
					post.getQueue().add(attributePost);
					this.addActionMessage(this.getText("jpsocial.message.attributeAdded",
							new String[]{_attributeId, _langId, this.getText("jpsocial.message." + _queueId)}));
				}
				// check whether we need to sign in
				if (!isLogged(this._queueId)) {
					_logger.debug("jpsocial: redirecting to the {} login after a new attribute has been added", this._queueId);
					return "redirectUrlFromEdit";
				}
				return SUCCESS;
			} else {
				this.addActionError(this.getText("jpsocial.message.cannotProceed"));
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in addAttribute", t);
			return FAILURE;
		}
	}

	/**
	 * Dispatch the attributes to the desired delivery queue
	 * @return the result code.
	 */
	public String postQueue() {
		Post post = this.getPendingPost();
		String networks = null;
		try {
			this.loadSocialSessionCredentials();
			// post content
			networks = this.submitContent();
			// evaluate operation output
			Map<String, List<String>> unsentMap = post.detectUnsentAttributes();
			// process error
			if (unsentMap.isEmpty() && null != networks && !"".equals(networks.trim())) {
				// System.out.println("~~~1~~~ "+this.getText("jpsocial.message.queueDelivered", new String[]{networks}));
				this.addActionMessage(this.getText("jpsocial.message.queueDelivered", new String[]{networks}));
				// purge the post object data
				this.purgePostInSession();
			} else {
				if (null != networks && !"".equals(networks.trim())) {
					// System.out.println("~~~2~~~ "+ this.getText("jpsocial.message.queuePartiallyDelivered",
					// new String[]{networks}));
					String unsentString = getUnsentAttributesErrorString(unsentMap);
					this.addActionError(this.getText("jpsocial.message.queuePartiallyDelivered",
							new String[]{unsentString}));
				} else {
					if (null == this.getFieldErrors()
							|| (null != this.getFieldErrors() && this.getFieldErrors().isEmpty())) {
						// System.out.println("~~~3~~~ "+ this.getText("jpsocial.message.emptyQueue")); 
						this.addActionError(this.getText("jpsocial.message.emptyQueue"));
						// reenable all the attributes
						post.restoreAttributes();
						return "emptyQueue";
					} else {
						// System.out.println("~~~4~~~ "+ this.getText("jpsocial.message.queueNotDelivered")); 
						this.addActionError(this.getText("jpsocial.message.queueNotDelivered"));
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in postQueue", t);
			this.addActionError("jpsocial.message.cannotPost");
			return "postError";
		}
		return SUCCESS;
	}

	/**
	 * Tweet the selected attributes. Attributes belonging to queue other than
	 * Twitter will be invalidated
	 * @return the result code.
	 */
	public String tweetContent() {
		String result = null;
		Post post = getPendingPost();
		//avoid login into portal with selected provider credentials
		this.addNoLoginToSession();
		try {
			result = this.saveAndApproveBeforePost(true);
			if (!result.equals(SUCCESS)) {
				return result;
			}
			// get the default attribute
			this.addDefaultContentAttribute(post, JpSocialSystemConstants.PROVIDER_TWITTER);
			// check whether the user is logged
			if (!isLogged(JpSocialSystemConstants.PROVIDER_TWITTER)) {
				_logger.debug("jpsocial: redirecting to login page after adding the default attribute");
				return "notLogged";
			}
		} catch (Throwable t) {
			_logger.error("error in tweetContent", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Post the desired attributes to the supported social networks
	 * @return the result code.
	 */
	public String postContent() {
		String result = null;
		Post post = getPendingPost();
		Content content = this.updateContentOnSession();
		boolean cleanQueue = false;
		try {
			// check if all the networks present in the queue are available for sharing
			Iterator<PostAttribute> itr = post.getQueue().iterator();
			while (itr.hasNext()) {
				PostAttribute attribute = itr.next();
				if (!isLogged(attribute.getQueue())) {
					cleanQueue = true;
				}
			}
			if (cleanQueue) {
				_logger.debug("jpsocial: detected spurious queue, cleaning");
				post.getQueue().clear();
			}
			// check whether we have somthing to share
			if (null != post.getQueue() && !post.getQueue().isEmpty()) {
				// update the last version of the content
				if (null != content) {
					post.setContent(content);
				}
				// ok we have something to share
				result = saveAndApproveBeforePost(true);
				if (!result.equals(SUCCESS)) {
					_logger.error("jpsocial: error saving the content, result {}", result);
					return result;
				}
			} else {
				// nothing to share
				this.addActionError(getText("jpsocial.message.nothingToShare"));
				return "edit";
			}
		} catch (Throwable t) {
			_logger.error("error in postContent", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Load the content and share its default attibute
	 * @return the result code.
	 */
	public String postContentFromList() {
		Content content = null;
		this.addNoLoginToSession();
		try {
			if (null != this.getQueueId()) {
				content = this.getContentManager().loadContent(this.getContentId(), true);
				if (null != content) {
					Post post = resetPostInSession(content);
					addDefaultContentAttribute(post, this._queueId);
				}
				if (!isLogged(this._queueId)) {
					_logger.debug("jpsocial: redirect to the {} login page from the list", this._queueId);
					return "redirectUrlFromList";
				}
				return "editQueue";
			}
		} catch (Throwable t) {
			_logger.error("error in postContentFromList", t);
			return FAILURE;
		}
		return INPUT;
	}
	
	/**
	 * Trampoline to the delivery queue page
	 * @return the result code.
	 */
	public String editQueue() {
		Post post = getPendingPost();
		try {
			// update the content
			this.updateContentOnSession();
			// valorize the data to post
			List<PostAttribute> invalidAttrs;
			try {
				invalidAttrs = extractAttributesValue();
			} catch (Exception e) {
				return INPUT;
			}
			// remove invalid attributes form the list
			if (null != invalidAttrs && !invalidAttrs.isEmpty()) {
				Iterator<PostAttribute> itr = invalidAttrs.iterator();
				while (itr.hasNext()) {
					PostAttribute attribute = itr.next();
					post.getQueue().remove(attribute);
					addActionError(getText("jpsocial.message.removedVideoAttribute", new String[]{attribute.getName()}));
					_logger.debug("Removed attribute frm the queue {}", attribute.getId());
				}
			}
			// check if there are field error pending
			if (!getActionErrors().isEmpty() || !getFieldErrors().isEmpty()) {
				// place the content in session again
				String sessionParam = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker();
				this.getRequest().getSession().setAttribute(sessionParam, post.getContent());
				return INPUT;
			}
		} catch (Throwable t) {
			_logger.error("error in editQueue", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Exit the delivery queue without
	 * @return the result code.
	 */
	public String cancelPost() {
		Post pendingPost = this.getPendingPost();
		try {
			// if there where some attributes excluded reenable them
			pendingPost.restoreAttributes();
			// ask for confirmation in any case
			pendingPost.setFast(false);
			// return to list if asked to
			if (null == this._origin
					|| (null != this._origin && ("".equals(this._origin.trim())
					|| JpSocialSystemConstants.ORIGIN_LIST == Integer.valueOf(this._origin)))) {
				return "list";
			}
		} catch (NumberFormatException e) {
			_logger.error("couldn't detecte the orgin page, returning to content list (check the 'origin' parameter in the form!)");
			return "list";
		} catch (Throwable t) {
			_logger.error("error in cancelPost", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Exit the delivery queue without
	 * @return the result code.
	 */
	public String backToContentEdit() {
		return SUCCESS;
	}
	
	/**
	 * Reenter the edit view after authentication
	 * @return the result code.
	 */
	public String reenter() {
		Post post = getPendingPost();
		if (null != post.getContent()) {
			String sessionParam = ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker();
			this.getRequest().getSession().setAttribute(sessionParam, post.getContent());
			this.setContentId(post.getContent().getId());
			addActionMessage(getText("jpsocial.message.loginSuccess"));
		} else {
			// oops missing content! 
			addActionMessage(getText("jpsocial.message.loginOkButError"));
			_logger.error("jpsocial: could not restore content after logging into the social network");
			return "list";
		}
		// edit
		return SUCCESS;
	}
	
	public String revokeCredentials() {
		removeSocialSessionCredentials();
		addActionMessage(getText("jpsocial.message.credentialsRevoked"));
		return SUCCESS;
	}
	
	/**
	 * Redirect user to the social network login from the content list page
	 * @return the result code.
	 */
	public String redirectUrlFromList() {
		String provider = getQueueId();
		String url = null;
		try {
			if (null != provider) {
				if (!isLogged(provider)) {
					// ok redirect user to the proper login page
					if (provider.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
						url = this.getBackendUrl(JpSocialSystemConstants.PROVIDER_FACEBOOK, "editQueue.action");
						url = this.getFacebookManager().getAuthenticationURL(url);
					} else if (provider.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
						url = this.getBackendUrl(JpSocialSystemConstants.PROVIDER_TWITTER, "editQueue.action");
						url = this.getTwitterManager().getAuthenticationURL(url);
					} else if (provider.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_LINKEDIN)) {
						url = this.getBackendUrl(JpSocialSystemConstants.PROVIDER_LINKEDIN, "editQueue.action");
						url = this.getLinkedInManager().getAuthenticationURL(url);
					} else {
						_logger.error("jpscial: error: unknown provider {}", provider);
						return INPUT;
					}
					this.setRedirUrl(url);
					_logger.debug("jpsocial: redirecting to {}", url);
				}
			}
			// if the contentId is defined upload the post in session
		} catch (Throwable t) {
			_logger.error("error in redirectUrl", t);
		}
		return SUCCESS;
	}
	
	/**
	 * Redirect user to the social network login from the edit page
	 * @return the result code.
	 */
	public String redirectUrlToEdit() {
		String queue = getQueueId();
		this.updateContentOnSession();
		String url = null;
		try {
			if (null != queue) {
				if (!isLogged(queue)) {
					// ok redirect user to the proper login page
					if (queue.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
						url = this.getBackendUrl(JpSocialSystemConstants.PROVIDER_FACEBOOK, "reenter.action");
						url = this.getFacebookManager().getAuthenticationURL(url);
					} else if (queue.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
						url = this.getBackendUrl(JpSocialSystemConstants.PROVIDER_TWITTER, "reenter.action");
						url = this.getTwitterManager().getAuthenticationURL(url);
					} else if (queue.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_YOUTUBE)) {
						url = this.getBackendUrl(JpSocialSystemConstants.PROVIDER_YOUTUBE, "reenter.action");
						url = this.getYoutubeManager().getAuthenticationURL(url);
					} else if (queue.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_LINKEDIN)) {
						url = this.getBackendUrl(JpSocialSystemConstants.PROVIDER_LINKEDIN, "reenter.action");
						url = this.getLinkedInManager().getAuthenticationURL(url);
					} else {
						_logger.error("jpscial: error: unknown provider {}", queue);
						return INPUT;
					}
					this.setRedirUrl(url);
					_logger.debug("jpsocial: redirecting to {}", url);
				}
			}
			// if the contentId is defined upload the post in session
		} catch (Throwable t) {
			_logger.error("error in redirectUrl", t);
		}
		return "redirect";
	}
	
	public String signIn() {
		return SUCCESS;
	}
	
	public String callback() {
		return SUCCESS;
	}
	
	public String signedIn() {
		return SUCCESS;
	}

	public String getLangId() {
		return _langId;
	}

	public void setLangId(String langId) {
		this._langId = langId;
	}

	public String getQueueId() {
		return _queueId;
	}

	public void setQueueId(String queueId) {
		this._queueId = queueId;
	}

	public String getAttributeId() {
		return _attributeId;
	}

	public void setAttributeId(String attributeId) {
		this._attributeId = attributeId;
	}

	@Deprecated
	public String getQueueType() {
		return _queueType;
	}

	@Deprecated
	public void setQueueType(String queueType) {
		this._queueType = queueType;
	}

	public String getOrigin() {
		return _origin;
	}

	public void setOrigin(String origin) {
		this._origin = origin;
	}

	public String getProvider() {
		return _provider;
	}

	public void setProvider(String provider) {
		this._provider = provider;
	}

	public String getUrlToShare() {
		return _urlToShare;
	}

	public void setUrlToShare(String urlToShare) {
		this._urlToShare = urlToShare;
	}

	public String getUrlToComeBack() {
		return _urlToComeBack;
	}

	public void setUrlToComeBack(String urlToComeBack) {
		this._urlToComeBack = urlToComeBack;
	}
	
	// management variable
	private String _origin;
	@Deprecated
	private String _queueType;
	// input field
	private String _langId;
	private String _queueId;
	private String _attributeId;
	private String _provider;
	private String _urlToShare;
	private String _urlToComeBack;
	
}
