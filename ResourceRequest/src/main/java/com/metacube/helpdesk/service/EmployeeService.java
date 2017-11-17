package com.metacube.helpdesk.service;

import java.util.List;
import java.util.Set;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.utility.Response;

public interface EmployeeService {

    Response create(EmployeeDTO employee);

    List<EmployeeDTO> getAllManagers(String authorisationToken, String userName);

    List<EmployeeDTO> getAllEmployees(String authorisationToken, String userName);

    Response addManager(String authorisationToken, String username,
            String managerUsername);

    Response deleteEmployee(String employeeToBeDeleted);

    Response updateEmployee(EmployeeDTO employeeToBeUpdated);

    Employee get(LogIn logIn);

    EmployeeDTO getEmployeeByUsername(String authorisationToken,
            String username, String employeeUsername);

    Employee dtoToModel(EmployeeDTO employeeDTO);

    EmployeeDTO modelToDto(Employee employee);

    Set<Team> getEmployeeTeams(String authorisationToken, String username,
            String employeeUsername);

    EmployeeDTO getEmployeeDetails(String authorisationTokenFromLogin,
            String username);
}
