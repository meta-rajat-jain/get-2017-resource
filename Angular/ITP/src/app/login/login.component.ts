import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormControl
} from "@angular/forms";
import Typed from "typed.js";
import { AuthService, SocialUser } from "angular4-social-login";
import { GoogleLoginProvider } from "angular4-social-login";
import { Authentication } from "../Model/Authentication";
import { AuthenticatedHeader } from "../Model/authenticatedHeader";
import { ResponseObject } from "../Model/responseObject";
import { UserService } from "../services/user.service";
import { CustomValidators } from "../Custom Validations/CustomValidation";
import { HomeComponent } from "../home/home.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  private home: HomeComponent;
  private responseObject: ResponseObject;
  private user: SocialUser;
  private loggedIn: boolean;
  private authenticationObject: Authentication;
  private reactiveForm: FormGroup;
  private username: string;
  private password: string;
  private Title: string = "This field is required";
  private selectedDomain: string;
  private checkDomainNames: string[];
  private domainTitle: string;
  private errorMessage: string;
  constructor(
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService
  ) {
    this.reactiveForm = this.fb.group({
      username: [
        null,
        Validators.compose([Validators.required, Validators.email])
      ],
      password: [null, Validators.required,Validators.minLength(8)],
    });
  }
  ngOnInit() {
    this.authService.authState.subscribe(user => {
      this.user = user;

      this.loggedIn = user != null;
      if (user != null) {
        this.signInWithGoogle(user);
      }
    });
    this.userService.getOrganisation();
  }
  login(username: string, password: string) {
    this.userService.authenticate(username, password).then(response => {
      this.responseObject = response;

      let authenticationHeader: AuthenticatedHeader = {
        username: username,
        authorisationToken: this.responseObject.response.authorisationToken
      };
      localStorage.setItem("employeeType", this.responseObject.employeeType);
      if (this.responseObject.response.statusCode == 1) {
        this.userService.saveUser(authenticationHeader);
       
        if (this.responseObject.employeeType == "Member") {
          this.router.navigate(["/memberDashboard"]);
        } else if (this.responseObject.employeeType == "Team Lead") {
          this.router.navigate(["/teamLeadDashboard"]);
        } else if (this.responseObject.employeeType == "Manager") {
          this.router.navigate(["/managerDashboard"]);
        } else if (this.responseObject.employeeType == "Admin") {
          this.router.navigate(["/adminDashboard"]);
        } else if (this.responseObject.employeeType.toString() == "Helpdesk") {
          this.router.navigate(["/helpDeskDashboard"]);
        } else {
          this.errorMessage = this.responseObject.response.message;
        }
        this.reactiveForm.reset();
        this.errorMessage = "";
      } else {
        this.errorMessage =
          "Invalid Credentials" + this.responseObject.response.message;
      }
    });
  }
  onSignIn() {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  signInWithGoogle(user: SocialUser): void {
    this.userService.authenticateGoogleUser(user).then(response => {
      console.log(response);
      this.responseObject = response;
      let authenticationHeader: AuthenticatedHeader = {
        username: user.email,
        authorisationToken: this.responseObject.response.authorisationToken
      };
      if (this.responseObject.response.statusCode == 1) {
        this.userService.saveUser(authenticationHeader);
        if (this.responseObject.employeeType == "Member") {
          this.router.navigate(["memberDashboard"]);
        } else if (this.responseObject.employeeType == "Team Lead") {
          this.router.navigate(["teamLeadDashboard"]);
        } else if (this.responseObject.employeeType == "Manager") {
          this.router.navigate(["managerDashboard"]);
        } else if (this.responseObject.employeeType == "Admin") {
          this.router.navigate(["adminDashboard"]);
        } else if (this.responseObject.employeeType == "Helpdesk") {
          this.router.navigate(["helpDeskDashboard"]);
        }
      } else {
        this.errorMessage =
          "This account is not registered with us.Please Log in with registered ID";
        this.router.navigate([""]);
      }
    });
  }
  forgetPassword() {
    this.router.navigate(["/password"]);
  }
}
