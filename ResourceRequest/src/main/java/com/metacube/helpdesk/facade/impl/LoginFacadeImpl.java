package com.metacube.helpdesk.facade.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.facade.LoginFacade;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.utility.Response;

@Component("loginFacade")
public class LoginFacadeImpl implements LoginFacade {
    
    @Resource
    LoginService loginService;

    @Override
    public Response loginAuthentication(String loginId, String password) {
        // TODO Auto-generated method stub
        return loginAuthentication(loginId,password);
    }

    @Override
    public Response verifyExternalLogin(String userName) {
        // TODO Auto-generated method stub
        return verifyExternalLogin(userName);
    }

   

   
}
