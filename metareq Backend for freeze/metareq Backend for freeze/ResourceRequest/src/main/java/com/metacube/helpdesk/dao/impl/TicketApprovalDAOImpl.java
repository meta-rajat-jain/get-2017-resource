package com.metacube.helpdesk.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.TicketApprovalDAO;
import com.metacube.helpdesk.dao.TicketDAO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Ticket;
import com.metacube.helpdesk.model.TicketApproval;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.vo.TicketStatusCount;

@Repository("ticketApprovalDAO")
@Transactional
public class TicketApprovalDAOImpl implements TicketApprovalDAO {

    @Autowired
    private SessionFactory sessionFactory;
    @Resource
    TicketDAO ticketDAO;

    /**
     * To save an approval object for ticket
     * 
     * @param the
     *            approval object to save
     * @return status flag for DB query execution
     */
    @Override
    public Status saveTicketApproval(TicketApproval ticketApproval) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(TicketApproval.class);
            session.save(ticketApproval);
        } catch (Exception e) {
            e.printStackTrace();
            result = Status.Error_Occured;
        }
        return result;
    }

    /**
     * To get tickets corresponding to a status open for action of helpdesk
     * 
     * @param the
     *            ticket status string
     * @return the list of tickets for the status
     */
    @Override
    public List<Ticket> getAllHelpdeskStatusBasedTickets(String status) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session
                .createCriteria(TicketApproval.class)
                .add(Restrictions.eq("helpdeskApprovalStatus", status))
                .setProjection(
                        Projections.projectionList().add(
                                Projections.property("ticketNo")))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Ticket> allHelpdeskStatusBasedTickets = cr.list();
        return allHelpdeskStatusBasedTickets;
    }

    /**
     * To get tickets corresponding to a status open for action of
     * manager/Approver
     * 
     * @param the
     *            ticket status string and the employee wwho is approver
     * @return the list of tickets for the status
     */
    @Override
    public List<Ticket> getTicketsOfApproverBasedOnStatus(Employee employee,
            String status) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session
                .createCriteria(TicketApproval.class)
                .add(Restrictions.eq("approver", employee))
                .add(Restrictions.eq("approveStatus", status))
                .setProjection(
                        Projections.projectionList().add(
                                Projections.property("ticketNo")))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Ticket> allStatusBasedTicketsByEmployee = cr.list();
        return allStatusBasedTicketsByEmployee;
    }

    /**
     * To update an approval table tuple
     */
    @Override
    public Status update(TicketApproval approvalObjectForCurrentTicket) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            session.update(approvalObjectForCurrentTicket);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }

    /**
     * Get Approval table entry for the given ticket
     */
    @Override
    public TicketApproval get(int ticketNo) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(TicketApproval.class).add(
                Restrictions.eq("ticketNo", ticketDAO.getTicket(ticketNo)));
        TicketApproval ticketApproval = (TicketApproval) cr.uniqueResult();
        return ticketApproval;
    }

    /**
     * To return the count for tickets based on status of ticket for helpdesk
     */
    @Override
    public List<TicketStatusCount> getTicketsCountOfHelpDeskBasedOnStatus() {
        List<TicketStatusCount> listOfCount = new ArrayList<TicketStatusCount>();
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session
                .createCriteria(TicketApproval.class)
                .setProjection(
                        Projections
                                .projectionList()
                                .add(Projections
                                        .groupProperty("helpdeskApprovalStatus"),
                                        "helpdeskApprovalStatus")
                                .add(Projections.count("ticketApprovalId"),
                                        "count"));
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

    /**
     * To return the count for tickets based on status of ticket for Approver
     */
    @Override
    public List<TicketStatusCount> getTicketsCountOfApproverBasedOnStatus(
            Employee employee) {
        List<TicketStatusCount> listOfCount = new ArrayList<TicketStatusCount>();
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session
                .createCriteria(TicketApproval.class)
                .add(Restrictions.eq("approver", employee))
                .setProjection(
                        Projections
                                .projectionList()
                                .add(Projections.groupProperty("approveStatus"),
                                        "approveStatus")
                                .add(Projections.count("ticketApprovalId"),
                                        "count"));
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
}
