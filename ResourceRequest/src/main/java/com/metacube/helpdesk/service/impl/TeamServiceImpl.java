package com.metacube.helpdesk.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dao.TeamDAO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.MessageConstants;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;
import com.metacube.helpdesk.utility.Validation;
@Service("teamService")
public class TeamServiceImpl implements TeamService {
    
    @Resource
    OrganisationDAO organisationDAO;
    
    @Resource
    LoginDAO loginDAO;
    
    @Resource
    EmployeeDAO employeeDAO;
    
    @Resource
    TeamDAO teamDAO;
    
    @Resource
    LoginService loginService;

      
    protected Team dtoToModel(TeamDTO teamDTO) {
        if (teamDTO == null) {
            return null;
        }
        Team team=new Team();
        System.out.println(team.getTeamId());
        System.out.println("A");
        team.setOrganisation(organisationDAO.getByDomain(teamDTO.getOrgDomain()));
        System.out.println("B");
        team.setTeamHead(employeeDAO.getEmployee(loginDAO.get(teamDTO.getTeamHeadUsername())));
        team.setTeamName(teamDTO.getTeamName());
        return team;
    }
    
    protected TeamDTO modelToDTO(Team team){
        if (team == null) {
            return null;
        }
        TeamDTO teamDTO=new TeamDTO();
        teamDTO.setOrgDomain(team.getOrganisation().getDomain());
        //getTeamHead() will return employee object
        //getUsername() will return Login object of that employee
        //next .getUsername() will return username corresponding to that login object
        teamDTO.setTeamHeadUsername(team.getTeamHead().getUsername().getUsername());
        teamDTO.setTeamName(team.getTeamName());
        return teamDTO;       
    }

    @Override
    public List<TeamDTO> getAllTeamsUnderHead(String authorisationToken, String username) {
        List<TeamDTO>  allTeamsDTO;
        if (loginService.authorizeRequest(authorisationToken, username)) {
            allTeamsDTO = new ArrayList<TeamDTO>();
            
            //fetch team for the logged in user
            List<Team> teamsUnderHead = teamDAO.getTeamForHead(employeeDAO.getEmployee(loginDAO.get(username)));
            
            //get all employees of the team
            List<Employee> employeeList = teamDAO.getAllEmployeesInTeam(teamsUnderHead);
            
            List<Team> allTeams=teamDAO.getAllTeamsByHeadList(employeeList);
            for (Team team : allTeams) {
                allTeamsDTO.add(modelToDTO(team));
            }
           return allTeamsDTO; 
        }
        return null;
    }
    
    @Override
    public Response createTeam(String authorisationTokenFromLogin, String username,TeamDTO teamDTO) {
        if(!Validation.validateHeaders(authorisationTokenFromLogin, username)){
            return new Response(0,null,"One or more header is missing");
        }
        if (loginService.authorizeRequest(authorisationTokenFromLogin, username)) {
            
            if(teamDAO.createTeam(dtoToModel(teamDTO)).equals(Status.Success)){
                System.out.println("fdhklfj");
                return new Response(1,authorisationTokenFromLogin,"Team Successfully Created");
            }else{
                return new Response(0,authorisationTokenFromLogin,"Team Creation Failed");
            }     
        }
        return new Response(0,authorisationTokenFromLogin,MessageConstants.UNAUTHORISED_USER);
    }

  

}
