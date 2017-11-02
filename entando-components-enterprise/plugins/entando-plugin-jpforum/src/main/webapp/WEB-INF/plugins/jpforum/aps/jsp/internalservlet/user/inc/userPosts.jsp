<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum showlet">
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />

	<s:set var="posts" value="%{getUserPosts(username)}" />
	<s:set var="pagerActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUserPosts.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
		

	<div class="jpforum_block">
		<s:if test="#session.currentUser.username != username">
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_MESSAGES_WRITTEN_BY" />&#32;<em><s:property value="%{getUserNick(username)}"/></em></h<s:property value="jpforumTitle2" />> 
		</s:if>
		<s:else>
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_MESSAGES_ALL_YOUR_MSG" /></h<s:property value="jpforumTitle2" />> 
		</s:else>
		
		<s:if test="#posts.size != null && #posts.size > 0">
			<form  action="<s:property value="%{#pagerActionPath}" escape="false" />" method="post" >
	
				<s:set var="count" value="postsPerPage" scope="request" />
				<jpforum:subset source="#posts" count="${count}" objectName="groupPosts" advanced="true" offset="5">
					<s:set name="group" value="#groupPosts" />
					
					<div class="pager">
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pagerInfo_posts.jsp" />
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
					</div>
					
					<s:iterator var="postId" status="status">
						<s:set var="post" value="%{getPost(#postId)}" />
						<s:set name="postCount" value="((#group.currItem -1) * postsPerPage) + #status.count"></s:set>
						<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#post.username" /></wp:parameter></wp:action></s:set>
						<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#post.code" /></wp:parameter><wp:parameter name="thread"><s:property value="#post.threadId" /></wp:parameter></wp:action>#<s:property value="jpforumPostHtmlId" /><s:property value="#post.code" /></s:set>
						
						<div class="jpforum_post_container" id="<s:property value="jpforumPostHtmlId" /><s:property value="#post.code" />">
							<%-- titolo del post--%>
							<h<s:property value="jpforumTitle3" /> class="title_level3">
								<a title="<wp:i18n key="jpforum_PERMALINK" />" href="<s:property value="%{#viewPostActionPath}" escape="false" />"><abbr title="<wp:i18n key="jpforum_POST_LONG" />"><wp:i18n key="jpforum_POST_SHORT" /></abbr>&#32;<s:property value="#postCount"  />&#32;<wp:i18n key="jpforum_OF" />&#32;<s:property value="#posts.size" /></a>&#32;<wp:i18n key="jpforum_WRITTEN_BY" />&#32;<a href="<s:property value="%{#viewUserActionPath}" escape="false" />"><s:property value="%{getUserNick(#post.username)}" /></a>&#32;<wp:i18n key="jpforum_ON_DATE" />&#32;<s:date format="%{#jpforum_time_format}" name="#post.postDate"/> (<s:date name="#post.postDate" nice="true" />)
							</h<s:property value="jpforumTitle3" />>				
							
							<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/thread/inc/post.jsp" />
							
							<div class="jpforum_tools_container">
								<p>
									<a class="jpforum_goto" href="<s:property value="%{#viewPostActionPath}" escape="false"/>"><wp:i18n key="jpforum_GOTO_POST" />&#32;<em><s:property value="%{getThread(#post.threadId).getPost().getTitle()}" /></em></a>
								</p>
							</div>
						</div>
						<hr />
					</s:iterator>
					
					<div class="pager">
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
					</div>
					
				</jpforum:subset>
		
			</form>
		</s:if>
		<s:else>
			<p><wp:i18n key="jpforum_NO_POSTS" /></p>
		</s:else>
	
		<hr />
	
		<div class="jpforum_tools_container">
			<p>
				<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
				
				<s:if test="#session.currentUser.username != username">
					<a class="jpforum_user_profile" href="<s:property value="%{#viewUserActionPath}" escape="false" />"><wp:i18n key="jpforum_GOTO_PROFILE_OF" />&#32;<em><s:property value="%{getUserNick(username)}" /></em></a>
				</s:if>
				<s:else>
					<a class="jpforum_user_panel" href="<s:property value="%{#viewUserActionPath}" escape="false" />"><wp:i18n key="jpforum_GOTO_UR_PANEL" /></a>
				</s:else>			
			</p>
		</div>
		
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>