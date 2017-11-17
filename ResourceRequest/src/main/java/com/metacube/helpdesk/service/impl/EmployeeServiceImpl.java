package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dao.TeamDAO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.Designation;
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
    TeamService teamService;
    @Resource
    TeamDAO teamDAO;
    @Resource
    OrganisationService organisationService;
    @Autowired
    MailSend mailSend;

    public String getOrgDomainFromUsername(String username) {
        String[] splittedUserName = username.split("@");
        return splittedUserName[1];
    }

    public Response validateEmployeeObject(EmployeeDTO employeeDTO) {
        if (Validation.isNull(employeeDTO.getName())
                || Validation.isNull(employeeDTO.getLogin())
                || Validation.isNull(employeeDTO.getLogin().getUsername())
                || Validation.isNull(employeeDTO.getLogin().getPassword())
                || Validation.isNull(employeeDTO.getContactNumber())
                || Validation.isNull(employeeDTO.getOrgDomain())
                || Validation.isEmpty(employeeDTO.getName())
                || Validation.isEmpty(employeeDTO.getContactNumber())
                || Validation.isEmpty(employeeDTO.getOrgDomain())) {
            return new Response(0, null, "Please fill all required fields");
        }
        if (!Validation.validateInput(employeeDTO.getLogin().getUsername(),
                Constants.EMAILREGEX)) {
            return new Response(0, null, "Incorrect format of email");
        }
        if (!Validation.validateInput(employeeDTO.getContactNumber(),
                Constants.CONTACT_NUMBER_REGEX)) {
            return new Response(0, null, "Incorrect format of contact number");
        }
        return null;
    }

    public Response create(EmployeeDTO employeeDTO) {
        Employee employee = null;
        // default value is set here
        // parameters will come through constant class
        employeeDTO.setStatus(Constants.EMPLOYEE_STATUS_ACTIVE);
        // parameters will come through enum
        employeeDTO.setDesignation(Designation.Member);
        Response response = validateEmployeeObject(employeeDTO);
        if (response != null) {
            return response;
        } else {
            /*
             * method to check that the organisation to which the employee
             * belongs exist or not.
             */
            if (organisationDAO.getByDomain(employeeDTO.getOrgDomain()) == null) {
                return new Response(2, null, "Domain not exist");
            }
            /*
             * method to check if employee with this username already exist or
             * not
             */
            if (loginDAO.get(employeeDTO.getLogin().getUsername()) != null) {
                return new Response(2, null,
                        MessageConstants.USERNAME_ALREADY_EXIST);
            }
            employeeDTO.getLogin().setEnabled(false);
            LogIn logIn = loginService.createLogIn(employeeDTO.getLogin());
            String orgDomain = getOrgDomainFromUsername(logIn.getUsername());
            if (!orgDomain.equals(employeeDTO.getOrgDomain())) {
                return new Response(
                        2,
                        null,
                        "This username can't belong to the specified organisation - Format:yourlogin@orgdomain");
            }
            if (loginDAO.create(logIn).equals(Status.Success)) {
                employee = dtoToModel(employeeDTO);
            } else {
                return new Response(0, null,
                        MessageConstants.ACCOUNT_NOT_CREATED);
            }
            if (employeeDAO.create(employee).equals(Status.Success)) {
                try {
                    mailSend.sendMail(
                            Constants.DEFAULT_MAIL_SENDER,
                            employeeDTO.getLogin().getUsername(),
                            Constants.VERIFICATION_SUBJECT,
                            Constants.VERIFICATION_MESSAGE
                                    + SimpleMD5
                                            .hashingWithConstantSalt(employeeDTO
                                                    .getLogin().getUsername())
                                    + "/?username="
                                    + employeeDTO.getLogin().getUsername());
                } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return new Response(1, null,
                        "Verification mail has been sent to your account");
            } else {
                return new Response(0, null,
                        MessageConstants.ACCOUNT_NOT_CREATED);
            }
        }
    }

    @Override
    public Employee dtoToModel(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }
        Employee employee = new Employee();
        // employee.setEmployeeId(employeeDAO.getEmployee(loginService.dtoToModel(employeeDTO.getLogin())).getEmployeeId());
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

    @Override
    public EmployeeDTO modelToDto(Employee employee) {
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
        List<EmployeeDTO> allManagersDTO = new ArrayList<EmployeeDTO>();
        if (loginService.authenticateRequest(authorisationToken, userName)) {
            if (!loginService.getAccountType(userName)
                    .equals(Designation.Admin)) {
                return null;
            }
            Organisation organisation = organisationService
                    .getOrganisationFromUserName(userName);
            if (Validation.isNull(organisation)) {
                return null;
            }
            List<Employee> allManagers = employeeDAO
                    .getAllManagers(organisation);
            for (Employee manager : allManagers) {
                allManagersDTO.add(modelToDto(manager));
            }
            return allManagersDTO;
        }
        return allManagersDTO;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees(String authorisationToken,
            String userName) {
        List<EmployeeDTO> allEmployeesDTO = new ArrayList<EmployeeDTO>();
        if (loginService.authenticateRequest(authorisationToken, userName)) {
            if (!loginService.getAccountType(userName)
                    .equals(Designation.Admin)
                    && !loginService.getAccountType(userName).equals(
                            Designation.Manager)) {
                return null;
            }
            /*
             * to get organisation from username
             */
            Organisation organisation = organisationService
                    .getOrganisationFromUserName(userName);
            List<Employee> allemployees = employeeDAO
                    .getAllEmployees(organisation);
            for (Employee employee : allemployees) {
                allEmployeesDTO.add(modelToDto(employee));
            }
        }
        return allEmployeesDTO;
    }

    @Override
    public Response addManager(String authorisationTokenFromLogin,
            String username, String managerUsername) {
        if (Validation.isNull(managerUsername)) {
            return new Response(0, null,
                    "UnABLE TO FETCH MANAGER DATA from request");
        }
        if (!loginService.getAccountType(username).equals(Designation.Admin)) {
            return new Response(0, null, MessageConstants.UNAUTHORISED_USER);
        }
        LogIn managerLogInObject = loginDAO.get(managerUsername);
        if (managerLogInObject != null) {
            Employee employee = employeeDAO.getEmployee(managerLogInObject);
            if (employee == null) {
                return new Response(2, authorisationTokenFromLogin,
                        "User with this username does not exist");
            }
            employee.setDesignation(Designation.Manager);
            if (employeeDAO.updateEmployee(employee).equals(Status.Success)) {
                Team team = teamService.createTeam(managerUsername);
                teamService.addEmployeeToTeam(employee, team);
                return new Response(1, authorisationTokenFromLogin,
                        "Manager Added Successfully");
            }
        }
        return new Response(2, authorisationTokenFromLogin,
                "User with this username does not exist");
    }

    /**
     * 
     */
    @Override
    public Response deleteEmployee(String employeeToBeDeleted) {
        LogIn employeeToBeDeletedObject = loginDAO.get(employeeToBeDeleted);
        if (employeeToBeDeletedObject != null) {
            Employee employee = employeeDAO
                    .getEmployee(employeeToBeDeletedObject);
            employee.setStatus(Constants.EMPLOYEE_STATUS_INACTIVE);
            if (employeeDAO.updateEmployee(employee).equals(Status.Success)) {
                return new Response(1, null, "Employee Deleted Successfully");
            } else {
                return new Response(0, null, "Unable to delete employee");
            }
        } else {
            return new Response(0, null, "Employee to be  deleted not exist");
        }
    }

    @Override
    public Response updateEmployee(EmployeeDTO employeeToBeUpdated) {
        int employeeId = employeeDAO.getEmployee(
                loginDAO.get(employeeToBeUpdated.getLogin().getUsername()))
                .getEmployeeId();
        Employee e = dtoToModel(employeeToBeUpdated);
        e.setEmployeeId(employeeId);
        if (employeeDAO.updateEmployee(e).equals(Status.Success)) {
            return new Response(1, null,
                    "Employee Profile updated Successfully");
        } else {
            return new Response(0, null, "Employee to be  update not exist");
        }
    }

    @Override
    public Employee get(LogIn logIn) {
        return employeeDAO.getEmployee(logIn);
    }

    @Override
    public EmployeeDTO getEmployeeByUsername(
            String authorisationTokenFromLogin, String username,
            String employeeUsername) {
        if (!Validation.validateHeaders(authorisationTokenFromLogin, username)) {
            return null;
        }
        if (loginService.authenticateRequest(authorisationTokenFromLogin,
                username)) {
            return modelToDto(employeeDAO.getEmployee(loginDAO
                    .get(employeeUsername)));
        }
        return new EmployeeDTO();
    }

    // unused method
    @Override
    public Set<Team> getEmployeeTeams(String authorisationTokenFromLogin,
            String username, String employeeUsername) {
        if (loginService.authenticateRequest(authorisationTokenFromLogin,
                username)) {
            Employee employee = employeeDAO.getEmployee(loginDAO
                    .get(employeeUsername));
            if (employee != null) {
                return employee.getTeams();
            }
        }
        return new HashSet<Team>();
    }

    @Override
    public EmployeeDTO getEmployeeDetails(String authorisationTokenFromLogin,
            String username) {
        if (loginService.authenticateRequest(authorisationTokenFromLogin,
                username)) {
            return modelToDto(employeeDAO.getEmployee(loginDAO.get(username)));
        }
        return new EmployeeDTO();
    }
}
