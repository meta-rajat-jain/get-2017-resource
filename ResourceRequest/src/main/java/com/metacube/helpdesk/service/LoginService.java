package com.metacube.helpdesk.service;

import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.LoginResponse;
import com.metacube.helpdesk.utility.Response;

public interface LoginService {

    LoginResponse loginAuthentication(String loginId, String password);
    LoginDTO modelToDto(LogIn login);
    LogIn dtoToModel(LoginDTO loginDetails);
    LoginResponse verifyExternalLogin(String userName);
    Boolean authorizeRequest(String authorizationToken,String userName);
    
}
