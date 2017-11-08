package com.metacube.helpdesk.dao;

import java.util.List;








import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.utility.Response;
import com.metacube.helpdesk.utility.Status;

public interface TeamDAO {


    //List<Team> getAllTeamsByHeadList(List<Employee> employeeList);
    List<Team> getTeamForHead(Employee employee);
   // List<Employee> getAllEmployeesInTeam(List<Team> teamsUnderHead);
    Team createTeam(Team team);
    Status addEmployeeToTeam(Employee employee);
    Team getTeamByName(String teamName);
    void ifEmployeeExistInTeam(String username, String teamHeadUsername);
    List<Team> getTeamsHeadedByAnEmployee(Employee employee);
    

}
