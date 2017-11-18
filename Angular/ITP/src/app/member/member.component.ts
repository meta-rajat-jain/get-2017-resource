import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Authentication } from '../Model/Authentication';
import { Ticket } from '../Model/Ticket';
import { RequestedResource } from '../Model/requestResource';
import { Team } from '../Model/team';
import { TicketStatusCount } from '../Model/ticketStatusCount';
import { Employee } from '../Model/signEmp';
import { MemberService } from "../services/member.service";

@Component({
  selector: 'app-member',
  templateUrl: './member.component.html',
  styleUrls: ['./member.component.css']
})
export class MemberComponent implements OnInit {
  username:string='';
  emailId:string;
  authenticationHeader:AuthenticatedHeader;
  authentication:Authentication;
  selectedResource:RequestedResource;
  selectedTeam:Team;
  resourceValues:RequestedResource[]=[];
  teamsOfEmployee:Team[]=[];
  status:string;
  OpenCount:number;
  InProgressCount:number;
  ClosedCount:number;
  NeedInfoCount:number;
  ApprovedCount:number;
  ticketCount:TicketStatusCount[]=[];
  loggedInUser:Employee;
  constructor(private fb: FormBuilder,private router:Router,private memberService:MemberService) { }

  ngOnInit() {
 
    this.getCount();
    this.memberService.getUserInformation().then(response=>{this.loggedInUser=response;});
    this.authenticationHeader=JSON.parse(localStorage.getItem('authenticationObject'));
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split('@');
    this.username = name[0];
    this.emailId = this.authenticationHeader.username;
    
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
            console.log(ticket.count);
            this.ApprovedCount= ticket.count;
          }
          else{
            console.log(ticket.status);
          }
        }
      });
    }
  Request(priority,requestType,resourceType,resource,team,comment,location):void{
    this.memberService.makeRequest(this.authenticationHeader.username,this.authenticationHeader.username,priority,requestType,this.selectedResource,comment,location,this.selectedTeam.teamName).then(response =>{console.log(response);window.location.reload();});
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
 
}
