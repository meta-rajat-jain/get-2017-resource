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
import { HttpClient } from "./httpClient";


@Injectable()
export class TeamDetailService {

    private headers: Headers = new Headers();
    private addToTeamUrl=RequestConstants.MANAGER_REQUEST+'addEmployeeToTeam';
    private getTeams=RequestConstants.MANAGER_REQUEST+'getEmployeesByTeamName';
    private approveTicketUrl=RequestConstants.TICKET_REQUEST+'updateTicket';
    private getEmployeesToAddUrl=RequestConstants.MANAGER_REQUEST+'getEmployeesNotInParticularTeam';



    authenticationHeader:AuthenticatedHeader;
    
    constructor(private http: HttpClient) {
   }
    
         
     
     addToTeam(employee:Employee,teamName:string):Promise<Authentication>{
         let employeeTeamDetail = {  
            "employeeDTO" : employee , 
            "teamDTO" : {"teamName":teamName}
        }
           
        return this.http.post(this.addToTeamUrl,JSON.stringify(employeeTeamDetail) )
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
     }
     getTeamsDetail(teamNm:string):Promise<Employee[]>{
        let teamName = { "teamName": teamNm }
        return this.http.post(this.getTeams,teamName )
        .toPromise()
        .then(response => response.json() as Employee[])
        .catch(this.handleError);
     }
     getEmployeesToAdd(teamNm:string):Promise<Employee[]>{
        let teamName = { "teamName": teamNm }
        return this.http.post(this.getEmployeesToAddUrl,teamName  )
        .toPromise()
        .then(response => response.json() as Employee[])
        .catch(this.handleError);
     }
     
     private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
    }