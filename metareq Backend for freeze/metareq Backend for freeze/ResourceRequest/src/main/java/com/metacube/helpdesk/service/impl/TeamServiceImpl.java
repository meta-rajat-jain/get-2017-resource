package com.metacube.helpdesk.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
import com.metacube.helpdesk.model.LogIn;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.service.EmployeeService;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.Designation;
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
        Set<Employee> employees = team.getEmployees();
        employees.add(employeeDAO.getEmployee(loginDAO.get(teamDTO
                .getTeamHeadUsername())));
        team.setEmployees(employees);
        team.setTeamId(teamDTO.getTeamId());
        team.setOrganisation(organisationDAO.getByDomain(teamDTO.getOrgDomain()));
        team.setTeamHead(employeeDAO.getEmployee(loginDAO.get(teamDTO
                .getTeamHeadUsername())));
        team.setManager(employeeDAO.getEmployee(loginDAO.get(teamDTO
                .getManagerUsername())));
        team.setTeamName(teamDTO.getTeamName());
        return team;
    }

    @Override
    public Response validateTeamObject(TeamDTO teamDTO) {
        // null and empty check
        if (Validation.isNull(teamDTO.getTeamName())
                || Validation.isEmpty(teamDTO.getTeamName())
                || Validation.isNull(teamDTO.getTeamHeadUsername())
                || Validation.isEmpty(teamDTO.getTeamHeadUsername())
                || Validation.isNull(teamDTO.getManagerUsername())
                || Validation.isEmpty(teamDTO.getManagerUsername())
                || Validation.isNull(teamDTO.getOrgDomain())
                || Validation.isEmpty(teamDTO.getOrgDomain())) {
            return new Response(0, null,
                    MessageConstants.REQUIRED_DATA_NOT_SPECIFIED);
        }
        return null;
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
        teamDTO.setManagerUsername((team.getManager().getUsername()
                .getUsername()));
        teamDTO.setTeamName(team.getTeamName());
        return teamDTO;
    }

    /*
     * @Override public List<TeamDTO> getAllTeamsUnderHead(String
     * authorisationToken, String username) { List<TeamDTO> allTeamsDTO; if
     * (loginService.authorizeRequest(authorisationToken, username)) {
     * allTeamsDTO = new ArrayList<TeamDTO>(); //fetch team for the logged in
     * user List<Team> teamsUnderHead =
     * teamDAO.getTeamForHead(employeeDAO.getEmployee(loginDAO.get(username)));
     * //get all employees of the team List<Employee> employeeList =
     * teamDAO.getAllEmployeesInTeam(teamsUnderHead); List<Team>
     * allTeams=teamDAO.getAllTeamsByHeadList(employeeList); for (Team team :
     * allTeams) { allTeamsDTO.add(modelToDTO(team)); } return allTeamsDTO; }
     * return null; }
     */
    @Override
    public Response createTeam(String username, TeamDTO teamDTO) {
        if (employeeDAO
                .getEmployee(loginDAO.get(teamDTO.getTeamHeadUsername())) == null) {
            return new Response(0, null, "Team Head Not exist");
        }
        if (!loginService.getAccountType(username).equals(Designation.Manager)) {
            return new Response(0, null, "You are not authorize to create Team");
        }
        if (teamDAO.getTeamByName(teamDTO.getTeamName()) != null) {
            return new Response(0, null,
                    "Team Creation Failed : Probable reason Team with this name Already Exists");
        }
        // add null check for team dTO converted team
        if (teamDAO.createTeam(dtoToModel(teamDTO)) != null) {
            addHeadToManagerTeam(teamDTO.getTeamHeadUsername(), username);
            addEmployeeToTeam(employeeDAO.getEmployee(loginDAO.get(teamDTO
                    .getTeamHeadUsername())), teamDAO.getTeamByName(teamDTO
                    .getTeamName()));
            return new Response(1, null, "Team Successfully Created");
        } else {
            return new Response(0, null,
                    "Team Creation Failed : Probable reason Team with this name Already Exists");
        }
    }

    // validations to be applied here
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
        if (!loginService.getAccountType(managerUsername).equals(
                Designation.Manager)) {
            return new Team();
        }
        String[] splittedUsername = managerUsername.split("@");
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName("TEAM " + splittedUsername[0]);
        teamDTO.setTeamHeadUsername(managerUsername);
        teamDTO.setManagerUsername(managerUsername);
        teamDTO.setOrgDomain(splittedUsername[1]);
        return teamDAO.createTeam(dtoToModel(teamDTO));
    }

    @Override
    public Response addEmployeeToTeam(EmpTeamDTO empTeamDTo, String username) {
        if (!loginService.getAccountType(username).equals(Designation.Manager)) {
            return new Response(0, null,
                    "You are not authorize to add employee to team");
        }
        Employee employee = employeeDAO.getEmployee(loginService
                .dtoToModel(empTeamDTo.getEmployeeDTO().getLogin()));
        if (!Validation.isNull(employee)) {
            Team team = teamDAO.getTeamByName((empTeamDTo.getTeamDTO()
                    .getTeamName()));
            if (!Validation.isNull(team)) {
                if (addEmployeeToTeam(employee, team).equals(Status.Success)) {
                    return new Response(1, null, "Employee added to team");
                } else {
                    return new Response(0, null, "Employee cant be added");
                }
            }
            return new Response(0, null, "Team with this name does not exist");
        }
        return new Response(0, null,
                "Employee with this username does not exist");
    }

    // add validation to this method
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
    public List<TeamDTO> getAllTeamsUnderManager(String username) {
        List<TeamDTO> teamDTOList = new ArrayList<TeamDTO>();
        if (!loginService.getAccountType(username).equals(Designation.Manager)) {
            return teamDTOList;
        }
        List<Team> teams = teamDAO.getTeamForManager(employeeDAO
                .getEmployee(loginDAO.get(username)));
        for (Team team : teams) {
            teamDTOList.add(modelToDTO(team));
        }
        return teamDTOList;
    }

    @Override
    public Set<EmployeeDTO> getAllEmployeesUnderHead(String username) {
        Employee employeeToGetTeamsOf = employeeDAO.getEmployee(loginDAO
                .get(username));
        Set<EmployeeDTO> employeeDTOList = new HashSet<EmployeeDTO>();
        if (employeeToGetTeamsOf != null) {
            List<Team> teamsHeaded = teamDAO
                    .getTeamsHeadedByAnEmployee(employeeToGetTeamsOf);
            for (Team headedTeam : teamsHeaded) {
                for (Employee emp : headedTeam.getEmployees()) {
                    employeeDTOList.add(employeeService.modelToDto(emp));
                }
            }
        }
        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> getEmployeesByTeamName(String username,
            String teamName) {
        List<EmployeeDTO> allEmployeesDTOInTeam = new ArrayList<EmployeeDTO>();
        if (!loginService.getAccountType(username).equals(Designation.Manager)) {
            return allEmployeesDTOInTeam;
        }
        Team team = getTeamByName(teamName);
        if (team != null) {
            Set<Employee> employeeInThisTeam = team.getEmployees();
            for (Employee employee : employeeInThisTeam) {
                allEmployeesDTOInTeam.add(employeeService.modelToDto(employee));
            }
        }
        return allEmployeesDTOInTeam;
    }

    @Override
    public List<TeamDTO> getTeamsByEmployee(String username, String teamMember) {
        List<TeamDTO> employeeTeamsDTO = new ArrayList<TeamDTO>();
        if (!Validation.isEmpty(teamMember) && !Validation.isNull(teamMember)) {
            Employee employee = employeeDAO.getEmployee(loginDAO
                    .get(teamMember));
            if (employee != null) {
                Set<Team> employeeTeams = employee.getTeams();
                for (Team team : employeeTeams) {
                    employeeTeamsDTO.add(modelToDTO(team));
                }
            }
        }
        return employeeTeamsDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesNotInPaticularTeam(String username,
            String teamName) {
        List<EmployeeDTO> allEmployeesDTONotInTeam = new ArrayList<EmployeeDTO>();
        LogIn employeeUsername[];
        if (!loginService.getAccountType(username).equals(Designation.Manager)) {
            return allEmployeesDTONotInTeam;
        }
        Team team = getTeamByName(teamName);
        if (team != null) {
            Set<Employee> employeeInThisTeam = team.getEmployees();
            if (employeeInThisTeam != null) {
                employeeUsername = new LogIn[employeeInThisTeam.size()];
                int i = 0;
                Iterator<Employee> itr = employeeInThisTeam.iterator();
                while (itr.hasNext()) {
                    employeeUsername[i++] = itr.next().getUsername();
                }
                List<Employee> employeeNotInThisTeam = employeeDAO
                        .getEmployeesNotInPaticularTeam(employeeUsername);
                for (Employee employee : employeeNotInThisTeam) {
                    allEmployeesDTONotInTeam.add(employeeService
                            .modelToDto(employee));
                }
            }
        }
        return allEmployeesDTONotInTeam;
    }
}
