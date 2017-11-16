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
import { RequestsService } from "../services/requests.service";

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.css']
})
export class RequestsComponent implements OnInit {
  username:string;
  type:string;
  status:string;
  sub:any;
  tickets :Ticket[]=[];
  constructor(private requestsService:RequestsService, private route: ActivatedRoute,private router:Router, private location: Location) { }

  ngOnInit() {
     
 
    this.sub = this.route.params.subscribe(params=>{
      console.log(params['type']);
      console.log(params['status']);
      this.type = params['type'];
      this.status = params['status'];
    });
    this.init();
  }
init():void{

   console.log(this.type);
 
   console.log(this.status);
 
  this.requestsService.getRequests(this.status,this.type).then(response => this.tickets = response);
}
  approve(ticket:Ticket,type:string):void{
   console.log(ticket,type);
    this.requestsService.approveTicket(ticket,type).then(response => {console.log(response);});
  }
  needInfo(ticket:Ticket,operation:string):void{
    console.log(ticket);
    console.log( operation);
    this.router.navigate(['needInformation',ticket.ticketNo,this.type,operation]);
  }
  decline(ticket:Ticket,type:string):void{
    this.requestsService.rejectTicket(ticket,type).then(response => {console.log(response);});    
  }

  goBack(): void {
    this.location.back();
  }
  
}
