package com.metacube.helpdesk.controller;
import com.metacube.helpdesk.utility.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.service.impl.LoginServiceImpl;

@Controller
@RequestMapping(value = "/auth")
public class LoginController {
    @Autowired
    LoginServiceImpl loginServiceImpl;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Response greeting
            (@RequestBody LoginDTO loginDTO) {
        System.out.println("in controller");
        Response obj = loginServiceImpl.loginAuthentication(loginDTO.getUsername(), loginDTO.getPassword());
        return obj;
    }
    
    @RequestMapping(value="/hello", method = RequestMethod.GET)
    public @ResponseBody String getUserBy() {
        return "vaishali";
    }
}
