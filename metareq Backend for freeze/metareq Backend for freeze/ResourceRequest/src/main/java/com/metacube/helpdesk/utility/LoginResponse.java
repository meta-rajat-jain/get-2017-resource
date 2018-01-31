package com.metacube.helpdesk.utility;

public class LoginResponse {

    private Response response;
    private Designation employeeType;
    
    
	public LoginResponse() {
		
	}
	public LoginResponse(Response response, Designation employeeType) {
	
		this.response = response;
		this.employeeType = employeeType;
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public Designation getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(Designation employeeType) {
		this.employeeType = employeeType;
	}
    
}
