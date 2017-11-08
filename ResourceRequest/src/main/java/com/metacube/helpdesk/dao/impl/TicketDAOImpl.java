package com.metacube.helpdesk.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.metacube.helpdesk.dao.GenericDAO;
import com.metacube.helpdesk.dao.TicketDAO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.utility.Status;

@Repository("ticketDAO")
@Transactional
public class TicketDAOImpl extends GenericDAO implements TicketDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public TicketDAOImpl() {
        super(Ticket.class);
    }
    
    @Override
    public Status saveTicket(Ticket ticket){
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(Ticket.class);
            session.save(ticket);

        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result; 
    }
    
    @Override
    public List<Ticket> getTicketsGeneratedByEmployee(Employee employee){
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Ticket.class)
                .add(Restrictions.eq("requester", "employee"));
        List<Ticket> allTicketsByEmployee = cr.list();
        return allTicketsByEmployee;
    }
    
    @Override
    public List<Ticket> getTicketsOfEmployeeBasedOnStatus(Employee employee,String status){
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Ticket.class)
                .add(Restrictions.eq("requester", employee))
                .add(Restrictions.eq("status",status ));
        List<Ticket> allStatusBasedTicketsByEmployee = cr.list();
        return allStatusBasedTicketsByEmployee;
    }
    
    @Override
    public List<Ticket> getAllDateBasedTickets(Date date){
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Ticket.class)
                .add(Restrictions.eq("requestDate", date));
        List<Ticket> allDateBasedTickets = cr.list();
        return allDateBasedTickets;
    }
    
    @Override
    public Status deleteTicket(Ticket ticket){
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            ticket.setStatus("Closed");
            session.update(ticket);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }
    
    @Override
    public Status updateTicket(Ticket ticket){
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            session.update(ticket);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }

}
