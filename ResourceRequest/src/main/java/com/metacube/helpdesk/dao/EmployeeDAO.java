package com.metacube.helpdesk.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface EmployeeDAO {

    // LogIn get(String loginId);
    Status create(Employee employee);

    List<Employee> getAllEmployees(Organisation organisation);
    
    List<Employee> getAllManagers(Organisation organisation);

    Employee get(LogIn userName);

    Status delete(LogIn userName);

    Status addManager(String authorisationToken, String username,
            Employee manager);
}
