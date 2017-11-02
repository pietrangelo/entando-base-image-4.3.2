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
package com.agiletec.plugins.jpforum.aps.system.services.statistics;

public interface IStatisticDAO {

	public void createRecord(int threadCode);

	public void addView(int threadCode);

	public void deleteThreadStatistics(int threadCode);

	public int loadViews(int postCode);
	
	public MostOnlineUsersRecord loadMostOnlineUsers();

	public void updateMostOnlineUsers(MostOnlineUsersRecord record);

	public void addMostOnlineUsers(MostOnlineUsersRecord record);

}
