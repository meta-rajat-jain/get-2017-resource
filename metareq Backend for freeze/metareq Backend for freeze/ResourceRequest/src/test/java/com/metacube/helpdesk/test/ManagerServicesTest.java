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
    static final String TEAM_TITLE = "Team Shubham";
    @Autowired
    LoginService loginService;
    @Autowired
    TeamService teamService;

    @Test
    public void test41_createTeamTestSuccess() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setManagerUsername(EMPLOYEE_MANAGER);
        teamDTO.setTeamHeadUsername(EMPLOYEE_MANAGER);
        teamDTO.setOrgDomain("metacube.com");
        teamDTO.setTeamName("Team Shubham");
        teamDTO.setTeamId(0);
        Response response = teamService.createTeam(EMPLOYEE_MANAGER, teamDTO);
        assertEquals(1, response.getStatusCode());
        assertEquals("Team Successfully Created", response.getMessage());
    }

    @Test
    public void test42_createTeamTestEmployeeAsHeadSuccess() {
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
    public void test43_createTeamTestFailureDuplicateTeamName() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setManagerUsername(EMPLOYEE_MANAGER);
        teamDTO.setTeamHeadUsername(EMPLOYEE_MANAGER);
        teamDTO.setOrgDomain("metacube.com");
        teamDTO.setTeamName("Team Shubham");
        Response response = teamService.createTeam(EMPLOYEE_MANAGER, teamDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals(
                "Team Creation Failed : Probable reason Team with this name Already Exists",
                response.getMessage());
    }

    @Test
    public void test43_createTeamTestFailureNonExistantTeamHeadSpecified() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamName("Team invalid head");
        Response response = teamService.createTeam("abc@metacube.com", teamDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals("Team Head Not exist", response.getMessage());
    }

    @Test
    public void test43_createTeamTestFailureNonManager() {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamHeadUsername(NORMAL_EMPLOYEE);
        teamDTO.setTeamName("Team not authorise head");
        Response response = teamService.createTeam(NORMAL_EMPLOYEE, teamDTO);
        assertEquals(0, response.getStatusCode());
        assertEquals("You are not authorize to create Team",
                response.getMessage());
    }

    @Test
    public void test44_addEmployeeToTeamTestSuccesss() {
        EmpTeamDTO empTeamDTO = new EmpTeamDTO();
        empTeamDTO
                .setEmployeeDTO(new EmployeeDTO(new LoginDTO(NORMAL_EMPLOYEE)));
        empTeamDTO.setTeamDTO(new TeamDTO(TEAM_TITLE));
        Response response = teamService.addEmployeeToTeam(empTeamDTO,
                EMPLOYEE_MANAGER);
        // assertEquals(1, response.getStatusCode());
        assertEquals("Employee added to team", response.getMessage());
    }

    @Test
    public void test45_addEmployeeToTeamTestByNonManager() {
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
    public void test46_addEmployeeToTeamTestInNonExistantTeam() {
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
    public void test47_addEmployeeToTeamItAlreadyExistsIn() {
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
    public void test48_addNonExistantEmployeeToTeam() {
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
    public void test49_getTeamsByManagerSuccess() {
        List<TeamDTO> teams = teamService.getTeamsByEmployee(EMPLOYEE_MANAGER,
                EMPLOYEE_MANAGER);
        assertEquals(2, teams.size());
    }

    @Test
    public void test50_getTeamsForEmployeeSuccess() {
        List<TeamDTO> teams = teamService.getTeamsByEmployee(EMPLOYEE_MANAGER,
                NORMAL_EMPLOYEE);
        assertEquals(1, teams.size());
    }

    @Test
    public void test51_getTeamsForEmployeeNotInAnyTeam() {
        List<TeamDTO> teams = teamService.getTeamsByEmployee(EMPLOYEE_MANAGER,
                NORMAL_EMPLOYEE4);
        assertEquals(0, teams.size());
    }

    @Test
    public void test51_getEmployeesNotInParticularTeamSuccesss() {
        List<EmployeeDTO> teams = teamService.getEmployeesNotInPaticularTeam(
                EMPLOYEE_MANAGER, TEAM_TITLE);
        assertEquals(4, teams.size());
    }

    @Test
    public void test52_getEmployeesNotInParticularTeamByNonManager() {
        List<EmployeeDTO> teams = teamService.getEmployeesNotInPaticularTeam(
                NORMAL_EMPLOYEE3, TEAM_TITLE);
        assertEquals(0, teams.size());
    }

    @Test
    public void test53_getEmployeesNotInParticularTeamNonExistantTeam() {
        List<EmployeeDTO> teams = teamService.getEmployeesNotInPaticularTeam(
                EMPLOYEE_MANAGER, "Team NotExist");
        assertEquals(0, teams.size());
    }

    @Test
    public void test54_getEmployeesByTeamSuccess() {
        List<EmployeeDTO> teams = teamService.getEmployeesByTeamName(
                EMPLOYEE_MANAGER, TEAM_TITLE);
        assertEquals(2, teams.size());
    }

    @Test
    public void test55_getEmployeesByTeamNonExistant() {
        List<EmployeeDTO> teams = teamService.getEmployeesByTeamName(
                EMPLOYEE_MANAGER, "Team Not Exist");
        assertEquals(0, teams.size());
    }

    @Test
    public void test57_getTeamsUnderManagerSuccess() {
        List<TeamDTO> teams = teamService
                .getAllTeamsUnderManager(EMPLOYEE_MANAGER);
        assertEquals(3, teams.size());
    }

    @Test
    public void test58_getTeamsUnderManagerUnauthorisedUser() {
        List<TeamDTO> teams = teamService
                .getAllTeamsUnderManager(NORMAL_EMPLOYEE3);
        assertEquals(0, teams.size());
    }

    @Test
    public void test59_getTeamsUnderManagerInvalidUser() {
        List<TeamDTO> teams = teamService
                .getAllTeamsUnderManager("abc@xys.com");
        assertEquals(0, teams.size());
    }
}
