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
package org.entando.entando.plugins.jpemailmarketing.aps.internalservlet.subscriber;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.ISubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.entando.entando.plugins.jpemailmarketing.apsadmin.portal.specialwidget.course.CourseConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.url.URLManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;

public class SubscriberManageFrontAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SubscriberManageFrontAction.class);

	public String viewFileAndActivateCourse() {
		try {
			Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getEmail(), this.getToken());
			if (null != subscriber) {
				Form form = this.getFormManager().getForm(subscriber.getCourseId());
				int courseId = subscriber.getCourseId();
				if (subscriber.getStatus().equalsIgnoreCase(Subscriber.STATUS_UNCONFIRMED)) {
					InputStream is = this.getFormManager().getIncentive(courseId);
					this.setDownloadInputStream(is);
					this.setDownloadFileName(form.getFileIncentiveName());
					this.setDownloadContentType(URLConnection.guessContentTypeFromStream(is));
					subscriber.setStatus(Subscriber.STATUS_CONFIRMED);
					this.getSubscriberManager().confirmSubscriber(subscriber);
					return SUCCESS;
				} else {
					_logger.warn("The subscriber with id:{} and email {} cannot be activated. Current status is {}", subscriber.getId(), subscriber.getEmail(), subscriber.getStatus());
					InputStream is = this.getFormManager().getIncentive(courseId);
					this.setDownloadInputStream(is);
					this.setDownloadFileName(form.getFileIncentiveName());
					this.setDownloadContentType(URLConnection.guessContentTypeFromStream(is));
					return SUCCESS;
				}
			} else {
				_logger.warn("No subscriber found with email {} and token {}", this.getEmail(), this.getToken());
			}

		} catch (Throwable t) {
			_logger.error("Error on activation of subscriber {} and token {}", this.getEmail(), this.getToken(), t);
			return null;
		}
		return null;
	}

	/**
	 * fa la rimozione.
	 * se il widget del form è ancora pubblicato, farà il redirect al'action di unsubscribe ../jpemailmarketing/Form/unsubscribe che rieseguirà l'azione. 
	 * altrimenti viene fatto il redirect alla home del portale
	 * @return
	 */
	public String unsubscribe() {
		try {
			Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getEmail(), this.getToken());
			if (null != subscriber) {

				if (subscriber.getStatus().equalsIgnoreCase(Subscriber.STATUS_CONFIRMED)) {
					subscriber.setStatus(Subscriber.STATUS_UNSUBSCRIBED);
					this.getSubscriberManager().unsubscribeSubscriber(subscriber);
				} else {
					_logger.warn("The subscriber with id:{} and email {} cannot be unsubscribed. Current status is {}", subscriber.getId(), subscriber.getEmail(), subscriber.getStatus());
				}
			} else {
				_logger.warn("No subscriber found with email {} and token {}", this.getEmail(), this.getToken());
			}
		} catch (Throwable t) {
			_logger.error("Error on unsubscribe subscriber {} and token {}", this.getEmail(), this.getToken(), t);
			return null;
		}
		return SUCCESS;
	}

	public String getRedirectURL() throws ApsSystemException {
		String redirPage = this.getRedirectPage();
		if (StringUtils.isNotBlank(redirPage)) {
			return redirPage;
		}
		return this.getHomeURL();
	}

	protected String getRedirectPage() throws ApsSystemException {
		Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getEmail(), this.getToken());
		if (null == subscriber) return this.getHomeURL();
		String widgetTypeCode = "jpemailmarketingForm";
		int courseId = subscriber.getCourseId();
		List<IPage> pages = this.getPageManager().getWidgetUtilizers(widgetTypeCode);
		pages = this.filterByConfigParamValue(pages, widgetTypeCode, "courseId", new Integer(courseId).toString());

		if (pages.isEmpty()) {
			return null;
		} else {
			IPage page = pages.get(0);

			int frame = 0;
			Widget[] showlets = page.getWidgets();
			for (int i = 0; i < showlets.length; i++) {
				Widget currentWidget = showlets[i];
				if (null != currentWidget && currentWidget.getType().getCode().equals(widgetTypeCode)) {
					frame = i;
					break;
				}
			}
			String baseUrl = this.getUrlManager().createUrl(page, this.getLangManager().getDefaultLang(), null);
			StringBuffer sbBuffer = new StringBuffer(baseUrl).append("?").append("email=").append(subscriber.getEmail()).append("&token=").append(this.getToken()).append("&internalServletFrameDest=").append(frame).append("&internalServletActionPath=/ExtStr2/do/FrontEnd/jpemailmarketing/Form/unsubscribe.action");
			return sbBuffer.toString();
		}
	}

	private String getHomeURL() {
		return this.getUrlManager().createUrl(this.getPageManager().getRoot(), this.getLangManager().getDefaultLang(), null);
	}


	protected List<IPage> filterByConfigParamValue(List<IPage> pages, String widgetTypeCode, String paramName, String paramValue) {
		List<IPage> filteredPages = new ArrayList<IPage>();
		Iterator<IPage> it = pages.iterator();
		while (it.hasNext()) {
			IPage currentPage = it.next();
			Widget[] showlets = currentPage.getWidgets();
			for (int i = 0; i < showlets.length; i++) {
				Widget currentWidget = showlets[i];
				if (null != currentWidget && currentWidget.getType().getCode().equals(widgetTypeCode)) {
					ApsProperties config = currentWidget.getConfig();
					if (null != config) {
						String value = config.getProperty(paramName);
						if (StringUtils.isNotBlank(value) && value.equals(paramValue)) {
							filteredPages.add(currentPage);
						}
					}
				}
			}
		}
		return filteredPages;
	}


	protected ISubscriberManager getSubscriberManager() {
		return _subscriberManager;
	}
	public void setSubscriberManager(ISubscriberManager subscriberManager) {
		this._subscriberManager = subscriberManager;
	}

	public String getEmail() {
		return _email;
	}
	public void setEmail(String email) {
		this._email = email;
	}

	public String getToken() {
		return _token;
	}
	public void setToken(String token) {
		this._token = token;
	}


	public String getDownloadFileName() {
		return _downloadFileName;
	}
	public void setDownloadFileName(String downloadFileName) {
		this._downloadFileName = downloadFileName;
	}

	public String getDownloadContentType() {
		return _downloadContentType;
	}
	public void setDownloadContentType(String downloadContentType) {
		this._downloadContentType = downloadContentType;
	}

	public InputStream getDownloadInputStream() {
		return _downloadInputStream;
	}
	public void setDownloadInputStream(InputStream downloadInputStream) {
		this._downloadInputStream = downloadInputStream;
	}

	protected IFormManager getFormManager() {
		return _formManager;
	}
	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public URLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(URLManager urlManager) {
		this._urlManager = urlManager;
	}


	private String _email;
	private String _token;

	private String _downloadFileName;
	private String _downloadContentType;
	private InputStream _downloadInputStream;

	private ISubscriberManager _subscriberManager;
	private IFormManager _formManager;
	private IPageManager _pageManager;
	private URLManager _urlManager;
}
