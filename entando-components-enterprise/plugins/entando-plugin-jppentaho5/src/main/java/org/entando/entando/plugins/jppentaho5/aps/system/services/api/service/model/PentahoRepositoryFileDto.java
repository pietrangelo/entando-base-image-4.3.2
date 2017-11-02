package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author entando
 * 
 * Pentaho Rest API WebPage Documentation: 
 * https://help.pentaho.com/Documentation/6.0/0R0/070/010/0A0/0O0#.2Frepo.2Ffiles.2F.7BpathId_.7D.2Fchildren
 * 
 */
@XmlRootElement(name="repositoryFileDto")
@XmlAccessorType(XmlAccessType.FIELD)
public class PentahoRepositoryFileDto implements IRepositoryFileDto{
   
	private String aclNode;
	private String createdDate;
	private String fileSize;
	private boolean folder;
	private boolean hidden;
	private String id;
	private String locale;  
	private String locked;
	private String name;
	private String ownerType;
	private String path;  
	private String title;	  
	private String versionCommentEnabled;
	private String versioned;		
	private String versioningEnabled;	
	
	public String getAclNode() {
		return aclNode;
	}

	public void setAclNode(String aclNode) {
		this.aclNode = aclNode;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVersionCommentEnabled() {
		return versionCommentEnabled;
	}

	public void setVersionCommentEnabled(String versionCommentEnabled) {
		this.versionCommentEnabled = versionCommentEnabled;
	}

	public String getVersioned() {
		return versioned;
	}

	public void setVersioned(String versioned) {
		this.versioned = versioned;
	}

	public String getVersioningEnabled() {
		return versioningEnabled;
	}

	public void setVersioningEnabled(String versioningEnabled) {
		this.versioningEnabled = versioningEnabled;
	}

	@Override
	public String toString() {
		return String.format("id: %s, name: %s, title: %s, fileSize: %s",
    		id,
    		name,    		
    		title,
    		fileSize);
  }
  
}
