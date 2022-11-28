package com.tnt.apiAggregation.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.apiAggregation.entity.ResponseEntity;
import com.tnt.apiAggregation.repository.ResponseRepository;

@Service
public class ApiResponseService {
	private static final Logger log = LoggerFactory.getLogger(ApiResponseService.class);
	@Autowired
	ResponseRepository repo;
	
	@Transactional
	public void createRequest(UUID uuid,String reqType, String requestParam) {
		log.info("Storing request in DB [ uuid: "+uuid+" reqType: "+reqType+" requestParam: "+requestParam+"]");
		repo.createRequest(uuid,reqType,requestParam,Timestamp.valueOf(LocalDateTime.now()));
		log.info("response saved to DB for "+reqType);
	}

	@Transactional
	public void savePricingResponse(String resType, Map<String, BigDecimal> responseMap) {
		responseMap.forEach((key, value) -> repo.saveRespose(value.toString(), resType, key.trim()));
		log.info("Response saved to DB for "+resType);
	}

	@Transactional
	public void saveTrackingResponse(String resType, Map<String, String> responseMap) {
		responseMap.forEach((key, value) -> repo.saveRespose(value, resType, key.trim()));
		log.info("Response saved to DB for "+resType);
	}

	@Transactional
	public void saveShipmentResponse(String resType, Map<String, List<String>> responseMap) {
		responseMap.forEach((key, value) -> repo.saveRespose(
				value.stream().map(Object::toString).collect(Collectors.joining(",")), resType, key.trim()));
		log.info("Response saved to DB for "+resType);
	}

	public ResponseEntity getResponse(UUID uuid, String reqParam) {
		List<ResponseEntity> res = repo.findResponse(uuid, reqParam.trim());
		if (null != res && res.size() > 0) {
			return res.get(0);
		}
		return null;
	}

	@Transactional
	public void deleteApiResponse() {
		repo.deleteApiRespose();
		log.info("All Responses deleted from database");

	}

}
