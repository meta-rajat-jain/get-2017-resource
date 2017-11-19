package com.metacube.helpdesk.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.dto.OrganisationDTO;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.OrganisationService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpServicesTest {

    @Autowired
    OrganisationService organisationService;
    static final String ADMIN_NAME = "Admin";
    static final String ADMIN_PASSWORD = "admin123";
    static final String ADMIN_USERNAME = "admin@metacube.com";
    static final String ADMIN_CONTACT_NUMBER = "9876543210";
    static final String ORG_DOMAIN = "metacube.com";
    static final String ORG_HELPDESK = "ithelpdesk@metacube.com";
    @Autowired
    LoginService loginService;
    @Autowired
    EmployeeService employeeService;
    static final String EMPLOYEE1_NAME = "Vaishali Jain";
    static final String EMPLOYEE_PASSWORD = "metacube";
    static final String EMPLOYEE1_USERNAME = "vaishali.jain@metacube.com";
    static final String EMPLOYEE_CONTACT_NUMBER = "9876543210";
    static final String EMPLOYEE_ORG_DOMAIN = "metacube.com";
    static final String EMPLOYEE_DESIGNATION = "Member";
    static final String EMPLOYEE2_NAME = "Shreya Bordia";
    static final String EMPLOYEE2_USERNAME = "shreya.bordia@metacube.com";
    static final String EMPLOYEE3_NAME = "Udit Saxena";
    static final String EMPLOYEE3_USERNAME = "udit.saxena@metacube.com";
    static final String EMPLOYEE4_NAME = "Shubham Sharma";
    static final String EMPLOYEE4_USERNAME = "shubham.sharma@metacube.com";
    static final String EMPLOYEE5_NAME = "Pawan Manglani";
    static final String EMPLOYEE5_USERNAME = "pawan.manglani@metacube.com";
    static final String EMPLOYEE6_NAME = "Shivam Lalwani";
    static final String EMPLOYEE6_USERNAME = "shivam.lalwani@metacube.com";

    @Test
    public void test01_CreateOrgInvalidCredentialName() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName("_admin");
        organisationDto.setContactNumber(ADMIN_CONTACT_NUMBER);
        organisationDto.setDomain(ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(ADMIN_USERNAME);
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INVALID_NAME, response.getMessage());
    }

    @Test
    public void test01_CreateOrgInvalidCredentialContactNumber() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName(ADMIN_NAME);
        organisationDto.setContactNumber("12345678");
        organisationDto.setDomain(ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(ADMIN_USERNAME);
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INVALID_CONTACT_NUMBER,
                response.getMessage());
    }

    @Test
    public void test01_CreateOrgInvalidCredentialDomain() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName(ADMIN_NAME);
        organisationDto.setContactNumber(ADMIN_CONTACT_NUMBER);
        organisationDto.setDomain("metacube");
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(ADMIN_USERNAME);
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INVALID_DOMAIN, response.getMessage());
    }

    @Test
    public void test01_CreateOrgInvalidCredentialUsername() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName(ADMIN_NAME);
        organisationDto.setContactNumber(ADMIN_CONTACT_NUMBER);
        organisationDto.setDomain(ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("adminmetacube.com");
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INVALID_EMAIL_ADDRESS,
                response.getMessage());
    }

    @Test
    public void test01_CreateOrgValidNullDomainCredential() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName(ADMIN_NAME);
        organisationDto.setContactNumber(ADMIN_CONTACT_NUMBER);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(ADMIN_USERNAME);
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.REQUIRED_DATA_NOT_SPECIFIED,
                response.getMessage());
    }

    @Test
    public void test02_CreateOrgValidCredential() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName(ADMIN_NAME);
        organisationDto.setContactNumber(ADMIN_CONTACT_NUMBER);
        organisationDto.setDomain(ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(ADMIN_USERNAME);
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.ACCOUNT_CREATED, response.getMessage());
        assertNotNull(loginService.getLogin(ORG_HELPDESK));
    }

    @Test
    public void test03_CreateOrgDomainAlreadyExist() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName("Org Admin");
        organisationDto.setContactNumber(ADMIN_CONTACT_NUMBER);
        organisationDto.setDomain(ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("oadmin@metacube.com");
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 2;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.DOMAIN_ALREADY_EXIST,
                response.getMessage());
    }

    @Test
    public void test03_CreateOrgUsernameAlreadyExist() {
        OrganisationDTO organisationDto = new OrganisationDTO();
        organisationDto.setName("Org Admin");
        organisationDto.setContactNumber(ADMIN_CONTACT_NUMBER);
        organisationDto.setDomain("mmetacube.com");
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("admin@metacube.com");
        loginDto.setPassword(ADMIN_PASSWORD);
        organisationDto.setLogin(loginDto);
        Response response = new Response();
        response = organisationService.create(organisationDto);
        int statusCode = 2;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.USERNAME_ALREADY_EXIST,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployeeInvalidCredentialName() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName("123Vaishali");
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE1_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INVALID_NAME, response.getMessage());
    }

    @Test
    public void test04_CreateEmployeeInvalidCredentialContactNumber() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE1_NAME);
        employeeDto.setContactNumber("546837498");
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE1_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INVALID_CONTACT_NUMBER,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployeeInvalidCredentialUsername() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE1_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("vaishalijainmetacube.com");
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INVALID_EMAIL_ADDRESS,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployeeValidCredentials() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE1_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE1_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.VERIFICATION_MAIL_SENT,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployee2ValidCredentials() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE2_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE2_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.VERIFICATION_MAIL_SENT,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployee3ValidCredentials() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE3_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE3_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.VERIFICATION_MAIL_SENT,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployee4ValidCredentials() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE4_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE4_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.VERIFICATION_MAIL_SENT,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployee5ValidCredentials() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE5_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE5_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.VERIFICATION_MAIL_SENT,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployee6ValidCredentials() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE6_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE6_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.VERIFICATION_MAIL_SENT,
                response.getMessage());
    }

    @Test
    public void test04_CreateAnotherEmployeeValidCredentials() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName("Anushtha Gupta");
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("anushtha.gupta@metacube.com");
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 1;
        // assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.VERIFICATION_MAIL_SENT,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployeeWhenDomainNotExist() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE1_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain("abc.com");
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE1_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 2;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.DOMAIN_NOT_EXIST, response.getMessage());
    }

    @Test
    public void test04_CreateEmployeeWhenUsernameAlreadyExist() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName(EMPLOYEE1_NAME);
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername(EMPLOYEE1_USERNAME);
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 2;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.USERNAME_ALREADY_EXIST,
                response.getMessage());
    }

    @Test
    public void test04_CreateEmployeeWhenInconsistentUsernameAndOrg() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName("Rajat Jain");
        employeeDto.setContactNumber(EMPLOYEE_CONTACT_NUMBER);
        employeeDto.setOrgDomain(EMPLOYEE_ORG_DOMAIN);
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("cserajatjain@gmail.com");
        loginDto.setPassword(EMPLOYEE_PASSWORD);
        employeeDto.setLogin(loginDto);
        Response response = new Response();
        response = employeeService.create(employeeDto);
        int statusCode = 2;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.INCONSISTENT_ORG_FOR_USER,
                response.getMessage());
    }
}
