package com.metacube.helpdesk.dao.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.ResourceCategoryDAO;
import com.metacube.helpdesk.model.ResourceCategory;

@Repository("resourceCategory")
@Transactional
public class ResourceCategoryDAOImpl implements ResourceCategoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ResourceCategory getResourceCategoryByName(String categoryName) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(ResourceCategory.class).add(
                Restrictions.eq("categoryName", categoryName));
        ResourceCategory resourceCategory = (ResourceCategory) cr
                .uniqueResult();
        return resourceCategory;
    }

    @Override
    public List<ResourceCategory> getAllResourceCategory() {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(ResourceCategory.class);
        List<ResourceCategory> allCategories = cr.list();
        return allCategories;
    }
}
