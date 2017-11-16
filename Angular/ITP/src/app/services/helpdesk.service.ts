import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { URLSearchParams, RequestOptions } from '@angular/http';
import { Authentication } from '../Model/Authentication';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { signUpOrganisation } from '../Model/signUpOrganisation';
import { Employee } from '../Model/signEmp';
import { ResponseObject } from '../Model/responseObject';
import { Team } from '../Model/team';
import { Login } from '../Model/login';
import { TicketStatusCount } from '../Model/ticketStatusCount';
import { RequestConstants } from "../Constants/request";

@Injectable()
export class HelpdeskService {


    private headers: Headers = new Headers();


    private logOutUrl=RequestConstants.AUTHENTICATION_REQUEST+'logout';
    private getTicketStatusCountUrl=RequestConstants.HELPDESK_REQUEST+'getTicketCountForHelpDesk';
    private getUserInfoUrl=RequestConstants.EMPLOYEE_REQUEST+'getEmployeeDetails';


    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }

  
    logOut(): Promise<Authentication> {
        return this.http.get(this.logOutUrl, { headers: this.headers })
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }
    getCounts():Promise<TicketStatusCount[]>{
        return this.http.get(this.getTicketStatusCountUrl,{headers:this.headers} )
        .toPromise()
        .then(response =>  response.json() as TicketStatusCount[] )
        .catch(this.handleError);
    }
    
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}