import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin/admin.service';
import { Employee } from '../Model/signEmp';
import { AdminComponent } from '../admin/admin.component';
import { ParamMap, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import { EmployeeDetailService } from './employee-detail.service';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.css']
})
export class EmployeeDetailComponent implements OnInit {
  employees:Employee[]=[];
  employee:Employee;
  sub:any;
  id:boolean;
  constructor(private employeeDetail:EmployeeDetailService, private route: ActivatedRoute) { }

  ngOnInit() {
     this.getEmployeeDetail();
    
  }
  getEmployeeDetail():void{
  

    this.sub = this.route.params.subscribe(params => {
      this.id = params['type']; 
     console.log(this.id);
     });
     console.log("After");
     console.log(this.id);
     this.getId();
  this.route.paramMap
  .switchMap((params: ParamMap) =>  this.employeeDetail.getEmployeeDetail(params.get('username')))
  .subscribe(employee => this.employee = employee);
  }
 
getId():void{
  console.log("in method");
  console.log(this.id);
}


}
