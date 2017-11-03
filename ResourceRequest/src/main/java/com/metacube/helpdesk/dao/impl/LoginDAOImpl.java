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
    public LogIn get(String username){
        Session session = this.sessionFactory.getCurrentSession();
     // Criteria query
     Criteria cr = session.createCriteria(LogIn.class).add(
     Restrictions.eq("username",username));
     LogIn login = (LogIn) cr.uniqueResult();
     return login;
    }

    @Override
    public Status create(LogIn login) {
       
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(LogIn.class);
             
            session.save(login);
            
    
        } catch (Exception e) {
           e.printStackTrace();
            result = Status.Error_Occured;
        }
return result;
    }

    @Override
    public void update(String username,String authorisationToken) {
        Session session = this.sessionFactory.getCurrentSession();
        LogIn login = get(username);
        login.setAuthorisationToken(authorisationToken);
        session.update(login);
        
    }

    @Override
    public Response destroyAuthorisationToken(String authorisationToken,
            String username) {
        Session session = this.sessionFactory.getCurrentSession();
        LogIn login = get(username);
        login.setAuthorisationToken(null);
        session.update(login);
        return new Response(1,null,MessageConstants.SUCCESSFULLY_LOGGEDOUT);
    }

    @Override
    public Response update(String username) {
        Session session = this.sessionFactory.getCurrentSession();
        LogIn login = get(username);
        login.setEnabled(true);
        session.update(login);
        return new Response(1,null,MessageConstants.REGISTER_SUCCESSFULLY);
    }
    
}
