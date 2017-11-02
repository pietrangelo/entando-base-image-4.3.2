package org.entando.entando.plugins.jppentaho5.aps.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameterValue;

import com.agiletec.aps.system.exception.ApsSystemException;

public class ReportDownloadTag extends ReportTag {

	/**
	 * Return the report parameters list for the current report
	 * @param pentahoManager
	 * @return
	 * @throws ApsSystemException
	 */
	@Override
	protected List<PentahoParameter> getReportParameters(IPentahoManager pentahoManager, String path) throws ApsSystemException {
		List<PentahoParameter> reportParameters = pentahoManager.getReportFormParameterList(path, true);
		Map<String, PentahoParameterValue> values = new HashMap<String, PentahoParameterValue>();
		// remove HTML outputs
		for (PentahoParameter parameter: reportParameters) {
			if (parameter.getName().equals(PentahoSystemConstants.PENTAHO_OUTPUT_PARAMETER)) {
				for (String name: parameter.getValues().keySet()) {
					if (!name.contains(PentahoSystemConstants.PENTAHO_OUTPUT_PARAMETER_VALUE_HTML)) {
						PentahoParameterValue value = parameter.getValues().get(name);
						
						values.put(name, value);
					}
				}
				parameter.setValues(values);
			}
		}
		return reportParameters;
	}
	
}
