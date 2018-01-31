package com.metacube.helpdesk.dto;

import java.io.Serializable;
import com.metacube.helpdesk.utility.Designation;

public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1463103389931391431L;
    private String name;

    public EmployeeDTO( LoginDTO login ) {
        super();
        this.login = login;
    }
    private String contactNumber;
    private Designation designation;
    private String status;
    private String orgDomain;
    private LoginDTO login;

    public EmployeeDTO() {
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public EmployeeDTO( String name, String contactNumber,
            Designation designation, String status, String orgDomain,
            LoginDTO login ) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.designation = designation;
        this.status = status;
        this.orgDomain = orgDomain;
        this.login = login;
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

    public LoginDTO getLogin() {
        return login;
    }

    public void setLogin(LoginDTO login) {
        this.login = login;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((login == null) ? 0 : login.getUsername().hashCode());
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
        EmployeeDTO other = (EmployeeDTO) obj;
        if (login == null) {
            if (other.login != null)
                return false;
        } else if (!login.getUsername().equals(other.login.getUsername()))
            return false;
        return true;
    }
}
