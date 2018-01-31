import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Authentication } from "../Model/Authentication";
import { Employee } from "../Model/signEmp";
import { Team } from "../Model/team";
import { ManagerService } from "../services/manager.service";
import { AdminService } from "../services/admin.service";
import { Location } from '@angular/common';
import { FormBuilder,
  FormGroup,
  Validators,
  FormControl } from '@angular/forms';
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
  ) {
    this.reactiveForm = this.fb.group({
      'teamName'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('[A-Za-z]+ *[A-Za-z ]+$')])]
    });
    
  }

  ngOnInit() {
    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );
 
    let name = this.authenticationHeader.username.split("@");
    this.username = name[0];
   
  }
  createTeam(teamName: string, headName: string): void {

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

  checkEmployee(headName: string): void {
    this.adminService.getEmployees().then(response => {
      this.employees = response;
     for (let emp of this.employees) {

        if (headName == emp.login.username) {

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

