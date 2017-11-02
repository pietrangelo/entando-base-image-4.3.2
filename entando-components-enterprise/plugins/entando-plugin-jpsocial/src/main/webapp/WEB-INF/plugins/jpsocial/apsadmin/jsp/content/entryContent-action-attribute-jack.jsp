<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:set var="show" value="%{false}" />
<s:iterator value="content.attributeList" var="attribute" status="status">
  <s:set var="show" value="%{#show || #attribute.type=='SocialHypertext' || #attribute.type=='SocialLongtext' || #attribute.type=='SocialMonotext' || #attribute.type=='SocialText' || #attribute.type=='SocialImage' || #attribute.type=='SocialAttach'}" />
</s:iterator>

<s:if test="#show">

  <div class="input-group input-group-btn">

      <wpsa:actionParam action="tweetContent" var="tweetAction">
        <wpsa:actionSubParam name="contentId" value="%{contentId}" />
        <wpsa:actionSubParam name="queueId" value="%{'twitter'}" />
        <wpsa:actionSubParam name="origin" value="%{'1'}" />
      </wpsa:actionParam>
      <wpsf:submit
        action="%{#tweetAction}"
        cssClass="btn btn-default"
        value="%{getText('jpsocial.content.tweetit')}"
        title="%{getText('jpsocial.content.tweetit.note')}" />

    <wpsa:actionParam action="postContent" var="postContentAction">
      <wpsa:actionSubParam name="contentId" value="%{contentId}" />
      <wpsa:actionSubParam name="origin" value="%{'1'}" />
    </wpsa:actionParam>

    <wpsf:submit
      cssClass="btn btn-default"
      action="%{#postContentAction}"
      value="%{getText('jpsocial.content.saveAndShare')}"
      title="%{getText('jpsocial.content.saveAndShare.note')}" />
  </div>
</s:if>

