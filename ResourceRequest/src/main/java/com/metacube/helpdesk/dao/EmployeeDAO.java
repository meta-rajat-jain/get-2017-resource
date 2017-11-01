package com.metacube.helpdesk.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface EmployeeDAO {
    
   // LogIn get(String loginId);
    Status create(Employee employee);
    
    List<Employee> getAllEmployees();
    List<Employee> getAllManagers();
    Employee get(LogIn userName);
    Status delete(LogIn userName);
}
