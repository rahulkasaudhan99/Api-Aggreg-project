package com.tnt.apiAggregation.endpoint;

import java.math.BigDecimal;
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

import com.tnt.apiAggregation.dto.PricingRequestDto;
import com.tnt.apiAggregation.externalGateway.ExternalAPIGateway;
import com.tnt.apiAggregation.service.ApiResponseService;
import com.tnt.apiAggregation.service.AggregationService;
import com.tnt.apiAggregation.utility.Constant;

@Component
public class PricingMessageProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(PricingMessageProcessor.class);

	@Autowired
	ExternalAPIGateway pricingGateway;
	@Autowired
	private ApiResponseService apiResponseRepo;
	@Autowired
	AggregationService aggService;

	@Override
	public void process(Exchange exchange) throws Exception {
		@SuppressWarnings("unchecked")
		List<Exchange> exchng = (List<Exchange>) exchange.getIn().getBody();
		log.info("pricing message reached in processor : "+exchng.size());
		Set<String> set = new HashSet<String>();
		PricingRequestDto pricingRequest = null;
		for (Exchange ex : exchng) {
			pricingRequest = ex.getIn().getBody(PricingRequestDto.class);
			for (String str : pricingRequest.getRequestStr()) {
				log.info(pricingRequest.getUuid() + "  " + "PRICING" + "  " + str);
				apiResponseRepo.createRequest(pricingRequest.getUuid(), "PRICING", str);
				set.add(str);
			}
		}
		String reqStr = null;
		for(String s:set) {
			if (null != reqStr) reqStr = reqStr + "," + s;
			else reqStr = s;	
		}
		log.info("Only Unique Messages taken from queue : " + reqStr);
		Map<String, BigDecimal> resMap = null;
		try {
			resMap = pricingGateway.getPricing(reqStr);
		}catch (Exception e) {
			log.info("Throws error on calling external pricingApi");
			resMap = new HashMap<String, BigDecimal>();
			for (String str : pricingRequest.getRequestStr()) {
					log.info("Error :" + reqStr);
					resMap.put(str, new BigDecimal(Constant.PRICING_API_ERROR));
			}
		}
		apiResponseRepo.savePricingResponse("PRICING", resMap);
	}

}
