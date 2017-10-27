package com.metacube.helpdesk.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Organisation")
public class Organisation {

	@Id
	@Column(name="orgID")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int orgID;

	@Column(name="orgName")
	private String orgName;

	@Column(name="domain")
	private String domain;

	@Column(name="contactNumber")
	private String contactNumber;

	public Organisation() {

	}

	public Organisation(int orgID, String orgName, String domain,
			String contactNumber) {

		this.orgID = orgID;
		this.orgName = orgName;
		this.domain = domain;
		this.contactNumber = contactNumber;
	}

	public int getOrgID() {
		return orgID;
	}

	public void setOrgID(int orgID) {
		this.orgID = orgID;
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
