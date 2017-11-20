package com.metacube.helpdesk.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.GenericDAO;
import com.metacube.helpdesk.dao.TicketDAO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.vo.TicketStatusCount;

@Repository("ticketDAO")
@Transactional
public class TicketDAOImpl extends GenericDAO implements TicketDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public TicketDAOImpl() {
        super(Ticket.class);
    }

    @Override
    public int saveTicket(Ticket ticket) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(Ticket.class);
        System.out.println(ticket.getRequestDate());
        return (int) session.save(ticket);
    }

    @Override
    public List<Ticket> getTicketsGeneratedByEmployee(Employee employee) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Ticket.class)
                .add(Restrictions.eq("requester", employee))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Ticket> allTicketsByEmployee = cr.list();
        return allTicketsByEmployee;
    }

    @Override
    public List<TicketStatusCount> getTicketCountForStatusOfEmployee(
            Employee employee) {
        List<TicketStatusCount> listOfCount = new ArrayList<TicketStatusCount>();
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session
                .createCriteria(Ticket.class)
                .add(Restrictions.eq("requester", employee))
                .setProjection(
                        Projections
                                .projectionList()
                                .add(Projections.groupProperty("status"),
                                        "status")
                                .add(Projections.count("ticketNo"), "count"));
        List<Object> results = cr.list();
        for (Object r : results) {
            Object[] row = (Object[]) r;
            TicketStatusCount count = new TicketStatusCount();
            count.setStatus((String) row[0]);
            count.setCount((Long) row[1]);
            listOfCount.add(count);
        }
        return listOfCount;
    }

    @Override
    public List<Ticket> getTicketsOfEmployeeBasedOnStatus(Employee employee,
            String status) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Ticket.class)
                .add(Restrictions.eq("requester", employee))
                .add(Restrictions.eq("status", status))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Ticket> allStatusBasedTicketsByEmployee = cr.list();
        return allStatusBasedTicketsByEmployee;
    }

    @Override
    public List<Ticket> getAllDateBasedTickets(Date date) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Ticket.class)
                .add(Restrictions.eq("requestDate", date))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Ticket> allDateBasedTickets = cr.list();
        return allDateBasedTickets;
    }

    @Override
    public Status deleteTicket(Ticket ticket) {
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
    public Status updateTicket(Ticket ticket) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            session.update(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            result = Status.Error_Occured;
        }
        return result;
    }

    @Override
    public Ticket getTicket(int ticketNo) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Ticket.class).add(
                Restrictions.eq("ticketNo", ticketNo));
        Ticket ticket = (Ticket) cr.uniqueResult();
        return ticket;
    }
}
