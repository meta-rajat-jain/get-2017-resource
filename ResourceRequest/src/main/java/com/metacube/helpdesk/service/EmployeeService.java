package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.utility.Response;

public interface EmployeeService {
    Response create(EmployeeDTO employee);

    List<EmployeeDTO> getAllManagers();

    List<EmployeeDTO> getAllEmployees(); 
}
