import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NewRequestComponent } from '../new-request/new-request.component';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { RequestResource } from '../Model/request';
import { MemberService } from './member.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-member',
  templateUrl: './member.component.html',
  styleUrls: ['./member.component.css']
})
export class MemberComponent implements OnInit {
  username:string;
  emailId:string;
  authenticationHeader:AuthenticatedHeader;
  reactiveForm: FormGroup;
  constructor(private fb: FormBuilder,private router:Router,private memberService:MemberService) { }

  ngOnInit() {
    this.authenticationHeader=JSON.parse(localStorage.getItem('authenticationObject'));
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split('@');
    this.username = name[0];
    this.emailId = this.authenticationHeader.username;
    this.reactiveForm = this.fb.group({
      'requestedBy'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('[A-Za-z]+ *[A-Za-z ]+$')])],
      'requestedFor'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('^[A-Za-z]+ *[A-Za-z ]+$')])],
      })
    }
  

  Request(requester,requestedFor,priority,resource,comment,location):void{
    let request : RequestResource={
      ticketNo:0,
      requestedBy:this.authenticationHeader.username,
      requestFor:requestedFor,
      priority:priority,
      resourceRequested:resource,
      comment:comment,
      location:location
    }  
    console.log(request);
    this.memberService.makeRequest(request);
    }

}
