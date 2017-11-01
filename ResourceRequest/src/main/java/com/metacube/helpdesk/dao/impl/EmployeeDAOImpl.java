package com.metacube.helpdesk.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;

@Repository("employeeDAO")
@Transactional
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Employee get(LogIn userName) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Employee.class).add(
                Restrictions.eq("username", userName));

        Employee employee = (Employee) cr.uniqueResult();
        return employee;
    }

    @Override
    public List<Employee> getAllManagers(Organisation organisation) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Employee.class)
                .add(Restrictions.eq("designation", "Manager"))
                .add(Restrictions.eq("organisation", organisation));

        List<Employee> allManagers = cr.list();
        return allManagers;
    }

    @Override
    public List<Employee> getAllEmployees(Organisation organisation) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Employee.class).add(
                Restrictions.eq("organisation", organisation));
        List<Employee> allEmployees = cr.list();
        return allEmployees;
    }

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

    @Override
    public Status delete(LogIn userName) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            Employee employee = get(userName);
            employee.setStatus("inactive");
            session.update(employee);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }

    @Override
    public Status addManager(String authorisationToken, String username, Employee manager) {
        Status result = Status.Success;
        Session session = this.sessionFactory.getCurrentSession();
        
       manager.setDesignation("Manager");
        session.update(manager);
        return result;
    }

}
