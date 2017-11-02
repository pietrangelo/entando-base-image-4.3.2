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
package com.agiletec.plugins.jpforum.aps.internalservlet.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import com.opensymphony.xwork2.util.LocalizedTextUtil;

public class TokenInterceptor extends org.apache.struts2.interceptor.TokenInterceptor {

	private static final Logger _logger =  LoggerFactory.getLogger(TokenInterceptor.class);

	@Override
    protected String handleInvalidToken(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        String errorMessage = LocalizedTextUtil.findText(this.getClass(), "struts.messages.invalid.token",
                invocation.getInvocationContext().getLocale(),
                "The form has already been processed or no token was supplied, please try again.", new Object[0]);

        String message = LocalizedTextUtil.findText(this.getClass(), "struts.messages.invalid.token.message",
                invocation.getInvocationContext().getLocale(),
                "Stop double-submission of forms.", new Object[0]);

        if (action instanceof ValidationAware) {
        	if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_ERROR_MESSAGE)) {
        		((ValidationAware) action).addActionError(errorMessage);
        	} else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_ACTION_MESSAGE)) {
        		((ValidationAware) action).addActionMessage(message);
        	} else if (this.getTypeMessages().equalsIgnoreCase(TYPE_RETURN_NONE_MESSAGE)) {
        		_logger.debug("TokenInterceptor without message for class {}", invocation.getAction().getClass().getName());
        	}
        } else {
            _logger.warn(errorMessage);
        }
        return INVALID_TOKEN_CODE;
    }
	
	public void setTypeMessages(String typeMessages) {
		this._typeMessages = typeMessages;
	}
	public String getTypeMessages() {
		return _typeMessages;
	}

	private String _typeMessages;

	public static final String TYPE_RETURN_ACTION_ERROR_MESSAGE = "error";
	public static final String TYPE_RETURN_ACTION_MESSAGE = "message";
	public static final String TYPE_RETURN_NONE_MESSAGE = "none";
	
}
