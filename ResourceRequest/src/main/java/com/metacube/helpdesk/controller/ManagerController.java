package com.metacube.helpdesk.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.metacube.helpdesk.dto.EmpTeamDTO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Validation;

@CrossOrigin
@Controller
@RequestMapping(value = "/manager")
public class ManagerController {

    @Resource
    TeamService teamService;

    // gets all teams under a manager
    @RequestMapping(value = "/getAllTeams", method = RequestMethod.GET)
    public @ResponseBody List<TeamDTO> getAllTeams(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TeamDTO>();
        }
        return teamService
                .getAllTeamsUnderManager(authorisationToken, username);
    }

    /**
     * return all employees under that team
     * 
     * @param authorisationToken
     * @param username
     * @param team
     * @return
     */
    @RequestMapping(value = "/getEmployeesByTeamName", method = RequestMethod.POST)
    public @ResponseBody List<EmployeeDTO> getEmployeesByTeamName(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody TeamDTO team) {
        if (!Validation.validateHeaders(authorisationToken, username)
                && Validation.isNull(team)) {
            return new ArrayList<EmployeeDTO>();
        }
        return teamService.getEmployeesByTeamName(authorisationToken, username,
                team.getTeamName());
    }

    /**
     * return employees not in particular team
     * 
     * @param authorisationToken
     * @param username
     * @param team
     * @return
     */
    @RequestMapping(value = "/getEmployeesNotInParticularTeam", method = RequestMethod.POST)
    public @ResponseBody List<EmployeeDTO> getEmployeesNotInPaticularTeam(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody TeamDTO team) {
        if (!Validation.validateHeaders(authorisationToken, username)
                && Validation.isNull(team)) {
            return new ArrayList<EmployeeDTO>();
        }
        return teamService.getEmployeesNotInPaticularTeam(authorisationToken,
                username, team.getTeamName());
    }

    /**
     * return team in which employee is a member
     * 
     * @param authorisationToken
     * @param username
     * @param employeeDto
     * @return
     */
    @RequestMapping(value = "/getTeamsByEmployee", method = RequestMethod.POST)
    public @ResponseBody List<TeamDTO> getTeamsByEmployee(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody EmployeeDTO employeeDto) {
        if (!Validation.validateHeaders(authorisationToken, username)
                && Validation.isNull(employeeDto)) {
            return new ArrayList<TeamDTO>();
        }
        return teamService.getTeamsByEmployee(authorisationToken, username,
                employeeDto.getLogin().getUsername());
    }

    /**
     * return team in which logged in employee is a member
     * 
     * @param authorisationToken
     * @param username
     * @param employeeDto
     * @return
     */
    @RequestMapping(value = "/getTeamsForLoggedInUser", method = RequestMethod.GET)
    public @ResponseBody List<TeamDTO> getTeamsByEmployee(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TeamDTO>();
        }
        return teamService.getTeamsByEmployee(authorisationToken, username,
                username);
    }

    @RequestMapping(value = "/createTeam", method = RequestMethod.POST)
    public @ResponseBody Response createTeam(
            @RequestHeader(value = "username") String username,
            @RequestBody TeamDTO teamDTO) {
        return teamService.createTeam(username, teamDTO);
    }

    @RequestMapping(value = "/addEmployeeToTeam", method = RequestMethod.POST)
    public @ResponseBody Response addEmployeeToTeam(
            @RequestBody EmpTeamDTO empTeamDTo) {
        if (empTeamDTo == null) {
            return new Response(0, null,
                    "One or more required data is missing with request ");
        }
        return teamService.addEmployeeToTeam(empTeamDTo);
    }
}
