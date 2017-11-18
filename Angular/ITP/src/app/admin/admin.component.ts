import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Employee } from "../Model/signEmp";
import { Authentication } from "../Model/Authentication";
import { AdminService } from "../services/admin.service";
import { UserService } from "../services/user.service";


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  username: string;
  autheticatedHeader: AuthenticatedHeader;
  managers: Employee[]=[];
  authentication: Authentication;
  reactiveForm: FormGroup;
  employee:Employee;
  type:boolean=true;
  empType:boolean;
  title:string;
  constructor(private adminService: AdminService, private userService: UserService,private router:Router) {  
  }

  ngOnInit() {
    this.autheticatedHeader = JSON.parse(localStorage.getItem('authenticationObject'));
    let name = this.autheticatedHeader.username.split('@');
    this.username = name[0];
   
    this.getManagers();
  
  }
  logOut(): void {
    this.adminService.logOutUser().then(response => {
      this.authentication = response;
      if (this.authentication.statusCode == 1){
        localStorage.removeItem('authenticationObject');
        localStorage.clear();
        this.router.navigate(['']);
      }
    });
  }
  
  getManagers():void{
    this.adminService.getManagers().then( response =>this.managers = response   );
  }

}