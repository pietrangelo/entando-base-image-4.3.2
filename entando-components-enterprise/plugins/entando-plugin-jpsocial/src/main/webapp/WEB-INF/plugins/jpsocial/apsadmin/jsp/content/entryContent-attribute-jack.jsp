<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<s:if test="(!#attributeTracer.monoListElement) && (!#attributeTracer.listElement) && (!#attributeTracer.compositeElement)">
	<%-- SocialHypertext --%>
	<s:if test="#attribute.type == 'SocialHypertext'">
			<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
                        <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
			<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" />
	</s:if>
	
	<%-- SocialText --%>
	<s:elseif test="#attribute.type == 'SocialText'">
			<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
			<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" />
                            
	</s:elseif>
	
	<%-- SocialLongtext --%>
	<s:elseif test="#attribute.type == 'SocialLongtext'">
			<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
			<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
	</s:elseif>
	
  <%-- SocialImage --%>  
  <s:elseif test="#attribute.type == 'SocialImage'">
			<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
			<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
			<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
	</s:elseif>
    
   <%-- SocialAttach --%>  
  <s:elseif test="#attribute.type == 'SocialAttach'">
			<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />"><s:property value="#attribute.name" /><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /></label>
			<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
			<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
	</s:elseif>   
    
	<%-- SocialMonotext --%>
	<s:elseif test="#attribute.type == 'SocialMonotext'">
			<label class="display-block" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
                            <s:property value="#attribute.name" />
                            <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />
                        </label>
			<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
			<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" />
	</s:elseif>
</s:if>
<s:else>
	<%-- monolist || list || compo --%>
	<s:if test="#attributeTracer.monoListElement || #attributeTracer.listElement || #attributeTracer.compositeElement">
		<%-- SocialHypertext --%>
		<s:if test="#attribute.type == 'SocialHypertext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
				<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
		</s:if>
		<%-- SocialText --%>
		<s:elseif test="#attribute.type == 'SocialText'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
				<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
		</s:elseif>
		<%-- SocialLongtext --%>
		<s:elseif test="#attribute.type == 'SocialLongtext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
				<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
		</s:elseif>
		<%-- SocialMonotext --%>
		<s:elseif test="#attribute.type == 'SocialMonotext'">
				<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" /> 
				<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
		</s:elseif>
    <%-- SocialImage --%>
    <s:elseif test="#attribute.type == 'SocialImage'">
				<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" /> 
				<s:include value="/WEB-INF/plugins/jpsocial/apsadmin/jsp/content/modules/include/jpsocial-share-menu.jsp" /> 
		</s:elseif>
	</s:if>
</s:else>