package com.metacube.helpdesk.controller;
import javax.annotation.Resource;

import com.metacube.helpdesk.utility.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;

@Controller
@RequestMapping(value = "/auth")
public class LoginController {
    @Resource
    LoginService loginService;
    
    @Resource
    EmployeeService employeeService;
    
    @Resource
    OrganisationService organisationService;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Response greeting
            (@RequestBody LoginDTO loginDTO) {
        System.out.println("in controller");
        Response obj = loginService.loginAuthentication(loginDTO.getUsername(), loginDTO.getPassword());
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
    
    
}
