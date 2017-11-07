import { CanActivate } from "@angular/router";
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { ResponseObject } from "../Model/responseObject";
import { HomeService } from "../home/home.service";
import { Login } from "../Model/login";
import { Employee } from "../Model/signEmp";
@Injectable()
export class ActivateGuardManager implements CanActivate {
responseObject:ResponseObject;
authorised:boolean;

server: string = 'http://172.16.33.111:8080/';
controller: string ='ResourceRequest/rest/auth/';
request: string = this.server + this.controller;
private checkUserUrl=this.request + 'demo';


constructor(private homeService:HomeService,private http:Http) {}
canActivate(){
return this.isAuthorized();
}

isAuthorized():boolean {
  
    this.getUser().then( response => {
       console.log(response);
        this.responseObject = response;
        console.log("empType" + this.responseObject.employeeType);
        if(this.responseObject.employeeType=='Manager'){
               this.authorised=true;
        }
        else{
            this.authorised= false;
        }
        console.log(this.authorised);
        
    })
    console.log(this.authorised);
    return this.authorised;
}
getUser():Promise<ResponseObject>{
    console.log(JSON.parse(this.homeService.getAuthenticationObject()).username);
    let login:Login={
        username:JSON.parse(this.homeService.getAuthenticationObject()).username,
        password:"",
        authorisationToken:JSON.parse(this.homeService.getAuthenticationObject()).authorisationToken
        }
 
    return  this.http.post(this.checkUserUrl,login)
    .toPromise()
    .then(response =>  response.json() as ResponseObject)
    ;
}

}