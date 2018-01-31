package com.metacube.helpdesk.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Organisation")
public class Organisation implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
	@Column(name="orgId")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int orgId;

	@Column(name="orgName", nullable = false)
	private String orgName;

	@Column(name="domain", nullable = false)
	private String domain;

	@Column(name="contactNumber", nullable = false)
	private String contactNumber;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="org_username",referencedColumnName = "username",unique=true,nullable=false)
    private LogIn username;

	public Organisation() {

	}

	public Organisation(String orgName, String domain, String contactNumber,
            LogIn username) {
        this.orgName = orgName;
        this.domain = domain;
        this.contactNumber = contactNumber;
        this.username = username;
    }



    public LogIn getUsername() {
        return username;
    }



    public void setUsername(LogIn username) {
        this.username = username;
    }



    public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

}
