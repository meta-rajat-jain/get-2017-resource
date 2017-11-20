import {Injectable} from '@angular/core';
import { Http, Headers, RequestOptions,URLSearchParams } from "@angular/http";
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
@Injectable()
export class HttpClient {

  constructor(private http: Http) {
   
  }

  createAuthorizationHeader(headers: Headers) {
    let authHeader = JSON.parse(localStorage.getItem('authenticationObject')) as AuthenticatedHeader;
    headers.append('Content-Type', 'application/json');
    headers.append('authorisationToken', authHeader.authorisationToken);
    headers.append('username', authHeader.username);    
  }

  get(url) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    return this.http.get(url, {
      headers: headers
    });
  }
  getByQuery(url,resourceType) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    let params: URLSearchParams = new URLSearchParams();
    params.set("resourceCategory", resourceType);
    let options = new RequestOptions({ headers: headers, search: params });
    
    return this.http.get(url, options);
  }

  post(url, data) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    return this.http.post(url, data, {
      headers: headers
    });
  }
}