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
	
	<div class="jpforum_search">
		
		<div class="jpforum_block">
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_SEARCH_POSTS" /></h<s:property value="jpforumTitle2"/>>
			<s:set var="searchActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Search/search.action" /><s:if test="%{null != posts && posts.size > 0}">#jpforum_search_results</s:if></s:set>
			<form action="<s:property value="searchActionPath" escape="false" />" method="post">
				<div class="jpforum_block">
					<%-- normal search --%>
					<p>
						<label for="jpforum_search_text"><wp:i18n key="jpforum_LABEL_SEARCH_TEXT" /></label>:<br />
						<s:textfield id="jpforum_search_text" name="textToFind" cssClass="text" /><input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_SEARCH" />" />
					</p>
				</div>
				
				<hr />
				
				<s:set var="textTest" value="textToFind" />
				<p>
					<s:if test='%{!( (#textTest != null) && (#textTest.length() != null) && (#textTest.length() > 0) )}'> 
							<wp:i18n key="jpforum_SEARCH_NO_PARAMETER_INSERTED" />
					</s:if>
					<s:else>
						<wp:i18n key="jpforum_SEARCH_YOU_SEARCHED" />: <em><s:property value="#textTest" /></em>.
					</s:else>
				</p>
				
				<div id="jpforum_search_results">
					<s:if test="%{null != posts && posts.size > 0}">
							<s:set var="count" value="postsPerPage" scope="request" />
							<jpforum:subset source="posts" count="${count}" objectName="groupPosts" advanced="true" offset="5">
									<s:set name="group" value="#groupPosts" />
									 
									<div class="pager">
										<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pagerInfo_search.jsp" />
										<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
									</div>
				
									<s:iterator var="postId" status="status"> 
										<s:set var="post" value="%{getPost(#postId)}" />
										<s:set name="postCount" value="((#group.currItem -1) * postsPerPage) + #status.count"></s:set>
										<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username" ><s:property value="#post.username" /></wp:parameter></wp:action></s:set>
											<s:set var="post" value="%{getPost(#postId)}" />
											<div class="jpforum_post_container" id="<s:property value="jpforumPostHtmlId" /><s:property value="#post.code" />">
												<%-- titolo del post--%>
												<h<s:property value="jpforumTitle3" /> class="title_level3">
													<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post" ><s:property value="#post.code" /></wp:parameter></wp:action></s:set>
													<a title="<wp:i18n key="jpforum_PERMALINK" />" href="<s:property value="%{#viewPostActionPath}" escape="false" />#<s:property value="jpforumPostHtmlId" /><s:property value="#post.code" />"><wp:i18n key="jpforum_SEARCH_RESULT" />&#32;<s:property value="#postCount"  />&#32;<wp:i18n key="jpforum_OF" />&#32;<s:property value="posts.size" /></a>, <abbr title="<wp:i18n key="jpforum_POST_LONG" />"><wp:i18n key="jpforum_POST_SHORT" /></abbr>&#32;<wp:i18n key="jpforum_WRITTEN_BY" />&#32;<a href="<s:property value="%{#viewUserActionPath}" escape="false" />"><s:property value="%{getUserNick(#post.username)}" /></a>&#32;<wp:i18n key="jpforum_ON_DATE" />&#32;<s:date format="%{#jpforum_time_format}" name="#post.postDate"/> (<s:date name="#post.postDate" nice="true" />)
												</h<s:property value="jpforumTitle3" />>					
												
												<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/thread/inc/post.jsp" />
												<div class="jpforum_tools_container">
													<p>
														<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post" ><s:property value="#post.code" /></wp:parameter></wp:action>#<s:property value="#jpforumPostHtmlId"/><s:property value="#post.code" /></s:set>
														<a class="jpforum_goto" href="<s:property value="%{#viewPostActionPath}" escape="false"/>"><wp:i18n key="jpforum_GOTO_POST" />&#32;<em><s:property value="%{getThread(#post.threadId).getPost().getTitle()}" /></em></a>
													</p>
												</div>
											</div>
											<hr />
									</s:iterator>
									
									<%-- paginatore --%>
									<div class="pager"><s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" /></div>
									<s:if test="#group.size > #group.max">
										<hr />
									</s:if>				
								</jpforum:subset>			
					</s:if>
					<s:else>
						<p><wp:i18n key="jpforum_SEARCH_POSTS_NO_RESULTS" /></p>
					</s:else>
				</div>
			</form>
		</div>
	</div>

	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>
