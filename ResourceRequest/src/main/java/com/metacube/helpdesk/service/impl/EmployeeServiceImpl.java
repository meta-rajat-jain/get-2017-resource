package com.metacube.helpdesk.service.impl;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.SimpleMD5;
import com.metacube.helpdesk.utility.Status;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Resource
    EmployeeDAO employeeDAO;

    @Resource
    LoginDAO loginDAO;

    @Resource
    OrganisationDAO organisationDAO;
    

    @Resource
    LoginService loginService;
    
    public Response create(EmployeeDTO employeeDTO) {
        int statusCode = 0;
        String message = "";
        Employee employee = null;

        // default value is set here
        // parameters will come through constant class
        employeeDTO.setStatus("active");
        // parameters will come  through constant class
        employeeDTO.setDesignation("Team Member");

        LogIn logIn = new LogIn();
        logIn.setUsername(employeeDTO.getLoginDetails().getUsername());

        try {
            logIn.setPassword(SimpleMD5.hashingWithConstantSalt(employeeDTO
                    .getLoginDetails().getPassword()));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        logIn.setAuthorisationToken(null);

        Status addFlag = loginDAO.create(logIn);
        System.out.println("login creation flag "+addFlag);
        if (addFlag.equals(Status.Success)) {
            employee = dtoToModel(employeeDTO);
        }else {
            statusCode = 0;
            message = MessageConstants.ACCOUNT_NOT_CREATED;
        }
        addFlag = employeeDAO.create(employee);

        if (addFlag.equals(Status.Success)) {
            statusCode = 1;
            message = MessageConstants.ACCOUNT_CREATED;

        } else {
            statusCode = 0;
            message = MessageConstants.ACCOUNT_NOT_CREATED;
        }

        return new Response(statusCode, null, message);
    }

    protected Employee dtoToModel(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setEmployeeName(employeeDTO.getName());
        employee.setContactNumber(employeeDTO.getContactNumber());
        employee.setDesignation(employeeDTO.getDesignation());
        employee.setOrganisation(organisationDAO.get(employeeDTO.getOrgDomain()));
        employee.setStatus(employeeDTO.getStatus());
        employee.setUsername(loginDAO.get((employeeDTO.getLoginDetails())
                .getUsername()));

        return employee;
    }

    protected EmployeeDTO modelToDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employee.getEmployeeName());
        employeeDTO.setContactNumber(employee.getContactNumber());
        employeeDTO.setDesignation(employee.getDesignation());
        employeeDTO.setStatus(employee.getStatus());
        employeeDTO.setOrgDomain(employee.getOrganisation().getDomain());
        //doubtful case
        employeeDTO.setLoginDetails(loginService.modelToDto(employee.getUsername()));
       
        return employeeDTO;
    }
}
