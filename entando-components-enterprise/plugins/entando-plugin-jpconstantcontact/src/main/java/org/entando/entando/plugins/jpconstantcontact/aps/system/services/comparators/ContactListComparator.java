package org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators;

import java.util.Comparator;

import com.constantcontact.components.contacts.ContactList;

public class ContactListComparator implements Comparator<ContactList> {

    public int compare(ContactList p1, ContactList p2) {

     String lastName1 = p1.getName().toUpperCase();
     String lastName2 = p2.getName().toUpperCase();

     return lastName1.compareTo(lastName2);
    }
}   

