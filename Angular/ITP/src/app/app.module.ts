import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpModule } from "@angular/http";
import { AppComponent } from "./app.component";
import { HomeComponent } from "./home/home.component";
import { AppRoutingModule } from "./Routing/app-routing.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { SocialLoginModule, AuthServiceConfig } from "angular4-social-login";
import { GoogleLoginProvider } from "angular4-social-login";
import { GoogleSignInComponent } from "angular-google-signin";
import { AdminComponent } from "./admin/admin.component";
import { OrderListModule } from "primeng/primeng";
import { FilterPipe } from "./filter.pipe";
import { EmployeeDetailComponent } from "./employee-detail/employee-detail.component";
import { ManagerComponent } from "./manager/manager.component";
import { MemberComponent } from "./member/member.component";
import { TeamHeadComponent } from "./team-head/team-head.component";
import { RequestsComponent } from "./requests/requests.component";
import { NeedInformationComponent } from "./need-information/need-information.component";
import { TeamDetailComponent } from "./team-detail/team-detail.component";
import { HelpdeskComponent } from "./helpdesk/helpdesk.component";
import { SelectModule } from "ng2-select";
import { DropdownModule } from "primeng/primeng";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { PasswordComponent } from "./password/password.component";
import { UserService } from "./services/user.service";
import { AdminService } from "./services/admin.service";
import { EmployeeDetailService } from "./services/employee-detail.service";
import { MemberService } from "./services/member.service";
import { ActivateGuard } from "./services/router-guards/activateGuard";
import { ActivateGuardHelpDesk } from "./services/router-guards/activateGuardHelpdesk";
import { HelpdeskService } from "./services/helpdesk.service";
import { ManagerService } from "./services/manager.service";
import { RequestsService } from "./services/requests.service";
import { NeedInformationService } from "./services/need-information.service";
import { PasswordService } from "./services/password.service";
import { TeamDetailService } from "./services/team-detail.service";
import { NotFoundComponent } from './not-found/not-found.component';
import { EmployeeComponent } from './employee/employee.component';
import { CreateEmployeeComponent } from './create-employee/create-employee.component';
import { TeamComponent } from './team/team.component';
import { CreateTeamComponent } from './create-team/create-team.component';
import { RequestResourceComponent } from './request-resource/request-resource.component';
import { RequestResourceService } from "./services/requestrsource.service";
import { HttpClient } from "./services/httpClient";
import { SignupComponent } from './signup/signup.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { RequestConstants } from "./Constants/request";
import {NgxPaginationModule} from 'ngx-pagination';
import { Ng2OrderModule } from 'ng2-order-pipe';
import { LogOutService } from "./services/logout.service";


let config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider(
      RequestConstants.GoogleId
    )
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
    NotFoundComponent,
    EmployeeComponent,
    CreateEmployeeComponent,
    TeamComponent,
    CreateTeamComponent,
    RequestResourceComponent,
    SignupComponent,
    LoginComponent,
    NavbarComponent
    
  ],
  imports: [
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    ReactiveFormsModule,
    SocialLoginModule,
    DropdownModule,
    NgxPaginationModule,
    Ng2OrderModule
  ],
  providers: [
    UserService,
    AdminService,
    EmployeeDetailService,
    MemberService,
    ActivateGuard,
    ActivateGuardHelpDesk,
    HelpdeskService,
    ManagerService,
    RequestsService,
    NeedInformationService,
    TeamDetailService,
    PasswordService,
    RequestResourceService,
    HttpClient,
    LogOutService,
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
