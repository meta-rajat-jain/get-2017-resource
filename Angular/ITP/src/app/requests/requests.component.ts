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
declare var $;
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
  tickets :Ticket[];
  p: number = 1;
  constructor(private requestsService:RequestsService, private route: ActivatedRoute,private router:Router, private location: Location) { }

  ngOnInit() {

 
    this.sub = this.route.params.subscribe(params=>{

      this.type = params['type'];
      this.status = params['status'];
    });
    this.init();
    
      
  }
init():void{

 
  this.requestsService.getRequests(this.status,this.type).then(response => this.tickets = response);
}
  approve(ticket:Ticket,type:string):void{
 
    this.requestsService.approveTicket(ticket,type).then(response => {location.reload(true)});
  }
  needInfo(ticket:Ticket,operation:string):void{

    this.router.navigate(['needInformation',ticket.ticketNo,this.type,operation]);
  }
  decline(ticket:Ticket,type:string):void{
    this.requestsService.rejectTicket(ticket,type).then(response => {location.reload(true);});    
  }

  goBack(): void {
    this.location.back();
  }
  
}
