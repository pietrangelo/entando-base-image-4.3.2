<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
	<legend><s:text name="jpmemcached.name" /></legend>
	<p>
		<s:set name="jpmemcached_paramName" value="'jpmemcached_memcachedAddress'" />
        <label for="<s:property value="#jpmemcached_paramName" />" class="basic-mint-label"><s:text name="jpmemcached.hookpoint.configSystemParams.jpmemcached_memcachedAddress" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpmemcached_paramName}" id="%{#jpmemcached_paramName}" value="%{systemParams.get(#jpmemcached_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpmemcached_paramName + externalParamMarker}" value="true"/>
	</p>
  
	<p>
		<s:text name="jpmemcached.hookpoint.configSystemParams.reloadConfigurationInfo" />
	</p>
	
</fieldset>