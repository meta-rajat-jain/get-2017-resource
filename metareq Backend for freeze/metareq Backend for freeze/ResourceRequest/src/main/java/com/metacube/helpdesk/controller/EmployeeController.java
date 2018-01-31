package com.metacube.helpdesk.controller;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.Validation;

@CrossOrigin
@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Resource
    EmployeeService employeeService;
    @Resource
    TeamService teamService;
    @Resource
    LoginService loginService;

    /**
     * This method return set of team in which employee is a member
     * 
     * @param authorisationToken
     * @param username
     * @param employeeDto
     * @return
     */
    @RequestMapping(value = "/getTeamsByEmployee", method = RequestMethod.POST)
    public @ResponseBody Set<Team> getEmployeeHead(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeDto) {
        // validate headers
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new HashSet<Team>();
        }
        // authenticate headers logged in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new HashSet<Team>();
        }
        // If required data is not specified
        if (Validation.isNull(employeeDto.getLogin().getUsername())
                || Validation.isEmpty(employeeDto.getLogin().getUsername())) {
            return new HashSet<Team>();
        }
        // calls service method
        return employeeService.getEmployeeTeams(username, employeeDto
                .getLogin().getUsername());
    }

    /**
     * @param authorisationToken
     * @param username
     * @return set of all employees under the particular head
     */
    @RequestMapping(value = "/getEmployeesUnderHead", method = RequestMethod.GET)
    public @ResponseBody Set<EmployeeDTO> getEmployeeHead(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        // validate headers
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new HashSet<EmployeeDTO>();
        }
        // authenticate headers logged in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new HashSet<EmployeeDTO>();
        }
        // calls service method
        return teamService.getAllEmployeesUnderHead(username);
    }

    /**
     * return complete detail of the logged in user
     * 
     * @param authorisationToken
     * @param username
     * @return
     */
    @RequestMapping(value = "/getEmployeeDetails", method = RequestMethod.GET)
    public @ResponseBody EmployeeDTO getEmployeeDetails(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        // validate headers
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new EmployeeDTO();
        }
        // authenticate headers logged in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new EmployeeDTO();
        }
        // calls service method
        return employeeService.getEmployeeDetails(username);
    }
}
