import { Component, OnInit } from '@angular/core';
import { TicketStatusCount } from '../Model/ticketStatusCount';
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Authentication } from "../Model/Authentication";
import { HelpdeskService } from "../services/helpdesk.service";
import { Router } from "@angular/router";
import { ManagerService } from "../services/manager.service";


@Component({
  selector: 'app-helpdesk',
  templateUrl: './helpdesk.component.html',
  styleUrls: ['./helpdesk.component.css']
})
export class HelpdeskComponent implements OnInit {
  title:string;
  OpenCount:number;
  InProgressCount:number;
  ClosedCount:number;
  NeedInfoCount:number;
  ApprovedCount:number;
  ticketCount:TicketStatusCount[]=[];
  status:string;
  type:string;
  username:string;
  authenticationHeader:AuthenticatedHeader;
  domain:string;
  emailId:string;
  authentication:Authentication;
  constructor(private helpDeskService:HelpdeskService,private router:Router,private managerService:ManagerService) { }

  ngOnInit() {
    this.getCount();

    this.authenticationHeader=JSON.parse(localStorage.getItem('authenticationObject'));
    this.emailId = this.authenticationHeader.username;
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split('@');
    this.username = name[0];
    this.username = this.username.toUpperCase();
      this.domain = name[1];
  }
  getCount():void{
    this.helpDeskService.getCounts().then(response => {
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
      }
    });
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
  logOut(){
    this.managerService.logOut().then(response => {
      this.authentication = response;
      
      if (this.authentication.statusCode == 1){
        localStorage.clear();
        localStorage.removeItem('authenticationObject');
        this.router.navigate(['']);
      }
    });
  }
}
