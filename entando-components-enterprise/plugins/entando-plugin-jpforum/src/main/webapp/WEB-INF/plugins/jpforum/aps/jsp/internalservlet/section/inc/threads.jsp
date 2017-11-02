<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<s:set var="currentLang"><wp:info key="currentLang" /></s:set> 
<s:set var="section" value="currentSection" id="section" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum_block jpforum_threads">

	<h<s:property value="jpforumTitle2" /> class="title_level2"><wp:i18n key="jpforum_TITLE_THREADS" /></h<s:property value="jpforumTitle2" />>

	<%-- Create New Thread Link --%>
	<div class="jpforum_tools_container">
		<%-- new thread link --%> 
		<s:if test="#section.open">
			<s:if test="allowedOnCreateThread">
				<s:set var="newThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/newThread.action" ><wp:parameter name="section"><s:property value="#section.code" /></wp:parameter></wp:action></s:set>
				<p>
					<a class="jpforum_new_thread" href="<s:property value="%{#newThreadActionPath}" escape="false" />"><wp:i18n key="jpforum_CREATE_NEW_THREAD" /></a><br />
				</p>
			</s:if>
			<%-- if the user cannot create threads and is "guest", so show the login link --%>
			<s:elseif test="!allowedOnCreateThread && (#session.currentUser.username.equals('guest'))">
				<%//TODO creare link per login page %>
				<p><wp:i18n key="jpforum_THREAD_LOGIN_TO_CREATE" /></p>
				<p><a class="jpforum_goto" href="#loginpage-todo"><wp:i18n key="jpforum_GOTO_LOGIN" /></a></p>
			</s:elseif>
			<s:else>
				<p><wp:i18n key="jpforum_SECTION_YOU_CANNOT_CREATE_THREADS" /></p>
			</s:else>
		</s:if>
		<s:else> 
			<p><wp:i18n key="jpforum_SECTION_CLOSED_CANNOT_CREATE_THREAD" /></p> 
		</s:else>
	</div>
	
	<div class="jpforum_block jpforum_threads_sticky">
		<h<s:property value="jpforumTitle3" /> class="title_level3"><wp:i18n key="jpforum_TITLE_THREADS_STICKY" /></h<s:property value="jpforumTitle3" />>
		<s:set var="importants" value="%{getThreads(true)}" /> 
		<s:if test="null != #importants &&  #importants.size > 0">	
			<ul class="thread_list">
				<s:iterator value="#importants" var="threadCode">
					<li>
						<s:set name="thread" value="%{getThread(#threadCode)}" />
						<s:set name="lastPost" value="%{getPost(#thread.postCodes[#thread.postCodes.size - 1])}" />
						<s:set var="viewThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/viewThread.action" ><wp:parameter name="thread"><s:property value="#thread.code" /></wp:parameter></wp:action>#jpforum_thread_list</s:set>
						<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#lastPost.code" /></wp:parameter><wp:parameter name="thread"><s:property value="#thread.code" /></wp:parameter></wp:action>#<s:property value="jpforumPostHtmlId" /><s:property value="#lastPost.code" /></s:set>
						<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#thread.post.username" /></wp:parameter></wp:action></s:set>
						<s:set var="viewLastPostUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#lastPost.username" /></wp:parameter></wp:action></s:set>
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/thread/inc/render_single_thread_item.jsp" />
					</li>
				</s:iterator>
			</ul>
		</s:if>
		<s:else>
			<p><wp:i18n key="jpforum_NO_STYCKY_THREADS_HERE" /></p>
		</s:else>
	</div>

<%-- NOT STICKY threads  --%>	
	<div class="jpforum_block" id="jpforum_threads_not_sticky"> <%-- html id used for pager action --%>
		<h<s:property value="jpforumTitle3" /> class="title_level3"><wp:i18n key="jpforum_TITLE_THREADS_NORMAL" /></h<s:property value="jpforumTitle3" />>
		
		<s:set var="threads" value="%{getThreads(false)}" /> 
		<s:if test="null != #threads &&  #threads.size > 0">
			<s:set var="count" value="#threadPagerSize" scope="request" />
			<s:set var="pagerActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Browse/viewSection.action" ><wp:parameter name="section"><s:property value="section" /></wp:parameter></wp:action>#jpforum_threads_not_sticky</s:set>
			<form  action="<s:property value="%{#pagerActionPath}" escape="false" />" method="post" >
				<%-- thread title --%>
				
				<jpforum:subset source="#threads" count="${count}" objectName="groupPosts" advanced="true" offset="5">
					<%-- pager --%>
					<s:set name="group" value="#groupPosts" />
					<div class="pager">
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pagerInfo_threads.jsp" />
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
					</div>
					
					<%-- thread list --%>
					<ul class="thread_list">
						<s:iterator var="threadCode">
							<li>
								<s:set name="thread" value="%{getThread(#threadCode)}" />
								<s:set name="lastPost" value="%{getPost(#thread.postCodes[#thread.postCodes.size - 1])}" />
								<s:set var="viewThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Thread/viewThread.action" ><wp:parameter name="thread"><s:property value="#thread.code" /></wp:parameter></wp:action>#jpforum_thread_list</s:set>
								<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#lastPost.code" /></wp:parameter><wp:parameter name="thread"><s:property value="#thread.code" /></wp:parameter></wp:action>#<s:property value="jpforumPostHtmlId" /><s:property value="#lastPost.code" /></s:set>
								<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#thread.post.username" /></wp:parameter></wp:action></s:set>
								<s:set var="viewLastPostUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#lastPost.username" /></wp:parameter></wp:action></s:set>
								<%-- render thread --%>
								<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/thread/inc/render_single_thread_item.jsp" />
							</li> 
						</s:iterator> 
					</ul>
					
					<%-- pager --%>
					<div class="pager">
						<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
					</div>
				</jpforum:subset>
			</form>
		</s:if>
		<s:else>
			<p><wp:i18n key="jpforum_NO_NORMAL_THREADS_HERE" /></p>
		</s:else>
	</div>

</div>