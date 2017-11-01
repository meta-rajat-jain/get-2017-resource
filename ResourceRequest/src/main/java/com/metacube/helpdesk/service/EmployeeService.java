package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.utility.Response;

public interface EmployeeService {
    Response create(EmployeeDTO employee);

    List<EmployeeDTO> getAllManagers(String authorisationToken, String userName);

    List<EmployeeDTO> getAllEmployees(String authorisationToken, String userName);

    Response addManager(String authorisationToken, String username,String managerUsername); 
}
