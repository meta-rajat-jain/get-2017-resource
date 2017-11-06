package com.metacube.helpdesk.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="HelpDeskResponse")
public class HelpDeskResponse implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    //Autogenerated employeeId
    @Id
    @Column(name="responseId")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int responseId;
    
  //defines status of the employee
    //to be made enum
    @Column(name="status", nullable = false)
    private String status;
    
    @Column(name="comment")
    private String comment;
    
    @OneToOne
    @JoinColumn(name="ticketNo",unique=true,nullable=false)
    private Ticket ticket;

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public HelpDeskResponse(int responseId, String status, String comment,
            Ticket ticket) {
        
        this.responseId = responseId;
        this.status = status;
        this.comment = comment;
        this.ticket = ticket;
    }

    public HelpDeskResponse() {
      
    }
    
}
