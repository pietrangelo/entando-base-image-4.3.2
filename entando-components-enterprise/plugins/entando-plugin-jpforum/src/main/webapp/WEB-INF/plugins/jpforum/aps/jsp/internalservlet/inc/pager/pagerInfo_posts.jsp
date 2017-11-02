<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<p><wp:i18n key="jpforum_YOU_ARE_AT_PAGE" />&#32;<strong><s:property value="#group.currItem" />&#32;<wp:i18n key="jpforum_OF" />&#32;<s:property value="#group.maxItem" /></strong>&#32;<wp:i18n key="jpforum_PAGER_OF_POST_LIST" />.</p>
<p><wp:i18n key="jpforum_PAGER_THERE_ARE" />&#32;<strong><s:property value="#group.size" /></strong>&#32;<wp:i18n key="jpforum_PAGER_TOTAL_POSTS" />.</p>