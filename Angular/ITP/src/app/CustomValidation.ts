import { FormArray, FormControl, FormGroup, ValidationErrors } from '@angular/forms';   
import { Employee } from './Model/signEmp';
import { HomeService } from './home/home.service';

export class CustomValidators{
    ngOnInIt():void{
        this.getOrg();
    }
     static checkDomainNames: string[]=[];
    constructor(private log:HomeService){};
    
    getOrg():string[]{
         this.log.getOrganisation().then(response=>{ CustomValidators.checkDomainNames = response });
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
                'countryCity': {
                  'message': error
                }
              };
              return error ? message : null;
          }
        }


}