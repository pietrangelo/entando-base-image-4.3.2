package org.entando.entando.plugins.jppentaho5.apsadmin.portal.specialwidget;

import java.util.List;

import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.Widget;

public class ReportDownloadConfig extends ReportConfig {

	private static final Logger _logger = LoggerFactory.getLogger(ReportDownloadConfig.class);

	@Override
	public String init() {
		String result = super.init();

		try {
			if (result == SUCCESS) {
				String reportPathParam = getWidget().getConfig().getProperty(PentahoSystemConstants.CONFIG_REPORT_PATH);
				setPathParam(reportPathParam);
				String argsParam = getWidget().getConfig().getProperty(PentahoSystemConstants.CONFIG_REPORT_ARGS);
				setArgsParam(argsParam);
			}
		} catch (Throwable t) {
			_logger.error("error entering configuration action", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	protected void createValuedShowlet() throws Exception {
		Widget widget = this.createNewShowlet();
		List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();
		for (int i=0; i<parameters.size(); i++) {
			WidgetTypeParameter param = parameters.get(i);
			String paramName = param.getName();
			String value = this.getRequest().getParameter(paramName);
			if (value != null && value.trim().length() > 0) {
				widget.getConfig().setProperty(paramName, value);
			}
		}
		List<PentahoParameter> params = getPentahoManager().getReportFormParameterList(getPathParam(), true); // <- NOTE THIS!
		StringBuilder csv = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			PentahoParameter parameter = params.get(i);

			csv.append(parameter.getName());
			if (i < params.size() - 1) {
				csv.append(",");
			}
		}
		widget.getConfig().setProperty(PentahoSystemConstants.CONFIG_REPORT_PARAMCOUNT, csv.toString());
		_logger.info("This report to download needs the following parameters: " + csv.toString());
		this.setShowlet(widget);
	}

}
