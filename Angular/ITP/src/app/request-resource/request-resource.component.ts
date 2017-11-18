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
    resource: RequestedResource,
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
        
       const url = this.type.toLowerCase() + 'Dashboard';
        console.log(response);
        console.log(url);
        
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
  goBack(): void {
    this.location.back();
  }
}
