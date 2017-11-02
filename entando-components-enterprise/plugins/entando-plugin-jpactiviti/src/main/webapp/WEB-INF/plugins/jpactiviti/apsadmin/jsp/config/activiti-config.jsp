<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"  %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core"  %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<s:text name="menu.settings.jpactiviti" /></span>
</h1>

<s:form action="save">

	<s:if test="hasActionMessages()">
		<div class="alert alert-success alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

    <div class="form-group">
        <s:set name="jpactiviti_paramName" value="'jpactiviti_explorer_baseUrl'" />
        <label for="<s:property value="#jpactiviti_paramName" />"><s:text name="jpactiviti.hookpoint.configSystemParams.explorerBaseURL" /></label>
        <wpsf:textfield  name="%{#jpactiviti_paramName}" id="%{#jpactiviti_paramName}" value="%{systemParams.get(#jpactiviti_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpactiviti_paramName + externalParamMarker}" value="true"/>
		<span class="help-block"><s:text name="jpactiviti.help.config.explorer" /></span>
    </div>
	
    <div class="form-group">
        <s:set name="jpactiviti_paramName" value="'jpactiviti_rest_baseUrl'" />
        <label for="<s:property value="#jpactiviti_paramName" />"><s:text name="jpactiviti.hookpoint.configSystemParams.restBaseURL" /></label>
        <wpsf:textfield  name="%{#jpactiviti_paramName}" id="%{#jpactiviti_paramName}" value="%{systemParams.get(#jpactiviti_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpactiviti_paramName + externalParamMarker}" value="true"/>
		<span class="help-block"><s:text name="jpactiviti.help.config.rest" /></span>
    </div>


	<div class="form-horizontal">
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-floppy-o"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</div>

</s:form>