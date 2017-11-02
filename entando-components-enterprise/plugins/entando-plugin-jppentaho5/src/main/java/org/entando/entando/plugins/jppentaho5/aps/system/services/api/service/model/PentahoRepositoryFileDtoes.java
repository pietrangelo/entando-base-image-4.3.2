package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model;

import java.util.List;

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
@XmlRootElement(name="repositoryFileDtoes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PentahoRepositoryFileDtoes implements IRepositoryFileDtoes{
   
	private List<PentahoRepositoryFileDto> repositoryFileDto;

	public List<PentahoRepositoryFileDto> getRepositoryFileDto() {
		return repositoryFileDto;
	}

	public void setRepositoryFileDto(List<PentahoRepositoryFileDto> repositoryFileDto) {
		this.repositoryFileDto = repositoryFileDto;
	}

}
