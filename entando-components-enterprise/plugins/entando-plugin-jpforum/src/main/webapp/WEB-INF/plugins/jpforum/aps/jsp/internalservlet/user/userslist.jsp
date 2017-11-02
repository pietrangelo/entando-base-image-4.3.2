<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpforum" uri="/jpforum-aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
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
	
	<div class="jpforum_block">
		<div class="jpforum_usersearch">
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_SEARCH_IN_USERS" /></h<s:property value="jpforumTitle2"/>>
			
			<s:set var="searchUsersActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/Search/search.action" />#jpforum_userlist</s:set>
			<form action="<s:property value="searchUsersActionPath" escape="false" />" method="post">
				<div class="jpforum_block">
					<p>
						<label for="jpforum_searchuser_text"><wp:i18n key="jpforum_LABEL_SEARCH_USERS" /></label>:<br />
						<s:textfield cssClass="text" id="jpforum_searchuser_text" name="text" /><input class="button" type="submit" value="<wp:i18n key="jpforum_BUTTON_GO" />" />
					</p>
					<%//TODO ie7 bottone storto %>
					<s:set var="textTest" value="text" />
					<p>
						<s:if test='%{!( (#textTest != null) && (#textTest.length() != null) && (#textTest.length() > 0) )}'>
							<wp:i18n key="jpforum_SEARCH_USER_NO_PARAMETER_INSERTED" />
						</s:if>
						<s:else>
							<wp:i18n key="jpforum_SEARCH_YOU_SEARCHED" />: <em><s:property value="#textTest" /></em>.
						</s:else>
					</p>
				</div>
				
				<div class="jpforum_block">
					<s:if test="users.size > 0">
							<div id="jpforum_userlist">
								<s:set var="count" value="#usersPagerSize" scope="request" />
								<jpforum:subset source="users" count="${count}" objectName="groupPosts" advanced="true" offset="5">
									
									<%-- pager --%>
									<s:set name="group" value="#groupPosts" />
									<div class="pager">
										<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pagerInfo_search.jsp" />
										<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
									</div>
									
									<%-- user list --%>
									<ul>
										<s:iterator var="user">
											<li>
												<p>
													<s:set var="currentUsername" scope="request"><s:property value="#user"/></s:set>
													<s:set var="viewUserActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/viewUser.action" ><wp:parameter name="username"><s:property value="#user" /></wp:parameter></wp:action></s:set>
													<jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  username="${currentUsername}"/>
													<a href="<s:property value="#viewUserActionPath" escape="false" />">
														<s:if test="null != #request.currentAvatar">
															<img src="<s:property value="#request.currentAvatar"/>"/>
														</s:if>
														<s:property value="%{getUserNick(#user)}"/>
													</a>
												</p>   
											</li> 
										</s:iterator>
									</ul>
									
									<%-- pager --%>
									<div class="pager">
										<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/pager/pager_formBlock.jsp" />
									</div>
									
								</jpforum:subset>		
							</div>
					</s:if>
					<s:else>
						<p><wp:i18n key="jpforum_SEARCH_USERS_NO_RESULTS" /></p>
					</s:else>
				</div>
			</form>
		</div>	
	</div>

	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>