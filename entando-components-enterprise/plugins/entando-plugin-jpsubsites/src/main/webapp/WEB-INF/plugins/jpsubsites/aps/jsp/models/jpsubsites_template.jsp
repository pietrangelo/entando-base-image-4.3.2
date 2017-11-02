<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpss" uri="/jpsubsites-core" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<wp:info key="currentLang" />">
<head>
    <wp:currentPage param="code" var="pageCode"/>
    <jpss:currentSubsite param="home" var="subsiteHome" />
    <title><wp:i18n key="jpsubsites_TITLE" /> | <jpss:currentSubsite param="title" /><c:if test="${pageCode!=subsiteHome}" > | <wp:currentPage param="title" /></c:if></title>
    <link type="text/css" rel="stylesheet" href="<wp:resourceURL/>plugins/jpsubsites/static/css/jpsubsites.css" media="screen" />
    <link type="text/css" rel="stylesheet" href="<wp:resourceURL/>plugins/jpsubsites/static/css/pagemodels/general.css" media="screen" />
    <jpss:currentSubsite param="css" var="subsiteCss" />
    <c:if test="${subsiteCss!=null}">
            <link type="text/css" rel="stylesheet" href="<wp:resourceURL/>plugins/jpsubsites/static/css/<c:out value="${subsiteCss}" />" media="screen" />
    </c:if>
    <%--
    <!--[if lte IE 6]>
            <link rel="stylesheet" type="text/css" href="<wp:resourceURL/>plugins/jpsubsites/static/css/pagemodels/general_ie6.css" media="screen" />
            <link rel="stylesheet" type="text/css" href="<wp:resourceURL/>plugins/jpsubsites/static/css/showlets/patches_ie6.css" media="screen" />
    <![endif]-->
    <wp:outputHeadInfo type="CSS">
            <link rel="stylesheet" type="text/css" href="<wp:resourceURL/>plugins/jpsubsites/static/css/<wp:printHeadInfo />" media="screen" />
    </wp:outputHeadInfo>
    <link rel="stylesheet" type="text/css" href="<wp:cssURL />media/stampa.css" media="print" />
    --%>
</head>
    <body>
        <div id="header">
            <div id="header_1">
                <div id="header_1_1">
                    <a href="<wp:url page="homepage" />" title="<wp:i18n key="jpsubsites_BACK_TO_PRINCIPAL_SITE" />"><img src="<wp:resourceURL/>plugins/jpsubsites/static/img/logo_01.png" alt="<wp:i18n key="jpsubsites_LOGO" />"/></a>
                </div>
                <div id="header_1_2">
                    <h1><wp:i18n key="jpsubsites_TITLE" /></h1>
                </div>
                <%--
                <div id="header_1_2">
                    <h1><wp:i18n key="jpsubsites_TITLE" /></h1>
                </div>
                --%>
                <div id="header_1_3">
                    <h2><jpss:currentSubsite param="title" /></h2>
                    <p class="slogan"><jpss:currentSubsite param="descr" /></p>
                </div>
            </div>
            <div class="noscreen">
                <p>[ <a href="#a2" id="a0"><wp:i18n key="jpsubsites_JUMP_TO_CONTENTS" /></a> ]</p>
                <dl>
                    <dt><wp:i18n key="jpsubsites_SERVICES" />:</dt>
                    <dd><a href="#a1" accesskey="1"><wp:i18n key="jpsubsites_NAVIGATION_MENU" /></a> [1];</dd>
                    <dd><a href="#a2" accesskey="2"><wp:i18n key="jpsubsites_CONTENTS" /></a>;</dd>
                </dl>
            </div>
            <div id="header_2">
                <jpss:currentSubsite param="img" var="subsiteImg" />
                <c:if test="${subsiteImg!=null}">
                    <img src="<wp:resourceURL/>plugins/jpsubsites/static/img/<c:out value="${subsiteImg}" />" alt="" />
                </c:if>
            </div>
        </div>
        <div id="subheader">
            <wp:show frame="0"/>
        </div>
        <div id="main">
            <div id="colonna1">
                <p class="noscreen">[ <a href="#a0" id="a1"><wp:i18n key="jpsubsites_BACK_TO_TOP" /></a> ]</p>
                <p class="noscreen">[ <a href="#a2"><wp:i18n key="jpsubsites_JUMP_TO_CONTENTS" /></a> ]</p>
                <wp:show frame="1"/>
                <wp:show frame="2"/>
                <wp:show frame="3"/>
                <wp:show frame="4"/>
                <wp:show frame="5"/>
            </div>
            <div id="colonna2">
                <p class="noscreen">[ <a href="#a0" id="a2"><wp:i18n key="jpsubsites_BACK_TO_TOP" /></a> ]</p>
                <wp:show frame="6"/>
                <wp:show frame="7"/>
                <wp:show frame="8"/>
                <wp:show frame="9"/>
                <wp:show frame="10"/>
            </div>
        </div>
        <div id="footer">
            <p><wp:i18n key="jpsubsites_COPYRIGHT" escapeXml="false" /></p>
            <wp:i18n key="jpsubsites_CREDITS_POWERED_BY" escapeXml="false" />
        </div>
    </body>
</html>