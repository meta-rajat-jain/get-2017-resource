import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { ManagerService } from './manager.service';
import { Authentication } from '../Model/Authentication';
import { AdminService } from '../admin/admin.service';
import { Employee } from '../Model/signEmp';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { MemberService } from '../member/member.service';
import { RequestedResource } from '../Model/requestResource';

@Component({
  selector: 'app-manager',
  templateUrl: './manager.component.html',
  styleUrls: ['./manager.component.css']
})
export class ManagerComponent implements OnInit {
  username:string;
  authenticationHeader:AuthenticatedHeader;
  authentication:Authentication;
  employees:Employee[]=[];
  title:string;
  openCount:number=1;
  InProgressCount:number=2;
  status:string;
  reactiveForm: FormGroup;
  resourceType:RequestedResource[]=[];
  resourceName:string[]=[];
  constructor(private managerService:ManagerService,private fb: FormBuilder, private router:Router,private adminService:AdminService,private memberService:MemberService) { }

  ngOnInit() {
    this.authenticationHeader=JSON.parse(localStorage.getItem('authenticationObject'));
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split('@');
    this.username = name[0];
    this.reactiveForm = this.fb.group({
      'requestedBy'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('[A-Za-z]+ *[A-Za-z ]+$')])],
      'requestedFor'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('^[A-Za-z]+ *[A-Za-z ]+$')])],
      })
  }

  createTeam(teamName:string,headName:string):void{
   
    this.managerService.createTeam(teamName,headName).then(response => {
    console.log("service response");
   
    });
   location.reload(true);
  }

  logOut(){
    this.managerService.logOutManager().then(response => {
      this.authentication = response;
      if (this.authentication.statusCode == 1){
        localStorage.clear();
        localStorage.removeItem('authenticationObject');
        this.router.navigate(['']);
      }
    });
  }
  checkEmployee(headName:string):void{
    this.adminService.getEmployees().then( response => {this.employees = response;
      
            for(let emp of this.employees){
              console.log("before if" + headName);
              console.log(emp.login.username);
              if(headName == emp.login.username){
                console.log(headName +"==" + emp.login.username);
                this.title='';
                break;
              }
              else{

                this.title='Invalid user';
              }
            }
          } )
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
    this.status = 'NeedInformation';
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
  resourceRequested(requestType:string,resourceType:string):void{
    console.log(requestType);
    console.log(resourceType);
  if(requestType=='New'){
    this.memberService.getResourceRequested(resourceType).then(response => { console.log(response);this.resourceType = response ;  
      let i=0;
    for(let resource of this.resourceType){
      
      this.resourceName[i++] = resource.resourceName;
    }
    
    });
  }
  }
  Request(requestedFor,priority,requestType,resourceType,resource,comment,location):void{
    this.memberService.makeRequest(this.authenticationHeader.username,requestedFor,priority,requestType,resourceType,resource,comment,location).then(response =>{console.log(response)});
  }
}
