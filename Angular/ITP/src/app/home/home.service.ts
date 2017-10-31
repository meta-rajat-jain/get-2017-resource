import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import {Authentication} from './Authentication'
import 'rxjs/add/operator/toPromise';
import { AuthenticatedHeader } from './authenticatedHeader';
import { Login } from './login';
import { signUpEmployee } from './signEmp';
import { signUpOrganisation } from './signUpOrganisation';
import { SocialUser } from 'angular4-social-login';
import { URLSearchParams } from '@angular/http';

@Injectable()
export class HomeService {
    
    private headers = new Headers({ 'Content-Type': 'application/json' });
    private loginUrl = 'http://172.16.33.120:8080/ResourceRequest/rest/auth/login';
    private signUpEmployeeUrl = 'http://172.16.33.120:8080/ResourceRequest/rest/auth//signup/employee';
    private signUpOrganisationUrl = 'http://172.16.33.120:8080/ResourceRequest/rest/auth//signup/organisation';
    private signInWithGoogleUrl="http://172.16.33.120:8080/ResourceRequest/rest/auth/verifyUser";
    private getOrganisationUrl="http://172.16.33.120:8080/ResourceRequest/rest/auth/getOrganisationDomains";
    domainNames : string[] ;
    constructor(private http: Http) {}



        getOrganisation(): Promise<string[]> {
        return this.http.get(this.getOrganisationUrl)
        .toPromise()
        .then(response =>{ console.log("in getting org" + response) 
                this.domainNames =  response.json() 
                            })
        .catch(this.handleError);
        }
        
        getDomainNames():string[]{
            console.log("getting domain names: " + this.domainNames);
            return this.domainNames;
        }
    
    
    authenticate(username:string,password:string): Promise<Authentication> {
        let login:Login={
        username:username,
        password:password,
        authorisationToken:""
        }
        console.log(login);
        const url = `${this.loginUrl} `;
        return  this.http.post(url, JSON.stringify(login), {headers: this.headers})
        .toPromise()
        .then(response => response.json() as Authentication)
        .catch(this.handleError);
        }

       
        saveUser(authenticated:AuthenticatedHeader) {
            console.log(JSON.stringify(authenticated));
            localStorage.setItem('authenticationObject', JSON.stringify(authenticated));
            
        }
        
        getUsername(): string {
            return localStorage.getItem('authenticationObject');
        }
        signUp(usernameEmp,passwordEmp,emailIdEmp,contactnoEmp,selectedDomain): Promise<Authentication>{

            let login:Login={
                username:emailIdEmp ,
                password:passwordEmp,
                authorisationToken:""
                }
            let signEmp:signUpEmployee={
                name:usernameEmp,
                contactNumber:contactnoEmp,
                orgDomain:selectedDomain,
                designation:"",
                status:"",
                login:login
                
                }   
                console.log(JSON.stringify(signEmp)) ;
                const url = `${this.signUpEmployeeUrl} `;
                return  this.http.post(url, JSON.stringify(signEmp), {headers: this.headers})
                .toPromise()
                .then(response => response.json() as Authentication)
                .catch(this.handleError);
        }
       
        signUpOrganisation(usernameOrg,emailIdOrg,domainname, passwordOrg,contactnoOrg): Promise<Authentication>{
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
                console.log(JSON.stringify(signOrg)) ;
                const url = `${this.signUpOrganisationUrl} `;
                return  this.http.post(url, JSON.stringify(signOrg), {headers: this.headers})
                .toPromise()
                .then(response => response.json() as Authentication)
                .catch(this.handleError);
        }
        authenticateGoogleUser(user:SocialUser):Promise<Authentication>{
            const url = `${this.signInWithGoogleUrl}`;
           
            let params: URLSearchParams = new URLSearchParams();
            console.log("in service getting username " + user.email );
            params.set('userName',user.email );
            return this.http.get(url,{ search: params })
            .toPromise()
            .then(response => response.json() as Authentication)
            .catch(this.handleError);
        }
        private handleError(error: any): Promise<any> {
            console.error('An error occurred', error); 
            return Promise.reject(error.message || error);
            }

}