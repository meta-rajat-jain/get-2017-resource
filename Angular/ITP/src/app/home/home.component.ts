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

@Component({
  selector: "home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.css"]
})
export class HomeComponent implements OnInit {
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

  ngOnInit(): void {
    var typed = new Typed("#typed", {
      strings: [
        "Welcome to It Resource Request",
        "This is a for Resource Request Managment",
        "We are the best in bussiness when it comes to resource request Managment",
        "Enjoy your stay"
      ],
      smartBackspace: true,
      typeSpeed: 150,
      startDelay: 10,
      backDelay: 100,
      backSpeed: 150,
      showCursor: true,
      fadeOutClass: "typed-fade-out",
      fadeOutDelay: 500,
      cursorChar: "|",
      autoInsertCss: true,
      loop: true
    });
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
          this.router.navigate([""]);
        }
        this.errorMessage = "";
      } else {
        this.errorMessage =
          "Invalid Credentials" + this.responseObject.response.message;
        this.router.navigate(["/"]);
      }
    });
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
          location.reload(true);
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
          location.reload(true);
        } else {
          this.errorMessage =
            "Invalid Credentials" + this.authenticationObject.message;
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

  checkOrganisation(email: string): void {
    this.checkDomainNames = this.userService.getDomainNames();
    let input = email.split("@");
    for (let domain of this.checkDomainNames) {
      if (input[1] === domain) {
        this.domainTitle = "";
        break;
      } else {
        this.domainTitle = "Your Organisation is not registered with us";
      }
    }
    console.log(this.domainTitle + "in domain Title after setting its value");
  }

  flip1(): void {
    document.getElementById("side-2").className = "flip flip-side-1";
    document.getElementById("side-1").className = "flip flip-side-2";
  }

  flip(): void {
    document.getElementById("side-1").className = "flip flip-side-1";
    document.getElementById("side-2").className = "flip flip-side-2";
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

  forgetPassword() {
    this.router.navigate(["/password"]);
  }
}
