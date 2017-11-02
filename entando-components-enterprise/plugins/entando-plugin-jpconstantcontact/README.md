Constant Contact APP
==
---

What is Constant Contact?
--
---
Constant Contact is a company that offers several online marketing services more info (http://www.constantcontact.com/).

Entando Constant Contact Prerequisites 
--
---

In order to use the APP you need to create a Constant Contact APP that will allows you to interact with email marketing services and online surveys 
through Entando platform.

There is an integrated connector to the App that gives access to online services of Constant Contact  through API REST.

How to create a Constant Contact APP?
--
---

Go to [constantcontact-mashery] and create a new app following the instruction.

Pay attention to the callback URL, that must be similar to 
```
http://yourwebsite[:port]/portalName/do/Ctct/completeLogin 
```
Where :
- portalName is the name of the portal.
- yourwebsite is the url of your website can be for testing purpose localhost
- port is the port number is 8080 *non mandatory*
- do/Ctct/completeLogin  **mandatory**

Once you finish the process to create you Constant Contact APP.

You will receive an email with the Constant Contact API Keys data. Those data will be request during the installation of the APP in an Entando Portal.

Install the APP by adding this dependency to the pom.xml
```
		<dependency>
		<groupId>org.entando.entando.plugins</groupId>
		<artifactId>entando-plugin-jpconstantcontact</artifactId>
		<version>${entando.version}</version>
		<type>war</type>
		</dependency>
```

`Attention to use one functionality at one time, because there is a static limit of simultaneously server queries.`


Functioning:
--

After the first start of the portal please configure the APP by inserting the required data:
- APIKEY
- Secret
- Token 
All these info you have been provided during the creation of the Constant Contact APP.

In the APP configuration page by clicking on the Pages Redirect tab, you have to insert the pages code that  manage 
- Contacts
- Campaigns 
- Events

---
All the features exposed by the plugin are protected by **OAuth2** protocol security.

The APP allows you to send emails(called Campaigns) with complex templates to multiple recipients(Contacts) grouped into lists(Contact Lists).
It's also possible to create events in which users can subscribe.



The APP offers you 5 widgets:
    
- **Contacts** : manages users and lists of users.
- **Campaigns** : manages emails and emails' scheduling.
- **Events**: manages the events and events' scheduling.
- **Campaigns Reports**: graphical representation of the unfolding of campaigns.
- **Events Reports**: graphical representation of the unfolding of events.

Details:
---

###Contact widget


It Allows you create and remove lists to which attach users.
So the possibility to manage CRUD operations with recipients and add them to lists.
Into Contact details it's possible to update and add-remove the contact from list.
Note that when you remove the **last** list attached to a recipient, you are removing that recipient.
However for safety reasons, if you remove a entire list of contacts, the contacts which were attached to that list continue to survive.

###Campaign widget


This widget allow you to execute CRUD operation into Campaigns.
You can create a complex campaign(email with complex template) through a link to the Constant Contact website.
You will manage and schedule the Campaign through entando platform.
A campaign must be associated at least one Contact List to be sent.
A campaign can have some different states:

- DRAFT : Campaign is created, but not scheduled
- RUNNING : Campaign is Active
- SCHEDULED : The Campaing is scheduled
- SENT : Campaign is sent

It's possible set a specific schedule date or send right away.
Be careful to insert the schedule date in UTC Time Zone.

###Event widget

It allows you to manage events.
You can create update and activate-deactivate an event.
Through the world icon you can visit the event online and copy and paste the link into yours Email Campaings.
A feature of Events is that they need of a start date and an end date.
Be careful to insert them correctly and in UTC format.

An Event can have some different states:

- DRAFT : Event is Created,  it is not scheduled.
- ACTIVE : Event is active, users can register to the event.
- CANCELLED : Event has been stopped.
- COMPLETE : Event is closed successfully.


Note that at the moment, for external reasons of Constant Contact API, is necessary to Remove Events through Advanced UI(Constant Contact official website).


###Campaigns Reports

This widget is a reports of campaigns and with this the admin can have an idea of how many mails are sent and how many are opened.

###Events Reports


This widget shows you with a chart which are the events with more members.




[constantcontact-mashery]:https://constantcontact.mashery.com/
