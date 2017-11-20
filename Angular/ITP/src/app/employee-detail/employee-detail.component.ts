import { Component, OnInit } from '@angular/core';
import { Employee } from '../Model/signEmp';
import { AdminComponent } from '../admin/admin.component';
import { ParamMap, ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import { Login } from '../Model/login';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { EmployeeDetailService } from "../services/employee-detail.service";
@Component({
  selector: "app-employee-detail",
  templateUrl: "./employee-detail.component.html",
  styleUrls: ["./employee-detail.component.css"]
})
export class EmployeeDetailComponent implements OnInit {
  employees: Employee[] = [];
  employee: Employee;
  sub: any;
  id: boolean;
  authenticationHeader: AuthenticatedHeader;
  username: string;
  reactiveForm: FormGroup;
  name: string;
  constructor(
    private employeeDetail: EmployeeDetailService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit() {
   
    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );

    this.name = this.authenticationHeader.username;
    this.getEmployeeDetail();
    this.reactiveForm = this.fb.group({
      empName: [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(1),
          Validators.pattern("[A-Za-z]+ *[A-Za-z ]+$")
        ])
      ],
      empContact: [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10),
          Validators.pattern("^[7-9][0-9]{9}$")
        ])
      ]
    });
  }
  getEmployeeDetail(): void {
    this.sub = this.route.params.subscribe(params => {
      this.id = params["type"];

    });

    this.getId();
    this.route.paramMap
      .switchMap((params: ParamMap) =>
        this.employeeDetail.getEmployeeDetail(params.get("username"))
      )
      .subscribe(employee => (this.employee = employee));

    this.sub = this.route.params.subscribe(params => {
      this.username = params["username"];
  
    });
  }

  getId(): void {
 
  }
  updateEmployee(name: string, contactNo: number) {
   
    this.employeeDetail
      .updateEmployee(name, contactNo, this.employee, this.username)
      .then(response => {
       
        this.router.navigate(["/adminDashboard"]);
      });
  }
  goBack(): void {
    this.location.back();
  }
}
