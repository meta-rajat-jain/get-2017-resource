package com.metacube.helpdesk.dto;

import java.io.Serializable;



public class EmployeeDTO implements Serializable{

   
    private static final long serialVersionUID = 1463103389931391431L;
    
    
    private String name;
    private String contactNumber;
    private String designation;
    private String status;
    private String orgDomain;
    private LoginDTO loginDetails;

    public EmployeeDTO() {

    }

    public EmployeeDTO(String name, String contactNumber, String designation,
            String status, String orgDomain, LoginDTO loginDetails) {

        this.name = name;
        this.contactNumber = contactNumber;
        this.designation = designation;
        this.status = status;
        this.orgDomain = orgDomain;
        this.loginDetails = loginDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrgDomain() {
        return orgDomain;
    }

    public void setOrgDomain(String orgDomain) {
        this.orgDomain = orgDomain;
    }

    public LoginDTO getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(LoginDTO loginDetails) {
        this.loginDetails = loginDetails;
    }

}
