package com.metacube.helpdesk.service;

import java.util.List;
import java.util.Set;
import com.metacube.helpdesk.dto.EmpTeamDTO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;

public interface TeamService {

    Response createTeam(String username, TeamDTO teamDTO);

    Team createTeam(String managerUsername);

    Response addEmployeeToTeam(EmpTeamDTO empTeamDTo, String username);

    Status addEmployeeToTeam(Employee employee, Team team);

    List<EmployeeDTO> getEmployeesByTeamName(String username, String teamName);

    List<EmployeeDTO> getEmployeesNotInPaticularTeam(String username,
            String teamName);

    List<TeamDTO> getTeamsByEmployee(String username, String username2);

    List<TeamDTO> getAllTeamsUnderManager(String username);

    Set<EmployeeDTO> getAllEmployeesUnderHead(String username);

    Response validateTeamObject(TeamDTO teamDTO);
}
