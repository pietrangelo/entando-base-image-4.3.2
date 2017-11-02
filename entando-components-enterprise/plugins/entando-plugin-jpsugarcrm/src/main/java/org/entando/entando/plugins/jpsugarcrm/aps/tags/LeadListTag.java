package org.entando.entando.plugins.jpsugarcrm.aps.tags;

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpsugarcrm.aps.JpSugarCRMSystemConstants;
import org.entando.entando.plugins.jpsugarcrm.aps.services.client.CrmLeadInfo;
import org.entando.entando.plugins.jpsugarcrm.aps.services.client.ISugarCRMClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class LeadListTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(LeadListTag.class);

	private static final Integer DEFAULT_MAX_RESULTS = 10;
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		ISugarCRMClientManager sugarCRMClientManager = (ISugarCRMClientManager) ApsWebApplicationUtils.getBean(JpSugarCRMSystemConstants.SUGAR_CLIENT_MANAGER, this.pageContext);
		try {
			HttpSession session = this.pageContext.getSession();
			Widget widget = this.extractWidget(reqCtx);
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			Integer max = this.getMaxResults();
			if (null == max && null != widget) {
				String maxValue = this.extractWidgetParameter("maxResults", widget.getConfig(), reqCtx);
				if (StringUtils.isNumeric(maxValue)) {
					max = new Integer(maxValue);
				}
			}
			if (null == max) max = DEFAULT_MAX_RESULTS;

			List<CrmLeadInfo> list = null;
			try {
				list = sugarCRMClientManager.getLeads(currentUser, max);
			} catch (Exception e) {
				_logger.error("error loading leads", e);
			}
			if (StringUtils.isBlank(this.getVar())) {
				this.pageContext.getOut().print(list);
			} else {
				this.pageContext.setAttribute(this.getVar(), list);
			}

		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Error in LeadListTag", t);
		}
		this.release();
		return super.doEndTag();
	}

	private Widget extractWidget(RequestContext reqCtx) {
		Widget showlet = null;
		showlet = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		return showlet;
	}

	protected String extractWidgetParameter(String parameterName, ApsProperties widgetConfig, RequestContext reqCtx) {
		String value = null;
		if (null != widgetConfig) {
			value = (String) widgetConfig.get(parameterName);
		}
		return value;
	}

	@Override
	public void release() {
		super.release();
		this.setVar(null);
		this.setMaxResults(null);

	}

	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public Integer getMaxResults() {
		return _maxResults;
	}
	public void setMaxResults(Integer maxResults) {
		this._maxResults = maxResults;
	}

	private String _var;
	private Integer _maxResults;

}