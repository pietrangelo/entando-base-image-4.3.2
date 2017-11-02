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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.form;

import java.io.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Date;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpemailmarketing.apsadmin.form.model.FormUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

public class FormAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(FormAction.class);

	@Override
	public void validate() {
		super.validate();
	}

	public String newForm() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newForm", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String edit() {
		try {
			Form form = this.getFormManager().getForm(this.getCourseId());
			if (null == form) {
				this.addActionError(this.getText("error.form.null"));
				return INPUT;
			}
			this.populateForm(form);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			FormUpload fileCover = FormUpload.create(this.getCover(), this.getCoverFileName(), this.getCoverContentType());
			FormUpload foleIncentive = FormUpload.create(this.getIncentive(), this.getIncentiveFileName(), this.getIncentiveContentType());

			Form form = this.createForm();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getFormManager().addForm(form, fileCover, foleIncentive);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getFormManager().updateForm(form, fileCover, foleIncentive);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Delete the file and update the modified date.
	 * 
	 * @return
	 */
	public String deleteCover() {
		try {
			Form current = this.createForm();
			current.setFileCoverName(null);
			this.populateForm(current);
		} catch (Throwable t) {
			_logger.error("error in deleteCover", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String deleteIncentive() {
		try {
			Form current = this.createForm();
			current.setFileIncentiveName(null);
			this.populateForm(current);
		} catch (Throwable t) {
			_logger.error("error in deleteIncentive", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String viewFile() {
		try {
			String type = super.getParameter("type");
			return this.viewFile(type);
		} catch (Throwable t) {
			_logger.error("Error in view file", t);
			return FAILURE;
		}
	}

	/**
	 * WARINIG: just because there is one attach per type!
	 * @param type
	 * @return
	 */
	public String viewFile(String type) {
		try {
			Form form = this.getFormManager().getForm(this.getCourseId());
			if (null == form) {
				this.addActionError(this.getText("error.form.null"));
				return INPUT;
			}
			if (type.equalsIgnoreCase("cover")) {
				InputStream is = this.getFormManager().getCover(form.getCourseId());
				this.setDownloadInputStream(is);
				this.setDownloadFileName(form.getFileCoverName());
				this.setDownloadContentType(URLConnection.guessContentTypeFromStream(is));
			} else if (type.equalsIgnoreCase("incentive")) {
				InputStream is = this.getFormManager().getIncentive(form.getCourseId());
				this.setDownloadInputStream(is);
				this.setDownloadFileName(form.getFileIncentiveName());
				this.setDownloadContentType(URLConnection.guessContentTypeFromStream(is));
			}
		} catch (Throwable t) {
			_logger.error("Error in view file of type {}", type, t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trash() {
		try {
			Form form = this.getFormManager().getForm(this.getCourseId());
			if (null == form) {
				this.addActionError(this.getText("error.form.null"));
				return INPUT;
			}
			this.populateForm(form);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String delete() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getFormManager().deleteForm(this.getCourseId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String view() {
		try {
			Form form = this.getFormManager().getForm(this.getCourseId());
			if (null == form) {
				this.addActionError(this.getText("error.form.null"));
				return INPUT;
			}
			this.populateForm(form);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void populateForm(Form form) throws Throwable {
		this.setCourseId(form.getCourseId());
		this.setFileCoverName(form.getFileCoverName());
		this.setFileIncentiveName(form.getFileIncentiveName());
		this.setEmailSubject(form.getEmailSubject());
		this.setEmailText(form.getEmailText());
		this.setEmailButton(form.getEmailButton());
		this.setEmailMessageFriendly(form.getEmailMessageFriendly());
		this.setCreatedat(form.getCreatedat());
		this.setUpdatedat(form.getUpdatedat());
	}

	private Form createForm() {
		Form form = new Form();
		form.setCourseId(this.getCourseId());
		form.setFileCoverName(this.getFileCoverName());
		form.setFileIncentiveName(this.getFileIncentiveName());
		form.setEmailSubject(this.getEmailSubject());
		form.setEmailText(this.getEmailText());
		form.setEmailButton(this.getEmailButton());
		form.setEmailMessageFriendly(this.getEmailMessageFriendly());
		form.setCreatedat(this.getCreatedat());
		form.setUpdatedat(this.getUpdatedat());
		return form;
	}

	public Course getCourse(int id) {
		Course course = null;
		try {
			course = this.getCourseManager().getCourse(id);
		} catch (Throwable t) {
			_logger.error("Error getting course with id {}", id, t);
			throw new RuntimeException("Error getting course with id " + id, t);
		}
		return course;
	}

	public Lang getDefaultLang() {
		return this.getLangManager().getDefaultLang();
	}

	public ApsProperties getLabel(String key, int campaingId) {
		ApsProperties props = null;
		try {
			key = key + "_" + campaingId;
			props = this.getI18nManager().getLabelGroup(key);
		} catch (Throwable t) {
			_logger.error("Error getting label with key {}", key, t);
			throw new RuntimeException("Error getting label with key " + key, t);
		}
		return props;		
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	public String getFileCoverName() {
		return _fileCoverName;
	}
	public void setFileCoverName(String fileCoverName) {
		this._fileCoverName = fileCoverName;
	}

	public String getFileIncentiveName() {
		return _fileIncentiveName;
	}
	public void setFileIncentiveName(String fileIncentiveName) {
		this._fileIncentiveName = fileIncentiveName;
	}

	public String getEmailSubject() {
		return _emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this._emailSubject = emailSubject;
	}

	public String getEmailText() {
		return _emailText;
	}
	public void setEmailText(String emailText) {
		this._emailText = emailText;
	}

	public String getEmailButton() {
		return _emailButton;
	}
	public void setEmailButton(String emailButton) {
		this._emailButton = emailButton;
	}

	public String getEmailMessageFriendly() {
		return _emailMessageFriendly;
	}
	public void setEmailMessageFriendly(String emailMessageFriendly) {
		this._emailMessageFriendly = emailMessageFriendly;
	}

	public Date getCreatedat() {
		return _createdat;
	}
	public void setCreatedat(Date createdat) {
		this._createdat = createdat;
	}

	public Date getUpdatedat() {
		return _updatedat;
	}
	public void setUpdatedat(Date updatedat) {
		this._updatedat = updatedat;
	}

	/* upload file c */
	public File getCover() {
		return cover;
	}
	public void setCover(File cover) {
		this.cover = cover;
	}

	public String getCoverContentType() {
		return coverContentType;
	}
	public void setCoverContentType(String coverContentType) {
		this.coverContentType = coverContentType;
	}

	public String getCoverFileName() {
		return coverFileName;
	}
	public void setCoverFileName(String coverFileName) {
		this.coverFileName = coverFileName;
	}


	/* upload file i */
	public File getIncentive() {
		return incentive;
	}
	public void setIncentive(File incentive) {
		this.incentive = incentive;
	}

	public String getIncentiveContentType() {
		return incentiveContentType;
	}
	public void setIncentiveContentType(String incentiveContentType) {
		this.incentiveContentType = incentiveContentType;
	}

	public String getIncentiveFileName() {
		return incentiveFileName;
	}
	public void setIncentiveFileName(String incentiveFileName) {
		this.incentiveFileName = incentiveFileName;
	}

	/* download */
	//	public File getDownload() {
	//		return _download;
	//	}
	//	public void setDownload(File download) {
	//		this._download = download;
	//	}

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

	protected ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}

	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}

	private int _strutsAction;
	private int _courseId;
	private String _fileCoverName;
	private String _fileIncentiveName;
	private String _emailSubject;
	private String _emailText;
	private String _emailButton;
	private String _emailMessageFriendly;
	private Date _createdat;
	private Date _updatedat;

	private File cover;
	private String coverContentType;
	private String coverFileName;
	private File incentive;
	private String incentiveContentType;
	private String incentiveFileName;

	//private File _download;
	private String _downloadFileName;
	private String _downloadContentType;
	private InputStream _downloadInputStream;

	private IFormManager _formManager;
	private ICourseManager _courseManager;
	private II18nManager _i18nManager;

}