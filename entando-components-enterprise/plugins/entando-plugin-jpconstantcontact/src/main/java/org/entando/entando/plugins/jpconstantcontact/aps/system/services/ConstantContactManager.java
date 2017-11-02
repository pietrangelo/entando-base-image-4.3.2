package org.entando.entando.plugins.jpconstantcontact.aps.system.services;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpconstantcontact.aps.system.JpConstantContactSystemConstants;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.config.ConstantContactConfig;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.parse.ConstantContactConfigDOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.constantcontact.ConstantContact;
import com.constantcontact.components.contacts.Address;
import com.constantcontact.components.contacts.Contact;
import com.constantcontact.components.contacts.ContactList;
import com.constantcontact.components.contacts.EmailAddress;
import com.constantcontact.components.emailcampaigns.EmailCampaignRequest;
import com.constantcontact.components.emailcampaigns.EmailCampaignResponse;
import com.constantcontact.components.emailcampaigns.schedules.EmailCampaignSchedule;
import com.constantcontact.components.emailcampaigns.tracking.opens.EmailCampaignTrackingOpen;
import com.constantcontact.components.eventspot.Event;
import com.constantcontact.components.eventspot.Registrant.Registrant;
import com.constantcontact.exceptions.service.ConstantContactServiceException;
import com.constantcontact.services.contactlists.ContactListService;
import com.constantcontact.services.emailcampaigns.EmailCampaignService;
import com.constantcontact.services.eventspot.EventSpotService;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.plugins.jpconstantcontact.aps.system.JpConstantContactSystemConstants;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.config.ConstantContactConfig;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.parse.ConstantContactConfigDOM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConstantContactManager extends AbstractService implements IConstantContactManager{
    
    private static final Logger _logger = LoggerFactory.getLogger(ConstantContactManager.class);
    
    @Override
    public void init() throws Exception {
       
        _logger.debug("{}: initialized ", this.getClass().getName());
    }
    
    
    public boolean connectionSalesForce(){
    try{
          System.out.println("\n\n#######Entered in connectionSalesForce");
        _connection = new ConstantContact(getConfig().getApiKey(),getConfig().getToken());
          System.out.println("Connection ok");
        return true;
        }catch(Exception e) {
        // TODO Auto-generated catch block
        System.out.println("Connection error: "+e);
        return false;
        }
    }
    
    /*
    *Search a ContactList by its own name
    *return ContactList 
     */
    public ContactList getListByName(String name) throws ConstantContactServiceException {
    List<ContactList> list= getContactLists();
    for(ContactList temp: list){
                if(temp.getName().equals(name)) {return temp;}
            }
    return null;
    }
    
    /**
     * Creates a new contact( method used by salesforce plugin) 
     * @param lead the new Contact
     * @param salesforceList the new List
     */
    @Override
    public void createNewSalesforceLead(Contact lead, ContactList salesforceList){
       
       EmailAddress address= lead.getEmailAddresses().get(0);
       String mail=address.getEmailAddress();
        
        if(connectionSalesForce()==true){
            System.out.println("\n\n#######Connection ok");
          try{
            salesforceList = addNewContactList(salesforceList);
            Thread.sleep(5000);
           } catch(ConstantContactServiceException e){
            System.out.println("List is Alredy used, it will be not created");
           } catch (Throwable t) {
            System.out.println("Unexpected exception" + t.getMessage());
           }
           try{
               if(existsContactMailBool(getContacts(),mail )==true){
                System.out.println("Mail just used by an active contact");
                }
               else if(existsContactMailBool(getContactsREMOVED(),mail )==true){
            System.out.println("Mail was of a removed event");
               }else{
                    EmailAddress tmp= new EmailAddress();
                    tmp.setEmailAddress(mail);
                    List<EmailAddress> lista= new ArrayList<EmailAddress>();
                    lista.add(tmp);
                    lead.setEmailAddresses(lista);
                    ContactList contactList= getListByName(salesforceList.getName());
                    if(contactList != null){ 
                        List<ContactList> list= new ArrayList<ContactList>();
                       list.add(contactList);
                       lead.setLists(list);
                       addNewContact(lead);
                       System.out.println("Contact successfully created");  
                    }else{  
                    System.out.println("Error creating contact, list = null"); }
               }
           }catch(ConstantContactServiceException e){
               System.out.println("Error creating contact");
           }
       }else{
         System.out.println("\n\n#######No connection");
        }
    }
        
     
    
    
    
     /**Check if an email is just used in a specific list, like active or removed@retu
     * @param lista List to iterate
     * @param mail String to be compared
     * @return true if mail is found in list, false otherwise
     **/
    public boolean existsContactMailBool(List<Contact> list, String mail){
        if(list== null){ System.out.println("list empty");}
        else{
            for(Contact temp: list){
                if(contain(temp.getEmailAddresses(),mail)) {return true;}
            }}
        return false;
    }
      public boolean contain(List<EmailAddress> lista, String name){
        boolean result=false;
        for(EmailAddress tmp : lista){
            if( name.equals(tmp.getEmailAddress())){
                System.out.println("\n>>Found :"+tmp.getEmailAddress().toString());
                return true;}}
        return result;
    }
    
    
    /**
     * Loads ConstantContact settings from DB
     * @throws ApsSystemException
     */
    protected void loadConfig() throws ApsSystemException {
        try{
            String xmlConfig = this.getConfigManager().getConfigItem(JpConstantContactSystemConstants.CONFIG_ITEM_CONSTANTCONTACT);
            ConstantContactConfig config = this.parseXML(xmlConfig);
            this.setConfig(config);
        } catch (Throwable t) {
            _logger.error("Error loading Config");
            throw new ApsSystemException("Error loading Config", t);
        }
    }
    
    /**
     * Transforms XML into ConstantContactConfig Object
     * @param xmlConfig The xml to parse
     * @return the ConstantContactConfig by the xml stored in DB
     * @throws ApsSystemException
     */
    private ConstantContactConfig parseXML(String xmlConfig) throws ApsSystemException {
        ConstantContactConfig config=null;
        try{
            ConstantContactConfigDOM dom=new ConstantContactConfigDOM();
            config=dom.extractConfig(xmlConfig);
        } catch (Throwable t) {
            _logger.error("Error parsing XML {}",xmlConfig);
            throw new ApsSystemException("Error parsing XML", t);
        }
        return config;
    }
    
    /**
     * Changes an Object ConstantContactConfig into XML format
     * @param newConfig The ConstantContactConfig to convert
     * @return The xml of the ConstantContactConfig to store in DB
     * @throws ApsSystemException
     */
    private String buildXml(ConstantContactConfig newConfig) throws ApsSystemException {
        String result=null;
        try{
            ConstantContactConfigDOM dom=new ConstantContactConfigDOM();
            result = dom.createConfigXml(newConfig);
        } catch (Throwable t) {
            _logger.error("Error building XML {}",newConfig);
            throw new ApsSystemException("Error building XML", t);
        }
        return result;
    }
    
    /**
     * Updates the ConstantContact config
     * @param newConfig The new config to update
     * @throws ApsSystemException
     */
    public void updateConfig(ConstantContactConfig newConfig) throws ApsSystemException {
        try{
            String xml = this.buildXml(newConfig);
            this.getConfigManager().updateConfigItem(JpConstantContactSystemConstants.CONFIG_ITEM_CONSTANTCONTACT, xml);
            this.setConfig(newConfig);
        } catch (Throwable t) {
            _logger.error("Error updating Mailgun Config {}",newConfig);
            throw new ApsSystemException("Error updating Mailgun Config", t);
        }
    }
    
    /**
     * Create a new Instance on Constant Contact Client
     * @param apiKey
     * @param accessToken
     * @return
     */
    public static boolean connect(String apiKey, String accessToken){
        try{
            _connection = new ConstantContact(apiKey, accessToken);
            return true;
        }catch(Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Connection error: "+e);
            return false;
        }
    }
    
    //CONTACTS
    public Contact addNewContact(Contact contact) throws ConstantContactServiceException{
        return _connection.addContact(contact);
    }
    public List<Contact> getContacts() throws ConstantContactServiceException{
        return _connection.getContacts(null, null, Contact.Status.ACTIVE).getResults();
    }
    public List<Contact> getContactsREMOVED() throws ConstantContactServiceException{
        return _connection.getContacts(null, null, Contact.Status.REMOVED).getResults();
    }
    public Contact getAContact(String id) throws ConstantContactServiceException{
        return _connection.getContact(id);
        
    }
    //Delete a specific contact from all lists
    public void deleteContactFromList(String ContactId) throws IllegalArgumentException, ConstantContactServiceException{
        this._connection.deleteContactFromLists(ContactId);
    }
    //Overloading
    /**Delete a specific contact from a specific list
     */
    public void deleteContactFromList(String ContactId,String listId) throws IllegalArgumentException, ConstantContactServiceException{
        this._connection.deleteContactFromList(ContactId, listId);
    }
    /**Delete DEFINITIVELY a contact, so its Email Address is banned- Attention
     */
    public void deleteDefinitivelyContact(String id) throws IllegalArgumentException, ConstantContactServiceException{
        this._connection.deleteContact(id);
    }
    
    public void updateAContact(Contact a) throws IllegalArgumentException, ConstantContactServiceException{
        this._connection.updateContact(a, false);
        
    }
    
    //CONTACT LIST
    public ContactList addNewContactList(ContactList newList) throws IllegalArgumentException, ConstantContactServiceException{
        return _connection.addList(newList);
    }
    public void deleteContactList(String token,String Id) throws ConstantContactServiceException{
        ContactListService service= new ContactListService();
        service.deleteList(token, Id);
    }
    /**
     * Generate the main ContactList lis
     * @return main ContactList lis
     * @throws ConstantContactServiceException
     */
    public List<ContactList> getContactLists() throws ConstantContactServiceException{
        return _connection.getLists(null);
    }
    /**
     * Generate a specific ContactList
     * @param id
     * @return a ContactList by its id
     * @throws ConstantContactServiceException
     */
    public ContactList getAList(String id) throws ConstantContactServiceException{
        return _connection.getList(id);
    }
    public List<Contact> getContactsOfList(String id) throws ConstantContactServiceException{
        return _connection.getContactsFromList(id).getResults();
    }
    
    //CAMPAIGNS
    /**
     * Generate a list of EmailCampaignResponse, to be converted in Email CampaignRequest
     * @return List<EmailCampaignResponse>
     * @throws IllegalArgumentException, IllegalArgumentException, ConstantContactServiceException
     */
    public List<EmailCampaignResponse> getEmailCampaings() throws IllegalArgumentException, IllegalArgumentException, ConstantContactServiceException{
        return _connection.getEmailCampaigns().getResults();
    }
    public boolean deleteEmailCampaign(String accesstoken, String id) throws ConstantContactServiceException{
        EmailCampaignService service = new EmailCampaignService();
        return service.deleteCampaign(accesstoken, id);
    }
    public  EmailCampaignResponse addEmailCampaign(String accesstoken, EmailCampaignRequest campaign) throws ConstantContactServiceException{
        EmailCampaignService service = new EmailCampaignService();
        try{ return service.addCampaign(accesstoken, campaign);
        }catch(Exception e){System.out.println("errore nella creazione della campagna nel Managerr");return null;}
    }
    public EmailCampaignResponse addsimpleaddEmailCampaign(EmailCampaignRequest a) throws IllegalArgumentException, ConstantContactServiceException{
        try{return _connection.addEmailCampaign(a);
        }catch(Exception e){System.out.println("errore nella creazione della campagna nel Manager");return null;}
    }
    public void schedulateCampaign(String CampaignId,EmailCampaignSchedule sched) throws IllegalArgumentException, ConstantContactServiceException{
        this._connection.addEmailCampaignSchedule(CampaignId, sched);
    }
    public EmailCampaignResponse getAEmailCampaign(String id) throws ConstantContactServiceException{
        return this._connection.getEmailCampaign(id);
    }
    public void updateEmailCampaign(EmailCampaignRequest a) throws IllegalArgumentException, ConstantContactServiceException{
        
        this._connection.updateEmailCampaign(a);
        
    }
    public Integer getsentSentMails( String id) throws ConstantContactServiceException{
        return this._connection.getEmailCampaignTrackingSends(id, Integer.SIZE, null).getResults().size();
    }
    public  List<EmailCampaignTrackingOpen> getOpenedMails( String id) throws ConstantContactServiceException{
        return this._connection.getEmailCampaignTrackingOpens(id, Integer.SIZE, null).getResults();
    }
    
    
//EVENTS
    
    public List<Event> getEvents() throws ConstantContactServiceException{
        return _connection.getEvents(100).getResults();
        
    }
    
    public Event addEvent(Event e) throws IllegalArgumentException, ConstantContactServiceException{
        return this._connection.addEvent(e);
    }
    
    public Event getEvent(String id) throws ConstantContactServiceException{
        return this._connection.getEvent(id);
    }
    
    
    /**
     *Gets a List of 100 Registrants of an Event
     * @param id of the Event
     * @return a list of Registrants
     */
    public List<Registrant> getRegistrants(String id) throws IllegalArgumentException, ConstantContactServiceException{
        return this._connection.getEventRegistrants(id, 100).getResults();
    }
    
    
    public boolean updateStatusEvent(String eventId, String status) throws IllegalArgumentException, ConstantContactServiceException{
        return this._connection.updateEventStatus(eventId, status);
    }
    
    
    public  Event updateEvent(String accesstoken, Event event) throws IllegalArgumentException, ConstantContactServiceException{
        EventSpotService service= new EventSpotService();
        return service.updateEvent(accesstoken, event);
    }
    
    
    public ConstantContact getConnection() {
        return _connection;
    }
    
    public void setConnection(ConstantContact _connection) {
        this._connection = _connection;
    }
    
    public ConstantContactConfig getConfig() {
        try {
            loadConfig();
        } catch (ApsSystemException ex) {
            //TO DO:
            _logger.error("Exception: "+ex);
        }
        return _config;
    }
    
    public void setConfig(ConstantContactConfig _config) {
        this._config = _config;
    }
    
    
    
    public ConfigInterface getConfigManager() {
        return _configManager;
    }
    
    public void setConfigManager(ConfigInterface _configManager) {
        this._configManager = _configManager;
    }
    public ConstantContactConfigDOM getConfigDOM() {
        return _configDOM;
    }
    
    public void setConfigDOM(ConstantContactConfigDOM _configDOM) {
        this._configDOM = _configDOM;
    }
    
    private ConstantContactConfig _config;
    protected static ConstantContact _connection;
    private ConfigInterface _configManager;
    private ConstantContactConfigDOM _configDOM;
    
}



