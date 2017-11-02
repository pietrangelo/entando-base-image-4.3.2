<%@ taglib prefix="wp" uri="/aps-core" %>

<div class="showlet">
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<div class="jpforum">
		<form action="<wp:url page="forum_search_results" />" method="post">
			<wp:info key="currentLang" var="currentLang" />
			<p>
				<label for="jpforum_search_text"><wp:i18n key="jpforum_LABEL_SEARCH_POST" />:</label><br />
				<input type="text" id="jpforum_search_text" name="textToFind" class="text" />
			</p>
			<p class="rightText">
				<input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_SEARCH" />" />
			</p>
		</form>
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>
	
</div>