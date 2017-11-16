import { FormArray, FormControl, FormGroup, ValidationErrors } from '@angular/forms';   

import { UserService } from "../services/user.service";


export class CustomValidators{
    ngOnInIt():void{
        this.getOrg();
    }
     static checkDomainNames: string[]=[];
    constructor(private userService:UserService){};
    
    getOrg():string[]{
         this.userService.getOrganisation().then(response=>{ CustomValidators.checkDomainNames = response });
         return CustomValidators.checkDomainNames;
    }
     static  checkOrganisation(form:FormGroup):ValidationErrors{
         
          if(form.get('reactiveForm.empEmail')!=null){
           const email=   form.get('reactiveForm.empEmail')
             
            let emailId = email.value;
             let input = emailId.split("@");
             let error = null;
          
            for(let domain of this.checkDomainNames){
          
              if(input[1] === domain){
              
                break;
              }
              else{
                error = 'Your Organisation is not registered with us';
                
              }
            }
            const message = {
                'error': {
                  'message': error
                }
              };
              return error ? message : null;
          }
        }


}