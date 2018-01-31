package com.metacube.helpdesk.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.ResourceDAO;
import com.metacube.helpdesk.model.ItResource;
import com.metacube.helpdesk.model.ResourceCategory;

@Repository("resourceDAO")
@Transactional
public class ResourceDAOImpl implements ResourceDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<ItResource> getAllCategoryBasedResources(
            ResourceCategory resourceCategory) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(ItResource.class).add(
                Restrictions.eq("resourceCategory", resourceCategory));
        List<ItResource> allCategoryBasedResources = cr.list();
        return allCategoryBasedResources;
    }
}
