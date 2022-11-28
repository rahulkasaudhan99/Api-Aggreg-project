package com.tnt.apiAggregation.endpoint;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageRouter extends RouteBuilder {

	@Value("${pricing-request.queue}")
	private String pricing_req_queue;

	@Value("${tracking-request.queue}")
	private String tracking_req_queue;

	@Value("${shipment-request.queue}")
	private String shipments_req_queue;
	
	@Value("${request.batch.size}")
	private String requests_batch_size;
	
	@Value("${request.batch.copletion.time}")
	private String requests_batch_completion_time;

	@Autowired
	PricingMessageProcessor pricingMsgProcessor;
	
	@Autowired
	TrackingMessageProcessor trackingMsgProcessor;
	
	@Autowired
	ShipmentMessageProcessor shipmentMsgProcessor;

	
	@Override
	public void configure() throws Exception {
		from("jms:" + pricing_req_queue).aggregate(new GroupedExchangeAggregationStrategy()).constant(true)
				.completionSize(requests_batch_size).completionTimeout(requests_batch_completion_time).forceCompletionOnStop().process(pricingMsgProcessor);

		from("jms:" + tracking_req_queue).aggregate(new GroupedExchangeAggregationStrategy()).constant(true)
				.completionSize(requests_batch_size).completionTimeout(requests_batch_completion_time).forceCompletionOnStop().process(trackingMsgProcessor);

		from("jms:" + shipments_req_queue).aggregate(new GroupedExchangeAggregationStrategy()).constant(true)
				.completionSize(requests_batch_size).completionTimeout(requests_batch_completion_time).forceCompletionOnStop().process(shipmentMsgProcessor);

	}

}
