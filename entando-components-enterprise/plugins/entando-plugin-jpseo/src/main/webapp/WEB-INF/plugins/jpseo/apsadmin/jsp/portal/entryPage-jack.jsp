<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<div>

<fieldset><legend><s:text name="jpseo.label.config" /></legend>
	<p>
		<label for="friendlyCode" class="basic-mint-label"><s:text name="jpseo.label.friendlyCode" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="friendlyCode" value="%{#attr.friendlyCode}" id="friendlyCode" cssClass="text" />
	</p>
<s:if test="strutsAction != 3">


	<p>
		<label for="xmlConfig" class="basic-mint-label"><s:text name="jpseo.label.xmlConfig" />:</label>
		<wpsf:textarea useTabindexAutoIncrement="true" cols="50" rows="5" name="xmlConfig" id="xmlConfig" cssClass="text" value="%{#attr.xmlConfig}" />
	</p>

		<s:set var="seoParametersExampleVar" >
	<s:text name="jpseo.label.example" />:
		<seoparameters>
			<parameter key="key1"><![CDATA[VALUE_1]]></parameter>
			<parameter key="key2">VALUE_2</parameter>
			<parameter key="key3">
				<property key="en">VALUE_3 EN</property>
				<property key="it">VALUE_3 IT</property>
			</parameter>
			<parameter key="key4">VALUE_4</parameter>
			<parameter key="key5">
				<property key="en">VALUE_5 EN</property>
				<property key="it"><![CDATA[VALUE_5 IT]]></property>
				<property key="fr">VALUE_5 FR</property>
			</parameter>
			<parameter key="key6"><![CDATA[VALUE_6]]></parameter>
		</seoparameters>
		</s:set>

		<pre>
			<s:property value="#seoParametersExampleVar" escapeXml="false" />
		</pre>

</s:if>
</fieldset>

	<fieldset><legend><s:text name="jpseo.label.descriptions" /></legend>
		<s:iterator value="langs" var="lang">
		<s:set var="mykey" value="'description_lang'+#lang.code" />
		<p>
			<label for="description_lang<s:property value="code" />" class="basic-mint-label"><span class="monospace">(<s:property value="#lang.code" />)</span> <s:text name="jpseo.label.pageDescription" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="%{'description_lang'+code}" id="%{'description_lang'+code}" value="%{#attr[#mykey]}" cssClass="text" />
		</p>
		</s:iterator>

		<ul class="noBullet">
			<li>
				<input type="checkbox" value="true"  name="useExtraDescriptions" id="useExtraDescriptions" class="radiocheck" <s:if test="#attr.useExtraDescriptions"> checked="checked" </s:if>><label for="useExtraDescriptions"><abbr lang="en" title="<s:text name="name.SEO.full" />"><s:text name="name.SEO.short" /></abbr>:&#32;<s:text name="jpseo.label.useBetterDescriptions" /></label>
			</li>
		</ul>
	</fieldset>

</div>

