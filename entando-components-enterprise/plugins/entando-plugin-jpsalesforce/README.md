# Entando Saleforce connector

This plugin let Entando create leads (associated to a given campaign) directly
into Salesforce, using their API. Leads are collected through an Entando portal
by mean of a form published in the frontend: this form is essentially a
standard jpwebdynamicform form with the difference that the same form is also
submitted to the Salesforce which, in turn, creates the lead bound to a given
campaign.
The form is created when configuring the widget "Salesforce - publish a form"
in the administration interface.
This plugin also implements the management of the Leads through the appropriate
frontend widgets such as **Salesforce connector - Create a new Lead**,
**Salesforce connector - Leads search** and **Salesforce connector - Project details**.
Portal users must login into basecamp, using the **Salesforce connector - Login**
widget before they can use the widgets aforementioned.



## Digital certificates

This plugin authenticates over _https_ with Salesforce: this means that the
certificates for the servers used must be present in your local JVM
certificates file `.../jre/lib/security/cacerts`).

The certificates are those of the following sites:

 - https://login.salesforce.com
 - https://na15.salesforce.com (this one is subject to change)

The incorrect or missing certificates installation will prevent the plugin
from operating correctly.

### Retrieving digital certificates [Linux]

- Download the certificates in the `/tmp/` (if not found in the package) with the following
 commands:

	**echo -n | openssl s_client -connect login.salesforce.com:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > /tmp/login.salesforce.com.cert**
	
	**echo -n | openssl s_client -connect na15.salesforce.com:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > /tmp/na15.salesforce.com.cert**

- Import them _in the executing JVM_
  `.../jre/lib/security/cacerts with the command`

  **keytool -import -alias login.salesforce -file /tmp/login.salesforce.com.cert -keystore /<JAVA_HOME>/jre/lib/security/cacerts
  keytool -import -alias na15.salesforce -file /tmp/na15.salesforce.com.cert -keystore /<JAVA_HOME>/jre/lib/security/cacerts**

PLEASE NOTE: the **na15.salesforce.com** address might change. In that case the
log shows which is the url the manager is trying to connect to; just repeat
the procedure above


- Add the following snippet in the POM of your project

		<dependency>
			<groupId>org.entando.entando.plugins</groupId>
			<artifactId>entando-plugin-jpsalesforce</artifactId>
			<version>${entando.version}</version>
			<type>war</type>
		</dependency>

## Usage
 - create a connected App from the Salesforce website
 - configure the plugin in Entando

### Create the connected App from Salesforce:

  * click on  **Setup** on the Salesforce main page
  From the left menu -> **Create** -> **Apps** then click **New** in the section **Connected Apps**
* the callback URL is always in the form of **http://localhost:8080/myPortal/do/salesforce/access** (the port _8080_ is typically needed in development environments)
* Grant the following permissions to the Appp: **Access and manage your data** and **Access your basic information (od etc.)**
* set the checkbox **Enable OAuth Settings**

Plugin settings are under te **Plugins** -> **Salesforce connector** menu in the backoffice of Entando

### Customizable fields


* OID: unique number identifying the company (retrieved from the email sent
  upon registration)
* Client ID: ID of the connected app created previously
* Client secret: the secret key of the app
* User ID: the username (email) used to connect to the Salesforce portal
* Security Token: additional security token associated to the user when the
  account is created (received by email!); it can be neither edited nor
  shown in the Salesforce administration page.
* password: password associated to the account


### Predefined values (leave the deafults untouched!)
* Login URL:
  https://login.salesforce.com/services/oauth2/authorize is the address used
  to log-in
* Token Negotiation URL: https://login.salesforce.com/services/oauth2/token is
  the address used to negotiate access tokens
* Desired version: use the desired version of the API. If this setting is left
  blank the system will use the oldest version available. If the version
  declared is neither found nor valid the system will use again the oldest
  version available. The default version is **24.0**.
