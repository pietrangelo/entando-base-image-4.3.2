<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:ifauthorized permission="jpsocial_post">
    <a href="<s:url namespace="/do/jacms/Content" action="postContentFromList">
           <s:param name="contentId" value="%{contentId}" />
           <s:param name="queueId" value="%{'twitter'}" />
           <s:param name="origin" value="%{'2'}" />
       </s:url>" title="<s:text name="jpsocial.hookpoint.entryContent.tweet.this" />:&#32;<s:property value="#content.id" />">
        <span class="icon fa fa-fw fa-twitter" ></span>&#32;
        <s:text name="jpsocial.hookpoint.entryContent.tweet.this" />
    </a>   

    <a href="<s:url namespace="/do/jacms/Content" action="postContentFromList">
           <s:param name="contentId" value="%{contentId}" />
           <s:param name="queueId" value="%{'facebook'}" />
           <s:param name="origin" value="%{'2'}" />
       </s:url>" title="<s:text name="jpsocial.hookpoint.entryContent.facebook.this" />:&#32;<s:property value="#content.id" />">
        <span class="icon fa fa-fw fa-facebook" ></span>&#32;
        <s:text name="jpsocial.hookpoint.entryContent.facebook.this" />
    </a>   
    <a href="<s:url namespace="/do/jacms/Content" action="postContentFromList">
           <s:param name="contentId" value="%{contentId}" />
           <s:param name="queueId" value="%{'linkedin'}" />
           <s:param name="origin" value="%{'2'}" />
       </s:url>" title="<s:text name="jpsocial.hookpoint.entryContent.linkedin.this" />:&#32;<s:property value="#content.id" />">
        <span class="icon fa fa-fw fa-linkedin" ></span>&#32;
        <s:text name="jpsocial.hookpoint.entryContent.linkedin.this" />
    </a>   

</wp:ifauthorized>
