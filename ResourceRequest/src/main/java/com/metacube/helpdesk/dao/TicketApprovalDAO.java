package com.metacube.helpdesk.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.model.TicketApproval;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface TicketApprovalDAO {

    Status saveTicketApproval(TicketApproval ticketApproval);

    List<Ticket> getTicketsOfApproverBasedOnStatus(Employee employee,
            String status);

    List<Ticket> getAllHelpdeskStatusBasedTickets(String status);

   

    Status update(TicketApproval approvalObjectForCurrentTicket);

    TicketApproval get(int ticketNo);

}
