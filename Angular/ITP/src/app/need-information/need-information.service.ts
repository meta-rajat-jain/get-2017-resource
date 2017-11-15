import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Authentication } from '../Model/Authentication';
import { Ticket } from '../Model/Ticket';
import { RequestedResource } from '../Model/requestResource';

@Injectable()
export class NeedInformationService {

    server: string = 'http://172.16.33.111:8080/';
    controller: string = 'ResourceRequest/rest/';
    request: string = this.server + this.controller;
    private headers: Headers = new Headers();
    private needInformationOfTicket = this.request + 'ticket/getTicket';
    private updateTicket = this.request + 'ticket/updateTicket';
    constructor(private http: Http) {
        this.headers.append('Content-Type', 'application/json');
        this.headers.append('authorisationToken', JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken);
        this.headers.append('username', JSON.parse(localStorage.getItem('authenticationObject')).username);
     }
  
    getTicket(ticketNumber:number){
        console.log(ticketNumber);
        let ticketNo = { "ticketNo" : ticketNumber} ;
        console.log(JSON.stringify(ticketNo));
        return this.http.post(this.needInformationOfTicket,JSON.stringify(ticketNo),{ headers: this.headers })
        .toPromise()
        .then(response =>  response.json() as Ticket )
        .catch(this.handleError);
        }
        updateRequest(ticketNo:number,username:string,requestedFor:string,priority:string,requestType:string,resource:RequestedResource,comment:string,locn:string,team:string,status:string): Promise<Authentication> {
            console.log(resource + comment + locn + team);
             let requestedResource:RequestedResource={
                 resourceId:resource.resourceId,
                 resourceName:resource.resourceName,
                 resourceCategoryName:resource.resourceCategoryName
                 }
              let request : Ticket={
               ticketNo:ticketNo,
               requesterName:username,
               requestedFor:requestedFor,
               priority:priority,
               comment:comment,
               requestedResource:requestedResource,
               teamName: team,
               seatLocation:locn,
               requestType:requestType,
               status:status
               }
               console.log(request);
             
                 return this.http.post(this.updateTicket,request, { headers: this.headers })
                 .toPromise()
                 .then(response => response.json() as Authentication )
                 .catch(this.handleError);
         }
         needInfoRequest(ticketNo:number,username:string,requestedFor:string,priority:string,requestType:string,resource:RequestedResource,comment:string,locn:string,team:string): Promise<Authentication> {
            console.log(resource + comment + locn + team);
             let requestedResource:RequestedResource={
                 resourceId:resource.resourceId,
                 resourceName:resource.resourceName,
                 resourceCategoryName:resource.resourceCategoryName
                 }
              let request : Ticket={
               ticketNo:ticketNo,
               requesterName:username,
               requestedFor:requestedFor,
               priority:priority,
               comment:comment,
               requestedResource:requestedResource,
               teamName: team,
               seatLocation:locn,
               requestType:requestType,
               status:'NeedInfo'
               }
               console.log(request);
             
                 return this.http.post(this.updateTicket,request, { headers: this.headers })
                 .toPromise()
                 .then(response => response.json() as Authentication )
                 .catch(this.handleError);
         }
        private handleError(error: any): Promise<any> {
            console.error('An error occurred', error);
            return Promise.reject(error.message || error);
        }
    }