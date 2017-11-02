<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
	<legend><s:text name="jpremotenotifier.name" /></legend>
	
	<p>
        <s:set name="jpremotenotifier_paramName" value="'jpremotenotifier_alarm_recipients'" />
        <label for="<s:property value="#jpremotenotifier_paramName"/>" class="basic-mint-label"><s:text name="jpremotenotifier.hookpoint.configSystemParams.alarmRecipients" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpremotenotifier_paramName}" id="%{#jpremotenotifier_paramName}" value="%{systemParams.get(#jpremotenotifier_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpremotenotifier_paramName + externalParamMarker}" value="true"/>
	</p>
	
	<p>
        <s:set name="jpremotenotifier_paramName" value="'jpremotenotifier_alarm_senderCode'" />
        <label for="<s:property value="#jpremotenotifier_paramName"/>" class="basic-mint-label"><s:text name="jpremotenotifier.hookpoint.configSystemParams.alarmSenderCode" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpremotenotifier_paramName}" id="%{#jpremotenotifier_paramName}" value="%{systemParams.get(#jpremotenotifier_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpremotenotifier_paramName + externalParamMarker}" value="true"/>
	</p>
	
	<p>
        <s:set name="jpremotenotifier_paramName" value="'jpremotenotifier_alarm_delay'" />
        <label for="<s:property value="#jpremotenotifier_paramName"/>" class="basic-mint-label"><s:text name="jpremotenotifier.hookpoint.configSystemParams.alarmDelay" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpremotenotifier_paramName}" id="%{#jpremotenotifier_paramName}" value="%{systemParams.get(#jpremotenotifier_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpremotenotifier_paramName + externalParamMarker}" value="true"/>
	</p>
	
</fieldset>