import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Authentication } from '../Model/Authentication';
import { Ticket } from '../Model/Ticket';

@Injectable()
export class NeedInformationService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private needInformation = this.request + '';
    
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }
     getRequest(ticket:Ticket,type:string):Promise<Ticket>{
        
        return this.http.post(this.needInformation,ticket,{ headers: this.headers })
        .toPromise()
        .then(response =>  response.json() as Ticket )
        .catch(this.handleError);
        }

        private handleError(error: any): Promise<any> {
            console.error('An error occurred', error);
            return Promise.reject(error.message || error);
        }
    }