<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="references['jpsubsitesSubsiteManagerUtilizers']">
<s:set var="referencingSubsites" value="references['jpsubsitesSubsiteManagerUtilizers']" />
<s:include value="/WEB-INF/plugins/jpsubsites/apsadmin/jsp/portal/include/referencingSubsites.jsp" />
</s:if>