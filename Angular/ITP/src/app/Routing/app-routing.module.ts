import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from '../home/home.component';
import { AdminComponent } from '../admin/admin.component';
import { EmployeeDetailComponent } from '../employee-detail/employee-detail.component';
import { ManagerComponent } from '../manager/manager.component';
import { TeamHeadComponent } from '../team-head/team-head.component';
import { MemberComponent } from '../member/member.component';
import { NewRequestComponent } from '../new-request/new-request.component';
import { ActivateGuard } from '../router-guards/activateGuard';
import { ActivateGuardManager } from '../router-guards/activateGuardManager';
import { ActivateGuardMember } from '../router-guards/activateGuardMember';





const routes: Routes = [
  { path: '', component:HomeComponent},
  { path: 'adminDashboard',  component: AdminComponent,canActivate:[ActivateGuard]},
  { path: 'employeeDetail/:username/:type', component: EmployeeDetailComponent },
  
  { path: 'managerDashboard', component: ManagerComponent,canActivate:[ActivateGuardManager] },
  { path: 'teamHeadDashboard', component: TeamHeadComponent,canActivate:[ActivateGuardMember] },
  { path: 'memberDashboard', component: MemberComponent,canActivate:[ActivateGuardMember]},
  { path: 'newRequest/:username', component: NewRequestComponent },
  /*{ path: 'cart', component: Cart },
  { path: 'checkout', component: Checkout },
  { path: 'payment', component: PaymentComponent },
  { path: 'order', component:OrderComponent},
  { path: 'orderDetail/:orderId', component:OrderDetailComponent}*/

];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers:[ ActivateGuard,ActivateGuardManager,ActivateGuardMember]
})
export class AppRoutingModule {}
