package org.entando.entando.plugins.jpconstantcontact.aps.system.services;


import com.constantcontact.components.contacts.ContactList;
import com.constantcontact.components.contacts.EmailAddress;
import com.constantcontact.components.emailcampaigns.EmailCampaignBase;
import com.constantcontact.components.emailcampaigns.EmailCampaignRequest;
import com.constantcontact.components.emailcampaigns.EmailCampaignResponse;
import com.constantcontact.components.emailcampaigns.MessageFooter;
import com.constantcontact.components.emailcampaigns.SentToContactList;
import com.constantcontact.components.emailcampaigns.schedules.EmailCampaignSchedule;
import com.constantcontact.components.emailcampaigns.tracking.opens.EmailCampaignTrackingOpen;
import com.constantcontact.exceptions.service.ConstantContactServiceException;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.entando.entando.plugins.jpconstantcontact.apsadmin.ctct.ConstantContactAction;

public class ConstantContact_Campaign extends ConstantContactAction{
    
    
    public ConstantContact_Campaign(){}
    
//    //MAIL AND MAIL FROM ARE EQUALS: theese are the same mail used on login, obvious
//    //campaign
//    //Create new Campaign
//    public String addSimpleCampaign() throws IllegalArgumentException, ConstantContactServiceException{
//        try{
//            System.out.println("\nStart adding new campaing: \n");
//            EmailCampaignRequest newcampaing = new EmailCampaignRequest();
//            newcampaing.setSubject(_object);
//            SentToContactList singleSent= new SentToContactList(_currentList);
//            List<SentToContactList> listasent = new ArrayList<SentToContactList>();
//            listasent.add(singleSent);
//            newcampaing.setSentToContactLists(listasent);
//            newcampaing.setFromName(_sentFromName);
//            EmailAddress mail= new EmailAddress();
//            //use Acccount info and take email?
//            //or insert manually
//            mail.setEmailAddress(_emailAddress);
//            newcampaing.setFromEmail(_sentFromEmail);
//            newcampaing.setReplyToEmail(_replyToEmail);
//            newcampaing.setEmailContentFormat(EmailCampaignBase.EmailContentFormat.HTML);
//            // newcampaing.setCreatedDate(currentDate());
//            newcampaing.setEmailContent("<html><body>"+_contentEmail+"</body></html>");
//            newcampaing.setGreetingName(EmailCampaignBase.GreetingName.FIRST_NAME);
//            newcampaing.setGreetingString(_greetings);
//            MessageFooter foot= new MessageFooter();
//            foot.setAddressLine1(_address);
//            foot.setOrganizationName(_organizationName);
//            foot.setCountry(_country);
//            foot.setPostalCode(_postalCode);
//            foot.setCity(_citta);
//            newcampaing.setMessageFooter(foot);
//            newcampaing.setName(_campaignName);
//            newcampaing.setTextContent(_contentEmail);
//            newcampaing.setStatus("DRAFT");
//            //newcampaing.setTemplateType(_template);
//            newcampaing.setVisibleInUi(true);
//            EmailCampaignResponse temp= new EmailCampaignResponse();
//            temp=this.getCtctManager().addEmailCampaign(this.getRequest().getSession().getAttribute("access_token").toString(),newcampaing);
//            return SUCCESS;
//        }
//        catch(Exception e){
//            System.out.println("error making email campaign");
//            this._messageErrors="Error, insert correct values in all fields!";
//            return "errorField";
//        }
//    }
    
    /**Date in UTC format
     *@rturn current Date(String)
     */
    public String currentDate(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        dateFormat.setTimeZone(tz);
        Date date = new Date();
        String dateCurrent=dateFormat.format(date);
        return dateCurrent;
    }
    
    public Date currentDateTraslated(){
        return null;
    }
    
    
    public String scheduleCampaign(){
        try{
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            dateFormat.setTimeZone(tz);
            Date date = new Date();
            //Date dateAfter = new Date(date.getTime()+120000);
            // Date dateAfter = new Date(date.getTime()+7200);
            Date dateAfter = new Date(date.getTime()+72120);
            //60 000   1 minute
            String dateAfterPrint=dateFormat.format(date);
            String newDatePrint=dateFormat.format(dateAfter);
            EmailCampaignSchedule schedule= new EmailCampaignSchedule();
            schedule.setId(_campaignId);
            schedule.setScheduledDate(newDatePrint);
            this.getCtctManager().schedulateCampaign(_campaignId, schedule);
            return SUCCESS;
        }catch(Exception e){
            System.out.println("\n Error making date");
             _messageErrors="Insert a correct schedule date and be sure to have inserted a list of recipient";
            return "errorField";
        }
    }
    
    
    public String emailCampaignUpdate() throws ConstantContactServiceException{
        
        try{
            String result=null;
            EmailCampaignResponse updateCampaigntemp= getEmailCampaing();
            EmailCampaignRequest updateCampaign=updateCampaigntemp.toEmailCampaignRequest();
            updateCampaign.setName(_campaignName);
            updateCampaign.setSubject(_object);
            updateCampaign.setFromEmail(_sentFromEmail);
            updateCampaign.setFromName(_sentFromName);
            updateCampaign.setReplyToEmail(_replyToEmail);
            updateCampaign.setGreetingString(_greetings);
            updateCampaign.setEmailContentFormat(EmailCampaignBase.EmailContentFormat.HTML);
            // newcampaing.setCreatedDate(currentDate());
            updateCampaign.setEmailContent("<html><body>"+_contentEmail+"</body></html>");
            updateCampaign.setGreetingName(EmailCampaignBase.GreetingName.FIRST_NAME);
            MessageFooter foot= new MessageFooter();
            foot.setAddressLine1(_address);
            foot.setOrganizationName(_organizationName);
            foot.setCountry(_country);
            foot.setPostalCode(_postalCode);
            foot.setCity(_citta);
            updateCampaign.setMessageFooter(foot);
            updateCampaign.setTextContent(_contentEmail);
            
            List<SentToContactList> contactLists = updateCampaign.getSentToContactLists();
            if(containList(contactLists, _currentList)==true){
                _messageErrors="Contact updated, but list is already assigned to this contact";
                result= "errorField";
            }else{
                SentToContactList sentList= new SentToContactList();
                sentList.setId(_currentList);
                contactLists.add(sentList);
                updateCampaign.setSentToContactLists(contactLists);
                result= SUCCESS;
            }
            this.getCtctManager().updateEmailCampaign(updateCampaign);
            
            return result;
        }catch(Exception e){
            System.out.println("error updating Campaign");
            _messageErrors="Insert correct values in the form, Attention to insert an univocal Campaign Name";
            return "errorField";
        }
    }
    
    public Integer getEmailsSent( String campaignId ) throws ConstantContactServiceException{
        return this.getCtctManager().getsentSentMails(campaignId);
    }

    public Integer getEmailsOpenedDistinct(String campaignId) throws ConstantContactServiceException{
        Set<String> setOpened = new HashSet<String>();
        List<EmailCampaignTrackingOpen> opened= this.getCtctManager().getOpenedMails(campaignId);
        for( EmailCampaignTrackingOpen temp : opened  ){
            setOpened.add(temp.getContactId());
        }
        return setOpened.size();
    }
    
    public Integer getNuberSentMails() throws IllegalArgumentException, ConstantContactServiceException{
        String stat="SENT";
        int counter=0;
        List<EmailCampaignResponse> campaigns=setUserCampaigns();
        for( EmailCampaignResponse temp : campaigns){
            System.out.print(" stato: "+temp.getStatus());
            if (stat.equals(temp.getStatus())){
                counter++;
            }
        }
        return counter;
    }
    public String[][] getReports() throws IllegalArgumentException, ConstantContactServiceException{
        System.out.println("array  inizio");
        List<EmailCampaignResponse> campaigns=setUserCampaigns();
        int limit=getNuberSentMails();
        System.out.println("campange:"+campaigns);
        System.out.println("limit:"+limit);
        int counter=0;
        System.out.println("counter:"+counter);
        String [][] arrayReport= new String[limit][3];
        String stat="SENT";
        for( EmailCampaignResponse temp : campaigns){
            System.out.print(" stato: "+temp.getStatus());
            if (!stat.equals(temp.getStatus())){continue;}
            System.out.println("####entro nel for:");
            int sent=0;
            int open=0;
            String id=temp.getId();
            if(getEmailsSent(id)>=0){sent=getEmailsSent(id);}
            if(getEmailsOpenedDistinct(id)>=0){open=getEmailsOpenedDistinct(id);}
            arrayReport[counter][0]="casa";
            arrayReport[counter][1]="5";
            arrayReport[counter][2]="3";
            counter++;
        }
        //stampa
        for(int i=0;i<limit;i++){
            for(int j=0;j<3;j++){
                System.out.print(" valori: "+arrayReport[i][j]);
            }
            System.out.println("\n");
        }
        return arrayReport;
    }
    
    /**Check if a List of SentToContactList contains a specific SentToContactList
     *
     * @param list_A
     * @param idListCompare
     * @return
     */
    public boolean containList(List<SentToContactList> list_A, String idListCompare){
        System.out.println("entrato in containList");
        String comp=idListCompare;
        for(SentToContactList tmp: list_A){
            if(comp.equals(tmp.getId().toString())){
                System.out.println("########## la lista e' gia' presente");
                return true;
            }
        }
        return false;
    }
    
    
    /*Clear a List of SentToContactList by a Campaign
    check if all SentToContactList are active, if someone doesn't exists, this will not be included in the new List of SentToContactList
    @return a List SentToContactList made only of active ContactLists
    */
    public List<SentToContactList> clearSentToContactList() throws ConstantContactServiceException{
        List<SentToContactList> listaSents= getEmailCampaing().getSentToContactLists();
        List<SentToContactList> listaSentsUpdated= new ArrayList<SentToContactList>();
        for(SentToContactList tmp : listaSents){
            if (existsCampaign(tmp.getId())==true){
                listaSentsUpdated.add(tmp);
            }
        }
        return listaSentsUpdated;
    }
    

    /**Remove a ContactList by a SentToContactList
     * @param id identifier of ContactList
     * @return a SentToContactList without the specified element
     * @throws ConstantContactServiceException
     */
    public List<SentToContactList> removeAContactList( String id) throws ConstantContactServiceException{
        List<SentToContactList> listaSents= getEmailCampaing().getSentToContactLists();
        List<SentToContactList> listaSentsUpdated= new ArrayList<SentToContactList>();
        for(SentToContactList tmp : listaSents){
            if (!(tmp.getId().equals(id))){
                listaSentsUpdated.add(tmp);
            }
        }
        return listaSentsUpdated;
    }
    
    
    public String emailComplexCampaignUpdate() throws ConstantContactServiceException{
        String date="";
        String result=null;
        EmailCampaignResponse updateCampaigntemp= getEmailCampaing();
        EmailCampaignRequest updateCampaign=updateCampaigntemp.toEmailCampaignRequest();
        updateCampaign.setName(_campaignName);
        updateCampaign.setTextContent("default content");
        updateCampaign.setEmailContent("<html><body>"+updateCampaigntemp.getEmailContent()+"</body></html>");
        updateCampaign.setTemplateType("CUSTOM");
        
        
        MessageFooter foot= new MessageFooter();
        foot.setAddressLine1(_address);
        foot.setOrganizationName(_organizationName);
        foot.setPostalCode(_postalCode);
        foot.setCity(_citta);
        foot.setCountry(_country);
       
        updateCampaign.setMessageFooter(foot);

        //check all contactLists, if someone doesn't exists, this will not be included in the new sentToContactList
        List<SentToContactList> contactLists= clearSentToContactList();
        //control if an item in list was selected,  if _currentList==999 no item was selected
        if(!(Integer.parseInt(_currentList)==999)){
            if(containList(contactLists, _currentList)==true){
                contactLists=removeAContactList(_currentList);
                updateCampaign.setSentToContactLists(contactLists);
            }else{
                SentToContactList sentList= new SentToContactList(_currentList);
                contactLists.add(sentList);
                updateCampaign.setSentToContactLists(contactLists);
            }
        }else{
            System.out.println("\n\n#############You have not selected any list to add/remove ");
        }
        try{
            this.getCtctManager().updateEmailCampaign(updateCampaign);
            _messageSuccess="Campaign successfully updated ";
            return SUCCESS;
        }catch(Exception e){
            _messageErrors="Insert Correct values in the form";
            return "errorField";
        }
    }
    
    /**
     * Add a schedule date to the current Campaign
     */
    public String schedulate(){
      String date="";
     if(!date.equals(_eventStartDate)){
            try{
                System.out.println(" Data di schedule passata: "+createStartDate());
                EmailCampaignSchedule schedule=new EmailCampaignSchedule();
                schedule.setId(_campaignId);
                String scheduleteDate=createStartDate();
                schedule.setScheduledDate(scheduleteDate);
                this.getCtctManager().schedulateCampaign(_campaignId, schedule);
                return SUCCESS;
            }catch(Exception e){
                _messageErrors="Insert a correct schedule date and be sure to have inserted a list of recipient";
                return "errorField";
            }
        }else{
            System.out.println("You have to insert date and time");
             _messageErrors="You have not inserted right schedule date";
             return "errorField";
        }
    }
    
    /**
     * Creates the Starting date of an event according to ISO_8601 model
     * @return the starting date
     */
    private String createStartDate(){
        return this._eventStartDate+ISO_8601_T+this._eventStartTimeHours+":"+this._eventStartTimeMinutes+ISO_8601_Z;
    }
    
    
    
    
    public List<SentToContactList> getCampaignToContactLists() throws ConstantContactServiceException{
        return getEmailCampaing().getSentToContactLists();
        
    }
    
    public EmailCampaignResponse getEmailCampaing() throws ConstantContactServiceException{
        return this.getCtctManager().getAEmailCampaign(_campaignId);
    }
    
    public List<EmailCampaignResponse> setUserCampaigns() throws IllegalArgumentException, ConstantContactServiceException {
        try{
            return this.getCtctManager().getEmailCampaings();
        }catch(Exception e){
            System.out.println("error loading campaigns");
            return null;
        }
    }
    
    public String deleteACampaign(){
        try{
            this.getCtctManager().deleteEmailCampaign(this.getRequest().getSession().getAttribute("access_token").toString(),_campaignId);
            return SUCCESS;
        }catch(Exception e){
            System.out.println("Error deleting Campaign");
            return "generalError";
        }
    }
    
    public String getObject() {
        return _object;
    }
    
    public void setObject(String _object) {
        this._object = _object;
    }
    
    public String getSentFromEmail() {
        return _sentFromEmail;
    }
    
    public void setSentFromEmail(String _sentFromEmail) {
        this._sentFromEmail = _sentFromEmail;
    }
    
    public String getReplyToEmail() {
        return _replyToEmail;
    }
    
    public void setReplyToEmail(String _replyToEmail) {
        this._replyToEmail = _replyToEmail;
    }
    
    public String getContentHTML(String content){
        
        return  _startContentTag+content+_endContentTag;
    }
    
    public String getContentEmail() {
        return _contentEmail;
    }
    
    public void setContentEmail(String _contentEmail) {
        this._contentEmail = _contentEmail;
    }
    
    public String getGreetings() {
        return _greetings;
    }
    
    public void setGreetings(String _greetings) {
        this._greetings = _greetings;
    }
    
    public String getOrganizationName() {
        return _organizationName;
    }
    
    public void setOrganizationName(String _organizationName) {
        this._organizationName = _organizationName;
    }
    
    public String getCountry() {
        return _country;
    }
    
    public void setCountry(String _country) {
        this._country = _country;
    }
    
    public String getCitta() {
        return _citta;
    }
    
    public void setCitta(String _citta) {
        this._citta = _citta;
    }
    
    public String getPostalCode() {
        return _postalCode;
    }
    
    public void setPostalCode(String _postalCode) {
        this._postalCode = _postalCode;
    }
    
    public String getCampaignName() {
        return _campaignName;
    }
    
    public void setCampaignName(String _campaignName) {
        this._campaignName = _campaignName;
    }
    
    
    public String getCurrentList() {
        return _currentList;
    }
    
    public void setCurrentList(String _currentList) {
        this._currentList = _currentList;
    }
    
    public String getSentFromName() {
        return _sentFromName;
    }
    
    public void setSentFromName(String _sentFromName) {
        this._sentFromName = _sentFromName;
    }
    public String getEmailAddress() {
        return _emailAddress;
    }
    
    public void setEmailAddress(String _emailAddress) {
        this._emailAddress = _emailAddress;
    }
    public String getAddress() {
        return _address;
    }
    
    public void setAddress(String _address) {
        this._address = _address;
    }
    
    public String getCampaignId() {
        return _campaignId;
    }
    
    public void setCampaignId(String _campaignId) {
        this._campaignId = _campaignId;
    }
    
    public String getTemplate() {
        return _template;
    }
    
    public void setTemplate(String _template) {
        this._template = _template;
    }
    
    public String getContentEmailFull() {
        return _contentEmailFull;
    }
    
    public void setContentEmailFull(String _contentEmailFull) {
        this._contentEmailFull = _contentEmailFull;
    }
    public String getContentStyleSheet() {
        return _contentStyleSheet;
    }
    
    public void setContentStyleSheet(String _contentStyleSheet) {
        this._contentStyleSheet = _contentStyleSheet;
    }
    
    public String getDateSchedule() {
        return _dateSchedule;
    }
    
    public void setDateSchedule(String _dateSchedule) {
        this._dateSchedule = _dateSchedule;
    }
    
    public String getTimeSchedule() {
        return _timeSchedule;
    }
    
    public void setTimeSchedule(String _timeSchedule) {
        this._timeSchedule = _timeSchedule;
    }
    
    public String getEventStartDate() {
        return _eventStartDate;
    }
    
    public void setEventStartDate(String _eventStartDate) {
        this._eventStartDate = _eventStartDate;
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
    
    
    
    public static final String _startContentTag="<html><body>";
    public static final String _endContentTag="</html></body>";
    
    public String _object;
    public String _currentList;
    public String _emailAddress;
    public String _sentFromEmail;
    public String _replyToEmail;
    public String _contentEmail;
    public String _greetings;
    public String _address;
    public String _organizationName;
    public String _country;
    public String _citta;
    public String _postalCode;
    public String _campaignName;
    public String _sentFromName;
    public String _template;
    public String _campaignId;
    public String _messageSuccess;
    public String _messageErrors;

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
    public String _contentEmailFull;
    public String _contentStyleSheet;
    
    public String _dateSchedule;
    public String _timeSchedule;
    
    
    public String _eventStartDate;
    public String _eventStartTimeMinutes;
    public String _eventStartTimeHours;
    public static final String ISO_8601_T="T";
    public static final String ISO_8601_Z="Z";
    public static final String SUCCESS_UPDATING="Campaign successfully updated";
    
}
