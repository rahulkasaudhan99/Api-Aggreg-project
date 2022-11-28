package com.tnt.apiAggregation.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tnt.apiAggregation.entity.ResponseEntity;
import com.tnt.apiAggregation.entity.ResponseId;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseEntity, ResponseId> {

	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO RESPONSE(uuid,req_type,req_param,created_on) VALUES(?1,?2,?3,?4) ")
	public int createRequest(UUID uuid, String reqType, String reqParam, Timestamp st);
	
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE RESPONSE SET res_param=?1 WHERE  RES_PARAM IS NULL AND req_type=?2 AND req_param=?3 ")
	public int saveRespose(String resParam, String reqType, String reqParam);
	
	@Query(nativeQuery = true, value = "SELECT * FROM RESPONSE WHERE uuid=?1 and req_param = ?2 and res_param is not null")
	public List<ResponseEntity> findResponse(UUID uuid, String reqParam);

	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM RESPONSE WHERE TIMESTAMPDIFF(MINUTE,CREATED_ON, CURRENT_TIMESTAMP())>10")
	public int deleteApiRespose();
	
}
