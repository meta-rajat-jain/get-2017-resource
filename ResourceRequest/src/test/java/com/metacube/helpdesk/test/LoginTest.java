package com.metacube.helpdesk.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
import com.metacube.helpdesk.utility.Designation;
import com.metacube.helpdesk.utility.LoginResponse;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.SimpleMD5;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {

    @Autowired
    LoginService loginService;
    @Autowired
    EmployeeService employeeService;
    static final String EMPLOYEE_NAME = "Vaishali Jain";
    static final String EMPLOYEE_PASSWORD = "metacube";
    static final String INCORRECT_PASSWORD = "thisisincorrectpassword";
    static final String EMPLOYEE_USERNAME = "vaishali.jain@metacube.com";
    static final String EMPLOYEE_CONTACT_NUMBER = "9876543210";
    static final String EMPLOYEE_ORG_DOMAIN = "metacube.com";
    static final String EMPLOYEE_DESIGNATION = "Member";
    static final String EMPLOYEE_NAME2 = "Gaurav Tak";
    static final String EMPLOYEE_NONVERIFIED = "gaurav.tak@metacube.com";
    static final String EMPLOYEE_NONVERIFIED2 = "shivam.lalwani@metacube.com";
    static final String EMPLOYEE_INACTIVE = "anushtha.gupta@metacube.com";
    static final String HELPDESK = "ithelpdesk@metacube.com";
    @Autowired
    OrganisationService organisationService;
    static final String ADMIN_NAME = "Admin";
    static final String ADMIN_PASSWORD = "admin123";
    static final String ADMIN_USERNAME = "admin@metacube.com";
    static final String ADMIN_CONTACT_NUMBER = "987654321";
    static final String authorisationToken = "c06f126dd6d510afe0bfe4d0191dd38c";
    static final String authorisationTokenOrg = "c06f126dd6d510afe0bfe4d0191dd38c";

    @Test
    public void loginOrganisationTestSuccess() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(ADMIN_USERNAME);
        loginDTO.setPassword(ADMIN_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(1, loginResponse.getResponse().getStatusCode());
        assertEquals(Designation.Admin, loginResponse.getEmployeeType());
    }

    @Test
    public void loginOrganisationTestSuccessAurthorisationTokenCreation() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(ADMIN_USERNAME);
        loginDTO.setPassword(ADMIN_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(1, loginResponse.getResponse().getStatusCode());
        assertEquals(Designation.Admin, loginResponse.getEmployeeType());
        assertNotEquals(null, loginResponse.getResponse()
                .getAuthorisationToken());
    }

    @Test
    public void loginTestFailureWrongUsernameFormat() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("adminmeta");
        loginDTO.setPassword(ADMIN_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(Designation.InvalidAccount,
                loginResponse.getEmployeeType());
        assertEquals("Incorrect format of email", loginResponse.getResponse()
                .getMessage());
        ;
    }

    @Test
    public void loginTestFailureNullValueIsPassed() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(ADMIN_USERNAME);
        loginDTO.setPassword(null);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(Designation.InvalidAccount,
                loginResponse.getEmployeeType());
        assertEquals(MessageConstants.USERNAME_PASSWORD_EMPTY, loginResponse
                .getResponse().getMessage());
    }

    @Test
    public void loginEmployeeTestSuccess() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(EMPLOYEE_USERNAME);
        loginDTO.setPassword(EMPLOYEE_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(1, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.LOGIN_SUCCESSFUL, loginResponse
                .getResponse().getMessage());
        assertEquals(Designation.Member, loginResponse.getEmployeeType());
    }

    @Test
    public void loginEmployeeTestSuccessAurthorisationTokenCreation() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(EMPLOYEE_USERNAME);
        loginDTO.setPassword(EMPLOYEE_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(1, loginResponse.getResponse().getStatusCode());
        assertEquals(Designation.Member, loginResponse.getEmployeeType());
        assertNotEquals(null, loginResponse.getResponse()
                .getAuthorisationToken());
    }

    @Test
    public void loginEmployeeTestFailureUnverifiedAccount() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(EMPLOYEE_NONVERIFIED);
        loginDTO.setPassword(EMPLOYEE_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.YOUR_EMAIL_IS_NOT_VARIFIED, loginResponse
                .getResponse().getMessage());
    }

    @Test
    public void loginEmployeeTestFailureInactiveAccount() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(EMPLOYEE_INACTIVE);
        loginDTO.setPassword(EMPLOYEE_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.INACTIVE_ACCOUNT, loginResponse
                .getResponse().getMessage());
    }

    @Test
    public void loginEmployeeTestFailureIncorrectPassword() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(EMPLOYEE_USERNAME);
        loginDTO.setPassword(INCORRECT_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.CURRENT_PASSWORD_IS_NOT_CORRECT,
                loginResponse.getResponse().getMessage());
    }

    @Test
    public void externalLoginTestFailure() {
        LoginResponse loginResponse = loginService
                .verifyExternalLogin("grv6495@gmail.com");
        assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.UNAUTHORISED_USER, loginResponse
                .getResponse().getMessage());
        assertEquals(null, loginResponse.getResponse().getAuthorisationToken());
    }

    @Test
    public void externalloginEmployeeTestSuccess() {
        LoginResponse loginResponse = loginService
                .verifyExternalLogin(EMPLOYEE_USERNAME);
        assertEquals(1, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.LOGIN_SUCCESSFUL, loginResponse
                .getResponse().getMessage());
        assertEquals(Designation.Member, loginResponse.getEmployeeType());
        assertNotEquals(null, loginResponse.getResponse()
                .getAuthorisationToken());
    }

    @Test
    public void externalLoginOrganisationTestSuccess() {
        LoginResponse loginResponse = loginService
                .verifyExternalLogin(ADMIN_USERNAME);
        assertEquals(1, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.LOGIN_SUCCESSFUL, loginResponse
                .getResponse().getMessage());
        assertEquals(Designation.Admin, loginResponse.getEmployeeType());
        assertNotEquals(null, loginResponse.getResponse()
                .getAuthorisationToken());
    }

    @Test
    public void getAccountTypeTestOrganisationAdmin() {
        assertEquals(Designation.Admin,
                loginService.getAccountType(ADMIN_USERNAME));
    }

    @Test
    public void getAccountTypeTestEmployeeMember() {
        assertEquals(Designation.Member,
                loginService.getAccountType(EMPLOYEE_USERNAME));
    }

    @Test
    public void getAccountTypeTestHelpdesk() {
        assertEquals(Designation.Helpdesk,
                loginService.getAccountType(HELPDESK));
    }

    @Test
    public void getAccountTypeTestNonExistant() {
        assertEquals(Designation.InvalidAccount,
                loginService.getAccountType("grv6495@gmail.com"));
    }

    @Test
    public void forgotPasswordTestSuccess() {
        Response resp = loginService.forgotPassword(EMPLOYEE_USERNAME);
        assertEquals(1, resp.getStatusCode());
        assertEquals("Password has been updated successfully",
                resp.getMessage());
    }

    @Test
    public void forgotPasswordTestFailureUnverifiedUser() {
        Response resp = loginService.forgotPassword(EMPLOYEE_NONVERIFIED);
        assertEquals(0, resp.getStatusCode());
        assertEquals(MessageConstants.YOUR_EMAIL_IS_NOT_VARIFIED,
                resp.getMessage());
    }

    @Test
    public void forgotPasswordTestFailureNonExistantUsername() {
        Response resp = loginService.forgotPassword("grv6495@gmail.com");
        assertEquals(0, resp.getStatusCode());
        assertEquals("No such username exists", resp.getMessage());
    }

    @Test
    public void logoutSuccess() {
        Response resp = loginService.logOut(authorisationToken,
                EMPLOYEE_USERNAME);
        assertEquals(1, resp.getStatusCode());
        assertEquals(MessageConstants.SUCCESSFULLY_LOGGEDOUT, resp.getMessage());
    }

    @Test
    public void logoutFailure() {
        Response resp = loginService.logOut("hdbjhsbdh", EMPLOYEE_USERNAME);
        assertEquals(0, resp.getStatusCode());
        assertEquals(MessageConstants.UNAUTHORISED_USER, resp.getMessage());
    }

    @Test
    public void enableLoginSuccess() {
        try {
            assertEquals(
                    "Congratulations! Your account has been verified successfully",
                    loginService.enableLogIn(EMPLOYEE_NONVERIFIED2, SimpleMD5
                            .hashingWithConstantSalt(EMPLOYEE_NONVERIFIED2)));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.getMessage();
        }
    }

    @Test
    public void enableLoginFailure() {
        try {
            assertEquals(
                    "verification url is not correct",
                    loginService.enableLogIn(EMPLOYEE_NONVERIFIED2,
                            SimpleMD5.hashingWithConstantSalt("abcdefghi")));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
}
