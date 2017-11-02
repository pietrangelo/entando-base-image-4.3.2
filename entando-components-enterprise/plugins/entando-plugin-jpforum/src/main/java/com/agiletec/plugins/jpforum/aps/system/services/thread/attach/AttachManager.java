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
package com.agiletec.plugins.jpforum.aps.system.services.thread.attach;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;

@Aspect
public class AttachManager extends AbstractService implements IAttachManager {

	private static final Logger _logger =  LoggerFactory.getLogger(AttachManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready ",this.getClass().getName());
	}

	@Override
	public void deleteAttach(int code, String filename)	throws ApsSystemException {
		try {
			Post post = this.getThreadManager().getPost(code);
			List<Attach> attachs = getAttachs(code);
			if (null != attachs && attachs.size() > 0) {
				Iterator<Attach> it = attachs.iterator();
				while (it.hasNext()) {
					Attach attach = it.next();
					if (attach.getName().equals(filename)) {
						String fileFullPath = this.getAttachDiskFolder() + post.getUsername() + File.separator + attach.getFileName();
						File file = new File(fileFullPath);
						file.delete();
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione allegato post {}", code, t);
			throw new ApsSystemException("Errore in eliminazione allegato post " + code, t);
		}
	}

	@Before("execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.deletePost(..)) && args(key)")
	public void deletePostAttachs(Object key) throws ApsSystemException {
		int code = ((Integer) key).intValue();
		this.deleteAttachs(code);
	}

	@Before("execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.deleteThread(..)) && args(key)")
	public void deleteThreadAttachs(Object key) throws ApsSystemException {
		int code = ((Integer) key).intValue();
		Thread thread = this.getThreadManager().getThread(code);
		List<Integer> postCodes = thread.getPostCodes();
		if (null != postCodes && postCodes.size() > 0) {
			Iterator<Integer> it = postCodes.iterator();
			while (it.hasNext()) {
				int postCode = it.next().intValue();
				this.deleteAttachs(postCode);
			}
		}
	}

	public void deleteAttachs(int code) throws ApsSystemException {
		try {
			//Post post = this.getPost(code);
			List<Attach> attachs = this.getAttachs(code);
			if (null != attachs && attachs.size() > 0) {
				Iterator<Attach> it = attachs.iterator();
				while (it.hasNext()) {
					Attach attach = it.next();
					String fileFullPath = this.getAttachDiskFolder() + attach.getUserName() + File.separator + attach.getFileName();
					File file = new File(fileFullPath);
					file.delete();
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione allegati post {}", code, t);
			throw new ApsSystemException("Errore in eliminazione allegati post " + code, t);
		}
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void deleteAttachs(Object key) throws ApsSystemException {
		//decommentare se, in fase di rimozione utente, si desidera cancellare anche gli allegati relativi allo stesso
//		String username = "";
//		if (key instanceof String) {
//			username = key.toString();
//		} else if (key instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails) key;
//			username = userDetails.getUsername();
//		}
//		this.deleteAttachs(username);
	}

	public void deleteAttachs(String username) throws ApsSystemException {
		try {
			File userDir = new File(this.getAttachDiskFolder() + username);
			if (userDir.exists()) {
				FileUtils.forceDelete(userDir);
			}
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione allegati per utente {}", username, t);
			throw new ApsSystemException("Errore in eliminazione allegati per utente " + username, t);
		}
	}

	@Override
	public List<Attach> getAttachs(int code) throws ApsSystemException {
		List<Attach> attachs = new ArrayList<Attach>();
		try {
			Post post = this.getThreadManager().getPost(code);
			if (null == post) {
				_logger.debug("richiesti allegati per post nullo con codice {}", code);
				return attachs;
			}
			AttachFileNameFilter filter = new AttachFileNameFilter(code);
			File userDir = new File(this.getAttachDiskFolder() + post.getUsername());
			String[] files = userDir.list(filter);
			if (null != files && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					File currFile = new File(userDir + File.separator + files[i]);
					Attach attach = new Attach();
					attach.setFileName(currFile.getName());
					attach.setFileSize(new Long(currFile.length()).intValue());
					attach.setPostCode(post.getCode());
					attach.setUserName(post.getUsername());
					attachs.add(attach);
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in recupero allegati per post {}", code, t);
			throw new ApsSystemException("Errore in recupero allegati per post " + code, t);
		}
		return attachs;
	}

	@Override
	public List<Attach> getAttachs(String username) throws ApsSystemException {
		List<Attach> attachs = new ArrayList<Attach>();
		try {
			File dir = new File(this.getAttachDiskFolder() + username);
			String[] files = dir.list();
			if (null != files && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					File currFile = new File(dir + File.separator + files[i]);
					if (!currFile.isDirectory()) {
						Attach attach = new Attach();
						attach.setFileName(currFile.getName());
						attach.setFileSize(new Long(currFile.length()).intValue());
						attach.setPostCode(attach.getPostCodeFromFileName());
						attach.setUserName(username);
						attachs.add(attach);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in recuoero allegati per utente {}", username, t);
			throw new ApsSystemException("Errore in recuoero allegati per utente " + username, t);
		}
		return attachs;
	}

	@Override
	public void saveAttachs(List<Attach> attachs) throws ApsSystemException {
		try {
			Iterator<Attach> it = attachs.iterator();
			while (it.hasNext()) {
				Attach attach =  it.next();
				_logger.debug("Salvataggio allegato per post: {} -- filename: {}", attach.getPostCode(), attach.getFileName());
				InputStream inputStream = attach.getInputStream();
				String userDirPath = this.getAttachDiskFolder()  + attach.getUserName();
				File dir = new File(userDirPath);
				if (!dir.exists()) {
					FileUtils.forceMkdir(new File(userDirPath));
				}
				String fullFileName = null;
				fullFileName = userDirPath + File.separator + attach.getPostCode() + "_" +  attach.getFileName();
				FileOutputStream outStream = new FileOutputStream(fullFileName);
				while (inputStream.available() > 0) {
					outStream.write(inputStream.read());
				}
				outStream.close();
				inputStream.close();
			}
		} catch (Throwable t) {
			_logger.error("Errore in salvataggio allegati", t);
			throw new ApsSystemException("Errore in salvataggio allegati", t);
		}
	}

	public void setAttachDiskFolder(String attachDiskFolder) {
		this._attachDiskFolder = attachDiskFolder;
	}
	@Override
	public String getAttachDiskFolder() {
		try {
			if (null == this._attachDiskFolder) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(this.getConfigManager().getParam(SystemConstants.PAR_RESOURCES_DISK_ROOT));
				if (!buffer.toString().endsWith(File.separator)) {
					buffer.append(File.separator);
				}
				buffer.append("plugins").append(File.separator)
					.append("jpforum").append(File.separator).append("attachs").append(File.separator);
				this._attachDiskFolder = buffer.toString();
			}
		} catch (Throwable t) {
			_logger.error("Error creating Attach Disk Folder", t);
			throw new RuntimeException("Error creating Attach Disk Folder", t);
		}
		return _attachDiskFolder;
	}

	public void setAttachFolderMaxSize(int attachFolderMaxSize) {
		this._attachFolderMaxSize = attachFolderMaxSize;
	}
	@Override
	public int getAttachFolderMaxSize() {
		return _attachFolderMaxSize;
	}

	protected IThreadManager getThreadManager() {
		return _threadManager;
	}
	public void setThreadManager(IThreadManager threadManager) {
		this._threadManager = threadManager;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	private String _attachDiskFolder;
	private int _attachFolderMaxSize;

	private IThreadManager _threadManager;
	private ConfigInterface _configManager;

}
