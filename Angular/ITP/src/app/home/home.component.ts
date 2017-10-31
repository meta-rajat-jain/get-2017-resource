import { Component, OnInit, HostListener, Directive } from '@angular/core';
import { Router } from '@angular/router';
import { Authentication } from './Authentication';
import { HomeService } from './home.service';
import { AuthenticatedHeader } from './authenticatedHeader';
import { FormBuilder, FormGroup, Validators,FormControl } from '@angular/forms';
import Typed from 'typed.js';
import { AuthService, SocialUser } from 'angular4-social-login';
import { GoogleLoginProvider } from 'angular4-social-login';

declare var $:any;


@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: [ './home.component.css' ]
  })


  export class HomeComponent implements OnInit {
    home:HomeComponent;
    private user: SocialUser;
    private loggedIn: boolean;
    authenticationObject:Authentication;
    reactiveForm: FormGroup;
    username:string;
    password:string;
    Title:string = 'This field is required';
    selectedDomain:string;
    checkDomainNames:string[];
    domainTitle:string;
    constructor(private log:HomeService, private router:Router,private fb: FormBuilder,private authService: AuthService) { 
      this.reactiveForm = this.fb.group({
        'username' : [null,Validators.compose([Validators.required,Validators.email])],
        'password' : [null, Validators.required],

        'empName'  : [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('^[a-zA-Z]{3,30}$')])],
        'empEmail'  : [null,Validators.compose([Validators.required,Validators.minLength(1)])],
        'empPassword'  : [null,Validators.compose([Validators.required,Validators.minLength(8)])],
        'empContact'  : [null,Validators.compose([Validators.required,Validators.minLength(10),Validators.maxLength(10),Validators.pattern('^[7-9][0-9]{9}$')])],

        'orgContactNo'  : [null,Validators.compose([Validators.required,Validators.minLength(10),Validators.maxLength(10),Validators.pattern('^[7-9][0-9]{9}$')])],
        'orgName'  : [null,Validators.compose([Validators.required,Validators.pattern('^[a-zA-Z]{2,30}$')])],
        'orgEmail'  :  [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.email])],
        'orgPassword'  : [null,Validators.compose([Validators.required,Validators.minLength(8)])],
        'orgDomainName'  :  [null,Validators.compose([Validators.required,Validators.minLength(1),Validators.pattern('^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9]\.[a-zA-Z]{2,6}$')])]
      
      });
     
        }

    ngOnInit(): void {
      var typed = new Typed("#typed", {
        strings:["Welcome to It Resource Request", "This is a for Resource Request Managment","We are the best in bussiness when it comes to resource request Managment","Enjoy your stay"],
        smartBackspace: true ,
        typeSpeed: 150,
        startDelay: 10,
        backDelay: 100,
        backSpeed:150,
        showCursor: true,
        fadeOutClass: 'typed-fade-out',
        fadeOutDelay: 500,
         cursorChar: '|',
        autoInsertCss: true,
        loop: true
    });
    this.authService.authState.subscribe((user) => {
      this.user = user;
      console.log(user);
      this.loggedIn = (user != null);
      if(user!=null){
        console.log("in if" + user.email);
        this.signInWithGoogle(user);
      }
      });
     this.log.getOrganisation();
      }


      login(username:string,password:string){
        
                console.log("in username" + username);
        
                this.log.authenticate(username,password).then(response => {
                  this.authenticationObject = response;
                  console.log(this.authenticationObject.statusCode);
                  let authenticationHeader:AuthenticatedHeader ={
                    username:username,
                    authenticationToken:this.authenticationObject.authorisationToken
                    
                  }
                  if(this.authenticationObject.statusCode==1) {
                        this.log.saveUser(authenticationHeader);
                        console.log(authenticationHeader + "in compo");
                        console.log(authenticationHeader.authenticationToken);
                        console.log(this.authenticationObject.authorisationToken);
                        console.log(this.log.getUsername());
                        console.log("in lo" + localStorage.getItem('authenticationObject'));
                      this.router.navigate(['/dashboard']);
                    }else {
                    
                      this.router.navigate(['/']);
                    }
                  
                });
               
            }
           
           
            signUpEmp(usernameEmp,passwordEmp,emailIdEmp,contactnoEmp){

                      console.log("in username" + usernameEmp);
                      let input = emailIdEmp.split("@");
                      
                      console.log("input[0]: " + input[0] + "input[1]" + input[1]);
                      let mySelect = input[1];
              
                      this.log.signUp(usernameEmp,passwordEmp,emailIdEmp,contactnoEmp,mySelect).then(response => {
                        this.authenticationObject = response;
                        console.log("status.code" + this.authenticationObject.statusCode);
                        let authenticationHeader:AuthenticatedHeader ={
                          username:usernameEmp,
                          authenticationToken:this.authenticationObject.authorisationToken
                          
                        }          
                   
                      }); 
                  }
      
                  signUpOrganisation(usernameOrg,emailIdOrg,domainname, passwordOrg,contactnoOrg){
                    
                            console.log("in username" + usernameOrg);
                            console.log("selected Domain" + emailIdOrg);
                    
                            this.log.signUpOrganisation(usernameOrg,emailIdOrg,domainname, passwordOrg,contactnoOrg).then(response => {
                              this.authenticationObject = response;
                              console.log(this.authenticationObject.statusCode);
                              let authenticationHeader:AuthenticatedHeader ={
                                username:usernameOrg,
                                authenticationToken:this.authenticationObject.authorisationToken
                                
                              }          
                         
                            }); 
                        }
      
      
                        onSignIn() {
                        
                          this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
                          }

                          signInWithGoogle(user:SocialUser):void{
                              console.log("Function is Called" + user.email);
                              this.log.authenticateGoogleUser(user).then( response => {
                                this.authenticationObject = response;
                                console.log("in google + profile status code" + this.authenticationObject.statusCode);
                                console.log("in google + profile authentication object" + this.authenticationObject.authorisationToken + this.authenticationObject.message);
                                let authenticationHeader:AuthenticatedHeader ={
                                  username:user.email,
                                  authenticationToken:this.authenticationObject.authorisationToken
                                  
                                }
                                if(this.authenticationObject.statusCode==1) {
                                      this.log.saveUser(authenticationHeader);
                                      console.log(authenticationHeader + "in google + account");
                                      console.log(authenticationHeader.authenticationToken+ "in google + account");
                                      console.log(this.authenticationObject.authorisationToken+ "in google + account");
                                      console.log(this.log.getUsername()+ "in google + account");
                                      console.log("in lo" + localStorage.getItem('authenticationObject')+ "in google + account");
                                    this.router.navigate(['/dashboard']);
                                  }else {
                                  
                                    this.router.navigate(['/']);
                                  }



                              });
                            
                          }

                          checkOrganisation(email:string):void{
                            this.checkDomainNames = this.log.getDomainNames();
                             let input = email.split("@");
                        
                            console.log("checked domain:" );
                            console.log(this.checkDomainNames);
                            for(let domain of this.checkDomainNames){
                              console.log("getting value of each domain:"  );
                              console.log( domain);
                              console.log(input[1] + "input[1]");
                              if(input[1] === domain){
                                this.domainTitle ='';
                              }
                              else{
                                this.domainTitle = 'Your Organisation is not registered with us';
                              }
                            }
                            console.log(this.domainTitle + "in domain Title after setting its value");
                          }


     
            flip1():void{
          document.getElementById( 'side-2' ).className = 'flip flip-side-1';
          document.getElementById( 'side-1' ).className = 'flip flip-side-2';
       }
     
      flip():void{
          document.getElementById( 'side-1' ).className = 'flip flip-side-1';
          document.getElementById( 'side-2' ).className = 'flip flip-side-2';
        
      }
      
     displayForm(type:string):void{
        var x = document.getElementById("organisation");
        var y = document.getElementById("employee");
       if(type==="employee"){
        x.style.display = "none";
        y.style.display = "block";
       }
       else{
        y.style.display = "none";
        x.style.display = "block";
       }
      }
      
    }