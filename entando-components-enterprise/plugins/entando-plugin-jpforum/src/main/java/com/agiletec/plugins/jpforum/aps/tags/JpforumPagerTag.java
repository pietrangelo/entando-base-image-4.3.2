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
package com.agiletec.plugins.jpforum.aps.tags;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts2.util.SubsetIteratorFilter;
import org.apache.struts2.views.jsp.StrutsBodyTagSupport;

import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.apsadmin.tags.util.ComponentPagerVO;
import com.agiletec.plugins.jpforum.aps.tags.util.JpforumPagerTagHelper;
import com.opensymphony.xwork2.util.ValueStack;

public class JpforumPagerTag extends StrutsBodyTagSupport {
	
	public int doStartTag() throws JspException {
        Object source = this.findValue(_sourceAttr);
        ServletRequest request =  this.pageContext.getRequest();
        
		ValueStack stack = this.getStack();
		ComponentPagerVO compPagerVo = new ComponentPagerVO(stack);
		try {
			JpforumPagerTagHelper helper = new JpforumPagerTagHelper();
			IPagerVO pagerVo = helper.getPagerVO((Collection)source, this._countAttr, this.isAdvanced(), this.getOffset(), request);
			compPagerVo.initPager(pagerVo);
			stack.getContext().put(this.getObjectName(), compPagerVo);
            stack.setValue("#attr['" + this.getObjectName() + "']", compPagerVo, false);
		} catch (Throwable t) {
			throw new JspException("Errore in creazione del paginatore", t);
		}
		
        _subsetIteratorFilter = new SubsetIteratorFilter();
        _subsetIteratorFilter.setCount(this._countAttr);
        _subsetIteratorFilter.setDecider(null);
        _subsetIteratorFilter.setSource(source);
        _subsetIteratorFilter.setStart(compPagerVo.getBegin());
        _subsetIteratorFilter.execute();
        this.getStack().push(_subsetIteratorFilter);
        if (getId() != null) {
            pageContext.setAttribute(getId(), _subsetIteratorFilter);
        }
        return EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws JspException {
        this.getStack().pop();
        _subsetIteratorFilter = null;
        return EVAL_PAGE;
    }
    
    public void setCount(int count) {
        _countAttr = count;
    }
    public void setSource(String source) {
        _sourceAttr = source;
    }
    
    protected String getObjectName() {
		return _objectName;
	}
	public void setObjectName(String objectName) {
		this._objectName = objectName;
	}
	
	protected boolean isAdvanced() {
		return _advanced;
	}
	public void setAdvanced(boolean advanced) {
		this._advanced = advanced;
	}
	
	protected int getOffset() {
		return _offset;
	}
	public void setOffset(int offset) {
		this._offset = offset;
	}
    
	private int _countAttr;
    private String _sourceAttr;
    
    private SubsetIteratorFilter _subsetIteratorFilter = null;
    
    private String _objectName;
	
	private int _offset;
	private boolean _advanced;
	
}