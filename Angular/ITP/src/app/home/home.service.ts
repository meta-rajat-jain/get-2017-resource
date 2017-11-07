import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/toPromise';
import { SocialUser } from 'angular4-social-login';
import { URLSearchParams } from '@angular/http';
import { Login } from '../Model/login';
import { Authentication } from '../Model/Authentication';
import { AuthenticatedHeader } from '../Model/authenticatedHeader';
import { signUpOrganisation } from '../Model/signUpOrganisation';
import { Employee } from '../Model/signEmp';
import { ResponseObject } from '../Model/responseObject';

@Injectable()
export class HomeService {
     server: string = 'http://172.16.33.111:8080/';
     controller: string ='ResourceRequest/rest/auth/';
     request: string = this.server + this.controller;
    
   
    private headers = new Headers({ 'Content-Type': 'application/json' });
    private loginUrl = this.request + 'login';
    private signUpEmployeeUrl = this.request + 'signup/employee';
    private signUpOrganisationUrl = this.request + 'signup/organisation';
    private signInWithGoogleUrl=this.request + 'verifyUser';
    private getOrganisationUrl=this.request + 'getOrganisationDomains';
    private checkUserUrl=this.request + 'demo';
    domainNames : string[] ;
    constructor(private http: Http) {}



        getOrganisation(): Promise<string[]> {
        return this.http.get(this.getOrganisationUrl)
        .toPromise()
        .then(response => this.domainNames =  response.json()        )
        .catch(this.handleError);
        }
        
        getDomainNames():string[]{
            console.log("getting domain names: " + this.domainNames);
            return this.domainNames;
        }
    
    
    authenticate(username:string,password:string): Promise<ResponseObject> {
        let login:Login={
        username:username,
        password:password,
        authorisationToken:""
        }
        console.log(login);
        const url = `${this.loginUrl} `;
        return  this.http.post(url,login)
        .toPromise()
        .then(response => response.json() as ResponseObject)
        .catch(this.handleError);
        }

       
        saveUser(authenticated:AuthenticatedHeader) {
            console.log(JSON.stringify(authenticated));
            localStorage.setItem('authenticationObject', JSON.stringify(authenticated));
            
        }
        
        getAuthenticationObject(): string {
            console.log("for header" + JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken + JSON.parse(localStorage.getItem('authenticationObject')).username);
            console.log(typeof(JSON.parse(localStorage.getItem('authenticationObject')).authorisationToken));
            return localStorage.getItem('authenticationObject');
        }
        


        signUp(usernameEmp,passwordEmp,emailIdEmp,contactnoEmp,selectedDomain): Promise<Authentication>{

            let login:Login={
                username:emailIdEmp ,
                password:passwordEmp,
                authorisationToken:""
                }
            let signEmp:Employee={
                name:usernameEmp,
                contactNumber:contactnoEmp,
                orgDomain:selectedDomain,
                designation:"",
                status:"",
                login:login
                
                }   
                console.log(JSON.stringify(signEmp)) ;

                console.log("in here######"+this.signUpEmployeeUrl);
                const url = `${this.signUpEmployeeUrl} `;
                console.log(JSON.stringify(signEmp));
                return  this.http.post(url, signEmp)
                .toPromise()
                .then(response => response.json() as Authentication)
                .catch(this.handleError);
        }
       
        signUpOrganisation(usernameOrg,emailIdOrg,domainname, passwordOrg,contactnoOrg): Promise<Authentication>{
            console.log("url" + this.signUpOrganisationUrl);
            let login:Login={
                username:emailIdOrg,
                password:passwordOrg,
                authorisationToken:""
                }
            let signOrg:signUpOrganisation={
                name:usernameOrg,
                contactNumber:contactnoOrg,
                domain:domainname,
                login:login
                
                }  
                console.log("url" + this.signUpOrganisationUrl);
                console.log(JSON.stringify(signOrg)) ;
                const url = `${this.signUpOrganisationUrl} `;
                return  this.http.post(url,signOrg)
                .toPromise()
                .then(response => response.json() as Authentication)
                .catch(this.handleError);
        }
        authenticateGoogleUser(user:SocialUser):Promise<ResponseObject>{
            const url = `${this.signInWithGoogleUrl}`;
           
            let params: URLSearchParams = new URLSearchParams();
            console.log("in service getting username " + user.email );
            params.set('username',user.email );
            return this.http.get(url,{ search: params } )
            .toPromise()
            .then(response => response.json() as ResponseObject)
            .catch(this.handleError);
        }
        
        private handleError(error: any): Promise<any> {
            console.error('An error occurred', error); 
            return Promise.reject(error.message || error);
            }

}