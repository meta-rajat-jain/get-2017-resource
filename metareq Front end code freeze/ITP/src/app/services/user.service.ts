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
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { RequestConstants } from "../Constants/request";

@Injectable()
export class UserService {
    private headers = new Headers({ 'Content-Type': 'application/json' });
    private loginUrl=RequestConstants.AUTHENTICATION_REQUEST+'login';
    private signUpEmployeeUrl=RequestConstants.AUTHENTICATION_REQUEST+ 'signup/employee';
    private signUpOrganisationUrl=RequestConstants.AUTHENTICATION_REQUEST+'signup/organisation';
    private signInWithGoogleUrl=RequestConstants.AUTHENTICATION_REQUEST+ 'verifyUser';
    private getOrganisationUrl=RequestConstants.AUTHENTICATION_REQUEST+'getOrganisationDomains';
    private checkUserUrl=RequestConstants.AUTHENTICATION_REQUEST+ 'accessVerification';



    domainNames: string[];
    authorised: boolean;
    responseObject: ResponseObject;
    constructor(private http: Http, private router: Router) { }

    

    getOrganisation(): Promise<string[]> {
        return this.http.get(this.getOrganisationUrl)
            .toPromise()
            .then(response => this.domainNames = response.json())
            .catch(this.handleError);
    }

    getDomainNames(): string[] {
        return this.domainNames;
    }


    authenticate(username: string, password: string): Promise<ResponseObject> {
        let login: Login = {
            username: username,
            password: password,
            authorisationToken: ""
        }
        const url = `${this.loginUrl} `;
        return this.http.post(url, login)
            .toPromise()
            .then(response => response.json() as ResponseObject)
            .catch(this.handleError);
    }


    saveUser(authenticated: AuthenticatedHeader) {
        localStorage.setItem('authenticationObject', JSON.stringify(authenticated));

    }

    getAuthenticationObject(): string {
        return localStorage.getItem('authenticationObject');
    }



    signUp(usernameEmp, passwordEmp, emailIdEmp, contactnoEmp, selectedDomain): Promise<Authentication> {
        let login: Login = {
            username: emailIdEmp,
            password: passwordEmp,
            authorisationToken: ""
        }
        let signEmp: Employee = {
            name: usernameEmp,
            contactNumber: contactnoEmp,
            orgDomain: selectedDomain,
            designation: "Member",
            status: "",
            login: login

        }

        const url = `${this.signUpEmployeeUrl} `;
        return this.http.post(url, signEmp)
            .toPromise()
            .then(response => response.json() as Authentication)
            .catch(this.handleError);
    }

    signUpOrganisation(usernameOrg, emailIdOrg, domainname, passwordOrg, contactnoOrg): Promise<Authentication> {
        let login: Login = {
            username: emailIdOrg,
            password: passwordOrg,
            authorisationToken: ""
        }
        let signOrg: signUpOrganisation = {
            name: usernameOrg,
            contactNumber: contactnoOrg,
            domain: domainname,
            login: login

        }
        const url = `${this.signUpOrganisationUrl} `;
        return this.http.post(url, signOrg)
            .toPromise()
            .then(response => response.json() as Authentication)
            .catch(this.handleError);
    }
    authenticateGoogleUser(user: SocialUser): Promise<ResponseObject> {
        const url = `${this.signInWithGoogleUrl}`;

        let params: URLSearchParams = new URLSearchParams();
        params.set('username', user.email);
        return this.http.get(url, { search: params })
            .toPromise()
            .then(response => response.json() as ResponseObject)
            .catch(this.handleError);
    }
    getUser(): Observable<boolean> {
        if (localStorage.getItem('authenticationObject') != null) {
            let login: Login = {
                username: JSON.parse(this.getAuthenticationObject()).username,
                password: "",
                authorisationToken: JSON.parse(this.getAuthenticationObject()).authorisationToken
            }

            return this.http.post(this.checkUserUrl, login).map(response => {

                this.responseObject = response.json();
                if (this.responseObject.employeeType == 'Admin') {
                    return true;
                }

                this.router.navigate(['']);
            });
        }
        else {
            this.router.navigate(['']);
        }
    }
    getManager(): Observable<boolean> {
        if (localStorage.getItem('authenticationObject') != null) {
            let login: Login = {
                username: JSON.parse(this.getAuthenticationObject()).username,
                password: "",
                authorisationToken: JSON.parse(this.getAuthenticationObject()).authorisationToken
            }

            return this.http.post(this.checkUserUrl, login).map(response => {

                this.responseObject = response.json();
                if (this.responseObject.employeeType == 'Manager') {
                    return true;
                }

                this.router.navigate(['']);
            });
        }
        else {
            this.router.navigate(['']);
        }
    }
    getMember(): Observable<boolean> {
        if (localStorage.getItem('authenticationObject') != null) {
            let login: Login = {
                username: JSON.parse(this.getAuthenticationObject()).username,
                password: "",
                authorisationToken: JSON.parse(this.getAuthenticationObject()).authorisationToken
            }

            return this.http.post(this.checkUserUrl, login).map(response => {

                this.responseObject = response.json();
                if (this.responseObject.employeeType == 'Member') {
                    return true;
                }

                this.router.navigate(['']);
            });
        }
        else {
            this.router.navigate(['']);
        }
    }
    getHelpDeskAuthentication(): Observable<boolean> {
        if (localStorage.getItem('authenticationObject') != null) {
            let login: Login = {
                username: JSON.parse(this.getAuthenticationObject()).username,
                password: "",
                authorisationToken: JSON.parse(this.getAuthenticationObject()).authorisationToken
            }

            return this.http.post(this.checkUserUrl, login).map(response => {

                this.responseObject = response.json();
                if (this.responseObject.employeeType == 'Helpdesk') {
                    return true;
                }

                this.router.navigate(['']);
            });
        }
        else {
            this.router.navigate(['']);
        }
    }
    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }

}