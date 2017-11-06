package com.metacube.helpdesk.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Employee")
public class Employee implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Autogenerated employeeId
    @Id
    @Column(name = "employeeId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int employeeId;

    // defines table attribute employeeName
    @Column(name = "employeeName", nullable = false)
    private String employeeName;

    @Column(name = "designation", nullable = false)
    private String designation;

    // defines table attribute contactNumber
    @Column(name = "contactNumber", nullable = false)
    private String contactNumber;

    // defines status of the employee
    @Column(name = "status", nullable = false)
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username", unique = true, nullable = false)
    private LogIn username;

    @ManyToOne
    @JoinColumn(name = "orgId", nullable = false)
    private Organisation organisation;

    public Employee() {

    }

    public Employee(String employeeName, String designation,
            String contactNumber, String status, LogIn username,
            Organisation organisation) {

        this.employeeName = employeeName;
        this.designation = designation;
        this.contactNumber = contactNumber;
        this.status = status;
        this.username = username;
        this.organisation = organisation;

    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LogIn getUsername() {
        return username;
    }

    public void setUsername(LogIn username) {
        this.username = username;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
}
