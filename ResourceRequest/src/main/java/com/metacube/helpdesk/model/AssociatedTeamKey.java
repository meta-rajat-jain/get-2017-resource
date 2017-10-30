package com.metacube.helpdesk.model;

import java.io.Serializable;

public class AssociatedTeamKey implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6749596016474921991L;
    int employeeId;
    public AssociatedTeamKey() {
        super();
    }
    public AssociatedTeamKey(int employeeId, int teamId) {
        super();
        this.employeeId = employeeId;
        this.teamId = teamId;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public int getTeamId() {
        return teamId;
    }
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    int teamId;
}
