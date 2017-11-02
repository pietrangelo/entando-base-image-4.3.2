 package org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators;

import java.util.Comparator;

import com.constantcontact.components.eventspot.Event;

public class EventComparator implements Comparator<Event> {

    public int compare(Event p1, Event p2) {

     String lastName1 = p1.getName().toUpperCase();
     String lastName2 = p2.getName().toUpperCase();

     return lastName1.compareTo(lastName2);
    }
}   

