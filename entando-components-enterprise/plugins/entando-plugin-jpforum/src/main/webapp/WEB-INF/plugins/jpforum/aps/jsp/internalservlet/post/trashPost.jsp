<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:set var="currentLang"><wp:info key="currentLang" /></s:set>
<s:set var="section" value="currentSection" />
<s:set var="currentPost" value="%{getPost(post)}" />
<s:set var="currentThread" value="%{getThread(#currentPost.threadId)}" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum showlet">

	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>

	<%--
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />>
	--%>

	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />

	<s:if test="#section != null">
		<%-- INCLUSIONE BRICIOLE --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/breadcrumbs.jsp" />
		<hr />
	</s:if>

	<div class="jpforum_block">
		<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_POST_TRASH" /></h<s:property value="jpforumTitle2" />>

		<s:set var="deletePostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/deletePost.action" /></s:set>
		<form action="<s:property value="deletePostActionPath" escape="false" />" method="post">
			<div class="jpforum_block">
				<p class="noscreen">
					<wpsf:hidden name="strutsAction" />
					<wpsf:hidden name="post" />
					<wpsf:hidden name="thread" />
				</p>
				<dl>
					<dt><wp:i18n key="jpforum_LABEL_POST_TITLE" /></dt>
						<dd>
							<s:if test="#currentPost.title != null && #currentPost.length > 0"><em><s:property value="#currentPost.title"/></em></s:if>
							<s:else><wp:i18n key="jpforum_POST_UNTITLED" /></s:else>
						</dd>
					<s:if test="#currentPost.text != null">
						<dt><wp:i18n key="jpforum_LABEL_POST_BODY" /></dt>
						<dd>
							<s:property escape="false" value="%{getHtml(#currentPost.text)}"/>
						</dd>
					</s:if>
					<dt><wp:i18n key="jpforum_LABEL_THREAD"></wp:i18n></dt>
						<dd><s:property value="#currentThread.post.title" /></dd>
				</dl>
			</div>

			<p><wp:i18n key="jpforum_POST_CONFIRM_REMOVE" /></p>
			<p>
				<input class="button" type="submit" value="<wp:i18n key="jpforum_BUTTON_CONFIRM_REMOVE" />" />
			</p>
			<p>
				<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#currentPost.code" /></wp:parameter><wp:parameter name="thread"><s:property value="#currentPost.threadId" /></wp:parameter></wp:action>#<s:property value="jpforumPostHtmlId" /><s:property value="#currentPost.code" /></s:set>
				<a href="<s:property value="%{#viewPostActionPath}" escape="false" />"><wp:i18n key="jpforum_GOTO_CANCEL_TO_THREAD" /></a>
			</p>
		</form>
	</div>

	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>

</div>