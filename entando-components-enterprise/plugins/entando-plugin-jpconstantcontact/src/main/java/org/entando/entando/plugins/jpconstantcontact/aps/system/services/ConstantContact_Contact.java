
package org.entando.entando.plugins.jpconstantcontact.aps.system.services;

import com.constantcontact.components.contacts.Contact;
import com.constantcontact.components.contacts.ContactList;
import com.constantcontact.components.contacts.EmailAddress;
import com.constantcontact.components.contacts.Note;
import com.constantcontact.components.emailcampaigns.SentToContactList;
import com.constantcontact.exceptions.service.ConstantContactServiceException;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts2.components.ElseIf;
import org.entando.entando.plugins.jpconstantcontact.aps.internalservlet.login.LoginConstantContactAction;
import org.entando.entando.plugins.jpconstantcontact.apsadmin.ctct.ConstantContactAction;

/** String
 *
 * @author home
 */
public class ConstantContact_Contact extends ConstantContactAction{
    
    public ConstantContact_Contact(){}
    
    public boolean contain(List<EmailAddress> lista, String name){
        boolean result=false;
        for(EmailAddress tmp : lista){
            if( name.equals(tmp.getEmailAddress())){
                System.out.println("\n>>Found :"+tmp.getEmailAddress().toString());
                return true;}}
        return result;
    }
    
    public Contact getContactByEmail(List<Contact> lista, String mail){
        Contact result=null;
        if(lista== null){ System.out.println("list empty");}
        else{
            for(Contact temp: lista){
                if(contain(temp.getEmailAddresses(),mail)) {return temp;}
            }}
        return result;
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
    
    
    
    public String addSimpleContact() throws ConstantContactServiceException, InterruptedException{
        if(isInSession()){
            enstablishConnection();
            String result=null;
            //check if that email is already used by an active contact
            if(existsContactMailBool(getContactsList(),this._newAddress )==true){
                System.out.println("Mail just used by an active contact");
                this._messageErrors="Error, email address is already used by a Contact!";
                result="errorField";
                //check if that email is ALREADY used by a deleted contact
            }else if(existsContactMailBool(getListRemoved(),this._newAddress )==true){
                try{
                    System.out.println("Mail just used by a removed contact, retrieve it");
                    //resuscita contact to the list NUOVO PROGETTO
                    List<ContactList> list= new ArrayList<ContactList>();
                    list.add(this.getCtctManager().getAList(_currentList));
                    Contact nuovo=getContactByEmail(getListRemoved(), this._newAddress);
                    System.out.println("\n\nContatto  name: "+nuovo.getFirstName());
                    nuovo.setFirstName(this._fName);
                    nuovo.setLastName(this._surname);
                    nuovo.setLists(list);
                    this.getCtctManager().updateAContact(nuovo);
                    result=SUCCESS;
                }catch(Exception error){
                    System.out.println("\n\nError retrieving a deleted contact");
                    result="errorField";
                }
                //email address is NEW
            }else{
                try{
                    Contact added= new Contact();
                    added.setFirstName(this._fName);
                    added.setLastName(this._surname);
                    EmailAddress tmp= new EmailAddress();
                    tmp.setEmailAddress(this._newAddress);
                    List<EmailAddress> lista= new ArrayList<EmailAddress>();
                    lista.add(tmp);
                    added.setEmailAddresses(lista);
                    List<ContactList> list= new ArrayList<ContactList>();
                    list.add(this.getCtctManager().getAList(_currentList));
                    added.setLists(list);
                    this.getCtctManager().addNewContact(added);
                    result=SUCCESS;
                }catch(ConstantContactServiceException e ){
                    System.out.println("\n\nError creating Contact");
                    _messageErrors="The email address entered is not valid";
                    result="errorField";
                }catch(Exception ex ){
                    System.out.println("\n\nError creating Contact");
                    result="errorConnection";
                }
            }
            return result;
        }return "noLogged";
    }
    
    public List<Contact> getContactsList() throws ConstantContactServiceException {
        return this.getCtctManager().getContacts();
    }
    
    public List<Contact> getListRemoved() throws ConstantContactServiceException {
        return this.getCtctManager().getContactsREMOVED();
    }
    
    public String deleteContactFromAlLLists() throws ConstantContactServiceException {
        if(isInSession()){
            enstablishConnection();
            try{
                this.getCtctManager().deleteContactFromList(this._contactId);
                //IMPORTANT: UPDATE USER CONTACTS LIST
                return SUCCESS;
            }catch(Exception e ){
                System.out.println("Error deleting Contact");
                return "generalError";
            }
        }return "noLogged";
    }
    
    public List<ContactList> getContactLists() throws ConstantContactServiceException{
        return getCurrentContact().getLists();
    }
    
    public String deleteContactFromAList() throws ConstantContactServiceException {
        if(isInSession()){
            enstablishConnection();
            try{
                this.getCtctManager().deleteContactFromList(this._contactId,this._listId);
                return SUCCESS;
            }catch(Exception e ){
                System.out.println("Error deleting Contact");
                return "generalError";
            }
        }return "noLogged";
    }
    
    
//    public String deleteContactFromAList2() throws ConstantContactServiceException {
//        if(isInSession()){
//            enstablishConnection();
//                try{
//            Contact updated= getCurrentContact();
//            
//            List<ContactList> listaSents= getCurrentContact().getLists();
//            List<ContactList> listaSentsUpdated= new ArrayList<ContactList>();
//            for(ContactList tmp : listaSents){
//                if (!(tmp.getId().equals(_listId))){
//                    listaSentsUpdated.add(tmp);
//                }
//            }
//            
//            updated.setLists(listaSentsUpdated);
//            this.getCtctManager().updateAContact(updated);
//              return SUCCESS;
//            }catch(Exception e ){
//                System.out.println("Error deleting Contact");
//                return "generalError";
//            }
//        }return "noLogged";
//        
//    }
    
    
    public boolean containList(List<ContactList> list_A, String idListCompare){
        System.out.println("entrato in containList");
        String comp=idListCompare;
        for(ContactList tmp: list_A){
            if(comp.equals(tmp.getId().toString())){
                System.out.println("########## la lista e' gia' presente");
                return true;
            }
        }
        return false;
    }
    
    /**Remove a ContactList by a List ContactList
    * @param id identifier of ContactList
    * @return a SentToContactList without the specified element
    * @throws ConstantContactServiceException
    */
    public List<ContactList> removeAContactList( String id) throws ConstantContactServiceException{
      List<ContactList> listaSents= getCurrentContact().getLists();
      List<ContactList> listaSentsUpdated= new ArrayList<ContactList>();
          for(ContactList tmp : listaSents){
            if (!(tmp.getId().equals(id))){
            listaSentsUpdated.add(tmp);
            }
      }
      return listaSentsUpdated;
    }


    /*Clear a List of ContactList by a List ContactList
    check if all SentToContactList are active, if someone doesn't exists, this will not be included in the new List of SentToContactList
    @return a List SentToContactList made only of active ContactLists
    */
    public List<ContactList> clearSentToContactList() throws ConstantContactServiceException{
      List<ContactList> listaSents= getCurrentContact().getLists();
      List<ContactList> listaSentsUpdated= new ArrayList<ContactList>();
          for(ContactList tmp : listaSents){
            if (existsCampaign(tmp.getId())==true){
            listaSentsUpdated.add(tmp);
            }
      }
      return listaSentsUpdated;
    }

    /**Get the Contact in which you are working, got by _contactId
     *
     * @return
     * @throws ConstantContactServiceException
     */
    public Contact getCurrentContact() throws ConstantContactServiceException{
      return this.getCtctManager().getAContact(_contactId);
    }
    
    
    public String updateContact() throws ConstantContactServiceException{
        String result=null;
        Contact added=this.getCtctManager().getAContact(_contactId);
        added.setFirstName(this._fName);
        added.setLastName(this._surname);
        added.setCompanyName(_company);
        added.setJobTitle(_jobTitle);
        added.setCellPhone(_cellPhone);
        added.setHomePhone(_homePhone);
        List<ContactList> contactLists= clearSentToContactList();
        //        System.out.println("##########noteold contact : "+ getContactById().getNotes().toString());
        //    controllo note:
        //      added.setNotes(null);
        //      this.getCtctManager().updateAContact(added);
        //      List<Note> noteList = new ArrayList<Note>();
        ////    noteList.clear();
        //      Note nota= new Note();
        //      nota.setNote(_noteContent);
        //      noteList.add(nota);
        //      System.out.println("##########note della nuova lista : "+ noteList.toString());
        //      List<Note> noteList = new ArrayList<Note>();
        //      added.setNotes(noteList);
    //  to delete List<ContactList> contactLists = getContactList(_contactId);
        if(!(Integer.parseInt(_currentList)==999)){
            if(containList(contactLists, _currentList)==true){
                contactLists=removeAContactList(_currentList);
                added.setLists(contactLists);
                 _message="List Correctly Removed";
            }else{
                contactLists.add(this.getCtctManager().getAList(_currentList));
                added.setLists(contactLists);
                _message="List Correctly Added";
            }
        }
        try{
            if(contactLists.size()==0){
             this.getCtctManager().updateAContact(added);
                  return "list";
            }            
            this.getCtctManager().updateAContact(added);
            return SUCCESS;
        }catch(Exception e ){
            System.out.println("\n\nError updating Contact");
            _messageErrors="There are some errors,Insert correct values in fields";
            return "errorField";
        }
    }
    
//    public String getAListName(String contactId) throws ConstantContactServiceException{
//        return this.getCtctManager().getAList(contactId).getName();
//    }
    
//      public List<Note> getContactNotes() throws ConstantContactServiceException{
//          System.out.println("note:"+getContactById().getNotes().toString() );
//        return getContactById().getNotes();
//    }
    
    public String getContactId() {
        return _contactId;
    }
    public void setContactId(String _contactId) {
        this._contactId = _contactId;
    }
    public ConstantContactManager getCtctManager() {
        return _ctctManager;
    }
    public void setCtctManager(ConstantContactManager _ctctManager) {
        this._ctctManager = _ctctManager;
    }
    public String getfName() {
        return _fName;
    }
    public void setfName(String fName) {
        this._fName = fName;
    }
    public String getSurname() {
        return _surname;
    }
    public void setSurname(String _surname) {
        this._surname = _surname;
    }
    public LoginConstantContactAction getLoginCtctAction() {
        return _loginCtctAction;
    }
    public void setLoginCtctAction(LoginConstantContactAction _loginCtctAction) {
        this._loginCtctAction = _loginCtctAction;
    }
    public String getListId() {
        return _listId;
    }
    public void setListId(String listId) {
        this._listId = listId;
    }
    public String getNewAddress() {
        return _newAddress;
    }
    public void setNewAddress(String newAddress) {
        this._newAddress = newAddress;
    }
    public String getCurrentList() {
        return _currentList;
    }
    
    public void setCurrentList(String _currentList) {
        this._currentList = _currentList;
    }
    public String getMailAddress() {
        return _mailAddress;
    }
    
    public void setMailAddress(String _mailAddress) {
        this._mailAddress = _mailAddress;
    }
    
    public String getJobTitle() {
        return _jobTitle;
    }
    
    public void setJobTitle(String _jobTitle) {
        this._jobTitle = _jobTitle;
    }
    
    public String getCompany() {
        return _company;
    }
    
    public void setCompany(String _company) {
        this._company = _company;
    }
    
    public String getCellPphone() {
        return _cellPhone;
    }
    
    public void setCellPphone(String _cellPphone) {
        this._cellPhone = _cellPphone;
    }
    
    public String getHomePhone() {
        return _homePhone;
    }
    
    public void setHomePhone(String _homePhone) {
        this._homePhone = _homePhone;
    }
    
      public String getNoteContent() {
        return _noteContent;
    }
    
    public void setNoteContent(String _noteContent) {
        this._noteContent = _noteContent;
    }
  
    public List<ContactList> getContactList() {
        return _contactList;
    }
    
    public void setContactList(List<ContactList> _contactList) {
        this._contactList = _contactList;
    }
      public String getMessage() {
    return _message;
  }

  public void setMessage(String _message) {
    this._message = _message;
  }
  
      public String getMessageErrors() {
    return _messageErrors;
  }

  public void setMessageErrors(String _messageErrors) {
    this._messageErrors = _messageErrors;
  }
    
    public LoginConstantContactAction _loginCtctAction;
    private ConstantContactManager _ctctManager;
    public String _fName;
    public String _surname;
    public String _newAddress;
    public String _contactId;
    public String _listId;
    public String _message;
    public String _messageErrors;
    public String _currentList;
    public String _mailAddress;
    public String _jobTitle;
    public String _company;
    public String _cellPhone;
    public String _homePhone;
    public List<ContactList> _contactList;
    public String _noteContent;
}

