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
import {OrderListModule} from 'primeng/primeng';
import { FilterPipe } from './filter.pipe';
import { EmployeeDetailComponent } from './employee-detail/employee-detail.component';
import { EmployeeDetailService } from './employee-detail/employee-detail.service';
import { ManagerComponent } from './manager/manager.component';
import { MemberComponent } from './member/member.component';
import { NewRequestComponent } from './new-request/new-request.component';
import { TeamHeadComponent } from './team-head/team-head.component';
import { RequestService } from './new-request/new-request.service';
import { MemberService } from './member/member.service';
import { ActivateGuard } from './router-guards/activateGuard';

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
    NewRequestComponent,
    TeamHeadComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    ReactiveFormsModule,
    SocialLoginModule,
    OrderListModule
    
  ],
  providers: [HomeService,AdminService,EmployeeDetailService,MemberService,ActivateGuard,
    {
    provide: AuthServiceConfig,
    useFactory: provideConfig
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
