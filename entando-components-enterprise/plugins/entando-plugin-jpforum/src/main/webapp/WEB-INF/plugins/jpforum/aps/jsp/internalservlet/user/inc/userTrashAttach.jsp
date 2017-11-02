<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:set var="deleteAttachActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/Attach/deleteAttach.action" /></s:set>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/attach/trashAttach.jsp" />
