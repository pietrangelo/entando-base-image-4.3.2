package org.entando.entando.plugins.jppentaho5.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jppentaho5.aps.system.PentahoSystemConstants;
import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.PentahoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class PentahoReportTag extends TagSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(PentahoReportTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		IPentahoManager pentahoManager = (IPentahoManager) ApsWebApplicationUtils.getBean(PentahoManager.MANAGER_ID, this.pageContext);
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		String reportUrl = null;
		
		_baseUrl = getBaseUrl();
		_path = getPath();
		_csvArgs = getCsvArgs();
		try {
			if (null == _path
					|| null == _csvArgs) {
				Widget widget = this.extractWidget(reqCtx);
				ApsProperties widgetConfig = widget.getConfig();
				
				if (null == _csvArgs) {
					_csvArgs = widgetConfig.getProperty(PentahoSystemConstants.CONFIG_REPORT_ARGS);
				}
				if (null == _path) {
					_path = widgetConfig.getProperty(PentahoSystemConstants.CONFIG_REPORT_PATH);
				}
			}
			if (null != _baseUrl) {
				reportUrl = pentahoManager.getPentahoReportUrl(_baseUrl, _path, _csvArgs);
			} else {
				reportUrl = pentahoManager.getPentahoReportUrl(_path, _csvArgs);
			}
			this.pageContext.setAttribute(this.getVar(), reportUrl);
		} catch (Throwable t) {
			_logger.error("Error in doStartTag", t);
			throw new JspException("Error in ControllerTag doStartTag", t);
		}
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		this.release();
		return super.doEndTag();
	}

	@Override
	public void release() {
		this.setVar(null);
	}

	private Widget extractWidget(RequestContext reqCtx) {
		Widget widget = null;
		widget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		return widget;
	}

	protected String extractWidgetParameter(String parameterName, ApsProperties widgetConfig, RequestContext reqCtx) {
		return (String) widgetConfig.get(parameterName);
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	public String getPath() {
		return _path;
	}
	public void setPath(String path) {
		this._path = path;
	}
	public String getBaseUrl() {
		return _baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this._baseUrl = baseUrl;
	}
	public String getCsvArgs() {
		return _csvArgs;
	}
	public void setCsvArgs(String csvArgs) {
		this._csvArgs = csvArgs;
	}

	private String _var;
	private String _path;
	private String _baseUrl;
	private String _csvArgs;
}
