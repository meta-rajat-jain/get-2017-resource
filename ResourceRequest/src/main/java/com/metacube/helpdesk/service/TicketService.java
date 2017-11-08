package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.dto.ResourceCategoryDTO;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.Ticket;

public interface TicketService {

    TicketDTO modelToDto(Ticket ticket);

    Ticket dtoToModel(TicketDTO ticketDTO);

    

}
