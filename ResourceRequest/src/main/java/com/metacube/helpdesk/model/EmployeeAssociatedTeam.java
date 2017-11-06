package com.metacube.helpdesk.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(AssociatedTeamKey.class)
@Table(name="EmployeeAssociatedTeam")
public class EmployeeAssociatedTeam implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name="employeeId",nullable=false)
    private Employee employeeId;
    
    @Id
    @ManyToOne
    @JoinColumn(name="teamId",nullable=false)
    private Team teamId;

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Team getTeamId() {
        return teamId;
    }

    public void setTeamId(Team teamId) {
        this.teamId = teamId;
    }

    public EmployeeAssociatedTeam(Employee employeeId, Team teamId) {
        
        this.employeeId = employeeId;
        this.teamId = teamId;
    }

    public EmployeeAssociatedTeam() {
      
    }
    
    
}
