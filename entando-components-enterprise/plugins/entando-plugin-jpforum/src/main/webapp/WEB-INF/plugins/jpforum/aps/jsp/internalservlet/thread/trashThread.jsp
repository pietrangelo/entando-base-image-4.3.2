<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ page contentType="charset=UTF-8" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 
<s:set var="section" value="currentSection" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum showlet">
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>

	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />
	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/breadcrumbs.jsp" />
	<hr />
	
	<div class="jpforum_block">
		<s:set var="deleteThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/deleteThread.action" /></s:set>
		<form action="<s:property value="%{#deleteThreadActionPath}" escape="false" />" method="post">
			
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_THREAD_REMOVE" /></h<s:property value="jpforumTitle2" />>
		
			<s:if test="hasActionErrors()">
				<div class="message message_error">
					<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="message.title.ActionErrors" /></h<s:property value="jpforumTitle3" />> 
					<ul>
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
					</ul>
				</div>
			</s:if>
		
			<s:if test="hasFieldErrors()">
				<div class="message message_error">
					<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="message.title.FieldErrors" /></h<s:property value="jpforumTitle3" />>	
					<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
						<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
					</ul>
				</div>
			</s:if>
			
			<s:if test="hasActionMessages()">
				<div class="message message_confirm">
					<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="messages.confirm" /></h<s:property value="jpforumTitle3" />> 
					<ul>
						<s:iterator value="actionMessages">
							<li><s:property/></li>
						</s:iterator>
					</ul>
				</div>
			</s:if>
				
			<p>
				<wpsf:hidden name="strutsAction" />
				<wpsf:hidden name="section" value="%{getCurrentSection().code}" />
				<wpsf:hidden name="thread" />
				<wpsf:hidden name="post" />
			</p>
			
			<s:set name="thread" value="currentThread" />
			<s:set name="lastPost" value="%{getPost(#thread.postCodes[#thread.postCodes.size - 1])}" />
			<%--
				<p><wp:i18n key="jpforum_THREAD_SUMMARY" /></p>
			 --%>
			<dl>
				<dt><wp:i18n key="jpforum_LABEL_POST_TITLE" /></dt>
				<dd><s:property value="#thread.post.title"/></dd>
				
				<dt><wp:i18n key="jpforum_LABEL_THREAD_REPLIES" /></dt>
				<dd><s:property value="#thread.postCodes.size -1 "/></dd>
				
				<dt><wp:i18n key="jpforum_LABEL_THREAD_VISITED" /></dt>
				<dd><s:property value="%{getViews(#thread.code)}" /></dd>
				
				<dt><wp:i18n key="jpforum_LABEL_THREAD_LAST_POST" /></dt>
				<dd><s:date format="%{#jpforum_time_format}" name="#lastPost.postDate"/> (<s:date name="#lastPost.postDate" nice="true" />)</dd>
				
			</dl>
	 		
			<p><wp:i18n key="jpforum_THREAD_CONFIRM_REMOVE" /></p>
			
			<p>
				<input class="button" type="submit" value="<wp:i18n key="jpforum_BUTTON_CONFIRM_REMOVE" />" />
				&#32;|&#32;
				<s:set var="viewThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/viewThread.action" ><wp:parameter name="thread"><s:property value="#thread.code" /></wp:parameter></wp:action></s:set>
				<a href="<s:property value="%{#viewThreadActionPath}" escape="false" />">
					<wp:i18n key="jpforum_GOTO_CANCEL_TO_THREAD" />
				</a>			
			</p>
		 
			<%--
				<s:property value="#thread.code"/> |  
				TITOLO: [<s:property value="#thread.post.title"/>] | 
				RISPOSTE: [<s:property value="#thread.postCodes.size -1 "/>] | 
				VISTO: <s:property value="%{getViews(#thread.code)}" /> | 
				INFO_LAST_POST: <s:property value="#lastPost.code" />
				SCRITTO_DA <a href="#"><s:property value="#lastPost.username" /></a> IL <s:date format="dd MMM yyyy hh:mm:ss" name="#lastPost.postDate"/>(<s:date name="#lastPost.postDate" nice="true" />)
			 --%>
			 
		</form>
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>
