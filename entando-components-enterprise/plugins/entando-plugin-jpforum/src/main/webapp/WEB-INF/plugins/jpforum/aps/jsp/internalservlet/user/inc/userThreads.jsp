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
	
	<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>

	<%-- 
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />
	
	<div class="jpforum_block">
		<s:if test="#session.currentUser.username != username">
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_THREADS_WRITTEN_BY" />&#32;<a href="<s:property value="%{#viewUserActionPath}" escape="false" />"><em><s:property value="%{getUserNick(username)}"/></em></a></h<s:property value="jpforumTitle2" />> 
		</s:if>
		<s:else>
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_THREADS_YOU_STARTED" /></h<s:property value="jpforumTitle2" />> 
		</s:else>
	
		<s:set var="threads" value="%{getUserThreads(username)}" />
		<s:if test="null != #threads &&  #threads.size > 0">
			<s:set var="count" value="postsPerPage" scope="request" />
			
			<s:set var="pagerActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUserThreads.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter></wp:action></s:set>
			<form  action="<s:property value="%{#pagerActionPath}" escape="false" />" method="post" >
				
				<jpforum:subset source="#threads" count="${count}" objectName="groupPosts" advanced="true" offset="5">
					<s:set name="group" value="#groupPosts" />
					
					<div class="pager">
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pagerInfo_threads.jsp" />
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
					</div>
					
					<ul class="thread_list">
						<s:iterator var="threadCode">
							<li>
								<s:set var="thread" value="%{getThread(#threadCode)}" />
								<s:set var="lastPost" value="%{getPost(#thread.postCodes[#thread.postCodes.size - 1])}" />
								<s:set var="viewThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/viewThread.action" ><wp:parameter name="thread"><s:property value="#thread.code" /></wp:parameter></wp:action></s:set>
								<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#lastPost.code" /></wp:parameter></wp:action></s:set>
								<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#thread.post.username" /></wp:parameter></wp:action></s:set>
								<s:set var="viewLastPostUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#lastPost.username" /></wp:parameter></wp:action></s:set>
								<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/thread/inc/render_single_thread_item.jsp" />				
							</li>
						</s:iterator>
					</ul> 
		
					<div class="pager">
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
					</div>
				</jpforum:subset>
			</form>
		</s:if>
		<s:else>
			<p><wp:i18n key="jpforum_THREAD_NO_THREAD_FOUND" /></p>
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