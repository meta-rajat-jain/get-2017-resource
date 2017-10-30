package com.metacube.helpdesk.service;

import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.Response;

public interface LoginService {

    Response loginAuthentication(String loginId, String password);
    LoginDTO modelToDto(LogIn login);
    LogIn dtoToModel(LoginDTO loginDetails);

}
