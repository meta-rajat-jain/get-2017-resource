import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';
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
    private getRequestMember = this.request + 'ticket/getAllStatusBasedTickets';
    private getRequestHelpdesk = this.request + 'ticket/getAllStatusBasedTicketsForHelpdesk';
    private changeStatusOfTicketUrl = this.request + 'ticket/updateTicket';
    authenticationHeader:AuthenticatedHeader;
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }
     getRequests(status:string,type:string):Promise<Ticket[]>{
         if(type=='Manager'){
            let params: URLSearchParams = new URLSearchParams();
            console.log("in service getting status and type" + status + type);
            params.set('status',status );
            let options = new RequestOptions({headers:this.headers, search: params });
            return this.http.get(this.getRequestManager, options )
            .toPromise()
            .then(response => response.json() as Ticket[])
            .catch(this.handleError);
         }
         else if(type =='Team'){
           
           
            const url = `${this.getRequestTeam}/${status}`;
            console.log(url);
            return this.http.get(url, {headers:this.headers} )
            .toPromise()
            .then(response => response.json() as Ticket[])
            .catch(this.handleError);
         }
         else if(type =='Member') {
            const url = `${this.getRequestMember}/${status}`;
            console.log(url);
            return this.http.get(url,{headers:this.headers} )
            .toPromise()
            .then(response => response.json() as Ticket[])
            .catch(this.handleError);
         }
         else if(type =='Helpdesk') {
            const url = `${this.getRequestHelpdesk}/${status}`;
            console.log(url);
            return this.http.get(url,{headers:this.headers} )
            .toPromise()
            .then(response => response.json() as Ticket[])
            .catch(this.handleError);
         }
         
         
     }
     approveTicket(ticket:Ticket,type:string):Promise<Authentication>{
        if(type=='HelpDesk'){
        ticket.status = 'InProgress';
         }else {
             ticket.status = 'Approved';
         }
        return this.http.post(this.changeStatusOfTicketUrl,ticket, { headers: this.headers })
        .toPromise()
        .then(response => response.json() as Authentication )
        .catch(this.handleError);

     }
     rejectTicket(ticket:Ticket,type:string):Promise<Authentication>{
        ticket.status = 'Closed';
        return this.http.post(this.changeStatusOfTicketUrl,ticket, { headers: this.headers })
        .toPromise()
        .then(response => response.json() as Authentication )
        .catch(this.handleError);

     }
     private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
    }