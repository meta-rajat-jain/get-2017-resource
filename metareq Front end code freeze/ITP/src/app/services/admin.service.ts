import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Employee } from '../Model/signEmp';
import { Authentication } from '../Model/Authentication';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { RequestConstants } from "../Constants/request";
import { HttpClient } from "./httpClient";

@Injectable()
export class AdminService {
    private getManagerUrl=RequestConstants.ADMIN_REQUEST+'getAllManagers';
    private getAllEmployeeUrl=RequestConstants.ADMIN_REQUEST+'getAllEmployees';
    private getEmployeeUrl=RequestConstants.ADMIN_REQUEST+'updateEmployee';
    private deleteEmployeeUrl=RequestConstants.ADMIN_REQUEST+'deleteEmployee';
    private addManagerUrl=RequestConstants.ADMIN_REQUEST+'addManager';
    private headers: Headers = new Headers();
    
    constructor(private http: HttpClient) {
       


    }
   
    getManagers(): Promise<Employee[]> {
        return this.http.get(this.getManagerUrl, )
            .toPromise()
            .then(response => response.json() as Employee[])
            .catch(this.handleError);
    }


    getEmployees():Promise<Employee[]>{
        return this.http.get(this.getAllEmployeeUrl)
        .toPromise()
        .then(response => {return response.json() as Employee[] })
        .catch(this.handleError);
    }
    editEmployee(employee:Employee):Promise<Authentication>{
         return this.http.post(this.getEmployeeUrl,employee)
        .toPromise()
        .then(response =>  response.json() as Authentication )
        .catch(this.handleError);
    }
    deleteEmployee(employee:Employee):Promise<Authentication>{
        return this.http.post(this.deleteEmployeeUrl,employee)
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
    }
   
    addManager(employee:Employee):Promise<Authentication>{
        return this.http.post(this.addManagerUrl,employee)
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
    }
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }

}
