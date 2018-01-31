package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.Designation;
import com.metacube.helpdesk.utility.LoginResponse;
import com.metacube.helpdesk.utility.MailSend;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.SimpleMD5;
import com.metacube.helpdesk.utility.Status;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    LoginDAO loginDAO;
    @Resource
    EmployeeDAO employeeDAO;
    @Resource
    OrganisationDAO organisationDAO;
    @Autowired
    MailSend mailSend;

    /**
     * method to log in the user by checking its authentication its credentials
     * are valid or not
     */
    @Override
    public LoginResponse loginAuthentication(String loginId, String password) {
        int status = 0;
        String authorisationToken = null;
        String message = null;
        Designation employeeType = Designation.InvalidAccount;
        LoginDTO loginDTO = modelToDto(loginDAO.get(loginId));
        if (loginDTO != null) {
            try {
                // if user has not verified its account
                if (!loginDTO.isEnabled()) {
                    return new LoginResponse(new Response(status,
                            authorisationToken,
                            MessageConstants.YOUR_EMAIL_IS_NOT_VARIFIED),
                            employeeType);
                }
                // if password is incorrect
                if (loginDTO.getUsername().equals(loginId)
                        && loginDTO.getPassword().equals(
                                (SimpleMD5.hashingWithConstantSalt(password)))) {
                    String uniqueID = null;
                    uniqueID = loginId
                            + SimpleMD5.hashingWithConstantSalt(password)
                            + new Date().toString();
                    authorisationToken = SimpleMD5.hashing(uniqueID);
                    if (authorisationToken != null) {
                        LogIn login = loginDAO.get(loginId);
                        login.setAuthorisationToken(authorisationToken);
                        loginDAO.update(login);
                        status = 1;
                        message = MessageConstants.LOGIN_SUCCESSFUL;
                        // if employee is inactive
                        Employee emp = employeeDAO.getEmployee(loginDAO
                                .get(loginId));
                        if (emp != null) {
                            if (emp.getStatus().equals(
                                    Constants.EMPLOYEE_STATUS_INACTIVE)) {
                                status = 0;
                                message = "This user acoount is currently inactive , contact your administrator for assistance";
                            }
                        }
                        employeeType = getAccountType(loginId);
                    }
                } else {
                    status = 0;
                    authorisationToken = null;
                    message = MessageConstants.CURRENT_PASSWORD_IS_NOT_CORRECT;
                }
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
                new LoginResponse(new Response(0, null,
                        MessageConstants.INTERNAL_SERVER_ERROR), employeeType);
            }
        } else {
            status = 0;
            authorisationToken = null;
            message = MessageConstants.INVALID_USERNAME;
        }
        return new LoginResponse(new Response(status, authorisationToken,
                message), employeeType);
    }

    /**
     * method creating Login object and setting password as its hash
     */
    @Override
    public LogIn createLogIn(LoginDTO loginDto) {
        LogIn logIn = new LogIn();
        logIn.setUsername(loginDto.getUsername());
        // MD5 Hashing
        try {
            logIn.setPassword(SimpleMD5.hashingWithConstantSalt(loginDto
                    .getPassword()));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        logIn.setAuthorisationToken(null);
        logIn.setEnabled(loginDto.isEnabled());
        return logIn;
    }

    // dto to model conversion
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

    // model to dto conversion
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

    /**
     * method to check while changing password previous password is correct or
     * not
     */
    @Override
    public Response checkPassword(String authorisationToken, String username,
            String currentPassword) throws NoSuchAlgorithmException,
            NoSuchProviderException {
        LogIn login = loginDAO.get(username);
        // perform checking of password by hashing the current password enrtered
        if (login.getPassword().equals(
                SimpleMD5.hashingWithConstantSalt(currentPassword))) {
            return new Response(1, authorisationToken, "Password is valid");
        } else {
            return new Response(0, authorisationToken,
                    "Previous password is not correct");
        }
    }

    /**
     * method to change password in case user wants to change password
     */
    @Override
    public Response changePassword(String authorisationToken, String username,
            String usernameForPasswordChange, String prevPassword,
            String newPassword) {
        LogIn login = loginDAO.get(usernameForPasswordChange);
        if (login == null) {
            return new Response(0, authorisationToken,
                    "No such username exists");
        }
        if (!login.getEnabled()) {
            return new Response(0, authorisationToken,
                    MessageConstants.YOUR_EMAIL_IS_NOT_VARIFIED);
        }
        try {
            if (login.getPassword().equals(
                    SimpleMD5.hashingWithConstantSalt(prevPassword))) {
                login.setPassword(SimpleMD5
                        .hashingWithConstantSalt(newPassword));
                loginDAO.update(login);
                return new Response(1, authorisationToken,
                        "Password has been updated successfully");
            } else {
                return new Response(0, authorisationToken,
                        "Previous password is not correct");
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            return new Response(0, null, MessageConstants.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * method performing forgot password functionality It will send random
     * password to the specified mail and store that hash in the database
     */
    @Override
    public Response forgotPassword(String usernameForForgotPassword) {
        LogIn login = loginDAO.get(usernameForForgotPassword);
        if (login == null) {
            return new Response(0, null, MessageConstants.USERNAME_NOT_EXIST);
        }
        // logged in user is enabled or not
        if (!login.getEnabled()) {
            return new Response(0, null,
                    MessageConstants.YOUR_EMAIL_IS_NOT_VARIFIED);
        }
        // generate random password
        String randomPassword = Double.toString(Math.random() * 100);
        // send this password in mail
        mailSend.sendMail(Constants.DEFAULT_MAIL_SENDER,
                usernameForForgotPassword, Constants.FORGOT_PASSWORD,
                Constants.FORGOT_PASSWORD_MESSAGE + randomPassword);
        // set hash of that random password in database
        try {
            login.setPassword(SimpleMD5.hashingWithConstantSalt(randomPassword));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            return new Response(0, null, MessageConstants.INTERNAL_SERVER_ERROR);
        }
        if (loginDAO.update(login).equals(Status.Success)) {
            return new Response(1, "", "Password has been updated successfully");
        } else {
            return new Response(0, "", "Password Updation failed : Try again!");
        }
    }

    /**
     * method to verify google+ login
     */
    @Override
    public LoginResponse verifyExternalLogin(String username) {
        String authorisationToken = null;
        int status = 0;
        String message = null;
        Designation employeeType = null;
        LogIn loginObject = loginDAO.get(username);
        if (loginObject != null) {
            // perform hashing and allow login
            try {
                String uuid = loginObject.getUsername()
                        + SimpleMD5.hashingWithConstantSalt(loginObject
                                .getPassword()) + new Date().toString();
                authorisationToken = SimpleMD5.hashing(uuid);
                status = 1;
                message = MessageConstants.LOGIN_SUCCESSFUL;
                Employee emp = employeeDAO.getEmployee(loginDAO.get(username));
                // returning employee type
                if (emp != null) {
                    employeeType = emp.getDesignation();
                } else if (username.contains("admin")) {
                    employeeType = Designation.Admin;
                } else if (username.contains("ithelpdesk")) {
                    employeeType = Designation.Helpdesk;
                }
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
        } else {
            status = 0;
            authorisationToken = null;
            message = MessageConstants.UNAUTHORISED_USER;
        }
        return new LoginResponse(new Response(status, authorisationToken,
                message), employeeType);
    }

    /**
     * method to check logged in user is authenticated or not
     */
    @Override
    public Boolean authenticateRequest(String authorizationToken,
            String username) {
        Boolean flag = false;
        LogIn loggedUser = loginDAO.get(username);
        /*
         * if there is no user in database with that username
         */
        if (loggedUser == null) {
            return flag;
        }
        // to check if user is authorized user or not
        if (authorizationToken.equals(loggedUser.getAuthorisationToken())
                && loggedUser.getEnabled()) {
            flag = true;
        }
        return flag;
    }

    // performing logout functionality by destroying authorisation token
    @Override
    public Response logOut(String username) {
        return loginDAO.destroyAuthorisationToken(username);
    }

    /**
     * method to enable user when the verification url is hit by the valid user
     */
    @Override
    public String enableLogIn(String username, String hashUsername) {
        LogIn loginObj = loginDAO.get(username);
        // checking url is correct or not
        try {
            if (hashUsername
                    .equals(SimpleMD5.hashingWithConstantSalt(username))) {
                loginObj.setEnabled(true);
                loginDAO.update(loginObj);
                return "Congratulations! Your account has been verified successfully";
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "verification url is not correct";
    }

    /**
     * method to get login object on the basis of the username
     */
    @Override
    public LogIn getLogin(String username) {
        return loginDAO.get(username);
    }

    /**
     * method to return designation of the logged in employee
     */
    @Override
    public Designation getAccountType(String username) {
        Designation accountType = Designation.InvalidAccount;
        Employee emp = employeeDAO.getEmployee(loginDAO.get(username));
        if (emp != null) {
            accountType = emp.getDesignation();
        } else if (organisationDAO.getByLogin(loginDAO.get(username)) != null) {
            accountType = Designation.Admin;
        } else if (username.toLowerCase().contains("helpdesk")) {
            accountType = Designation.Helpdesk;
        }
        return accountType;
    }
}