/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.search.request;

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
public class PrForInvoiceSearchRequestModel {
	private Integer pcId;
	private BigInteger orgHierID;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String grnNo;
	private String purchaseReturnInvNo;
	private String purchaseReturnNo;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private Integer grnTypeId;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private int page;
	private int size;
}
