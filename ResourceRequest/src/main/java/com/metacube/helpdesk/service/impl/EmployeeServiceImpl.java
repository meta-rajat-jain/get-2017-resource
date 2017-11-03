package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.MailSend;
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

    @Resource
    OrganisationService organisationService;
    
    @Autowired
    MailSend mailSend;
    
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
        logIn.setEnabled(false);
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
            message = "Verification mail has been sent to your account";
            mailSend.sendMail(Constants.DEFAULT_MAIL_SENDER, employeeDTO
                    .getLogin().getUsername(), Constants.VERIFICATION_SUBJECT,
                    Constants.VERIFICATION_MESSAGE
                            + employeeDTO.getLogin().getUsername());

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
    public List<EmployeeDTO> getAllManagers(String authorisationToken,
            String userName) {
        List<EmployeeDTO>  allManagersDTO;
        
        if (loginService.authorizeRequest(authorisationToken, userName)) {
            /*
             * to get organisation from username
             */
            allManagersDTO = new ArrayList<EmployeeDTO>();
            Organisation organisation=organisationService.getOrganisationFromUserName(userName);
            List<Employee> allManagers = employeeDAO.getAllManagers(organisation);
            for (Employee manager : allManagers) {
                allManagersDTO.add(modelToDto(manager));
            }
            return allManagersDTO;
        }
        return null;
    }
    
    @Override
    public List<EmployeeDTO> getAllEmployees(String authorisationToken,
            String userName) {
        List<EmployeeDTO> allEmployeesDTO=new ArrayList<EmployeeDTO>() ;
        if (loginService.authorizeRequest(authorisationToken, userName)) {
            /*
             * to get organisation from username
             */
            Organisation organisation=organisationService.getOrganisationFromUserName(userName);
            List<Employee> allemployees=employeeDAO.getAllEmployees(organisation);
            for(Employee employee:allemployees){
                allEmployeesDTO.add( modelToDto(employee));
            }
            }
        
        return allEmployeesDTO; 
    }

    @Override
    public Response addManager(String authorisationTokenFromLogin, String username,String managerUsername) {
        if(!Validation.validateHeaders(authorisationTokenFromLogin, username)){
            return new Response(0,null,"One or more header is missing");
        }
        if (loginService.authorizeRequest(authorisationTokenFromLogin, username)) {
            LogIn managerLogInObject= loginDAO.get(managerUsername);
            if(managerLogInObject!=null){
                if(employeeDAO.addManager(authorisationTokenFromLogin,username,employeeDAO.getEmployee(managerLogInObject)).equals(Status.Success)){
                   return new Response(1,authorisationTokenFromLogin,"Manager Added Successfully"); 
                }
            }
          return new Response(2,authorisationTokenFromLogin,"User with this username does not exist");
        }
       return new Response(0,null,MessageConstants.UNAUTHORISED_USER);
    }
    
    /**
     * 
     */
    @Override
    public Response deleteEmployee(String authorisationTokenFromLogin, String username,
            String employeeToBeDeleted) {
        if(!Validation.validateHeaders(authorisationTokenFromLogin, username)){
            return new Response(0,null,"One or more header is missing");
        }
        if (loginService.authorizeRequest(authorisationTokenFromLogin, username)) {
            LogIn employeeToBeDeletedObject= loginDAO.get(employeeToBeDeleted);
            if(employeeToBeDeletedObject!=null){
                if(employeeDAO.deleteEmployee(employeeDAO.getEmployee(employeeToBeDeletedObject)).equals(Status.Success)){
                    return new Response(1,authorisationTokenFromLogin,"Employee Deleted Successfully"); 
                 } 
            }else{
                return new Response(0,authorisationTokenFromLogin,"Employee to be  deleted not exist");  
            }
        }
        return new Response(0,null,MessageConstants.UNAUTHORISED_USER);
    }

    @Override
    public EmployeeDTO getEmployee(String username) {        
        return modelToDto(employeeDAO.getEmployee(loginDAO.get(username)));
    }

    @Override
    public Response updateEmployee(String authorisationTokenFromLogin, String username,
            EmployeeDTO employeeToBeUpdated) {  
        if(!Validation.validateHeaders(authorisationTokenFromLogin, username)){
            return new Response(0,null,"One or more header is missing");
        }
        if (loginService.authorizeRequest(authorisationTokenFromLogin, username)) {
            
                if(employeeDAO.updateEmployee(dtoToModel(employeeToBeUpdated)).equals(Status.Success)){
                    return new Response(1,authorisationTokenFromLogin,"Employee Profile updated Successfully"); 
                 } 
            else{
                return new Response(0,authorisationTokenFromLogin,"Employee to be  update not exist");  
            }
        }
        return new Response(0,null,MessageConstants.UNAUTHORISED_USER);
    }

    @Override
    public Employee get(LogIn logIn) { 
        return employeeDAO.getEmployee(logIn);
    }

    @Override
    public EmployeeDTO getEmployeeByUsername(String authorisationTokenFromLogin,
            String username, String employeeUsername) {
        if(!Validation.validateHeaders(authorisationTokenFromLogin, username)){
            return null;
        }
        if (loginService.authorizeRequest(authorisationTokenFromLogin, username)) {
            return getEmployee( username);
        }
        return null;
    }
}
