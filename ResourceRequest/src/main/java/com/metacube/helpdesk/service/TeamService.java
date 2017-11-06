package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.dto.EmpTeamDTO;
import com.metacube.helpdesk.dto.EmployeeDTO;
import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.utility.*;

public interface TeamService {

 

    List<TeamDTO> getAllTeamsUnderHead(String authorisationToken,
            String username);


    Response createTeam(String authorisationToken, String username,
            TeamDTO teamDTO);


    Status createTeam(String managerUsername);


    Response addEmployeeToTeam(String authorisationToken, String username,
            EmpTeamDTO empTeamDTo);


  


}
