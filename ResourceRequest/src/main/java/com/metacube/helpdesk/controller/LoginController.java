package com.metacube.helpdesk.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.dto.PasswordDTO;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
import com.metacube.helpdesk.utility.Constants;
import com.metacube.helpdesk.utility.Designation;
import com.metacube.helpdesk.utility.LoginResponse;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Validation;

@CrossOrigin
@Controller
@RequestMapping(value = "/auth")
public class LoginController {

    @Resource
    LoginService loginService;
    @Resource
    EmployeeService employeeService;
    @Resource
    OrganisationService organisationService;

    /**
     * performing login functionality (calls facade login method)
     * 
     * @param loginDTO
     *            defines the login credentials
     * @return the response of the request having statuscode ,authorisation
     *         token and message depending on if employee or organisation exist
     *         or not
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody LoginResponse logIn(@RequestBody LoginDTO loginDTO) {
        if (Validation.isNull(loginDTO)) {
            return new LoginResponse(new Response(0, null,
                    "Required Data Not Found with your Request"), null);
        }
        // Null check
        if (Validation.isNull(loginDTO.getUsername())
                || Validation.isNull(loginDTO.getPassword())
                || Validation.isEmpty(loginDTO.getUsername())
                || Validation.isEmpty(loginDTO.getPassword())) {
            return new LoginResponse(new Response(0, null,
                    "Please specify required fields"),
                    Designation.InvalidAccount);
        }
        // Enter mail id is in correct format or not
        if (!Validation.validateInput(loginDTO.getUsername(),
                Constants.EMAILREGEX)) {
            return new LoginResponse(new Response(0, null,
                    "Incorrect format of email"), Designation.InvalidAccount);
        }
        // calls service method
        return loginService.loginAuthentication(loginDTO.getUsername(),
                loginDTO.getPassword());
    }

    /**
     * method providing service to change password to the user
     * 
     * @param authorisationToken
     * @param username
     * @param passwordDTO
     * @return response object
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public @ResponseBody Response changePassword(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody PasswordDTO passwordDTO) {
        // null check
        if (Validation.isNull(passwordDTO)) {
            return new Response(0, null,
                    "Required Data Not Found with your Request");
        }
        // calls service change password functionality
        return loginService.changePassword(authorisationToken, username,
                passwordDTO.getUsername(), passwordDTO.getPrevPassword(),
                passwordDTO.getNewPassword());
    }

    /**
     * this method will give a random password to the user at his mail
     * 
     * @param loginDTO
     * @return
     */
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public @ResponseBody Response forgotPasword(@RequestBody LoginDTO loginDTO) {
        // null check
        if (Validation.isNull(loginDTO)) {
            return new Response(0, null,
                    "Required Data Not Found with your Request");
        }
        // calls service method
        return loginService.forgotPassword(loginDTO.getUsername());
    }

    /**
     * Method to check that user is authorize or not on the basis of employee
     * type
     * 
     * @param loginDTO
     * @return
     */
    @RequestMapping(value = "/accessVerification", method = RequestMethod.POST)
    public @ResponseBody LoginResponse accessVerification(
            @RequestBody LoginDTO loginDTO) {
        // null check
        if (Validation.isNull(loginDTO)) {
            return new LoginResponse(new Response(0, null,
                    "Required Data Not Found with your Request"), null);
        }
        return new LoginResponse(new Response(1,
                loginDTO.getAuthorisationToken(),
                "Find associated account type string in response"),
                loginService.getAccountType(loginDTO.getUsername()));
    }

    /**
     * performs logout functionality
     * 
     * @param authorisationToken
     * @param username
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public @ResponseBody Response logOut(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        // To check if headers are null or not
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new Response(0, authorisationToken,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        // authenticate the log in credentials
        if (loginService.authenticateRequest(authorisationToken, username)) {
            return loginService.logOut(username);
        }
        // if fails return message as logged in user
        return new Response(0, authorisationToken,
                MessageConstants.UNAUTHORISED_USER);
    }

    /**
     * method to create employee or sign up it
     * 
     * @param employee
     * @return
     */
    @RequestMapping(value = "/signup/employee", method = RequestMethod.POST)
    public @ResponseBody Response signUpEmp(@RequestBody EmployeeDTO employee) {
        // null check
        if (Validation.isNull(employee)) {
            return new Response(0, null,
                    "Required Data Not Found with your Request");
        }
        return employeeService.create(employee);
    }

    /**
     * method to sign up organisation
     * 
     * @param organisation
     * @return
     */
    @RequestMapping(value = "/signup/organisation", method = RequestMethod.POST)
    public @ResponseBody Response signUpOrg(
            @RequestBody OrganisationDTO organisation) {
        // null check
        if (Validation.isNull(organisation)) {
            return new Response(0, null,
                    "Required Data Not Found with your Request");
        }
        return organisationService.create(organisation);
    }

    // for requests of external login
    @RequestMapping(value = "/verifyUser", method = RequestMethod.GET)
    public @ResponseBody LoginResponse verifyUserName(
            @RequestParam("username") String username) {
        return loginService.verifyExternalLogin(username);
    }

    /**
     * to check that the verification link is verified by valid user or not and
     * set employee as enable on the basis of that
     * 
     * @param username
     * @param hashUsername
     * @return
     */
    @RequestMapping(value = "/verifySignUp/{hashed}", method = RequestMethod.GET)
    public @ResponseBody String verificationSignUp(
            @RequestParam("username") String username,
            @PathVariable("hashed") String hashUsername) {
        return loginService.enableLogIn(username, hashUsername);
    }

    /**
     * return list of all the organisation domains registered
     * 
     * @return
     */
    @RequestMapping(value = "/getOrganisationDomains", method = RequestMethod.GET)
    public @ResponseBody List<String> getAllOrgsDomains() {
        return organisationService.getAllOrganisationDomains();
    }
}
