package com.metacube.helpdesk.controller;
import java.util.List;

import javax.annotation.Resource;

import com.metacube.helpdesk.utility.Response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    /*@RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> greeting(
            @RequestBody LoginDTO loginDTO) {
        System.out.println("loginDTO"
                );
        try{
            System.out.println(loginDTO
                    );
        if (loginDTO == null) {
            return new ResponseEntity<Response>(new Response(0, null,
                    "Empty data"), HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        } else {
            Response obj = loginService.loginAuthentication(
                    loginDTO.getUsername(), loginDTO.getPassword());
            return new ResponseEntity<Response>(obj, HttpStatus.OK);
        }
        }catch(HttpMessageNotReadableException hmmre  ){
            System.out.println("loginDTO in catch"
                    );
            return new ResponseEntity<Response>(new Response(0, null,
                    "Empty data"), HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }
    }*/
    /**
     * performing login functionality (calls facade login method)
     * @param loginDTO defines the login credentials
     * @return the response of the request having statuscode ,authorisation
     *         token and message depending on if employee or organisation exist
     *         or not
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Response> logIn(
            @RequestBody LoginDTO loginDTO) {
        if (loginDTO == null) {
            return new ResponseEntity<Response>(new Response(0, null,
                    "Request format incorrect"), HttpStatus.NOT_ACCEPTABLE);
        } else {
            Response obj = loginService.loginAuthentication(
                    loginDTO.getUsername(), loginDTO.getPassword());
            return new ResponseEntity<Response>(obj, HttpStatus.OK);
        }

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
}
