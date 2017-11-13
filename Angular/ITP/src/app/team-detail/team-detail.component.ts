import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin/admin.service';
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
import { TeamDetailService } from './team-detail.service';
import { Team } from '../Model/team';
import { ManagerService } from '../manager/manager.service';

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
        console.log(params['teamName']);
        console.log(params['operation']);
        this.teamName = params['teamName'];
        this.operation = params['operation'];
      });
      this.init();
      this.adminService.getEmployees().then(response => { this.employees = response});
      this.teamService.getTeamsDetail(this.teamName).then(response =>  { this.teamEmployees = response});
    }
  init():void{
  
     console.log(this.teamName);
   
     console.log(this.operation);
  }
  addToTeam(employee:Employee):void{
   this.teamService.addToTeam(employee,this.teamName).then(response => {console.log(response)});
  }
  
}


  
  
 