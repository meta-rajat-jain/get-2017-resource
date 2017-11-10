import { CanActivate, Router } from "@angular/router";
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { ResponseObject } from "../Model/responseObject";
import { HomeService } from "../home/home.service";
import { Login } from "../Model/login";
import { Employee } from "../Model/signEmp";
@Injectable()
export class ActivateGuard implements CanActivate {
responseObject:ResponseObject;
authorised:boolean;

constructor(private homeService:HomeService,private http:Http,private router:Router) {}
canActivate(){

 return this.homeService.getUser();
}
}