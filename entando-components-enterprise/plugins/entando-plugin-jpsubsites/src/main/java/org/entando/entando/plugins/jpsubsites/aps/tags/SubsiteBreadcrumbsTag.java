/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.tags;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.widget.INavigatorParser;
import com.agiletec.aps.system.services.page.widget.NavigatorExpression;
import com.agiletec.aps.system.services.page.widget.NavigatorTarget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.plugins.jpsubsites.aps.system.JpsubsitesSystemConstants;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;

/**
 * Tag iterativo che genera un menù a briciole di pane a partire dalla home del sottosito.
 * @author E.Santoboni - E.Mezzano
 */
public class SubsiteBreadcrumbsTag extends TagSupport {
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		this._reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		String navSpec = this.getBreadCrumbsNavSpec();
		INavigatorParser navigatorParser = (INavigatorParser) ApsWebApplicationUtils.getBean("jpsubsitesBreadcrumbsNavigatorParser", this.pageContext);
		IPage currPage = (IPage) this._reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		this._targets = navigatorParser.parseSpec(navSpec, currPage, currentUser);
		if (this._targets == null || this._targets.isEmpty()) {
			return SKIP_BODY;
		} else {
			this.purgeTargets(currPage, request);
			this._index = 0;
			NavigatorTarget currentTarget = this.getCurrentTarget();
			this.pageContext.setAttribute(this.getVar(), currentTarget);
			return EVAL_BODY_INCLUDE;
		}
	}
	
	@Override
	public int doAfterBody() throws JspException {
		this._index++;
		if (this._index >= this._targets.size()) {
			return SKIP_BODY;
		} else {
			NavigatorTarget currentTarget = this.getCurrentTarget();
			this.pageContext.setAttribute(this.getVar(), currentTarget);
			return EVAL_BODY_AGAIN;
		}
	}
	
	private void purgeTargets(IPage currPage, ServletRequest request) {
		Subsite subsite = (Subsite) request.getAttribute(JpsubsitesSystemConstants.REQUEST_PARAM_CURRENT_SUBSITE);
		if (subsite != null) {
			String subsiteRoot = subsite.getHomepage();
			if (subsiteRoot.equals(currPage.getCode())) {
				NavigatorTarget last = (NavigatorTarget) this._targets.get(this._targets.size()-1);
				this._targets = new ArrayList<NavigatorTarget>();
				this._targets.add(last);
			} else {
				boolean found = false;
				List<NavigatorTarget> targets = new ArrayList<NavigatorTarget>();
				for (NavigatorTarget target : this._targets) {
					if (found || target.getCode().equals(subsiteRoot)) {
						found = true;
						targets.add(target);
					}
				}
				this._targets = targets;
			}
		}
	}
	
	private NavigatorTarget getCurrentTarget() {
		NavigatorTarget item = (NavigatorTarget) _targets.get(_index);
		item.setRequestContext(this._reqCtx);
		return item;
	}
	
	@Override
	public int doEndTag() throws JspException {
		this.pageContext.removeAttribute(this.getVar());
		return super.doEndTag();
	}
	
	@Override
	public void release() {
		_targets = null;
		_reqCtx = null;
		_var = null;
	}
	
	private String getBreadCrumbsNavSpec() {
		NavigatorExpression expression = new NavigatorExpression();
		expression.setSpecId(NavigatorExpression.SPEC_CURRENT_PAGE_ID);
		expression.setOperatorId(NavigatorExpression.OPERATOR_PATH_ID);
		return expression.toString();
	}
	
	/**
	 * Restituisce il nome della variabile con cui viene inserito 
	 * il target corrente nel contesto della pagina.
	 * @return Il nome della variabile caratterizzante il target corrente.
	 */
	public String getVar() {
		return _var;
	}
	/**
	 * Setta il nome della variabile con cui viene inserito 
	 * il target corrente nel contesto della pagina.
	 * @param var Il nome della variabile caratterizzante il target corrente.
	 */
	public void setVar(String var) {
		this._var = var;
	}
	
	private List<NavigatorTarget> _targets;
	private int _index;
	private String _var;
	private RequestContext _reqCtx;
	
}