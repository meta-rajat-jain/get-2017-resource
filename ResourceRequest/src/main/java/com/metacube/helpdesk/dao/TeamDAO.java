package com.metacube.helpdesk.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.model.Team;
import com.metacube.helpdesk.utility.Status;

@Repository
public interface TeamDAO {

    List<Team> getTeamForHead(Employee employee);

    Team createTeam(Team team);

    Status addEmployeeToTeam(Employee employee);

    Team getTeamByName(String teamName);

    void ifEmployeeExistInTeam(String username, String teamHeadUsername);

    List<Team> getTeamsHeadedByAnEmployee(Employee employee);

    List<Team> getTeamForManager(Employee employee);
}
