import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Employee } from '../Model/signEmp';
import { Authentication } from '../Model/Authentication';

@Injectable()
export class AdminService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private getManagerUrl = this.request + 'admin/getAllManagers';
    private getAllEmployeeUrl = this.request + 'admin/getAllEmployees';
    private logOutUrl = this.request + 'auth/logout';
    private getEmployeeUrl = this.request  + 'admin/updateEmployee';
    private deleteEmployeeUrl = this.request + 'admin/deleteEmployee';
    private addManagerUrl = this.request + 'admin/addManager';
    constructor(private http: Http) {
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
        .then(response =>  response.json() as Employee[] )
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
