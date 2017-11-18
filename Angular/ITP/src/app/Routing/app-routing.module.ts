import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '../home/home.component';
import { AdminComponent } from '../admin/admin.component';
import { EmployeeDetailComponent } from '../employee-detail/employee-detail.component';
import { ManagerComponent } from '../manager/manager.component';
import { TeamHeadComponent } from '../team-head/team-head.component';
import { MemberComponent } from '../member/member.component';
import { ActivateGuardManager } from '../services/router-guards/activateGuardManager';
import { ActivateGuardMember } from '../services/router-guards/activateGuardMember';
import { RequestsComponent } from '../requests/requests.component';
import { NeedInformationComponent } from '../need-information/need-information.component';
import { TeamDetailComponent } from '../team-detail/team-detail.component';
import { PasswordComponent } from '../password/password.component';
import { HelpdeskComponent } from '../helpdesk/helpdesk.component';
import { ActivateGuardHelpDesk } from '../services/router-guards/activateGuardHelpdesk';
import { ActivateGuard } from '../services/router-guards/activateGuard';
import { NotFoundComponent } from '../not-found/not-found.component';
import { EmployeeComponent } from '../employee/employee.component';
import { CreateEmployeeComponent } from '../create-employee/create-employee.component';
import { CreateTeamComponent } from "../create-team/create-team.component";
import { TeamComponent } from "../team/team.component";
import { RequestResourceComponent } from "../request-resource/request-resource.component";
import { SignupComponent } from '../signup/signup.component';
import { LoginComponent } from '../login/login.component';





const routes: Routes = [
  { path: '', component:HomeComponent},
  { path: 'adminDashboard',  component: AdminComponent,canActivate:[ActivateGuard]},
  { path: 'employeeDetail/:username/:type', component: EmployeeDetailComponent },
  { path: 'managerDashboard', component: ManagerComponent,canActivate:[ActivateGuardManager] },
  { path: 'memberDashboard', component: MemberComponent,canActivate:[ActivateGuardMember]},
  { path: 'requestDetail/:status/:type', component:RequestsComponent },
  { path: 'needInformation/:ticketNo/:type/:operation', component: NeedInformationComponent },
  { path: 'teamDetailComponent/:teamName/:operation', component: TeamDetailComponent },
  { path: 'password', component:PasswordComponent },
  { path: 'helpDeskDashboard', component:HelpdeskComponent },
  { path: 'employee',component:EmployeeComponent,canActivate:[ActivateGuard]},
  { path: 'createEmployee',component:CreateEmployeeComponent,canActivate:[ActivateGuard] },
  { path: 'createTeam',component:CreateTeamComponent, canActivate:[ActivateGuardManager] },
  { path: 'teamUnderManager',component:TeamComponent,canActivate:[ActivateGuardManager] },
  { path: 'requestResource/:type',component:RequestResourceComponent },
  { path: 'signup',component:SignupComponent },
  { path: 'login',component:LoginComponent },
  { path: '**', component: NotFoundComponent },


];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers:[ ActivateGuard,ActivateGuardManager,ActivateGuardMember,ActivateGuardHelpDesk]
})
export class AppRoutingModule {}