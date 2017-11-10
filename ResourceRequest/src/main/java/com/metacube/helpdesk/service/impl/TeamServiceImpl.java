package com.metacube.helpdesk.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.metacube.helpdesk.dao.EmployeeDAO;
import com.metacube.helpdesk.dao.LoginDAO;
import com.metacube.helpdesk.dao.OrganisationDAO;
import com.metacube.helpdesk.dao.TeamDAO;
import com.metacube.helpdesk.dto.EmpTeamDTO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Organisation;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.service.EmployeeService;
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

    @Resource
    EmployeeService employeeService;

  

    protected Team dtoToModel(TeamDTO teamDTO) {
        if (teamDTO == null) {
            return null;
        }
        Team team = new Team();
        System.out.println(team.getTeamId());
        System.out.println("A");
        Set<Employee> employees = team.getEmployees();
        employees.add(employeeDAO.getEmployee(loginDAO.get(teamDTO
                .getTeamHeadUsername())));
        team.setEmployees(employees);
        // team.setTeamId(getTeamByName(teamDTO.getTeamName()).getTeamId());
        team.setOrganisation(organisationDAO.getByDomain(teamDTO.getOrgDomain()));
        team.setTeamHead(employeeDAO.getEmployee(loginDAO.get(teamDTO
                .getTeamHeadUsername())));
        team.setTeamName(teamDTO.getTeamName());
        return team;
    }

    public Team getTeamByName(String teamName) {
        return teamDAO.getTeamByName(teamName);
    }

    protected TeamDTO modelToDTO(Team team) {
        if (team == null) {
            return null;
        }
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setOrgDomain(team.getOrganisation().getDomain());
        // getTeamHead() will return employee object
        // getUsername() will return Login object of that employee
        // next .getUsername() will return username corresponding to that login
        // object
        teamDTO.setTeamHeadUsername(team.getTeamHead().getUsername()
                .getUsername());
        teamDTO.setTeamName(team.getTeamName());
        return teamDTO;
    }

    /*
     * @Override public List<TeamDTO> getAllTeamsUnderHead(String
     * authorisationToken, String username) { List<TeamDTO> allTeamsDTO; if
     * (loginService.authorizeRequest(authorisationToken, username)) {
     * allTeamsDTO = new ArrayList<TeamDTO>();
     * 
     * //fetch team for the logged in user List<Team> teamsUnderHead =
     * teamDAO.getTeamForHead(employeeDAO.getEmployee(loginDAO.get(username)));
     * 
     * //get all employees of the team List<Employee> employeeList =
     * teamDAO.getAllEmployeesInTeam(teamsUnderHead);
     * 
     * List<Team> allTeams=teamDAO.getAllTeamsByHeadList(employeeList); for
     * (Team team : allTeams) { allTeamsDTO.add(modelToDTO(team)); } return
     * allTeamsDTO; } return null; }
     */

    @Override
    public Response createTeam(String authorisationTokenFromLogin,
            String username, TeamDTO teamDTO) {
        if (!Validation.validateHeaders(authorisationTokenFromLogin, username)) {
            return new Response(0, null, "One or more header is missing");
        }
        if (loginService
                .authorizeRequest(authorisationTokenFromLogin, username)) {

            if (teamDTO.getTeamHeadUsername() == null) {
                teamDTO.setTeamHeadUsername(username);
            }
            if (teamDAO.createTeam(dtoToModel(teamDTO)) != null) {

                addHeadToManagerTeam(teamDTO.getTeamHeadUsername(), username);
                addEmployeeToTeam(employeeDAO.getEmployee(loginDAO.get(teamDTO
                        .getTeamHeadUsername())), teamDAO.getTeamByName(teamDTO
                        .getTeamName()));
                return new Response(1, authorisationTokenFromLogin,
                        "Team Successfully Created");
            } else {
                return new Response(0, authorisationTokenFromLogin,
                        "Team Creation Failed : Probable reason Team with this name Already Exists ");
            }
        }
        return new Response(0, authorisationTokenFromLogin,
                MessageConstants.UNAUTHORISED_USER);
    }

    private Response addHeadToManagerTeam(String teamHeadUsername,
            String managerUsername) {
        String[] splittedUsername = managerUsername.split("@");
        String teamName = "TEAM " + splittedUsername[0];
        Team team = teamDAO.getTeamByName((teamName));
        Employee employee = employeeDAO.getEmployee(loginDAO
                .get(teamHeadUsername));
        if (addEmployeeToTeam(employee, team).equals(Status.Success)) {
            return new Response(1, null,
                    "Head successfully added to manager team");
        } else {
            return new Response(0, null,
                    "Employee cant be added Probable reason employee already exit in team");
        }
    }

    /**
     * Used to create team on creation of any manager automatically
     */
    @Override
    public Team createTeam(String managerUsername) {
        String[] splittedUsername = managerUsername.split("@");
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName("TEAM " + splittedUsername[0]);
        teamDTO.setTeamHeadUsername(managerUsername);
        // addHeadToManagerTeam(managerUsername, managerUsername);
        teamDTO.setOrgDomain(splittedUsername[1]);
        return teamDAO.createTeam(dtoToModel(teamDTO));
    }

    @Override
    public Response addEmployeeToTeam(String authorisationTokenFromLogin,
            String username, EmpTeamDTO empTeamDTo) {
        if (!Validation.validateHeaders(authorisationTokenFromLogin, username)) {
            return new Response(0, null, "One or more header is missing");
        }
        if (loginService
                .authorizeRequest(authorisationTokenFromLogin, username)) {

            Employee employee = employeeDAO.getEmployee(loginService
                    .dtoToModel(empTeamDTo.getEmployeeDTO().getLogin()));
            Team team = teamDAO.getTeamByName((empTeamDTo.getTeamDTO()
                    .getTeamName()));
            if (addEmployeeToTeam(employee, team).equals(Status.Success)) {
                return new Response(1, authorisationTokenFromLogin,
                        "Employee added to team");
            } else {
                return new Response(0, null, "Employee cant be added");
            }

        }
        return new Response(0, authorisationTokenFromLogin,
                MessageConstants.UNAUTHORISED_USER);
    }

    @Override
    public Status addEmployeeToTeam(Employee employee, Team team) {
        Set<Team> listOfTeams = employee.getTeams();
        if (listOfTeams.contains(team)) {
            return Status.Error_Occured;
        }
        listOfTeams.add(team);
        employee.setTeams(listOfTeams);
        if (teamDAO.addEmployeeToTeam(employee).equals(Status.Success)) {
            return Status.Success;
        } else {
            return Status.Error_Occured;
        }
    }

    @Override
    public List<TeamDTO> getAllTeamsUnderHead(String authorisationToken,
            String username) {
        // validate that username belongs to manager only
        List<TeamDTO> teamDTOList = new ArrayList<TeamDTO>();
        String[] splittedUsername = username.split("@");
        String teamName = "TEAM " + splittedUsername[0];
        Team team = teamDAO.getTeamByName((teamName));
        Set<Employee> employeeInThisTeam = team.getEmployees();
        System.out.println(employeeInThisTeam);
        for (Employee employee : employeeInThisTeam) {
            if (!employee.getUsername().getUsername().equals(username)) {
                List<Team> teamsHeadedByEmployee = teamDAO
                        .getTeamsHeadedByAnEmployee(employee);
                for (Team teamHeadByEmployee : teamsHeadedByEmployee) {
                    teamDTOList.add(modelToDTO(teamHeadByEmployee));
                }
            }
        }
        return teamDTOList;
    }

    @Override
    public List<EmployeeDTO> getEmployeesByTeamName(
            String authorisationTokenFromLogin, String username, String teamName) {
        if (!Validation.validateHeaders(authorisationTokenFromLogin, username)) {
            return null;
        }
        List<EmployeeDTO> allEmployeesDTOInTeam = new ArrayList<EmployeeDTO>();

        if (loginService
                .authorizeRequest(authorisationTokenFromLogin, username)) {
            /*
             * to get organisation from username
             */
            /*
             * allEmployeesDTOInTeam = new ArrayList<EmployeeDTO>();
             * List<Employee> allEmployeesInTeam =
             * teamDAO.getEmployeesByTeamName(getTeamByName(teamName));
             * 
             * for (Employee employee : allEmployeesInTeam) {
             * allEmployeesDTOInTeam.add(employeeService.modelToDto(employee));
             * } return allEmployeesDTOInTeam;
             */

            Team team = getTeamByName(teamName);
            Set<Employee> employeeInThisTeam = team.getEmployees();
            for (Employee employee : employeeInThisTeam) {
                allEmployeesDTOInTeam.add(employeeService.modelToDto(employee));
            }

        }
        return allEmployeesDTOInTeam;
    }

    @Override
    public List<TeamDTO> getTeamsByEmployee(String authorisationTokenFromLogin,
            String username, EmployeeDTO employeeDto) {
        if (!Validation.validateHeaders(authorisationTokenFromLogin, username)) {
            return null;
        }
        List<TeamDTO> employeeTeamsDTO = new ArrayList<TeamDTO>();
        if (loginService.authorizeRequest(authorisationTokenFromLogin, username)) {
           Employee employee=employeeDAO.getEmployee(loginDAO.get(employeeDto.getLogin().getUsername())); 
           Set<Team> employeeTeams=employee.getTeams();
           for (Team team : employeeTeams) {
               if(team.getTeamHead().getUsername().getUsername()!=employee.getUsername().getUsername()) {
               employeeTeamsDTO.add(modelToDTO(team));
               }
           }
        }      
        return employeeTeamsDTO;
    }
}
