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
package org.entando.entando.plugins.jpsocial.aps.system;

public interface JpSocialSystemConstants {

  public final static String DEV_MODE_PARAM_NAME = "jpsocial_providerDevMode";
  
  public final static String FACEBOOK_CONSUMER_KEY_PARAM_NAME = "jpsocial_facebookConsumerKey";
  public final static String FACEBOOK_CONSUMER_SECRET_PARAM_NAME = "jpsocial_facebookConsumerSecret";
  
  public final static String TWITTER_CONSUMER_KEY_PARAM_NAME = "jpsocial_twitterConsumerKey";
  public final static String TWITTER_CONSUMER_SECRET_PARAM_NAME = "jpsocial_twitterConsumerSecret";
  public static final String TWITTER_CONSUMER_MANAGER = "jpsocialTwitterCookieConsumerManager";
  
  public final static String GOOGLE_CONSUMER_KEY_PARAM_NAME = "jpsocial_googleConsumerKey";
  public final static String GOOGLE_CONSUMER_SECRET_PARAM_NAME = "jpsocial_googleConsumerSecret";

  
  public final static String LINKEDIN_CONSUMER_KEY_PARAM_NAME = "jpsocial_linkedinConsumerKey";
  public final static String LINKEDIN_CONSUMER_SECRET_PARAM_NAME = "jpsocial_linkedinConsumerSecret";
  public final static String LINKEDIN_MANAGER = "jpsocialLinkedinManager";
  
  
  
  public final static String BITLY_MANAGER = "jpsocialBitLyManager";
  public final static String BITLY_USERNAME = "jpsocial_bitlyUsername";
  public final static String BITLY_APIKEY = "jpsocial_bitlyApikey";
 
  
//  public final static String YOUTUBE_CONSUMER_KEY_PARAM_NAME = "jpsocial_youtubeConsumerKey";
//  public final static String YOUTUBE_CONSUMER_SECRET_PARAM_NAME = "jpsocial_youtubeConsumerSecret";
  public final static String YOUTUBE_DEVELOPER_KEY = "jpsocial_youtubeDeveloperKey";
  
  public final static String TW_REDIRECT_URL = "tw_redirectUrl";
  public final static String FB_REDIRECT_URL = "fb_redirectUrl";
  public final static String LI_REDIRECT_URL = "li_redirectUrl";
  
  public final static String BACKEND_ACTION = "do/jacms/Content/reenter.action";
  
  public final static String PROVIDER_TWITTER = "twitter";
  public final static String PROVIDER_FACEBOOK = "facebook";
  public final static String PROVIDER_GOOGLE = "google";
  public final static String PROVIDER_YOUTUBE = "youtube";
  public final static String PROVIDER_LINKEDIN = "linkedin";
  
  public final static String ATTRIBUTE_SOCIALHYPERTEXT = "SocialHypertext";
  public final static String ATTRIBUTE_SOCIALLONGTEXT = "SocialLongtext";
  public final static String ATTRIBUTE_SOCIALMONOTEXT = "SocialMonotext";
  public final static String ATTRIBUTE_SOCIALIMAGE = "SocialImage";
  public final static String ATTRIBUTE_SOCIALATTACH = "SocialAttach";
  public final static String ATTRIBUTE_SOCIALTEXT = "SocialText";
  
  /* default argument when no attribute with the Title role is found */
  public final static String ATTRIBUTE_SOCIALDESCRIPTION = "SocialDescription";
  
  /* permission */
  public final static String PERMISSION_POST = "jpsocial_post";
  
  /* Managers */
  public final static String TWITTER_CLIENT_MANAGER = "jpsocialTwitterManager";
  public final static String FACEBOOK_CLIENT_MANAGER = "jpsocialFacebookManager";
  
  /* Session paramters name */
  public final static String SESSION_PARAM_TWITTER = "jpsocial_session_" + PROVIDER_TWITTER;
  public final static String SESSION_PARAM_FACEBOOK = "jpsocial_session_" + PROVIDER_FACEBOOK;
  public final static String SESSION_PARAM_GOOGLE = "jpsocial_session_" + PROVIDER_GOOGLE;
  public final static String SESSION_PARAM_YOUTUBE = "jpsocial_session_" + PROVIDER_YOUTUBE;
  public final static String SESSION_PARAM_LINKEDIN = "jpsocial_session_" + PROVIDER_LINKEDIN;
  
  /* queue elements to post */
  public final static String SESSION_PARAM_PENDING = "jpsocial_session_pending";
  
  /* supported services */
  public final String SUPPORTED_PROVIDERS[] = {PROVIDER_TWITTER, PROVIDER_FACEBOOK, PROVIDER_YOUTUBE, PROVIDER_GOOGLE, PROVIDER_LINKEDIN};
  
  /* attributes type supported */
  public final static int TYPE_TEXT = 0;
  public final static int TYPE_IMAGE = 1;
  public final static int TYPE_VIDEO = 2;
  
  /* return point  */
  public final static int ORIGIN_EDIT = 1;
  public final static int ORIGIN_LIST = 2;
  
  /* showlet code */
  public final static String SHOWLET_LOGIN_TWITTER = "jpsocial_twlogin";
  public final static String SHOWLET_LOGIN_FACEBOOK = "jpsocial_fblogin";
  public final static String SHOWLET_LOGIN_GOOGLE = "jpsocial_gologin";

  /* Enforcer servlets -- sevlets that provides authentication on redir */ 
 public final static String SERVLET_TWITTER = "TwitterEnforcer";
 public final static String SERVLET_FACEBOOK = "FacebookEnforcer";
 public final static String SERVLET_LINKEDIN = "LinkedInEnforcer";
 public final static String SERVLET_YOUTUBE = "YoutubeEnforcer";
 
 
 
 
 /* misc */
 public static String VIDEO_FILE_FORMAT = "video/*";
 public final static String VAR_AUTH = "authorizedUser";
 public final static String DEFAULT_CATEGORY = "News";
 public final static String NO_LOGIN = "noLogin";
 
 public enum PROVIDER {facebook, twitter}
 
 public static String SOCIAL_ACTION_NAMESPACE = "/do/jpsocial/Front/Share";
  
}
