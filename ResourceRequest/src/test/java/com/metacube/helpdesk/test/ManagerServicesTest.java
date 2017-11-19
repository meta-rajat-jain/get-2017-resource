package com.metacube.helpdesk.test;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.metacube.helpdesk.dto.EmpTeamDTO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.LoginDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.service.LoginService;
import com.metacube.helpdesk.service.TeamService;
import com.metacube.helpdesk.utility.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "test-config.xml" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManagerServicesTest {

    static final String authorisationToken = "c06f126dd6d510afe0bfe4d0191dd38c";
    static final String authorisationTokenOrg = "c06f126dd6d510afe0bfe4d0191dd38c";
    static final String ADMIN_PASSWORD = "admin123";
    static final String ADMIN_USERNAME = "admin@metacube.com";
    static final String EMPLOYEE_NONVERIFIED = "gaurav.tak@metacube.com";
    static final String EMPLOYEE_NONVERIFIED2 = "shivam.lalwani@metacube.com";
    static final String EMPLOYEE_INACTIVE = "anushtha.gupta@metacube.com";
    static final String EMPLOYEE_MANAGER = "shubham.sharma@metacube.com";
    static final String NORMAL_EMPLOYEE = "vaishali.jain@metacube.com";
    static final String NORMAL_EMPLOYEE2 = "shreya.bordia@metacube.com";
    static final String NORMAL_EMPLOYEE3 = "udit.saxena@metacube.com";
    static final String NORMAL_EMPLOYEE4 = "pawan.manglani@metacube.com";
    static final String EMPLOYEE_PASSWORD = "metacube";
    static final String TEAM_TITLE = "Team Shubhi";
    @Autowired
    LoginService loginService;
    @Autowired
    TeamService teamService;

    @Test
    public void createTeamTestFailureNoNameProvided() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setManagerUsername(EMPLOYEE_MANAGER);
        teamDTO.setTeamHeadUsername(EMPLOYEE_MANAGER);
        teamDTO.setOrgDomain("metacube.com");
        teamDTO.setTeamName(null);
        Response response = teamService.createTeam(EMPLOYEE_MANAGER, teamDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals("team name is not provided", response.getMessage());
    }

    @Test
    public void createTeamTestSuccess() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setManagerUsername(EMPLOYEE_MANAGER);
        teamDTO.setTeamHeadUsername(EMPLOYEE_MANAGER);
        teamDTO.setOrgDomain("metacube.com");
        teamDTO.setTeamName("Team Shubhi");
        Response response = teamService.createTeam(EMPLOYEE_MANAGER, teamDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals("Team Successfully Created", response.getMessage());
    }

    @Test
    public void createTeamTestEmployeeAsHeadSuccess() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setManagerUsername(EMPLOYEE_MANAGER);
        teamDTO.setTeamHeadUsername(NORMAL_EMPLOYEE3);
        teamDTO.setOrgDomain("metacube.com");
        teamDTO.setTeamName("Team Udit");
        Response response = teamService.createTeam(EMPLOYEE_MANAGER, teamDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals("Team Successfully Created", response.getMessage());
    }

    @Test
    public void createTeamTestFailureDuplicateTeamName() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setManagerUsername(EMPLOYEE_MANAGER);
        teamDTO.setTeamHeadUsername(EMPLOYEE_MANAGER);
        teamDTO.setOrgDomain("metacube.com");
        teamDTO.setTeamName("Team Shubhi");
        Response response = teamService.createTeam(EMPLOYEE_MANAGER, teamDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals(
                "Team Creation Failed : Probable reason Team with this name Already Exists",
                response.getMessage());
    }

    @Test
    public void createTeamTestFailureNoTeamHeadSpecified() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName("Team XYZ");
        teamDTO.setOrgDomain("metacube.com");
        Response response = teamService.createTeam(EMPLOYEE_MANAGER, teamDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals("Team Successfully Created", response.getMessage());
    }

    @Test
    public void createTeamTestFailureNonExistantTeamHeadSpecified() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName("Team invalid head");
        Response response = teamService.createTeam("abc@metacube.com", teamDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals("Team Head Not exist", response.getMessage());
    }

    @Test
    public void createTeamTestFailureNonManager() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName("Team not authorise head");
        Response response = teamService.createTeam(NORMAL_EMPLOYEE, teamDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals("You are not authorize to create Team",
                response.getMessage());
    }

    @Test
    public void addEmployeeToTeamTestSuccesss() {
        EmpTeamDTO empTeamDTO = new EmpTeamDTO();
        empTeamDTO
                .setEmployeeDTO(new EmployeeDTO(new LoginDTO(NORMAL_EMPLOYEE)));
        empTeamDTO.setTeamDTO(new TeamDTO(TEAM_TITLE));
        Response response = teamService.addEmployeeToTeam(empTeamDTO,
                EMPLOYEE_MANAGER);
        assertEquals(1, response.getStatusCode());
        assertEquals("Employee added to team", response.getMessage());
    }

    @Test
    public void addEmployeeToTeamTestByNonManager() {
        EmpTeamDTO empTeamDTO = new EmpTeamDTO();
        empTeamDTO.setEmployeeDTO(new EmployeeDTO(
                new LoginDTO(NORMAL_EMPLOYEE2)));
        empTeamDTO.setTeamDTO(new TeamDTO(TEAM_TITLE));
        Response response = teamService.addEmployeeToTeam(empTeamDTO,
                NORMAL_EMPLOYEE3);
        assertEquals(0, response.getStatusCode());
        assertEquals("You are not authorize to add employee to team",
                response.getMessage());
    }

    @Test
    public void addEmployeeToTeamTestInNonExistantTeam() {
        EmpTeamDTO empTeamDTO = new EmpTeamDTO();
        empTeamDTO.setEmployeeDTO(new EmployeeDTO(
                new LoginDTO(NORMAL_EMPLOYEE2)));
        empTeamDTO.setTeamDTO(new TeamDTO("Team NotExist"));
        Response response = teamService.addEmployeeToTeam(empTeamDTO,
                EMPLOYEE_MANAGER);
        assertEquals(0, response.getStatusCode());
        assertEquals("Team with this name does not exist",
                response.getMessage());
    }

    @Test
    public void addEmployeeToTeamItAlreadyExistsIn() {
        EmpTeamDTO empTeamDTO = new EmpTeamDTO();
        empTeamDTO
                .setEmployeeDTO(new EmployeeDTO(new LoginDTO(NORMAL_EMPLOYEE)));
        empTeamDTO.setTeamDTO(new TeamDTO(TEAM_TITLE));
        Response response = teamService.addEmployeeToTeam(empTeamDTO,
                EMPLOYEE_MANAGER);
        assertEquals(0, response.getStatusCode());
        assertEquals("Employee cant be added", response.getMessage());
    }

    @Test
    public void addNonExistantEmployeeToTeam() {
        EmpTeamDTO empTeamDTO = new EmpTeamDTO();
        empTeamDTO.setEmployeeDTO(new EmployeeDTO(new LoginDTO("abc@xyz.com")));
        empTeamDTO.setTeamDTO(new TeamDTO(TEAM_TITLE));
        Response response = teamService.addEmployeeToTeam(empTeamDTO,
                EMPLOYEE_MANAGER);
        assertEquals(0, response.getStatusCode());
        assertEquals("Employee with this username does not exist",
                response.getMessage());
    }

    @Test
    public void getTeamsByManagerSuccess() {
        List<TeamDTO> teams = teamService.getTeamsByEmployee(
                authorisationToken, EMPLOYEE_MANAGER, EMPLOYEE_MANAGER);
        assertEquals(2, teams.size());
    }

    @Test
    public void getTeamsForEmployeeSuccess() {
        List<TeamDTO> teams = teamService.getTeamsByEmployee(
                authorisationToken, EMPLOYEE_MANAGER, NORMAL_EMPLOYEE);
        assertEquals(1, teams.size());
    }

    @Test
    public void getTeamsForEmployeeByNonManager() {
        List<TeamDTO> teams = teamService.getTeamsByEmployee(
                authorisationToken, NORMAL_EMPLOYEE, NORMAL_EMPLOYEE);
        assertEquals(0, teams.size());
    }

    @Test
    public void getTeamsForEmployeeNotInAnyTeam() {
        List<TeamDTO> teams = teamService.getTeamsByEmployee(
                authorisationToken, EMPLOYEE_MANAGER, NORMAL_EMPLOYEE3);
        assertEquals(0, teams.size());
    }

    @Test
    public void getEmployeesNotInParticularTeamSuccesss() {
        List<EmployeeDTO> teams = teamService.getEmployeesNotInPaticularTeam(
                authorisationToken, EMPLOYEE_MANAGER, TEAM_TITLE);
        assertEquals(4, teams.size());
    }

    @Test
    public void getEmployeesNotInParticularTeamByNonManager() {
        List<EmployeeDTO> teams = teamService.getEmployeesNotInPaticularTeam(
                authorisationToken, NORMAL_EMPLOYEE3, TEAM_TITLE);
        assertEquals(0, teams.size());
    }

    @Test
    public void getEmployeesNotInParticularTeamNonExistantTeam() {
        List<EmployeeDTO> teams = teamService.getEmployeesNotInPaticularTeam(
                authorisationToken, EMPLOYEE_MANAGER, "Team NotExist");
        assertEquals(0, teams.size());
    }

    @Test
    public void getEmployeesByTeamSuccess() {
        List<EmployeeDTO> teams = teamService.getEmployeesByTeamName(
                authorisationToken, EMPLOYEE_MANAGER, TEAM_TITLE);
        assertEquals(2, teams.size());
    }

    @Test
    public void getEmployeesByTeamNonExistant() {
        List<EmployeeDTO> teams = teamService.getEmployeesByTeamName(
                authorisationToken, EMPLOYEE_MANAGER, "Team Not Exist");
        assertEquals(0, teams.size());
    }

    @Test
    public void getEmployeesByTeamFailedAuthentication() {
        List<EmployeeDTO> teams = teamService.getEmployeesByTeamName(
                "anbnkdbjkhdhkgdhk", EMPLOYEE_MANAGER, TEAM_TITLE);
        assertEquals(0, teams.size());
    }

    @Test
    public void getTeamsUnderManagerSuccess() {
        List<TeamDTO> teams = teamService.getAllTeamsUnderManager(
                authorisationToken, EMPLOYEE_MANAGER);
        assertEquals(3, teams.size());
    }

    @Test
    public void getTeamsUnderManagerUnauthorisedUser() {
        List<TeamDTO> teams = teamService.getAllTeamsUnderManager(
                authorisationToken, NORMAL_EMPLOYEE3);
        assertEquals(0, teams.size());
    }

    @Test
    public void getTeamsUnderManagerInvalidUser() {
        List<TeamDTO> teams = teamService.getAllTeamsUnderManager(
                authorisationToken, "abc@xys.com");
        assertEquals(0, teams.size());
    }
}
