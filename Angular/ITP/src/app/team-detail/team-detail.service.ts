import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Ticket } from '../Model/Ticket';
import { Employee } from '../Model/signEmp';
import { Authentication } from '../Model/Authentication';
import { Login } from '../Model/login';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { Team } from '../Model/team';


@Injectable()
export class TeamDetailService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private getRequestManager = this.request + 'ticket/getAllTicketsOfLoggedInEmployee';
    private getRequestTeam = this.request + 'ticket/getAllStatusBasedTicketsForApprover';
    private approveTicketUrl = this.request + 'ticket/updateTicket';
    authenticationHeader:AuthenticatedHeader;
    
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }
     manageTeam(team:Team,):Promise<Ticket[]>{
        
            return this.http.post(this.getRequestManager, {headers:this.headers})
            .toPromise()
            .then(response => response.json() as Ticket[])
            .catch(this.handleError);
         }
         
     
     addToTeam(employee:Employee,teamName:string):Promise<Authentication>{
         let employeeTeamDetail = { "employeeTeamDetail": [ 
            { "employee" : [employee]} , 
            { "teamName" : [teamName]} ] }
        return this.http.post(this.getRequestManager,employeeTeamDetail, {headers:this.headers} )
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
     }
     getTeamsDetail(teamName:string):Promise<Employee[]>{
        return this.http.post(this.getRequestManager,teamName , {headers:this.headers} )
        .toPromise()
        .then(response => response.json() as Employee[])
        .catch(this.handleError);
     }
     
     private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
    }