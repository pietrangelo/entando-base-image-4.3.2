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
package org.entando.entando.plugins.jpmultisite.apsadmin.multisite;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.category.helper.ICategoryActionHelper;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultisiteAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteAction.class);

	@Override
	public void validate() {
		super.validate();
		if (this.getStrutsAction() == ApsAdminSystemConstants.ADD || this.getStrutsAction() == ApsAdminSystemConstants.PASTE) {
			this.checkDuplicatedUser();
			if (StringUtils.isBlank(this.getMultisiteAdminPassword())) {
				this.addFieldError("multisiteAdminPassword", this.getText("requiredstring"));
			}
			Multisite loadMultisite = null;
			try{
				loadMultisite = this.getMultisiteManager().loadMultisite(this.getCode());
			} catch (Throwable t) {
				this.addActionError("Error loading multisite on validation");
			}
			if(loadMultisite != null){
				String[] args = {this.getCode()};
				this.addFieldError("code", this.getText("jpmultisite.error.duplicateCode",args));
			}
		}
		if (null != this.getHeaderImageFileContentType() && !StringUtils.contains(this.getHeaderImageFileContentType(), "image")) {
			this.addFieldError("headerImage", this.getText("jpmultisite.multisite.message.error.invalidFileType"));
		}
		if (null != this.getTemplateCssFileContentType() && !StringUtils.contains(this.getTemplateCssFileContentType(), "text/css")) {
			this.addFieldError("templateCss", this.getText("jpmultisite.multisite.message.error.invalidFileType"));
		}

		ApsProperties titles = this.loadProperties("titles", true);
		this.getMultisite().setTitles(titles);
		ApsProperties descriptions = this.loadProperties("descriptions", false);
		this.getMultisite().setDescriptions(descriptions);
	}

	protected void checkDuplicatedUser() {
		String username = this.getMultisiteAdminUsername();
		try {
			if (this.existsUser(username)) {
				String[] args = {username};
				this.addFieldError("username", this.getText("error.user.duplicateUser", args));
			}
		} catch (Throwable t) {
			_logger.error("Error checking duplicate user '{}'", username, t);
			//ApsSystemUtils.logThrowable(t, this, "checkDuplicatedUser", "Error checking duplicate user '" + username + "'");
		}
	}

	protected boolean existsUser(String username) throws Throwable {
		return (username != null && username.trim().length() >= 0 && null != this.getUserManager().getUser(username));
	}

	private ApsProperties loadProperties(String prefix, boolean validate) {
		ApsProperties properties = new ApsProperties();
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		HttpServletRequest request = this.getRequest();
		while (langsIter.hasNext()) {
			Lang lang = langsIter.next();
			String code = lang.getCode();
			String paramName = prefix + "_" + code;
			String paramValue = request.getParameter(paramName);
			if (null != paramValue && paramValue.trim().length() > 0) {
				properties.put(code, paramValue);
			} else {
				if (validate) {
					String[] args = {lang.getDescr()};
					this.addFieldError(paramName, this.getText("jpmultisite.multisite.message.error." + prefix + ".missingValue", args));
				}
			}
		}
		return properties;
	}

	public String newMultisite() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newMultisite", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String edit() {
		try {
			Multisite multisite = this.getMultisiteManager().loadMultisite(this.getCode());
			if (null == multisite) {
				this.addActionError(this.getText("error.multisite.null"));
				return INPUT;
			}
			this.setMultisite(multisite);
			this.populateForm(multisite,false);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String entryClone() {
		try {
			Multisite multisite = this.getMultisiteManager().loadMultisite(this.getCode());
			this.setMultisiteCloneSource(this.getCode());
			if (null == multisite) {
				this.addActionError(this.getText("error.multisite.null"));
				return INPUT;
			}
			this.setMultisite(multisite);
			this.populateForm(multisite,true);
			this.setStrutsAction(ApsAdminSystemConstants.PASTE);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		User user = null;
		try {
			Multisite multisite = this.fillMultisite();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				user = new User();
				user.setUsername(this.getMultisiteAdminUsername());
				user.setPassword(this.getMultisiteAdminPassword());
				this.getMultisiteManager().addMultisite(multisite, user);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				if (StringUtils.isBlank(this.getMultisiteAdminPassword())) {
					user = null;
				} else {
					user = new User();
					user.setUsername(this.getMultisiteAdminUsername());
					user.setPassword(this.getMultisiteAdminPassword());
				}
				this.getMultisiteManager().updateMultisite(multisite, user);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String clone() {
		User user = null;
		try {
			Multisite sourceMultisite = this.getMultisiteManager().loadMultisite(this.getMultisiteCloneSource());	
			Multisite multisite = this.fillMultisite();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.PASTE == strutsAction) {
				user = new User();
				user.setUsername(this.getMultisiteAdminUsername());
				user.setPassword(this.getMultisiteAdminPassword());
				this.getMultisiteManager().cloneMultisite(sourceMultisite, multisite, user);
			} 
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void saveFiles(Multisite multisite) throws ApsSystemException {
		saveCss(multisite);
		saveHeaderImage(multisite);
	}

	private void saveCss(Multisite multisite) throws ApsSystemException {
		try {
			if (this.getTemplateCssFile() != null) {
				String templateCss = JpmultisiteSystemConstants.MULTISITE_SUBPATH_RESOURCES_ROOT + JpmultisiteSystemConstants.MULTISITE_CSS_FILENAME + this.getCode() + "." + FilenameUtils.getExtension(this.getTemplateCssFileFileName());
				this.getStorageManager().saveFile(templateCss, false, new FileInputStream(this.getTemplateCssFile()));
				multisite.setTemplateCss(templateCss);
			}
		} catch (Throwable t) {
			_logger.error("error saving css", t);
		}
	}

	private void saveHeaderImage(Multisite multisite) throws ApsSystemException {
		try {
			if (this.getHeaderImageFile() != null) {
				String headerImage = JpmultisiteSystemConstants.MULTISITE_SUBPATH_RESOURCES_ROOT + JpmultisiteSystemConstants.MULTISITE_HEADER_IMAGE_FILENAME + this.getCode() + "." + FilenameUtils.getExtension(this.getHeaderImageFileFileName());
				this.getStorageManager().saveFile(headerImage, false, new FileInputStream(this.getHeaderImageFile()));
				multisite.setHeaderImage(headerImage);
			}
		} catch (Throwable t) {
			_logger.error("error saving header", t);
		}
	}

	public String trash() {
		try {
			Multisite multisite = this.getMultisiteManager().loadMultisite(this.getCode());
			if (null == multisite) {
				this.addActionError(this.getText("error.multisite.null"));
				return INPUT;
			}
			this.populateForm(multisite,false);
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
				this.getMultisiteManager().deleteMultisite(this.getCode());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String view() {
		try {
			Multisite multisite = this.getMultisiteManager().loadMultisite(this.getCode());
			if (null == multisite) {
				this.addActionError(this.getText("error.multisite.null"));
				return INPUT;
			}
			this.populateForm(multisite,false);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void populateForm(Multisite multisite, boolean clone) throws Throwable {
		if(!clone){
			this.setCode(multisite.getCode());
		}
		this.setUrl(multisite.getUrl());
		this.setHeaderImage(multisite.getHeaderImage());
		this.setTemplateCss(multisite.getTemplateCss());
		this.setCatCode(multisite.getCatCode());

	}

	private Multisite fillMultisite() throws ApsSystemException {
		Multisite loadedMultisite = this.getMultisiteManager().loadMultisite(this.getCode());
		Multisite multisite =  (loadedMultisite != null)? loadedMultisite: this.getMultisite();
		multisite.setCode(this.getCode());
		multisite.setUrl(this.getUrl());
		multisite.setCatCode(this.getCode());
		multisite.setTitles(this.getMultisite().getTitles());
		multisite.setDescriptions(this.getMultisite().getDescriptions());
		saveFiles(multisite);
		return multisite;
	}

	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}

	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		this._code = code;
	}

	public String getUrl() {
		return _url;
	}

	public void setUrl(String url) {
		this._url = url;
	}

	protected IMultisiteManager getMultisiteManager() {
		return _multisiteManager;
	}

	public void setMultisiteManager(IMultisiteManager multisiteManager) {
		this._multisiteManager = multisiteManager;
	}

	public Multisite getMultisite() {
		return _multisite;
	}

	public void setMultisite(Multisite multisite) {
		this._multisite = multisite;
	}

	public String getCatCode() {
		return _catCode;
	}

	public void setCatCode(String catCode) {
		this._catCode = catCode;
	}

	public ICategoryActionHelper getHelper() {
		return _helper;
	}

	public void setHelper(ICategoryActionHelper helper) {
		this._helper = helper;
	}

	public ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	public String getMultisiteAdminUsername() {
		_multisiteAdminUsername = SystemConstants.ADMIN_USER_NAME + JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR + this.getCode();
		return _multisiteAdminUsername;
	}

	public void setMultisiteAdminUsername(String multisiteAdminUsername) {
		this._multisiteAdminUsername = multisiteAdminUsername;
	}

	public String getMultisiteAdminPassword() {
		return _multisiteAdminPassword;
	}

	public void setMultisiteAdminPassword(String multisiteAdminPassword) {
		this._multisiteAdminPassword = multisiteAdminPassword;
	}

	public IUserManager getUserManager() {
		return _userManager;
	}

	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	public File getHeaderImageFile() {
		return _headerImageFile;
	}

	public void setHeaderImageFile(File headerImageFile) {
		this._headerImageFile = headerImageFile;
	}

	public File getTemplateCssFile() {
		return _templateCssFile;
	}

	public void setTemplateCssFile(File templateCssFile) {
		this._templateCssFile = templateCssFile;
	}

	public IStorageManager getStorageManager() {
		return storageManager;
	}

	public void setStorageManager(IStorageManager storageManager) {
		this.storageManager = storageManager;
	}

	public String getHeaderImage() {
		return _headerImage;
	}

	public void setHeaderImage(String headerImage) {
		this._headerImage = headerImage;
	}

	public String getTemplateCss() {
		return _templateCss;
	}

	public void setTemplateCss(String templateCss) {
		this._templateCss = templateCss;
	}

	public String getHeaderImageFileFileName() {
		return _headerImageFileFileName;
	}

	public void setHeaderImageFileFileName(String headerImageFileFileName) {
		this._headerImageFileFileName = headerImageFileFileName;
	}

	public String getHeaderImageFileContentType() {
		return _headerImageFileContentType;
	}

	public void setHeaderImageFileContentType(String headerImageFileContentType) {
		this._headerImageFileContentType = headerImageFileContentType;
	}

	public String getTemplateCssFileFileName() {
		return _templateCssFileFileName;
	}

	public void setTemplateCssFileFileName(String templateCssFileFileName) {
		this._templateCssFileFileName = templateCssFileFileName;
	}

	public String getTemplateCssFileContentType() {
		return _templateCssFileContentType;
	}

	public void setTemplateCssFileContentType(String templateCssFileContentType) {
		this._templateCssFileContentType = templateCssFileContentType;
	}

	public String getMultisiteCloneSource() {
		return _multisiteCloneSource;
	}

	public void setMultisiteCloneSource(String multisiteCloneSource) {
		this._multisiteCloneSource = multisiteCloneSource;
	}
	
	private Multisite _multisite = new Multisite();

	private int _strutsAction;
	private String _code;
	private String _url;

	private String _headerImage;
	private File _headerImageFile;
	private String _headerImageFileFileName;
	private String _headerImageFileContentType;

	private String _templateCss;
	private File _templateCssFile;
	private String _templateCssFileFileName;
	private String _templateCssFileContentType;

	private String _catCode;
	
	private String _multisiteCloneSource;

	private String _multisiteAdminUsername;
	private String _multisiteAdminPassword;

	private ICategoryManager _categoryManager;

	private IMultisiteManager _multisiteManager;
	private ICategoryActionHelper _helper;
	private IUserManager _userManager;

	private IStorageManager storageManager;
}
