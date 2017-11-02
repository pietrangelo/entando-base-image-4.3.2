<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<span id="jpsocial-share-button-<s:property value="#attribute.name" />">
    <div class="margin-base-vertical">

        <button class="btn btn-default accordion-toggle" 
                data-toggle="collapse"
                data-parent="jpsocial-share-button-<s:property value="#attribute.name" />"
                href="#jpsocial-share-items-<s:property value="#attribute.name"/>">
            <span class="icon fa fa-share-square-o" alt="<s:text name="jpsocial.hookpoint.attribute.share" />"></span>&#32;
            <s:text name="jpsocial.hookpoint.attribute.share" />&#32;<s:property value="#attribute.name" />&#32;
            <span class="icon fa fa-chevron-down"></span>
        </button>
    </div>        
    <div class="panel-collapse collapse" id="jpsocial-share-items-<s:property value="#attribute.name" />">
        <div class="btn-group">
            <%-- Decide here what option to show --%>
            <%-- image attribute //start --%>
            <s:if test="%{#attribute.type == 'SocialImage'}">
                <%-- Facebook --%>
                <s:if test="%{isLogged('facebook') == true}">
                    <s:set name="logged" value="%{'btn btn-success btn-block'}"/>
                </s:if>
                <s:else>
                    <s:set name="logged" value="%{'btn btn-default btn-block'}"/>
                </s:else>
                <wpsa:actionParam action="addAttribute" var="addFbAttr">
                    <wpsa:actionSubParam name="contentId" value="%{contentId}" />
                    <wpsa:actionSubParam name="queueId" value="%{'facebook'}" />
                    <wpsa:actionSubParam name="langId" value="%{#lang.code}" />
                    <wpsa:actionSubParam name="attributeId" value="%{#attribute.name}" />
                </wpsa:actionParam>
                <wpsf:submit
                    action="%{#addFbAttr}"
                    cssClass="%{#logged}"
                    value="%{getText('jpsocial.label.facebook')}"
                    title="%{getText('jpsocial.label.facebook.note')}" />  
            </s:if>
            <%-- image attribute //end --%>

            <%-- video attribute //start--%>
            <s:elseif test="%{#attribute.type == 'SocialAttach'}" >
                <s:if test="%{isLogged('youtube') == true}">
                    <s:set name="logged" value="%{'btn btn-success'}"/>
                </s:if>
                <s:else>
                    <s:set name="logged" value="%{'btn btn-default'}"/>
                </s:else>
                <wpsa:actionParam action="addAttribute" var="addYoutubeAttr">
                    <wpsa:actionSubParam name="contentId" value="%{contentId}" />
                    <wpsa:actionSubParam name="langId" value="%{#lang.code}" />
                    <wpsa:actionSubParam name="queueId" value="%{'youtube'}" />
                    <wpsa:actionSubParam name="attributeId" value="%{#attribute.name}" />
                </wpsa:actionParam>
                <wpsf:submit
                    action="%{#addYoutubeAttr}"
                    cssClass="%{#logged}"
                    value="%{getText('jpsocial.label.youtube')}"
                    title="%{getText('jpsocial.label.youtube.note')}" />
            </s:elseif>
            <%-- video attribute //end--%>

            <%-- text attributes //start --%>
            <s:else>

                <%-- Twitter --%>
                <s:if test="%{isLogged('twitter') == true}">
                    <s:set name="logged" value="%{'btn btn-success'}"/>
                </s:if>
                <s:else>
                    <s:set name="logged" value="%{'btn btn-default'}"/>
                </s:else>
                <wpsa:actionParam action="addAttribute" var="addTweetAttr">
                    <wpsa:actionSubParam name="contentId" value="%{contentId}" />
                    <wpsa:actionSubParam name="langId" value="%{#lang.code}" />
                    <wpsa:actionSubParam name="queueId" value="%{'twitter'}" />
                    <wpsa:actionSubParam name="attributeId" value="%{#attribute.name}" />
                </wpsa:actionParam>
                <wpsf:submit
                    action="%{#addTweetAttr}"
                    cssClass="%{#logged}"
                    value="%{getText('jpsocial.label.tweetit')}"
                    title="%{getText('jpsocial.label.tweetit.note')}" />
                <%-- Facebook --%>
                <s:if test="%{isLogged('facebook') == true}">
                    <s:set name="logged" value="%{'btn btn-success'}"/>
                </s:if>
                <s:else>
                    <s:set name="logged" value="%{'btn btn-default'}"/>
                </s:else>
                <wpsa:actionParam action="addAttribute" var="addFbAttr">
                    <wpsa:actionSubParam name="contentId" value="%{contentId}" />
                    <wpsa:actionSubParam name="langId" value="%{#lang.code}" />
                    <wpsa:actionSubParam name="queueId" value="%{'facebook'}" />
                    <wpsa:actionSubParam name="attributeId" value="%{#attribute.name}" />
                </wpsa:actionParam>
                <wpsf:submit
                    action="%{#addFbAttr}"
                    cssClass="%{#logged}"
                    value="%{getText('jpsocial.label.facebook')}"
                    title="%{getText('jpsocial.label.facebook.note')}" />
                <%-- Linkedin --%>
                <s:if test="%{isLogged('linkedin') == true}">
                    <s:set name="logged" value="%{'btn btn-success'}"/>
                </s:if>
                <s:else>
                    <s:set name="logged" value="%{'btn btn-default'}"/>
                </s:else>
                <wpsa:actionParam action="addAttribute" var="addLiAttr">
                    <wpsa:actionSubParam name="contentId" value="%{contentId}" />
                    <wpsa:actionSubParam name="langId" value="%{#lang.code}" />
                    <wpsa:actionSubParam name="queueId" value="%{'linkedin'}" />
                    <wpsa:actionSubParam name="attributeId" value="%{#attribute.name}" />
                </wpsa:actionParam>
                <wpsf:submit
                    action="%{#addLiAttr}"
                    cssClass="%{#logged}"
                    value="%{getText('jpsocial.label.linkedin')}"
                    title="%{getText('jpsocial.label.linkedin.note')}" />
            </s:else> <%-- *text --%>
        </div>
    </div>
</span>
