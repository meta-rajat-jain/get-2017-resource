package com.metacube.helpdesk.dao;

import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.model.TicketHistory;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface TicketHistoryDAO {

    Status saveTicketHistory(TicketHistory ticketHistory);

}
