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
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  private home: HomeComponent;
  private responseObject: ResponseObject;
  private user: SocialUser;
  private loggedIn: boolean;
  private authenticationObject: Authentication;
   reactiveForm: FormGroup;
   reactiveFormOrganisation: FormGroup;
  private username: string;
  private password: string;
  private Title: string = "This field is required";
  private selectedDomain: string;
  private checkDomainNames: string[];
  domainTitle: string ;
   errorMessage: string;
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
      password: [null, Validators.required],

      empName: [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(1),
          Validators.pattern("[A-Za-z]+ *[A-Za-z ]+$")
        ])
      ],
      empEmail: [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(1),
          CustomValidators.checkOrganisation
        ])
      ],
      empPassword: [
        null,
        Validators.compose([Validators.required, Validators.minLength(8)])
      ],
      empContact: [
        null,
        Validators.compose([
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(10),
          Validators.pattern("^[7-9][0-9]{9}$")
        ])
      ],
    });
this.reactiveFormOrganisation = this.fb.group({
  orgContactNo: [
    null,
    Validators.compose([
      Validators.required,
      Validators.minLength(10),
      Validators.maxLength(10),
      Validators.pattern("^[7-9][0-9]{9}$")
    ])
  ],
  orgName: [
    null,
    Validators.compose([
      Validators.required,
      Validators.pattern("[A-Za-z]+ *[A-Za-z ]+$")
    ])
  ],
  orgEmail: [
    null,
    Validators.compose([
      Validators.required,
      Validators.minLength(1),
      Validators.email
    ])
  ],
  orgPassword: [
    null,
    Validators.compose([Validators.required, Validators.minLength(8)])
  ],
  orgDomainName: [
    null,
    Validators.compose([
      Validators.required,
      Validators.minLength(1),
      Validators.pattern(
        "^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9].[a-zA-Z]{2,6}$"
      )
    ])
  ]
});
  }
  ngOnInit() {
    this.userService.getOrganisation();
  }
  signUpEmp(usernameEmp, passwordEmp, emailIdEmp, contactnoEmp) {
    let input = emailIdEmp.split("@");
    let mySelect = input[1];

    this.userService
      .signUp(usernameEmp, passwordEmp, emailIdEmp, contactnoEmp, mySelect)
      .then(response => {
        this.authenticationObject = response;
        let authenticationHeader: AuthenticatedHeader = {
          username: usernameEmp,
          authorisationToken: this.authenticationObject.authorisationToken
        };
        if (this.authenticationObject.statusCode == 1) {
          this.userService.saveUser(authenticationHeader);
          this.errorMessage = this.authenticationObject.message;
          this.reactiveForm.reset();
          
        } else {
          this.errorMessage = this.authenticationObject.message;
        }
      });
  }

  signUpOrganisation(
    usernameOrg,
    emailIdOrg,
    domainname,
    passwordOrg,
    contactnoOrg
  ) {
    this.userService
      .signUpOrganisation(
        usernameOrg,
        emailIdOrg,
        domainname,
        passwordOrg,
        contactnoOrg
      )
      .then(response => {
        this.authenticationObject = response;
        let authenticationHeader: AuthenticatedHeader = {
          username: usernameOrg,
          authorisationToken: this.authenticationObject.authorisationToken
        };
        if (this.authenticationObject.statusCode == 1) {
          this.userService.saveUser(authenticationHeader);
          this.errorMessage =
            "Valid Credentials" + this.authenticationObject.message;
            this.reactiveForm.reset();
            
        } else {
          this.errorMessage =
            "Invalid Credentials" + this.authenticationObject.message;
        }
      });
  }
  checkOrganisation(email: string): void {
    this.domainTitle="NO such domain exists";
    this.checkDomainNames = this.userService.getDomainNames();
    let input = email.split("@");
    for (let domain of this.checkDomainNames) {
      if (input[1] == domain) {
        this.domainTitle = "";
        break;
      } else {
        this.domainTitle = "Your Organisation is not registered with us";
      }
    }
  }
  displayForm(type: string): void {
    var x = document.getElementById("organisation");
    var y = document.getElementById("employee");
    if (type === "employee") {
      x.style.display = "none";
      y.style.display = "block";
    } else {
      y.style.display = "none";
      x.style.display = "block";
    }
  }

}
