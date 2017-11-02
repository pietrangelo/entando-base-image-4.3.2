/*
 * Copyright 2016-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jppentaho5.aps.system.services.api.model;

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
