<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12">
    <legend><s:text name="jpactiviti.name" /></legend>

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

</fieldset>