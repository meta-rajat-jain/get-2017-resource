import { Component, OnInit } from '@angular/core';
import { Ticket } from '../Model/Ticket';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { Location } from '@angular/common';
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { NeedInformationService } from "../services/need-information.service";
import { MemberService } from "../services/member.service";
@Component({
  selector: "app-need-information",
  templateUrl: "./need-information.component.html",
  styleUrls: ["./need-information.component.css"]
})
export class NeedInformationComponent implements OnInit {
  sub: any;
  type: string;
  ticketNo: number;
  ticket: Ticket;
  operation: string;
  authenticationHeader: AuthenticatedHeader;
  requestType:string;
  comment:string;
  locn:string;
  constructor(
    private needInformationService: NeedInformationService,
    private route: ActivatedRoute,
    private router: Router,
    private memberService: MemberService,
    private location: Location
  ) {}

  ngOnInit() {

    this.sub = this.route.params.subscribe(params => {
      this.ticketNo = params["ticketNo"];
      this.type = params["type"];
      this.operation = params["operation"];
 
      this.init();
    });
  }
  init(): void {
   
    this.needInformationService.getTicket(this.ticketNo).then(response => {
    
      this.ticket = response;
      this.requestType = this.ticket.requestType;

    });
  }
  editRequest(priority, comment, location, status): void {
    this.needInformationService
      .updateRequest(
        this.ticket.ticketNo,
        this.ticket.requesterName,
        this.ticket.requestedFor,
        priority,
        this.ticket.requestType,
        this.ticket.requestedResource,
        comment,
        location,
        this.ticket.teamName,
        status,
        this.ticket.requestDate
      )
      .then(response => {
      
        this.location.back();
      });
  }
  NeedInformation(comment, status): void {
    this.needInformationService
      .needInfoRequest(
        this.ticket.ticketNo,
        this.ticket.requesterName,
        this.ticket.requestedFor,
        this.ticket.priority,
        this.ticket.requestType,
        this.ticket.requestedResource,
        comment,
        this.ticket.seatLocation,
        this.ticket.teamName,
        this.ticket.requestDate
      )
      .then(response => {
       
        this.location.back();
      });
  }
  goBack(): void {
    this.location.back();
  }
}