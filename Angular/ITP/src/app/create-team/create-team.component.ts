import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Authentication } from "../Model/Authentication";
import { Employee } from "../Model/signEmp";
import { FormGroup, Validators, FormBuilder } from "@angular/forms";
import { Team } from "../Model/team";
import { ManagerService } from "../services/manager.service";
import { AdminService } from "../services/admin.service";
import { Location } from '@angular/common';

@Component({
  selector: 'app-create-team',
  templateUrl: './create-team.component.html',
  styleUrls: ['./create-team.component.css']
})
export class CreateTeamComponent implements OnInit {
  username: string;
  authenticationHeader: AuthenticatedHeader;
  authentication: Authentication;
  employees: Employee[] = [];
  title: string;
  teamMessage:string='';
  reactiveForm: FormGroup;
  constructor(
    private managerService: ManagerService,
    private fb: FormBuilder,
    private router: Router,
    private adminService: AdminService,
    private location:Location
  ) {}

  ngOnInit() {
    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split("@");
    this.username = name[0];
   
  }
  createTeam(teamName: string, headName: string): void {
    console.log(teamName + headName);
    if(headName === ""){
      headName = null;
    }
    this.managerService.createTeam(teamName, headName).then(response => {
      this.authentication = response;
      
      if(this.authentication.statusCode == 0){
        this.teamMessage = this.authentication.message;
      }
      else{
        this.teamMessage = this.authentication.message;
        
       
      }
     
    });
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
  checkEmployee(headName: string): void {
    this.adminService.getEmployees().then(response => {
      this.employees = response;
     for (let emp of this.employees) {
        console.log("before if" + headName);
        console.log(emp.login.username);
        if (headName == emp.login.username) {
          console.log(headName + "==" + emp.login.username);
          this.title = "";
          break;
        } else {
          this.title = "Invalid user";
        }
      }
    });
  }
  goBack(): void {
    this.location.back();
  }
  
}

