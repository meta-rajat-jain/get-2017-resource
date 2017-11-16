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
import { RequestConstants } from "../Constants/request";


@Injectable()
export class TeamDetailService {

    private headers: Headers = new Headers();
    private addToTeamUrl=RequestConstants.MANAGER_REQUEST+'addEmployeeToTeam';
    private getTeams=RequestConstants.MANAGER_REQUEST+'getEmployeesByTeamName';
    private approveTicketUrl=RequestConstants.TICKET_REQUEST+'updateTicket';
    private getEmployeesToAddUrl=RequestConstants.MANAGER_REQUEST+'getEmployeesNotInPaticularTeam';



    authenticationHeader:AuthenticatedHeader;
    
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }
    
         
     
     addToTeam(employee:Employee,teamName:string):Promise<Authentication>{
         let employeeTeamDetail = {  
            "employeeDTO" : employee , 
            "teamDTO" : {"teamName":teamName}
        }
            console.log(JSON.stringify(employeeTeamDetail));
        return this.http.post(this.addToTeamUrl,JSON.stringify(employeeTeamDetail), {headers:this.headers} )
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
     }
     getTeamsDetail(teamNm:string):Promise<Employee[]>{
        let teamName = { "teamName": teamNm }
        return this.http.post(this.getTeams,teamName , {headers:this.headers} )
        .toPromise()
        .then(response => response.json() as Employee[])
        .catch(this.handleError);
     }
     getEmployeesToAdd(teamNm:string):Promise<Employee[]>{
        let teamName = { "teamName": teamNm }
        return this.http.post(this.getEmployeesToAddUrl,teamName , {headers:this.headers} )
        .toPromise()
        .then(response => response.json() as Employee[])
        .catch(this.handleError);
     }
     
     private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
    }