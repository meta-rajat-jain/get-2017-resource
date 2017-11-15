package com.metacube.helpdesk.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.utility.Designation;
import com.metacube.helpdesk.utility.LoginResponse;
import com.metacube.helpdesk.utility.Response;

public interface LoginService {

    LoginResponse loginAuthentication(String loginId, String password);
    LoginDTO modelToDto(LogIn login);
    LogIn dtoToModel(LoginDTO loginDetails);
    LoginResponse verifyExternalLogin(String userName);
    Boolean authorizeRequest(String authorizationToken,String userName);
    Response logOut(String authorisationToken, String username);
   
	Response checkPassword(String authorisationToken, String username,
			String currentPassword) throws NoSuchAlgorithmException,
			NoSuchProviderException;
	
	Response changePassword(String authorisationToken, String username,
			String usernameForPasswordChange, String prevPassword,
			String newPassword);
	Designation getAccountType(String username);
	Response forgotPassword(String usernameForForgotPassword);
	Response enableLogIn(String username, String hashUsername);
    
}
