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

    @RequestMapping(value = "/getTeamsByEmployee", method = RequestMethod.POST)
    public @ResponseBody Set<Team> getEmployeeHead(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeDto) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new HashSet<Team>();
        }
        return employeeService.getEmployeeTeams(authorisationToken, username,
                employeeDto.getLogin().getUsername());
    }

    @RequestMapping(value = "/getEmployeesUnderHead", method = RequestMethod.GET)
    public @ResponseBody Set<EmployeeDTO> getEmployeeHead(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new HashSet<EmployeeDTO>();
        }
        return teamService.getAllEmployeesUnderHead(username,
                authorisationToken);
    }

    @RequestMapping(value = "/getEmployeeDetails", method = RequestMethod.GET)
    public @ResponseBody EmployeeDTO getEmployeeDetails(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new EmployeeDTO();
        }
        return employeeService.getEmployeeDetails(authorisationToken, username);
    }
}
