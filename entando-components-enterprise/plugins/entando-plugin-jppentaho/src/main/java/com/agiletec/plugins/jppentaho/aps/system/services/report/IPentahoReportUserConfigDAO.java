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
package com.agiletec.plugins.jppentaho.aps.system.services.report;

import com.agiletec.plugins.jppentaho.aps.system.services.report.model.PentahoReportUserWidgetConfig;

public interface IPentahoReportUserConfigDAO {
	
	public PentahoReportUserWidgetConfig getConfig(PentahoReportUserWidgetConfig config);
	
	public void addConfig(PentahoReportUserWidgetConfig config);
	
	public void deleteConfig(PentahoReportUserWidgetConfig config);
	
	public void updateConfig(PentahoReportUserWidgetConfig config);
	
}