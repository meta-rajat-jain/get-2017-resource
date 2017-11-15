package com.metacube.helpdesk.vo;

import com.metacube.helpdesk.utility.Response;

public class GetResponse<T> {
	private Response responseStatus;
	private T payloadData;
	public GetResponse() {
		super();
	}
	public GetResponse(Response responseStatus, T payloadData) {
		super();
		this.responseStatus = responseStatus;
		this.payloadData = payloadData;
	}
	public Response getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(Response responseStatus) {
		this.responseStatus = responseStatus;
	}
	public T getPayloadData() {
		return payloadData;
	}
	public void setPayloadData(T payloadData) {
		this.payloadData = payloadData;
	}
}
