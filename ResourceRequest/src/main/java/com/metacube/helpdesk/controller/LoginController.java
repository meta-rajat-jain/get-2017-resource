package com.metacube.helpdesk.controller;
import java.util.List;



import javax.annotation.Resource;

import com.metacube.helpdesk.utility.LoginResponse;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;






import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
@CrossOrigin
@Controller
@RequestMapping(value = "/auth")
public class LoginController {
    @Resource
    LoginService loginService;
    
    @Resource
    EmployeeService employeeService;
    
    @Resource
    OrganisationService organisationService;
    
    /**
     * performing login functionality (calls facade login method)
     * @param loginDTO defines the login credentials
     * @return the response of the request having statuscode ,authorisation
     *         token and message depending on if employee or organisation exist
     *         or not
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody LoginResponse logIn(
            @RequestBody LoginDTO loginDTO) {
        
            LoginResponse obj = loginService.loginAuthentication(
                    loginDTO.getUsername(), loginDTO.getPassword());
            return obj;

    }
    
    @RequestMapping(value = "/signup/employee", method = RequestMethod.POST)
    public @ResponseBody Response signUpEmp
            (@RequestBody EmployeeDTO employee) {
        Response obj = employeeService.create(employee);
        return obj;
    }
    
    @RequestMapping(value = "/signup/organisation", method = RequestMethod.POST)
    public @ResponseBody Response signUpOrg
            (@RequestBody OrganisationDTO organisation) {
        Response obj = organisationService.create(organisation);
        return obj;
    }
    
    @RequestMapping(value="/hello", method = RequestMethod.GET)
    public @ResponseBody String getUserBy() {
        return "vaishali";
    }
    
    //for requests of external login
    @RequestMapping(value="/verifyUser", method = RequestMethod.GET)
    public @ResponseBody Response verifyUserName(@RequestParam("userName") String userName) {
        return loginService.verifyExternalLogin(userName);
    }
    
    
    @RequestMapping(value="/getOrganisation", method = RequestMethod.GET)
    public @ResponseBody List<OrganisationDTO> getAllOrgs() {
        return organisationService.getAllOrganisation();
    }   
    
    @RequestMapping(value="/getOrganisationDomains", method = RequestMethod.GET)
    public @ResponseBody List<String> getAllOrgsDomains() {
        return organisationService.getAllOrganisationDomains();
    }
    
    @RequestMapping(value="/getAllManagers", method = RequestMethod.GET)
    public @ResponseBody List<EmployeeDTO> getAllManagers() {
        return employeeService.getAllManagers();
    }
    
    @RequestMapping(value="/getAllEmployees", method = RequestMethod.GET)
    public @ResponseBody List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    
    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response resolveException() {
        return new Response(3, null,MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
    }
}
