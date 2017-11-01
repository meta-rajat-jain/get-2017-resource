package com.metacube.helpdesk.utility;


public class Response {
    //enum
    private int statusCode;
    private String authorisationToken;
    private String message;
    
    public Response() {
    }
    public Response(int statusCode, String authorisationToken, String message) {
        this.statusCode = statusCode;
        this.authorisationToken = authorisationToken;
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getAuthorisationToken() {
        return authorisationToken;
    }
    public void setAuthorisationToken(String authorisationToken) {
        this.authorisationToken = authorisationToken;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
