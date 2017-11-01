package com.metacube.helpdesk.utility;

public class LoginResponse {

    private Response response;
    private String employeeType;
    public Response getResponse() {
        return response;
    }
    public void setResponse(Response response) {
        this.response = response;
    }
    public String getEmployeeType() {
        return employeeType;
    }
    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
    public LoginResponse(Response response, String employeeType) {
        this.response = response;
        this.employeeType = employeeType;
    }
    public LoginResponse() {
    }
    
}
