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
package com.agiletec.plugins.jpforum.aps.system.services.section;

import java.util.List;

public interface ISectionDAO {

	public List<Section> loadSections();

	public void addSection(Section section);

	public void deleteSection(Section section);

	public void updateSection(Section section);

	public void updatePosition(Section sectionDown, Section sectionUp);

	public List<String> getSectionsForGroup(String groupName);

}
