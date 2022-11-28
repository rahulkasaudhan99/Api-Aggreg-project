package com.tnt.apiAggregation.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tnt.apiAggregation.service.ApiResponseService;

@Service
public class InternalService{
	private static final Logger log = LoggerFactory.getLogger(InternalService.class);

	@Autowired
	private ApiResponseService apiResponseService;

	public void cleanResponses() {
		apiResponseService.deleteApiResponse();
		log.info("deleted response data");
	}

}
