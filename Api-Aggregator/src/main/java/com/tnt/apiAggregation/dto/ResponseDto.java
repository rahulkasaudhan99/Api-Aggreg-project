package com.tnt.apiAggregation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response
 */
@SuppressWarnings("serial")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-01T22:33:25.097Z[GMT]")

public class ResponseDto implements Serializable {
	@JsonProperty("pricing")
	@Valid
	private Map<String, BigDecimal> pricing = null;

	@JsonProperty("shipments")
	@Valid
	private Map<String, List<String>> shipments = null;

	@JsonProperty("track")
	@Valid
	private Map<String, String> track = null;

	public ResponseDto pricing(Map<String, BigDecimal> pricing) {
		this.pricing = pricing;
		return this;
	}

	public ResponseDto putPricingItem(String key, BigDecimal pricingItem) {
		if (this.pricing == null) {
			this.pricing = new HashMap<String, BigDecimal>();
		}
		this.pricing.put(key, pricingItem);
		return this;
	}

	@Schema(description = "")
	@Valid
	public Map<String, BigDecimal> getPricing() {
		return pricing;
	}

	public void setPricing(Map<String, BigDecimal> pricing) {
		this.pricing = pricing;
	}

	public ResponseDto shipments(Map<String, List<String>> shipments) {
		this.shipments = shipments;
		return this;
	}

	public ResponseDto putShipmentsItem(String key, List<String> shipmentsItem) {
		if (this.shipments == null) {
			this.shipments = new HashMap<String, List<String>>();
		}
		this.shipments.put(key, shipmentsItem);
		return this;
	}
	
	@Schema(description = "")
	@Valid
	public Map<String, List<String>> getShipments() {
		return shipments;
	}

	public void setShipments(Map<String, List<String>> shipments) {
		this.shipments = shipments;
	}

	public ResponseDto track(Map<String, String> track) {
		this.track = track;
		return this;
	}

	public ResponseDto putTrackItem(String key, String trackItem) {
		if (this.track == null) this.track = new HashMap<String, String>();
		this.track.put(key, trackItem);
		return this;
	}
	
	@Schema(description = "")

	public Map<String, String> getTrack() {
		return track;
	}

	public void setTrack(Map<String, String> track) {
		this.track = track;
	}

	@Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj) return true;
		if (null == obj || getClass() != obj.getClass()) return false;
		ResponseDto responseDto = (ResponseDto) obj;
		return Objects.equals(this.pricing, responseDto.pricing) && Objects.equals(this.shipments, responseDto.shipments)
				&& Objects.equals(this.track, responseDto.track);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pricing, shipments, track);
	}
}
