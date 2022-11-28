package com.tnt.apiAggregation.service;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

import com.tnt.apiAggregation.dto.PricingRequestDto;
import com.tnt.apiAggregation.dto.ResponseDto;
import com.tnt.apiAggregation.dto.ShipmentRequestDto;
import com.tnt.apiAggregation.dto.TrackingRequestDto;

@Service
public interface AggregationService {

	public ResponseDto buidAggRes(PricingRequestDto pricing, TrackingRequestDto track, ShipmentRequestDto shipments)
			throws InterruptedException, ExecutionException;
	
	public Set<String> generateSet(String reqStr);
	
}
