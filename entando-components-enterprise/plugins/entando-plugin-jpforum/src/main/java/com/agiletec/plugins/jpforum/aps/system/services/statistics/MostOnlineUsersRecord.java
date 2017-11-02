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

import java.util.Date;

public class MostOnlineUsersRecord {

	public int getCount() {
		return _count;
	}
	public void setCount(int count) {
		this._count = count;
	}
	public Date getRecordDate() {
		return _recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this._recordDate = recordDate;
	}
	private int _count;
	private Date _recordDate;
}
