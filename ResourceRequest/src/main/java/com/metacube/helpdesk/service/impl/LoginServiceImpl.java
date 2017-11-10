package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.utility.LoginResponse;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.SimpleMD5;
import com.metacube.helpdesk.utility.Validation;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    LoginDAO loginDAO;
    
    @Resource
    EmployeeDAO employeeDAO;
    
    @Resource
    OrganisationDAO organisationDAO;
    
    @Override
    public LoginResponse loginAuthentication(String loginId, String password) {
        int status = 0;
        String authorisationToken = null;
        String message = null;
        String employeeType = "Organisation Admin";

        if (Validation.isNull(loginId) || Validation.isNull(password)
                || Validation.isEmpty(loginId) || Validation.isEmpty(password)) {
            return new LoginResponse(new Response(status, authorisationToken,
                    "Please specify username or password"),employeeType);
        }

        if (!Validation.validateInput(loginId, Constants.EMAILREGEX)) {
            return new LoginResponse(new Response(status, authorisationToken,
                    "Incorrect format of email"),employeeType);
        }

        LoginDTO loginDTO = modelToDto(loginDAO.get(loginId));
        
        
        if (loginDTO != null) {
            if(!loginDTO.isEnabled()){
                return new LoginResponse(new Response(status, authorisationToken, "Please first Verify your account"),employeeType);
            }
            try {
                if (loginDTO.getUsername().equals(loginId)
                        && loginDTO.getPassword().equals(
                                (SimpleMD5.hashingWithConstantSalt(password)))) {
                    String uniqueID = null;
                    try {
                        uniqueID = loginId
                                + SimpleMD5.hashingWithConstantSalt(password)
                                + new Date().toString();
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
                    if (authorisationToken != null) {
                        loginDAO.update(loginId, authorisationToken);
                        status = 1;
                        message = MessageConstants.LOGIN_SUCCESSFUL;
                        Employee emp = employeeDAO.getEmployee(loginDAO.get(loginId)); 
                         if(emp!=null) {
                             employeeType = emp.getDesignation();
                         }
                    } else {
                        status = 0;
                        message = MessageConstants.AUTHENTICATION_ERROR;
                    }

                } else {
                    status = 0;
                    authorisationToken = null;
                    message = MessageConstants.CURRENT_PASSWORD_IS_NOT_CORRECT;
                }
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            status = 0;
            authorisationToken = null;
            message = MessageConstants.INVALID_USERNAME;
        }

        
        return new LoginResponse(new Response(status, authorisationToken, message),employeeType);
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
        login.setEnabled(loginDto.isEnabled());
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
        loginDto.setEnabled(login.getEnabled());
        return loginDto;
    }

    @Override
    public LoginResponse verifyExternalLogin(String userName) {
        String authorisationToken = null;
        int status = 0;
        String message = null;
        String employeeType ="Admin";
        LogIn loginObject = loginDAO.get(userName);

        if (loginObject != null) {
            // perform hashing and allow login
            try {
                String uuid = loginObject.getUsername()
                        + SimpleMD5.hashingWithConstantSalt(loginObject
                                .getPassword()) + new Date().toString();
                authorisationToken = SimpleMD5.hashing(uuid);
                status = 1;
                message = MessageConstants.LOGIN_SUCCESSFUL;
                Employee emp = employeeDAO.getEmployee(loginDAO.get(userName)); 
                if(emp!=null) {
                    employeeType = emp.getDesignation();
                }
                
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
        } else {
            status = 0;
            authorisationToken = null;
            message = MessageConstants.UNAUTHORISED_USER;
        }
        return new LoginResponse(new Response(status, authorisationToken, message),employeeType);
    }

    @Override
    public Boolean authorizeRequest(String authorizationToken, String username) {
        Boolean flag=false;
        // TODO Auto-generated method stub
        LogIn loggedUser=loginDAO.get(username);
        /*
         * if there is no user in database with that username
         */
        if(loggedUser==null){
            return flag;
        }
        //to check if user is authorized user or not 
        if(authorizationToken.equals(loggedUser.getAuthorisationToken())){
            flag=true;
        }
        return flag;
    }

    @Override
    public Response logOut(String authorisationToken, String username) {
        if(authorizeRequest(authorisationToken, username)){
            return loginDAO.destroyAuthorisationToken(authorisationToken,  username);
        }
        return new Response(0, authorisationToken,MessageConstants.UNAUTHORISED_USER);
    }
    
    @Override
    public LoginResponse getLoginType(LoginDTO loginDTO) {
        String accountType = "Invalid Account";
        Employee emp = employeeDAO.getEmployee(dtoToModel(loginDTO));
        if (emp != null) {
            accountType = emp.getDesignation();
        } else if (organisationDAO.getByLogin(dtoToModel(loginDTO)) != null) {
            accountType = "Admin";
        }
        return new LoginResponse(new Response(1,
                loginDTO.getAuthorisationToken(),
                "Find associated account type string in response"), accountType);
    }

    @Override
    public Response enableLogIn(String username) { 
       return loginDAO.update(username);
    }
}
