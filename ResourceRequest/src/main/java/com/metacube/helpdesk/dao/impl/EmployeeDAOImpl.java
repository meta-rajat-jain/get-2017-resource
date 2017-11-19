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
import com.metacube.helpdesk.dao.GenericDAO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.Designation;
import com.metacube.helpdesk.utility.Status;

@Repository("employeeDAO")
@Transactional
public class EmployeeDAOImpl extends GenericDAO<Employee> implements
        EmployeeDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public EmployeeDAOImpl() {
        super(Employee.class);
    }

    /**
     * @param Login
     *            object corresponding to current logged in user. Description :
     *            Method to get employee details on the basis of its login
     *            information.
     * @return Employee object corresponding to username.
     */
    /**
     * @param Organisation
     *            object corresponding to which managers are required.
     *            Description : Method to get list of all managers in particular
     *            organisation.
     * @return List of managers of type Employee.
     */
    @Override
    public List<Employee> getAllManagers(Organisation organisation) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session
                .createCriteria(Employee.class)
                .add(Restrictions.eq("designation", Designation.Manager))
                .add(Restrictions.eq("organisation", organisation))
                .add(Restrictions
                        .eq("status", Constants.EMPLOYEE_STATUS_ACTIVE))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Employee> allManagers = cr.list();
        return allManagers;
    }

    /**
     * @param Organisation
     *            object corresponding to which all employees are required.
     *            Description : Method to get list of all employees in
     *            particular organisation.
     * @return List of all employees of type Employee.
     */
    @Override
    public List<Employee> getAllEmployees(Organisation organisation) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session
                .createCriteria(Employee.class)
                .add(Restrictions.eq("organisation", organisation))
                .add(Restrictions
                        .eq("status", Constants.EMPLOYEE_STATUS_ACTIVE))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Employee> allEmployees = cr.list();
        return allEmployees;
    }

    /**
     * @param Login
     *            object corresponding to which employee details are required.
     *            Description : Method to get details of particular employee.
     * @return Employee object having all details of employee.
     */
    @Override
    public Employee getEmployee(LogIn login) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Employee.class).add(
                Restrictions.eq("username", login));
        Employee employee = (Employee) cr.uniqueResult();
        return employee;
    }

    /**
     * @param Employee
     *            object to be added. Description : Method to add employee to
     *            the corresponding organisation.
     * @return Status Success : if employee added successfully Error_Occured :
     *         if any error occurred while performing add functionality
     */
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
    public Object updateEmployee(Employee employee) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            session.update(employee);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }

    @Override
    public List<Employee> getEmployeesNotInPaticularTeam(LogIn[] array) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Employee.class);
        criteria.add(Restrictions.not(Restrictions.in("username", array)))
                .add(Restrictions
                        .eq("status", Constants.EMPLOYEE_STATUS_ACTIVE))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Employee> allEmployees = criteria.list();
        return allEmployees;
    }
}
