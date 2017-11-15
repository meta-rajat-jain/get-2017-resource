import { Component, OnInit } from '@angular/core';
import { Ticket } from '../Model/Ticket';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { NeedInformationService } from './need-information.service';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { MemberService } from '../member/member.service';
import { Location } from '@angular/common';
@Component({
  selector: 'app-need-information',
  templateUrl: './need-information.component.html',
  styleUrls: ['./need-information.component.css']
})
export class NeedInformationComponent implements OnInit {
  sub:any;
  type:string;
  ticketNo:number;
  ticket:Ticket;
  operation:string;
  authenticationHeader:AuthenticatedHeader;
  constructor(private needInformationService: NeedInformationService, private route: ActivatedRoute,private router:Router,private memberService:MemberService,private location: Location) { }

  ngOnInit() {

     this.sub = this.route.params.subscribe(params => {
     this.ticketNo = params['ticketNo']; 
     this.type = params['type']; 
     this.operation = params['operation']; 
     console.log(this.ticketNo);
     this.init();
     });
     
 
   
    }
    init():void{
      console.log("it is called");
      this.needInformationService.getTicket(this.ticketNo).then(response => {console.log(response);this.ticket = response});
 }
 editRequest(priority,comment,location,status):void{
  this.needInformationService.updateRequest(this.ticket.ticketNo,this.ticket.requesterName,this.ticket.requestedFor,priority,this.ticket.requestType,this.ticket.requestedResource,comment,location,this.ticket.teamName,status).then(response =>{console.log(response);this.location.back();});
  }
  NeedInformation(comment,status):void{
    this.needInformationService.needInfoRequest(this.ticket.ticketNo,this.ticket.requesterName,this.ticket.requestedFor,this.ticket.priority,this.ticket.requestType,this.ticket.requestedResource,comment,this.ticket.seatLocation,this.ticket.teamName).then(response =>{console.log(response);this.location.back();});
    
  }
}