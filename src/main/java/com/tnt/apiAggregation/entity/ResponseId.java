
package com.tnt.apiAggregation.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;

public class ResponseId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "uuid")
	private UUID uuid;

	@Column(name = "req_param")
	private String reqParam;

}
