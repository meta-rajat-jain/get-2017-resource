import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { Authentication } from '../Model/Authentication';
import { Ticket } from '../Model/Ticket';
import { RequestedResource } from '../Model/requestResource';
import { RequestConstants } from "../Constants/request";
import { HttpClient } from "./httpClient";


@Injectable()
export class NeedInformationService {


    private headers: Headers = new Headers();
    private needInformationOfTicket=RequestConstants.TICKET_REQUEST+'getTicket';
    private updateTicket=RequestConstants.TICKET_REQUEST+'updateTicket';

    constructor(private http: HttpClient) {
        
    }
  
    getTicket(ticketNumber:number){
        console.log(ticketNumber);
        let ticketNo = { "ticketNo" : ticketNumber} ;
        console.log(JSON.stringify(ticketNo));
        return this.http.post(this.needInformationOfTicket,JSON.stringify(ticketNo))
        .toPromise()
        .then(response =>  response.json() as Ticket )
        .catch(this.handleError);
        }
        updateRequest(ticketNo:number,username:string,requestedFor:string,priority:string,requestType:string,resource:RequestedResource,comment:string,locn:string,team:string,status:string,requestDate:Date): Promise<Authentication> {
            let  date:any;
            console.log(resource + comment + locn + team);
           date =  new Date(); 
           date=Date.now();
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
               status:status,
               lastUpdatedByUsername:username,
               lastDateOfUpdate: date,
               requestDate:requestDate
               }
               console.log(request);
             
                 return this.http.post(this.updateTicket,request)
                 .toPromise()
                 .then(response => response.json() as Authentication )
                 .catch(this.handleError);
         }
         needInfoRequest(ticketNo:number,username:string,requestedFor:string,priority:string,requestType:string,resource:RequestedResource,comment:string,locn:string,team:string,requestDate:Date): Promise<Authentication> {
            console.log(resource + comment + locn + team);
            let  date:any;
            console.log(resource + comment + locn + team);
           date =  new Date(); 
           date=Date.now();
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
               status:'NeedInfo',
               lastUpdatedByUsername:username,
               lastDateOfUpdate: date,
               requestDate:requestDate
               }
               console.log(request);
             
                 return this.http.post(this.updateTicket,request)
                 .toPromise()
                 .then(response => response.json() as Authentication )
                 .catch(this.handleError);
         }
        private handleError(error: any): Promise<any> {
            console.error('An error occurred', error);
            return Promise.reject(error.message || error);
        }
    }