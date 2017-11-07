
export class Authentication{
 statusCode:number;
 //if status code is 0 results in login failure and message in message field
 authorisationToken:string ;
 //if status code is 0 authorisationToken is null and if status code is 1 login is successfull
 message:string ;
}