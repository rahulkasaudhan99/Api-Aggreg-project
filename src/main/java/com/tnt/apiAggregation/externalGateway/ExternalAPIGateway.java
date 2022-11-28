package com.tnt.apiAggregation.externalGateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalAPIGateway {
	
	private static final Logger log = LoggerFactory.getLogger(ExternalAPIGateway.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${pricing.api.base.url}")
	private String pricing_api_url;
	
	@Value("${shipment.api.base.url}")
	private String shipment_api_url;
	
	@Value("${tracking.api.base.url}")
	private String tracking_api_url;
	
	public Map<String, BigDecimal> getPricing(String pricingStr) throws Exception {
		log.info("Calling External pricingAPI: "+pricingStr);
		ParameterizedTypeReference<HashMap<String, BigDecimal>> responseType = new ParameterizedTypeReference<HashMap<String, BigDecimal>>() {};
		String pricingUri = pricing_api_url + pricingStr;
		RequestEntity<Void> request = RequestEntity.get(pricingUri).accept(MediaType.APPLICATION_JSON).build();
		Map<String, BigDecimal> response = restTemplate.exchange(request, responseType).getBody();
		log.info("Response received from pricing API: "+response);
		return response;

	}
	
	public Map<String, String> getTrackResponse(String trackingStr) throws Exception {
		log.info("Calling External trackingAPI: " + trackingStr);
		String trackingUri = tracking_api_url + trackingStr;
		ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() {};
		RequestEntity<Void> request = RequestEntity.get(trackingUri).accept(MediaType.APPLICATION_JSON).build();
		Map<String, String> response = restTemplate.exchange(request, responseType).getBody();
		log.info("Response received from pricing API: "+response);
		return response;
	}
	
	public Map<String, List<String>> getShipmentsResponse(String shipmentsStr) {
		log.info("Calling External shipmentAPI: "+shipmentsStr);
		ParameterizedTypeReference<HashMap<String, List<String>>> responseType = new ParameterizedTypeReference<HashMap<String, List<String>>>() {};
		String shipmentUri = shipment_api_url + shipmentsStr;
		RequestEntity<Void> request = RequestEntity.get(shipmentUri).accept(MediaType.APPLICATION_JSON).build();
		Map<String, List<String>> response = restTemplate.exchange(request, responseType).getBody();
		log.info("Response received from pricing API: "+response);
		return response;
	}

}
