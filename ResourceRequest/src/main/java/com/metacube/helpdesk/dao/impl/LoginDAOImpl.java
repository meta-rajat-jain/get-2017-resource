package com.metacube.helpdesk.dao.impl;



import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.modal.LogIn;


@Repository
@Transactional
public class LoginDAOImpl {
    
    @Autowired
    private SessionFactory sessionFactory;
    public LogIn get(String username){
        Session session = this.sessionFactory.getCurrentSession();
     // Criteria query
     Criteria cr = session.createCriteria(LogIn.class).add(
     Restrictions.eq("username",username));
     LogIn login = (LogIn) cr.uniqueResult();
     return login;
    }
    
}
