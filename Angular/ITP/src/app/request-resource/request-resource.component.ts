import { Component, OnInit } from '@angular/core';
import { MemberService } from "../services/member.service";
import { RequestedResource } from "../Model/requestResource";
import { Team } from "../Model/team";
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { ActivatedRoute, Router } from "@angular/router";
import { Employee } from "../Model/signEmp";
import { ManagerService } from "../services/manager.service";
import { Location } from "@angular/common";
import { RequestResourceService } from "../services/requestrsource.service";
import { NgForm } from '@angular/forms';
@Component({
  selector: "app-request-resource",
  templateUrl: "./request-resource.component.html",
  styleUrls: ["./request-resource.component.css"]
})
export class RequestResourceComponent implements OnInit {
  type: string;
  teamName: string;
  selectedTeam: Team;
  authenticationHeader: AuthenticatedHeader;
  resourceValues: RequestedResource[] = [];
  selectedResource: RequestedResource;
  sub: any;
  teamsOfEmployee: Team[];
  requestedEmployee: Employee[];
  title: string;
  username: string;
  emailId: string;
  requesterFor:string;
  isValidFormSubmitted = false;
  errorTitle:string;
  constructor(
    private memberService: MemberService,
    private route: ActivatedRoute,
    private managerService: ManagerService,
    private requestResourceService: RequestResourceService,
    private location: Location,
    private router:Router
  ) {}

  ngOnInit() {

    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );
    this.sub = this.route.params.subscribe(params => {
      this.type = params["type"];
      console.log(this.type);
    });
    let name = this.authenticationHeader.username.split("@");
    this.username = name[0];
    this.emailId = this.authenticationHeader.username;
  }
  resourceRequested(requestType: string, resourceType: string): void {
    console.log(requestType);
    console.log(resourceType);
    if (requestType == "New") {
      this.memberService
        .getResourceRequested(resourceType)
        .subscribe(response => {
          console.log(response);
          this.resourceValues = response;
        });
    }
    if (this.type === "Member") {
      this.memberService.getTeamsOfEmployee().then(response => {
        console.log(response);
        this.teamsOfEmployee = response;
      });
    }
  }
  Request(
    priority: string,
    requestType: string,
    resourceType: string,
    comment: string,
    locn: string
  ): void {
    if (this.type === "Manager" || this.teamsOfEmployee==null) {
      this.teamName = null;
    } else if (this.type === "Member") {
      this.teamName = this.selectedTeam.teamName;
    }
    if(this.type=='Member'){
      this.requesterFor = this.authenticationHeader.username;
    }
    console.log("in priority" + priority);
    console.log("in priority" + requestType);
    console.log("in priority" + resourceType);
    console.log("in comment" + comment);
    console.log("in location" + locn);
    console.log(
      this.authenticationHeader.username +
        "  " +
        this.requesterFor + 
        "  " +
        priority +
        "  " +
        requestType +
        "  " +
        this.selectedResource +
        "  " +
        comment +
        "  " +
        locn +
        "  " +
        this.teamName
    );
    this.memberService
      .makeRequest(
        this.authenticationHeader.username,
        this.requesterFor,
        priority,
        requestType,
        this.selectedResource,
        comment,
        locn,
        this.teamName
      )
      .then(response => {
        console.log(response);
      location.reload(true);
        
      });
  }
  canRequest(employeeName: string): void {
    this.managerService.canRequest().then(response => {
      this.requestedEmployee = response;

      for (let emp of this.requestedEmployee) {
        console.log("before if" + employeeName);
        console.log(emp.login.username);
        if (employeeName == emp.login.username) {
          console.log(employeeName + "==" + emp.login.username);
          this.title = "";
          break;
        } else {
          this.title = "Invalid user";
        }
      }
    });
  }
  onFormSubmit(form: NgForm) {
	  this.isValidFormSubmitted = false;
	  if(form.valid){
	    this.isValidFormSubmitted = true;
	  }  
	  let priority: string = form.form.controls['priority'].value;	
    let requestType:string = form.form.controls['requestType'].value;
    let resourceType:string = form.controls['resourceType'].value;
    let comment:string = form.form.controls['comment'].value;
    let locn:string = form.form.controls['locn'].value;
    console.log("in form" + form.form.controls['priority'].value);
    console.log(form.controls['priority'].value);
    console.log(form.form.controls['priority'].value);
    if(priority == '' || priority == null || priority == undefined){
      form.controls['priority'].setErrors({'incorrect': true});
      this.isValidFormSubmitted = false;
      this.errorTitle = "Please select a valid entry";
      return;
    }
    else{
      this.errorTitle = "";
      form.controls['priority'].setErrors(null);
    }
    if(requestType == '' || requestType == null ){
      form.controls['requestType'].setErrors({'incorrect': true});
      this.isValidFormSubmitted = false;
      this.errorTitle = "Please select a valid entry";
      return;
    }
    else{
      this.errorTitle = "";
      form.controls['requestType'].setErrors(null);
    }
    if(resourceType == '' || resourceType == null ){
      form.controls['resourceType'].setErrors({'incorrect': true});
      this.isValidFormSubmitted = false;
      this.errorTitle = "Please select a valid entry";
      return;
    }
    else{
      this.errorTitle = "";
      form.controls['resourceType'].setErrors(null);
    }
    
	  this.Request(priority,requestType,resourceType,comment,locn);
	 
	  
	  
	}
  goBack(): void {
    this.location.back();
  }
}
