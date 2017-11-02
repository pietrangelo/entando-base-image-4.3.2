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
package org.entando.entando.plugins.jpsubsites.aps.system.services.page.widget;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.widget.NavigatorExpression;
import com.agiletec.aps.system.services.page.widget.NavigatorParser;
import com.agiletec.aps.system.services.page.widget.NavigatorTarget;
import com.agiletec.aps.system.services.user.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class BreadcrumbsNavigatorParser extends NavigatorParser {
	
	@Override
	public List<NavigatorTarget> parseSpec(String spec, IPage currentPage, UserDetails currentUser) {
		List<NavigatorTarget> targets = new ArrayList<NavigatorTarget>();
		if (null != spec && spec.length() > 0) {
			String[] expressions = spec.split("\\"+EXPRESSION_SEPARATOR);
			for (int i=0; i<expressions.length; i++) {
				String expression = expressions[i].trim();
				if (expression.length() > 0) {
					NavigatorExpression navExpression = new NavigatorExpression(expression);
					targets = this.parseSubSpec(navExpression, currentPage, targets, currentUser);
				}
			}
		}
		return targets;
	}
	
	private List<NavigatorTarget> parseSubSpec(NavigatorExpression navExpression, IPage page, List<NavigatorTarget> targets, UserDetails user) {
		IPage basePage = page;
		if (null == basePage) {
			return targets;
		}
		targets = this.processBasePage(navExpression, basePage, targets, user);
		return targets;
	}
	
	private List<NavigatorTarget> processBasePage(NavigatorExpression navExpression, IPage basePage, 
			List<NavigatorTarget> targets, UserDetails user) {
		IPage page = basePage;
		int index = targets.size();
		int limit = 0;
		if (this.isUserAllowed(user, page)) {
			targets.add(index, new NavigatorTarget(page, 0));
		}
		while (!page.isRoot() && limit < 20) {
			page = page.getParent();
			if (this.isUserAllowed(user, page)) {
				targets.add(index, new NavigatorTarget(page, 0));
			}
			limit++;
		}
		return targets;
	}
	
	private boolean isUserAllowed(UserDetails user, IPage page) {
		return this.getAuthManager().isAuth(user, page);
	}
	
}
