package com.metacube.helpdesk.utility;

public class TicketCreationResponse {

    private Response response;
    private int ticketNo;

    public int getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(int ticketNo) {
        this.ticketNo = ticketNo;
    }

    public TicketCreationResponse() {
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public TicketCreationResponse( Response response, int ticketNo ) {
        super();
        this.response = response;
        this.ticketNo = ticketNo;
    }
}
