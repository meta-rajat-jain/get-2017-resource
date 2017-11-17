package com.metacube.helpdesk.dto;

import java.io.Serializable;

import com.metacube.helpdesk.model.LogIn;

public class OrganisationDTO implements Serializable {

   
    private static final long serialVersionUID = 1L;
    private String name;
    private String contactNumber;
    private String domain;
    private LoginDTO login;
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
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public OrganisationDTO(String name, String contactNumber, String domain,
    		LoginDTO login) {
        
        this.name = name;
        this.contactNumber = contactNumber;
        this.domain = domain;
        this.login = login;
    }
    public OrganisationDTO() {
        super();
    }
	public LoginDTO getLogin() {
		return login;
	}
	public void setLogin(LoginDTO login) {
		this.login = login;
	}
   
    
    
}
