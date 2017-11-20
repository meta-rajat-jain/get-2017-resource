
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { URLSearchParams, RequestOptions } from '@angular/http';
import { Authentication } from '../Model/Authentication';
import { Login } from '../Model/login';


@Injectable()
export class PasswordService {

server: string = 'http://172.16.33.111:8080/';
controller: string = 'ResourceRequest/rest/';
request: string = this.server + this.controller;
forgetPasswordUrl:string=this.request+'auth/forgotPassword';
private headers: Headers = new Headers();
constructor(private http: Http) {
this.headers.append('Content-Type', 'application/json');
}

forgetPassword(username:string){
let login:Login={ username:username,
password:"",
authorisationToken:""
}
return this.http.post(this.forgetPasswordUrl,login)
.toPromise()
.then(response =>{ response.json() as Authentication} )
.catch(this.handleError);
}
private handleError(error: any): Promise<any> {
console.error('An error occurred', error);
return Promise.reject(error.message || error);
}
}