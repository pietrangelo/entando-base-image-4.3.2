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
package com.agiletec.plugins.jpforum.apsadmin.rss;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jpforum.aps.internalservlet.actions.AbstractForumAction;
import com.agiletec.plugins.jpforum.aps.system.services.section.Section;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

public class ForumRssAction extends AbstractForumAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ForumRssAction.class);

	/**
	 * Shows the rss for a given thread
	 * The rss for a thread loads all the posts
	 * @return
	 */
	public String showThread() {
		try {
			Thread thread = this.checkThread();
			Section section = this.checkSection();
			if (section == null) return null;
			String feedLink = this.getFeedLink();
			SyndFeed syndFeed = new SyndFeedImpl();
			syndFeed.setFeedType(FEED_TYPE_RSS_20);
			syndFeed.setLink(feedLink);

			this.buildSyndFeedForThread(syndFeed, thread, section, feedLink);
			this.setSyndFeed(syndFeed);
			this.setFeedType(syndFeed.getFeedType());

		} catch (Throwable t) {
			_logger.error("error in showThread", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Shows the rss for a given section
	 * For each thread inside the section, the rss will show the last post
	 * The rss for a thread loads all the posts
	 * @return
	 */
	public String showSection() {
		try {
			Section section = this.checkSection();
			if (section == null) return null;
			String feedLink = this.getFeedLink();
			SyndFeed syndFeed = new SyndFeedImpl();
			syndFeed.setFeedType(FEED_TYPE_RSS_20);
			syndFeed.setLink(feedLink);

			List<Integer> threads = this.getThreadManager().getThreads(this.getSectionId(), null);
			this.buildSyndFeedForSection(syndFeed, threads, section, feedLink);

			this.setSyndFeed(syndFeed);
			this.setFeedType(syndFeed.getFeedType());

		} catch (Throwable t) {
			_logger.error("error in show", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Checks and return a section.
	 * A section is valid if the user is allowed to.
	 * @return
	 */
	private Section checkSection() {
		Section section = this.getSectionManager().getSection(this.getSectionId());
		if (null != section) {
			section = this.getWrappedSection(section);
		}
		return section;
	}

	/**
	 * Check and returns a thread.
	 * @return
	 */
	private Thread checkThread() {
		Thread thread = null;
		if (null != this.getThreadId()) {
			thread = this.getThread(this.getThreadId());
			if (null != thread) {
				this.setSectionId(thread.getSectionId());
			}
		}
		return thread;
	}


	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//

	public void buildSyndFeedForSection(SyndFeed feed, List<Integer> threads, Section section, String feedLink) throws ApsSystemException {
		String title = section.getTitle(this.getLangCode());
		if (null == title || title.trim().length() == 0) {
			title = section.getTitle(this.getLangManager().getDefaultLang().getCode());
		}
		String sectionDescr = section.getDescription(this.getLangCode());
		if (null == sectionDescr || sectionDescr.trim().length() == 0) {
			sectionDescr = section.getDescription(this.getLangManager().getDefaultLang().getCode());
		}

		feed.setTitle(title);
		feed.setDescription(sectionDescr);

		feed.setEntries(this.getEntriesForSection(threads, feedLink));

	}

	public void buildSyndFeedForThread(SyndFeed feed, Thread thread, Section section, String feedLink) throws ApsSystemException {
		String title = thread.getPost().getTitle();
		if (null == title || title.trim().length() == 0) {
			title = section.getTitle(this.getLangManager().getDefaultLang().getCode());
		}
		String sectionDescr = section.getDescription(this.getLangCode());
		if (null == sectionDescr || sectionDescr.trim().length() == 0) {
			sectionDescr = section.getDescription(this.getLangManager().getDefaultLang().getCode());
		}

		feed.setTitle(title);
		feed.setDescription(sectionDescr);

		feed.setEntries(this.getEntriesForThread(thread, feedLink));

	}


	private List<SyndEntry> getEntriesForSection(List<Integer> threadList, String feedLink) throws ApsSystemException {
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		Iterator<Integer> threadIterator = threadList.iterator();
		while (threadIterator.hasNext()) {
			Thread thread = this.getThreadManager().getThread(threadIterator.next());
			List<Integer> posts = thread.getPostCodes();
			Integer postId = posts.get(posts.size()-1);
			Post currentPost = this.getThreadManager().getPost(postId);
			entries.add(this.createEntry(thread.getPost().getTitle(), currentPost, feedLink));
		}
		return entries;
	}

	private List<SyndEntry> getEntriesForThread(Thread thread, String feedLink) throws ApsSystemException {
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		List<Integer> posts = thread.getPostCodes();
		Iterator<Integer> postsIt = posts.iterator();
		while (postsIt.hasNext()) {
			Post currentPost = this.getThreadManager().getPost(postsIt.next());
			entries.add(this.createEntry(currentPost.getTitle(), currentPost, feedLink));
		}
		return entries;
	}


	private SyndEntry createEntry(String title, Post post, String feedLink) throws ApsSystemException {
		SyndEntry entry = new SyndEntryImpl();
		try {
			entry.setTitle(title);
			SyndContent description = new SyndContentImpl();
			description.setType(SYNDCONTENT_TYPE_TEXTHTML);
			description.setValue(this.getMarkupParser().XMLtoHTML(post.getText()));
			entry.setDescription(description);

			String link = this.createLink(post, feedLink);
			entry.setLink(link);

			entry.setPublishedDate(post.getPostDate());

		} catch (Throwable t) {
			_logger.error("error in createEntry", t);
			throw new ApsSystemException("Error in createEntry", t);
		}
		return entry;
	}

	private String createLink(Post post, String feedLink) {
		SymbolicLink symbolicLink = new SymbolicLink();
		StringBuffer sbBuffer = new StringBuffer(feedLink);
		sbBuffer.append("?post=");
		sbBuffer.append(post.getCode());
		sbBuffer.append("#jpforumPost" + post.getCode());
		symbolicLink.setDestinationToUrl(sbBuffer.toString());
		return symbolicLink.getUrlDest();
	}

	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//

	public String getForumPortalPage(String showletTypeCode)  {
		String pageCode = null;
		try {
			List<IPage> pages = new ArrayList<IPage>();

			IPage root = this.getPageManager().getRoot();
			this.getShowletUtilizers(root, showletTypeCode, pages);
			if (!pages.isEmpty()) {
				pageCode = pages.get(0).getCode();
			}
		} catch (Throwable t) {
			_logger.error("Error during searching page utilizers of showlet with code {}", showletTypeCode, t);
			String message = "Error during searching page utilizers of showlet with code " + showletTypeCode;
			throw new RuntimeException(message, t);
		}
		return pageCode;
	}

	private void getShowletUtilizers(IPage page, String showletTypeCode, List<IPage> showletUtilizers) {
		Widget[] widgets = page.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			Widget widget = widgets[i];
			if (null != widget && null != widget.getType() && showletTypeCode.equals(widget.getType().getCode())) {
				showletUtilizers.add(page);
				break;
			}
		}
		IPage[] children = page.getChildren();
		for (int i = 0; i < children.length; i++) {
			IPage child = children[i];
			this.getShowletUtilizers(child, showletTypeCode, showletUtilizers);
		}
	}

	private String getFeedLink() {
		String feedLink = "";
		String baseUrl = this.getBaseConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		feedLink = baseUrl;
		String pagecode = this.getForumPortalPage("jpforum_forum");
		return feedLink + this.getLangCode() + "/" + pagecode + ".wp" ;
	}

	public String getLangCode() {
		String code = this.getLangManager().getDefaultLang().getCode();
		Lang lang = this.getLangManager().getLang(this.getLang());
		if (null != lang) {
			code =  lang.getCode();
		}
		return code;
	}

	public void setSectionId(String sectionId) {
		this._sectionId = sectionId;
	}
	public String getSectionId() {
		return _sectionId;
	}

	public void setThreadId(Integer threadId) {
		this._threadId = threadId;
	}
	public Integer getThreadId() {
		return _threadId;
	}

	public void setSyndFeed(SyndFeed syndFeed) {
		this._syndFeed = syndFeed;
	}
	public SyndFeed getSyndFeed() {
		return _syndFeed;
	}

	public void setFeedType(String feedType) {
		this._feedType = feedType;
	}
	public String getFeedType() {
		return _feedType;
	}

	public void setLang(String lang) {
		this._lang = lang;
	}
	public String getLang() {
		return _lang;
	}

	public void setBaseConfigManager(ConfigInterface baseConfigManager) {
		this._baseConfigManager = baseConfigManager;
	}
	protected ConfigInterface getBaseConfigManager() {
		return _baseConfigManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	protected IPageManager getPageManager() {
		return _pageManager;
	}

	private SyndFeed _syndFeed;
	private String _feedType;
	private String _sectionId;
	private Integer _threadId;
	private String _lang;
	private ConfigInterface _baseConfigManager;
	private IPageManager _pageManager;

	public static final String FEED_TYPE_RSS_20 = "rss_2.0";
	public static final String SYNDCONTENT_TYPE_TEXTHTML = "text/html";


	@Override
	public String[] getBreadCrumbs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Section getCurrentSection() {
		// TODO Auto-generated method stub
		return null;
	}

}
