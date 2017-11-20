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

    @Test
    public void test11_loginOrganisationTestSuccess() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(ADMIN_USERNAME);
        loginDTO.setPassword(ADMIN_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        assertEquals(1, loginResponse.getResponse().getStatusCode());
        assertEquals(Designation.Admin, loginResponse.getEmployeeType());
    }

    @Test
    public void test11_loginOrganisationTestSuccessAurthorisationTokenCreation() {
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
    public void test12_loginEmployeeTestSuccess() {
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
    public void test12_loginEmployeeTestSuccessAurthorisationTokenCreation() {
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
    public void test13_loginEmployeeTestFailureUnverifiedAccount() {
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
    public void test13_loginEmployeeTestFailureInactiveAccount() {
        LoginDTO loginDTO = new LoginDTO();
        Response response = employeeService.deleteEmployee(EMPLOYEE_INACTIVE,
                ADMIN_USERNAME);
        /*
         * assertEquals(MessageConstants.EMPLOYEE_DELETED_SUCCESSFULLY,
         * response.getMessage());
         */
        loginDTO.setUsername(EMPLOYEE_INACTIVE);
        loginDTO.setPassword(EMPLOYEE_PASSWORD);
        LoginResponse loginResponse = loginService.loginAuthentication(
                loginDTO.getUsername(), loginDTO.getPassword());
        // assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.INACTIVE_ACCOUNT, loginResponse
                .getResponse().getMessage());
    }

    @Test
    public void test13_loginEmployeeTestFailureIncorrectPassword() {
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
    public void test14_externalLoginTestFailure() {
        LoginResponse loginResponse = loginService
                .verifyExternalLogin("grv6495@gmail.com");
        assertEquals(0, loginResponse.getResponse().getStatusCode());
        assertEquals(MessageConstants.UNAUTHORISED_USER, loginResponse
                .getResponse().getMessage());
        assertEquals(null, loginResponse.getResponse().getAuthorisationToken());
    }

    @Test
    public void test15_externalloginEmployeeTestSuccess() {
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
    public void test15_externalLoginOrganisationTestSuccess() {
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
    public void test15_getAccountTypeTestOrganisationAdmin() {
        assertEquals(Designation.Admin,
                loginService.getAccountType(ADMIN_USERNAME));
    }

    @Test
    public void test15_getAccountTypeTestEmployeeMember() {
        assertEquals(Designation.Member,
                loginService.getAccountType(EMPLOYEE_USERNAME));
    }

    @Test
    public void test15_getAccountTypeTestHelpdesk() {
        assertEquals(Designation.Helpdesk,
                loginService.getAccountType(HELPDESK));
    }

    @Test
    public void test15_getAccountTypeTestNonExistant() {
        assertEquals(Designation.InvalidAccount,
                loginService.getAccountType("grv6495@gmail.com"));
    }

    @Test
    public void test15_forgotPasswordTestSuccess() {
        Response resp = loginService.forgotPassword(EMPLOYEE_USERNAME);
        assertEquals(1, resp.getStatusCode());
        assertEquals("Password has been updated successfully",
                resp.getMessage());
    }

    @Test
    public void test15_forgotPasswordTestFailureUnverifiedUser() {
        Response resp = loginService.forgotPassword(EMPLOYEE_NONVERIFIED);
        assertEquals(0, resp.getStatusCode());
        assertEquals(MessageConstants.YOUR_EMAIL_IS_NOT_VARIFIED,
                resp.getMessage());
    }

    @Test
    public void test15_forgotPasswordTestFailureNonExistantUsername() {
        Response resp = loginService.forgotPassword("grv6495@gmail.com");
        assertEquals(0, resp.getStatusCode());
        assertEquals(MessageConstants.USERNAME_NOT_EXIST, resp.getMessage());
    }

    @Test
    public void test16_logoutSuccess() {
        Response resp = loginService.logOut(EMPLOYEE_USERNAME);
        assertEquals(1, resp.getStatusCode());
        assertEquals(MessageConstants.SUCCESSFULLY_LOGGEDOUT, resp.getMessage());
    }

    @Test
    public void test17_enableLoginSuccess() {
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
    public void test17_enableLoginFailure() {
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
