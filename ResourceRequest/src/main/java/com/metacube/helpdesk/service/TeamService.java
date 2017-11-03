package com.metacube.helpdesk.service;

import java.util.List;

import com.metacube.helpdesk.dto.TeamDTO;
import com.metacube.helpdesk.model.Employee;
import com.metacube.helpdesk.utility.Response;

public interface TeamService {

 

    List<TeamDTO> getAllTeamsUnderHead(String authorisationToken,
            String username);


    Response createTeam(String authorisationToken, String username,
            TeamDTO teamDTO);


}
