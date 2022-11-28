package com.tnt.apiAggregation.endpoint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tnt.apiAggregation.dto.TrackingRequestDto;
import com.tnt.apiAggregation.externalGateway.ExternalAPIGateway;
import com.tnt.apiAggregation.service.ApiResponseService;
import com.tnt.apiAggregation.service.AggregationService;
import com.tnt.apiAggregation.utility.Constant;

@Component
public class TrackingMessageProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(TrackingMessageProcessor.class);

	@Autowired
	ExternalAPIGateway trackingGateway;
	@Autowired
	private ApiResponseService apiResponseService;
	@Autowired
	AggregationService aggService;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		List<Exchange> exchng = (List<Exchange>) exchange.getIn().getBody();
		log.info("tracking message reached in processor : "+exchng.size());
		Set<String> set = new HashSet<String>();
		TrackingRequestDto trackingRequest = null;
		for (Exchange ex : exchng) {
			trackingRequest = ex.getIn().getBody(TrackingRequestDto.class);
			for (String str : trackingRequest.getRequestStr()) {
				log.info(trackingRequest.getUuid() + "  " + "TRACKING" + "  " + str);
				apiResponseService.createRequest(trackingRequest.getUuid(),"TRACKING", str);
				set.add(str);
			}
		}
		String reqStr=null;
		for(String s:set) {
			if (null != reqStr) reqStr = reqStr + "," + s;
			else reqStr = s;
		}
		log.info("Only Unique Messages taken from queue : " + reqStr);
		Map<String, String> resMap = null;
		try {
			resMap = trackingGateway.getTrackResponse(reqStr);
		}catch (Exception e) {
			log.info("Throws error on calling external trackingApi");
			resMap = new HashMap<String, String>();
			for (String str : trackingRequest.getRequestStr()) {
					log.info("Error : " + str);
					resMap.put(str, Constant.APR_ERROR);
			}
		}
		apiResponseService.saveTrackingResponse("TRACKING", resMap);

	}

}
