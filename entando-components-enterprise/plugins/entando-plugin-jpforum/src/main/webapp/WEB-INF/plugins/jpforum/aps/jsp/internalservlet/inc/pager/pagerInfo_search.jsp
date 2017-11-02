<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<p><wp:i18n key="jpforum_PAGER_SEARCH_RETURNED" />&#32;<strong><s:property value="#group.size" /></strong>&#32;<wp:i18n key="jpforum_PAGER_SEARCH_RESULTS" />.</p>
<p><wp:i18n key="jpforum_YOU_ARE_AT_PAGE" />&#32;<strong><s:property value="#group.currItem" /></strong>&#32;<wp:i18n key="jpforum_OF" />&#32;<s:property value="#group.maxItem" />.</p>