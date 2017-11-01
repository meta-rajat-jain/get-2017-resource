package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.SimpleMD5;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.utility.Validation;

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
        String authorisationToken = null;

        // default value is set here
        // parameters will come through constant class
        employeeDTO.setStatus("active");
        // parameters will come through constant class
        employeeDTO.setDesignation("Team Member");

        if (Validation.isNull(employeeDTO.getName())
                || Validation.isNull(employeeDTO.getLogin())
                || Validation.isNull(employeeDTO.getLogin().getUsername())
                || Validation.isNull(employeeDTO.getLogin().getPassword())
                || Validation.isNull(employeeDTO.getContactNumber())
                || Validation.isNull(employeeDTO.getOrgDomain())
                || Validation.isEmpty(employeeDTO.getName())
                || Validation.isEmpty(employeeDTO.getContactNumber())
                || Validation.isEmpty(employeeDTO.getOrgDomain())) {
            return new Response(statusCode, authorisationToken,
                    "Please fill all required fields");
        }

        if (!Validation.validateInput(employeeDTO.getLogin().getUsername(),
                Constants.EMAILREGEX)) {
            return new Response(statusCode, authorisationToken,
                    "Incorrect format of email");
        }

        if (!Validation.validateInput(employeeDTO.getContactNumber(),
                Constants.CONTACT_NUMBER_REGEX)) {
            return new Response(statusCode, authorisationToken,
                    "Incorrect format of contact number");
        }

        if (organisationDAO.getByDomain(employeeDTO.getOrgDomain()) == null) {
            return new Response(2, authorisationToken, "Domain not exist");
        }

        if (loginDAO.get(employeeDTO.getLogin().getUsername()) != null) {
            return new Response(2, authorisationToken,
                    MessageConstants.USERNAME_ALREADY_EXIST);
        }
        LogIn logIn = new LogIn();
        logIn.setUsername(employeeDTO.getLogin().getUsername());

        try {
            logIn.setPassword(SimpleMD5.hashingWithConstantSalt(employeeDTO
                    .getLogin().getPassword()));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        logIn.setAuthorisationToken(null);
        String[] orgDomainFromUsername = employeeDTO.getLogin().getUsername()
                .split("@");

        if (!orgDomainFromUsername[1].equals(employeeDTO.getOrgDomain())) {
            return new Response(
                    2,
                    authorisationToken,
                    "This username can't belong to the specified organisation - Format:yourlogin@orgdomain");
        }
        Status addFlag = loginDAO.create(logIn);
        System.out.println("login creation flag " + addFlag);
        if (addFlag.equals(Status.Success)) {
            employee = dtoToModel(employeeDTO);
        } else {
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
        employee.setOrganisation(organisationDAO.getByDomain(employeeDTO
                .getOrgDomain()));
        employee.setStatus(employeeDTO.getStatus());
        employee.setUsername(loginDAO.get((employeeDTO.getLogin())
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
        // doubtful case
        employeeDTO.setLogin(loginService.modelToDto(employee.getUsername()));

        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getAllManagers() {
        // TODO Auto-generated method stub
        List<EmployeeDTO> allManagersDTO=new ArrayList<EmployeeDTO>() ;
        List<Employee> allManagers=employeeDAO.getAllManagers();
        for(Employee manager:allManagers){
            allManagersDTO.add( modelToDto(manager));
        }
        return allManagersDTO; 
    }
    
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        // TODO Auto-generated method stub
        List<EmployeeDTO> allEmployeesDTO=new ArrayList<EmployeeDTO>() ;
        List<Employee> allemployees=employeeDAO.getAllEmployees();
        for(Employee employee:allemployees){
            allEmployeesDTO.add( modelToDto(employee));
        }
        return allEmployeesDTO; 
    }
}
