import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { RequestResource } from '../Model/request';
import { RequestService } from './new-request.service';
import { ActivatedRoute } from '@angular/router';

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
  let request : RequestResource={
    ticketNo:0,
    requestedBy:this.username,
    requestFor:requestedFor,
    priority:priority,
    resourceRequested:resource,
    comment:comment,
    location:location
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
