/**
 * 
 */
package com.hitech.dms.web.model.jobcard.search.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class PdiCheckListResponse {
	private BigInteger pdiDtlId;
	private BigInteger checkpointId;
	private String activeStatus;
	private String checkpointDesc;
	private String defaultTick;
	private String fieldType;
	private String specification;
	private BigInteger aggregateId;
	private String aggregate;
	private Integer aggregateSequenceNo;
	private Integer checkpointSequenceNo;
	private String remark;
	private String observedSpecification;
}
