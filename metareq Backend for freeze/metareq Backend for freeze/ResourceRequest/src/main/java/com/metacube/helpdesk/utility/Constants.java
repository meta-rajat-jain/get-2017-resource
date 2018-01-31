package com.metacube.helpdesk.utility;

public class Constants {

    public static final String EMAILREGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String CONTACT_NUMBER_REGEX = "^[7-9][0-9]{9}$";
    public static final String NAME = "[A-Za-z]+ *[A-Za-z ]+$";
    public static final String DOMAIN_REGEX = "[_a-zA-Z]+[_a-zA-Z0-9]*[.][_a-zA-Z]{2,4}";
    public static final String VERIFICATION_URL = "http://172.16.33.111:8080/ResourceRequest/rest/auth/verifySignUp";
    public static final String DEFAULT_MAIL_SENDER = "itresourcerequest@gmail.com";
    public static final String VERIFICATION_SUBJECT = "Please Verify your user Account ";
    public static final String VERIFICATION_MESSAGE = "Please verify account by clicking on following link "
            + Constants.VERIFICATION_URL + "/";
    public static final String FORGOT_PASSWORD = "Temporary Password to login";
    public static final String FORGOT_PASSWORD_MESSAGE = "Your new password is ";
    public static final String TICKET_OPEN = "Open";
    public static final String TICKET_INPROGRESS = "InProgress";
    public static final String TICKET_APPROVED = "Approved";
    public static final String TICKET_NEEDINFO = "NeedInfo";
    public static final String TICKET_CLOSED = "Closed";
    public static final String EMPLOYEE_STATUS_ACTIVE = "Active";
    public static final String EMPLOYEE_STATUS_INACTIVE = "Inactive";
}
