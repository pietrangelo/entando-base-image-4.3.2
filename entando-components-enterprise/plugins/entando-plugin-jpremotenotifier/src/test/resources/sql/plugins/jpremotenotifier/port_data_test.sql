INSERT INTO sysconfig(version, item, descr, config)
    VALUES ('test', 'jpremotenotify_sitesConfig', 'Remote Notifier for cluster', '<sitesConfig useSan="false">
		<site code="1">
				<descr>Domain</descr>
				<baseUrl>http://localhost:8080/</baseUrl>
				<fullBaseUrl>http://localhost:8080/jAPS-EG/</fullBaseUrl>
				<ip>127.0.0.1</ip>
		</site>
		<site code="2">
				<descr>Slave</descr>
				<baseUrl>http://localhost:8081/</baseUrl>
				<fullBaseUrl>http://localhost:8081/jAPS-EG/</fullBaseUrl>
		</site>
		<site code="3">
				<descr>Remote Slave</descr>
				<baseUrl>http://localhost:8081/</baseUrl>
				<fullBaseUrl>http://localhost:8081/jAPS-EG/</fullBaseUrl>
		</site>
</sitesConfig>');