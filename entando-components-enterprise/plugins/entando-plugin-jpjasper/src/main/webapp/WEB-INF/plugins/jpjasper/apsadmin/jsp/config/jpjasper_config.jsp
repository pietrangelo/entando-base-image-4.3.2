<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="jpjasper.title.Configuration" />
    </span>
</h1>

<div id="main">
<s:form action="save" cssClass="form-horizontal">
	<s:if test="hasFieldErrors()">
	<div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h4 class="margin-none"><s:text name="message.title.FieldErrors" /></h4>
			<ul class="margin-base-vertical">
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
	<div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h4 class="margin-none"><s:text name="message.title.ActionErrors" /></h4>
			<ul class="margin-base-vertical">
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionMessages()">
	<div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
		<h4 class="margin-none"><s:text name="messages.confirm" /></h4>
			<ul class="margin-base-vertical">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

    <div class="form-group">
      <div class="col-xs-12">
		<label for="config_baseUrl"><s:text name="config.baseURL" /></label>
                  <div class="input-group">
		<wpsf:textfield name="config.baseURL" id="config_baseURL" value="%{config.baseURL}" cssClass="form-control" />
          <span class="input-group-btn">
		<wpsf:submit  cssClass="btn btn-default" action="ping"  value="%{getText('label.ping')}" />
          </span>
        </div>
	</div>
    </div>
	
    <div class="form-group">
      <div class="col-xs-12">
		<label for="config_reportViewerPageCode"><s:text name="config.reportViewerPageCode"></s:text></label>
		<select name="config.reportViewerPageCode" id="config_reportViewerPageCode" class="form-control">
			<s:iterator id="page" value="freePages">
				<option <s:if test="config.reportViewerPageCode == #page.code">selected="selected"</s:if> 
					value="<s:property value="#page.code"/>"><s:if test="!#page.showable"> [i]</s:if><s:property value="#page.getShortFullTitle(currentLang.code)"/></option>
			</s:iterator>
		</select>
		</div>
    </div>
        
    <div class="form-group">
      <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
        <s:submit type="button" action="save" cssClass="btn btn-primary btn-block">
          <span class="icon fa fa-times"></span>&#32;
          <s:text name="label.save" />
        </s:submit>
      </div>
    </div>

</s:form>
</div>
