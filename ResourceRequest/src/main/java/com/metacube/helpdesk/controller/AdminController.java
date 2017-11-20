package com.metacube.helpdesk.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Validation;

@CrossOrigin
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    EmployeeService employeeService;
    @Resource
    LoginService loginService;

    /**
     * @param authorisationToken
     *            : Token for authorisation of admin of a particular
     *            organisation.
     * @param username
     *            : username of admin of a particular organisation.
     * @return List of all the managers of the particular organisation.
     */
    // from self code review ToDo: make return type as response entity
    @RequestMapping(value = "/getAllManagers", method = RequestMethod.GET)
    public @ResponseBody List<EmployeeDTO> getAllManagers(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        // To check if headers are null or not
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<EmployeeDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<EmployeeDTO>();
        }
        // calls service method to get all managers corresponding to that
        // organisation
        return employeeService.getAllManagers(username);
    }

    /**
     * @param authorisationToken
     *            : Token for authorisation of user who is logged in.
     * @param username
     *            : username of admin who is logged in.
     * @return List of all the employees of the particular organisation.
     */
    // from self code review ToDo: make return type as response entity
    @RequestMapping(value = "/getAllEmployees", method = RequestMethod.GET)
    public @ResponseBody List<EmployeeDTO> getAllEmployees(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        // To check if headers are null or not
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<EmployeeDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<EmployeeDTO>();
        }
        // calls service method to get all employees corresponding to that
        // organisation
        return employeeService.getAllEmployees(username);
    }

    /**
     * @param username
     *            : username of admin who is logged in.
     * @return Response : contains status code 0 : Data not specified 1: Success
     *         2: If entity already exist contains authorisation token contains
     *         message : description of response
     */
    // headers authentication is performed through interceptor.
    @RequestMapping(value = "/addManager", method = RequestMethod.POST)
    public @ResponseBody Response addManager(
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO manager) {
        // To check if data is not specified
        if (Validation.isNull(manager)) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        // If required data is not specified
        if (Validation.isNull(manager.getLogin().getUsername())
                || Validation.isEmpty(manager.getLogin().getUsername())) {
            return new Response(0, null, MessageConstants.INVALID_USERNAME);
        }
        // calls service method to add manager in the organisation
        return employeeService.addManager(username, manager.getLogin()
                .getUsername());
    }

    /**
     * @param authorisationToken
     *            : Token for authorisation of user who is logged in.
     * @param username
     *            : username of admin who is logged in.
     * @return Employee details on the basis of email id.
     */
    // from self code review ToDo: make return type as response entity
    @RequestMapping(value = "/getEmployee", method = RequestMethod.POST)
    public @ResponseBody EmployeeDTO getEmployee(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeUsername) {
        // if data is not correctly specified return blank object
        if (employeeUsername == null) {
            return new EmployeeDTO();
        }
        // To check if headers are null or not
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new EmployeeDTO();
        }
        // to authenticate that logged in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new EmployeeDTO();
        }
        if (Validation.isNull(employeeUsername.getLogin().getUsername())
                || Validation
                        .isEmpty(employeeUsername.getLogin().getUsername())) {
            return new EmployeeDTO();
        }
        // calls service method to get details of particular employee
        return employeeService.getEmployeeByUsername(username, employeeUsername
                .getLogin().getUsername());
    }

    /**
     * @param username
     *            : username of admin who is logged in.
     * @return Response : contains status code 0 : Data not specified 1: Success
     *         2: If entity already exist contains authorisation token contains
     *         message : description of response
     */
    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public @ResponseBody Response deleteEmployee(
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeToBeDeleted) {
        if (Validation.isNull(employeeToBeDeleted)) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        if (Validation.isNull(employeeToBeDeleted.getLogin().getUsername())
                || Validation.isEmpty(employeeToBeDeleted.getLogin()
                        .getUsername())) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        // calls service delete method to delete employee
        return employeeService.deleteEmployee(employeeToBeDeleted.getLogin()
                .getUsername(), username);
    }

    @RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
    public @ResponseBody Response updateEmployee(
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeToBeUpdated) {
        if (Validation.isNull(employeeToBeUpdated)) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        if (Validation.isNull(employeeToBeUpdated.getLogin().getUsername())
                || Validation.isEmpty(employeeToBeUpdated.getLogin()
                        .getUsername())) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        // calls service update method to update employee
        return employeeService.updateEmployee(employeeToBeUpdated, username);
    }

    @ExceptionHandler({ org.springframework.http.converter.HttpMessageNotReadableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response resolveException() {
        return new Response(3, null,
                MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
    }
}
