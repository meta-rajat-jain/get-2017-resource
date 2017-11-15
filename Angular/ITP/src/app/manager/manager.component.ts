import { Component, OnInit,ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { ManagerService } from './manager.service';
import { Authentication } from '../Model/Authentication';
import { AdminService } from '../admin/admin.service';
import { Employee } from '../Model/signEmp';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { MemberService } from '../member/member.service';
import { RequestedResource } from '../Model/requestResource';
import { Team } from '../Model/team';
import { TicketStatusCount } from '../Model/ticketStatusCount';
import {SelectItem} from 'primeng/primeng'

@Component({
  selector: 'app-manager',
  templateUrl: './manager.component.html',
  styleUrls: ['./manager.component.css'],
})
export class ManagerComponent implements OnInit {
  username:string;
  authenticationHeader:AuthenticatedHeader;
  authentication:Authentication;
  employees:Employee[]=[];
  title:string;
  OpenCount:number;
  InProgressCount:number;
  ClosedCount:number;
  NeedInfoCount:number;
  ApprovedCount:number;

  teamOpenCount:number;
  teamInProgressCount:number;
  teamClosedCount:number;
  teamNeedInfoCount:number;
  teamApprovedCount:number;

  status:string;
  reactiveForm: FormGroup;
  resourceValues:RequestedResource[]=[];
  resourceNames:string[]=[];
  teamsUnderManager:Team[]=[];
  teamsOfEmployee:Team[]=[];
  selectedResource:RequestedResource;
  ticketCount:TicketStatusCount[]=[];
  ticketCountTeam:TicketStatusCount[]=[];
  selectedTeam:Team;
  loggedInUser:Employee;
  constructor(private managerService:ManagerService,private fb: FormBuilder, private router:Router,private adminService:AdminService,private memberService:MemberService) { }

  ngOnInit() {
    this.authenticationHeader=JSON.parse(localStorage.getItem('authenticationObject'));
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split('@');
    this.username = name[0];
    this.getCount();
    this.getTeamCount();
    this.memberService.getUserInformation().then(response=>{this.loggedInUser=response;});
    }
    getCount():void{
      this.memberService.getCounts().then(response => {
        this.ticketCount = response;
        console.log(this.ticketCount);
        console.log("length" );
        console.log( this.ticketCount.length);
        for(let ticket of this.ticketCount){
          if(ticket.status == 'Open'){
            console.log(ticket.count);
            this.OpenCount= ticket.count;
          }
          else if(ticket.status == 'InProgress'){
            console.log(ticket.count);
            this.InProgressCount= ticket.count;
          }
          else if(ticket.status == 'Closed'){
            console.log(ticket.count);
            this.ClosedCount= ticket.count;
          }
          else if(ticket.status == 'NeedInfo'){
            console.log(ticket.count);
            this.NeedInfoCount= ticket.count;
          }
          else if(ticket.status == 'Approved'){
            console.log(ticket.status + ticket.count);
            this.ApprovedCount= ticket.count;
          }
        }
      });
    }
    getTeamCount():void{
      this.memberService.getTeamCounts().then(response => {
        this.ticketCountTeam = response;
        console.log(this.ticketCountTeam);
        console.log("length" );
        console.log( this.ticketCountTeam.length);
        for(let ticket of this.ticketCountTeam){
          if(ticket.status == 'Open'){
            console.log(ticket.status + ticket.count);
            this.teamOpenCount= ticket.count;
          }
          else if(ticket.status == 'InProgress'){
            console.log(ticket.count);
            this.teamInProgressCount= ticket.count;
          }
          else if(ticket.status == 'Closed'){
            console.log(ticket.count);
            this.teamClosedCount= ticket.count;
          }
          else if(ticket.status == 'NeedInfo'){
            console.log(ticket.count);
            this.teamNeedInfoCount= ticket.count;
          }
          else if(ticket.status == 'Approved'){
            console.log(ticket.status +ticket.count);
            this.teamApprovedCount= ticket.count;
          }
        }
      });
    }
  createTeam(teamName:string,headName:string):void{
   
    this.managerService.createTeam(teamName,headName).then(response => {
    console.log("service response");
   
    });
   location.reload(true);
  }

  logOut(){
    this.managerService.logOut().then(response => {
      this.authentication = response;
      localStorage.clear();
      localStorage.removeItem('authenticationObject');
      this.router.navigate(['']);
      if (this.authentication.statusCode == 1){
        localStorage.clear();
        localStorage.removeItem('authenticationObject');
        this.router.navigate(['']);
      }
    });
  }
  checkEmployee(headName:string):void{
    this.adminService.getEmployees().then( response => {this.employees = response;
      
            for(let emp of this.employees){
              console.log("before if" + headName);
              console.log(emp.login.username);
              if(headName == emp.login.username){
                console.log(headName +"==" + emp.login.username);
                this.title='';
                break;
              }
              else{

                this.title='Invalid user';
              }
            }
          } )
  }
  getRequestsInProgress(type:string):void{
    console.log(type);
    this.status = 'Inprogress';
    this.router.navigate(['requestDetail',this.status,type]);
  }
  getRequestsOpen(type:string):void{
    this.status = 'Open';
    console.log(type);
    this.router.navigate(['requestDetail',this.status,type]);
  }
  getRequestsNeedInfo(type:string):void{
    this.status = 'NeedInfo';
    this.router.navigate(['requestDetail',this.status,type]);
  }
  getRequestsClosed(type:string):void{
    this.status = 'Closed';
    this.router.navigate(['requestDetail',this.status,type]);
  }
  getRequestsApproved(type:string):void{
    this.status = 'Approved';
    this.router.navigate(['requestDetail',this.status,type]);
  }
  resourceRequested(requestType:string,resourceType:string):void{
    console.log(requestType);
    console.log(resourceType);
  if(requestType=='New'){
    this.memberService.getResourceRequested(resourceType).subscribe(response => { console.log(response);this.resourceValues = response   ;  
    });
  }
  }

  getTeamsUnderManager():void{
    this.managerService.getTeamsUnderManager().then(response=>{this.teamsUnderManager=response;console.log(response);});
   
  }

  Request(requestedFor:string,priority:string,requestType:string,resourceType:string,resource:RequestedResource,comment:string,locn:string):void{
    let teamName = null;
    console.log("in comment" + comment);
    console.log("in location" + locn);
    this.memberService.makeRequest(this.authenticationHeader.username,requestedFor,priority,requestType,this.selectedResource,comment,locn,teamName).then(response =>{
      console.log(response);
      window.location.reload();
      });
  
  }

  manageTeam(team:Team,operation:string):void{
    console.log(team);
    console.log(operation);
  this.router.navigate(['/teamDetailComponent',team.teamName,operation]);
 }
}
