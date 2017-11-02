<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jpmultisite-apsadmin-core" uri="/jpmultisite-apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:info key="currentLang" var="currentLang" />
<jpmultisite-apsadmin-core:currentMultisite var="currentMultisite" />
<jpmultisite-apsadmin-core:multisite key="home" var="multisiteHomeVar" lang="${currentLang}" code="${currentMultisite.code}"  />
<jpmultisite-apsadmin-core:multisite key="title" var="multisiteTitleVar" lang="${currentLang}" code="${currentMultisite.code}"  />
<jpmultisite-apsadmin-core:multisite key="descr" var="multisiteDescrVar" lang="${currentLang}" code="${currentMultisite.code}"  />
<jpmultisite-apsadmin-core:multisite key="img" var="multisiteImgVar" lang="${currentLang}" code="${currentMultisite.code}"  />
<jpmultisite-apsadmin-core:multisite key="css" var="multisiteCssVar" lang="${currentLang}" code="${currentMultisite.code}"  />
<!DOCTYPE html>
<html lang="<wp:info key="currentLang" />">
	<head>
		<title>
				<wp:currentPage param="title" /> - <c:out value="${multisiteTitleVar}" />
		</title>
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />

		<link rel="icon" href="<wp:info key="systemParam" paramName="applicationBaseURL" />favicon.png" type="image/png" />

		<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
		<!--[if lt IE 9]>
			<script src="<wp:resourceURL />static/js/entando-misc-html5-essentials/html5shiv.js"></script>
		<![endif]-->

		<%-- load jquery and bootstrap before the common-utils --%>
			<wp:headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />
			<wp:headInfo type="JS" info="entando-misc-bootstrap/bootstrap.min.js" />
			<wp:headInfo type="CSS" info="../entando-misc-bootstrap/bootstrap/bootstrap/css/bootstrap.min.css" />
			<wp:headInfo type="CSS" info="../entando-misc-bootstrap/bootstrap/bootstrap/css/bootstrap-responsive.min.css" />

		<%-- standard include --%>
			<jsp:include page="/WEB-INF/aps/jsp/models/inc/models-common-utils.jsp" />

		<%-- default css --%>
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpmultisite/static/css/pagemodels/jpmultisite_home.css" />

		<%-- multisite specific css --%>
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL /><c:out value="${multisiteCssVar}" />" />
	</head>

	<body>

		<div class="navbar-wrapper">
			<div class="container">

				<div id="navigation" class="navbar navbar-inverse">
					<div class="navbar-inner">
						<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>
						<a id="title" class="brand" href="<wp:url page="${multisiteHomeVar}" />"><c:out value="${multisiteTitleVar}" /></a>
						<div class="nav-collapse collapse">
							<%-- 0. Main Navigation  --%>
									<wp:show frame="0" />

							<ul id="main-pages" class="nav">
								<wp:currentPage param="code" var="currentPageCode" />
								<wp:nav spec="code(${multisiteHomeVar}).subtree(1)" var="page">
									<li <c:if test="${page.code == currentPageCode}">class="active"</c:if>>
										<a href="<c:out value="${page.url}" />"><c:out value="${page.title}" /></a>
									</li>
								</wp:nav>
							</ul>

							<%-- language --%>
								<wp:info key="langs" var="langs" />
								<c:if test="${fn:length(langs) > 1}">
									<wp:info key="currentLang" var="currentLang" />
									<wp:info key="defaultLang" var="defaultLang" />
									<ul id="languages" class="nav pull-right">
										<li class="dropdown">
											<a
												href="#"
												class="dropdown-toggle"
												data-toggle="dropdown">
													<span title="<wp:i18n key="CHANGE_LANGUAGE" />" class="icon-flag icon-white"></span>
													<b class="caret"></b>
											</a>
											<ul class="dropdown-menu">
												<%-- langs[0] is always the default lang --%>
												<li class="<c:out value="${langs[0].code==currentLang?'active':''}" />"><a class="<c:out value="${langs[0].code==currentLang?'active':''}" />" href="<wp:url lang="${langs[0].code}" />"><c:out value="${langs[0].descr}" /></a></li>
												<li class="divider"></li>
												<c:forEach items="${langs}" begin="1" var="l" varStatus="status">
													<li class="<c:out value="${l.code==currentLang?'active':''}" />"><a class="<c:out value="${l.code==currentLang?'active':''}" />" href="<wp:url lang="${l.code}" />"><c:out value="${l.descr}" /></a></li>
												</c:forEach>
											</ul>
										</li>
									</ul>
								</c:if>

						</div><!--/.nav-collapse -->
					</div><!-- /.navbar-inner -->
				</div><!-- /.navbar -->

			</div> <!-- /.container -->
		</div><!-- /.navbar-wrapper -->

		<div class="carousel slide">
			<div class="carousel-inner">

				<div id="logo-container" class="item active">
					<img id="logo" src="<wp:resourceURL /><c:out value="${multisiteImgVar}" />" alt=" " />
					<div class="container">
						<div class="carousel-caption">

							<!-- <h1>Example headline.</h1> -->
							<p id="description" class="lead">
								<c:out value="${multisiteDescrVar}" />
							</p>

						</div>
					</div>
				</div>

			</div>
		</div>

		<div class="container">

			<div class="row main-spacer">
				<div id="left-column" class="span4">
						<%-- 1. Small Column I  --%>
							<wp:show frame="1" />
						<%-- 2. Small Column II  --%>
							<wp:show frame="2" />
						<%-- 3. Small Column III  --%>
							<wp:show frame="3" />
						<%-- 4. Small Column IV  --%>
							<wp:show frame="4" />
				</div>
				<div id="right-column" class="span4">
						<%-- 5. Main Column I //the main frame  --%>
							<wp:show frame="5" />
						<%-- 6. Main Column II  --%>
							<wp:show frame="6" />
						<%-- 7. Main Column III  --%>
							<wp:show frame="7" />
						<%-- 8. Main Column IV  --%>
							<wp:show frame="8" />
						<%-- 9. Main Column V  --%>
							<wp:show frame="9" />
				</div>
			</div>

			<hr />

			<footer id="footer">
				<div class="row-fluid">
					<div class="span6">
						<%-- 10. Footer  --%>
							<wp:show frame="10" />
					</div>
					<div id="credits" class="span6 text-right">
						<%-- version2: JSTL style --%>
						<jsp:useBean id="date" class="java.util.Date" />
						<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
						 &copy; <c:out value="${currentYear}" />&#32;<c:out value="${multisiteTitleVar}" />
					</div>

				</div>
			</footer>
		</div>

	</body>
</html>
