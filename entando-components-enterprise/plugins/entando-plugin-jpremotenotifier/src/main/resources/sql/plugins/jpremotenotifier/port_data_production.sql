INSERT INTO sysconfig(version, item, descr, config) VALUES ('production', 'jpremotenotify_sitesConfig', 'Remote Notifier for cluster', '<sitesConfig useSan="false">
		<site code="A">
				<descr>Domain</descr>
				<baseUrl>http://localhost:8080/</baseUrl>
				<fullBaseUrl>http://localhost:8080/testcluster/</fullBaseUrl>
				<ip>127.0.0.1</ip>
		</site>
		<site code="B">
				<descr>Slave</descr>
				<baseUrl>http://localhost:8081/</baseUrl>
				<fullBaseUrl>http://localhost:8081/testcluster/</fullBaseUrl>
				<ip>127.0.0.1</ip>
		</site>
</sitesConfig>');