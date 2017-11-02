/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package org.entando.entando.plugins.jpconstantcontact.aps.system.services;

import com.constantcontact.ConstantContact;
import com.constantcontact.components.contacts.Contact;
import com.constantcontact.components.contacts.ContactList;
import org.entando.entando.plugins.jpconstantcontact.aps.system.services.config.ConstantContactConfig;

/**
 *
 * @Alberto Piras
 */
public interface IConstantContactManager {
    
   
    public ConstantContactConfig getConfig();
    
    public void setConfig(ConstantContactConfig _config);
    
    public void createNewSalesforceLead(Contact lead, ContactList salesforceList);
    
}
