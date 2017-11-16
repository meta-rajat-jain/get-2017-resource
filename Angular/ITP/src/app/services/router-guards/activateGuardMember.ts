import { CanActivate, Router } from "@angular/router";
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import { ResponseObject } from "../../Model/responseObject";
import { UserService } from "../user.service";

@Injectable()
export class ActivateGuardMember implements CanActivate {
responseObject:ResponseObject;
authorised:boolean;

constructor(private userService:UserService,private http:Http,private router:Router) {}
canActivate(){
return this.userService.getMember();
}
}