package com.metacube.helpdesk.controller;

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
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Validation;

@CrossOrigin
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    EmployeeService employeeService;
    
    /**
     * @param authorisationToken : Token for authorisation of admin of a particular organisation.
     * @param username : username of admin of a particular organisation.
     * @return List of all the managers of the particular organisation.
     * ToDo : make return type as response entity;
     */
    @RequestMapping(value = "/getAllManagers", method = RequestMethod.GET)
    public @ResponseBody List<EmployeeDTO> getAllManagers(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
    	//To check if headers are null or not
    	if(!Validation.validateHeaders(authorisationToken, username)){
    		return null;
    	}
    	//To validate if username is in correct format or not
    	if(!Validation.validateInput(username, Constants.EMAILREGEX)){
    		return null;
    	}
    	//calls service method to get all managers corresponding to that organisation
        return employeeService.getAllManagers(authorisationToken, username);
    }

    /**
     * @param authorisationToken : Token for authorisation of user who is logged in.
     * @param username : username of admin  who is logged in.
     * @return List of all the employees of the particular organisation.
     * ToDo : make return type as response entity;
     */
    @RequestMapping(value = "/getAllEmployees", method = RequestMethod.GET)
    public @ResponseBody List<EmployeeDTO> getAllEmployees(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
    	//To check if headers are null or not
    	if(!Validation.validateHeaders(authorisationToken, username)){
    		return null;
    	}
    	//To validate if username is in correct format or not
    	if(!Validation.validateInput(username, Constants.EMAILREGEX)){
    		return null;
    	}
    	//calls service method to get all employees corresponding to that organisation
        return employeeService.getAllEmployees(authorisationToken, username);
    }
       
    /**
     * @param authorisationToken : Token for authorisation of user who is logged in.
     * @param username : username of admin  who is logged in.
     * @return 
     */
    @RequestMapping(value = "/addManager", method = RequestMethod.POST)
    public @ResponseBody Response addManager(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username, @RequestBody EmployeeDTO manager) {
    	
    	if(!Validation.validateHeaders(authorisationToken, username)){
    		return new Response(0, null, "One or more header is missing");
    	}
    	
    	if(!Validation.validateInput(username, Constants.EMAILREGEX)){
    		return new Response(0, null, "Please enter username in correct format");
    	}
    	if(Validation.isNull(manager)){
    		return new Response(0, null, "Please enter name of employee to be add as manger");
    	}
        return employeeService.addManager(authorisationToken,username, manager.getLogin().getUsername());
    }
    
    @RequestMapping(value = "/getEmployee", method = RequestMethod.POST)
    public @ResponseBody EmployeeDTO getEmployee(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username, @RequestBody EmployeeDTO employeeUsername) {
        return employeeService.getEmployeeByUsername(authorisationToken,username, employeeUsername.getLogin().getUsername());
    }
       
    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public @ResponseBody Response deleteEmployee(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username, @RequestBody EmployeeDTO employeeToBeDeleted) {
        return employeeService.deleteEmployee(authorisationToken,username, employeeToBeDeleted.getLogin().getUsername());
    }
    
    @RequestMapping(value = "/updateEmployee", method = RequestMethod.POST)
    public @ResponseBody Response updateEmployee(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username, @RequestBody EmployeeDTO employeeToBeUpdated) {
        return employeeService.updateEmployee(authorisationToken,username, employeeToBeUpdated);
    }

    @ExceptionHandler({ org.springframework.http.converter.HttpMessageNotReadableException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response resolveException() {
        return new Response(3, null,
                MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
    }
    
   
}
