package com.metacube.helpdesk.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.TicketHistoryDAO;
import com.metacube.helpdesk.model.TicketHistory;
import com.metacube.helpdesk.utility.Status;

@Repository("ticketHistoryDAO")
@Transactional
public class TicketHistoryDAOImpl implements TicketHistoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Status saveTicketHistory(TicketHistory ticketHistory) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(TicketHistory.class);
            session.save(ticketHistory);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }
}
