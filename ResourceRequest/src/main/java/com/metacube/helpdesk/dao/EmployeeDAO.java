package com.metacube.helpdesk.dao;

import org.springframework.stereotype.Repository;

import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface EmployeeDAO {
    
   // LogIn get(String loginId);
    Status create(Employee employee);
}
