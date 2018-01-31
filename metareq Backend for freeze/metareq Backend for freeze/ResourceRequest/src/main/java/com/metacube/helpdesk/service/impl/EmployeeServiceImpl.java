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

    /**
     * method to validate employee object
     * 
     * @param employeeDTO
     * @return
     */
    public Response validateEmployeeObject(EmployeeDTO employeeDTO) {
        // null and empty check
        if (Validation.isNull(employeeDTO.getName())
                || Validation.isNull(employeeDTO.getLogin())
                || Validation.isNull(employeeDTO.getLogin().getUsername())
                || Validation.isNull(employeeDTO.getLogin().getPassword())
                || Validation.isNull(employeeDTO.getContactNumber())
                || Validation.isNull(employeeDTO.getOrgDomain())
                || Validation.isEmpty(employeeDTO.getName())
                || Validation.isEmpty(employeeDTO.getContactNumber())
                || Validation.isEmpty(employeeDTO.getOrgDomain())) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        // email pattern check
        if (!Validation.validateInput(employeeDTO.getLogin().getUsername(),
                Constants.EMAILREGEX)) {
            return new Response(0, null, MessageConstants.INVALID_EMAIL_ADDRESS);
        }
        // domain pattern check
        if (!Validation.validateInput(employeeDTO.getOrgDomain(),
                Constants.DOMAIN_REGEX)) {
            return new Response(0, null, MessageConstants.INVALID_DOMAIN);
        }
        // name pattern check
        if (!Validation.validateInput(employeeDTO.getName(), Constants.NAME)) {
            return new Response(0, null, MessageConstants.INVALID_NAME);
        }
        // contact number pattern check
        if (!Validation.validateInput(employeeDTO.getContactNumber(),
                Constants.CONTACT_NUMBER_REGEX)) {
            return new Response(0, null,
                    MessageConstants.INVALID_CONTACT_NUMBER);
        }
        return null;
    }

    /**
     * @param : EmployeeDTO object This method will create employee end will
     *        create its login details
     * @return : Response object with message and status
     */
    // After self code review To Do :To save default value in database itself
    public Response create(EmployeeDTO employeeDTO) {
        Employee employee = null;
        // default value is set here
        // parameters will come through constant class
        employeeDTO.setStatus(Constants.EMPLOYEE_STATUS_ACTIVE);
        // parameters will come through enum
        // at time of sign up each employee designation will be Member
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
                return new Response(2, null, MessageConstants.DOMAIN_NOT_EXIST);
            }
            /*
             * method to check if employee with this username already exist or
             * not
             */
            if (loginDAO.get(employeeDTO.getLogin().getUsername()) != null) {
                return new Response(2, null,
                        MessageConstants.USERNAME_ALREADY_EXIST);
            }
            // initially employee is set as disabled and will change to enable
            // only
            // after clicking on sent verification mail
            employeeDTO.getLogin().setEnabled(false);
            // create login object
            LogIn logIn = loginService.createLogIn(employeeDTO.getLogin());
            // get org domain from employee username
            String orgDomain = getOrgDomainFromUsername(logIn.getUsername());
            // check if orgdomain from username and domain match or not
            if (!orgDomain.equals(employeeDTO.getOrgDomain())) {
                return new Response(2, null,
                        MessageConstants.INCONSISTENT_ORG_FOR_USER);
            }
            // if login object save succesfully or not
            if (loginDAO.create(logIn).equals(Status.Success)) {
                employee = dtoToModel(employeeDTO);
            } else {
                return new Response(0, null,
                        MessageConstants.ACCOUNT_NOT_CREATED);
            }
            // create employee entry i database
            if (employeeDAO.create(employee).equals(Status.Success)) {
                // send verification mail
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
                        MessageConstants.VERIFICATION_MAIL_SENT);
            } else {
                return new Response(0, null,
                        MessageConstants.ACCOUNT_NOT_CREATED);
            }
        }
    }

    /**
     * DTO to model conversion
     */
    @Override
    public Employee dtoToModel(EmployeeDTO employeeDTO) {
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

    /**
     * model to DTO conversion
     */
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
        employeeDTO.setLogin(loginService.modelToDto(employee.getUsername()));
        return employeeDTO;
    }

    /**
     * @param authorisationToken
     *            : Token for authorisation of admin of a particular
     *            organisation.
     * @param userName
     *            : Name of logged in user This method will return all the
     *            managers in the particular organisation which is logged in
     */
    /*
     * In case of any error we are returning a empty list to make consistency of
     * response type at front end. ToDo :Change later to the response type
     * object
     */
    @Override
    public List<EmployeeDTO> getAllManagers(String userName) {
        // List of all managers of type Employee
        List<EmployeeDTO> allManagersDTO = new ArrayList<EmployeeDTO>();
        if (!loginService.getAccountType(userName).equals(Designation.Admin)) {
            return allManagersDTO;
        }
        // To get organisation of the logged in user admin
        Organisation organisation = organisationService
                .getOrganisationFromUserName(userName);
        // If organisation is null or not found return empty list
        if (Validation.isNull(organisation)) {
            return allManagersDTO;
        }
        List<Employee> allManagers = employeeDAO.getAllManagers(organisation);
        // model to DTO conversion
        for (Employee manager : allManagers) {
            allManagersDTO.add(modelToDto(manager));
        }
        return allManagersDTO;
    }

    /**
     * @param authorisationToken
     *            : Token for authorisation of admin of a particular
     *            organisation.
     * @param userName
     *            : Name of logged in user This method will return all the
     *            employees in the particular organisation which is logged in
     */
    /*
     * In case of any error we are returning a empty list to make consistency of
     * response type at front end. ToDo :Change later to the response type
     * object
     */
    @Override
    public List<EmployeeDTO> getAllEmployees(String userName) {
        // List of all Employee type
        List<EmployeeDTO> allEmployeesDTO = new ArrayList<EmployeeDTO>();
        // to check that this service access is provided to admin and
        // manager only
        // if logged in user is not admin or manager return empty list
        if (!loginService.getAccountType(userName).equals(Designation.Admin)
                && !loginService.getAccountType(userName).equals(
                        Designation.Manager)) {
            return allEmployeesDTO;
        }
        /*
         * to get organisation from username
         */
        Organisation organisation = organisationService
                .getOrganisationFromUserName(userName);
        // If organisation is null or not found return empty list
        if (Validation.isNull(organisation)) {
            return allEmployeesDTO;
        }
        List<Employee> allemployees = employeeDAO.getAllEmployees(organisation);
        // model to DTO conversion
        for (Employee employee : allemployees) {
            allEmployeesDTO.add(modelToDto(employee));
        }
        return allEmployeesDTO;
    }

    /**
     * @param loggedInUsername
     *            : name of the logged in user
     * @param managerUsername
     *            : name of the employee to be upgraded as manager
     * @return Response This method will upgrade the designation of the employee
     *         as manager
     */
    @Override
    public Response addManager(String loggedInUsername, String managerUsername) {
        // to check that this service access is provided
        // if logged in user is not admin return response with status code 0 as
        // unauthorised user
        if (!loginService.getAccountType(loggedInUsername).equals(
                Designation.Admin)) {
            return new Response(0, null, MessageConstants.UNAUTHORISED_USER);
        }
        // To get employee corresponding to the name to be upgraded as manager
        LogIn managerLogInObject = loginDAO.get(managerUsername);
        if (managerLogInObject != null) {
            Employee employee = employeeDAO.getEmployee(managerLogInObject);
            // if employee not found return username not exist
            if (employee == null) {
                return new Response(2, null,
                        MessageConstants.USERNAME_NOT_EXIST);
            }
            // set employee designation as Manager
            employee.setDesignation(Designation.Manager);
            if (employeeDAO.updateEmployee(employee).equals(Status.Success)) {
                // Create default team corresponding to that employee
                Team team = teamService.createTeam(managerUsername);
                // add himself to particular team
                teamService.addEmployeeToTeam(employee, team);
                return new Response(1, null,
                        MessageConstants.MANAGER_ADDED_SUCCESSFULLY);
            }
        }
        return new Response(2, null, MessageConstants.USERNAME_NOT_EXIST);
    }

    /**
     * @param loggedInUsername
     *            : name of the logged in user
     * @param employeeToBeDeleted
     *            : name of the employee to be deleted
     * @return Response This method will delete the corresponding employee
     */
    @Override
    public Response deleteEmployee(String employeeToBeDeleted,
            String loggedInUser) {
        // to check that this service access is provided to admin only
        // if logged in user is not admin return response with status code 0 as
        // unauthorised user
        if (!loginService.getAccountType(loggedInUser)
                .equals(Designation.Admin)) {
            return new Response(0, null, MessageConstants.UNAUTHORISED_USER);
        }
        // get employee corresponding to that username
        LogIn employeeToBeDeletedObject = loginDAO.get(employeeToBeDeleted);
        if (employeeToBeDeletedObject != null) {
            Employee employee = employeeDAO
                    .getEmployee(employeeToBeDeletedObject);
            if (employee.getStatus().equals(Constants.EMPLOYEE_STATUS_INACTIVE)) {
                return new Response(0, null,
                        MessageConstants.USERNAME_NOT_EXIST);
            }
            // update the status of employee
            employee.setStatus(Constants.EMPLOYEE_STATUS_INACTIVE);
            if (employeeDAO.updateEmployee(employee).equals(Status.Success)) {
                return new Response(1, null,
                        MessageConstants.EMPLOYEE_DELETED_SUCCESSFULLY);
            } else {
                return new Response(0, null,
                        MessageConstants.ERROR_IN_DELETING_EMPLOYEE);
            }
        } else {
            return new Response(0, null, MessageConstants.USERNAME_NOT_EXIST);
        }
    }

    /**
     * @param loggedInUsername
     *            : name of the logged in user
     * @param employeeDtoToBeUpdated
     *            : name of the employee to be updated
     * @return Response This method will update the employee details
     */
    @Override
    public Response updateEmployee(EmployeeDTO employeeDtoToBeUpdated,
            String loggedInUser) {
        // get employee object
        Employee employee = employeeDAO.getEmployee(loginDAO
                .get(employeeDtoToBeUpdated.getLogin().getUsername()));
        // if null than that employee not exist return response username not
        // exist
        if (employee == null) {
            return new Response(0, null, MessageConstants.USERNAME_NOT_EXIST);
        }
        // To check logged in user or employee requesting for that service is
        // admin only
        if (!loginService.getAccountType(loggedInUser)
                .equals(Designation.Admin)) {
            return new Response(0, null, MessageConstants.UNAUTHORISED_USER);
        }
        // updation performed
        employee.setContactNumber(employeeDtoToBeUpdated.getContactNumber());
        employee.setEmployeeName(employeeDtoToBeUpdated.getName());
        if (employeeDAO.updateEmployee(employee).equals(Status.Success)) {
            return new Response(1, null,
                    MessageConstants.EMPLOYEE_UPDATED_SUCCESSFULLY);
        } else {
            return new Response(0, null, MessageConstants.USERNAME_NOT_EXIST);
        }
    }

    /**
     * @param loggedInUsername
     *            : name of the logged in user
     * @param managerUsername
     *            : name of the employee to get detail
     * @return Response : This method will return the complete detail of that
     *         user
     */
    /*
     * In case of any error we are returning a empty object to make consistency
     * of response type at front end. ToDo :Change later to the response type
     * object
     */
    @Override
    public EmployeeDTO getEmployeeByUsername(String loggedInUsername,
            String employeeUsername) {
        // to check that this service access is provided to admin and
        // manager only
        // if logged in user is not admin or manager return empty list
        if (!loginService.getAccountType(loggedInUsername).equals(
                Designation.Admin)
                && !loginService.getAccountType(loggedInUsername).equals(
                        Designation.Manager)) {
            return new EmployeeDTO();
        }
        // model to DTO conversion
        return modelToDto(employeeDAO.getEmployee(loginDAO
                .get(employeeUsername)));
    }

    @Override
    public Set<Team> getEmployeeTeams(String username, String employeeUsername) {
        Employee employee = employeeDAO.getEmployee(loginDAO
                .get(employeeUsername));
        if (employee != null) {
            return employee.getTeams();
        }
        return new HashSet<Team>();
    }

    @Override
    public EmployeeDTO getEmployeeDetails(String username) {
        return modelToDto(employeeDAO.getEmployee(loginDAO.get(username)));
    }
}
