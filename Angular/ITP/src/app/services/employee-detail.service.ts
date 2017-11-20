import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Employee } from '../Model/signEmp';
import { Authentication } from '../Model/Authentication';
import { Login } from '../Model/login';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { RequestConstants } from "../Constants/request";
import { HttpClient } from "./httpClient";

@Injectable()
export class EmployeeDetailService {

    private headers: Headers = new Headers();


    private getEmployeeUrl=RequestConstants.ADMIN_REQUEST+'getEmployee';
    private updateEmployeeUrl=RequestConstants.ADMIN_REQUEST+'updateEmployee';


    authenticationHeader:AuthenticatedHeader;
    constructor(private http: HttpClient) {

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
            designation:null,
            status:"",
            login:login
         }
         return this.http.post(this.getEmployeeUrl,employee)
         .toPromise()
         .then(response =>    response.json() as Employee)
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

          return this.http.post(this.updateEmployeeUrl,emp)
          .toPromise()
          .then(response =>   response.json() as Authentication)
          .catch(this.handleError);



    }
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}