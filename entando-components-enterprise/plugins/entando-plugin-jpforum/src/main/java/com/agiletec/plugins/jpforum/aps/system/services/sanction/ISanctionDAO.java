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
package com.agiletec.plugins.jpforum.aps.system.services.sanction;

import java.util.List;

public interface ISanctionDAO {

	public void insertSanction(Sanction sanction);

	public void removeSanction(int id);

	public Sanction loadSanction(int id);

	public List<Integer> loadSanctions(String username);

	public void removeSanctions(String username);

	public boolean isUnderSanction(String username);
}
