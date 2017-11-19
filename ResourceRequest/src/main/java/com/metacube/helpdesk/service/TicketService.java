package com.metacube.helpdesk.service;

import java.util.List;
import com.metacube.helpdesk.dto.TicketDTO;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.vo.TicketStatusCount;

public interface TicketService {

    TicketDTO modelToDto(Ticket ticket);

    Ticket dtoToModel(TicketDTO ticketDTO);

    Response saveTicket(String username, TicketDTO ticketDTO);

    Response updateTicket(String username, TicketDTO ticketDTO);

    Status saveTicketHistory(int ticketNo);

    List<TicketDTO> getAllTicketsOfEmployee(String authorisationToken,
            String username, String username2);

    Status saveTicketApproval(Ticket ticket);

    List<TicketDTO> getAllStatusBasedTicketsForApprover(
            String authorisationToken, String username, String status);

    List<TicketDTO> getAllHelpdeskStatusBasedTickets(String authorisationToken,
            String username, String status);

    Response ticketUpdateApprovalChange(String username, TicketDTO ticketDTO);

    List<TicketDTO> getAllStatusBasedTicketsOfUserr(
            String authorisationTokenFromLogin, String username, String status);

    List<TicketStatusCount> getAllStatusBasedTicketsCountForApprover(
            String authorisationToken, String username);

    List<TicketStatusCount> getAllTicketCountOnStatus(
            String authorisationToken, String username);

    TicketDTO getTicket(String authorisationToken, String username,
            TicketDTO ticketDTO);

    List<TicketStatusCount> getAllStatusBasedTicketsCountForHelpdesk(
            String authorisationToken, String username);
}
