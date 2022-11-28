package com.fedex.assessment.endpoint.rest;

import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;

import com.tnt.apiAggregation.controller.AggregationController;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

class AutomatedTest2 {

	private static final Logger log = LoggerFactory.getLogger(AutomatedTest2.class);
	@SuppressWarnings("unused")
	private MockMvc mockMvc;
	@InjectMocks
	private AggregationController aggController;

	//scenario 2 - Requests with 5 reqParams
	@Test
	public void automatedTest() {
		long startTime = System.currentTimeMillis();
		
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("http://localhost:8080/aggregation?pricing=AR,BR,PN,QN,SN&track=123456881,109347282,109347262,109347283,109347284&shipments=123456881,109347282,109347262,109347283,109347284");
		String res = response.asString();
		ValidatableResponse validatableResp = response.then();
		
		//verify responseTime < 5000 milliseconds or 5 seconds (queueBatchSize>=5 cap full)
		validatableResp.time(Matchers.lessThan(5000L));
		
	    long responseTime = (System.currentTimeMillis() - startTime);
	    log.info("Test2 response"+res);
	    log.info("Test2 Response Time in secs: "+(double)responseTime/1000);
	}

}
