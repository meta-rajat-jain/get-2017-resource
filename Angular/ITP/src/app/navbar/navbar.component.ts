import { Component, OnInit } from '@angular/core';
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Authentication } from "../Model/Authentication";
import { ManagerService } from "../services/manager.service";
import { Router } from "@angular/router";
import { AdminService } from "../services/admin.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  username: string ='';
  authenticationHeader: AuthenticatedHeader;
  authentication: Authentication;
  type:string;
  constructor(private managerService:ManagerService,private router:Router,private adminService:AdminService) { }

  ngOnInit() {
    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split("@");
    this.username = name[0];
    this.type = localStorage.getItem('employeeType');
  }

  logOut() {
    this.managerService.logOut().then(response => {
      this.authentication = response;
      if (this.authentication.statusCode == 1) {
        localStorage.clear();
        localStorage.removeItem("authenticationObject");
        this.router.navigate([""]);
      }
    });
  }
  goToRequestTab(type:string):void{
    this.router.navigate(['requestResource',type]); 
  }

}
