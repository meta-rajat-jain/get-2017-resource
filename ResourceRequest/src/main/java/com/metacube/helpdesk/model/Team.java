package com.metacube.helpdesk.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Team")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;
    /*
     * Autogenerated teamIdmapped with database table Team column teamId
     */
    @Id
    @Column(name = "teamId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int teamId;

    public Team( int teamId, String teamName, Organisation organisation,
            Employee teamHead, Employee manager, Set<Employee> employees ) {
        super();
        this.teamId = teamId;
        this.teamName = teamName;
        this.organisation = organisation;
        this.teamHead = teamHead;
        this.manager = manager;
        this.employees = employees;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
    @Column(name = "teamName", nullable = false, unique = true)
    private String teamName;
    @ManyToOne
    @JoinColumn(name = "orgId")
    private Organisation organisation;
    @ManyToOne
    @JoinColumn(name = "teamHead")
    private Employee teamHead;
    @ManyToOne
    @JoinColumn(name = "manager")
    private Employee manager;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "teams")
    private Set<Employee> employees = new HashSet<>();

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Team() {
    }

    public Team( String teamName, Organisation organisation, Employee teamHead ) {
        this.teamName = teamName;
        this.organisation = organisation;
        this.teamHead = teamHead;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Employee getTeamHead() {
        return teamHead;
    }

    public void setTeamHead(Employee teamHead) {
        this.teamHead = teamHead;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + teamId;
        result = prime * result
                + ((teamName == null) ? 0 : teamName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Team other = (Team) obj;
        if (teamId != other.teamId)
            return false;
        if (teamName == null) {
            if (other.teamName != null)
                return false;
        } else if (!teamName.equals(other.teamName))
            return false;
        return true;
    }
}
