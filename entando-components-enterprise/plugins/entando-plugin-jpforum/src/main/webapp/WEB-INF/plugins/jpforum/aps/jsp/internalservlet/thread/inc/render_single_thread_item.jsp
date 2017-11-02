<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<%-- 
In order to work, please setup this variables before including this jsp:
-------------
NAME / WHAT 
-------------
thread / thread object
lastPost / this is the last post of the "thread"
viewThreadActionPath / path to Struts2 Action for rendering the thread
viewPostActionPath / action to the last post of the current thread
viewUserActionPath / path to the profile of author of the thread 
viewLastPostUserActionPath / path to the profile of the last post of the thread
--%>
<div class="thread thread_open_<s:property value="#thread.open" />">
	<h<s:property value="jpforumTitle4" /> class="thread_title title_level4"><a href="<s:property value="%{#viewThreadActionPath}" escape="false" />"><s:property value="#thread.post.title"/></a> (<s:if test="#thread.open"><wp:i18n key="jpforum_LABEL_THREAD_STATUS_OPEN" /></s:if><s:else><wp:i18n key="jpforum_LABEL_THREAD_STATUS_CLOSED" /></s:else>)&#32;<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpforum/Rss/showThread.action?threadId=<s:property value="#thread.post.code" />&amp;lang=<s:property value="#currentLang" />" title="<wp:i18n key="jpforum_RSS_LONG" />&#32;<s:property value="#thread.post.title" />" class="rss_icon"><img src="<wp:resourceURL />plugins/jpforum/static/img/icons/rss.png" alt="RSS"/></a></h<s:property value="jpforumTitle4" />>
	<dl class="thread_info">
		<dt><wp:i18n key="jpforum_LABEL_THREAD_REPLIES" /></dt>     
			<dd><strong><s:property value="#thread.postCodes.size -1 "/></strong>&#32;<wp:i18n key="jpforum_THREAD_REPLIES" /></dd>
		<dt><wp:i18n key="jpforum_LABEL_THREAD_VISITED" /></dt>
			<dd><strong><s:property value="%{getViews(#thread.code)}" /></strong>&#32;<wp:i18n key="jpforum_THREAD_VISITS" /></dd>
		<dt><wp:i18n key="jpforum_THREAD_INSERT_DATE" /></dt>
			<dd class="break" title="<s:date name="#thread.post.postDate" nice="true" />"><s:date format="%{#jpforum_time_format}" name="#thread.post.postDate"/></dd>
		<dt><wp:i18n key="jpforum_THREAD_AUTHOR" /></dt>
			<dd><a href="<s:property value="%{#viewUserActionPath}" escape="false" />"><s:property value="%{getUserNick(#thread.post.username)}" /></a></dd>
		<dt><wp:i18n key="jpforum_LABEL_THREAD_LAST_POST" /></dt> 
			<dd class="break">
				<a href="<s:property value="%{#viewPostActionPath}" escape="false" />"><wp:i18n key="jpforum_LABEL_THREAD_LAST_POST" /></a> <span title="<s:date format="%{#jpforum_time_format}" name="#lastPost.postDate"/>" ><s:date name="#lastPost.postDate" nice="true" /></span>
				<wp:i18n key="jpforum_WRITTEN_BY" />&#32;<a href="<s:property value="%{#viewLastPostUserActionPath}" escape="false" />"><s:property value="%{getUserNick(#lastPost.username)}" /></a>
			</dd> 
	</dl>
</div>