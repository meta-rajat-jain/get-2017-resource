package com.metacube.helpdesk.dto;

import java.io.Serializable;



public class LoginDTO implements Serializable{

    
    /**
     * 
     */
    private static final long serialVersionUID = -6028356566155970945L;
    private String username;
    private String password;
    private String authorisationToken;

    public LoginDTO() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorisationToken() {
        return authorisationToken;
    }

    public void setAuthorisationToken(String authorisationToken) {
        this.authorisationToken = authorisationToken;
    }

    public LoginDTO(String username, String password, String authorisationToken) {
        super();
        this.username = username;
        this.password = password;
        this.authorisationToken = authorisationToken;
    }

    public LoginDTO(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

}
