package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.utility.Response;

public interface TicketService {

    TicketDTO modelToDto(Ticket ticket);

    Ticket dtoToModel(TicketDTO ticketDTO);

    Response saveTicket(String authorisationToken, String username,
            TicketDTO ticketDTO);

 


    Response updateTicket(String authorisationToken, String username,
            TicketDTO ticketDTO);

    Status saveTicketHistory(Ticket ticket);

    List<TicketDTO> getAllTicketsOfEmployee(String authorisationToken,
            String username, String username2);

    Status saveTicketApproval(Ticket ticket);

    List<TicketDTO> getAllStatusBasedTicketsForApprover(String authorisationToken,
            String username,String status);

    List<TicketDTO> getAllHelpdeskStatusBasedTickets(String authorisationToken,
            String username, String status);

    Response ticketUpdateApprovalChange(String authorisationToken,
            String username, TicketDTO ticketDTO);

    

}
