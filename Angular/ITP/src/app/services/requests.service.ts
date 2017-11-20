import { Component, OnInit } from "@angular/core";
import { Injectable } from "@angular/core";
import { Headers, Http, RequestOptions } from "@angular/http";
import "rxjs/add/operator/toPromise";
import { Ticket } from "../Model/Ticket";
import { Employee } from "../Model/signEmp";
import { Authentication } from "../Model/Authentication";
import { Login } from "../Model/login";
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { RequestConstants } from "../Constants/request";
import { HttpClient } from "./httpClient";

@Injectable()
export class RequestsService {
  
  private headers: Headers = new Headers();
  private getRequestManager = RequestConstants.TICKET_REQUEST +
     "getAllTicketsOfLoggedInEmployee";
  private getRequestTeam = RequestConstants.TICKET_REQUEST +
    "getAllStatusBasedTicketsForApprover";
  private getRequestMember = RequestConstants.TICKET_REQUEST +
    "getAllStatusBasedTickets";
  private getRequestHelpdesk = RequestConstants.TICKET_REQUEST +
    "getAllStatusBasedTicketsForHelpdesk";
  private changeStatusOfTicketUrl = RequestConstants.TICKET_REQUEST +
    "updateTicket";

  authenticationHeader: AuthenticatedHeader;
  constructor(private http: HttpClient) {
}
  getRequests(status: string, type: string): Promise<Ticket[]> {
    if (type == "Manager") {
      const url = `${this.getRequestMember}/${status}`;
      return this.http
        .get(url)
        .toPromise()
        .then(response => response.json() as Ticket[])
        .catch(this.handleError);
    } else if (type == "Team") {
      const url = `${this.getRequestTeam}/${status}`;
      console.log(url);
      return this.http
        .get(url)
        .toPromise()
        .then(response => response.json() as Ticket[])
        .catch(this.handleError);
    } else if (type == "Member") {
      const url = `${this.getRequestMember}/${status}`;
      console.log(url);
      return this.http
        .get(url)
        .toPromise()
        .then(response => response.json() as Ticket[])
        .catch(this.handleError);
    } else if (type == "Helpdesk") {
      const url = `${this.getRequestHelpdesk}/${status}`;
      console.log(url);
      return this.http
        .get(url)
        .toPromise()
        .then(response => response.json() as Ticket[])
        .catch(this.handleError);
    }
  }
  approveTicket(ticket: Ticket, type: string): Promise<Authentication> {
    if(ticket.status === "InProgress" && type==="Helpdesk"){
      ticket.status = "Closed";
    }
    else if (ticket.status === "Approved" && type == "Helpdesk") {
      ticket.status = "InProgress";
    } else {
      ticket.status = "Approved";
    }
    return this.http
      .post(this.changeStatusOfTicketUrl, ticket)
      .toPromise()
      .then(response => response.json() as Authentication)
      .catch(this.handleError);
  }
  rejectTicket(ticket: Ticket, type: string): Promise<Authentication> {
    ticket.status = "Closed";
    return this.http
      .post(this.changeStatusOfTicketUrl, ticket)
      .toPromise()
      .then(response => response.json() as Authentication)
      .catch(this.handleError);
  }
  private handleError(error: any): Promise<any> {
    console.error("An error occurred", error);
    return Promise.reject(error.message || error);
  }
}
