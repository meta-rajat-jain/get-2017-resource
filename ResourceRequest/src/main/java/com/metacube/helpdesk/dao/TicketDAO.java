package com.metacube.helpdesk.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface TicketDAO {

    Status saveTicket(Ticket ticket);

    List<Ticket> getTicketsGeneratedByEmployee(Employee employee);

    List<Ticket> getTicketsOfEmployeeBasedOnStatus(Employee employee,
            String status);

    List<Ticket> getAllDateBasedTickets(Date date);

    Status deleteTicket(Ticket ticket);

    Status updateTicket(Ticket ticket);

}
