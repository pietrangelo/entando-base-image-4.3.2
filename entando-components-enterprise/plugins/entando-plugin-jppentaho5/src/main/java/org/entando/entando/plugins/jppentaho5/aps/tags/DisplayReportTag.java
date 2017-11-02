package org.entando.entando.plugins.jppentaho5.aps.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.PentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class DisplayReportTag extends ReportTag {

	private static final Logger _logger = LoggerFactory.getLogger(DisplayReportTag.class);


	@Override
	public int doStartTag() throws JspException {
		IPentahoManager pentahoManager = (IPentahoManager) ApsWebApplicationUtils.getBean(PentahoManager.MANAGER_ID, this.pageContext);
		ServletRequest request =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		final String paramPrefix = generateFormParameterPrefix(reqCtx);
		Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
		Map<String, String> submittedParameters = new HashMap<String, String>();
		Map<String, String> echoedParameters = new HashMap<String, String>();
		ApsProperties widgetConfig = widget.getConfig();

		try {
			_paramNames = widgetConfig.getProperty(PentahoSystemConstants.CONFIG_REPORT_PARAMCOUNT);

			int expectedParamNumber = expectedParameterNumber();

			for (Object param: request.getParameterMap().keySet()) {
				String name = (String) param;

				if (name.startsWith(REPORT_PARAMETER_PREFIX)) {
					boolean isLocalParam = name.startsWith(paramPrefix) ? true : false;
					String[] values = request.getParameterValues(name);

					for (String value: values) {
						String originalName = name;

						if (StringUtils.isBlank(value)) {
							System.out.println(">!> SKIPPING EMPTY PARAMETER: " + name);
							continue;
						}
						if (isLocalParam) {
							originalName = name.replace(paramPrefix, "");

							submittedParameters.put(originalName, value);
							System.out.println(">!> LOCAL PARAMETER " + originalName + ":" + value);
						} else {
							echoedParameters.put(originalName, value);
							System.out.println(">!> ECHOED PARAM " + originalName + ":" + value);
						}
					}
				}
			}
			System.out.println("#1# EXPECTED " + expectedParamNumber +", FOUND " + submittedParameters.size());

			String csvArgs = getCsvArgs();
			String path = getPath();
			
			// check whether we have to show the form; get configured parameters
			Map<String, PentahoParameter> configurationParameters = new HashMap<String, PentahoParameter>();

			// check the default values
			if (StringUtils.isNotBlank(csvArgs)) {
				String[] parameters = csvArgs.split(",");

				for (String currentParameter: parameters) {
					String[] currentParam = currentParameter.split("=");

					if (currentParam.length == 2) {
						String hiddenParamName = paramPrefix + currentParam[0];
						PentahoParameter hiddenParameter = prepareDefaultParameter(hiddenParamName, currentParam[0] ,currentParam[1]);

						configurationParameters.put(currentParam[0], hiddenParameter);
						System.out.println(">!> ADDING HIDDEN " + hiddenParamName + ":" + currentParam[1]);
					} else {
						_logger.error("??? IGNORING CONFIGURATION PARAMETER " + currentParameter);
					}
				}
			}
			
			// transform the default parameter map into a map we can check for validity
			System.out.println("#2# EXPECTED " + expectedParamNumber +", FOUND " + configurationParameters.size());
			Map<String, String> cpm = new HashMap<String, String>();
			if (configurationParameters.size() >= expectedParamNumber) {

				for (String key: configurationParameters.keySet()) {
					PentahoParameter hiddenParameter = configurationParameters.get(key);
					String hiddenValue = hiddenParameter.getValues().get("hidden").getValue();

					cpm.put(hiddenParameter.getName(), hiddenValue);
					System.out.println(">!> PROCESSING HIDDEN: " + hiddenParameter.getName() + ":" + hiddenValue);
				}
			}
			// check whether we can render
			if (!cpm.isEmpty()
					&& isValidConfiguration(cpm)) {
				String url = pentahoManager.getPentahoReportUrl(path, cpm);
				
				this.pageContext.setAttribute(this.getUrlVar(), url);
				System.out.println(">>> URL " + url);
			} else {
				this.pageContext.setAttribute(this.getUrlVar(), new String("none"));
			}
			// render the form; append echoed parameters to the default ones
			for (String key: echoedParameters.keySet()) {
				String value = echoedParameters.get(key);

				PentahoParameter defaultParameter = prepareDefaultParameter(key, null ,value);
				configurationParameters.put(key, defaultParameter);

				System.out.println(">!> INSERTING ECHOED PARAMETER " + defaultParameter.getName());
			}
			
			// get parameters from the report and add them to the configuration parameters if not already present
			List<PentahoParameter> reportParameters = getReportParameters(pentahoManager, path);
			for (PentahoParameter currentParameter: reportParameters) {
				if (!configurationParameters.containsKey(currentParameter.getName())) {
					System.out.println(">!> INSERTING FORM PARAMETER " + currentParameter.getName());
					configurationParameters.put(currentParameter.getName(), currentParameter);
				} else {
					System.out.println(">!> DISCARDING CONFIGURED PARAMETER " + currentParameter.getName());
				}
			}
			// finally prepare the list of parameters
			List<PentahoParameter> formParameters = new ArrayList<PentahoParameter>();

			for (String currentParamName: configurationParameters.keySet()) {
				PentahoParameter formParameter = configurationParameters.get(currentParamName);
				String formParameterName = formParameter.getName();

				// if echoed keep the original name otherwise add the parameter prefix
				if (!formParameterName.startsWith(REPORT_PARAMETER_PREFIX)) {
					formParameterName = paramPrefix + formParameter.getName();
				}
				formParameter.setFormName(formParameterName);
				formParameters.add(formParameter);
				System.out.println(">!> FINAL FORM NAME " + formParameter.getFormName());
			}
			// keep original sorting of parameters
			formParameters = sortFormArguments(reportParameters, formParameters);
			this.pageContext.setAttribute(this.getParamVar(), formParameters);
		} catch (Throwable t) {
			_logger.error("error rendering report form", t);
		}
		return EVAL_BODY_INCLUDE;
	}
}
