package com.metacube.helpdesk.dto;

import java.io.Serializable;
import java.util.Date;

import com.metacube.helpdesk.model.ItResource;

public class TicketDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int ticketNo;

	private String requesterName;
	private String requestedFor;
	private String requestType;
	private String priority;
	private String status;
	private String comment;
	private ItResource requestedResource;
	private String seatLocation;
	private String lastUpdatedByUsername;
	private Date lastDateOfUpdate;
	private Date requestDate;
	private String teamName;

	public TicketDTO() {

	}

	public TicketDTO(int ticketNo, String requesterName, String requestedFor,
			String requestType, String priority, String status, String comment,
			ItResource requestedResource, String seatLocation,
			String lastUpdatedByUsername, Date lastDateOfUpdate,
			Date requestDate, String teamName) {

		this.ticketNo = ticketNo;
		this.requesterName = requesterName;
		this.requestedFor = requestedFor;
		this.requestType = requestType;
		this.priority = priority;
		this.status = status;
		this.comment = comment;
		this.requestedResource = requestedResource;
		this.seatLocation = seatLocation;
		this.lastUpdatedByUsername = lastUpdatedByUsername;
		this.lastDateOfUpdate = lastDateOfUpdate;
		this.requestDate = requestDate;
		this.teamName = teamName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getLastUpdatedByUsername() {
		return lastUpdatedByUsername;
	}

	public void setLastUpdatedByUsername(String lastUpdatedByUsername) {
		this.lastUpdatedByUsername = lastUpdatedByUsername;
	}

	public Date getLastDateOfUpdate() {
		return lastDateOfUpdate;
	}

	public void setLastDateOfUpdate(Date lastDateOfUpdate) {
		this.lastDateOfUpdate = lastDateOfUpdate;
	}

	public String getSeatLocation() {
		return seatLocation;
	}

	public void setSeatLocation(String seatLocation) {
		this.seatLocation = seatLocation;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public int getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(int ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getRequestedFor() {
		return requestedFor;
	}

	public void setRequestedFor(String requestedFor) {
		this.requestedFor = requestedFor;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	public ItResource getRequestedResource() {
		return requestedResource;
	}

	public void setRequestedResource(ItResource requestedResource) {
		this.requestedResource = requestedResource;
	}
}
