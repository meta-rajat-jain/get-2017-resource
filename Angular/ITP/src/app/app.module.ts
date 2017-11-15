import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { HomeService } from './home/home.service';
import { AppRoutingModule } from './Routing/app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SocialLoginModule, AuthServiceConfig } from "angular4-social-login";
import { GoogleLoginProvider } from "angular4-social-login";
import { GoogleSignInComponent} from 'angular-google-signin';
import { AdminComponent } from './admin/admin.component';
import { AdminService } from './admin/admin.service';
import {  OrderListModule} from 'primeng/primeng';
import { FilterPipe } from './filter.pipe';
import { EmployeeDetailComponent } from './employee-detail/employee-detail.component';
import { EmployeeDetailService } from './employee-detail/employee-detail.service';
import { ManagerComponent } from './manager/manager.component';
import { MemberComponent } from './member/member.component';
import { TeamHeadComponent } from './team-head/team-head.component';
import { MemberService } from './member/member.service';
import { ActivateGuard } from './router-guards/activateGuard';
import { ManagerService } from './manager/manager.service';
import { RequestsService } from './requests/requests.service';
import { RequestsComponent } from './requests/requests.component';
import { NeedInformationComponent } from './need-information/need-information.component';
import { NeedInformationService } from './need-information/need-information.service';
import { TeamDetailComponent } from './team-detail/team-detail.component';
import { TeamDetailService } from './team-detail/team-detail.service';
import { HelpdeskComponent } from './helpdesk/helpdesk.component';
import { SelectModule } from 'ng2-select';
import {DropdownModule} from 'primeng/primeng';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { PasswordComponent } from './password/password.component';
import { PasswordService } from './password/password.service';
import { ActivateGuardHelpDesk } from './router-guards/activateGuardHelpdesk';
import { HelpdeskService } from './helpdesk/helpdesk.service';
let config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider('579262438220-gtpft4dkqeur6oe38c7t0ph8oeiqrmtl.apps.googleusercontent.com')
  }
]);
export function provideConfig() {
  return config;
}


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    GoogleSignInComponent,
    AdminComponent,
    FilterPipe,
    EmployeeDetailComponent,
    ManagerComponent,
    MemberComponent,
    TeamHeadComponent,
    RequestsComponent,
    NeedInformationComponent,
    TeamDetailComponent,
    HelpdeskComponent,
    PasswordComponent,

  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    ReactiveFormsModule,
    SocialLoginModule,
    DropdownModule   
  ],
  providers: [HomeService,AdminService,EmployeeDetailService,MemberService,ActivateGuard,ActivateGuardHelpDesk,HelpdeskService,ManagerService,RequestsService,NeedInformationService,TeamDetailService,PasswordService,
    {
    provide: AuthServiceConfig,
    useFactory: provideConfig
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
