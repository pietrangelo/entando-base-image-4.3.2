package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author entando
 *  
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PentahoTransparentAuthTicket implements ITransparentAuthTicket{
   
	public String getTicketId() {
		return ticketId;
	}


	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	private String ticketId;
	

	@Override
	public String toString() {
		return String.format("ticketId: %s",
			ticketId);
  }
  
}
