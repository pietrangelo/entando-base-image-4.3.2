package org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators;

import java.util.Comparator;

import com.constantcontact.components.contacts.Contact;

public class ContactComparator implements Comparator<Contact> {

    public int compare(Contact p1, Contact p2) {

     String lastName1 = p1.getLastName().toUpperCase();
     String lastName2 = p2.getLastName().toUpperCase();

     return lastName1.compareTo(lastName2);
    }
}   

