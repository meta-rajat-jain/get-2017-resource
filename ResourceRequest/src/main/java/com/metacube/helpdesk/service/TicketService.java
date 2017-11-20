package com.metacube.helpdesk.service;

import java.util.List;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.utility.TicketCreationResponse;
import com.metacube.helpdesk.vo.TicketStatusCount;

public interface TicketService {

    TicketDTO modelToDto(Ticket ticket);

    Ticket dtoToModel(TicketDTO ticketDTO);

    TicketCreationResponse saveTicket(String username, TicketDTO ticketDTO);

    Response updateTicket(String username, TicketDTO ticketDTO);

    Status saveTicketHistory(int ticketNo);

    List<TicketDTO> getAllTicketsOfEmployee(String username2);

    Status saveTicketApproval(Ticket ticket);

    List<TicketDTO> getAllStatusBasedTicketsForApprover(String username,
            String status);

    List<TicketDTO> getAllHelpdeskStatusBasedTickets(String username,
            String status);

    Response ticketUpdateApprovalChange(String username, TicketDTO ticketDTO);

    List<TicketDTO> getAllStatusBasedTicketsOfUser(String username,
            String status);

    List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
            String username);

    List<TicketStatusCount> getAllTicketCountOnStatus(String username);

    TicketDTO getTicket(TicketDTO ticketDTO);

    List<TicketStatusCount> getAllStatusBasedTicketsCountForHelpdesk();
}
