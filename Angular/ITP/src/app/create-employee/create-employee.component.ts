import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Employee } from "../Model/signEmp";
import { Authentication } from "../Model/Authentication";
import { AdminService } from "../services/admin.service";
import { UserService } from "../services/user.service";
import { ManagerService } from "../services/manager.service";

@Component({
  selector: 'app-create-employee',
  templateUrl: './create-employee.component.html',
  styleUrls: ['./create-employee.component.css']
})
export class CreateEmployeeComponent implements OnInit {
  username: string;
  autheticatedHeader: AuthenticatedHeader;
  authentication: Authentication;
  reactiveForm: FormGroup;
  employee:Employee;
  type:boolean=true;
  empType:boolean;
  title:string;
  constructor(private adminService: AdminService, private userService: UserService,private router:Router,private fb: FormBuilder,private managerService:ManagerService) {
    this.reactiveForm = this.fb.group({
    'empName'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('[A-Za-z]+ *[A-Za-z ]+$')])],
    'empEmail'  : [null,Validators.compose([Validators.required,Validators.minLength(1)])],
    'empPassword'  : [null,Validators.compose([Validators.required,Validators.minLength(8)])],
    'empContact'  : [null,Validators.compose([Validators.required,Validators.minLength(10),Validators.maxLength(10),Validators.pattern('^[7-9][0-9]{9}$')])],
    })
    
  }

  ngOnInit() {
    this.autheticatedHeader = JSON.parse(localStorage.getItem('authenticationObject'));
    let name = this.autheticatedHeader.username.split('@');
    this.username = name[0];

  }
  logOut(): void {
    this.managerService.logOut().then(response => {
      this.authentication = response;
      if (this.authentication.statusCode == 1){
        localStorage.removeItem('authenticationObject');
        localStorage.clear();
        this.router.navigate(['']);
      }
    });
  }

  registerEmployee(name:string,username:string,password:string,contactNo:number){
    let input = username.split("@");
    console.log(username);
   console.log(input[0]);
    let domain = input[1];
    console.log("in domain" + domain);
    this.userService.signUp(name,password,username,contactNo,domain).then(
      response =>{ this.authentication = response;
                   if(this.authentication.statusCode ==1){
                     this.router.navigate(['adminDashboard']);
                     location.reload(true);
                   }                     });
  }

  checkDomain(email:string){
    let domain=this.autheticatedHeader.username.split('@')[1];
    let userDomain=email.split('@')[1];
    if(domain==userDomain){
      this.title='';
    }
    else{
      this.title='Specify correct domain name';
    }
  }
}