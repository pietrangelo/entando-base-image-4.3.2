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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.form;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.api.JAXBForm;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.event.FormChangedEvent;
import org.entando.entando.plugins.jpemailmarketing.apsadmin.form.model.FormUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

public class FormManager extends AbstractService implements IFormManager {

	private static final Logger _logger =  LoggerFactory.getLogger(FormManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}

	@Override
	public Form getForm(int courseId) throws ApsSystemException {
		Form form = null;
		try {
			form = this.getFormDAO().loadForm(courseId);
		} catch (Throwable t) {
			_logger.error("Error loading form with courseId '{}'", courseId,  t);
			throw new ApsSystemException("Error loading form with courseId: " + courseId, t);
		}
		return form;
	}

	@Override
	public List<Integer> getForms() throws ApsSystemException {
		List<Integer> forms = new ArrayList<Integer>();
		try {
			forms = this.getFormDAO().loadForms();
		} catch (Throwable t) {
			_logger.error("Error loading Form list",  t);
			throw new ApsSystemException("Error loading Form ", t);
		}
		return forms;
	}

	@Override
	public List<Integer> searchForms(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> forms = new ArrayList<Integer>();
		try {
			forms = this.getFormDAO().searchForms(filters);
		} catch (Throwable t) {
			_logger.error("Error searching Forms", t);
			throw new ApsSystemException("Error searching Forms", t);
		}
		return forms;
	}

	@Override
	public void addForm(Form form) throws ApsSystemException {
		try {
			Date date = new Date();
			form.setCreatedat(date);
			form.setUpdatedat(date);
			//int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			//form.setId(f);
			this.getFormDAO().insertForm(form);
			this.notifyFormChangedEvent(form, FormChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding Form", t);
			throw new ApsSystemException("Error adding Form", t);
		}
	}

	@Override
	public void addForm(Form form, FormUpload fileCover, FormUpload fileIncentive) throws ApsSystemException {
		try {
			Date date = new Date();
			form.setCreatedat(date);
			form.setUpdatedat(date);

			//int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			//form.setId(key);

			if (null != fileCover) form.setFileCoverName(fileCover.getFileName());
			if (null != fileIncentive) form.setFileIncentiveName(fileIncentive.getFileName());

			this.saveFormFiles(form, fileCover, fileIncentive);
			this.getFormDAO().insertForm(form);
			this.notifyFormChangedEvent(form, FormChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			this.deleteFormFiles(form.getCourseId());
			_logger.error("Error adding Form", t);
			throw new ApsSystemException("Error adding Form ", t);
		}
	}

	@Override
	public void updateForm(Form form) throws ApsSystemException {
		try {
			Date date = new Date();
			form.setUpdatedat(date);
			//the fileCoverName and the fileIncentiveName are set here
			this.saveFormFiles(form, null, null);
			this.getFormDAO().updateForm(form);
			this.notifyFormChangedEvent(form, FormChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating Form {}", form.getCourseId(), t);
			throw new ApsSystemException("Error updating Form " + form.getCourseId(), t);
		}
	}

	@Override
	public void updateForm(Form form, FormUpload fileCover, FormUpload fileIncentive) throws ApsSystemException {
		try {
			Date date = new Date();
			form.setUpdatedat(date);

			//the fileCoverName and the fileIncentiveName are set here
			this.saveFormFiles(form, fileCover, fileIncentive);
			this.getFormDAO().updateForm(form);
			this.notifyFormChangedEvent(form, FormChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating Form {}", form.getCourseId(), t);
			throw new ApsSystemException("Error updating Form " + form.getCourseId(), t);
		}
	}

	@Override
	public void deleteForm(int courseId) throws ApsSystemException {
		try {
			Form form = this.getForm(courseId);
			this.getFormDAO().removeForm(courseId);
			this.deleteFormFiles(form.getCourseId());
			this.notifyFormChangedEvent(form, FormChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting Form with courseId {}", courseId, t);
			throw new ApsSystemException("Error deleting Form with courseId:" + courseId, t);
		}
	}

	private void saveFormFiles(Form form, FormUpload fileCover, FormUpload fileIncentive) throws Throwable {
		try {
			if (StringUtils.isBlank(form.getFileCoverName())) {
				this.deleteCoverFile(form.getCourseId());
			} else {
				if (null == this.getFile(form.getCourseId(), FOLDER_NAME_COVER, form.getFileCoverName())) {
					this.deleteCoverFile(form.getCourseId());
					form.setFileCoverName(null);
				}
			}
			if (StringUtils.isBlank(form.getFileIncentiveName())) {
				this.deleteIncentiveFile(form.getCourseId());
			} else {
				if (null == this.getFile(form.getCourseId(), FOLDER_NAME_INCENTIVE, form.getFileIncentiveName())) {
					this.deleteIncentiveFile(form.getCourseId());
					form.setFileIncentiveName(null);
				}
			}
			if (null != fileCover) {
				this.deleteCoverFile(form.getCourseId());
				String folder = this.getCoursesDiskFolder() + form.getCourseId() + File.separator + FOLDER_NAME_COVER + File.separator;
				String path = folder + fileCover.getFileName();
				File destFile = new File(path);
				FileUtils.copyFile(fileCover.getFile(), destFile);
				form.setFileCoverName(fileCover.getFileName());
			}
			if (null != fileIncentive) {
				this.deleteIncentiveFile(form.getCourseId());
				String folder = this.getCoursesDiskFolder() + form.getCourseId() + File.separator + FOLDER_NAME_INCENTIVE + File.separator;
				String path = folder + fileIncentive.getFileName();
				File destFile = new File(path);
				FileUtils.copyFile(fileIncentive.getFile(), destFile);
				form.setFileIncentiveName(fileIncentive.getFileName());
			}
		} catch (Throwable t) {
			_logger.error("Error saving Form files for courseId {}", form.getCourseId(), t);
			throw new ApsSystemException("Error deleting Form with courseId:" + form.getCourseId(), t);
		}
	}

	@Override
	public InputStream getCover(int courseId) throws ApsSystemException {
		try {
			return this.getFile(courseId, FOLDER_NAME_COVER, null);
		} catch (Throwable t) {
			_logger.error("IO error loading cover file for course {}", courseId, t);
			throw new RuntimeException("IO error loading cover file for course  " + courseId, t);
		}
	}

	@Override
	public InputStream getIncentive(int courseId) throws ApsSystemException {
		try {
			return this.getFile(courseId, FOLDER_NAME_INCENTIVE, null);
		} catch (Throwable t) {
			_logger.error("IO error loading incentive file for course {}", courseId, t);
			throw new RuntimeException("IO error loading incentive file for course  " + courseId, t);
		}
	}

	/**
	 * Returns the InputStream of a course related file.
	 * @param courseId the id of the course
	 * @param type can be "cover" or "incentive"
	 * @param fileName optional. If null will be returned the first file, otherwise will be performed an exact search.
	 * @return the InputStream of a course related file
	 * @throws ApsSystemException
	 */
	protected InputStream getFile(int courseId, String type, String fileName) throws ApsSystemException {
		InputStream is = null;
		try {
			String folder = this.getCoursesDiskFolder() + courseId + File.separator + type + File.separator;
			File dir = new File(folder);
			if (dir.exists()) {
				File file = null;
				if (null == fileName) {
					File[] files = dir.listFiles();
					if (null != files && files.length > 0) {
						file = files[0];
					}
				} else {
					file = FileUtils.getFile(dir, fileName);
				}
				if (null != file) is = new FileInputStream(file);
			}
		} catch (Throwable t) {
			_logger.error("error loading {} file for course {}", type, courseId, t);
			throw new RuntimeException("error loading file for course " + courseId, t);
		}
		return is;
	}

	public void deleteFormFiles(int courseId) {
		try {
			String folder = this.getCoursesDiskFolder() + courseId + File.separator;
			FileUtils.deleteQuietly(new File(folder));
		} catch (Throwable t) {
			_logger.error("error deleting files for course {}", courseId, t);
			throw new RuntimeException();
		}
	}

	public void deleteCoverFile(int courseId) {
		try {
			this.deleteFile(courseId, FOLDER_NAME_COVER);
		} catch (Throwable t) {
			_logger.error("error deleting cover folder for course {}", courseId, t);
			throw new RuntimeException("error deleting cover folder for course " + courseId, t);
		}
	}

	public void deleteIncentiveFile(int courseId) {
		try {
			this.deleteFile(courseId, FOLDER_NAME_INCENTIVE);
		} catch (Throwable t) {
			_logger.error("error deleting incentive folder for course {}", courseId, t);
			throw new RuntimeException("error deleting incentive folder for course " + courseId, t);
		}
	}

	protected void deleteFile(int courseId, String type) {
		try {
			String folder = this.getCoursesDiskFolder() + courseId + File.separator + type + File.separator;
			FileUtils.deleteQuietly(new File(folder));
		} catch (Throwable t) {
			throw new RuntimeException("error deleting folder for course " + courseId, t);
		}
	}

	protected String getCoursesDiskFolder() {
		String baseFolder = null;
		try {
			baseFolder = this.getProtectedBaseDiskRootFolder();
			if (!baseFolder.endsWith(File.separator)) baseFolder = baseFolder + File.separator;
			baseFolder = baseFolder + "jpemailmarketing" + File.separator + "courses" + File.separator;

		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCoursesDiskFolder");
			throw new RuntimeException("Error creating getCoursesDiskFolder", t);
		}
		return baseFolder;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/forms?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBForm> getFormsForApi(Properties properties) throws Throwable {
		List<JAXBForm> list = new ArrayList<JAXBForm>();
		List<Integer> courseIdList = this.getForms();
		if (null != courseIdList && !courseIdList.isEmpty()) {
			Iterator<Integer> formIterator = courseIdList.iterator();
			while (formIterator.hasNext()) {
				int currentcourseId = formIterator.next();
				Form form = this.getForm(currentcourseId);
				if (null != form) {
					list.add(new JAXBForm(form));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/form?courseId=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
    public JAXBForm getFormForApi(Properties properties) throws Throwable {
        String courseIdString = properties.getProperty("courseId");
        int courseId = 0;
		JAXBForm jaxbForm = null;
        try {
            courseId = Integer.parseInt(courseIdString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'courseId' parameter - '" + courseIdString + "'", Response.Status.CONFLICT);
        }
        Form form = this.getForm(courseId);
        if (null == form) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Form with courseId '" + courseIdString + "' does not exist", Response.Status.CONFLICT);
        }
        jaxbForm = new JAXBForm(form);
        return jaxbForm;
    }

    /**
     * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/form 
     * @param jaxbForm
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void addFormForApi(JAXBForm jaxbForm) throws ApiException, ApsSystemException {
        if (null != this.getForm(jaxbForm.getCourseId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Form with courseId " + jaxbForm.getCourseId() + " already exists", Response.Status.CONFLICT);
        }
        Form form = jaxbForm.getForm();
        this.addForm(form);
    }

    /**
     * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/form 
     * @param jaxbForm
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void updateFormForApi(JAXBForm jaxbForm) throws ApiException, ApsSystemException {
        if (null == this.getForm(jaxbForm.getCourseId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Form with courseId " + jaxbForm.getCourseId() + " does not exist", Response.Status.CONFLICT);
        }
        Form form = jaxbForm.getForm();
        this.updateForm(form);
    }

    /**
     * DELETE http://localhost:8080/<portal>/api/rs/en/form?courseId=1
	 * @param properties
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void deleteFormForApi(Properties properties) throws Throwable {
        String courseIdString = properties.getProperty("courseId");
        int courseId = 0;
        try {
            courseId = Integer.parseInt(courseIdString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'courseId' parameter - '" + courseIdString + "'", Response.Status.CONFLICT);
        }
        this.deleteForm(courseId);
    }

	private void notifyFormChangedEvent(Form form, int operationCode) {
		FormChangedEvent event = new FormChangedEvent();
		event.setForm(form);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setFormDAO(IFormDAO formDAO) {
		this._formDAO = formDAO;
	}
	protected IFormDAO getFormDAO() {
		return _formDAO;
	}

	public String getProtectedBaseDiskRootFolder() {
		return _protectedBaseDiskRootFolder;
	}
	public void setProtectedBaseDiskRootFolder(	String protectedBaseDiskRootFolder) {
		this._protectedBaseDiskRootFolder = protectedBaseDiskRootFolder;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IFormDAO _formDAO;
	private String _protectedBaseDiskRootFolder;

	public static final String FOLDER_NAME_COVER = "cover";
	public static final String FOLDER_NAME_INCENTIVE = "incentive";
}
