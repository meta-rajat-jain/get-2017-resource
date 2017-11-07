import { Component, OnInit } from '@angular/core';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { Employee } from '../Model/signEmp';
import { AdminService } from './admin.service';
import { Authentication } from '../Model/Authentication';
import { HomeService } from '../home/home.service';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';


@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  username: string;
  autheticatedHeader: AuthenticatedHeader;
  managers: Employee[]=[];
  employees:Employee[]=[];
  authentication: Authentication;
  reactiveForm: FormGroup;
  employee:Employee;
  type:boolean=true;

  constructor(private adminService: AdminService, private homeService: HomeService,private router:Router,private fb: FormBuilder) {
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
    console.log( JSON.parse(localStorage.getItem('authenticationObject')));
    console.log(JSON.parse(localStorage.getItem('authenticationObject')));

   this.getManagers();
    this.getAllEmployees();
  }
  logOut(): void {
    this.adminService.logOutUser().then(response => {
      console.log(this.authentication.statusCode);
      this.authentication = response;
      console.log(response);
      localStorage.clear();
      localStorage.removeItem('authenticationObject');
      if (this.authentication.statusCode == 1){
        localStorage.clear();
        this.router.navigate(['/']);
      }
    });
  }
  getManagers():void{
    console.log("getmanager is called");
    this.adminService.getManagers().then( response =>this.managers = response   );
  }
  getEmployees():Employee[]{
    return this.employees;
  }
  getAllEmployees():void{
    this.adminService.getEmployees().then( response =>{ this.employees = response  } );
  }
  editEmployeeDetail(employee:Employee):void{
   this.type = false;
  this.getEmployeeDetail(employee);
  }
  deleteEmployee(employee:Employee):void{
    console.log(employee);
    this.adminService.deleteEmployee(employee).then(response => this.authentication = response);
  }

  getDetails(employee:Employee):void{
    this.type=true;
    this.getEmployeeDetail(employee);
  }
  addManager(employee:Employee):void{
    this.adminService.addManager(employee).then(response=> this.authentication = response);
    location.reload(true);
  }




  getEmployeeDetail(employee:Employee):void{
    console.log(employee);
    console.log("getting type" + this.type);
this.router.navigate(['/employeeDetail', employee.login.username,this.type ]);
  }
}