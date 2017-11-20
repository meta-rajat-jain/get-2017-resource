package com.metacube.helpdesk.test;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminServiceTest {

    @Autowired
    EmployeeService employeeService;
    static final String ADMIN_NAME = "Admin";
    static final String ADMIN_PASSWORD = "admin123";
    static final String ADMIN_USERNAME = "admin@metacube.com";
    static final String ADMIN_CONTACT_NUMBER = "9876543210";
    static final String ORG_DOMAIN = "metacube.com";
    static final String ORG_HELPDESK = "ithelpdesk@metacube.com";
    static final String EMPLOYEE_TO_ADD_MANAGER = "shubham.sharma@metacube.com";

    @Test
    public void test20_getAllManagersWhenNoManager() {
        assertEquals(0, employeeService.getAllManagers(ADMIN_USERNAME).size());
    }

    @Test
    public void test21_getAllEmployee() {
        List<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();
        assertEquals(7, employeeService.getAllEmployees(ADMIN_USERNAME).size());
    }

    @Test
    public void test21_getAllEmployeeLoggedInUserIsUnauthorize() {
        assertEquals(0,
                employeeService.getAllEmployees("udit.saxena@metacube.com")
                        .size());
    }

    @Test
    public void test22_addManagerWhenEmployeeToBeAddedAsManagerNotExist() {
        Response response = employeeService.addManager(ADMIN_USERNAME,
                "rajat.jain@metacube.com");
        int statusCode = 2;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.USERNAME_NOT_EXIST, response.getMessage());
    }

    @Test
    public void test23_addManager() {
        Response response = employeeService.addManager(ADMIN_USERNAME,
                EMPLOYEE_TO_ADD_MANAGER);
        int statusCode = 1;
        // assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.MANAGER_ADDED_SUCCESSFULLY,
                response.getMessage());
    }

    @Test
    public void test24_getAllManagerWhenManagerIsThere() {
        assertEquals(1, employeeService.getAllManagers(ADMIN_USERNAME).size());
    }

    @Test
    public void test25_deleteEmployeeWhenEmployeeIsNull() {
        Response response = employeeService
                .deleteEmployee(null, ADMIN_USERNAME);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.USERNAME_NOT_EXIST, response.getMessage());
    }

    @Test
    public void test25_deleteEmployeeWhenEmployeeIsAlreadyInactive() {
        Response response = employeeService.deleteEmployee(
                "anushtha.gupta@metacube.com", ADMIN_USERNAME);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.USERNAME_NOT_EXIST, response.getMessage());
    }

    @Test
    public void test25_deleteEmployeeWhenLoggedInUserIsUnauthorize() {
        Response response = employeeService.deleteEmployee(
                "anushtha.gupta@metacube.com", "udit.saxena@metacube.com");
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.UNAUTHORISED_USER, response.getMessage());
    }

    @Test
    public void test25_deleteEmployee() {
        assertEquals(7, employeeService.getAllEmployees(ADMIN_USERNAME).size());
        Response response = employeeService.deleteEmployee(
                "shreya.bordia@metacube.com", ADMIN_USERNAME);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.EMPLOYEE_DELETED_SUCCESSFULLY,
                response.getMessage());
        assertEquals(6, employeeService.getAllEmployees(ADMIN_USERNAME).size());
    }

    @Test
    public void test26_updateEmployeeWhenLoggedInUserIsUnauthorize() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName("Naina");
        employeeDto.setContactNumber("9999999999");
        employeeDto.setOrgDomain("metacube.com");
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("vaishali.jain@metacube.com");
        loginDto.setPassword("metacube");
        employeeDto.setLogin(loginDto);
        Response response = employeeService.updateEmployee(employeeDto,
                "udit.saxena@metacube.com");
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.UNAUTHORISED_USER, response.getMessage());
    }

    @Test
    public void test26_updateEmployeeWhenEmployeeNotExist() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName("Naina");
        employeeDto.setContactNumber("9999999999");
        employeeDto.setOrgDomain("metacube.com");
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("vai.jain@metacube.com");
        loginDto.setPassword("metacube");
        employeeDto.setLogin(loginDto);
        Response response = employeeService.updateEmployee(employeeDto,
                ADMIN_USERNAME);
        int statusCode = 0;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.USERNAME_NOT_EXIST, response.getMessage());
    }

    @Test
    public void test26_updateEmployee() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        employeeDto.setName("Naina");
        employeeDto.setContactNumber("9999999999");
        employeeDto.setOrgDomain("metacube.com");
        LoginDTO loginDto = new LoginDTO();
        loginDto.setUsername("vaishali.jain@metacube.com");
        loginDto.setPassword("metacube");
        employeeDto.setLogin(loginDto);
        Response response = employeeService.updateEmployee(employeeDto,
                ADMIN_USERNAME);
        int statusCode = 1;
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(MessageConstants.EMPLOYEE_UPDATED_SUCCESSFULLY,
                response.getMessage());
    }
}
