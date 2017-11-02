<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ page contentType="charset=UTF-8" %>

<wp:headInfo type="CSS" info="../../plugins/jppentaho/static/css/jppentaho.css"/>

<iframe class="jppentaho_iframe" src="<s:property value="xactionUrl" />" width="100%" height="100%">
  <p><wp:i18n key="jppentaho_ERROR_NO_IFRAME" /></p>
</iframe>