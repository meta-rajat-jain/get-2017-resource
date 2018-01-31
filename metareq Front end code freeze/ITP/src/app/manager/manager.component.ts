import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Authentication } from "../Model/Authentication";
import { Employee } from "../Model/signEmp";
import { FormGroup, Validators, FormBuilder } from "@angular/forms";
import { RequestedResource } from "../Model/requestResource";
import { Team } from "../Model/team";
import { TicketStatusCount } from "../Model/ticketStatusCount";
import { SelectItem } from "primeng/primeng";
import { ManagerService } from "../services/manager.service";
import { AdminService } from "../services/admin.service";
import { MemberService } from "../services/member.service";

@Component({
  selector: "app-manager",
  templateUrl: "./manager.component.html",
  styleUrls: ["./manager.component.css"]
})
export class ManagerComponent implements OnInit {
  username: string ='';
  authenticationHeader: AuthenticatedHeader;
  authentication: Authentication;
  employees: Employee[] = [];
  title: string;
  OpenCount: number=0;
  InProgressCount: number=0;
  ClosedCount: number=0;
  NeedInfoCount: number=0;
  ApprovedCount: number=0;

  teamOpenCount: number=0;
  teamInProgressCount: number=0;
  teamClosedCount: number=0;
  teamNeedInfoCount: number=0;
  teamApprovedCount: number=0;

  status: string;
  reactiveForm: FormGroup;
  resourceValues: RequestedResource[] = [];
  resourceNames: string[] = [];
  teamsUnderManager: Team[] = [];
  teamsOfEmployee: Team[] = [];
  selectedResource: RequestedResource;
  ticketCount: TicketStatusCount[] = [];
  ticketCountTeam: TicketStatusCount[] = [];
  selectedTeam: Team;
  loggedInUser: Employee;
  requestedEmployee: Employee[] = [];
  constructor(
    private managerService: ManagerService,
    private fb: FormBuilder,
    private router: Router,
    private adminService: AdminService,
    private memberService: MemberService
  ) {}

  ngOnInit() {

    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );
 
    let name = this.authenticationHeader.username.split("@");
    this.username = name[0];
    this.getCount();
    this.getTeamCount();
    this.memberService.getUserInformation().then(response => {
      this.loggedInUser = response;
    });
  }
  getCount(): void {
    this.memberService.getCounts().then(response => {
      this.ticketCount = response;

      for (let ticket of this.ticketCount) {
        if (ticket.status == "Open") {
       
          this.OpenCount = ticket.count;
        } else if (ticket.status == "InProgress") {
        
          this.InProgressCount = ticket.count;
        } else if (ticket.status == "Closed") {
       
          this.ClosedCount = ticket.count;
        } else if (ticket.status == "NeedInfo") {
          
          this.NeedInfoCount = ticket.count;
        } else if (ticket.status == "Approved") {
          
          this.ApprovedCount = ticket.count;
        }
      }
    });
  }
  getTeamCount(): void {
    this.memberService.getTeamCounts().then(response => {
      this.ticketCountTeam = response;

      for (let ticket of this.ticketCountTeam) {
        if (ticket.status == "Open") {
         
          this.teamOpenCount = ticket.count;
        } else if (ticket.status == "InProgress") {
         
          this.teamInProgressCount = ticket.count;
        } else if (ticket.status == "Closed") {
         
          this.teamClosedCount = ticket.count;
        } else if (ticket.status == "NeedInfo") {
         
          this.teamNeedInfoCount = ticket.count;
        } else if (ticket.status == "Approved") {
         
          this.teamApprovedCount = ticket.count;
        }
      }
    });
  }


  getRequestsInProgress(type: string): void {
  
    this.status = "Inprogress";
    this.router.navigate(["requestDetail", this.status, type]);
  }
  getRequestsOpen(type: string): void {
    this.status = "Open";
  
    this.router.navigate(["requestDetail", this.status, type]);
  }
  getRequestsNeedInfo(type: string): void {
    this.status = "NeedInfo";
    this.router.navigate(["requestDetail", this.status, type]);
  }
  getRequestsClosed(type: string): void {
    this.status = "Closed";
    this.router.navigate(["requestDetail", this.status, type]);
  }
  getRequestsApproved(type: string): void {
    this.status = "Approved";
    this.router.navigate(["requestDetail", this.status, type]);
  }
  goToRequestTab(type:string):void{
    this.router.navigate(['requestResource',type]); 
  }

 
}
