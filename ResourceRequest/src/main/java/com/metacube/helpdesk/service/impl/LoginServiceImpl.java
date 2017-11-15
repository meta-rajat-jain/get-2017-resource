package com.metacube.helpdesk.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
import com.metacube.helpdesk.utility.Validation;

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
    
    
    /* all validation have been done here*/
	@Override
	public LoginResponse loginAuthentication(String loginId, String password) {
		int status = 0;
		String authorisationToken = null;
		String message = null;
		Designation employeeType = Designation.InvalidAccount;

		if (Validation.isNull(loginId) || Validation.isNull(password)
				|| Validation.isEmpty(loginId) || Validation.isEmpty(password)) {
			return new LoginResponse(new Response(status, authorisationToken,
					"Please specify username or password"), employeeType);
		}

		if (!Validation.validateInput(loginId, Constants.EMAILREGEX)) {
			return new LoginResponse(new Response(status, authorisationToken,
					"Incorrect format of email"), employeeType);
		}
		LoginDTO loginDTO = modelToDto(loginDAO.get(loginId));
		if (loginDTO != null) {
			try {
				if (!loginDTO.isEnabled()) {
					return new LoginResponse(new Response(status,
							authorisationToken,
							"Please first Verify your account"), employeeType);
				}

				if (loginDTO.getUsername().equals(loginId)
						&& loginDTO.getPassword().equals(
								(SimpleMD5.hashingWithConstantSalt(password)))) {
					String uniqueID = null;

					uniqueID = loginId
							+ SimpleMD5.hashingWithConstantSalt(password)
							+ new Date().toString();

					authorisationToken = SimpleMD5.hashing(uniqueID);

					if (authorisationToken != null) {
						loginDAO.update(loginId, authorisationToken);
						status = 1;
						message = MessageConstants.LOGIN_SUCCESSFUL;
						Employee emp = employeeDAO.getEmployee(loginDAO
								.get(loginId));
					
						if (emp != null) {
							if(emp.getStatus() == "inactive") {
								status = 0;
								message = "This user acoount is currently inactive , contact your administrator for assistance";
							}
							employeeType = emp.getDesignation();
						} 
						//assuming one account for helpdesk which needs to contain helpdesk string in its username if its not an employee
						else if(loginId.toLowerCase().contains("helpdesk")) {
							employeeType = Designation.Helpdesk; 
						}
						//assuming one account for organisation admin which needs to contain admin string in its username 
						else if(loginId.toLowerCase().contains("admin")) {
							employeeType = Designation.Admin;
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
				// modification to handle exception to be done here
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
    
    
    /**/
    @Override
    public Response checkPassword(String authorisationToken,String username,String currentPassword) throws NoSuchAlgorithmException, NoSuchProviderException{
    	LogIn login=loginDAO.get(username);
    	if(login.getPassword().equals(SimpleMD5.hashingWithConstantSalt(currentPassword))){
    		 return new Response(1, authorisationToken,"Password is valid");
    	}else{
    		 return new Response(0, authorisationToken,"Previous password is not correct");
    	}
    }
    
    //validations done
    @Override
	public Response changePassword(String authorisationToken, String username,String usernameForPasswordChange,
			String prevPassword,String newPassword)  {
    	LogIn login=loginDAO.get(usernameForPasswordChange);
    	if(login==null) {
    		return new Response(0,authorisationToken,"No such username exists");
    	}
    	if(!login.getEnabled()) {
    		return new Response(0,authorisationToken,"This account is not verified : Verify first to change password"); 
    	}
    	try {
			if(login.getPassword().equals(SimpleMD5.hashingWithConstantSalt(prevPassword))){
				login.setPassword(SimpleMD5.hashingWithConstantSalt(newPassword));
				loginDAO.update(login);
				return new Response(1, authorisationToken,"Password has been updated successfully");
			}else{
				 return new Response(0, authorisationToken,"Previous password is not correct");
			}
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
			return new Response(0, null,MessageConstants.INTERNAL_SERVER_ERROR);	
		}
	}
    
    //validation done
	@Override
	public Response forgotPassword(String usernameForForgotPassword) {
		LogIn login=loginDAO.get(usernameForForgotPassword);
    	if(login==null) {
    		return new Response(0,null,"No such username exists");
    	}
    	if(!login.getEnabled()) {
    		return new Response(0,null,"This account is not verified : Verify first to make any request"); 
    	}
		
		String randomPassword=  Double.toString(Math.random()*100);
		
         mailSend.sendMail(Constants.DEFAULT_MAIL_SENDER, usernameForForgotPassword , Constants.FORGOT_PASSWORD,
                Constants.FORGOT_PASSWORD_MESSAGE + randomPassword);
         try {
			login.setPassword(SimpleMD5.hashingWithConstantSalt(randomPassword));
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
			return new Response(0, null,MessageConstants.INTERNAL_SERVER_ERROR);
		}
     	if(loginDAO.update(login).equals(Status.Success)){
     		return new Response(1, "","Password has been updated successfully");
     	} else {
     		return new Response(0, "","Password Updation failed : Try again!");
     	}
	}

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
                if(emp!=null) {
                    employeeType = emp.getDesignation();
                }else if(username.contains("admin")) {
                	employeeType = Designation.Admin;
                }else if (username.contains("ithelpdesk")) {
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
        return new LoginResponse(new Response(status, authorisationToken, message),employeeType);
    }
    
    /**
     * method to check logged in user is authorize or not
     */
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
        if(authorizationToken.equals(loggedUser.getAuthorisationToken())&& loggedUser.getEnabled() ){
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
    public Response enableLogIn(String username,String hashUsername) { 
       LogIn loginObj = loginDAO.get(username);
       try {
		if(hashUsername.equals(SimpleMD5.hashingWithConstantSalt(username))){
			   loginObj.setEnabled(true);
			   loginDAO.update(loginObj);
			   return new Response(1,null,"Account Successfully verified");
			   
		   } 
	} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
		e.printStackTrace();
	}
       return new Response(0,null,"verification url is not correct"); 
    }
    
    @Override
    public Designation getAccountType(String username) {
    	Designation accountType = Designation.InvalidAccount;
    	Employee emp = employeeDAO.getEmployee(loginDAO.get(username));
        if (emp != null) {
            accountType = emp.getDesignation();
        } else if (organisationDAO.getByLogin(loginDAO.get(username)) != null) {
            accountType = Designation.Admin;
        } else if(username.contains("ithelpdesk")){
        	accountType = Designation.Helpdesk;
        }
        return accountType;    	
    }
}
