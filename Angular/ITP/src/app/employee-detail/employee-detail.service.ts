import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Employee } from '../Model/signEmp';
import { Authentication } from '../Model/Authentication';
import { Login } from '../Model/login';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';

@Injectable()
export class EmployeeDetailService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private getEmployeeUrl = this.request + 'admin/getEmployee';
    private updateEmployeeUrl = this.request + 'admin/updateEmployee'
    authenticationHeader:AuthenticatedHeader;
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }

    getEmployeeDetail(username:string):Promise<Employee>{
        console.log("in username" + username);
        let login:Login={
            username:username,
            password:"",
            authorisationToken:""
        }
        let employee:Employee={
            name:"",
            contactNumber: 0,
            orgDomain:"",
            designation:"",
            status:"",
            login:login
         }
         return this.http.post(this.getEmployeeUrl,employee,{ headers: this.headers })
         .toPromise()
         .then(response =>   response.json() as Authentication)
         .catch(this.handleError);
    }
    updateEmployee(name:string,contactNo:number,employee:Employee,username:string):Promise<Authentication>{
        this.authenticationHeader = JSON.parse(localStorage.getItem('authenticationObject'));
      console.log(employee);
        let login:Login={
            username:username,
            authorisationToken:'',
            password:''
          }
          
          let emp:Employee={
            name:name,
            contactNumber:contactNo,
            orgDomain:employee.orgDomain,
            designation:employee.designation,
            status:employee.status,
            login:login
          }
          console.log("After");
          console.log(emp);
          return this.http.post(this.updateEmployeeUrl,emp,{ headers: this.headers })
          .toPromise()
          .then(response =>   response.json() as Authentication)
          .catch(this.handleError);



    }
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}