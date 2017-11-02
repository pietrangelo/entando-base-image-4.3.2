package org.entando.entando.plugins.jppentaho5.aps.tags;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.PentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoAttribute;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameter;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class ReportTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(ReportTag.class);


	// raccogli tutti i parametri di pentaho nella request
	// recupera il numero di parametri per questo report
	// se il numero di parametri nella request è pari al numero voluto allora genera l'url
	// altrimenti general il form
	// recupera i parametri del report
	// recupera i parametri configurati
	// se il numero dei parametri configurati è pari al numero voluto allora genera l'url
	// altrimenti visualizza il form con i parametri degli altri widget
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
		int retVal = SKIP_BODY;

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
							System.out.println(">>> SKIPPING EMPTY PARAMETER: " + name);
							continue;
						}
						if (isLocalParam) {
							originalName = name.replace(paramPrefix, "");

							submittedParameters.put(originalName, value);
							System.out.println(">>> LOCAL PARAMETER " + originalName + ":" + value);
						} else {
							echoedParameters.put(originalName, value);
							System.out.println(">>> ECHOED PARAM " + originalName + ":" + value);
						}
					}
				}
			}
			System.out.println("#1# EXPECTED " + expectedParamNumber +", FOUND " + submittedParameters.size());

			// check whether we can render
			if (submittedParameters.size() >= expectedParamNumber
					&& isValidConfiguration(submittedParameters)) {
				String url = pentahoManager.getPentahoReportUrl(_path, submittedParameters);

				this.pageContext.setAttribute(this.getUrlVar(), url);
				System.out.println(">>> URL " + url);
			} else {
				// check whether we have to show the form; get configured parameters
				Map<String, PentahoParameter> configurationParameters = new HashMap<String, PentahoParameter>();

				// check the default values
				if (StringUtils.isNotBlank(_csvArgs)) {
					String[] parameters = _csvArgs.split(",");
					
					for (String currentParameter: parameters) {
						String[] currentParam = currentParameter.split("=");

						if (currentParam.length == 2) {
							String hiddenParamName = paramPrefix + currentParam[0];
							PentahoParameter hiddenParameter = prepareDefaultParameter(hiddenParamName, currentParam[0] ,currentParam[1]);

							configurationParameters.put(currentParam[0], hiddenParameter);
							System.out.println(">>> ADDING HIDDEN " + hiddenParamName + ":" + currentParam[1]);
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
						System.out.println(">>> PROCESSING HIDDEN: " + hiddenParameter.getName() + ":" + hiddenValue);
					}
				}
				// check whether we can render
				if (!cpm.isEmpty()
						&& isValidConfiguration(cpm)) {
					String url = pentahoManager.getPentahoReportUrl(_path, cpm);
					
					this.pageContext.setAttribute(this.getUrlVar(), url);
					System.out.println(">>> URL " + url);
				} else {
					// we must render the form; append echoed parameters to the default ones
					for (String key: echoedParameters.keySet()) {
						String value = echoedParameters.get(key);

						PentahoParameter defaultParameter = prepareDefaultParameter(key, null ,value);
						configurationParameters.put(key, defaultParameter);

						System.out.println(">>> INSERTING ECHOED PARAMETER " + defaultParameter.getName());
					}
					// get parameters from the report and add them to the configuration parameters if not already present
					List<PentahoParameter> reportParameters = getReportParameters(pentahoManager, _path);
					for (PentahoParameter currentParameter: reportParameters) {
						if (!configurationParameters.containsKey(currentParameter.getName())) {

							System.out.println(">>> INSERTING FORM PARAMETER " + currentParameter.getName());
							configurationParameters.put(currentParameter.getName(), currentParameter);
						} else {
							System.out.println(">>> DISCARDING CONFIGURED PARAMETER " + currentParameter.getName());
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
						System.out.println(">>> FINAL FORM NAME " + formParameter.getFormName());
					}
					// keep original sorting of parameters
					formParameters = sortFormArguments(reportParameters, formParameters);
					// yeah!
					this.pageContext.setAttribute(this.getParamVar(), formParameters);
					this.pageContext.setAttribute(this.getUrlVar(), new String("none"));
					retVal = EVAL_BODY_INCLUDE;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			_logger.error("error rendering report form", t);
		}
		return retVal;
	}
	
	/**
	 * Return the report parameters list for the current report
	 * @param pentahoManager
	 * @return
	 * @throws ApsSystemException
	 */
	protected List<PentahoParameter> getReportParameters(IPentahoManager pentahoManager, String path) throws ApsSystemException {
		List<PentahoParameter> reportParameters = pentahoManager.getReportFormParameterList(path, false);
		return reportParameters;
	}
	
	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}

	@Override
	public void release() {
		this.setPath(null);
		this.setCsvArgs(null);
		this.setBaseUrl(null);
		this.setUrlVar(null);
		this.setParamVar(null);
	}

	/**
	 * Create a fake form parameter with the given name and values
	 * @param key
	 * @param value
	 * @return
	 * @throws Throwable
	 */
	protected PentahoParameter prepareDefaultParameter(String key, String name, String value) throws Throwable {
		PentahoParameter pp = new PentahoParameter();
		PentahoAttribute pa = new PentahoAttribute();
		PentahoParameterValue pv = new PentahoParameterValue();
		Map<String, PentahoParameterValue> values = new HashMap<String, PentahoParameterValue>();
		Map<String, PentahoAttribute> attributes = new HashMap<String, PentahoAttribute>();

		pa.setName("parameter-render-type");
		pa.setValue("hidden");
		pv.setValue(value);
		values.put("hidden", pv);
		attributes.put(PARAMETER_RENDER_TYPE, pa);
		if (StringUtils.isNotBlank(name)) {
			pp.setName(name); // this is for echoed parameters of other widgets
		} else {
			pp.setName(key);	// this is for current report
		}
		pp.setFormName(key);
		pp.setAttribute(attributes);
		pp.setValues(values);
		return pp;
	}

	/**
	 * Keep the original form parameters sorting
	 * @param sortedArgs
	 * @param list
	 * @return
	 */
	protected List<PentahoParameter> sortFormArguments(List<PentahoParameter> sortedArgs, List<PentahoParameter> list) {
		List<PentahoParameter> result = new ArrayList<PentahoParameter>();
		
		// process parameters belonging to the form
		if (null != sortedArgs && null != list) {
			for (PentahoParameter sortedParam: sortedArgs) {
				String sortedName = sortedParam.getName();
				
				for (PentahoParameter listParam: list) {
					String name = listParam.getName();
					
					if (sortedName.equals(name)
							&& listParam.getValues().get("hidden") == null) {
						result.add(listParam);
						System.out.println(">>> SORTING: " + name);
					}
				}
			}
		}
		// process hidden parameters
		for (PentahoParameter listParam: list) {
			
			if (listParam.getValues().get("hidden") != null) {
				System.out.println(">>> SORTING (HIDDEN) " + listParam.getName());
				result.add(listParam);
			}
		}
		return result;
	}
	
	/**
	 * Check whether parameters are enough to render the parameter. FIXME type is not checked
	 * @param parameters
	 * @return
	 */
	protected boolean isValidConfiguration(Map<String, String> parameters) {
		String[] args = _paramNames.split(",");
		boolean match = true;
		
		if (parameters == null
				|| parameters.isEmpty()
				|| parameters.size() != args.length) {
			return false;
		}
		for (String arg: args) {
			match &= parameters.containsKey(arg) && StringUtils.isNotBlank(parameters.get(arg));
			System.out.println(">>> CHECK " + arg + ":" + parameters.get(arg));
		}
		System.out.println(">>> OVERALL CHECK " + match);
		return match;
	}
	
	/**
	 * Generate a unique prefix to prepend to this widget report parameters
	 * @param ctx
	 * @return
	 * @throws JspException
	 */
	protected String generateFormParameterPrefix(RequestContext ctx) throws JspException {
		String prefix = null;
		Page page = null;
		int frame = 0;

		if (StringUtils.isBlank(_customPrefix)) {
			// human friendly, tied to pagecode and frame position
			if (null != ctx) {
				frame = (Integer) ctx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
				page = (Page) ctx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);

				prefix = REPORT_PARAMETER_PREFIX + page.getCode() + "_" + frame + "_";
			} else {
				// just a random string
				Random random = new Random();
				long rnd = random.nextLong();

				prefix = REPORT_PARAMETER_PREFIX + String.valueOf(rnd) + "_";
			}
		} else if (_customPrefix.equals(CUSTOM_PREFIX_TITLE)) {
			File file = new File(_path);
			
			prefix = REPORT_PARAMETER_PREFIX.concat(file.getName()).concat("_").replace(".", "_");
		} else if (_customPrefix.equals(CUSTOM_PREFIX_PATH)) {
			prefix = REPORT_PARAMETER_PREFIX.concat(_path).concat("_").replace(".", "_");
		} else {
			throw new JspException("Invalid prefix specification");
		}
		System.out.println("### PREFIX IS [" + prefix + "]");
		return prefix;
	}

	/**
	 * Return the number of the parameters needed to generate the report
	 * @return
	 */
	protected int expectedParameterNumber() {
		int cnt = 0;
		
//		System.out.println(">>> ARGS [" + _paramNames + "]");
		if (StringUtils.isNotBlank(_paramNames)) {
			String[] args = _paramNames.split(",");
			
			cnt = args.length;
		}
		return cnt;
	}
	
	public String getPath() {
		return _path;
	}
	public void setPath(String path) {
		this._path = path;
	}
	public String getCsvArgs() {
		return _csvArgs;
	}
	public void setCsvArgs(String csvArgs) {
		this._csvArgs = csvArgs;
	}
	public String getBaseUrl() {
		return _baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this._baseUrl = baseUrl;
	}
	public String getUrlVar() {
		return _urlVar;
	}
	public void setUrlVar(String urlVar) {
		this._urlVar = urlVar;
	}
	public String getParamVar() {
		return _paramVar;
	}
	public void setParamVar(String paramVar) {
		this._paramVar = paramVar;
	}
	public String getParamNames() {
		return _paramNames;
	}
	public void setParamNames(String paramNames) {
		this._paramNames = paramNames;
	}
	public String getCustomPrefix() {
		return _customPrefix;
	}
	public void setCustomPrefix(String customPrefix) {
		this._customPrefix = customPrefix;
	}

	private String _path;
	private String _csvArgs;
	private String _baseUrl;
	private String _urlVar;
	private String _paramVar;
	protected String _paramNames;
	private String _customPrefix;

	public static final String PARAMETER_RENDER_TYPE = "parameter-render-type";
	
	public static final String REPORT_PARAMETER_PREFIX = "pth5_";
	
	public static final String CUSTOM_PREFIX_TITLE = "title";
	public static final String CUSTOM_PREFIX_PATH = "path";
}
