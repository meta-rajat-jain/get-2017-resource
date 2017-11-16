import { CanActivate, Router } from "@angular/router";
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { UserService } from "../user.service";
import { ResponseObject } from "../../Model/responseObject";

@Injectable()
export class ActivateGuardManager implements CanActivate {
responseObject:ResponseObject;
authorised:boolean;

constructor(private userService:UserService,private http:Http,private router:Router) {}
canActivate(){
return this.userService.getManager();
}
}