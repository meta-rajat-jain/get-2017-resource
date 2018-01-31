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
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Validation;

@CrossOrigin
@Controller
@RequestMapping(value = "/manager")
public class ManagerController {

    @Resource
    TeamService teamService;
    @Resource
    LoginService loginService;

    // gets all teams under a manager
    @RequestMapping(value = "/getAllTeams", method = RequestMethod.GET)
    public @ResponseBody List<TeamDTO> getAllTeams(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        if (!Validation.validateHeaders(authorisationToken, username)) {
            return new ArrayList<TeamDTO>();
        }
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TeamDTO>();
        }
        return teamService.getAllTeamsUnderManager(username);
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
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<EmployeeDTO>();
        }
        if (Validation.isNull(team.getTeamName())
                || Validation.isEmpty(team.getTeamName())) {
            return new ArrayList<EmployeeDTO>();
        }
        return teamService.getEmployeesByTeamName(username, team.getTeamName());
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
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<EmployeeDTO>();
        }
        if (Validation.isNull(team.getTeamName())
                || Validation.isEmpty(team.getTeamName())) {
            return new ArrayList<EmployeeDTO>();
        }
        return teamService.getEmployeesNotInPaticularTeam(username,
                team.getTeamName());
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
        // to authenticate that logeed in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TeamDTO>();
        }
        if (Validation.isNull(employeeDto.getLogin().getUsername())
                || Validation.isEmpty(employeeDto.getLogin().getUsername())) {
            return new ArrayList<TeamDTO>();
        }
        return teamService.getTeamsByEmployee(username, employeeDto.getLogin()
                .getUsername());
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
        // to authenticate that logged in credentials are correct or not
        if (!loginService.authenticateRequest(authorisationToken, username)) {
            return new ArrayList<TeamDTO>();
        }
        return teamService.getTeamsByEmployee(username, username);
    }

    /**
     * method to create team and setting it manager and head user name
     * 
     * @param username
     * @param teamDTO
     * @return response on the basis of status
     */
    @RequestMapping(value = "/createTeam", method = RequestMethod.POST)
    public @ResponseBody Response createTeam(
            @RequestHeader(value = "username") String username,
            @RequestBody TeamDTO teamDTO) {
        // null check
        if (Validation.isNull(teamDTO)) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        teamDTO.setManagerUsername(username);
        if (teamDTO.getTeamHeadUsername() == null) {
            teamDTO.setTeamHeadUsername(username);
        }
        // validate team object
        Response response = teamService.validateTeamObject(teamDTO);
        if (response != null) {
            return response;
        }
        // calls service method
        return teamService.createTeam(username, teamDTO);
    }

    /**
     * method to add particular employee to particular team
     * 
     * @param username
     * @param empTeamDTo
     * @return
     */
    @RequestMapping(value = "/addEmployeeToTeam", method = RequestMethod.POST)
    public @ResponseBody Response addEmployeeToTeam(
            @RequestHeader(value = "username") String username,
            @RequestBody EmpTeamDTO empTeamDTo) {
        // if null
        if (empTeamDTo == null) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        // null check if employee or team object is not there
        if (Validation.isNull(empTeamDTo.getEmployeeDTO())
                || Validation.isNull(empTeamDTo.getEmployeeDTO())) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        if (Validation.isNull(empTeamDTo.getEmployeeDTO().getLogin()
                .getUsername())
                || Validation.isNull(empTeamDTo.getTeamDTO().getTeamName())) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        if (Validation.isEmpty(empTeamDTo.getEmployeeDTO().getLogin()
                .getUsername())
                || Validation.isEmpty(empTeamDTo.getTeamDTO().getTeamName())) {
            return new Response(0, null, MessageConstants.INVALID_CREDENTIALS);
        }
        // calls service method
        return teamService.addEmployeeToTeam(empTeamDTo, username);
    }
}
