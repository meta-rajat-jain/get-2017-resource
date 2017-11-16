import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Employee } from '../Model/signEmp';
import { Authentication } from '../Model/Authentication';
import { RequestConstants } from "../Constants/request";

@Injectable()
export class AdminService {
    private getManagerUrl=RequestConstants.ADMIN_REQUEST+'getAllManagers';
    private getAllEmployeeUrl=RequestConstants.ADMIN_REQUEST+'getAllEmployees';
    private logOutUrl=RequestConstants.AUTHENTICATION_REQUEST+'logout';
    private getEmployeeUrl=RequestConstants.ADMIN_REQUEST+'updateEmployee';
    private deleteEmployeeUrl=RequestConstants.ADMIN_REQUEST+'deleteEmployee';
    private addManagerUrl=RequestConstants.ADMIN_REQUEST+'addManager';
    private headers: Headers = new Headers();

    constructor(private http: Http) {
        console.log(localStorage.getItem('authenticationObject'));
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }

    getManagers(): Promise<Employee[]> {

        console.log(this.getManagerUrl);
        return this.http.get(this.getManagerUrl, { headers: this.headers })
            .toPromise()
            .then(response => response.json() as Employee[] )
            .catch(this.handleError);
    }


    logOutUser(): Promise<Authentication> {
        return this.http.get(this.logOutUrl, { headers: this.headers })
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }
    getEmployees():Promise<Employee[]>{
        return this.http.get(this.getAllEmployeeUrl, { headers: this.headers })
        .toPromise()
        .then(response => {console.log(response);return response.json() as Employee[] })
        .catch(this.handleError);
    }
    editEmployee(employee:Employee):Promise<Authentication>{
         return this.http.post(this.getEmployeeUrl,employee,{ headers: this.headers })
        .toPromise()
        .then(response =>  response.json() as Authentication )
        .catch(this.handleError);
    }
    deleteEmployee(employee:Employee):Promise<Authentication>{
        return this.http.post(this.deleteEmployeeUrl,employee,{headers:this.headers})
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
    }
   
    addManager(employee:Employee):Promise<Authentication>{
        return this.http.post(this.addManagerUrl,employee,{headers:this.headers})
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
    }
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }

}
