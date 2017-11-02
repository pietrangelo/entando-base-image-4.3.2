package org.entando.entando.plugins.jpconstantcontact.apsadmin.ctct;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.constantcontact.components.contacts.Contact;
import com.constantcontact.components.contacts.ContactList;
import com.constantcontact.components.emailcampaigns.EmailCampaignRequest;
import com.constantcontact.exceptions.service.ConstantContactServiceException;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.entando.entando.plugins.jpconstantcontact.aps.internalservlet.login.LoginConstantContactAction;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.*;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.config.ConstantContactConfig;



public class ConstantContactAction extends LoginConstantContactAction{
    
    public void ConstantContactAction(){};
    
    public String control(){
        if(isInSession()){
            return SUCCESS;}
        return "noLogged";
    }
    
    public boolean isInSession(){
        HttpSession session = this.getRequest().getSession();
        if(session.getAttribute("access_token")!=null&&_connessione==true){
            System.out.println("Session ok\n");
            return true;
        }else{
            System.out.println("No Session\n");
            return false;}
    }
    
    /**
     *Next methods get the constant contact settings parameters
     */
    public String getAPI(){
        return this.getCtctManager().getConfig().getApiKey();
    }
    
    public String getSecret(){
        return this.getCtctManager().getConfig().getSecret();
    }
    /**Get the access token stored in Settings by User
     * @return access token
     */
    public String getTokenSettings(){
        return this.getCtctManager().getConfig().getToken();
    }
    public String getContactPage(){
        return this.getCtctManager().getConfig().getContactPage();
    }
    public String getContactFrame(){
        return this.getCtctManager().getConfig().getContactFrame();
    }
    public String getCampaignPage(){
        return this.getCtctManager().getConfig().getCampaignPage();
    }
    public String getCampaignFrame(){
        return this.getCtctManager().getConfig().getCampaignFrame();
    }
    public String getEventPage(){
        return this.getCtctManager().getConfig().getEventPage();
    }
    public String getEventFrame(){
        return this.getCtctManager().getConfig().getEventFrame();
    }
    
    /**
     * Saves MailgunCOnfiguration
     * @return success or error
     */
    public String saveConfiguration(){
        ConstantContactConfig a= new ConstantContactConfig();
        a.setApiKey(_updateAPIKey);
        a.setSecret(_updateSecret);
        a.setToken(_updateToken);
        a.setContactPage(_updateContactPage);
        a.setContactFrame(_updateContactFrame);
        a.setCampaignPage(_updateCampaignPage);
        a.setCampaignFrame(_updateCampaignFrame);
        a.setEventPage(_updateEventPage);
        a.setEventFrame(_updateEventFrame);
        try{
            this.getCtctManager().updateConfig(a);
            _messageSuccessDB="Credentials updated successfully";
            return "success";
        }catch(ApsSystemException e){
            System.out.println("error updating credentials");
            _messageErrorDB="Error updating credentials";
            return "error";
        }
    }
    
    public List<ContactList> setMainContactsLists() throws Exception, Throwable{
        try{
            return this.getCtctManager().getContactLists();
        }
        catch(Exception e){System.out.println("Error reading ContactList list");
        return null;
        }
    }
    
    /**Gets the access token by SESSION
     * @return access token
     */
    public String getToken(){
        return this.getRequest().getSession().getAttribute("access_token").toString();
    }
    
    /**Gets the name of ContactList by its Id
     * @param id of the Campaign
     * @return ContactList's name
     * @throws ConstantContactServiceException
     */
    public String getNameContactListById(String id) throws ConstantContactServiceException{
        try{
            return this.getCtctManager().getAList(id).getName();
        }catch(Exception e){
            System.out.println("List not found, that was removed");}
        return "";
    }
    
    
    /**Verifies if exists a Campaign by its id
     * @param id of the Campaign
     * @return true if that campaign exists, false otherwise
     */
    public boolean existsCampaign(String id){
        try{this.getCtctManager().getAList(id);
        System.out.println("List  found");
        return true;
        }catch(Exception e){  System.out.println("List not found, that was removed");
        return false;}
    }
    
    /**Get and set Methods*/
    public List<EmailCampaignRequest> getUserCampaigns() {
        return _userCampaigns;
    }
    public String getListId() {
        return _listId;
    }
    public void setListId(String listId) {
        this._listId = listId;
    }
    public List<Contact> getSingleContactList() {
        return _singleContactList;
    }
    public List<ContactList> getMainContactsList() {
        return _mainContactsList;
    }
    
    public ConstantContactManager getCtctManager() {
        return _ctctManager;
    }
    public void setCtctManager(ConstantContactManager _ctctManager) {
        this._ctctManager = _ctctManager;
    }
    public LoginConstantContactAction getLoginCtctAction() {
        return _loginCtctAction;
    }
    public void setLoginCtctAction(LoginConstantContactAction _loginCtctAction) {
        this._loginCtctAction = _loginCtctAction;
    }
    
    
    public String getUpdateAPIKey() {
        return _updateAPIKey;
    }
    
    public void setUpdateAPIKey(String _updateAPIKey) {
        this._updateAPIKey = _updateAPIKey;
    }
    
    public String getUpdateSecret() {
        return _updateSecret;
    }
    
    public void setUpdateSecret(String _updateSecret) {
        this._updateSecret = _updateSecret;
    }
    public String getMessageSuccessDB() {
        return _messageSuccessDB;
    }

    public void setMessageSuccessDB(String _messageSuccessDB) {
        this._messageSuccessDB = _messageSuccessDB;
    }
    public String getMessageErrorDB() {
        return _messageErrorDB;
    }
    public void setMessageErrorDB(String _messageErrorDB) {
        this._messageErrorDB = _messageErrorDB;
    }
    public String getUpdateToken() {
        return _updateToken;
    }
    public void setUpdateToken(String _updateToken) {
        this._updateToken = _updateToken;
    }
     public String getUpdateContactPage() {
        return _updateContactPage;
    }
    public void setUpdateContactPage(String _updateContactPage) {
        this._updateContactPage = _updateContactPage;
    }
    public String getUpdateContactFrame() {
        return _updateContactFrame;
    }
    public void setUpdateContactFrame(String _updateContactFrame) {
        this._updateContactFrame = _updateContactFrame;
    }
    public String getUpdateCampaignPage() {
        return _updateCampaignPage;
    }
    public void setUpdateCampaignPage(String _updateCampaignPage) {
        this._updateCampaignPage = _updateCampaignPage;
    }
    public String getUpdateCampaignFrame() {
        return _updateCampaignFrame;
    }
    public void setUpdateCampaignFrame(String _updateCampaignFrame) {
        this._updateCampaignFrame = _updateCampaignFrame;
    }
    public String getUpdateEventPage() {
        return _updateEventPage;
    }
    public void setUpdateEventPage(String _updateEventPage) {
        this._updateEventPage = _updateEventPage;
    }
    public String getUpdateEventFrame() {
        return _updateEventFrame;
    }
    public void setUpdateEventFrame(String _updateEventFrame) {
        this._updateEventFrame = _updateEventFrame;
    }
    
    public LoginConstantContactAction _loginCtctAction;
    private ConstantContactManager _ctctManager;
    public List<ContactList> _mainContactsList;
    public List<Contact> _singleContactList;
    public String _listId;
    public List<EmailCampaignRequest> _userCampaigns;
    public Contact currentContact;
    public String _messageErrorDB;
    public String _updateAPIKey;
    public String _updateSecret;
    public String _updateToken;
    public String _updateContactPage;
    public String _updateContactFrame;
    public String _updateCampaignPage;
    public String _updateCampaignFrame;
    public String _updateEventPage;
    public String _updateEventFrame;
    public String _messageSuccessDB;
    
}