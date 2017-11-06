package com.metacube.helpdesk.controller;

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
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.Response;

@CrossOrigin
@Controller
@RequestMapping(value = "/manager")
public class ManagerController {
    
    @Resource
    TeamService teamService;
    
    @RequestMapping(value = "/getAllTeams", method = RequestMethod.GET)
    public @ResponseBody List<TeamDTO> getAllTeams(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username) {
        return teamService.getAllTeamsUnderHead(authorisationToken, username);
    }
    
    @RequestMapping(value = "/createTeam", method = RequestMethod.POST)
    public @ResponseBody Response createTeam(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody TeamDTO teamDTO) {
        return teamService.createTeam(authorisationToken, username,teamDTO);
    }
    
    @RequestMapping(value = "/addEmployeeToTeam", method = RequestMethod.POST)
    public @ResponseBody Response addEmployeeToTeam(
            @RequestHeader(value = "authorisationToken") String authorisationToken,
            @RequestHeader(value = "username") String username,
            @RequestBody EmpTeamDTO empTeamDTo) {
        return teamService.addEmployeeToTeam(authorisationToken, username,empTeamDTo);
    }
    
    
}
