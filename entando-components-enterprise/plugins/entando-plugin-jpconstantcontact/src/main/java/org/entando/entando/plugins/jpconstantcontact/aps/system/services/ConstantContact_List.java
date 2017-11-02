package org.entando.entando.plugins.jpconstantcontact.aps.system.services;

import com.constantcontact.components.contacts.Contact;
import com.constantcontact.components.contacts.ContactList;
import com.constantcontact.exceptions.service.ConstantContactServiceException;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import org.entando.entando.plugins.jpconstantcontact.apsadmin.ctct.ConstantContactAction;

public class ConstantContact_List extends ConstantContactAction{
  
  public ConstantContact_List(){}
  
  public List<Contact> setSingleContactList() throws ConstantContactServiceException {
    try{return this.getCtctManager().getContactsOfList(this._listId);
    }
    catch(Exception e){
      System.out.println("\n Error reading list members");
      return null;
    }
  }
  
  public String addList() throws Throwable{
    if(isInSession()){
      enstablishConnection();
      ContactList temp = new ContactList();
      temp.setName(this._listName);
      temp.setStatus(com.constantcontact.components.contacts.ContactListStatus.ACTIVE);
      try{
        this.getCtctManager().addNewContactList(temp);
        return SUCCESS;
      }catch(ConstantContactServiceException err){
        System.out.println("Error creating new list");
        _message="This list name was already used";
        return "errorField";
      }catch(Exception err){
        System.out.println("Error creating new list");
        return "errorConnection";
      }
    }return "noLogged";
  }
  
  public String deleteList(){
    if(isInSession()){
      enstablishConnection();
      try{
        this.getCtctManager().deleteContactList( this.getRequest().getSession().getAttribute("access_token").toString(),_listId);
        return SUCCESS;
      }catch(Exception e){
        return "generalError";
      }
    }return "noLogged";
  }
  
  public String getListName() {
    return _listName;
  }
  public void setListName(String _listName) {
    this._listName = _listName;
  }
  public String _listId;
  
  public String getListId() {
    return _listId;
  }
  public void setListId(String _listId) {
    this._listId = _listId;
  }
  public List<ContactList> getMainContactsList() {
    return _mainContactsList;
  }
  public String getMessage() {
    return _message;
  }
  
  public void setMessage(String _message) {
    this._message = _message;
  }
  
  public List<ContactList> _mainContactsList;
  public String _listName;
  public String _message;
  
  
}



