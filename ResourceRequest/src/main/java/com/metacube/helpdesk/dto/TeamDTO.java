package com.metacube.helpdesk.dto;

import java.io.Serializable;

public class TeamDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String teamName;
    private String orgDomain;
    private String teamHeadUsername;
    private String managerUsername;

    public TeamDTO() {

    }

    public TeamDTO(String teamName, String orgDomain, String teamHeadUsername) {

        this.teamName = teamName;
        this.orgDomain = orgDomain;
        this.teamHeadUsername = teamHeadUsername;
    }

    public TeamDTO(String teamName, String orgDomain, String teamHeadUsername,
			String managerUsername) {
		super();
		this.teamName = teamName;
		this.orgDomain = orgDomain;
		this.teamHeadUsername = teamHeadUsername;
		this.managerUsername = managerUsername;
	}

	public String getManagerUsername() {
		return managerUsername;
	}

	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}

	public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getOrgDomain() {
        return orgDomain;
    }

    public void setOrgDomain(String orgDomain) {
        this.orgDomain = orgDomain;
    }

    public String getTeamHeadUsername() {
        return teamHeadUsername;
    }

    public void setTeamHeadUsername(String teamHeadUsername) {
        this.teamHeadUsername = teamHeadUsername;
    }

}
