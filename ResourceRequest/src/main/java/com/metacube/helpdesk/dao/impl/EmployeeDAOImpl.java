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
import com.metacube.helpdesk.utility.Status;

@Repository("employeeDAO")
@Transactional
public class EmployeeDAOImpl  extends GenericDAO implements EmployeeDAO {

    @Autowired
    private SessionFactory sessionFactory;
    

    public EmployeeDAOImpl() {
        super(Employee.class);
    }
    
    /**
     * @param Login object corresponding to current logged in user.
     * Description : Method to get employee details on the basis of its login information.
     * @return Employee object corresponding to username.
     */
 
    
    /**
     * @param Organisation object corresponding to which managers are required.
     * Description : Method to get list of all managers in particular organisation.
     * @return List of managers of type Employee.
     */
    @Override
    public List<Employee> getAllManagers(Organisation organisation) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Employee.class)
                .add(Restrictions.eq("designation", "Manager"))
                .add(Restrictions.eq("organisation", organisation)).add(
                        Restrictions.eq("status", "active")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Employee> allManagers = cr.list();
        return allManagers;
    }
    
    /**
     * @param Organisation object corresponding to which all employees are required.
     * Description : Method to get list of all employees in particular organisation.
     * @return List of all employees of type Employee.
     */
    @Override
    public List<Employee> getAllEmployees(Organisation organisation) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Employee.class).add(
                Restrictions.eq("organisation", organisation)).add(
                        Restrictions.eq("status", "active")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);;
        List<Employee> allEmployees = cr.list();
        return allEmployees;
    }
    
    /**
     *@param Login  object corresponding to which employee details are required. 
     *Description : Method to get details of particular employee.
     *@return Employee object having all details of employee.
     */
    @Override
    public Employee getEmployee(LogIn login) {
        Session session = this.sessionFactory.getCurrentSession();
        // Criteria query
        Criteria cr = session.createCriteria(Employee.class).add(
                Restrictions.eq("username", login)).add(
                        Restrictions.eq("status", "active"));
        Employee employee = (Employee) cr.uniqueResult();
        return employee;
    }
    
    /**
     * @param Employee object to be added.
     * Description : Method to add employee to the corresponding organisation.
     * @return Status Success : if employee added successfully
     *                Error_Occured : if any error occurred while performing add functionality
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
    
    /**
     * @param Employee object to be deleted.
     * Description : Method to delete employee from the corresponding organisation. It will change the
     *               status of the employee as inactive from active.It will not remove employee from database.
     * @return Status Success : if employee deleted successfully
     *                Error_Occured : if any error occurred while performing delete functionality.
     */
    @Override
    public Status deleteEmployee(Employee employee) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            employee.setStatus("inactive");
            session.update(employee);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }
    

    /**
     * @param authorisationToken : Token for authorisation of admin of a particular organisation.
     * @param username : username of admin of a particular organisation.
     * @param manager :  Manager object to be deleted.
     * Description : Method to add manager to the corresponding organisation.       
     * @return Status Success : if manager added successfully
     *                Error_Occured : if any error occurred while performing adding functionality.
     */
    @Override
    public Status addManager(String authorisationToken, String username, Employee manager) {
        Status result = Status.Success;
        try {
        Session session = this.sessionFactory.getCurrentSession();    
        manager.setDesignation("Manager");
        session.update(manager);
        
        }catch (Exception e) {
            e.printStackTrace();
            result = Status.Error_Occured;
        }
        return result;
    }

    @Override
    public Object updateEmployee(Employee employee) {
        Status result = Status.Success;
        try {
            Session session = this.sessionFactory.getCurrentSession();
            //employee.setEmployeeId(getEmployee(employee.getUsername()).getEmployeeId());
            session.update(employee);
        } catch (Exception e) {
            result = Status.Error_Occured;
        }
        return result;
    }

}
