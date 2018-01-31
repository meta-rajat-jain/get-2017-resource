import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { URLSearchParams, RequestOptions } from '@angular/http';
import { Authentication } from '../Model/Authentication';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { signUpOrganisation } from '../Model/signUpOrganisation';
import { Employee } from '../Model/signEmp';
import { ResponseObject } from '../Model/responseObject';
import { RequestedResource } from '../Model/requestResource';
import { Ticket } from '../Model/Ticket';
import { Login } from '../Model/login';
import { Team } from '../Model/team';
import { TicketStatusCount } from '../Model/ticketStatusCount';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map' ;
import 'rxjs/Rx';
import { RequestConstants } from '../Constants/request';
import { HttpClient } from "./httpClient";
@Injectable()
export class RequestResourceService {
  private headers: Headers = new Headers();

  private makeRequestUrl = RequestConstants.TICKET_REQUEST + "saveTicket";
  private getResourcesUrl = RequestConstants.TICKET_REQUEST +
    "getAllCategoryBasedResources";

  constructor(private http: HttpClient) {}

  getResourceRequested(resourceType: string): Observable<RequestedResource[]> {
    return this.http
      .getByQuery(this.getResourcesUrl, resourceType)
      .map(response => response.json() as RequestedResource[])
      .catch(this.handleError);
  }

  private handleError(error: any): Promise<any> {
    console.error("An error occurred", error);
    return Promise.reject(error.message || error);
  }
}