package com.metacube.helpdesk.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Status;

@Repository("organisationDAO")
@Transactional
public class OrganisationDAOImpl implements OrganisationDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Status create(Organisation organisation) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(Organisation.class);
            System.out.println(session.save(organisation));
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }

    @Override
    public Organisation getByDomain(String domain) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(Organisation.class).add(
                Restrictions.eq("domain", domain));
        Organisation organisation = (Organisation) cr.uniqueResult();
        return organisation;
    }

    @Override
    public Organisation getByName(String orgName) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Organisation.class).add(
                Restrictions.eq("orgName", orgName));
        Organisation organisation = (Organisation) cr.uniqueResult();
        return organisation;
    }

    @Override
    public List<Organisation> getAll() {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Organisation.class);
        List<Organisation> organisation = cr.list();
        return organisation;
    }

    @Override
    public List<String> getAllOrganisationDomains() {
        // TODO Auto-generated method stub
        Session session = this.sessionFactory.getCurrentSession();
        Criteria cr = session.createCriteria(Organisation.class);
        cr.setProjection(Projections.property("domain"));
        List<String> organisationDomains = cr.list();
        return organisationDomains;
    }

    @Override
    public Organisation getByLogin(LogIn login) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Organisation.class).add(
                Restrictions.eq("username", login));
        Organisation organisation = (Organisation) cr.uniqueResult();
        return organisation;
    }
}
