package com.metacube.helpdesk.facade;

import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.Response;

public interface LoginFacade {
    Response loginAuthentication(String loginId, String password);
   
    Response verifyExternalLogin(String userName);
}
