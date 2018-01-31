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
import { Ticket } from '../Model/Ticket';
import { Team } from '../Model/team';
import { ManagerService } from "../services/manager.service";
import { AdminService } from "../services/admin.service";
import { TeamDetailService } from "../services/team-detail.service";


@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.css']
})
export class TeamDetailComponent implements OnInit {
  username:string;
  teamName:string;
  operation:string;
  sub:any;
  employees :Employee[]=[];
  teams:Team[]=[];
  teamEmployees:Employee[]=[];
  constructor(private teamService:TeamDetailService,private managerService:ManagerService,private adminService:AdminService,private route: ActivatedRoute,private router:Router, private location: Location) { }
  
    ngOnInit() {
       
   
      this.sub = this.route.params.subscribe(params=>{

        this.teamName = params['teamName'];
        this.operation = params['operation'];
      });
      
      this.getEmployeesToAdd();
       this.teamService.getTeamsDetail(this.teamName).then(response =>    this.teamEmployees = response);
    }

  addToTeam(employee:Employee):void{
   this.teamService.addToTeam(employee,this.teamName).then(response => this.location.back());
  }
  getEmployeesToAdd():void{
    this.teamService.getEmployeesToAdd(this.teamName).then(response => {
      let index=0;
      for(let employee of response){
        if(employee.designation!='Manager'){
          this.employees[index++] = employee;
        }
    }
    
  });
    
  }
  goBack(): void {
    this.location.back();
  }
  
}


  
  
 