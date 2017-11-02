<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/search.jsp" />
	<hr />
	
	<s:if test="#section != null">
		<%-- INCLUSIONE BRICIOLE --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/previous_sections.jsp" />
		<hr />
		
		<%-- INCLUSIONE SEZIONE_CORRENTE --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/inc/currentSectionInfo.jsp" />
		<hr />
		
		<%-- INCLUSIONE TABELLA SOTTO_SEZIONI --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/section/inc/subSections.jsp" />
		<hr />
		
		<%-- messaggi di errore vari --%>
		<s:if test="hasActionErrors()">
			<div class="jpforum_block">
				<div class="message message_error">
					<h<s:property value="jpforumTitle2" /> class="title_level2"><s:text name="message.title.ActionErrors" /></h<s:property value="jpforumTitle2" />>	
					<ul>
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
					</ul>
				</div>
			</div> 
			<hr />
		</s:if>
		
		<s:if test="hasFieldErrors()">
			<div class="jpforum_block">
				<div class="message message_error">
					<h<s:property value="jpforumTitle2" /> class="title_level2"><s:text name="message.title.FieldErrors" /></h<s:property value="jpforumTitle2" />>	
					<ul>
						<s:iterator value="fieldErrors">
							<s:iterator value="value">
							<li><s:property escape="false" /></li>
							</s:iterator>
						</s:iterator>
					</ul>
				</div>
			</div>
			<hr />
		</s:if>
		
		<s:if test="hasActionMessages()">
			<div class="jpforum_block">
				<div class="message message_confirm">
					<h<s:property value="jpforumTitle2" /> class="title_level2"><s:text name="messages.confirm" /></h<s:property value="jpforumTitle2" />>	
					<ul>
						<s:iterator value="actionMessages">
							<li><s:property/></li>
						</s:iterator>
					</ul>
				</div>
			</div>
			<hr />
		</s:if>
		
		<div class="jpforum_block">
			<s:set var="posts" value="currentThread.postCodes" />
			<s:set var="count" value="postsPerPage" scope="request" />
			<s:set var="pagerActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/viewThread.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter></wp:action>#jpforum_thread_list</s:set>
			<s:set var="lastPost" value="%{getPost(#posts[#posts.size-1])}"/>
			<s:set var="viewLastPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#lastPost.code" /></wp:parameter></wp:action>#<s:property value="%{jpforumPostHtmlId}" /><s:property value="#lastPost.code" /></s:set>
			
			<form  action="<s:property value="%{#pagerActionPath}" escape="false" />" method="post" >
				<%-- THREAD TITLE --%>
				<h<s:property value="jpforumTitle2" /> class="title_level2"><wp:i18n key="jpforum_TITLE_THREAD" />: <s:property value="currentThread.post.title" /></h<s:property value="jpforumTitle2" />>
				<p>
					<s:property value="#posts.size" />&#32;<wp:i18n key="jpforum_THREAD_MESSAGES_IN_THIS" />, <a href="<s:property value="#viewLastPostActionPath" escape="false" />"><wp:i18n key="jpforum_THREAD_THE_LAST_POST" /></a>&#32;<wp:i18n key="jpforum_THREAD_WAS_WRITTEN" />&#32;<s:date name="#lastPost.postDate" nice="true" /> 
				</p>
			
				<%-- moderator tools --%>		
				<s:if test="%{allowedOnModerateThread}">
					<div class="jpforum_block jpforum_tools_container">
						<h<s:property value="jpforumTitle3" /> class="title_level3"><wp:i18n key="jpforum_TITLE_THREAD_MOD_TOOLS" /></h<s:property value="jpforumTitle3" />>
						<p>
							<s:set var="changeStatusActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/changeStatus.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter></wp:action></s:set>
							<s:set var="changePinActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/changePin.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter></wp:action></s:set>
							<s:set var="deleteThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/trashThread.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter></wp:action></s:set>
							<s:if test="currentThread.open">
								<a class="jpforum_thread_close" href="<s:property value="%{#changeStatusActionPath}" escape="false" />"><wp:i18n key="jpforum_THREAD_MOD_CLOSE_IT" /></a>&#32;|&#32;
							</s:if>
							<s:else>
								<a class="jpforum_thread_open" href="<s:property value="%{#changeStatusActionPath}" escape="false" />"><wp:i18n key="jpforum_THREAD_MOD_OPEN_IT" /></a>&#32;|&#32;
							</s:else>
							
							<s:if test="currentThread.pin">
								<a class="jpforum_thread_unpin" href="<s:property value="%{#changePinActionPath}" escape="false" />"><wp:i18n key="jpforum_THREAD_MOD_UNPIN_IT" /></a>&#32;|&#32;
							</s:if>
							<s:else>
								<a class="jpforum_thread_pin" href="<s:property value="%{#changePinActionPath}" escape="false" />"><wp:i18n key="jpforum_THREAD_MOD_PIN_IT" /></a>&#32;|&#32;
							</s:else>
							<a class="jpforum_thread_trash" href="<s:property value="%{#deleteThreadActionPath}" escape="false" />"><wp:i18n key="jpforum_THREAD_MOD_REMOVE_IT" /></a>
						</p>
					</div>
					<hr />
				</s:if>	
				
				<div id="jpforum_thread_list">
	
					<jpforum:subset source="#posts" count="${count}" objectName="groupPosts" advanced="true" offset="5">
						<s:set name="group" value="#groupPosts" />
						<div class="pager">
							<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pagerInfo_posts.jsp" />
							<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
						</div>
						<hr />
						
						<%-- post list --%>
						<s:iterator var="postId" status="status">
							<s:set var="post" value="%{getPost(#postId)}" />
							<s:set name="postCount" value="((#group.currItem -1) * postsPerPage) + #status.count"></s:set>
							<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#post.username" /></wp:parameter></wp:action></s:set>
							
							<div class="jpforum_post_container" id="<s:property value="jpforumPostHtmlId" /><s:property value="#post.code" />">
								<%-- titolo del post--%>
								<h<s:property value="jpforumTitle3" /> class="title_level3">
									<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#post.code" /></wp:parameter></wp:action>#<s:property value="jpforumPostHtmlId" /><s:property value="#post.code" /></s:set>
									<a title="<wp:i18n key="jpforum_PERMALINK" />" href="<s:property value="%{#viewPostActionPath}" escape="false" />"><abbr title="<wp:i18n key="jpforum_POST_LONG" />"><wp:i18n key="jpforum_POST_SHORT" /></abbr>&#32;<s:property value="#postCount"  />&#32;<wp:i18n key="jpforum_OF" />&#32;<s:property value="#posts.size" /></a>&#32;<wp:i18n key="jpforum_WRITTEN_BY" />&#32;<a href="<s:property value="%{#viewUserActionPath}" escape="false" />"><s:property value="%{getUserNick(#post.username)}" /></a>&#32;<wp:i18n key="jpforum_ON_DATE" />&#32;<s:date format="%{#jpforum_time_format}" name="#post.postDate"/> (<s:date name="#post.postDate" nice="true" />)
								</h<s:property value="jpforumTitle3" />>
								
								<%-- corpo del post --%>
								<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/thread/inc/post.jsp" />
								
								<%-- post tools--%>
								<s:if test="%{allowedOnCreateThread || isAllowedOnEditPost(#postId) || (allowedOnModerateThread && (#postId != currentThread.code))}">
									<div class="jpforum_tools_container">
										<p>
											<s:if test="allowedOnCreateThread">
												<s:set var="replyPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/replyPost.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter><wp:parameter name="post"><s:property value="#postId" /></wp:parameter></wp:action></s:set>
												<s:set var="quotePostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/replyPost.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter><wp:parameter name="post"><s:property value="#postId" /></wp:parameter><wp:parameter name="q" value="true" /></wp:action></s:set>
												<a class="jpforum_post_reply" href="<s:property value="%{#replyPostActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_REPLY" /></a>&#32;|&#32;
												<a class="jpforum_post_quote" href="<s:property value="%{#quotePostActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_QUOTE" /></a>
											</s:if>
											<s:if test="%{isAllowedOnEditPost(#postId)}">
												<s:set var="editPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/editPost.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter><wp:parameter name="post"><s:property value="#postId" /></wp:parameter></wp:action></s:set>
												&#32;|&#32;<a class="jpforum_post_edit" href="<s:property value="%{#editPostActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_EDIT" /></a>
											</s:if>
											<s:if test="%{allowedOnModerateThread && #postId != currentThread.code}">
												<s:set var="editPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/trashPost.action" ><wp:parameter name="thread"><s:property value="currentThread.code" /></wp:parameter><wp:parameter name="post"><s:property value="#postId" /></wp:parameter></wp:action></s:set>
												&#32;|&#32;<a class="jpforum_post_trash" href="<s:property value="%{#editPostActionPath}" escape="false" />"><wp:i18n key="jpforum_BUTTON_REMOVE" /></a>
											</s:if>
										</p>
									</div>
								</s:if>
							</div>
							<hr />
						</s:iterator>
						
						<%-- paginatore --%>
						<div class="pager">
							<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
						</div>
						<s:if test="#group.size > #group.max">
							<hr />
						</s:if>
					</jpforum:subset>
				
				</div>
			</form>	
		</div>
		<%-- Login to Post in this thread --%>
		<s:if test="currentThread.open && (#session.currentUser.username.equals('guest'))">
			<div class="jpforum_block">
				<div class="jpforum_tools_container">
					<%//TODO creare link per login page %>
					<p><wp:i18n key="jpforum_POST_LOGIN_TO_POST" /></p>
					<p><a class="jpforum_goto" href="#loginpage-todo"><wp:i18n key="jpforum_GOTO_LOGIN" /></a></p>
				</div>
			</div>
		</s:if>	
	</s:if>
	<s:else>
		<div class="jpforum_block">
			<p>
				<wp:i18n key="jpforum_SECTION_NOT_FOUND" />. (<s:property value="#parameters.section" />)
			</p>
		</div>
	</s:else>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>
	
</div> 