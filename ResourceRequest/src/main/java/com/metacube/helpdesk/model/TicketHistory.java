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

import org.hibernate.annotations.Type;

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
    @Column(name="previousPriority",nullable=false)
    private String previousPriority;
    
    //to be made enum
    @Column(name="previousStatus",nullable=false)
    private String previousStatus;
    
    @Column(name="previousComment")
    private String previousComment;
    
    @Type(type="date")
    @Column(name="lastDateOfUpdate",nullable=false)
    private Date lastDateOfUpdate;
    
    @Column(name="lastUpdatedBy",nullable=false)
    private String lastUpdatedBy;

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

    public String getPreviousPriority() {
        return previousPriority;
    }

    public void setPreviousPriority(String previousPriority) {
        this.previousPriority = previousPriority;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getPreviousComment() {
        return previousComment;
    }

    public void setPreviousComment(String previousComment) {
        this.previousComment = previousComment;
    }

    public Date getLastDateOfUpdate() {
        return lastDateOfUpdate;
    }

    public void setLastDateOfUpdate(Date lastDateOfUpdate) {
        this.lastDateOfUpdate = lastDateOfUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public TicketHistory(int id, Ticket ticket, String previousPriority,
            String previousStatus, String previousComment,
            Date lastDateOfUpdate, String lastUpdatedBy) {
        super();
        this.id = id;
        this.ticket = ticket;
        this.previousPriority = previousPriority;
        this.previousStatus = previousStatus;
        this.previousComment = previousComment;
        this.lastDateOfUpdate = lastDateOfUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public TicketHistory() {
        super();
    }

    public TicketHistory(Ticket ticket, String previousPriority,
            String previousStatus, String previousComment,
            Date lastDateOfUpdate, String lastUpdatedBy) {
        super();
        this.ticket = ticket;
        this.previousPriority = previousPriority;
        this.previousStatus = previousStatus;
        this.previousComment = previousComment;
        this.lastDateOfUpdate = lastDateOfUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
     
}
