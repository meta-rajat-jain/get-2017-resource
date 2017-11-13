import { Component, OnInit } from '@angular/core';
import { Ticket } from '../Model/Ticket';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { NeedInformationService } from './need-information.service';

@Component({
  selector: 'app-need-information',
  templateUrl: './need-information.component.html',
  styleUrls: ['./need-information.component.css']
})
export class NeedInformationComponent implements OnInit {
  sub:any;
  type:string;
  ticket:Ticket;
  constructor(private needInformationService: NeedInformationService, private route: ActivatedRoute,private router:Router) { }

  ngOnInit() {

     this.sub = this.route.params.subscribe(params => {
     this.ticket = params['ticket']; 
     console.log(this.ticket);
     });
     
   
   
    }
    init():void{
      this.needInformationService.getRequest(this.ticket,this.type).then(response => this.ticket = response);
 }

}