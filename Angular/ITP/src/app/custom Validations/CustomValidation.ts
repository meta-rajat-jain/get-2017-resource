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
     static  checkOrganisation(form:FormControl):ValidationErrors{
      
      const isValid=/^[A-Z0-9._%+-]+@ +[A-Z0-9.-]/.test(form.value);
          if(isValid){
           const email=  form.value;
             console.log(email);
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
                'errorMessage': {
                  'message': error
                }
              };
              return error ? message : null;
          }
          
        }


}