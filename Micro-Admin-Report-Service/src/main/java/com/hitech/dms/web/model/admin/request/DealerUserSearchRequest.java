/**
 * 
 */
package com.hitech.dms.web.model.admin.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class DealerUserSearchRequest {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String empName;
	private String empCode;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private BigInteger orgHierId;
	private Integer page;
	private Integer size;
	private String status;

}
