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
import { RequestConstants } from "../Constants/request";
import { HttpClient } from "./httpClient";

@Injectable()
export class ManagerService {


    private headers: Headers = new Headers();
    private createTeamUrl=RequestConstants.MANAGER_REQUEST+'createTeam';
    private logOutUrl=RequestConstants.AUTHENTICATION_REQUEST+'logout';
    private getTeamsUrl=RequestConstants.MANAGER_REQUEST+'getAllTeams';
    private canRequestUrl = RequestConstants.EMPLOYEE_REQUEST + 'getEmployeesUnderHead';
    authenticationHeader:AuthenticatedHeader;
    username:string;
    constructor(private http: HttpClient) {
    }

    createTeam(teamName:string,headName:string): Promise<Authentication> {
        this.authenticationHeader = JSON.parse(localStorage.getItem('authenticationObject'));
        console.log(this.authenticationHeader);
        let username = this.authenticationHeader.username.split('@');
        let empName = username[0];
        console.log("in here" + username[0] +"yo" +  username[1]);
        let team :Team ={
            teamName:teamName,
            orgDomain:username[1],
            teamHeadUsername:headName
        }
        console.log(team);
        return this.http.post(this.createTeamUrl,team)
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }

    logOut(): Promise<Authentication> {
        return this.http.get(this.logOutUrl)
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }
    getTeamsUnderManager(){
        console.log("in get Teams"+JSON.parse(localStorage.getItem('authenticationObject')).username);
 
           
        return this.http.get(this.getTeamsUrl)
        .toPromise()
        .then(response => response.json() as Team[] )
        .catch(this.handleError);
    }
    canRequest(): Promise<Employee[]> {
        return this.http.get(this.canRequestUrl)
            .toPromise()
            .then(response => response.json() as Employee[] )
            .catch(this.handleError);
    }
    
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}