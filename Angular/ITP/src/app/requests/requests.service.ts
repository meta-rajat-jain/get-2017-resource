import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Ticket } from '../Model/Ticket';
import { Employee } from '../Model/signEmp';
import { Authentication } from '../Model/Authentication';
import { Login } from '../Model/login';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';


@Injectable()
export class RequestsService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private getRequestManager = this.request + 'ticket/getAllTicketsOfLoggedInEmployee';
    private getRequestTeam = this.request + 'ticket/getAllStatusBasedTicketsForApprover';
    private approveTicketUrl = this.request + 'ticket/updateTicket';
    authenticationHeader:AuthenticatedHeader;
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }
     getRequests(status:string,type:string):Promise<Ticket[]>{
         if(type=='Manager'){
            let params: URLSearchParams = new URLSearchParams();
            console.log("in service getting status and type" + status + type );
            params.set('status',status );
            return this.http.get(this.getRequestManager,{ search: params } )
            .toPromise()
            .then(response => response.json() as Ticket[])
            .catch(this.handleError);
         }
         else if(type ='Team'){
            let params: URLSearchParams = new URLSearchParams();
            console.log("in service getting status and type" + status + type );
            params.set('status',status );
            return this.http.get(this.getRequestTeam,{ search: params } )
            .toPromise()
            .then(response => response.json() as Ticket[])
            .catch(this.handleError);
         }
         
     }
     approveTicket(ticket:Ticket):Promise<Authentication>{
        ticket.status = 'Approved';
        return this.http.post(this.approveTicketUrl,ticket, { headers: this.headers })
        .toPromise()
        .then(response => response.json() as Authentication )
        .catch(this.handleError);

     }
     private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
    }