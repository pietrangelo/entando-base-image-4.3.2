package org.entando.entando.plugins.jpconstantcontact.aps.system.services.comparators;

import java.util.Comparator;

import com.constantcontact.components.emailcampaigns.EmailCampaignBase;

public class CampaignComparator implements Comparator<EmailCampaignBase> {

    public int compare(EmailCampaignBase p1, EmailCampaignBase p2) {

     String lastName1 = p1.getName().toUpperCase();
     String lastName2 = p2.getName().toUpperCase();

     return lastName1.compareTo(lastName2);
    }
}   

