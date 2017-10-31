package com.metacube.helpdesk.dao.impl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.utility.Status;

@Repository("employeeDAO")
@Transactional
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Status create(Employee employee) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Criteria cr = session.createCriteria(Employee.class);

            session.save(employee);

        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }

}
