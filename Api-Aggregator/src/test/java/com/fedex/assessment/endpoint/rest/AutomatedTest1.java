package com.fedex.assessment.endpoint.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import com.tnt.apiAggregation.controller.AggregationController;

import org.hamcrest.Matchers;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AutomatedTest1 {

	private static final Logger log = LoggerFactory.getLogger(AutomatedTest1.class);
	@SuppressWarnings("unused")
	private MockMvc mockMvc;
	@InjectMocks
	private AggregationController aggController;

	//scenario 1 - Request with only 2 reqParams
	@Test
	public void automatedTest() {
		long startTime = System.currentTimeMillis();	
		
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("http://localhost:8080/aggregation?pricing=CR,RN&track=123456871,123456872&shipments=123456872,123456873");
		String res = response.asString();
		ValidatableResponse validatableResp = response.then();
		
		//verify 5000ms < responseTime < 10000 milliseconds or 10 seconds (queueTimeout >=5 seconds cap not full)
		validatableResp.time(Matchers.lessThan(10000L));
		
	    long responseTime = (System.currentTimeMillis() - startTime);
	    log.info("Test1 response"+res);
	    log.info("Test1 Response Time in secs: "+(double)responseTime/1000);
	}
	
}

