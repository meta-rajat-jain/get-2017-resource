import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { URLSearchParams, RequestOptions } from '@angular/http';
import { Authentication } from '../Model/Authentication';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { signUpOrganisation } from '../Model/signUpOrganisation';
import { Employee } from '../Model/signEmp';
import { ResponseObject } from '../Model/responseObject';
import { RequestedResource } from '../Model/requestResource';
import { Ticket } from '../Model/Ticket';
import { Login } from '../Model/login';
import { Team } from '../Model/team';
import { TicketStatusCount } from '../Model/ticketStatusCount';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map' ;
import 'rxjs/Rx';
@Injectable()
export class MemberService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private makeRequestUrl = this.request + 'ticket/saveTicket';
    private logOutUrl = this.request + 'auth/logout';
    private getResourcesUrl = this.request + 'ticket/getAllCategoryBasedResources';
    private getTicketStatusCountUrl = this.request + 'ticket/getTicketCountByStatusOfRequester';
    private getTicketCountForApprover=this.request + 'ticket/getTicketCountForApprover';
    private getTeamsOfEmp = this.request + 'manager/getTeamsForLoggedInUser';
    private getUserInfoUrl= this.request + 'employee/getEmployeeDetails';
    
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }

    makeRequest(username:string,requestedFor:string,priority:string,requestType:string,resource:RequestedResource,comment:string,locn:string,team:string): Promise<Authentication> {
       console.log(resource + comment + locn + team);
       let status:string;
       if(localStorage.getItem('employeeType')=='Manager'){
           status='Approved';
       } else{
           status='Open';
       }
       let requestedResource:RequestedResource={
            resourceId:resource.resourceId,
            resourceName:resource.resourceName,
            resourceCategoryName:resource.resourceCategoryName
            }
            let request : Ticket={
          ticketNo:0,
          requesterName:username,
          requestedFor:requestedFor,
          priority:priority,
          comment:comment,
          requestedResource:requestedResource,
          teamName: team,
          seatLocation:locn,
          requestType:requestType,
          status:status
          }
          console.log(request);
        
            return this.http.post(this.makeRequestUrl,request, { headers: this.headers })
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }

    logOutMember(): Promise<Authentication> {
        return this.http.get(this.logOutUrl, { headers: this.headers })
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }
    getResourceRequested(resourceType:string):Observable<RequestedResource[]>{
        let params: URLSearchParams = new URLSearchParams();
        console.log("in service getting status and type" + resourceType );
        params.set('resourceCategory',resourceType );
        let options = new RequestOptions({headers:this.headers, search: params });
        return this.http.get(this.getResourcesUrl,options )
        .map(response => response.json() as RequestedResource[] )
        .catch(this.handleError);
    }

    getCounts():Promise<TicketStatusCount[]>{
        return this.http.get(this.getTicketStatusCountUrl,{headers:this.headers} )
        .toPromise()
        .then(response =>  response.json() as TicketStatusCount[] )
        .catch(this.handleError);
    }
    getTeamCounts():Promise<TicketStatusCount[]>{
        return this.http.get(this.getTicketCountForApprover,{headers:this.headers} )
        .toPromise()
        .then(response =>  response.json() as TicketStatusCount[] )
        .catch(this.handleError);
    }
    getTeamsOfEmployee():Promise<Team[]>{
        return this.http.get(this.getTeamsOfEmp,{headers:this.headers} )
        .toPromise()
        .then(response => response.json() as Team[] )
        .catch(this.handleError);   
    }
    getUserInformation(){
        return this.http.get(this.getUserInfoUrl,{headers:this.headers} )
        .toPromise()
        .then(response => response.json() as Employee )
        .catch(this.handleError); 
        }
     
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}