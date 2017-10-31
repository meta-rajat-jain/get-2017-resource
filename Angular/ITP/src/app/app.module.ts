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
import {GoogleSignInComponent} from 'angular-google-signin';
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
    GoogleSignInComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    ReactiveFormsModule,
    SocialLoginModule
    
  ],
  providers: [HomeService,
    {
    provide: AuthServiceConfig,
    useFactory: provideConfig
    }],
  bootstrap: [HomeComponent]
})
export class AppModule { }
