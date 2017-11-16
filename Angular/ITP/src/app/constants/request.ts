export class RequestConstants{
    public static SERVER: string = 'http://172.16.33.111:8080/';
    public static CONTROLLER: string = 'ResourceRequest/rest/';
    public static REQUEST: string = RequestConstants.SERVER + RequestConstants.CONTROLLER;
    public static AUTHENTICATION_REQUEST:string=RequestConstants.REQUEST+'auth/';
    public static ADMIN_REQUEST:string=RequestConstants.REQUEST+'admin/';
    public static MANAGER_REQUEST:string=RequestConstants.REQUEST+'manager/';
    public static EMPLOYEE_REQUEST:string=RequestConstants.REQUEST+'employee/';
    public static TEAM_REQUEST:string=RequestConstants.REQUEST+'team/';
    public static TICKET_REQUEST:string=RequestConstants.REQUEST+'ticket/';
    public static HELPDESK_REQUEST:string=RequestConstants.REQUEST+'helpdesk/';
    
    }