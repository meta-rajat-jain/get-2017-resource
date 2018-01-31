import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { PasswordService } from "../services/password.service";


@Component({
selector: 'app-password',
templateUrl: './password.component.html',
styleUrls: ['./password.component.css'],
encapsulation: ViewEncapsulation.None
})
export class PasswordComponent implements OnInit {

constructor(private passwordService:PasswordService) { }

ngOnInit() {
}

forgetPassword(userName:string){
this.passwordService.forgetPassword(userName);
}
}