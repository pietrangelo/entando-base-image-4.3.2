<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset class="col-xs-12">
    <legend><s:text name="jpsocial.name" /></legend>
    <div class="form-group">
        <div class="checkbox">
            <s:set name="jpsocial_paramName" value="'jpsocial_providerDevMode'" />
            <input type="checkbox" class="radiocheck" id="<s:property value="#jpsocial_paramName"/>" name="<s:property value="#jpsocial_paramName"/>" value="true" <s:if test="systemParams.get(#jpsocial_paramName)">checked="checked"</s:if> />
            <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
            <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.providerDevMode.active" /></label>
        </div>
    </div>

    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_facebookConsumerKey'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.facebookConsumerKey" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>
    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_facebookConsumerSecret'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.facebookConsumerSecret" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_twitterConsumerKey'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.twitterConsumerKey" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>
    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_twitterConsumerSecret'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.twitterConsumerSecret" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_googleConsumerKey'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.googleConsumerKey" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>
    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_googleConsumerSecret'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.googleConsumerSecret" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_youtubeDeveloperKey'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.youtubeDeveloperKey" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_bitlyUsername'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.bitlyUsername" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_bitlyApikey'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.bitlyApikey" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_linkedinConsumerKey'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.linkedinConsumerKey" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>
    <div class="form-group">
        <s:set name="jpsocial_paramName" value="'jpsocial_linkedinConsumerSecret'" />
        <label for="<s:property value="#jpsocial_paramName" />"><s:text name="jpsocial.hookpoint.configSystemParams.linkedinConsumerSecret" /></label>
        <wpsf:textfield name="%{#jpsocial_paramName}" id="%{#jpsocial_paramName}" value="%{systemParams.get(#jpsocial_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsocial_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="alert alert-info">
        <s:text name="jpsocial.hookpoint.configSystemParams.reloadConfigurationInfo" />
    </div>

</fieldset>
