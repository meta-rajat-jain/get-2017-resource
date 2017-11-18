import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { Authentication } from "../Model/Authentication";
import { Employee } from "../Model/signEmp";
import { Team } from "../Model/team";
import { ManagerService } from "../services/manager.service";
import { Location } from '@angular/common';
@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})

export class TeamComponent implements OnInit {
  username: string;
  authenticationHeader: AuthenticatedHeader;
  authentication: Authentication;
  employees: Employee[] = [];
  title: string;
  status: string;
  teamsUnderManager: Team[] = [];
  selectedTeam: Team;
  requestedEmployee: Employee[] = [];
  constructor(
    private managerService: ManagerService,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit() {
    this.authenticationHeader = JSON.parse(
      localStorage.getItem("authenticationObject")
    );
    console.log(this.authenticationHeader.username);
    let name = this.authenticationHeader.username.split("@");
    this.username = name[0];
    this.getTeamsUnderManager();
  }
  
  logOut() {
    this.managerService.logOut().then(response => {
      this.authentication = response;
      if (this.authentication.statusCode == 1) {
        localStorage.clear();
        localStorage.removeItem("authenticationObject");
        this.router.navigate([""]);
      }
    });
  }
  
  getTeamsUnderManager(): void {
    this.managerService.getTeamsUnderManager().then(response => {
      this.teamsUnderManager = response;
      console.log(response);
    });
  }

  manageTeam(team: Team, operation: string): void {
    console.log(team);
    console.log(operation);
    this.router.navigate(["/teamDetailComponent", team.teamName, operation]);
  }
  goBack(): void {
    this.location.back();
  }
  
}

