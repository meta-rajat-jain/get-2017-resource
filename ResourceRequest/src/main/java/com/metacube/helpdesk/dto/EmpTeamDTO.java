package com.metacube.helpdesk.dto;

import java.io.Serializable;

public class EmpTeamDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private EmployeeDTO employeeDTO;
    private TeamDTO teamDTO;

    public EmployeeDTO getEmployeeDTO() {
        return employeeDTO;
    }

    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    public TeamDTO getTeamDTO() {
        return teamDTO;
    }

    public void setTeamDTO(TeamDTO teamDTO) {
        this.teamDTO = teamDTO;
    }

    public EmpTeamDTO(EmployeeDTO employeeDTO, TeamDTO teamDTO) {

        this.employeeDTO = employeeDTO;
        this.teamDTO = teamDTO;
    }
    public EmpTeamDTO() {

    }
}
