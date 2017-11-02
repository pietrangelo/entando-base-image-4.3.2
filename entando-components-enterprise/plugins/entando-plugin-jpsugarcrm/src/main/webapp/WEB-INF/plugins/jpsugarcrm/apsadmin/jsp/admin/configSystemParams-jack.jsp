<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset class="col-xs-12">
    <legend><s:text name="jpsugarcrm.name" /></legend>

    <div class="form-group">
        <s:set name="jpsugarcrm_paramName" value="'jpsugarcrm_sugarCRM_baseUrl'" />
        <label for="<s:property value="#jpsugarcrm_paramName" />"><s:text name="jpsugarcrm.hookpoint.configSystemParams.sugarBaseURL" /></label>
        <wpsf:textfield  name="%{#jpsugarcrm_paramName}" id="%{#jpsugarcrm_paramName}" value="%{systemParams.get(#jpsugarcrm_paramName)}" cssClass="form-control" />
        <wpsf:hidden name="%{#jpsugarcrm_paramName + externalParamMarker}" value="true"/>
    </div>

    <div class="form-group">
        <div class="checkbox">
            <s:set name="jpsugarcrm_paramName" value="'jpsugarcrm_sugarCRM_ldap'" />
            <input type="checkbox" class="radiocheck" id="<s:property value="#jpsugarcrm_paramName"/>" name="<s:property value="#jpsugarcrm_paramName"/>" value="true" <s:if test="systemParams.get(#jpsugarcrm_paramName)">checked="checked"</s:if> />
            <wpsf:hidden name="%{#jpsugarcrm_paramName + externalParamMarker}" value="true"/>
            <label for="<s:property value="#jpsugarcrm_paramName" />"><s:text name="jpsugarcrm.hookpoint.configSystemParams.sugarCrmLdap.active" /></label>
        </div>
    </div>

</fieldset>