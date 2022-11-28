package com.tnt.apiAggregation.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PricingRequestDto implements Serializable{
	private static final long serialVersionUID = 1L;
	UUID uuid;
	Set<String> reqString;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Set<String> getRequestStr() {
		return reqString;
	}

	public void setRequestStr(HashSet<String> reqString) {
		this.reqString = reqString;
	}
	
	public PricingRequestDto(UUID uuid, Set<String> reqString) {
		super();
		this.uuid = uuid;
		this.reqString = reqString;
	}
	
}
