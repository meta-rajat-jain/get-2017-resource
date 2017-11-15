package com.metacube.helpdesk.vo;

public class TicketStatusCount {
    
    String status;
    Long count;    
	public TicketStatusCount() {
	}	
	public TicketStatusCount(String status, Long count) {
		
		this.status = status;
		this.count = count;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
}
