package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.SimpleMD5;


@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    LoginDAO loginDAO;
    
    

    @Override
    public Response loginAuthentication(String loginId,String password) {
        int status=0;
        String authorisationToken=null;
        String message;
        
        LoginDTO loginDTO = modelToDto(loginDAO.get(loginId));
        
        if (loginDTO!=null) {
            if(loginDTO.getUsername().equals(loginId) &&
                    loginDTO.getPassword().equals((password)))
            {
                String uniqueID = null;
                try {
                    uniqueID = loginId + SimpleMD5.hashingWithConstantSalt(password) + new Date().toString();
                } catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
                    e1.printStackTrace();
                }
                try {
                    authorisationToken = SimpleMD5.hashing(uniqueID);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }
                if(authorisationToken != null ) {
                    status = 1;
                    message = MessageConstants.LOGIN_SUCCESSFUL;
                } else {
                    status=0;
                    message = MessageConstants.AUTHENTICATION_ERROR;
                }
                
            } else {
                status = 0;
                authorisationToken = null;
                message = MessageConstants.CURRENT_PASSWORD_IS_NOT_CORRECT;
            }
        } else {
            status=0;
            authorisationToken=null;
            message = MessageConstants.INVALID_USERNAME;
        }
        System.out.println(loginDTO.getPassword()+"  "+loginDTO.getUsername());
        return new Response(status,authorisationToken,message);
    }
    
    @Override
    public LogIn dtoToModel(LoginDTO loginDto) {
        if (loginDto == null) {
            return null;
        }
        LogIn login = new LogIn();
        login.setUsername(loginDto.getUsername());
        login.setPassword(loginDto.getPassword());
        login.setAuthorisationToken(loginDto.getAuthorisationToken());

        return login;
    }

    @Override
    public LoginDTO modelToDto(LogIn login) {
        if (login == null) {
            return null;
        }
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(login.getUsername());
        loginDto.setPassword(login.getPassword());
        loginDto.setAuthorisationToken(login.getAuthorisationToken());

        return loginDto;
    }
    
    @Override
    public Response verifyExternalLogin(String userName) {
        String authorisationToken = null;
        int status=0;
        String message  = null;
        LogIn loginObject = loginDAO.get(userName);
        
        if(loginObject != null) {
            // perform hashing and allow login     
            try {
                String uuid = loginObject.getUsername()+
                        SimpleMD5.hashingWithConstantSalt(loginObject.getPassword())+new Date().toString();
                authorisationToken = SimpleMD5.hashing(uuid);
                status = 1;
                message = MessageConstants.LOGIN_SUCCESSFUL;
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
        } else {
            status = 0;
            authorisationToken = null;
            message = MessageConstants.UNAUTHORISED_USER;
        }
        return new Response(status,authorisationToken,message);
    }
}
