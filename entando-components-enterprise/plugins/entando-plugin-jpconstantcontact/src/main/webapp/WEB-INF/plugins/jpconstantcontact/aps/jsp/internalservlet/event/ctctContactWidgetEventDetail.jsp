<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


 <h2><wp:i18n key="jpconstantcontact_EVENTDETAILS"/></h2>

<s:set value="%{getCurrentEvent()}" var="itemVar"/>

<s:set var="_eventId" value="#itemVar.getId()"/>

<s:set value="#itemVar.getName()" var="name" scope="page"/>
<s:set value="#itemVar.getTitle()" var="title" scope="page"/>
<s:set value="#itemVar.getContact().getName()" var="userName" scope="page"/>
<s:set value="#itemVar.getContact().getOrganizationName()" var="organizationName" scope="page"/>
<s:set value="#itemVar.getStartDate()" var="start" scope="page"/>
<s:set value="#itemVar.getEndDate()" var="end" scope="page"/>
<s:set value="#itemVar.getLocation()" var="location" scope="page"/>
<s:set value="#itemVar.getTimeZoneId()" var="timezone" scope="page"/>   

<s:if test="_messageErrors!=null"><div class="alert alert-error"><s:property value="_messageErrors"/></div></s:if>
<s:if test="_messageSuccess!=null"><div class="alert alert-success"><s:property value="_messageSuccess"/></div></s:if>

<s:if test="%{#itemVar.getStatus().equals('DRAFT')}">
    <form action="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/updateEvent.action"/>" method="post" >
    <div class="form-group ">
        <label class=control-label" for="Event_Name" >*&nbspEvent Name &nbsp<abbr title="Name must be univocal">?</abbr></label>
        <div class="controls ">
            <wpsf:textfield id="_eventName" name="_eventName" value="%{#attr.name}" required="required"> </wpsf:textfield>
        </div>
    </div>  
    <div class="form ">
        <div class="form-group">
            <label class="control-label" for="Event_Title" >*&nbspEvent Title</label>
             <div class="controls">
               <wpsf:textfield id="_eventTitle" name="_eventTitle" value="%{#attr.title}" required="required"> </wpsf:textfield>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label" for="Event_Title" >*&nbspCreator</label>
             <div class="controls">
                <wpsf:textfield id="_userName" name="_userName" value="%{#attr.userName}" required="required" />
            </div>
        </div>
        <div class="form-group">
            <label class="control-label" for="Event_Title" >*&nbspOrganization Name</label>
             <div class="controls">
                 <wpsf:textfield id="_organizationName" name="_organizationName" value="%{#attr.organizationName}" required="required" />
            </div>
        </div>    
       <div class="form-group">
           <label class="control-label"  for="Start_Day" title="${attr.start}" >*&nbspStart Date (UTC)
           </label>
             <div class="controls controls-row">
                <wpsf:textfield id="Start_Day" name="_eventStartDate" value="%{#attr.start.substring(0,10)}"  cssClass="input-small" placeholder="yyyy-MM-dd" required="required"/>
                     HH
                <select  name="_eventStartTimeHours" id="Start_TimeHours" class="input-mini">
                     <option value="1" selected />1</option>
                    <option value="2" >2</option>
                    <option value="3" >3</option>
                    <option value="4"  >4</option>
                    <option value="5"  >5</option>
                    <option value="6"  >6</option>
                    <option value="7"  >7</option>
                    <option value="8"  >8</option>
                    <option value="9"  >9</option>
                    <option value="10"  >10</option>
                    <option value="11"  >11</option>
                    <option value="12"  >12</option>
                    <option value="13"  />13</option>
                    <option value="14"  />14</option>
                    <option value="15"  />15</option>
                    <option value="16"  />16</option>
                    <option value="17"  />17</option>
                    <option value="18"  />18</option>
                    <option value="19"  />19</option>
                    <option value="20"  />20</option>
                    <option value="21"  />21</option>
                    <option value="22"  />22</option>
                    <option value="23"  />23</option>
                    <option value="24"  />24</option>
                </select>
                mm  
                <select name="_eventStartTimeMinutes" id="Start_TimeMinutes" class="input-mini" >
                    <option value="0" selected />00</option>
                    <option value="15"  />15</option>
                    <option value="30"  />30</option>
                    <option value="45"  />45</option>
                </select>
            </div>
        </div>  
        <div class="form-group">
            <label class="control-label"  for="End_Day" title="${attr.end}">*&nbspEnd Date (UTC)</label>
            <div class="controls controls-row  ">
                <wpsf:textfield id="End_Day" cssClass="input-small" name="_eventEndDate"  value="%{#attr.end.substring(0,10)}" placeholder="yyyy-MM-dd" required="required"/>
                HH<select name="_eventEndTimeHours" id="End_TimeHours" class="input-mini">
                    <option value="1" selected />1</option>
                    <option value="2"  />2</option>
                    <option value="3"  />3</option>
                    <option value="4"  />4</option>
                    <option value="5"  />5</option>
                    <option value="6"  />6</option>
                    <option value="7"  />7</option>
                    <option value="8"  />8</option>
                    <option value="9"  />9</option>
                    <option value="10"  />10</option>
                    <option value="11"  />11</option>
                    <option value="12"  />12</option>
                    <option value="13"  />13</option>
                    <option value="14"  />14</option>
                    <option value="15"  />15</option>
                    <option value="16"  />16</option>
                    <option value="17"  />17</option>
                    <option value="18"  />18</option>
                    <option value="19"  />19</option>
                    <option value="20"  />20</option>
                    <option value="21"  />21</option>
                    <option value="22"  />22</option>
                    <option value="23"  />23</option>
                    <option value="24"  />24</option>
                </select>
                mm
                <select name="_eventEndTimeMinutes"  id="End_TimeMinutes" class="input-mini">
                    <option value="0" selected />00</option>
                    <option value="15"  />15</option>
                    <option value="30"  />30</option>
                    <option value="45"  />45</option>
                </select>
            </div>
        </div>      
        <div class="form-group">
            <label class="control-label"  for="Event_Location" >Location</label>
            <div class="controls">
                <wpsf:textfield id="Event_Location" name="_eventLocation" value="%{#attr.location}" />
            </div>
        </div> 
        <div class="form-group">
            <label class="control-label" for="Event_TimezoneOld" >*&nbsp Timezone</label>
             <div class="controls">
               <wpsf:textfield id="Event_TimezoneOld" name="" value="%{#attr.timezone}" cssClass="uneditable-input"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label"  for="Event_TimeZone" >&nbspChange Timezone</label> 
            <div class="controls input-append">
                <select name="_eventTimeZone" id="Event_TimeZone" required="required">
                    <option title="US/Eastern" value="US/Eastern" selected>[GMT-05:00] Eastern Time (US & Canada)</option>
                    <option title="US/Central" value="US/Central" >[GMT-06:00] Central Time (US & Canada)</option>
                    <option title="US/Mountain" value="US/Mountain" >[GMT-07:00] Mountain Time (US & Canada)</option>
                    <option title="US/Pacific" value="US/Pacific" >[GMT-08:00] Pacific Time (US & Canada)</option>
                    <option title="" value="" >--------------</option>
                    <option title="Etc/GMT+12" value="Etc/GMT+12" >[GMT-12:00] International Date Line West</option>
                    <option title="Pacific/Samoa" value="Pacific/Samoa" >[GMT-11:00] Samoa</option>
                    <option title="US/Hawaii" value="US/Hawaii" >[GMT-10:00] Hawaii</option>
                    <option title="Pacific/Tahiti" value="Pacific/Tahiti" >[GMT-10:00] Tahiti</option>
                    <option title="US/Alaska" value="US/Alaska" >[GMT-09:00] Alaska</option>
                    <option title="Mexico/BajaNorte" value="Mexico/BajaNorte" >[GMT-08:00] Baja California</option>
                    <option title="US/Arizona" value="US/Arizona" >[GMT-07:00] Arizona</option>
                    <option title="America/Chihuahua" value="America/Chihuahua" >[GMT-07:00] Chihuahua, Mazatlan</option>
                    <option title="CST" value="CST" >[GMT-06:00] Central America</option>
                    <option title="America/Mexico_City" value="America/Mexico_City" >[GMT-06:00] Guadalajara, Mexico City, Monterrey</option>
                    <option title="Canada/Saskatchewan" value="Canada/Saskatchewan" >[GMT-06:00] Saskatchewan</option>
                    <option title="America/Bogota" value="America/Bogota" >[GMT-05:00] Bogota</option>
                    <option title="US/East-Indiana" value="US/East-Indiana" >[GMT-05:00] Indiana (East)</option>
                    <option title="America/Lima" value="America/Lima" >[GMT-05:00] Lima</option>
                    <option title="America/Guayaquil" value="America/Guayaquil" >[GMT-05:00] Quito, Guayaquil</option>
                    <option title="America/Caracas" value="America/Caracas" >[GMT-04:30] Caracas</option>
                    <option title="America/Puerto_Rico" value="America/Puerto_Rico" >[GMT-04:00] Aruba, Barbados, Dominica, Puerto Rico, Virgin Islands</option>
                    <option title="America/Asuncion" value="America/Asuncion" >[GMT-04:00] Asuncion</option>
                    <option title="Canada/Atlantic" value="Canada/Atlantic" >[GMT-04:00] Atlantic Time (Canada), Bermuda</option>
                    <option title="America/Cuiaba" value="America/Cuiaba" >[GMT-04:00] Cuiaba</option>
                    <option title="America/Guyana" value="America/Guyana" >[GMT-04:00] Guyana</option>
                    <option title="America/La_Paz" value="America/La_Paz" >[GMT-04:00] La Paz</option>
                    <option title="America/Manaus" value="America/Manaus" >[GMT-04:00] Manaus</option>
                    <option title="America/Santiago" value="America/Santiago" >[GMT-04:00] Santiago</option>
                    <option title="Canada/Newfoundland" value="Canada/Newfoundland" >[GMT-03:30] Newfoundland</option>
                    <option title="Brazil/East" value="Brazil/East" >[GMT-03:00] Brasilia, Sao Paulo</option>
                    <option title="America/Buenos_Aires" value="America/Buenos_Aires" >[GMT-03:00] Buenos Aires</option>
                    <option title="America/Cayenne" value="America/Cayenne" >[GMT-03:00] Cayenne (French Guiana)</option>
                    <option title="America/Fortaleza" value="America/Fortaleza" >[GMT-03:00] Fortaleza, Maceio</option>
                    <option title="America/Godthab" value="America/Godthab" >[GMT-03:00] Godthab (Western, Greenland)</option>
                    <option title="America/Montevideo" value="America/Montevideo" >[GMT-03:00] Montevideo</option>
                    <option title="America/Paramaribo" value="America/Paramaribo" >[GMT-03:00] Suriname, Paramaribo</option>
                    <option title="Atlantic/South_Georgia" value="Atlantic/South_Georgia" >[GMT-02:00] Atlantic South Georgia</option>
                    <option title="Atlantic/Azores" value="Atlantic/Azores" >[GMT-01:00] Azores</option>
                    <option title="Atlantic/Cape_Verde" value="Atlantic/Cape_Verde" >[GMT-01:00] Cape Verde Islands</option>
                    <option title="Africa/Accra" value="Africa/Accra" >[GMT] Accra</option>
                    <option title="Africa/Casablanca" value="Africa/Casablanca" >[GMT] Casablanca</option>
                    <option title="Europe/London" value="Europe/London" >[GMT] Greenwich Mean Time: Dublin, Edinburgh</option>
                    <option title="Etc/Universal" value="Etc/Universal" >[GMT] Coorinated Universal Time</option>
                    <option title="Europe/Lisbon" value="Europe/Lisbon" >[GMT] Lisbon, Canary Islands, Faroe Islands</option>
                    <option title="Africa/Monrovia" value="Africa/Monrovia" >[GMT] Monrovia, Reykjavik</option>
                    <option title="Europe/Amsterdam" value="Europe/Amsterdam" >[GMT+01:00] London, United Kingdom</option>
                    <option title="Europe/Belgrade" value="Europe/Belgrade" >[GMT+01:00] Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna</option>
                    <option title="Europe/Brussels" value="Europe/Brussels" >[GMT+01:00] Belgrade, Bratislava, Budapest, Ljubljana, Prague</option>
                    <option title="Europe/Sarajevo" value="Europe/Sarajevo" >[GMT+01:00] Brussels, Copenhagen, Madrid, Paris</option>
                    <option title="Africa/Lagos" value="Africa/Lagos" >[GMT+01:00] Sarajevo, Skopje, Warsaw, Zagreb</option>
                    <option title="Africa/Windhoek" value="Africa/Windhoek" >[GMT+01:00] Lagos</option>
                    <option title="Asia/Amman" value="Asia/Amman" >[GMT+02:00] Amman</option>
                    <option title="Europe/Athens" value="Europe/Athens" >[GMT+02:00] Athens, Bucharest, Istanbul</option>
                    <option title="Asia/Beirut" value="Asia/Beirut" >[GMT+02:00] Beirut, Cairo, Damascus</option>
                    <option title="Africa/Harare" value="Africa/Harare" >[GMT+02:00] Harare, Kigali, Lusaka</option>
                    <option title="Europe/Helsinki" value="Europe/Helsinki" >[GMT+02:00] Helsinki, Kiev, Riga, Sofia, Tallinn, Vilnius</option>
                    <option title="Asia/Jerusalem" value="Asia/Jerusalem" >[GMT+02:00] Jerusalem, Tel Aviv</option>
                    <option title="Africa/Johannesburg" value="Africa/Johannesburg" >[GMT+02:00] Johannesburg, Pretoria</option>
                    <option title="Europe/Minsk" value="Europe/Minsk" >[GMT+02:00] Minsk</option>
                    <option title="Asia/Baghdad" value="Asia/Baghdad" >[GMT+03:00] Baghdad</option>
                    <option title="Asia/Kuwait" value="Asia/Kuwait" >[GMT+03:00] Kuwait, Riyadh</option>
                    <option title="Europe/Moscow" value="Europe/Moscow" >[GMT+03:00] Moscow, St. Petersburg</option>
                    <option title="Africa/Nairobi" value="Africa/Nairobi" >[GMT+03:00] Nairobi</option>
                    <option title="Europe/Volgograd" value="Europe/Volgograd" >[GMT+03:00] Volgograd</option>
                    <option title="Asia/Tehran" value="Asia/Tehran" >[GMT+03:30] Tehran</option>
                    <option title="Asia/Dubai" value="Asia/Dubai" >[GMT+04:00] Abu Dhabi, Dubai, Muscat</option>
                    <option title="Asia/Baku" value="Asia/Baku" >[GMT+04:00] Baku</option>
                    <option title="Indian/Mahe" value="Indian/Mahe" >[GMT+04:00] Seychelles, Mahe</option>
                    <option title="Indian/Mauritius" value="Indian/Mauritius" >[GMT+04:00] Port Louis, Mauritius</option>
                    <option title="Indian/Reunion" value="Indian/Reunion" >[GMT+04:00] Reunion</option>
                    <option title="Asia/Tbilisi" value="Asia/Tbilisi" >[GMT+04:00] Tbilisi</option>
                    <option title="Asia/Yerevan" value="Asia/Yerevan" >[GMT+04:00] Yerevan</option>
                    <option title="Asia/Kabul" value="Asia/Kabul" >[GMT+04:30] Kabul</option>
                    <option title="Asia/Aqtobe" value="Asia/Aqtobe" >[GMT+05:00] Aqtobe</option>
                    <option title="Asia/Ashgabat" value="Asia/Ashgabat" >[GMT+05:00] Ashgabat</option>
                    <option title="Asia/Dushanbe" value="Asia/Dushanbe" >[GMT+05:00] Dushanbe</option>
                    <option title="Asia/Karachi" value="Asia/Karachi" >[GMT+05:00] Karachi, Islamabad</option>
                    <option title="Indian/Maldives" value="Indian/Maldives" >[GMT+05:00] Maldives</option>
                    <option title="Asia/Oral" value="Asia/Oral" >[GMT+05:00] Oral</option>
                    <option title="Asia/Tashkent" value="Asia/Tashkent" >[GMT+05:00] Tashkent, Samarkand</option>
                    <option title="Asia/Yekaterinburg" value="Asia/Yekaterinburg" >[GMT+05:00] Yekaterinburg (Sverdlovsk)</option>
                    <option title="Asia/Kolkata" value="Asia/Kolkata" >[GMT+05:30] Chennai, Kolkata, Mumbai, New Delhi, Colombo</option>
                    <option title="Asia/Kathmandu" value="Asia/Kathmandu" >[GMT+05:45] Kathmandu</option>
                    <option title="Asia/Almaty" value="Asia/Almaty" >[GMT+06:00] Almaty</option>
                    <option title="Asia/Bishkek" value="Asia/Bishkek" >[GMT+06:00] Bishkek</option>
                    <option title="Asia/Dhaka" value="Asia/Dhaka" >[GMT+06:00] Dhaka</option>
                    <option title="Asia/Omsk" value="Asia/Omsk" >[GMT+06:00] Omsk</option>
                    <option title="Asia/Qyzylorda" value="Asia/Qyzylorda" >[GMT+06:00] Qyzylorda</option>
                    <option title="Asia/Novosibirsk" value="Asia/Novosibirsk" >[GMT+06:00] Novosibirsk</option>
                    <option title="Asia/Thimbu" value="Asia/Thimbu" >[GMT+06:00] Thimbu</option>
                    <option title="Asia/Rangoon" value="Asia/Rangoon" >[GMT+06:30] Yangon (Rangoon)</option>
                    <option title="Asia/Bangkok" value="Asia/Bangkok" >[GMT+07:00] Bangkok, Hanoi</option>
                    <option title="Asia/Jakarta" value="Asia/Jakarta" >[GMT+07:00] Jakarta</option>
                    <option title="Asia/Krasnoyarsk" value="Asia/Krasnoyarsk" >[GMT+07:00] Krasnoyarsk</option>
                    <option title="Asia/Chongqing" value="Asia/Chongqing" >[GMT+08:00] Beijing, Chongqing, Hong Kong, Urumqi</option>
                    <option title="Asia/Brunei" value="Asia/Brunei" >[GMT+08:00] Brunei</option>
                    <option title="Asia/Hong_Kong" value="Asia/Hong_Kong" >[GMT+08:00] Hong Kong</option>
                    <option title="Asia/Irkutsk" value="Asia/Irkutsk" >[GMT+08:00] Irkutsk</option>
                    <option title="Asia/Kuala_Lumpur" value="Asia/Kuala_Lumpur" >[GMT+08:00] Kuala Lumpur, Kuching</option>
                    <option title="Asia/Makassar" value="Asia/Makassar" >[GMT+08:00] Makassar, Ujung Pandang</option>
                    <option title="Asia/Manila" value="Asia/Manila" >[GMT+08:00] Manila</option>
                    <option title="Australia/Perth" value="Australia/Perth" >[GMT+08:00] Perth</option>
                    <option title="Asia/Singapore" value="Asia/Singapore" >[GMT+08:00] Singapore</option>
                    <option title="Asia/Taipei" value="Asia/Taipei" >[GMT+08:00] Taipei</option>
                    <option title="Asia/Ulaanbaatar" value="Asia/Ulaanbaatar" >[GMT+08:00] Ulaanbaatar</option>
                    <option title="Asia/Tokyo" value="Asia/Tokyo" >[GMT+09:00] Osaka, Sapporo, Tokyo</option>
                    <option title="Asia/Dili" value="Asia/Dili" >[GMT+09:00] Dili</option>
                    <option title="Asia/Jayapura" value="Asia/Jayapura" >[GMT+09:00] Jayapura</option>
                    <option title="Asia/Seoul" value="Asia/Seoul" >[GMT+09:00] Seoul</option>
                    <option title="Asia/Yakutsk" value="Asia/Yakutsk" >[GMT+09:00] Yakutsk</option>
                    <option title="Australia/Adelaide" value="Australia/Adelaide" >[GMT+09:30] Adelaide</option>
                    <option title="Australia/Darwin" value="Australia/Darwin" >[GMT+09:30] Darwin</option>
                    <option title="Australia/Brisbane" value="Australia/Brisbane" >[GMT+10:00] Brisbane</option>
                    <option title="Australia/Canberra" value="Australia/Canberra" >[GMT+10:00] Canberra, Melbourne, Sydney</option>
                    <option title="Pacific/Port_Moresby" value="Pacific/Port_Moresby" >[GMT+10:00] Port Moresby</option>
                    <option title="Pacific/Guam" value="Pacific/Guam" >[GMT+10:00] Guam, Saipan</option>
                    <option title="Australia/Hobart" value="Australia/Hobart" >[GMT+10:00] Hobart</option>
                    <option title="Asia/Vladivostok" value="Asia/Vladivostok" >[GMT+10:00] Vladivostok</option>
                    <option title="Pacific/Noumea" value="Pacific/Noumea" >[GMT+11:00] Noumea, New Caledonia</option>
                    <option title="Asia/Magadan" value="Asia/Magadan" >[GMT+11:00] Magadan</option>
                    <option title="Pacific/Guadalcanal" value="Pacific/Guadalcanal" >[GMT+11:00] Solomon Islands, Guadalcanal</option>
                    <option title="Pacific/Efate" value="Pacific/Efate" >[GMT+11:00] Vanuatu, Efate</option>
                    <option title="Pacific/Auckland" value="Pacific/Auckland" >[GMT+12:00] Auckland, Wellington</option>
                    <option title="Pacific/Fiji" value="Pacific/Fiji" >[GMT+12:00] Fiji</option>
                    <option title="Asia/Kamchatka" value="Asia/Kamchatka" >[GMT+12:00] Petropavlovsk-Kamchatski (Kamchatka)</option>
                    <option title="Pacific/Tongatapu" value="Pacific/Tongatapu" >[GMT+13:00] Nuku'alofa, Tongatapu (Tonga)</option>
                </select>
            </div>
        </div> 
         <div class="form-group">
            <label class="control-label" for="Status" >&nbspStatus:</label>
            <div class="controls"> <wpsf:textfield id="Status" value="%{#itemVar.getStatus()}" cssClass="uneditable-input"></wpsf:textfield>
            </div>
         </div>   
        <input type="hidden" name="_eventType" value="OTHER"/>
        <input type="hidden" value="${_eventId}" name="_eventId"/>    
            <div>
                <wpsf:submit type="button" cssClass="btn btn-primary">Save</wpsf:submit> <a style="margin-left:1%" id="SendNow" href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/updateEventStatus.action"><wp:parameter name="_eventId"><s:property value="%{#itemVar.getId()}" /></wp:parameter><wp:parameter name="_eventStatus">ACTIVE</wp:parameter></wp:action>" title="Activate" class="btn btn-success">Activate Now</a>
            </div>
        </div>
    </form>
</s:if> 
<s:else>
    <div class="alert alert-warning ">Only draft events can be updated, to <b>copy</b> an event go to <u><a href=" https://ui.constantcontact.com/rnavmap/evp/hub/details?id=${_eventId}" target="_blank">Advanced UI</a></u>
    </div>
    <div class="form ">
    <div class="form-group ">
        <label class=control-label" for="Event_Name" >*&nbspEvent Name</label>
        <div class="controls ">
            <wpsf:textfield id="_eventName" name="_eventName" value="%{#attr.name}" disabled="true"> </wpsf:textfield>
        </div>
    </div>  
        <div class="form-group">
            <label class="control-label" for="Event_Title" >*&nbspEvent Title</label>
             <div class="controls">
               <wpsf:textfield id="_eventTitle" name="_eventTitle" value="%{#attr.title}" disabled="true"> </wpsf:textfield>
            </div>
        </div>
            
        <div class="form-group">
            <label class="control-label" for="Event_Title" >*&nbspCreator</label>
             <div class="controls">
                <wpsf:textfield id="_userName" name="_userName" value="%{#attr.userName}" disabled="true" />
            </div>
        </div>
            
        <div class="form-group">
            <label class="control-label" for="Event_Title" >*&nbspOrganization Name</label>
             <div class="controls">
                <wpsf:textfield id="_organizationName" name="_organizationName" value="%{#attr.organizationName}" disabled="true" />
            </div>
        </div>    
       <div class="form-group">
           <label class="control-label"  for="Start_Day" title="${attr.start}" >*&nbspStart Date (UTC)</label>
             <div class="controls controls-row">
                <wpsf:textfield id="Start_Day" name="_eventStartDate" value="%{#attr.start}"  cssClass="input-small" placeholder="yyyy-MM-dd" disabled="true"/>
            </div>
        </div>  
        <div class="form-group">
            <label class="control-label"  for="End_Day" title="${attr.end}" >*&nbspEnd Date (UTC)</label>
            <div class="controls controls-row  ">
                <wpsf:textfield id="End_Day" cssClass="input-small" name="_eventEndDate"  value="%{#attr.end}" placeholder="yyyy-MM-dd" disabled="true"/>
            </div>
        </div>      
        <div class="form-group">
            <label class="control-label"  for="Event_Location" >Location</label>
            <div class="controls">
                <wpsf:textfield id="Event_Location" name="_eventLocation" value="%{#attr.location}" disabled="true"/>
            </div>
        </div> 
            
        <div class="form-group">
            <label class="control-label" for="Event_TimezoneOld" >*&nbsp Timezone</label>
             <div class="controls">
               <wpsf:textfield id="Event_TimezoneOld" name="" value="%{#attr.timezone}" disabled="true"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label" for="Status" >&nbspStatus:</label>
            <div class="controls"> <wpsf:textfield id="Status" value="%{#itemVar.getStatus()}" disabled="true"></wpsf:textfield>
            </div>
         </div>     
        <input type="hidden" value="${_eventId}" name="_eventId"/> 
        <%-- Collapse Schedule--%>     
        <s:if test="#itemVar.getStatus()=='ACTIVE'">     
           <legend class="margin-large-top cursor-pointer">
               <a href="#constantContact-schedule" data-toggle="collapse" data-target="#constantContact-schedule">
                Schedule&#32;
                <span class="icon fa fa-chevron-down pull-right"></span>
               </a>
           </legend> 
           <div id="constantContact-schedule" class="collapse">
               <div class="form-group">    
                   Stop this event: <a  href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/updateEventStatus.action"><wp:parameter name="_eventId"><s:property value="%{#itemVar.getId()}" /></wp:parameter><wp:parameter name="_eventStatus">CANCELLED</wp:parameter></wp:action>" title="Activate" class="btn btn-success btn-small">Stop</a>   
               </div>
           </div>
       </s:if>
    </div>      
</s:else>

<div class="margin-medium-top"> 
        <a href="<wp:action path="/ExtStr2/do/FrontEnd/Ctct/listEvents.action"></wp:action>" class="btn btn-default">Go back</a>
</div>









