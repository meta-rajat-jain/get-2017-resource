import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '../home/home.component';
import { AdminComponent } from '../admin/admin.component';
import { EmployeeDetailComponent } from '../employee-detail/employee-detail.component';
import { ManagerComponent } from '../manager/manager.component';
import { TeamHeadComponent } from '../team-head/team-head.component';
import { MemberComponent } from '../member/member.component';
import { ActivateGuard } from '../router-guards/activateGuard';
import { ActivateGuardManager } from '../router-guards/activateGuardManager';
import { ActivateGuardMember } from '../router-guards/activateGuardMember';
import { RequestsComponent } from '../requests/requests.component';
import { NeedInformationComponent } from '../need-information/need-information.component';
import { TeamDetailComponent } from '../team-detail/team-detail.component';
import { PasswordComponent } from '../password/password.component';
import { HelpdeskComponent } from '../helpdesk/helpdesk.component';
import { ActivateGuardHelpDesk } from '../router-guards/activateGuardHelpdesk';







const routes: Routes = [
  { path: '', component:HomeComponent},
  { path: 'adminDashboard',  component: AdminComponent,canActivate:[ActivateGuard]},
  { path: 'employeeDetail/:username/:type', component: EmployeeDetailComponent },
  { path: 'managerDashboard', component: ManagerComponent,canActivate:[ActivateGuardManager] },
  { path: 'memberDashboard', component: MemberComponent,canActivate:[ActivateGuardMember]},
  { path: 'requestDetail/:status/:type', component:RequestsComponent },
  { path: 'needInformation/:ticketNo/:type/:operation', component: NeedInformationComponent },
  { path: 'teamDetailComponent/:teamName/:operation', component: TeamDetailComponent },
  { path: 'password', component:PasswordComponent},
  { path: 'helpDeskDashboard', component:HelpdeskComponent}

];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers:[ ActivateGuard,ActivateGuardManager,ActivateGuardMember,ActivateGuardHelpDesk]
})
export class AppRoutingModule {}
