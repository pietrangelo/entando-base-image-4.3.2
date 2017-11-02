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
package com.agiletec.plugins.jppentaho.apsadmin.pentaho;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jppentaho.aps.system.services.report.IPentahoDynamicReportManager;
import com.agiletec.plugins.jppentaho.aps.util.PentahoHelper;

public class PentahoAbstractBaseAction extends BaseAction {

	public Set<String> getOutputTypes() {
		Set<String> outputTypes = new HashSet<String>();
		outputTypes.add(IPentahoDynamicReportManager.CSV_KEY);
		outputTypes.add(IPentahoDynamicReportManager.EXCEL_2007_KEY);
		outputTypes.add(IPentahoDynamicReportManager.EXCEL_KEY);
//		outputTypes.add(IPentahoDynamicReportManager.HTML_PAGINATED_KEY);
		outputTypes.add(IPentahoDynamicReportManager.HTML_STREAM_KEY);
		outputTypes.add(IPentahoDynamicReportManager.PDF_KEY);
		outputTypes.add(IPentahoDynamicReportManager.RTF_KEY);
		outputTypes.add(IPentahoDynamicReportManager.TEXT_KEY);
		return outputTypes;
	}

	protected Widget extractCurrentShowlet() {
            RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
            return (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
	}

	protected ApsProperties extractShowletConfig() {
            Widget currentShowlet = this.extractCurrentShowlet();
            WidgetType WidgetType = currentShowlet.getType();
            return (WidgetType.isLogic()) ? WidgetType.getConfig() : currentShowlet.getConfig();
	}

	protected Map<String, String> prepareParams(ApsProperties config) {
		Map<String, String> params = null;
		if (config!=null) {
			String queryString = config.getProperty(IPentahoDynamicReportManager.SHOWLET_QUERY_STRING);
			String profileParams = config.getProperty(IPentahoDynamicReportManager.SHOWLET_PROFILE_PARAMS);
			params = this.prepareParams(queryString, profileParams);
		} else {
			params = new HashMap<String, String>();
		}
		return params;
	}

	protected Map<String, String> prepareParams(String queryString, String profileParams) {
		Map<String, String> params = null;
		if (null != queryString && queryString.length() > 0) {
			params = this._helperPentahoTag.fromQueryStringToParamsMap(queryString);
		}
		if (params==null) {
			params = new HashMap<String, String>();
		}
		this._helperPentahoTag.addReportParamsFromProfile(this.getCurrentUser(), profileParams, params);
		return params;
	}

	protected String extractSolutionFromConfig(String username, RequestContext reqCtx, Integer currentFrame, Page page) {
		ApsProperties config = this.extractShowletConfig();
		String solution = null;
		if (config!=null) {
			solution = config.getProperty(IPentahoDynamicReportManager.SHOWLET_SOLUTION_PROPERTY);
		}
		return solution;
	}

	protected IPentahoDynamicReportManager getDynamicReportManager() {
		return _dynamicReportManager;
	}
	public void setDynamicReportManager(IPentahoDynamicReportManager dynamicReportManager) {
		this._dynamicReportManager = dynamicReportManager;
	}

	private IPentahoDynamicReportManager _dynamicReportManager;
    protected PentahoHelper _helperPentahoTag = new PentahoHelper();

}
