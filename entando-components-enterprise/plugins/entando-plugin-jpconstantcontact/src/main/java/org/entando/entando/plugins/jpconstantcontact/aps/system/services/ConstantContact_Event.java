/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package org.entando.entando.plugins.jpconstantcontact.aps.system.services;

import com.constantcontact.components.contacts.Contact;
import com.constantcontact.components.eventspot.Event;
import com.constantcontact.components.eventspot.EventHostContact;
import com.constantcontact.components.eventspot.Registrant.Registrant;
import com.constantcontact.exceptions.service.ConstantContactServiceException;
import com.constantcontact.services.eventspot.EventSpotService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletInputStream;
import org.apache.commons.io.IOUtils;
import org.entando.entando.plugins.jpconstantcontact.apsadmin.ctct.ConstantContactAction;
import org.json.JSONObject;

/**
 *
 * @author home
 */
public class ConstantContact_Event extends ConstantContactAction{
    
    
    public List<Event> getUserEvents() throws ConstantContactServiceException{
        return this.getCtctManager().getEvents();
    }
    
    public String addEvent(){
        if(isInSession()){
            Event newEvent= new Event();
            newEvent.setName(_eventName);
            newEvent.setTitle(_eventTitle);
            newEvent.setType(_eventType);
            EventHostContact contact= new EventHostContact();
            contact.setName(_userName);
            contact.setOrganizationName(_organizationName);
            newEvent.setContact(contact);
            newEvent.setStartDate(createStartDate());
            newEvent.setEndDate(createEndDate());
            newEvent.setLocation(_eventLocation);
            newEvent.setTimeZoneId(_eventTimeZone);
            try{
                this.getCtctManager().addEvent(newEvent);
                return SUCCESS;
            }catch( Exception e){
                System.out.println("error making Event");
                this._messageErrors="Please insert correct valus in the form and verify your connection";
                return "errorFields";
            }
        }return "noLogged";
    }
    
    public String updateStatus(){
        try{
            this.getCtctManager().updateStatusEvent(this._eventId,_eventStatus);
             this._messageSuccess="Event schedule successfully updated";
            return SUCCESS;
        }catch(ConstantContactServiceException exceptionArguments){
            System.out.println("error updating Event status,Event can't be scheduled in the past");
            this._messageErrors="Error updating Event status, event can't be scheduled in the past ";
            return "errorFields";
        }
        catch(IllegalArgumentException e){
            System.out.println("error updating Event status,Event can't be scheduled in the past");
            this._messageErrors="Error updating Event status, event can't be scheduled in the past ";
            return "errorFields";
        }
        catch(Exception e){
            System.out.println("error updating Event");
            this._messageErrors="Verify your internet connection";
            return "errorFields";
        }
    }
    
    
    public Event getCurrentEvent() throws ConstantContactServiceException{
        return this.getCtctManager().getEvent(_eventId);
    }
    
    
    public String update() throws ConstantContactServiceException, IOException{
        if(isInSession()){
            Event currentEvent= this.getCtctManager().getEvent(_eventId);
            currentEvent.setName(_eventName);
            currentEvent.setTitle(_eventTitle);
            EventHostContact contact= new EventHostContact();
            contact.setName(_userName);
            contact.setOrganizationName(_organizationName);
            currentEvent.setContact(contact);
            currentEvent.setType(_eventType);
//            currentEvent.setStartDate(_eventStarting);
//            currentEvent.setEndDate(_eventEnding);
            currentEvent.setStartDate(createStartDate());
            currentEvent.setEndDate(createEndDate());
            currentEvent.setLocation(_eventLocation);
            currentEvent.setTimeZoneId(_eventTimeZone);
            try{
                this.getCtctManager().updateEvent(this.getRequest().getSession().getAttribute("access_token").toString(), currentEvent);
                this._messageSuccess="Event successfully updated";
                return SUCCESS;
            }catch(IllegalArgumentException exceptionArguments){
                System.out.println("error updating Event");
                this._messageErrors="Insert correct values in the form";
                return "errorFields";
            }catch(ConstantContactServiceException exception){
                System.out.println("error updating Event");
                this._messageErrors="Insert correct values in the form";
                return "errorFields";
            }
            catch(Exception e){
                System.out.println("error updating Event");
                this._messageErrors="Insert correct values in the form and verify your internet connection";
                return "errorFields";
            }
        }return "noLogged";
    }

  
    /**
     * Creates the Starting date of an event according to ISO_8601 model
     * @return the starting date
     */
    private String createStartDate(){
        return this._eventStartDate+ISO_8601_T+this._eventStartTimeHours+":"+this._eventStartTimeMinutes+ISO_8601_Z;
    }
    
    /**
     * Creates the Ending date of an event according to ISO_8601 model
     * @return the starting date
     */
    private String createEndDate(){
        return this._eventEndDate+ISO_8601_T+this._eventEndTimeHours+":"+this._eventEndTimeMinutes+ISO_8601_Z;
    }
    
    public Event getAEvent(String id) throws ConstantContactServiceException{
        return this.getCtctManager().getEvent(id);
    }
    
    public List<Registrant> getEventRegistrants(String id) throws ConstantContactServiceException{
        return this.getCtctManager().getRegistrants(id);
    }
    
    
    public String getEventName() {
        return _eventName;
    }
    
    public void setEventName(String _eventName) {
        this._eventName = _eventName;
    }
    
    public String getEventTitle() {
        return _eventTitle;
    }
    
    public void setEventTitle(String _eventTitle) {
        this._eventTitle = _eventTitle;
    }
    
    public String getEventType() {
        return _eventType;
    }
    
    public void setEventType(String _eventType) {
        this._eventType = _eventType;
    }
    
    public String getEventLocation() {
        return _eventLocation;
    }
    
    public void setEventLocation(String _eventLocation) {
        this._eventLocation = _eventLocation;
    }
    
    
    public String getEventTimeZone() {
        return _eventTimeZone;
    }
    
    public void setEventTimeZone(String _eventTimeZone) {
        this._eventTimeZone = _eventTimeZone;
    }
    
    public String getEventStarting() {
        return _eventStarting;
    }
    
    public void setEventStarting(String _eventStarting) {
        this._eventStarting = _eventStarting;
    }
    
    public String getEventEnding() {
        return _eventEnding;
    }
    
    public void setEventEnding(String _eventEnding) {
        this._eventEnding = _eventEnding;
    }
    
    public String getEventStartDate() {
        return _eventStartDate;
    }
    
    public void setEventStartDate(String _eventStartDate) {
        this._eventStartDate = _eventStartDate;
    }
    
    public String getEventEndDate() {
        return _eventEndDate;
    }
    
    public void setEventEndDate(String _eventEndDate) {
        this._eventEndDate = _eventEndDate;
    }
    
    public String getEventStartTimeMinutes() {
        return _eventStartTimeMinutes;
    }
    
    public void setEventStartTimeMinutes(String _eventStartTimeMinutes) {
        this._eventStartTimeMinutes = _eventStartTimeMinutes;
    }
    
    public String getEventStartTimeHours() {
        return _eventStartTimeHours;
    }
    
    public void setEventStartTimeHours(String _eventStartTimeHours) {
        this._eventStartTimeHours = _eventStartTimeHours;
    }
    
    public String getEventEndTimeMinutes() {
        return _eventEndTimeMinutes;
    }
    
    public void setEventEndTimeMinutes(String _eventEndTimeMinutes) {
        this._eventEndTimeMinutes = _eventEndTimeMinutes;
    }
    
    public String getEventEndTimeHours() {
        return _eventEndTimeHours;
    }
    
    public void setEventEndTimeHours(String _eventEndTimeHours) {
        this._eventEndTimeHours = _eventEndTimeHours;
    }
    
    
    public String getEventId() {
        return _eventId;
    }
    
    public void setEventId(String _eventId) {
        this._eventId = _eventId;
    }
    public String getMessageSuccess() {
        return _messageSuccess;
    }
    
    public void setMessageSuccess(String _messageSuccess) {
        this._messageSuccess = _messageSuccess;
    }
    
    public String getMessageErrors() {
        return _messageErrors;
    }
    
    public void setMessageErrors(String _messageErrors) {
        this._messageErrors = _messageErrors;
    }
    public String getUserName() {
        return _userName;
    }
    
    public void setUserName(String _userName) {
        this._userName = _userName;
    }
    
    public String getOrganizationName() {
        return _organizationName;
    }
    
    public void setOrganizationName(String _organizationName) {
        this._organizationName = _organizationName;
    }
    
    
    public String getEventStatus() {
        return _eventStatus;
    }
    
    public void setEventStatus(String _eventStatus) {
        this._eventStatus = _eventStatus;
    }
    
    public Contact getCurrentContact() {
        return currentContact;
    }
    
    public void setCurrentContact(Contact currentContact) {
        this.currentContact = currentContact;
    }
    
    public String _eventName;
    public String _eventTitle;
    public String _eventType;
    public String _eventStarting;
    public String _eventEnding;
    public String _eventStartDate;
    public String _eventEndDate;
    public String _eventLocation;
    public String _eventTimeZone;
    public String _eventStartTimeMinutes;
    public String _eventStartTimeHours;
    public String _eventEndTimeMinutes;
    public String _eventEndTimeHours;
    public String _eventId;
    public String _eventStatus;
    public String _messageSuccess;
    public String _messageErrors;
    public String _userName;
    public String _organizationName;
    public static final String ISO_8601_T="T";
    public static final String ISO_8601_Z="Z";
    
}
