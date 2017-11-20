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
import { Authentication } from "../Model/Authentication";
@Component({
  selector: "app-request-resource",
  templateUrl: "./request-resource.component.html",
  styleUrls: ["./request-resource.component.css"]
})
export class RequestResourceComponent implements OnInit {
  type: string;
  teamName: string;
  selectedTeam: Team;
  authenticationObject: Authentication;
  authenticationHeader: AuthenticatedHeader;
  resourceValues: RequestedResource[] = [];
  selectedResource: RequestedResource;
  sub: any;
  teamsOfEmployee: Team[];
  requestedEmployee: Employee[];
  selectedType:string = 'false';
  title: string;
  username: string;
  emailId: string;
  requesterFor: string;
  isValidFormSubmitted = false;
  errorTitle: string;
  successfulSubmission:boolean=false;
  constructor(
    private memberService: MemberService,
    private route: ActivatedRoute,
    private managerService: ManagerService,
    private requestResourceService: RequestResourceService,
    private location: Location,
    private router: Router
  ) {
   
  }

  ngOnInit() {
    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );
    this.sub = this.route.params.subscribe(params => {
      this.type = params["type"];
     
    });
    let name = this.authenticationHeader.username.split("@");
    this.username = name[0];
    this.emailId = this.authenticationHeader.username;
    if (this.type == "Member") {
  
      this.memberService.getTeamsOfEmployee().then(response => {
    
        this.teamsOfEmployee = response;
      });
    }
  }
  resourceRequested(requestType: string, resourceType: string): void {
   
    if (requestType == "New") {
      this.memberService
        .getResourceRequested(resourceType)
        .subscribe(response => {
          
          this.resourceValues = response;
        
        });
    }
    else{
     this.resourceValues = [];
    }
    if (this.type == "Member") {
      this.memberService.getTeamsOfEmployee().then(response => {
       
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
    if (this.type === "Manager" || this.teamsOfEmployee == null) {
      this.teamName = null;
    } else if (this.type === "Member") {
      this.teamName = this.selectedTeam.teamName;
    }
    if (this.type == "Member") {
      this.requesterFor = this.authenticationHeader.username;
    }
  
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
        this.authenticationObject =response;
        if(this.authenticationObject.statusCode == 1){
          confirm("Ticket is generated successfully");
          this.successfulSubmission=true;
          location.reload(true);
        }
      });
  }
  canRequest(employeeName: string): void {
    this.managerService.canRequest().then(response => {
      this.requestedEmployee = response;

      for (let emp of this.requestedEmployee) {
       
        if (employeeName == emp.login.username) {
         
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
    if (form.valid) {
      this.isValidFormSubmitted = true;
    }
    let priority: string = form.form.controls["priority"].value;
    let requestType: string = form.form.controls["requestType"].value;
    let resourceType: string = form.controls["resourceType"].value;
    let comment: string = form.form.controls["comment"].value;
    let locn: string = form.form.controls["locn"].value;
    
    if (priority == "" || priority == null || priority == undefined) {
      form.controls["priority"].setErrors({ incorrect: true });
      this.isValidFormSubmitted = false;
      this.errorTitle = "Please select a valid entry";
      return;
    } else {
      this.errorTitle = "";
      form.controls["priority"].setErrors(null);
    }
    if (requestType == "" || requestType == null) {
      form.controls["requestType"].setErrors({ incorrect: true });
      this.isValidFormSubmitted = false;
      this.errorTitle = "Please select a valid entry";
      return;
    } else {
      this.errorTitle = "";
      form.controls["requestType"].setErrors(null);
    }
    if (resourceType == "" || resourceType == null) {
      form.controls["resourceType"].setErrors({ incorrect: true });
      this.isValidFormSubmitted = false;
      this.errorTitle = "Please select a valid entry";
      return;
    } else {
      this.errorTitle = "";
      form.controls["resourceType"].setErrors(null);
    }

    this.Request(priority, requestType, resourceType, comment, locn);
  }
  goBack(): void {
    this.location.back();
  }
}
