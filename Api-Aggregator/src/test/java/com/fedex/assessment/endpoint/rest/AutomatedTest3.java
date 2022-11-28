package com.fedex.assessment.endpoint.rest;

import java.util.concurrent.CountDownLatch;

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

class AutomatedTest3 {

	private static final Logger log = LoggerFactory.getLogger(AutomatedTest3.class);
	@SuppressWarnings("unused")
	private MockMvc mockMvc;
	@InjectMocks
	private AggregationController aggController;

	//scenario 3 - Two client 1st requests with 3 reqParams and 2nd requests with 2 reqParams
	//verify responseTime < 5000ms (queueBatchSize>=5  queue cap full)
	
	@Test
	public void automatedTest() throws InterruptedException {
		
		CountDownLatch latch = new CountDownLatch(2);
		new Thread(new Runnable() {
			public void run() {
				long startTime = System.currentTimeMillis();
				
				RequestSpecification httpRequest1 = RestAssured.given();
				Response response1 = httpRequest1.get("http://localhost:8080/aggregation?pricing=PP,PQ,PR&track=123456891,109347292,109347293&shipments=123456891,109347292,109347293");
				String res1 = response1.asString();
				ValidatableResponse validatableResp1 = response1.then();
				validatableResp1.time(Matchers.lessThan(5000L));
				
				log.info("Test3 response1"+res1);
				long responseTime = (System.currentTimeMillis() - startTime);
			    log.info("Test3 Response Time in secs: "+(double)responseTime/1000);
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				long startTime = System.currentTimeMillis();
				
				RequestSpecification httpRequest2 = RestAssured.given();
				Response response2 = httpRequest2.get("http://localhost:8080/aggregation?pricing=RN,BN&track=109347263,109347583&shipments=109347263,109347592");
				String res2 = response2.asString();
				ValidatableResponse validatableResp2 = response2.then();
				validatableResp2.time(Matchers.lessThan(5000L));
				
				log.info("Test3 response2"+res2);
				long responseTime = (System.currentTimeMillis() - startTime);
			    log.info("Test3 Response Time in secs: "+(double)responseTime/1000);
			}
		}).start();
		
	    
		
		latch.await();
		
	    
	}

}
