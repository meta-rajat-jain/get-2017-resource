import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

import { RequestService } from './new-request.service';
import { ActivatedRoute } from '@angular/router';
import { Ticket } from '../Model/Ticket';
import { RequestedResource } from '../Model/requestResource';

@Component({
  selector: 'app-new-request',
  templateUrl: './new-request.component.html',
  styleUrls: ['./new-request.component.css']
})
export class NewRequestComponent implements OnInit {
  Title:string;
  resourceType:string;
  resources:string[]=[];
  sub:any;
  username:string;
  reactiveForm: FormGroup;
  constructor(private fb: FormBuilder,private requestService:RequestService, private route: ActivatedRoute) {
    this.reactiveForm = this.fb.group({
    'requester'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('[A-Za-z]+ *[A-Za-z ]+$')])],
    'requestedFor'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('^[A-Za-z]+ *[A-Za-z ]+$')])],
    })
  }

  ngOnInit() {
    this.getUsername();
  }
  Request(requester,requestedFor,priority,resource,comment,location):void{
    let requestedResource:RequestedResource={
      resourceId:0,
      resourceName:resource,
      resourceCategoryName:''
    }
  let request : Ticket={
    ticketNo:0,
    requesterName:requester,
    requestedFor:requestedFor,
    priority:priority,
    comment:comment,
    requestedResource:requestedResource,
    teamName: '',
    location:location,
    requestType:'',
    status:''
    }  
  console.log(request);
  this.requestService.makeRequest(request);
  }
  getUsername():void{
    this.sub = this.route.params.subscribe(params => {
      this.username = params['username']; 
     console.log(this.username);
     });
  }
}
