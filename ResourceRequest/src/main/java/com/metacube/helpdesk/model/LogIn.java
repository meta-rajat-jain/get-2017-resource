package com.metacube.helpdesk.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LogIn")
public class LogIn implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
	@Column(name="logInId")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int logInId;
	
	@Column(name="username",unique=true, nullable=false)
	private String username;
	
	@Column(name="password")
	private String password;

	@Column(name="authorisationToken")
	private String authorisationToken;
	
	@Column(name="enabled")
    private Boolean enabled;
	
	
	public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public int getLogInId() {
        return logInId;
    }

    public void setLogInId(int logInId) {
        this.logInId = logInId;
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

    public LogIn() {
		
	}

    public LogIn(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public LogIn(String userName, String password, String authorisationToken) {
        this.username = userName;
        this.password = password;
        this.authorisationToken = authorisationToken;
    }

    public LogIn(String username, String password, String authorisationToken,
            Boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorisationToken = authorisationToken;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
	
}
