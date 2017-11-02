Include into web.xml the new servlet:

*************************
<!-- jpsocial :: start -->
    <servlet>
        <servlet-name>jpsocial_Callback</servlet-name>
        <servlet-class>
		org.entando.entando.plugins.jpsocial.aps.system.MineCallback
	</servlet-class>
    </servlet>
    <servlet>
        <servlet-name>jpsocial_TwitterConsumer</servlet-name>
        <servlet-class>
		org.entando.entando.plugins.jpsocial.aps.system.MineTwitterConsumer
	</servlet-class>
    </servlet>
    <servlet>
        <servlet-name>jpsocial_TwitterEnforcer</servlet-name>
        <servlet-class>
		org.entando.entando.plugins.jpsocial.aps.system.TwitterEnforcer
	</servlet-class>
    </servlet>
    <servlet>
        <servlet-name>jpsocial_FacebookEnforcer</servlet-name>
        <servlet-class>
		org.entando.entando.plugins.jpsocial.aps.system.FacebookEnforcer
	</servlet-class>
    </servlet>
    <servlet>
        <servlet-name>jpsocial_googleEnforcer</servlet-name>
        <servlet-class>
		org.entando.entando.plugins.jpsocial.aps.system.GoogleEnforcer
	</servlet-class>
    </servlet>
    <servlet>
        <servlet-name>jpsocial_youtubeEnforcer</servlet-name>
        <servlet-class>
		org.entando.entando.plugins.jpsocial.aps.system.YoutubeEnforcer
	</servlet-class>
    </servlet>    
    <servlet>
        <servlet-name>jpsocial_LinkedInEnforcer</servlet-name>
        <servlet-class>
		org.entando.entando.plugins.jpsocial.aps.system.LinkedInEnforcer
	</servlet-class>
    </servlet>    
    <!-- jpsocial :: END end -->
 <!-- jpsocial :: start -->
	<servlet-mapping>
        <servlet-name>jpsocial_Callback</servlet-name>
        <url-pattern>/jpsocial_OAuth/Callback</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>jpsocial_TwitterConsumer</servlet-name>
        <url-pattern>/Twitter</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>jpsocial_TwitterEnforcer</servlet-name>
        <url-pattern>/TwitterEnforcer/*</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>jpsocial_FacebookEnforcer</servlet-name>
        <url-pattern>/FacebookEnforcer/*</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>jpsocial_LinkedInEnforcer</servlet-name>
        <url-pattern>/LinkedInEnforcer/*</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>jpsocial_googleEnforcer</servlet-name>
        <url-pattern>/GoogleEnforcer/*</url-pattern>
    </servlet-mapping>
    <servlet-mapping>
        <servlet-name>jpsocial_youtubeEnforcer</servlet-name>
        <url-pattern>/YoutubeEnforcer/*</url-pattern>
    </servlet-mapping>
	<!-- jpsocial :: end -->
*************************








