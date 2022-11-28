package com.tnt.apiAggregation.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tnt.apiAggregation.dto.PricingRequestDto;
import com.tnt.apiAggregation.dto.ResponseDto;
import com.tnt.apiAggregation.dto.ShipmentRequestDto;
import com.tnt.apiAggregation.dto.TrackingRequestDto;
import com.tnt.apiAggregation.service.AggregationService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-01T22:33:25.097Z[GMT]")
@RestController
@Slf4j
public class AggregationController {

	private static final Logger log = LoggerFactory.getLogger(AggregationController.class);

	@Autowired
	AggregationService aggregationService;
	@Autowired
	ProducerTemplate producerTemplate;

	@Value("${pricing-request.queue}")
	private String pricing_req_queue;

	@Value("${tracking-request.queue}")
	private String tracking_req_queue;

	@Value("${shipment-request.queue}")
	private String shipments_rew_queue;

	@GetMapping(value = "/aggregation", produces = { "application/json" })
	public ResponseEntity<ResponseDto> aggregationGet(
			@NotNull @Parameter(schema = @Schema(), in = ParameterIn.QUERY, description = "", required = false) @Valid @RequestParam(value = "pricing", required = true) String pricing,
			@NotNull @Parameter(schema = @Schema(), in = ParameterIn.QUERY, description = "", required = false) @Valid @RequestParam(value = "track", required = true) String track,
			@NotNull @Parameter(schema = @Schema(), in = ParameterIn.QUERY, description = "", required = false) @Valid @RequestParam(value = "shipments", required = true) String shipments) {

		try {
			ResponseDto response = new ResponseDto();
			Set<String> pricingSet = aggregationService.generateSet(pricing);
			Set<String> trackSet = aggregationService.generateSet(track);
			Set<String> shipmentSet = aggregationService.generateSet(shipments);
			UUID pricingUUID = UUID.randomUUID();
			UUID trackUUID = UUID.randomUUID();
			UUID shipmentUUID = UUID.randomUUID();
			PricingRequestDto pricingRequest = new PricingRequestDto(pricingUUID, pricingSet);
			TrackingRequestDto trackingRequest = new TrackingRequestDto(trackUUID, trackSet);
			ShipmentRequestDto shipmentRequest = new ShipmentRequestDto(shipmentUUID, shipmentSet);
			
			//sending requests to Pricing Queue
			for(String prc : pricingSet) {
				Set<String> prcSet = new HashSet<String>();
				prcSet.add(prc);
				PricingRequestDto pricingReq=new PricingRequestDto(pricingUUID, prcSet);
				producerTemplate.sendBody("jms:" + pricing_req_queue, pricingReq);
				log.info("Pricing req msg : "+pricingReq.getUuid()+pricingReq.getRequestStr());
			}
			//sending requests to Track Queue
			for(String trck : trackSet) {
				Set<String> trckSet = new HashSet<String>();
				trckSet.add(trck);
				TrackingRequestDto trackingReq=new TrackingRequestDto(trackUUID, trckSet);
				producerTemplate.sendBody("jms:" + tracking_req_queue, trackingReq);
				log.info("tracking req msg : "+trackingReq.getUuid()+trackingReq.getRequestStr());
			}
			//sending requests to Shipment Queue
			for(String ship : shipmentSet) {
				Set<String> shipSet = new HashSet<String>();
				shipSet.add(ship);
				ShipmentRequestDto shipmentReq=new ShipmentRequestDto(shipmentUUID, shipSet);
				producerTemplate.sendBody("jms:" + shipments_rew_queue, shipmentReq);
				log.info("shipment req msg : "+shipmentReq.getUuid()+shipmentReq.getRequestStr());
			}
			
			log.info("message sent to queue");
			response = aggregationService.buidAggRes(pricingRequest, trackingRequest, shipmentRequest);
			return new ResponseEntity<ResponseDto>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Response can't be serialized for type application/json", e);
			return new ResponseEntity<ResponseDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
