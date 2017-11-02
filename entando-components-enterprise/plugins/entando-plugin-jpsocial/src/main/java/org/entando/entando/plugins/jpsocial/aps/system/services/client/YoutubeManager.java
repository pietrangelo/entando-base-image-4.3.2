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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.model.YoutubeCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.google.api.client.http.InputStreamContent;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;

/**
 *
 * @author entando
 */
public class YoutubeManager extends AbstractService implements IYoutubeManager {

	private static final Logger _logger =  LoggerFactory.getLogger(YoutubeManager.class);

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
      text =  text.replaceAll("<[^<>]+>", "").trim();
      // get rid of not allowed '<','>'
      text = text.replaceAll("<", "").replaceAll(">", "");
      if (text.length() > maxLength) {
        text = text.substring(0, maxLength);
      }
    }
    return text;
  }

  /**
   * Return a list of acceptable keywords; those in excess or invalid will be ignored
   * @param keywords
   * @return
   */
  private List<String> checkKeywords(List<String> keywords) {
    List<String> result = new ArrayList<String>();
    int count = 0;

    if (null != keywords && !keywords.isEmpty()) {
      Iterator<String> itr = keywords.iterator();

      while (itr.hasNext() && count < KEYWORDS_LENGTH) {
        String keyword = itr.next();

        keyword = checkText(keyword, KEYWORD_LENGTH);
        if (count + keyword.length() < KEYWORDS_LENGTH) {
          count += keyword.length();
          // add two more spaces if the category is formed by two or more words
          if (keyword.contains(" ")) {
            // youtube will infer quotation marks
            count += 2;
          }
          result.add(keyword);
        }
      }
    }
    return result;
  }

  @Override
  public void init() throws Exception {
    _logger.debug("{} ready. Youtube categories loaded {}",this.getClass().getName(), getYtCategories().size() );
  }

  /**
   * Upload a video.
   *
   * @param accessToken
   * @param filePath
   * @param title
   * @param description
   * @param ytCategories
   * @param keywords
   * @param developerTag
   * @param isPrivate
   * @throws ApsSystemException
   */
  @Override
  public void uploadVideo(String accessToken,
          InputStream stream,
          String title,
          String description,
          String ytCategory,
          List<String> keywords,
          List<String> developerTag) throws ApsSystemException {
    VideoEntry newEntry = new VideoEntry();
	File temp;
	try {
		temp = streamToFile(stream);
	} catch (IOException ex) {
		_logger.error("error creating tempFile", ex);
		throw new ApsSystemException("error creating tempFile");
	}
    YouTubeMediaGroup mediaGroup = newEntry.getOrCreateMediaGroup();
    String clientId = this.getConfigManager().getParam(JpSocialSystemConstants.GOOGLE_CONSUMER_KEY_PARAM_NAME);
    String devKey = this.getConfigManager().getParam(JpSocialSystemConstants.YOUTUBE_DEVELOPER_KEY);
    String developerMode = this.getConfigManager().getParam(JpSocialSystemConstants.DEV_MODE_PARAM_NAME);


	InputStreamContent mediaContent = new InputStreamContent(
          JpSocialSystemConstants.VIDEO_FILE_FORMAT, stream);
    // check for fields length
    title = checkText(title, TITLE_LENGTH);
    description = checkText(description, DESCRIPTION_LENGTH);
    keywords = checkKeywords(keywords);
    // create service
    YouTubeService service = new YouTubeService(clientId, devKey);
    service.setAuthSubToken(accessToken);
    try {
      mediaGroup.setTitle(new MediaTitle());
      mediaGroup.getTitle().setPlainTextContent(title);
      mediaGroup.addCategory(new MediaCategory());
      // assign category
      mediaGroup.getCategories().add(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, ytCategory));
      mediaGroup.setKeywords(new MediaKeywords());
      // process keywords
      if (null != keywords && !keywords.isEmpty()) {
        mediaGroup.getKeywords().addKeywords(keywords);
      }
      mediaGroup.setDescription(new MediaDescription());
      mediaGroup.getDescription().setPlainTextContent(description);
      mediaGroup.setPrivate("true".equalsIgnoreCase(developerMode));
//      mediaGroup.setPrivate(true);
      // process developer tag
      if (null != developerTag && !developerTag.isEmpty()) {
        Iterator<String> itr = developerTag.iterator();

        while (itr.hasNext()) {
          String tag = itr.next();

          mediaGroup.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, tag));
        }
      }
//      mediaGroup.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, "mydevtag"));
//      newEntry.setGeoCoordinates(new GeoRssWhere(37.0, -122.0));
      // alternatively, one could specify just a descriptive string
      newEntry.setLocation("Cagliari, CA");
      MediaFileSource ms = new MediaFileSource(temp, "video/*");
      newEntry.setMediaSource(ms);
      VideoEntry createdEntry = service.insert(new URL(UPLOAD_URL), newEntry);
	  temp.delete();
    } catch (Throwable t) {
    	_logger.error("Error uploading a video on youtube", t);
      throw new ApsSystemException("Error uploading a video on youtube", t);
    }
  }

  @Override
  public String getAuthenticationURL(String callbackUrl) {
    String baseURL = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
    String backendURL = baseURL + JpSocialSystemConstants.SERVLET_YOUTUBE + "/" + JpSocialSystemConstants.BACKEND_ACTION;
    String url;

    if (null != callbackUrl && !"".equals(callbackUrl.trim())) {
      url = AuthSubUtil.getRequestUrl(callbackUrl, GDATA_URL, false, true);
    } else {
      url = AuthSubUtil.getRequestUrl(backendURL, GDATA_URL, false, true);
    }
    return url;
  }

  @Override
  public boolean verifyUpload() throws ApsSystemException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<YoutubeCategory> getYoutubeGategories() {
    List<YoutubeCategory> list = new ArrayList<YoutubeCategory>();

    if (null != getYtCategories()) {
      for (Map.Entry<String, String> entry : getYtCategories().entrySet()) {
        YoutubeCategory category = new YoutubeCategory(entry.getKey(), entry.getValue());

        list.add(category);
      }
    }
    return list;
  }

  @Override
  public boolean isYoutubeVideo(String filename) {
    boolean result = false;

    if (null != filename && !"".equals(filename)) {
      int idx = filename.lastIndexOf(".");

      if (idx > 0 && idx < filename.length()) {
        String ext = filename.substring(++idx);

        return this._ytFormats.contains(ext);
      }
    }
    return result;
  }

   private File streamToFile (InputStream in) throws IOException {
        final File tempFile = File.createTempFile("ytv", null);
        tempFile.deleteOnExit();
		FileOutputStream out = new FileOutputStream(tempFile);
		IOUtils.copy(in, out);
        return tempFile;
    }


public ConfigInterface getConfigManager() {
    return _configManager;
  }

  public void setConfigManager(ConfigInterface configManager) {
    this._configManager = configManager;
  }

  public Map<String, String> getYtCategories() {
    return ytCategories;
  }

  public void setYtCategories(Map<String, String> ytCategories) {
    this.ytCategories = ytCategories;
  }

  public String getYtFormats() {
    return _ytFormats;
  }

  public void setYtFormats(String ytFormats) {
    this._ytFormats = ytFormats;
  }

  // managers
  private ConfigInterface _configManager;
  // config items
  private Map<String, String> ytCategories;
  private String _ytFormats;

  final static String UPLOAD_URL = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";
  final static String GDATA_URL = "https://gdata.youtube.com";
  final static int TITLE_LENGTH = 100;
  final static int DESCRIPTION_LENGTH = 5000;
  final static int KEYWORD_LENGTH = 30;
  final static int KEYWORDS_LENGTH = 500;

}
