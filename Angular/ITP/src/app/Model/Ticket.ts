import { RequestedResource } from "./requestResource";

export class Ticket
{
    ticketNo:number;
    requesterName:string;
    requestedFor:string;
    priority:string;
    comment:string;
    requestedResource:RequestedResource;
    teamName:string;
    seatLocation:string;
    requestType:string;
    status:string;
    
    }