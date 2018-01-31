
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
  export class LogOutService {
      private logOutUrl=RequestConstants.AUTHENTICATION_REQUEST+'logout';
      constructor(private http: HttpClient){}
          
      logOut(): Promise<Authentication> {
        return this.http.get(this.logOutUrl)
            .toPromise()
            .then(response => response.json() as Authentication )
            .catch(this.handleError);
    }

      private handleError(error: any): Promise<any> {
          console.error('An error occurred', error);
          return Promise.reject(error.message || error);
      }
  
  }
  