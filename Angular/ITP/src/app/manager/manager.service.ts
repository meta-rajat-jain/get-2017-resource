import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { URLSearchParams } from '@angular/http';
import { Authentication } from '../Model/Authentication';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { signUpOrganisation } from '../Model/signUpOrganisation';
import { Employee } from '../Model/signEmp';
import { ResponseObject } from '../Model/responseObject';
import { Team } from '../Model/team';

@Injectable()
export class ManagerService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private createTeamUrl = this.request + 'manager/createTeam';
    private logOutUrl = this.request + 'auth/logout';
    authenticationHeader:AuthenticatedHeader;
    username:string;
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
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
   
        return this.http.post(this.createTeamUrl,team, { headers: this.headers })
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }

    logOutManager(): Promise<Authentication> {
        return this.http.get(this.logOutUrl, { headers: this.headers })
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }
    

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}