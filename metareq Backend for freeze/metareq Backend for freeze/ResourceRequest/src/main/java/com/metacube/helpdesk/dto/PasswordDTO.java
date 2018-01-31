package com.metacube.helpdesk.dto;

public class PasswordDTO {
	 private String username;
	 private String prevPassword;
	 private String newPassword;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPrevPassword() {
		return prevPassword;
	}
	public void setPrevPassword(String prevPassword) {
		this.prevPassword = prevPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public PasswordDTO(String username, String prevPassword, String newPassword) {
		super();
		this.username = username;
		this.prevPassword = prevPassword;
		this.newPassword = newPassword;
	}
}
