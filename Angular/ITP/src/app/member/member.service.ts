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

@Injectable()
export class MemberService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private makeRequestUrl = this.request + 'ticket/saveTicket';
    private logOutUrl = this.request + 'auth/logout';
    private getResourcesUrl = this.request + 'ticket/getAllCategoryBasedResources';
   

    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }

    makeRequest(username:string,requestedFor:string,priority:string,requestType:string,resourceType:string,resource:RequestedResource,comment:string,location:string): Promise<Authentication> {
        console.log(resource);
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
          teamName: '',
          location:location,
          requestType:requestType,
          status:'Open'
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
    getResourceRequested(resourceType:string):Promise<RequestedResource[]>{
        let params: URLSearchParams = new URLSearchParams();
        console.log("in service getting status and type" + resourceType );
        params.set('resourceCategory',resourceType );
        let options = new RequestOptions({headers:this.headers, search: params });
        return this.http.get(this.getResourcesUrl,options )
        .toPromise()
        .then(response => response.json() as RequestedResource[])
        .catch(this.handleError);
    }

    

    getTeamsOfEmployee(){

    }
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}