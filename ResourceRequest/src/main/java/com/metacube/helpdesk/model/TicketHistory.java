package com.metacube.helpdesk.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TicketHistory")
public class TicketHistory implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -1700242899690125882L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id; 
    
    @ManyToOne
    @JoinColumn(name="ticketNo",nullable=false)
    private Ticket ticket;
    
    //to be made enum
    @Column(name="updatedPriority",nullable=false)
    private String updatedPriority;
    
    //to be made enum
    @Column(name="updatedStatus",nullable=false)
    private String updatedStatus;
    
    @Column(name="updatedComment")
    private String updatedComment;
    
    @Column(name="dateOfUpdate",nullable=false)
    private Date dateOfUpdate;
    
    @ManyToOne
    @JoinColumn(name="updatedBy",nullable=false)
    private Employee updatedBy;
    
    

    public TicketHistory() {
       
    }

    public TicketHistory(Ticket ticket, String updatedPriority,
            String updatedStatus, String updatedComment, Date dateOfUpdate,
            Employee updatedBy) {
      
        this.ticket = ticket;
        this.updatedPriority = updatedPriority;
        this.updatedStatus = updatedStatus;
        this.updatedComment = updatedComment;
        this.dateOfUpdate = dateOfUpdate;
        this.updatedBy = updatedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getUpdatedPriority() {
        return updatedPriority;
    }

    public void setUpdatedPriority(String updatedPriority) {
        this.updatedPriority = updatedPriority;
    }

    public String getUpdatedStatus() {
        return updatedStatus;
    }

    public void setUpdatedStatus(String updatedStatus) {
        this.updatedStatus = updatedStatus;
    }

    public String getUpdatedComment() {
        return updatedComment;
    }

    public void setUpdatedComment(String updatedComment) {
        this.updatedComment = updatedComment;
    }

    public Date getDateOfUpdate() {
        return dateOfUpdate;
    }

    public void setDateOfUpdate(Date dateOfUpdate) {
        this.dateOfUpdate = dateOfUpdate;
    }

    public Employee getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Employee updatedBy) {
        this.updatedBy = updatedBy;
    }   
}
