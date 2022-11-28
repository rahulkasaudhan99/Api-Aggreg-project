package com.tnt.apiAggregation.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public class TrackingRequestDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	UUID uuid;
	Set<String> requestStr;

	public TrackingRequestDto(UUID uuid, Set<String> requestStr) {
		super();
		this.uuid = uuid;
		this.requestStr = requestStr;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Set<String> getRequestStr() {
		return requestStr;
	}

	public void setRequestStr(Set<String> requestStr) {
		this.requestStr = requestStr;
	}

}
