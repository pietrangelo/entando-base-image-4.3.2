/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.resource.model;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResourceDimension;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.imageresizer.IImageResizer;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;
import javax.swing.ImageIcon;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteImageResource extends ImageResource implements MultisiteResourceInterface {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteImageResource.class);
	
	@Override
	protected String getUrlPath(ResourceInstance instance) {
		if (null == instance) return null;
		StringBuilder urlPath = new StringBuilder();
		if (this.isProtectedResource()) {
			//PATH di richiamo della servlet di autorizzazione
			//Sintassi /<RES_ID>/<SIZE>/<LANG_CODE>/
			String DEF = "def";
			urlPath.append(this.getProtectedBaseURL());
			if (!urlPath.toString().endsWith("/")) urlPath.append("/");
			urlPath.append(this.getId()).append("/");
			if (instance.getSize() < 0) {
				urlPath.append(DEF);
			} else {
				urlPath.append(instance.getSize());
			}
			urlPath.append("/");
			if (instance.getLangCode() == null) {
				urlPath.append(DEF);
			} else {
				urlPath.append(instance.getLangCode());
			}
			urlPath.append("/");
    	} else {
			StringBuilder subFolder = new StringBuilder(this.getFolder());
    		if (!subFolder.toString().endsWith("/")) {
				subFolder.append("/");
			}
			//This add the multisite code to the path
			String multisiteCodeByString = MultisiteUtils.getMultisiteCodeByString(this.getId());
			if(StringUtils.isNotBlank(multisiteCodeByString)){
				subFolder.append(multisiteCodeByString).append("/");
			}
    		subFolder.append(instance.getFileName());
			String path = this.getStorageManager().getResourceUrl(subFolder.toString(), false);
			urlPath.append(path);
    	}
		return urlPath.toString();
	}
	
	
	@Override
	public void saveResourceInstances(ResourceDataBean bean) throws ApsSystemException {
		try {
			String masterImageFileName = this.getInstanceFileName(bean.getFileName(), 0, null);
			String subPath = this.getDiskSubFolder() + masterImageFileName;
			this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
			File tempMasterFile = this.saveTempFile("temp_" + masterImageFileName, bean.getInputStream());
			ResourceInstance instance = new ResourceInstance();
			instance.setSize(0);
			instance.setFileName(masterImageFileName);
			String mimeType = bean.getMimeType();
			instance.setMimeType(mimeType);
			instance.setFileLength(bean.getFileSize() + " Kb");
			this.addInstance(instance);
			this.saveResizedInstances(bean, tempMasterFile.getAbsolutePath());
			this.getStorageManager().saveFile(subPath, 
					this.isProtectedResource(), new FileInputStream(tempMasterFile));
			tempMasterFile.delete();
		} catch (Throwable t) {
			_logger.error("Error saving image resource instances", t);
			//ApsSystemUtils.logThrowable(t, this, "saveResourceInstances");
			throw new ApsSystemException("Error saving image resource instances", t);
		}
	}
	
	private void saveResizedInstances(ResourceDataBean bean, String masterFilePath) throws ApsSystemException {
		try {
			Map<Integer, ImageResourceDimension> dimensions = this.getImageDimensionReader().getImageDimensions();
			Iterator<ImageResourceDimension> iterDimensions = dimensions.values().iterator();
			while (iterDimensions.hasNext()) {
				ImageResourceDimension dimension = iterDimensions.next();
				//Is the system use ImageMagick?
				if (!this.isImageMagickEnabled()) {
					ImageIcon imageIcon = new ImageIcon(masterFilePath);
					this.saveResizedImage(bean, imageIcon, dimension);
				} else {
					this.saveResizedImage(bean, dimension);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error saving resized image resource instances", t);
			//ApsSystemUtils.logThrowable(t, this, "saveResizedInstances");
			throw new ApsSystemException("Error saving resized image resource instances", t);
		}
	}
	
	/**
	 * Redim images using im4Java
	 * @param bean
	 * @param dimension
	 * @param mimeType
	 * @param baseDiskFolder
	 * @throws ApsSystemException
	 */
	private void saveResizedImage(ResourceDataBean bean, ImageResourceDimension dimension) throws ApsSystemException {
		if (dimension.getIdDim() == 0) {
			//salta l'elemento con id zero che non va ridimensionato
			return;
		}
		String imageName = this.getInstanceFileName(bean.getFileName(), dimension.getIdDim(), null);
		String subPath = super.getDiskSubFolder() + imageName;
		try {
			this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
			ResourceInstance resizedInstance = new ResourceInstance();
			resizedInstance.setSize(dimension.getIdDim());
			resizedInstance.setFileName(imageName);
			resizedInstance.setMimeType(bean.getMimeType());
			String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + "temp_" + imageName;
			File tempFile = new File(tempFilePath);
			long realLength = tempFile.length() / 1000;
			resizedInstance.setFileLength(String.valueOf(realLength) + " Kb");
			this.addInstance(resizedInstance);
			// create command
			ConvertCmd convertCmd = new ConvertCmd();
			//Is it a windows system?
			if (this.isImageMagickWindows()) {
				//yes so configure the path where ImagicMagick is installed
				convertCmd.setSearchPath(this.getImageMagickPath());
			}
			// create the operation, add images and operators/options
			IMOperation imOper = new IMOperation();
			imOper.addImage();
			imOper.resize(dimension.getDimx(), dimension.getDimy());
			imOper.addImage();
			convertCmd.run(imOper, bean.getFile().getAbsolutePath(), tempFile.getAbsolutePath());
			this.getStorageManager().saveFile(subPath, this.isProtectedResource(), new FileInputStream(tempFile));
			tempFile.delete();
		} catch (Throwable t) {
			_logger.error("Error creating resource file instance '{}'", subPath, t);
			//ApsSystemUtils.logThrowable(t, this, "saveResizedImage");
			throw new ApsSystemException("Error creating resource file instance '" + subPath + "'", t);
		}
	}
	
	private void saveResizedImage(ResourceDataBean bean, 
			ImageIcon imageIcon, ImageResourceDimension dimension) throws ApsSystemException {
		if (dimension.getIdDim() == 0) {
			//salta l'elemento con id zero che non va ridimensionato
			return;
		}
		String imageName = this.getInstanceFileName(bean.getFileName(), dimension.getIdDim(), null);
		String subPath = this.getDiskSubFolder() + imageName;
		try {
			this.getStorageManager().deleteFile(subPath, this.isProtectedResource());
			IImageResizer resizer = this.getImageResizer(subPath);
			ResourceInstance resizedInstance = 
					resizer.saveResizedImage(subPath, this.isProtectedResource(), imageIcon, dimension);
			this.addInstance(resizedInstance);
		} catch (Throwable t) {
			_logger.error("Error creating resource file instance '{}'", subPath, t);
			//ApsSystemUtils.logThrowable(t, this, "saveResizedImage");
			throw new ApsSystemException("Error creating resource file instance '" + subPath + "'", t);
		}
	}
	
	
	@Override
	protected String getDiskSubFolder() {
		StringBuilder diskFolder = new StringBuilder(super.getDiskSubFolder());
		if(null != this.getMultisiteCode() && !diskFolder.toString().contains(this.getMultisiteCode())){
			diskFolder = diskFolder.append(this.getMultisiteCode()).append(File.separator);
		}
		return diskFolder.toString();
	}
	
	private IImageResizer getImageResizer(String filePath) {
		String extension = filePath.substring(filePath.lastIndexOf('.') + 1).trim();
		String resizerClassName = this.getImageResizerClasses().get(extension);
		if (null == resizerClassName) {
			resizerClassName = this.getImageResizerClasses().get("DEFAULT_RESIZER");
		}
		try {
			Class resizerClass = Class.forName(resizerClassName);
			IImageResizer resizer = (IImageResizer) resizerClass.newInstance();
			resizer.setStorageManager(this.getStorageManager());
			return resizer;
		} catch (Throwable t) {
			throw new RuntimeException("Errore in creazione resizer da classe '"
					+ resizerClassName + "' per immagine tipo '" + extension + "'", t);
		}
	}

	public String getMultisiteCode() {
		return _multisiteCode;
	}

	@Override
	public void setMultisiteCode(String multisiteCode) {
		this._multisiteCode = multisiteCode;
	}

	private String _multisiteCode;

}
