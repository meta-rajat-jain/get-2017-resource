package com.metacube.helpdesk.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TicketApproval")
public class TicketApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ticketApprovalId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ticketApprovalId;

    @OneToOne
    @JoinColumn(name = "ticketNo", nullable = false)
    private Ticket ticketNo;

    @ManyToOne
    @JoinColumn(name = "approver", nullable = false)
    private Employee approver;

    @Column(name = "approveStatus", nullable = false)
    private String approveStatus;

    @Column(name = "helpdeskApprovalStatus", nullable = false)
    private String helpdeskApprovalStatus;

    public TicketApproval() {

    }

    public TicketApproval(int ticketApprovalId, Ticket ticketNo,
            Employee approver, String approveStatus,
            String helpdeskApprovalStatus) {
        this.ticketApprovalId = ticketApprovalId;
        this.ticketNo = ticketNo;
        this.approver = approver;
        this.approveStatus = approveStatus;
        this.helpdeskApprovalStatus = helpdeskApprovalStatus;
    }

    public int getTicketApprovalId() {
        return ticketApprovalId;
    }

    public void setTicketApprovalId(int ticketApprovalId) {
        this.ticketApprovalId = ticketApprovalId;
    }

    public Ticket getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(Ticket ticketNo) {
        this.ticketNo = ticketNo;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getHelpdeskApprovalStatus() {
        return helpdeskApprovalStatus;
    }

    public void setHelpdeskApprovalStatus(String helpdeskApprovalStatus) {
        this.helpdeskApprovalStatus = helpdeskApprovalStatus;
    }
}
