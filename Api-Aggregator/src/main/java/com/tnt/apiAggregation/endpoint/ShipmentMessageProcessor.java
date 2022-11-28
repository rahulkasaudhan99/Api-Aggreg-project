package com.tnt.apiAggregation.endpoint;

import java.util.Arrays;
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

import com.tnt.apiAggregation.dto.ShipmentRequestDto;
import com.tnt.apiAggregation.externalGateway.ExternalAPIGateway;
import com.tnt.apiAggregation.service.ApiResponseService;
import com.tnt.apiAggregation.service.AggregationService;
import com.tnt.apiAggregation.utility.Constant;

@Component
public class ShipmentMessageProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(ShipmentMessageProcessor.class);

	@Autowired
	ExternalAPIGateway shipmentGateway;
	@Autowired
	private ApiResponseService apiResponseService;
	@Autowired
	AggregationService aggService;
	
	@Override
	public void process(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		List<Exchange> exchng = (List<Exchange>) exchange.getIn().getBody();
		log.info("shipment message reached in processor : "+exchng.size());
		ShipmentRequestDto shipmentRequest = null;
		Set<String> set = new HashSet<String>();
		for (Exchange ex : exchng) {
			shipmentRequest = ex.getIn().getBody(ShipmentRequestDto.class);
			for (String str : shipmentRequest.getRequestStr()) {
				log.info(shipmentRequest.getUuid() + "  " + "SHIPMENT" + "  " + str);
				apiResponseService.createRequest(shipmentRequest.getUuid(), "SHIPMENT", str);
				set.add(str);
			}
		}
		String reqStr = null;
		for(String s:set) {
			if (null != reqStr) reqStr = reqStr + "," + s;
			else reqStr = s;
		}
		log.info("Only Unique Messages taken from queue : " + reqStr);
		Map<String, List<String>> resMap = null;
		try {
			resMap = shipmentGateway.getShipmentsResponse(reqStr);
		} catch (Exception e) {
			log.info("Throws error on calling external shipmentApi");
			resMap = new HashMap<String, List<String>>();
			for (String str : shipmentRequest.getRequestStr()) {
				{
					log.info("Error :" + reqStr);
					resMap.put(str, Arrays.asList(Constant.APR_ERROR));
				}
			}
		}
		apiResponseService.saveShipmentResponse("SHIPMENT", resMap);
	}
	
}
