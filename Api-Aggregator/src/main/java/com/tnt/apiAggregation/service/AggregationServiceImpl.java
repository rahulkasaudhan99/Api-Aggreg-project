package com.tnt.apiAggregation.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tnt.apiAggregation.dto.PricingRequestDto;
import com.tnt.apiAggregation.dto.ResponseDto;
import com.tnt.apiAggregation.dto.ShipmentRequestDto;
import com.tnt.apiAggregation.dto.TrackingRequestDto;
import com.tnt.apiAggregation.entity.ResponseEntity;
import com.tnt.apiAggregation.utility.Constant;

@Service
@Async
public class AggregationServiceImpl implements AggregationService{
	private static final Logger log = LoggerFactory.getLogger(AggregationServiceImpl.class);
	@Autowired
	private ApiResponseService apiResponseService;

	public ResponseDto buidAggRes(PricingRequestDto pricing, TrackingRequestDto track, ShipmentRequestDto shipments)
			throws InterruptedException, ExecutionException {
		
		ResponseDto aggregatedResponse = new ResponseDto();
		CompletableFuture<Map<String, BigDecimal>> pricingResult = generatePricingResp(pricing);
		CompletableFuture<Map<String, String>> trackingResult = generateTrackingResp(track);
		CompletableFuture<Map<String, List<String>>> shipmentResult = generateShipmentResp(shipments);
		
		log.info("All Tasks completed now started Aggregation!!");
		CompletableFuture.allOf(trackingResult, shipmentResult, pricingResult).join();
		
		log.info("All Tasks responses Aggregated!!");
		aggregatedResponse.setPricing(pricingResult.get());
		aggregatedResponse.setTrack(trackingResult.get());
		aggregatedResponse.setShipments(shipmentResult.get());
		
		return aggregatedResponse;
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, BigDecimal>> generatePricingResp(PricingRequestDto pricingRequest)
			throws InterruptedException {
		log.info("Task for generating pricing response Started: " + pricingRequest);
		Map<String, BigDecimal> responseMap = new HashMap<String, BigDecimal>();
		Set<String> pricingSet = pricingRequest.getRequestStr();
		ResponseEntity responseData = null;
		//while (pricingSet.size() != responseMap.size()) {
		while (pricingSet.size() != responseMap.size()) {
			for (String prc : pricingSet) {
				if (!responseMap.containsKey(prc)) responseData = apiResponseService.getResponse(pricingRequest.getUuid(), prc);
				if (null != responseData) {
					BigDecimal respData = new BigDecimal(responseData.getResParam());
					BigDecimal errorValue = new BigDecimal(Constant.PRICING_API_ERROR);
					BigDecimal value=respData.compareTo(errorValue) == 0 ? null : respData;
					responseMap.put(responseData.getReqParam(),value);
				}
			}
		}
		log.info("Task for generating pricing response Completed!!");
		return CompletableFuture.completedFuture(responseMap);
	}
	
	

	@Async("taskExecutor")
	private CompletableFuture<Map<String, String>> generateTrackingResp(TrackingRequestDto trackingRequest)
			throws InterruptedException {
		log.info("Task for generating Tracking response Started: " + trackingRequest);
		Map<String, String> responseMap = new HashMap<String, String>();
		Set<String> trackingSet = trackingRequest.getRequestStr();
		ResponseEntity responseData = null;
		while (trackingSet.size() != responseMap.size()) {
			for (String trck : trackingSet) {
				if (!responseMap.containsKey(trck))  responseData = apiResponseService.getResponse(trackingRequest.getUuid(), trck);
				if (null != responseData) {
					if (responseData.getResParam().equals(Constant.APR_ERROR)) responseMap.put(responseData.getReqParam(), null);
					else responseMap.put(responseData.getReqParam(), responseData.getResParam());
				}
			}
		}
		log.info("Task for generating Tracking response Completed!!");
		return CompletableFuture.completedFuture(responseMap);
	}
	
	

	@Async("taskExecutor")
	private CompletableFuture<Map<String, List<String>>> generateShipmentResp(ShipmentRequestDto shipmentRequest)
			throws InterruptedException {
		log.info("Task for generating shipment response Sarted: " + shipmentRequest);
		Map<String, List<String>> responseMap = new HashMap<String, List<String>>();
		Set<String> shipmentSet = shipmentRequest.getRequestStr();

		ResponseEntity responseData = null;
		while (shipmentSet.size() != responseMap.size()) {
			for (String ship : shipmentSet) {
				if (!responseMap.containsKey(ship)) responseData = apiResponseService.getResponse(shipmentRequest.getUuid(), ship);
				if (null != responseData) {
					if (responseData.getResParam().equals(Constant.APR_ERROR)) responseMap.put(responseData.getReqParam(), null);
					else responseMap.put(responseData.getReqParam(), Arrays.asList(responseData.getResParam().split(",")));
				}
			}
		}
		log.info("Task for generating shipment response Completed");
		return CompletableFuture.completedFuture(responseMap);
	}
	
	public Set<String> generateSet(String reqStr) {
		Set<String> set = new HashSet<String>();
		String[] strArray = reqStr.split(",");
		for (String str : strArray) {
			set.add(str);
		}
		return set;
	}

}
