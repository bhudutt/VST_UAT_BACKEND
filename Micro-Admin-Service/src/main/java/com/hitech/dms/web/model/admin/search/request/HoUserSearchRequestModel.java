/**
 * 
 */
package com.hitech.dms.web.model.admin.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserSearchRequestModel {
	private Integer pcId;
	private BigInteger orgHierId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String empName;
	private String empCode;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private Integer page;
	private Integer size;
}
