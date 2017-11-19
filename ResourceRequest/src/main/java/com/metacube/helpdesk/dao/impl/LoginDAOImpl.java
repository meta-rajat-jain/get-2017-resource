package com.metacube.helpdesk.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;

@Repository("loginDAO")
@Transactional
public class LoginDAOImpl implements LoginDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public LogIn get(String username) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(LogIn.class).add(
                Restrictions.eq("username", username));
        LogIn login = (LogIn) cr.uniqueResult();
        return login;
    }

    @Override
    public Status create(LogIn login) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            int id = (int) session.save(login);
            System.out.println("Login created");
        } catch (Exception e) {
            e.printStackTrace();
            result = Status.Error_Occured;
        }
        return result;
    }

    @Override
    public Response destroyAuthorisationToken(String authorisationToken,
            String username) {
        Session session = this.sessionFactory.getCurrentSession();
        LogIn login = get(username);
        login.setAuthorisationToken(null);
        session.update(login);
        return new Response(1, null, MessageConstants.SUCCESSFULLY_LOGGEDOUT);
    }

    @Override
    public Status update(LogIn login) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            session.update(login);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }
}
