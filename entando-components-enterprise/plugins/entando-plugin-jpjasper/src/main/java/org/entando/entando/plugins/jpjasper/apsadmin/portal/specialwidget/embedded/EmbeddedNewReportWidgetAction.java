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
package org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.embedded;

public class EmbeddedNewReportWidgetAction  extends AbstractEmbeddedJasperConfigAction {
	
	@Override
	public String getServerEndpoint() {
		return "/flow.html?_flowId=adhocFlow&viewAsAdhocFrame=true&decorate=no";
	}
	
}