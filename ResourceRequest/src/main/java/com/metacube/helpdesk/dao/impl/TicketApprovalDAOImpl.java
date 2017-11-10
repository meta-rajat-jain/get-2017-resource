package com.metacube.helpdesk.dao.impl;

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

@Repository("ticketApprovalDAO")
@Transactional
public class TicketApprovalDAOImpl implements TicketApprovalDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @Resource
    TicketDAO ticketDAO;
    
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
    
    
    @Override
    public List<Ticket> getAllHelpdeskStatusBasedTickets(String status) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(TicketApproval.class)
                .add(Restrictions.eq("helpdeskApprovalStatus", status))
                .add(Restrictions.eq("approveStatus","approved" ))
                .setProjection(Projections.projectionList()
                        .add(Projections.property("ticketNo"))
                        ).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Ticket> allHelpdeskStatusBasedTickets = cr.list();
        return allHelpdeskStatusBasedTickets;
    }

    @Override
    public List<Ticket> getTicketsOfApproverBasedOnStatus(Employee employee,
            String status) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(TicketApproval.class)
                .add(Restrictions.eq("approver", employee))
                .add(Restrictions.eq("approveStatus",status )).setProjection(Projections.projectionList()
                        .add(Projections.property("ticketNo"))
                        ).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Ticket> allStatusBasedTicketsByEmployee = cr.list();
        return allStatusBasedTicketsByEmployee;
    }

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

    @Override
    public TicketApproval get(int ticketNo) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(TicketApproval.class).add(
                Restrictions.eq("ticketNo", ticketDAO.getTicket(ticketNo)));
        TicketApproval ticketApproval = (TicketApproval) cr.uniqueResult();
        return  ticketApproval;
    }
}
