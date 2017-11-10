import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NewRequestComponent } from '../new-request/new-request.component';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { MemberService } from './member.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Authentication } from '../Model/Authentication';
import { Ticket } from '../Model/Ticket';
import { RequestedResource } from '../Model/requestResource';

@Component({
  selector: 'app-member',
  templateUrl: './member.component.html',
  styleUrls: ['./member.component.css']
})
export class MemberComponent implements OnInit {
  username:string;
  emailId:string;
  authenticationHeader:AuthenticatedHeader;
  authentication:Authentication;
  reactiveForm: FormGroup;
  resourceType:RequestedResource[]=[];
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
  Request(requestedFor,priority,requestType,resourceType,resource,comment,location):void{
    
    
    this.memberService.makeRequest(this.authenticationHeader.username,requestedFor,priority,requestType,resourceType,resource,comment,location).then(response =>{console.log(response)});
    }

    logOut(){
      this.memberService.logOutMember().then(response => { 
         this.authentication = response;
         if (this.authentication.statusCode == 1){
           localStorage.clear();
           localStorage.removeItem('authenticationObject');
           this.router.navigate(['/']);
         }
       });
    }
  
    resourceRequested(requestType:string,resourceType:string):void{
      console.log(requestType);
      console.log(resourceType);
    if(requestType=='New'){
      this.memberService.getResourceRequested(resourceType).then(response => { console.log(response);this.resourceType = response });
    }
    }
}
