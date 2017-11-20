import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Employee } from "../Model/signEmp";
import { Authentication } from "../Model/Authentication";
import { AdminService } from "../services/admin.service";
import { UserService } from "../services/user.service";
import { LogOutService } from "../services/logout.service";

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})





export class EmployeeComponent implements OnInit {
  username: string;
  autheticatedHeader: AuthenticatedHeader;
  employees:Employee[]=[];
  authentication: Authentication;
  employee:Employee;
  type:boolean=true;
  empType:boolean;
  title:string;
  p: number = 1;

  
  reverse: boolean = false;
  sort(){
  let  key: string = this.employee.name; 
    key = key;
    this.reverse = !this.reverse;
  }

  constructor(private adminService: AdminService,private router:Router,private logoutService: LogOutService,) {
  }

  ngOnInit() {
  
    this.autheticatedHeader = JSON.parse(localStorage.getItem('authenticationObject'));
    let name = this.autheticatedHeader.username.split('@');
    this.username = name[0];
    this.getAllEmployees();
    
  
  }
  logOut(): void {
    this.logoutService.logOut().then(response => {
      this.authentication = response;
      if (this.authentication.statusCode == 1){
        localStorage.removeItem('authenticationObject');
        localStorage.clear();
        this.router.navigate(['']);
      }
    });
  }

  getEmployees():Employee[]{
    return this.employees;
  }
  getAllEmployees():void{
    this.adminService.getEmployees().then( response =>{ this.employees = response ;
    } );
  }
  editEmployeeDetail(employee:Employee):void{
  this.type = false;
  this.getEmployeeDetail(employee);
  }
  deleteEmployee(employee:Employee):void{

    if ( confirm ('Are you sure you want to delete the employee : ' + employee.name )) {
    this.adminService.deleteEmployee(employee).then(response =>{ this.authentication = response;location.reload(true);});
    }
  }
  addManager(employee:Employee):void{
    this.adminService.addManager(employee).then(response=>
      { 
        this.authentication = response;
        this.router.navigate(['adminDashboard']);
        location.reload(true);
      });
  }
  getEmployeeDetail(employee:Employee):void{
    this.router.navigate(['/employeeDetail', employee.login.username,this.type ], { skipLocationChange: true });
  }


}